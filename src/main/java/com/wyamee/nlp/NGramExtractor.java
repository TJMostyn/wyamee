package com.wyamee.nlp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.wyamee.utils.MutableInt;

import opennlp.tools.tokenize.Tokenizer;

public class NGramExtractor {

	private Tokenizer tokenizer;
	private NonAlphabeticTokenFilter punctuationFilter;
	private StopwordTokenFilter stopwordFilter;
	
	public NGramExtractor() {
		try {
			tokenizer = ModelBasedNLPFactory.createTokenizer();
			stopwordFilter = StopwordTokenFilter.getInstance();
			punctuationFilter = new NonAlphabeticTokenFilter();
		}
		catch (IOException e) {
			throw new NLPModelLoadingException(e);
		}
	}
	
	public Map<String, MutableInt> getTermFrequencies(String content) {

		Map<String, MutableInt> instanceCounts = new HashMap<>();
		String[] tokens = tokenizer.tokenize(content.toLowerCase());
		
		boolean lastPunctuation = true;
		boolean lastStopword = true;
		for (int i = 0; i < tokens.length; i++) {
			boolean isPunctuation = punctuationFilter.isFiltered(tokens[i]);
			boolean isStopword = stopwordFilter.isFiltered(tokens[i]);
			
			if (! isPunctuation) {
				MutableInt.incrementMap(instanceCounts, tokens[i]);
				
				if (!isStopword && !lastPunctuation && !lastStopword) {
					String bigram = tokens[i - 1] + " " + tokens[i];
					MutableInt.incrementMap(instanceCounts, bigram);
				}
			}

			lastPunctuation = isPunctuation;
			lastStopword = isStopword;
		}
		
		return instanceCounts;
	}
}
