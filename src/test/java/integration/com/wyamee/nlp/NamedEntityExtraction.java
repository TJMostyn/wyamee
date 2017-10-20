package integration.com.wyamee.nlp;

import java.util.List;

import com.wyamee.data.NewsArticle;
import com.wyamee.nlp.NamedEntityExtractor;

import integration.com.wyamee.utils.NewsArticleTestLoader;

public class NamedEntityExtraction {

	public static void main(String[] args) {
		
		// Extract people
		NewsArticle newsArticle = NewsArticleTestLoader.loadRandomArticle();
		NamedEntityExtractor neExtractor = new NamedEntityExtractor();
		String content = newsArticle.getArticle().getDataContent().getBody();
		System.out.println(content);
		
		List<String> entities = neExtractor.extractPeople(content);
		for (String entity : entities) {
			System.out.println("Person: " + entity);
		}
		
		entities = neExtractor.extractLocations(content);
		for (String entity : entities) {
			System.out.println("Location: " + entity);
		}
		
		entities = neExtractor.extractOrganisations(content);
		for (String entity : entities) {
			System.out.println("Organisation: " + entity);
		}
	}
}
