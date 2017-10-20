package com.wyamee.nlp;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class ModelBasedNLPFactory {

	private static String TOKENIZER_MODEL_PATH = "nlp/models/en-token.bin";
	private static String NE_PERSON_MODEL_PATH = "nlp/models/en-ner-person.bin";
	private static String NE_LOCATION_MODEL_PATH = "nlp/models/en-ner-location.bin";
	private static String NE_ORGANISATION_MODEL_PATH = "nlp/models/en-ner-organization.bin";
	
	private static Tokenizer tokenizer = null;
	private static NameFinderME personFinder = null;
	private static NameFinderME locationFinder = null;
	private static NameFinderME organisationFinder = null;
	
	public static Tokenizer createTokenizer() throws IOException {
		if (tokenizer == null) {
			tokenizer = SimpleTokenizer.INSTANCE;
		}
		return tokenizer;
	}
	
	public static NameFinderME createPersonEntityFinder() {
		if (personFinder == null) {
			personFinder = loadNamedEntityModel(NE_PERSON_MODEL_PATH);
		}
		return personFinder;
	}
	
	public static NameFinderME createLocationEntityFinder() {
		if (locationFinder == null) {
			locationFinder = loadNamedEntityModel(NE_LOCATION_MODEL_PATH);
		}
		return locationFinder;
	}
	
	public static NameFinderME createOrganisationEntityFinder() {
		if (organisationFinder == null) {
			organisationFinder = loadNamedEntityModel(NE_ORGANISATION_MODEL_PATH);
		}
		return organisationFinder;
	}
	
	@SuppressWarnings("unused")
	private static Tokenizer loadMaxEntropyTokenizer() throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream resourceStream = loader.getResourceAsStream(TOKENIZER_MODEL_PATH);
		TokenizerModel model = new TokenizerModel(resourceStream);
		return new TokenizerME(model);
	}
	
	private static NameFinderME loadNamedEntityModel(String modelPath) {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream resourceStream = loader.getResourceAsStream(modelPath);
			TokenNameFinderModel model = new TokenNameFinderModel(resourceStream);
			return new NameFinderME(model);
		}
		catch (IOException e) {
			throw new NLPModelLoadingException(e);
		}
	}
}
