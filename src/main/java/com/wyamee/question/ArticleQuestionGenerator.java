package com.wyamee.question;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.wyamee.data.NewsArticle;
import com.wyamee.data.loader.NewsArticleLoader;
import com.wyamee.data.loader.NewsArticleLoaderException;

public class ArticleQuestionGenerator {

	private static final Logger LOG = Logger.getLogger(ArticleQuestionGenerator.class.getName());
	private String articleDirectory;
	private IQuestionExtractor questionExtractor;
	private int noQuestionsPerArticle;
	
	public ArticleQuestionGenerator(
		String articleDirectory, IQuestionExtractor questionExtractor, int noQuestionsPerArticle) {
		this.articleDirectory = articleDirectory;
		this.questionExtractor = questionExtractor;
		this.noQuestionsPerArticle = noQuestionsPerArticle;
	}
	
	public void generate() {
		generate(System.out, "stdout");
	}
	
	public void generate(String filePath) {
		File file = new File(filePath);
		try {
			if (! file.getParentFile().exists()) {
				throw new FileNotFoundException();
			}
			
			PrintStream printStream = new PrintStream(new FileOutputStream(file));
			generate(printStream, filePath);
		}
		catch (FileNotFoundException e) {
			LOG.severe("Unable to write to file " + filePath + ". Is it a valid path?");
		}
	}
	
	private void generate(PrintStream printStream, String outputName) {

		LOG.info("Getting all news articles from: " + articleDirectory);
		NewsArticleLoader articleLoader = new NewsArticleLoader(articleDirectory);
		
		LOG.info("Extracting questions for each article");
		List<String> failedArticles = new ArrayList<>();
		while (articleLoader.hasNext()) {
			try {
				NewsArticle article = articleLoader.next();
				List<ArticleQuestion> questions = questionExtractor.extractQuestions(
					article, noQuestionsPerArticle);
				String csvResult = toCommaSeparated(questions);
				printStream.print(csvResult);
			}
			catch (NewsArticleLoaderException e) {
				failedArticles.add(e.getFileName());
			}
		}
		
		LOG.warning(failedArticles.size() + " did not unmarshall correctly: " + failedArticles);
		LOG.info("Completed writing questions to " + outputName);
	}
	
	private String toCommaSeparated(List<ArticleQuestion> articleQuestions) {
		String delimiter = ",";
		StringBuilder builder = new StringBuilder();
		for (ArticleQuestion articleQuestion : articleQuestions) {
			builder.append(articleQuestion.getArticleId()).append(delimiter).
				append(articleQuestion.getQuestion()).append(delimiter).
				append(articleQuestion.getAnswer()).append("\n");
		}
		return builder.toString();
	}
}
