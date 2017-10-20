package integration.com.wyamee.search;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

import com.wyamee.data.NewsArticle;
import com.wyamee.query.IQueryByDocument;
import com.wyamee.query.TFIDFBasedQueryByDocument;
import com.wyamee.search.ArticleSearcher;
import com.wyamee.search.DocumentFields;

import integration.com.wyamee.utils.NewsArticleTestLoader;

public class QueryByDocument {

	private static String DIVIDER = "-------------------------------------------------------------";
	
	public static void main(String[] args) throws Exception {

		NewsArticle newsArticle = NewsArticleTestLoader.loadRandomArticle();
		System.out.println(DIVIDER);
		System.out.println(newsArticle.getArticle().getDescriptiveMetaData().getHeadline());
		System.out.println(newsArticle.getArticle().getDescriptiveMetaData().getSubHeadline());
		System.out.println(newsArticle.getArticle().getDataContent().getBody());
		
		// Create the query and show it
		IQueryByDocument qbd = new TFIDFBasedQueryByDocument();
		Query query = qbd.extract(newsArticle);
		System.out.println(DIVIDER);
		System.out.println(query.toString());
		
		// Search and show the results
		ArticleSearcher searcher = new ArticleSearcher();
		List<Document> documents = searcher.query(query, 10);
		System.out.println(DIVIDER);
		for (Document document : documents) {
			System.out.println(document.get(DocumentFields.ID.name()) + ": " +
				document.get(DocumentFields.HEADLINE.name()));
		}
	}
}
