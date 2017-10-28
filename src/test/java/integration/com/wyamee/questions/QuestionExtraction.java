package integration.com.wyamee.questions;

import java.util.List;

import com.wyamee.data.NewsArticle;
import com.wyamee.question.ArticleQuestion;
import com.wyamee.question.IQuestionExtractor;
import com.wyamee.question.SimpleQuestionExtractor;

import integration.com.wyamee.utils.NewsArticleTestLoader;

public class QuestionExtraction {

	public static void main(String[] args) throws Exception {
		runQuestionExtraction();
	}
	
	public static void runQuestionExtraction() {
		NewsArticle newsArticle = NewsArticleTestLoader.loadRandomArticle();
		String content = newsArticle.getArticle().getDataContent().getBody();
		System.out.println(content);
		
		IQuestionExtractor qExtractor = new SimpleQuestionExtractor();
		List<ArticleQuestion> questions = qExtractor.extractQuestions(newsArticle, 3);
		
		for (ArticleQuestion question : questions) {
			System.out.println(question.getQuestion() + ": " + question.getAnswer());
		}
	}
}
