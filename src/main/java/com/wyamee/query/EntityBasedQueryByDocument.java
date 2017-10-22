package com.wyamee.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.Query;

import com.wyamee.data.NewsArticle;
import com.wyamee.nlp.NGramScore;
import com.wyamee.nlp.NamedEntityExtractor;
import com.wyamee.utils.ArticleStringUtils;
import com.wyamee.utils.MutableInt;

public class EntityBasedQueryByDocument extends TFIDFBasedQueryByDocument {

	private NamedEntityExtractor namedEntityExtractor;
	private int noQueryTerms;
	
	public EntityBasedQueryByDocument(int noQueryTerms) {
		namedEntityExtractor = new NamedEntityExtractor();
		this.noQueryTerms = noQueryTerms;
	}
	
	@Override
	public Query extract(NewsArticle article) {
		String content = ArticleStringUtils.getConcatenatedDocumentText(article, false);
		Map<String, MutableInt> entityFreq = new HashMap<>();
		
		List<String> entities = namedEntityExtractor.extractPeople(content);
		entities.addAll(namedEntityExtractor.extractLocations(content));
		entities.addAll(namedEntityExtractor.extractOrganisations(content));
		
		for (String entity : entities) {
			MutableInt.incrementMap(entityFreq, entity.toLowerCase());
		}
		
		List<NGramScore> orderedScores = getOrderedIDFScores(entityFreq);
		return getQueryCreator().generate(
			orderedScores.subList(0, Math.min(noQueryTerms, orderedScores.size())));
	}
}
