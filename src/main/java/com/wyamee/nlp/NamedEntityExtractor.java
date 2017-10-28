package com.wyamee.nlp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

public class NamedEntityExtractor {

	private static final String[] PREPOSITIONS = new String[] {
		"at",
		"in",
		"to",
		"towards",
		"from"
	};
	
	private NameFinderME personFinder;
	private NameFinderME organisationFinder;
	private Tokenizer tokenizer;
	
	public NamedEntityExtractor() {
		personFinder = ModelBasedNLPFactory.createPersonEntityFinder();
		organisationFinder = ModelBasedNLPFactory.createOrganisationEntityFinder();
		
		try {
			tokenizer = ModelBasedNLPFactory.createTokenizer();
		}
		catch (IOException e) {
			throw new NLPModelLoadingException(e);
		}
	}
	
	public List<String> extractPeople(String content) {
		return extractEntities(content, personFinder);
	}
	
	public List<String> extractLocations(String content) {
		List<String> locations = new ArrayList<>();
		String[] tokens = tokenizer.tokenize(content);
		outerLabel:
		for (int i = 0; i < tokens.length; i++) {
			if (isLocationPreposition(tokens[i])) {
				StringBuilder location = new StringBuilder();
				while (i + 1 < tokens.length) {
					i++;
					
					if (tokens[i].substring(0, 1).matches("[A-Z]")) {
						location.append(tokens[i] + " ");
					}
					else {
						break;
					}
				}
				if (location.length() > 0) {
					for (String person : extractPeople(content)) {
						if (person.equalsIgnoreCase(location.toString())) break outerLabel;
					}
					locations.add(location.toString().trim());
				}
			}
		}
		return locations;
	}
	
	public List<String> extractOrganisations(String content) {
		return extractEntities(content, organisationFinder);
	}
	
	private List<String> extractEntities(String content, NameFinderME entityFinder) {

		List<String> entities = new ArrayList<>();
		String[] tokens = tokenizer.tokenize(content);
		Span entitySpans[] = entityFinder.find(tokens); 
		for (Span span: entitySpans) { 
			StringBuilder builder = new StringBuilder();
			for (int i = span.getStart(); i < span.getEnd(); i++) {
				builder.append(tokens[i] + " ");
			}
			entities.add(builder.toString().trim());
		}
		return entities;
	}
	
	private boolean isLocationPreposition(String potentialPreposition) {
		for (String preposition : PREPOSITIONS) {
			if (preposition.equalsIgnoreCase(potentialPreposition)) {
				return true;
			}
		}
		return false;
	}
}
