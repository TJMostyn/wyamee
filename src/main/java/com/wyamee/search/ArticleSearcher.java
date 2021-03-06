package com.wyamee.search;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.wyamee.utils.PropertiesHelper;

public class ArticleSearcher {

	private String indexLocation;
	private IndexSearcher indexSearcher;
	
	public ArticleSearcher() {
		try {
			PropertiesHelper properties = PropertiesHelper.getInstance();
			indexLocation = properties.getLuceneIndexDirectory();
			indexSearcher = createIndexSearcher();
		}
		catch (IOException e) {
			throw new RuntimeSearchException(e);
		}
	}
	
	public List<Document> query(Query query, int noResults) {
		try {
			List<Document> results = new ArrayList<>();
			TopDocs hits = indexSearcher.search(query, noResults);
		    for (ScoreDoc sd : hits.scoreDocs)
	        {
	            results.add(indexSearcher.doc(sd.doc));
	        }
		    return results;
		}
		catch (IOException e) {
			throw new RuntimeSearchException(e);
		}	
	}
	
	public Document queryById(String articleId) {
		try {
			QueryParser qp = new QueryParser(DocumentFields.ID.name(), new KeywordAnalyzer());
		    Query idQuery = qp.parse(articleId);
		    TopDocs hits = indexSearcher.search(idQuery, 1);
		    if (hits.totalHits == 0) {
		    	return null;
		    }
		    return indexSearcher.doc(hits.scoreDocs[0].doc);
		}
		catch (IOException | ParseException e) {
			throw new RuntimeSearchException(e);
		}
	}
	
	private IndexSearcher createIndexSearcher() throws IOException {
	    Directory dir = FSDirectory.open(Paths.get(indexLocation));
	    IndexReader reader = DirectoryReader.open(dir);
	    IndexSearcher searcher = new IndexSearcher(reader);
	    return searcher;
	}
}
