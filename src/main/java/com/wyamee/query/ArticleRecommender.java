package com.wyamee.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

import com.wyamee.data.NewsArticle;
import com.wyamee.search.ArticleSearcher;
import com.wyamee.search.DocumentFields;

public class ArticleRecommender {

	private IQueryByDocument queryByDocument;
	private ArticleSearcher searcher;
	
	public ArticleRecommender(IQueryByDocument queryByDocument) {
		this.queryByDocument = queryByDocument;
		searcher = new ArticleSearcher();
	}
	
	public List<String> discover(NewsArticle newsArticle, int noRelatedArticles) {
		
		Query query = queryByDocument.extract(newsArticle);
		List<Document> documents = searcher.query(query, noRelatedArticles);

		List<String> relatedArticleIds = new ArrayList<>();
		for (Document document : documents) {
			relatedArticleIds.add(document.get(DocumentFields.ID.name()));
		}
		
		return relatedArticleIds;
	}
}
