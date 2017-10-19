package com.wyamee.nlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CorpusStats {

	private class Statistics {
		public int instanceCount;
		public double idf;
		
		public Statistics(int instanceCount, double idf) {
			this.instanceCount = instanceCount;
			this.idf = idf;
		}
	}
	
	protected static String RESOURCES_ROOT = "src/main/resources/";
	protected static String FILE_PATH = "nlp/models/corpus-stats.csv";
	
	private static CorpusStats instance = null;
	private Map<String, Statistics> statistics;
	private double maxIDF;
	
	protected CorpusStats() {
		statistics = new HashMap<>();
		try {
			loadModelFile();
		}
		catch (IOException e) {
			throw new NLPModelLoadingException(e);
		}
	}
	
	public int getInstanceCount(String token) {
		Statistics results = statistics.get(token);
		if (results == null) return 0;
		return results.instanceCount;
	}
	
	public double getIDF(String token) {
		Statistics results = statistics.get(token);
		if (results == null) return maxIDF;
		return results.idf;
	}
	
	public double getMaxIDF() {
		return maxIDF;
	}
	
	private void loadModelFile() throws IOException {
		BufferedReader reader = null;
		try {
			File file = loadFileFromResources();
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length != 3) {
					//TODO: need to log this properly
					System.out.println("Knackered line: " + line);
					continue;
				}
				
				int ic = Integer.parseInt(parts[1]);
				double idf = Double.parseDouble(parts[2]);
				statistics.put(parts[0], new Statistics(ic, idf));
				if (idf > maxIDF) maxIDF = idf;
			}
		}
		finally {
			if (reader != null) reader.close();
		}
	}
	
	protected File loadFileFromResources() {
		ClassLoader classLoader = getClass().getClassLoader();
		return new File(classLoader.getResource(FILE_PATH).getFile());
	}
	
	public static synchronized CorpusStats getInstance() {
		if (instance == null) {
			instance = new CorpusStats();
		}
		return instance;
	}
}
