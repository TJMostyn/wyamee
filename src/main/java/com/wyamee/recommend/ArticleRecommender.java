package com.wyamee.recommend;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.time.ZoneId;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.Instant;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

import com.wyamee.data.NewsArticle;
import com.wyamee.query.IQueryByDocument;
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
	
	public List<String> discover(NewsArticle newsArticle, int noRelatedArticles, int daysInterval) {
		
		Query query = queryByDocument.extract(newsArticle);
		List<Document> documents = searcher.query(query, noRelatedArticles * 5);

		List<String> relatedArticleIds = new ArrayList<>();
		List<LocalDate> relatedDates = new ArrayList<>();
		for (Document document : documents) {
			long millis = Long.parseLong(document.get(DocumentFields.PUBLICATION_DATE.name()));
			LocalDate articleDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate();
			
			// Ignore the article if it is within n days of any more relevant documents
			if (! isFromValidDate(relatedDates, articleDate, daysInterval)) {
				continue;
			}
			
			relatedArticleIds.add(document.get(DocumentFields.ID.name()));
			relatedDates.add(articleDate);
			
			if (relatedArticleIds.size() == noRelatedArticles) break;
		}
		
		return relatedArticleIds;
	}
	
	private boolean isFromValidDate(List<LocalDate> relatedDates, LocalDate articleDate, int daysInterval) {
		for (LocalDate relatedDate : relatedDates) {
			if (DAYS.between(articleDate, relatedDate) < daysInterval) {
				return false;
			}
		}
		
		return true;
	}
}
