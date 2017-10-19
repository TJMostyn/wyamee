package com.wyamee.nlp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.wyamee.data.NewsArticle;
import com.wyamee.data.loader.NewsArticleLoader;
import com.wyamee.data.loader.NewsArticleLoaderException;
import com.wyamee.utils.MutableInt;
import com.wyamee.utils.PropertiesHelper;

public class CorpusStatsGenerator {
	
	public static void main(String[] args) {
		new CorpusStatsGenerator().generate();
	}
	
	public void generate() {
		
		PropertiesHelper properties = PropertiesHelper.getInstance();
		NewsArticleLoader articleLoader = new NewsArticleLoader(properties.getNewsArticleDirectory());
		
		NGramExtractor ngramExtractor = new NGramExtractor();
		Map<String, MutableInt> instanceCounts = new HashMap<>();
		Map<String, MutableInt> documentCounts = new HashMap<>();
		int noDocumentsProcessed = 0;
		
		while (articleLoader.hasNext()) {
			try {
				NewsArticle article = articleLoader.next();
				String articleText = getConcatenatedDocumentText(article);
				Map<String, MutableInt> termFrequencies = ngramExtractor.getTermFrequencies(articleText);
				mergeTermFrequencies(termFrequencies, instanceCounts);
				
				for (String uniqueToken : termFrequencies.keySet()) {
					MutableInt.incrementMap(documentCounts, uniqueToken);
				}
				noDocumentsProcessed++;
			}
			catch (NewsArticleLoaderException e) {
				//TODO: Log this error and handle properly
			}
		}

		// Convert the document counts to IDF
		Map<String, Double> tokenIDF = new HashMap<>();
		for (String token : documentCounts.keySet()) {
			double idf = calculateIDF(documentCounts.get(token).get(), noDocumentsProcessed);
			tokenIDF.put(token, idf);
		}
		
		// Write the results to a model file (used by CorpusStats)
		try {
			writeResultsToFile(instanceCounts, tokenIDF);
		}
		catch (IOException e) {
			throw new NLPModelLoadingException(e);
		}
	}
	
	private void writeResultsToFile(
		Map<String, MutableInt> instanceCounts, Map<String, Double> tokenIDF)
		throws IOException {
		
		File filePath = new File(CorpusStats.RESOURCES_ROOT + CorpusStats.FILE_PATH);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(filePath);
	     	for (String key : instanceCounts.keySet()) {	
				writer.println(
					key + "," + instanceCounts.get(key).get() + "," + tokenIDF.get(key));
			}
		}
		finally {
			if (writer != null) writer.close();
		}
	}
	
	private double calculateIDF(int documentFrequency, int totalNumDocuments) {
		return Math.log(totalNumDocuments / (float) documentFrequency);
	}
	
	private void mergeTermFrequencies(
		Map<String, MutableInt> docFreq, Map<String, MutableInt> allFreq) {
		
		for (String token : docFreq.keySet()) {
			MutableInt frequency = docFreq.get(token);
			MutableInt.incrementMap(allFreq, token, frequency.get());
		}
	}
	
	private String getConcatenatedDocumentText(NewsArticle article) {
		StringBuilder builder = new StringBuilder();
		builder.append(article.getArticle().getDescriptiveMetaData().getHeadline().toLowerCase());
		builder.append(". ");
		builder.append(article.getArticle().getDescriptiveMetaData().getSubHeadline().toLowerCase());
		builder.append(". ");
		builder.append(article.getArticle().getDataContent().getBody().toLowerCase());
		builder.append(". ");
		return builder.toString();
	}
}
