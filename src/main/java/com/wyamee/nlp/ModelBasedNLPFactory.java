package com.wyamee.nlp;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class ModelBasedNLPFactory {

	private static String TOKENIZER_MODEL_PATH = "nlp/models/en-token.bin";
	private static Tokenizer tokenizer = null;
	
	public static Tokenizer createTokenizer() throws IOException {
		if (tokenizer == null) {
			tokenizer = SimpleTokenizer.INSTANCE;
		}
		return tokenizer;
	}
	
	@SuppressWarnings("unused")
	private Tokenizer loadMaxEntropyTokenizer() throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream resourceStream = loader.getResourceAsStream(TOKENIZER_MODEL_PATH);
		TokenizerModel model = new TokenizerModel(resourceStream);
		return new TokenizerME(model);
	}
}
