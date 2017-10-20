package com.wyamee.nlp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

public class NamedEntityExtractor {

	private NameFinderME personFinder;
	private NameFinderME locationFinder;
	private NameFinderME organisationFinder;
	private Tokenizer tokenizer;
	
	public NamedEntityExtractor() {
		personFinder = ModelBasedNLPFactory.createPersonEntityFinder();
		locationFinder = ModelBasedNLPFactory.createLocationEntityFinder();
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
		return extractEntities(content, locationFinder);
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
}
