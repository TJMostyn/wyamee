package com.wyamee.data.loader;

public class NewsArticleLoaderException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String fileName;
	
	public NewsArticleLoaderException(String fileName, Exception e) {
		super(e);
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
}
