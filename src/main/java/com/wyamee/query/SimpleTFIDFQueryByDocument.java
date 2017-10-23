package com.wyamee.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.Query;

import com.wyamee.data.NewsArticle;
import com.wyamee.nlp.NGramExtractor;
import com.wyamee.nlp.NGramScore;
import com.wyamee.utils.ArticleStringUtils;
import com.wyamee.utils.MutableInt;

public class SimpleTFIDFQueryByDocument extends TFIDFBasedQueryByDocument {

	private static final float MAX_BIGRAM_PROPORTION = 0.9F;
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
		
		int noPermittedBigrams = (int) (noQueryTerms * MAX_BIGRAM_PROPORTION);
		int seenBigrams = 0;
		List<NGramScore> returnList = new ArrayList<>();
		for (int i = 0; i < orderedScores.size(); i++) {
			String ngram = orderedScores.get(i).getNgram();
			if (ngram.contains(" ")) {
				if (seenBigrams < noPermittedBigrams) {
					returnList.add(orderedScores.get(i));
					seenBigrams++;
					System.out.println(ngram);
				}
			}
			else {
				returnList.add(orderedScores.get(i));
				System.out.println(ngram);
			}
			if (returnList.size() >= noQueryTerms) break;
		}
		return getQueryCreator().generate(returnList);
	}
}
