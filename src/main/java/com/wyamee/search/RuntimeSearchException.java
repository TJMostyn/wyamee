package com.wyamee.search;

public class RuntimeSearchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RuntimeSearchException(Exception e) {
		super("Error querying search index", e);
	}
}
