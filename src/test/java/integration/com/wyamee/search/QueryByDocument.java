package integration.com.wyamee.search;

import java.io.File;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

import com.wyamee.data.NewsArticle;
import com.wyamee.query.IQueryByDocument;
import com.wyamee.query.TFIDFBasedQueryByDocument;
import com.wyamee.search.ArticleSearcher;
import com.wyamee.search.DocumentFields;
import com.wyamee.utils.PropertiesHelper;

public class QueryByDocument {

	private static String DIVIDER = "-------------------------------------------------------------";
	
	public static void main(String[] args) throws Exception {

		PropertiesHelper properties = PropertiesHelper.getInstance();
		
		// Select a random news article that we find in the list
		File directory = new File(properties.getNewsArticleDirectory());
		int index = ThreadLocalRandom.current().nextInt(0, directory.listFiles().length);
		File testFile = directory.listFiles()[index];
		
		// Load into a object and display
		NewsArticle newsArticle = loadAndMarshallArticle(testFile);
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
	
	protected static NewsArticle loadAndMarshallArticle(File articleFile) throws JAXBException {
		JAXBContext jaxBContext = JAXBContext.newInstance(NewsArticle.class);
        Unmarshaller unmarshaller = jaxBContext.createUnmarshaller();
        return (NewsArticle) unmarshaller.unmarshal(articleFile);
	}
}
