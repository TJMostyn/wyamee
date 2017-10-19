package com.wyamee.nlp;

public class NGramScore implements Comparable<NGramScore> {

	private String ngram;
	private double score;
	
	public NGramScore(String ngram, double score) {
		this.ngram = ngram;
		this.score = score;
	}
	
	public String getNgram() {
		return ngram;
	}
	
	public double getScore() {
		return score;
	}

	@Override
	public int compareTo(NGramScore o) {
		return ((Double) score).compareTo(o.score);
	}
}
