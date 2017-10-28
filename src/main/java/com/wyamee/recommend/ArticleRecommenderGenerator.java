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
import com.wyamee.recommend.ArticleRecommender.ArticleLite;

public class ArticleRecommenderGenerator {
	
	private static final Logger LOG = Logger.getLogger(ArticleRecommenderGenerator.class.getName());
	private static final String DELIMITER = "|";
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
		
		LOG.info("Extracting questions for each article");
		List<String> failedArticles = new ArrayList<>();
int noRec = 0;
		while (articleLoader.hasNext()) {
			try {
				NewsArticle article = articleLoader.next();
				List<ArticleLite> relatedArticles = articleRecommender.discover(
					article, noRelatedArticles, recommendDayInterval);
if (relatedArticles.size() == 0) noRec++;
				for (ArticleLite relatedArticle : relatedArticles) {
					StringBuilder line = new StringBuilder();
					line.append(article.getArticle().getDescriptiveMetaData().getNlaArticleId());
					line.append(DELIMITER);
					line.append(relatedArticle.getArticleId());
					line.append(DELIMITER);
					line.append(relatedArticle.getHeadline().replace("|", " "));
					line.append(DELIMITER);
					line.append(relatedArticle.getPublicationDate());
					printStream.println(line.toString());
				}
			}
			catch (NewsArticleLoaderException e) {
				failedArticles.add(e.getFileName());
			}
		}
System.out.println("--" + noRec);
		
		LOG.warning(failedArticles.size() + " did not unmarshall correctly: " + failedArticles);
		LOG.info("Completed writing questions to " + outputName);
	}
}