package com.wyamee;

import com.wyamee.data.loader.SearchEngineDataLoader;
import com.wyamee.nlp.CorpusStatsGenerator;
import com.wyamee.query.IQueryByDocument;
import com.wyamee.query.SimpleTFIDFQueryByDocument;
import com.wyamee.recommend.ArticleRecommenderGenerator;
import com.wyamee.utils.PropertiesHelper;

public class NewsArchiveRecommender {

	private enum Actions {
		INDEX,
		CORPUS,
		RECOMMEND,
		QUESTION;
		
		private static Actions load(String actionStr) {
			for (Actions action : Actions.values()) {
				if (action.name().equalsIgnoreCase(actionStr)) {
					return action;
				}
			}
			return null;
		}
	}
	
	private static NewsArchiveRecommender nar = new NewsArchiveRecommender();
	
	public static void main(String[] args) throws Exception {
		
		// Error if we only have one command line argument
		if (args.length != 1) {
			leaveWithError();
		}
		
		// Error if the action specified is not valid
		Actions action = Actions.load(args[0]);
		if (action == null) {
			leaveWithError();
		}

		switch (action) {
			case INDEX:
				nar.createAndPopulateSearchIndex();
				break;
			case CORPUS:
				nar.generateCorpusStats();
				break;
			case RECOMMEND:
				nar.generateArticleRecommendations();
				break;
			case QUESTION:
				nar.extractQuestionsFromArticles();
				break;
		}
	}
	
	private static void leaveWithError() {
		StringBuilder actions = new StringBuilder();
		for (Actions action : Actions.values()) {
			actions.append(action.name() + " ");
		}
		String errMsg = "Unable to run program. It takes one of the following arguments: " + actions.toString();
		throw new IllegalArgumentException(errMsg);
	}
	
	private PropertiesHelper propertiesHelper;
	
	public NewsArchiveRecommender() {
		propertiesHelper = PropertiesHelper.getInstance();
	}
	
	public void createAndPopulateSearchIndex() throws Exception {
		SearchEngineDataLoader dataLoader = new SearchEngineDataLoader(
			propertiesHelper.getLuceneIndexDirectory(), propertiesHelper.getNewsArticleDirectory());
		dataLoader.createIndex();
	}

	public void generateCorpusStats() {
		new CorpusStatsGenerator().generate();
	}
	
	public void generateArticleRecommendations() {
		
		String articleDirectory = propertiesHelper.getRecommendArticleDirectory();
		IQueryByDocument qbdAlgorithm = new SimpleTFIDFQueryByDocument(20);
		int noRelatedArticles = 10;
		int recommendDayInterval = propertiesHelper.getRecommendDayInterval();
		
		ArticleRecommenderGenerator generator = new ArticleRecommenderGenerator(
			articleDirectory, qbdAlgorithm, noRelatedArticles, recommendDayInterval);
		generator.generate(propertiesHelper.getRecommendArticleResultsFile());
	}
	
	public void extractQuestionsFromArticles() {
		
	}
}
