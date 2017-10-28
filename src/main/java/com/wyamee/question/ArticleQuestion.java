package com.wyamee.question;

public class ArticleQuestion {

	private String articleId;
	private String question;
	private String answer;
	
	public ArticleQuestion(String articleId, String question, String answer) {
		this.articleId = articleId;
		this.question = question;
		this.answer = answer;
	}
	
	public String getArticleId() {
		return articleId;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public String getAnswer() {
		return answer;
	}
}
