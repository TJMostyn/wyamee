package com.wyamee.nlp;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class StopwordTokenFilter implements ITokenFilter {

	private static String STOPWORD_LIST_PATH = "nlp/lists/stopwords.txt";
	private static StopwordTokenFilter instance = null;
	private Set<String> stopwords;
	
	private StopwordTokenFilter() {
		
		stopwords = new HashSet<String>();
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(STOPWORD_LIST_PATH).getFile());

		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String stopword = scanner.nextLine();
				stopwords.add(stopword);
			}

			scanner.close();
		} 
		catch (IOException e) {
			throw new NLPModelLoadingException(e);
		}
	}
	
	@Override
	public boolean isFiltered(String token) {
		return stopwords.contains(token.toLowerCase());
	}

	public static synchronized StopwordTokenFilter getInstance() {
		if (instance == null) {
			instance = new StopwordTokenFilter();
		}
		return instance;
	}
}
