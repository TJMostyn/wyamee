package com.wyamee.query;

import java.util.List;
import java.util.Map;

import org.apache.lucene.search.Query;

import com.wyamee.data.NewsArticle;
import com.wyamee.nlp.NGramExtractor;
import com.wyamee.nlp.NGramScore;
import com.wyamee.utils.ArticleStringUtils;
import com.wyamee.utils.MutableInt;

public class SimpleTFIDFQueryByDocument extends TFIDFBasedQueryByDocument {

	private NGramExtractor ngramExtractor;
	private int noQueryTerms;
	
	public SimpleTFIDFQueryByDocument(int noQueryTerms) {
		ngramExtractor = new NGramExtractor();
		this.noQueryTerms = noQueryTerms;
	}
	
	@Override
	public Query extract(NewsArticle article) {
		
		String content = ArticleStringUtils.getConcatenatedDocumentText(article, true);
		Map<String, MutableInt> termFreq = ngramExtractor.getTermFrequencies(content);
		List<NGramScore> orderedScores = getOrderedIDFScores(termFreq);
		return getQueryCreator().generate(
			orderedScores.subList(0, Math.min(noQueryTerms, orderedScores.size())));
	}
}
