package com.wyamee.query;

import org.apache.lucene.search.Query;
import com.wyamee.data.NewsArticle;

public interface IQueryByDocument {
	Query extract(NewsArticle article);
}
