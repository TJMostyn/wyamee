package com.wyamee.search;

import java.util.List;
import org.apache.lucene.search.Query;
import com.wyamee.nlp.NGramScore;

public interface IQueryCreator {
	Query generate(List<NGramScore> ngramScores);
}
