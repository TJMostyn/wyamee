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

	public static class ArticleLite {
		private String articleId;
		private String headline;
		private long publicationDate;
		
		public ArticleLite(String articleId, String headline, long publicationDate) {
			this.articleId = articleId;
			this.headline = headline;
			this.publicationDate = publicationDate;
		}
		
		public String getArticleId() {
			return articleId;
		}
		
		public String getHeadline() {
			return headline;
		}
		
		public long getPublicationDate() {
			return publicationDate;
		}
	}
	
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
	
	public List<ArticleLite> discover(NewsArticle newsArticle, int noRelatedArticles, int daysInterval) {
		
		Query query = queryByDocument.extract(newsArticle);
		List<Document> documents = searcher.query(query, noRelatedArticles * 5);
		
		//TODO: write this so that we query every month??

		List<ArticleLite> relatedArticle = new ArrayList<>();
		List<LocalDate> relatedDates = new ArrayList<>();
		for (Document document : documents) {
			long millis = Long.parseLong(document.get(DocumentFields.PUBLICATION_DATE.name()));
			LocalDate articleDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate();
			
			// Ignore the article if it is within n days of any more relevant documents
			if (! isFromValidDate(relatedDates, articleDate, daysInterval)) {
				continue;
			}
			
			relatedArticle.add(new ArticleLite(
				document.get(DocumentFields.ID.name()), 
				document.get(DocumentFields.HEADLINE.name()), 
				Long.parseLong(document.get(DocumentFields.PUBLICATION_DATE.name()))));
			relatedDates.add(articleDate);
			
			if (relatedArticle.size() == noRelatedArticles) break;
		}
		
		return relatedArticle;
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
