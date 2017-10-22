package com.wyamee;

import com.wyamee.data.loader.SearchEngineDataLoader;
import com.wyamee.nlp.CorpusStatsGenerator;
import com.wyamee.query.ArticleRecommenderGenerator;
import com.wyamee.query.IQueryByDocument;
import com.wyamee.query.SimpleTFIDFQueryByDocument;
import com.wyamee.utils.PropertiesHelper;

public class NewsArchiveRecommender {

	public static void main(String[] args) {
		
		//TODO: Handle all of these with command line interactions
		NewsArchiveRecommender nar = new NewsArchiveRecommender();
		nar.generateArticleRecommendations();
	}
	
	private PropertiesHelper propertiesHelper;

	public static void generateCorpusStats() {
		new CorpusStatsGenerator().generate();
	}
	
	public NewsArchiveRecommender() {
		propertiesHelper = PropertiesHelper.getInstance();
	}
	
	public void createAndPopulateSearchIndex() throws Exception {
		SearchEngineDataLoader dataLoader = new SearchEngineDataLoader(
			propertiesHelper.getLuceneIndexDirectory(), propertiesHelper.getNewsArticleDirectory());
		dataLoader.createIndex();
	}
	
	public void generateArticleRecommendations() {
		
		String articleDirectory = propertiesHelper.getRecommendArticleDirectory();
		IQueryByDocument qbdAlgorithm = new SimpleTFIDFQueryByDocument(20);
		int noRelatedArticles = 10;
		
		ArticleRecommenderGenerator generator = new ArticleRecommenderGenerator(
			articleDirectory, qbdAlgorithm, noRelatedArticles);
		generator.generate(propertiesHelper.getRecommendArticleResultsFile());
	}
	
	public void extractQuestionsFromArticles() {
		
	}
}
