package com.wyamee.data.loader;

import java.awt.TextField;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import com.wyamee.data.NewsArticle;
import com.wyamee.utils.PropertiesHelper;

public class SearchEngineDataLoader {
	
	public static void main(String[] args) throws Exception {
		
		PropertiesHelper properties = PropertiesHelper.getInstance();
		
		// Create the Lucene index
		
		// Get the articles one at a time and write them to the directory
		NewsArticleLoader articleLoader = new NewsArticleLoader(properties.getNewsArticleDirectory());
		while (articleLoader.hasNext()) {
			try {
				NewsArticle article = articleLoader.next();
				//System.out.println(article.getArticle().getDescriptiveMetaData().getHeadline());
			}
			catch (Exception e) {
				System.out.println("Error");
			}
		}
		// Close the index
	}
	
	/*private IndexWriter createWriter(String indexDirectory) throws IOException {
		
		FSDirectory dir = FSDirectory.open(Paths.get(indexDirectory));
	    IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
	    IndexWriter writer = new IndexWriter(dir, config);
	    return writer;
	}
	
	private Document createDocument(NewsArticle newsArticle)
	{
	    Document document = new Document();
	    document.add(new StringField("id", "vfdj", Field.Store.YES));
	    document.add(new TextField("firstName", firstName , Field.Store.YES));
	    document.add(new TextField("lastName", lastName , Field.Store.YES));
	    document.add(new TextField("website", website , Field.Store.YES));
	    return document;
	}*/
}
