package com.wyamee.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

public abstract class AbstractQueryCreator implements IQueryCreator {

	protected Query createQuery(String queryTerm, DocumentFields field) throws ParseException {
		QueryParser queryParser = new QueryParser(field.name(), new StandardAnalyzer());
		return queryParser.parse(queryTerm);
	}
}
