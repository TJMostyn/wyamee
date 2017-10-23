package com.wyamee.data.loader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import com.wyamee.data.NewsArticle;
import com.wyamee.search.DocumentFields;

public class SearchEngineDataLoader {
	
	private static final Logger LOG = Logger.getLogger(SearchEngineDataLoader.class.getName());
	private String luceneIndexDirectory;
	private String newsArticleDirectory;
	private boolean isDirectoryClasspath;
	
	public SearchEngineDataLoader(String luceneIndexDirectory, String newsArticleDirectory) {
		this.luceneIndexDirectory = luceneIndexDirectory;
		this.newsArticleDirectory = newsArticleDirectory;
	}
	
	public SearchEngineDataLoader(
		String luceneIndexDirectory, String newsArticleDirectory, boolean isDirectoryClasspath) {
		this(luceneIndexDirectory, newsArticleDirectory);
		this.isDirectoryClasspath = isDirectoryClasspath;
	}
	
	public void createIndex() throws IOException {
		
		IndexWriter indexWriter = null;
		
		try {
			LOG.info("Delete existing lucene directory at: " + luceneIndexDirectory);
			cleanIndexDirectory(luceneIndexDirectory);
			
			LOG.info("Creating the lucene directory at: " + luceneIndexDirectory);
			indexWriter = createIndexWriter(luceneIndexDirectory);
		
			LOG.info("Getting all news articles from: " + newsArticleDirectory);
			NewsArticleLoader articleLoader = new NewsArticleLoader(newsArticleDirectory);
			
			LOG.info("Found " + articleLoader.getNumberArticles() + " articles to process");
			List<String> failedArticles = new ArrayList<>();
			while (articleLoader.hasNext()) {
				try {
					NewsArticle article = articleLoader.next();
					indexWriter.addDocument(createLuceneDocument(article));
				}
				catch (NewsArticleLoaderException e) {
					failedArticles.add(e.getFileName());
				}
			}
			
			LOG.warning(failedArticles.size() + " articles did not unmarshall correctly: " + failedArticles);
		}
		catch (IOException | URISyntaxException e) {
			LOG.log(Level.SEVERE, "Error loading articles: " + e.getMessage(), e);
		}
		finally {
			LOG.info("Successfully created lucene index");
			if (indexWriter != null) {
				try {
					indexWriter.close();
				}
				catch (IOException e) { /* Do nothing */ }
			}
		}
	}
	
	private IndexWriter createIndexWriter(String indexDirectory) throws IOException, URISyntaxException {
		
		FSDirectory dir = getLoadedDirectory(indexDirectory);
	    IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
	    IndexWriter writer = new IndexWriter(dir, config);
	    return writer;
	}
	
	private void cleanIndexDirectory(String indexDirectory) {
		File indexFolder = new File(indexDirectory);
		File[] files = indexFolder.listFiles();
	    if(files != null) {
	        for(File f: files) {
	            f.delete();
	        }
	    }
	}
	
	private FSDirectory getLoadedDirectory(String indexDirectory) throws IOException, URISyntaxException {
		if (! isDirectoryClasspath) {
			return FSDirectory.open(Paths.get(indexDirectory));
		}
		else {
			return FSDirectory.open(new File(indexDirectory).toPath());
		}
	}
	
	private Document createLuceneDocument(NewsArticle newsArticle)
	{
	    Document document = new Document();
	    
	    // ID
	    document.add(new StringField(DocumentFields.ID.name(), 
    		new Long(newsArticle.getArticle().getDescriptiveMetaData().getNlaArticleId()).toString(), 
    		Field.Store.YES));
	    
	    // Article data
	    document.add(new TextField(DocumentFields.HEADLINE.name(), 
    		newsArticle.getArticle().getDescriptiveMetaData().getHeadline(), 
    		Field.Store.YES));
	    document.add(new TextField(DocumentFields.SUB_HEADLINE.name(), 
    		newsArticle.getArticle().getDescriptiveMetaData().getSubHeadline(), 
    		Field.Store.YES));
	    document.add(new TextField(DocumentFields.CONTENT.name(), 
    		newsArticle.getArticle().getDataContent().getBody(), 
    		Field.Store.YES));
	    document.add(new TextField(DocumentFields.PHOTO_CAPTION.name(), 
    		newsArticle.getArticle().getDataContent().getPhoto().getCaption(), 
    		Field.Store.YES));
	    document.add(new StringField(DocumentFields.BYLINE.name(), 
    		newsArticle.getArticle().getDescriptiveMetaData().getByline().toLowerCase(), 
    		Field.Store.YES));
	    document.add(new StringField(DocumentFields.AUTHOR.name(), 
    		newsArticle.getArticle().getDescriptiveMetaData().getAuthor().toLowerCase(), 
    		Field.Store.YES));
	    
	    // Article meta data
	    document.add(new StringField(DocumentFields.PUBLICATION_NAME.name(), 
    		newsArticle.getSourceMetaData().getPublicationName().toLowerCase(), 
    		Field.Store.YES));
	    document.add(new StringField(DocumentFields.EDITION.name(), 
    		newsArticle.getSourceMetaData().getEdition(), 
    		Field.Store.YES));
	    document.add(new StringField(DocumentFields.REGION.name(), 
    		newsArticle.getSourceMetaData().getRegion().toLowerCase(), 
    		Field.Store.YES));
	    
	    long time = newsArticle.getSourceMetaData().getPublicationDate().getTime();
	    document.add(new LongPoint(DocumentFields.PUBLICATION_DATE.name(), time));
	    document.add(new StoredField(DocumentFields.PUBLICATION_DATE.name(), time)); 
	    
	    int pageNumber = newsArticle.getSourceMetaData().getPageNumbers()[0];
	    document.add(new IntPoint(DocumentFields.PAGE_NUMBER.name(), pageNumber));
	    
	    // Calculate the percentage
	    int articlePcnt = newsArticle.getSourceMetaData().getArticlePercentage()[0] * 
    		Math.max(newsArticle.getSourceMetaData().getArticlePercentage()[1], 1);
	    document.add(new IntPoint(DocumentFields.ARTICLE_PERCENTAGE.name(), articlePcnt));
		
	    return document;
	}
}
