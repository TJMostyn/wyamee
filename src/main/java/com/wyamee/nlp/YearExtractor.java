package com.wyamee.nlp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import opennlp.tools.tokenize.Tokenizer;

public class YearExtractor {

	private static final String[] PREPOSITIONS = new String[] {
		"in",
		"during",
		"towards",
		"by",
		"from"
	};

	private static final Pattern YEAR_PATTERN = Pattern.compile("[0-9][0-9][0-9][0-9]");
	private Tokenizer tokenizer;
	
	public YearExtractor() {
		try {
			tokenizer = ModelBasedNLPFactory.createTokenizer();
		}
		catch (IOException e) {
			throw new NLPModelLoadingException(e);
		}
	}
	
	public List<String> extractYears(String text) {
		List<String> years = new ArrayList<>();
		String[] tokens = tokenizer.tokenize(text.trim());
		for (int i = 0; i < tokens.length; i++) {
			if (YEAR_PATTERN.matcher(tokens[i]).matches()) {
				if (i > 0 && isTimePreposition(tokens[i - 1]))
					years.add(tokens[i]);
			}
		}
		return years;
	}
	
	private boolean isTimePreposition(String potentialPreposition) {
		for (String preposition : PREPOSITIONS) {
			if (preposition.equalsIgnoreCase(potentialPreposition)) {
				return true;
			}
		}
		return false;
	}
}
