package com.wyamee.search;

import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

import com.wyamee.nlp.NGramScore;

public class SimpleQueryCreator extends AbstractQueryCreator {

	@Override
	public Query generate(List<NGramScore> ngramScores) {
		
		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		for (NGramScore ngramScore : ngramScores) {
			try {
				queryBuilder.add(
					createQuery(ngramScore.getNgram(), DocumentFields.HEADLINE), BooleanClause.Occur.SHOULD);
				queryBuilder.add(
					createQuery(ngramScore.getNgram(), DocumentFields.SUB_HEADLINE), BooleanClause.Occur.SHOULD);
				queryBuilder.add(
					createQuery(ngramScore.getNgram(), DocumentFields.CONTENT), BooleanClause.Occur.SHOULD);
			}
			catch (ParseException e) {
				//TODO: Log this correctly
				System.out.println("Error adding query: " + ngramScore.getNgram());
			}
		}
	    return queryBuilder.build();
	}
}
