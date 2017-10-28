package com.wyamee.nlp;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;

public class ModelBasedNLPFactory {

	private static String TOKENIZER_SENTENCE_MODEL_PATH = "nlp/models/en-sent.bin";
	private static String NE_PERSON_MODEL_PATH = "nlp/models/en-ner-person.bin";
	private static String NE_LOCATION_MODEL_PATH = "nlp/models/en-ner-location.bin";
	private static String NE_ORGANISATION_MODEL_PATH = "nlp/models/en-ner-organization.bin";

	private static Tokenizer tokenizer = null;
	private static SentenceDetector sentenceTokenizer = null;
	private static NameFinderME personFinder = null;
	private static NameFinderME locationFinder = null;
	private static NameFinderME organisationFinder = null;
	
	public static Tokenizer createTokenizer() throws IOException {
		if (tokenizer == null) {
			tokenizer = SimpleTokenizer.INSTANCE;
		}
		return tokenizer;
	}
	
	public static SentenceDetector createSentenceTokenizer() throws IOException {
		if (sentenceTokenizer == null) {
			sentenceTokenizer = loadSentenceModel(TOKENIZER_SENTENCE_MODEL_PATH);
		}
		return sentenceTokenizer;
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
	
	private static SentenceDetectorME loadSentenceModel(String modelName) throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream resourceStream = loader.getResourceAsStream(modelName);
		SentenceModel model = new SentenceModel(resourceStream);
		return new SentenceDetectorME(model);
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
