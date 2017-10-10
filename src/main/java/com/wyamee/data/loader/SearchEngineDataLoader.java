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

public class SearchEngineDataLoader {
	
	public static void main(String[] args) throws Exception {
		
		File testFile = new File(
			"/media/tobymostyn/Data Drive/Downloads/Users/jamieprangnell/Desktop/NLA-Sample-File/20161009_The Sunday Times_Reg-ulster_Sup-Sport_Ed-01_117975792.XML");
		
		JAXBContext context = JAXBContext.newInstance(NewsArticle.class);
		 
        Unmarshaller unmarshaller = context.createUnmarshaller();
        NewsArticle article = (NewsArticle) unmarshaller.unmarshal(testFile);
        System.out.println(article.getSourceMetaData().getPublicationDate());
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
