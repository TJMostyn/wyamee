package com.wyamee.question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.wyamee.data.NewsArticle;
import com.wyamee.nlp.ModelBasedNLPFactory;
import com.wyamee.nlp.NLPModelLoadingException;
import com.wyamee.nlp.NamedEntityExtractor;
import com.wyamee.nlp.PersonalPrepositionTokenFilter;
import com.wyamee.nlp.YearExtractor;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.tokenize.Tokenizer;

public class SimpleQuestionExtractor implements IQuestionExtractor {
	
	private enum QuestionType {
		PERSON,
		YEAR,
		LOCATION_OR_ORGANISATION
	}
	
	private static final String ANSWER_PLACEHOLDER = "_";
	private static final Pattern HTML_PATTERN = Pattern.compile("<[a-zA-Z0-9\\w\\s/]+>");
	private NamedEntityExtractor neExtractor;
	private YearExtractor yearExtractor;
	private Tokenizer tokenizer;
	private SentenceDetector sentenceTokenizer;
	private PersonalPrepositionTokenFilter stopwordFilter;
	
	public SimpleQuestionExtractor() {
		neExtractor = new NamedEntityExtractor();
		yearExtractor = new YearExtractor();
		stopwordFilter = new PersonalPrepositionTokenFilter();
		try {
			tokenizer = ModelBasedNLPFactory.createTokenizer();
			sentenceTokenizer = ModelBasedNLPFactory.createSentenceTokenizer();
		}
		catch (IOException e) {
			throw new NLPModelLoadingException(e);
		}
	}
	
	@Override
	public List<ArticleQuestion> extractQuestions(NewsArticle article, int maxQuestions) {
		
		String content = article.getArticle().getDataContent().getBody();
		content = HTML_PATTERN.matcher(content).replaceAll(" ");
		content = content.replaceAll("\n|\r", ". ").
			replaceAll(" \\. ", "").
			replaceAll("^\\. ", "").
			replaceAll("\\.+ ", ". ");
		
		List<ArticleQuestion> questions = new ArrayList<>();
		for (String sentence : sentenceTokenizer.sentDetect(content)) {

			List<String> people = neExtractor.extractPeople(sentence);
			List<String> locations = neExtractor.extractLocations(sentence);
			List<String> organisations = neExtractor.extractOrganisations(sentence);
			List<String> years = yearExtractor.extractYears(sentence);
			String[] tokens = tokenizer.tokenize(sentence);
			
			ArticleQuestion question = null;
			if (years.size() > 0) {
				question = new ArticleQuestion(
					((Long) article.getArticle().getDescriptiveMetaData().getNlaArticleId()).toString(), 
					formatQuestion(sentence, years.get(0), QuestionType.YEAR),
					years.get(0));
			}
			else if (locations.size() > 0) {
				String location = locations.get(0);
				String matchLocation = (location.split(" ").length > 0) ? location.split(" ")[0] : location;
				for (int i = 0; i < tokens.length; i++) {
					if (i > 0 && tokens[i].equalsIgnoreCase(matchLocation)) {
						if (people.size() > 0 || organisations.size() > 0) {
							question = new ArticleQuestion(
								((Long) article.getArticle().getDescriptiveMetaData().getNlaArticleId()).toString(), 
								formatQuestion(sentence, locations.get(0), QuestionType.LOCATION_OR_ORGANISATION),
								locations.get(0));
						}
					}
				}
			}
			else if (people.size() > 0 && people.get(0).contains(" ")) {
				question = new ArticleQuestion(
					((Long) article.getArticle().getDescriptiveMetaData().getNlaArticleId()).toString(), 
					formatQuestion(sentence, people.get(0), QuestionType.PERSON),
					people.get(0));
			}
			
			if (question != null) {
				// Answer not correctly replaced
				if (! question.getQuestion().contains(ANSWER_PLACEHOLDER))
					continue;
				
				// Not enough info (shouldn't start with unknown)
				if (stopwordFilter.isFiltered(tokens[0]))
					continue;
				
				// Too short to be interesting
				if (question.getQuestion().length() < 50)
					continue;

				questions.add(question);
			}
		}
		
		if (questions.size() > maxQuestions) {
			questions = questions.subList(0, maxQuestions);
		}
		
		return questions;
	}
	
	private String formatQuestion(String sentence, String answer, QuestionType type) {
		StringBuilder placeholder = new StringBuilder();
		for (int i = 0; i < answer.length(); i++) {
			placeholder.append(ANSWER_PLACEHOLDER);
		}
		return sentence.replace(answer, placeholder.toString()) + " (" + type.name() + ")";
	}
}
