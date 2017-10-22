package com.wyamee.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.wyamee.nlp.CorpusStats;
import com.wyamee.nlp.NGramScore;
import com.wyamee.search.IQueryCreator;
import com.wyamee.search.SimpleQueryCreator;
import com.wyamee.utils.MutableInt;

public abstract class TFIDFBasedQueryByDocument implements IQueryByDocument {

	private CorpusStats corpusStats;
	private IQueryCreator queryCreator;
	
	protected TFIDFBasedQueryByDocument() {
		corpusStats = CorpusStats.getInstance();
		queryCreator = new SimpleQueryCreator();
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
	
	protected IQueryCreator getQueryCreator() {
		return queryCreator;
	}
}
