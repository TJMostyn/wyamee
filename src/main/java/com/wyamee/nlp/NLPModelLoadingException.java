package com.wyamee.nlp;

public class NLPModelLoadingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NLPModelLoadingException(Exception e) {
		super("Error generating corpus stats", e);
	}
}
