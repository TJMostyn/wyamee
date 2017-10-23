package com.wyamee;

import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
public class NewsArchiveRecommenderTest {
	
	@Test
	public void testIndexCorrectlyCalled() throws Exception {
		NewsArchiveRecommender recommender = mock(NewsArchiveRecommender.class);
		mockRecommenderObject(recommender);
		NewsArchiveRecommender.main(new String[] {"INDEX"});
		verify(recommender, atLeastOnce()).createAndPopulateSearchIndex();
		verify(recommender, never()).generateCorpusStats();
		verify(recommender, never()).generateArticleRecommendations();
		verify(recommender, never()).extractQuestionsFromArticles();
	}
	
	@Test
	public void testCorpusCorrectlyCalled() throws Exception {
		NewsArchiveRecommender recommender = mock(NewsArchiveRecommender.class);
		mockRecommenderObject(recommender);
		NewsArchiveRecommender.main(new String[] {"CORPUS"});
		verify(recommender, never()).createAndPopulateSearchIndex();
		verify(recommender, atLeastOnce()).generateCorpusStats();
		verify(recommender, never()).generateArticleRecommendations();
		verify(recommender, never()).extractQuestionsFromArticles();
	}
	
	@Test
	public void testArticleRecommendationCorrectlyCalled() throws Exception {
		NewsArchiveRecommender recommender = mock(NewsArchiveRecommender.class);
		mockRecommenderObject(recommender);
		NewsArchiveRecommender.main(new String[] {"RECOMMEND"});
		verify(recommender, never()).createAndPopulateSearchIndex();
		verify(recommender, never()).generateCorpusStats();
		verify(recommender, atLeastOnce()).generateArticleRecommendations();
		verify(recommender, never()).extractQuestionsFromArticles();
	}
	
	@Test
	public void testQuestionGenerationCorrectlyCalled() throws Exception {
		NewsArchiveRecommender recommender = mock(NewsArchiveRecommender.class);
		mockRecommenderObject(recommender);
		NewsArchiveRecommender.main(new String[] {"QUESTION"});
		verify(recommender, never()).createAndPopulateSearchIndex();
		verify(recommender, never()).generateCorpusStats();
		verify(recommender, never()).generateArticleRecommendations();
		verify(recommender, atLeastOnce()).extractQuestionsFromArticles();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCommandLineArgNotPresent() throws Exception {
		
		mockRecommenderObject(Mockito.mock(NewsArchiveRecommender.class));
		NewsArchiveRecommender.main(new String[] {});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCommandLineArgTooMany() throws Exception {

		mockRecommenderObject(Mockito.mock(NewsArchiveRecommender.class));
		NewsArchiveRecommender.main(new String[] {"INDEX", "RECOMMEND"});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCommandLineArgIllegal() throws Exception {

		mockRecommenderObject(Mockito.mock(NewsArchiveRecommender.class));
		NewsArchiveRecommender.main(new String[] {"RUBBISH"});
	}
	
	private void mockRecommenderObject(NewsArchiveRecommender recommender) throws Exception {
		Field reader = NewsArchiveRecommender.class.getDeclaredField("nar");
		reader.setAccessible(true);
		reader.set(new NewsArchiveRecommender(), recommender);
	}
}
