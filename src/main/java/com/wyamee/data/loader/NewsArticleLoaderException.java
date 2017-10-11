package com.wyamee.data.loader;

public class NewsArticleLoaderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NewsArticleLoaderException(Exception e) {
		super("Error loading news articles", e);
	}
}
