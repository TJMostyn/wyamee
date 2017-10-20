package com.wyamee.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.Query;

import com.wyamee.data.NewsArticle;
import com.wyamee.nlp.CorpusStats;
import com.wyamee.nlp.NGramExtractor;
import com.wyamee.nlp.NGramScore;
import com.wyamee.search.IQueryCreator;
import com.wyamee.search.SimpleQueryCreator;
import com.wyamee.utils.MutableInt;

public class TFIDFBasedQueryByDocument implements IQueryByDocument {
	
	//TODO: Pass this into the class?
	private static int NO_QUERY_TERMS = 20;
	private NGramExtractor ngramExtractor;
	private CorpusStats corpusStats;
	private IQueryCreator queryCreator;
	
	public TFIDFBasedQueryByDocument() {
		ngramExtractor = new NGramExtractor();
		corpusStats = CorpusStats.getInstance();
		queryCreator = new SimpleQueryCreator();
	}
	
	@Override
	public Query extract(NewsArticle article) {
		
		String content = article.getArticle().getDataContent().getBody();
		Map<String, MutableInt> termFreq = ngramExtractor.getTermFrequencies(content);
		List<NGramScore> orderedScores = getOrderedIDFScores(termFreq);
		return queryCreator.generate(orderedScores.subList(0, NO_QUERY_TERMS));
		
	}
	
	protected List<NGramScore> getOrderedIDFScores(Map<String, MutableInt> termFreq) {
		List<NGramScore> orderedScores = new ArrayList<>();
		for (String token : termFreq.keySet()) {
			double tfidf = termFreq.get(token).get() * corpusStats.getIDF(token);
			orderedScores.add(new NGramScore(token, tfidf));
		}
		
		Collections.sort(orderedScores);
		Collections.reverse(orderedScores);
		return orderedScores;
	}
}
