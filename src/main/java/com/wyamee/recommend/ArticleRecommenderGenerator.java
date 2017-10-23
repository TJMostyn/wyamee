package com.wyamee.recommend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.wyamee.data.NewsArticle;
import com.wyamee.data.loader.NewsArticleLoader;
import com.wyamee.data.loader.NewsArticleLoaderException;
import com.wyamee.query.IQueryByDocument;

public class ArticleRecommenderGenerator {
	
	private static final Logger LOG = Logger.getLogger(ArticleRecommenderGenerator.class.getName());
	private String recommendArticleDirectory;
	private ArticleRecommender articleRecommender;
	private int noRelatedArticles;
	private int recommendDayInterval;
	
	public ArticleRecommenderGenerator(
		String recommendArticleDir, IQueryByDocument qbdAlogrithm, int noRelatedArticles, int recommendDayInterval) {
		this.recommendArticleDirectory = recommendArticleDir;
		articleRecommender = new ArticleRecommender(qbdAlogrithm);
		this.noRelatedArticles = noRelatedArticles;
		this.recommendDayInterval = recommendDayInterval;
	}
	
	public void generate() {
		generate(System.out, "stdout");
	}
	
	public void generate(String filePath) {
		File file = new File(filePath);
		try {
			if (! file.getParentFile().exists()) {
				throw new FileNotFoundException();
			}
			
			PrintStream printStream = new PrintStream(new FileOutputStream(file));
			generate(printStream, filePath);
		}
		catch (FileNotFoundException e) {
			LOG.severe("Unable to write to file " + filePath + ". Is it a valid path?");
		}
	}
	
	private void generate(PrintStream printStream, String outputName) {

		LOG.info("Getting all news articles from: " + recommendArticleDirectory);
		NewsArticleLoader articleLoader = new NewsArticleLoader(recommendArticleDirectory);
		
		LOG.info("Finding recommended articles for each article");
		List<String> failedArticles = new ArrayList<>();
		while (articleLoader.hasNext()) {
			try {
				NewsArticle article = articleLoader.next();
				List<String> relatedArticles = articleRecommender.discover(
					article, noRelatedArticles, recommendDayInterval);
				String csvResult = article.getArticle().getDescriptiveMetaData().getNlaArticleId() + 
					"," + toCommaSeparated(relatedArticles);
				printStream.println(csvResult);
			}
			catch (NewsArticleLoaderException e) {
				failedArticles.add(e.getFileName());
			}
		}
		
		LOG.warning(failedArticles.size() + " did not unmarshall correctly: " + failedArticles);
		LOG.info("Completed writing recommendations to " + outputName);
	}
	
	private String toCommaSeparated(List<String> list) {
		String delimiter = "|";
		StringBuilder builder = new StringBuilder();
		for (String item : list) {
			if (builder.length() > 0) {
				builder.append(delimiter);
			}
		    builder.append(item);
		}
		return builder.toString();
	}
}