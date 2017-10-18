package integration.com.wyamee.search;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.BooleanClause;

import com.wyamee.data.loader.SearchEngineDataLoader;
import com.wyamee.search.DocumentFields;

public class IndexValidator {
	
	public static void main(String[] args) throws Exception {
		new IndexValidator();
	}
	
	public IndexValidator() throws Exception {
		
		String indexPath = "src/test/resources/integration/index";
		String articlesPath = "src/test/resources/integration/articles";
		
		// Delete the integration index if it exists
		FileUtils.cleanDirectory(new File(indexPath));
		
		// Create the fake index
		SearchEngineDataLoader dataLoader = new SearchEngineDataLoader(
			indexPath, articlesPath, true);
		dataLoader.createIndex();
		
		// Load the index
		IndexSearcher indexSearcher = createIndexSearcher(indexPath);
		
		manageOutput(searchById(indexSearcher), "searchById");
		manageOutput(searchByHeadline(indexSearcher), "searchByHeadline");
		manageOutput(searchBySubHeadline(indexSearcher), "searchBySubHeadline");
		manageOutput(searchByContent(indexSearcher), "searchByContent");
		manageOutput(searchByPhotoCaption(indexSearcher), "searchByPhotoCaption");
		manageOutput(searchByline(indexSearcher), "searchByline");
		manageOutput(searchAuthor(indexSearcher), "searchAuthor");
		manageOutput(searchPublicationName(indexSearcher), "searchPublicationName");
		manageOutput(searchEdition(indexSearcher), "searchEdition");
		manageOutput(searchRegion(indexSearcher), "searchRegion");
		manageOutput(searchPublicationDate(indexSearcher), "searchPublicationDate");
		manageOutput(searchPageNumbers(indexSearcher), "searchPageNumbers");
		manageOutput(searchArticlePercentage(indexSearcher), "searchArticlePercentage");
	}
	
	public boolean searchById(IndexSearcher indexSearcher) throws Exception {
		return searchValue(indexSearcher, DocumentFields.ID.name(), new Long(117975070L).toString(), 1);
	}
	
	private void manageOutput(boolean result, String testName) throws Exception {
		if (! result) {
			//TODO: This needs to be replaced with logging...
			System.out.println("Failed integration test for index validator: " + testName);
		}
	}
	
	private boolean searchByHeadline(IndexSearcher indexSearcher) throws Exception {
		
		return searchAndClause(
			indexSearcher, DocumentFields.HEADLINE.name(), "trump", "boasts", 
			"Trump refuses to quit over lewd sex boasts");
	}
	
	private boolean searchBySubHeadline(IndexSearcher indexSearcher) throws Exception {
		
		return searchAndClause(
			indexSearcher, DocumentFields.SUB_HEADLINE.name(), "package", "sky", 
			"A suspect package may finally unravel the truth about Team Sky and their ethics");
	}
	
	private boolean searchByContent(IndexSearcher indexSearcher) throws Exception {
		
		QueryParser qp1 = new QueryParser(DocumentFields.CONTENT.name(), new StandardAnalyzer());
	    Query query1 = qp1.parse("manager");
		QueryParser qp2 = new QueryParser(DocumentFields.CONTENT.name(), new StandardAnalyzer());
	    Query query2 = qp2.parse("back");
	    
		BooleanQuery booleanQuery = new BooleanQuery.Builder().
		    add(query1, BooleanClause.Occur.MUST).
		    add(query2, BooleanClause.Occur.MUST).
		    build();

		TopDocs hits = indexSearcher.search(booleanQuery, 10);
	    
	    return hits.totalHits == 2;
	}
	
	private boolean searchByPhotoCaption(IndexSearcher indexSearcher) throws Exception {
		
		return searchAndClause(
			indexSearcher, DocumentFields.PHOTO_CAPTION.name(), "reputational", "bradley", 
			"Man in the middle: Bradley Wiggins has suffered significant reputational damage.over "
				+ "the past few weeks, as has Dave Brailsford, the under-fire boss of Team Sky");
	}
	
	private boolean searchByline(IndexSearcher indexSearcher) throws Exception {
		return searchValue(indexSearcher, DocumentFields.BYLINE.name(), "duncan Castles", 1);
	}
	
	private boolean searchAuthor(IndexSearcher indexSearcher) throws Exception {
		return searchValue(indexSearcher, DocumentFields.AUTHOR.name(), "toby harnden", 1);
	}
	
	private boolean searchPublicationName(IndexSearcher indexSearcher) throws Exception {
		return searchValue(indexSearcher, DocumentFields.PUBLICATION_NAME.name(), "The Sunday Times", 10);
	}
	
	private boolean searchEdition(IndexSearcher indexSearcher) throws Exception {
		return searchValue(indexSearcher, DocumentFields.EDITION.name(), "01", 10);
	}
	
	private boolean searchRegion(IndexSearcher indexSearcher) throws Exception {
		return searchValue(indexSearcher, DocumentFields.REGION.name(), "wales", 10);
	}
	
	private boolean searchPublicationDate(IndexSearcher indexSearcher) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		long startTime = sdf.parse("01102016").getTime();
		long endTime = sdf.parse("04102016").getTime();
		
		Query rangeQuery = LongPoint.newRangeQuery(
			DocumentFields.PUBLICATION_DATE.name(), startTime, endTime);
	    TopDocs hits = indexSearcher.search(rangeQuery, 10);
		    
	    if (hits.totalHits != 2)
	    	return false;
	    
	    return true;
	}
	
	private boolean searchPageNumbers(IndexSearcher indexSearcher) throws Exception {
		Query rangeQuery = IntPoint.newRangeQuery(
			DocumentFields.PAGE_NUMBER.name(), 4, 6);
	    TopDocs hits = indexSearcher.search(rangeQuery, 10);
		    
	    if (hits.totalHits != 1)
	    	return false;
	    
	    return true;
	}
	
	private boolean searchArticlePercentage(IndexSearcher indexSearcher) throws Exception {
		Query rangeQuery = IntPoint.newRangeQuery(
			DocumentFields.ARTICLE_PERCENTAGE.name(), 5, 10);
	    TopDocs hits = indexSearcher.search(rangeQuery, 10);
		    
	    if (hits.totalHits != 2)
	    	return false;
	    
	    return true;
	}
	
	private boolean searchValue(
		IndexSearcher indexSearcher, String fieldName, String fieldValue, int noResults) throws Exception {
		
		QueryParser qp = new QueryParser(fieldName, new KeywordAnalyzer());
	    Query idQuery = qp.parse(fieldValue.toLowerCase());
	    TopDocs hits = indexSearcher.search(idQuery, 10);
	    
	    if (hits.totalHits != noResults)
	    	return false;
	    
	    return true;
	}
	
	private boolean searchAndClause(IndexSearcher indexSearcher,
		String fieldName, String term1, String term2, String expectedFieldValue) throws Exception {
		
		QueryParser qp1 = new QueryParser(fieldName, new StandardAnalyzer());
	    Query query1 = qp1.parse(term1);
		QueryParser qp2 = new QueryParser(fieldName, new StandardAnalyzer());
	    Query query2 = qp2.parse(term2);
	    
		BooleanQuery booleanQuery = new BooleanQuery.Builder().
		    add(query1, BooleanClause.Occur.MUST).
		    add(query2, BooleanClause.Occur.MUST).
		    build();

		TopDocs hits = indexSearcher.search(booleanQuery, 10);
	    
	    if (hits.totalHits != 1)
	    	return false;
	    
	    for (ScoreDoc sd : hits.scoreDocs)
        {
            Document document = indexSearcher.doc(sd.doc);
            String fieldValue = document.get(fieldName);
            if (! fieldValue.equalsIgnoreCase(expectedFieldValue))
            	return false;
        }
	    
	    return true;
	}
	
	private IndexSearcher createIndexSearcher(String indexLocation) throws IOException
	{
	    Directory dir = FSDirectory.open(Paths.get(indexLocation));
	    IndexReader reader = DirectoryReader.open(dir);
	    IndexSearcher searcher = new IndexSearcher(reader);
	    return searcher;
	}
}
