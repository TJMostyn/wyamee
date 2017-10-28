package com.wyamee.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper {

	public static class PropertiesHelperException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public PropertiesHelperException(String msg) {
			super(msg);
		}
	}
	
	public static final String PROPERTIES_FILE_NAME = "application.properties";
	
    private static PropertiesHelper instance;

    private String luceneIndexDirectory = "lucene_index_directory";
    private String newsArticleDirectory = "news_article_directory";
    private String recommendArticleDirectory = "recommend_article_directory";
    private String articleRecommendResultsFile = "article_recommend_results_file";
    private String articleQuestionResultsFile = "article_questions_results_file";
    private String recommendDayInterval = "recommend_day_interval";
    
    private Properties properties;
    
    private PropertiesHelper() {
    	properties = new Properties();
		
		// Load the properties file
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream resourceStream = loader.getResourceAsStream(PROPERTIES_FILE_NAME);
		if (resourceStream == null) {
			throw new PropertiesHelperException("Unable to find properties: " 
				+ PROPERTIES_FILE_NAME);
		}
		
		try {
			properties.load(resourceStream);
		}
		catch (IOException e) {
			throw new PropertiesHelperException("Error loading properties file");
		}
    }

    public String getLuceneIndexDirectory() {
        return properties.getProperty(luceneIndexDirectory);
    }

    public String getNewsArticleDirectory() {
        return properties.getProperty(newsArticleDirectory);
    }

	public String getRecommendArticleDirectory() {
		return properties.getProperty(recommendArticleDirectory);
	}

	public String getArticleRecommendResultsFile() {
		return properties.getProperty(articleRecommendResultsFile);
	}

	public String getArticleQuestionResultsFile() {
		return properties.getProperty(articleQuestionResultsFile);
	}
	
	public int getRecommendDayInterval() {
		return Integer.parseInt(properties.getProperty(recommendDayInterval));
	}

    public static synchronized PropertiesHelper getInstance() {
    	if (instance == null) {
    		instance = new PropertiesHelper();
    	}
    	
        return instance;
    }
}