package integration.com.wyamee.recommend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;

import org.apache.lucene.document.Document;

import com.wyamee.search.ArticleSearcher;
import com.wyamee.search.DocumentFields;
import com.wyamee.utils.PropertiesHelper;

public class RecommendResultsDisplay {

	public static void main(String[] args) throws Exception {
		
		PropertiesHelper propHelper = PropertiesHelper.getInstance();
		ArticleSearcher searcher = new ArticleSearcher();
		File file = new File(propHelper.getArticleRecommendResultsFile());
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				String[] relatedIds = parts[1].split("\\|");
				
				for (String relatedId : relatedIds) {
					Document document = searcher.queryById(relatedId);
					System.out.println(new Date(Long.parseLong(
						document.get(DocumentFields.PUBLICATION_DATE.name()))) + ":" + 
						document.get(DocumentFields.HEADLINE.name()));
				}
				System.out.println("----------------------------------------------------");
			}
		}
	}
}
