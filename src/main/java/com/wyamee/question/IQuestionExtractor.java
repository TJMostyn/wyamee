package com.wyamee.question;

import java.util.List;

import com.wyamee.data.NewsArticle;

public interface IQuestionExtractor {
	List<ArticleQuestion> extractQuestions(NewsArticle article, int maxQuestions);
}
