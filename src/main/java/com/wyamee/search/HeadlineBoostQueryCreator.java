package com.wyamee.search;

import java.util.List;

import org.apache.lucene.search.Query;

import com.wyamee.nlp.NGramScore;

public class HeadlineBoostQueryCreator extends SimpleQueryCreator {

	private static final float HEADLINE_BOOST = 1.2F;
	
	@Override
	public Query generate(List<NGramScore> ngramScores) {
		return generate(ngramScores, HEADLINE_BOOST, 1, 1);
	}
}