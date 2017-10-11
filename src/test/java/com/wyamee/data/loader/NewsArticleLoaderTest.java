package com.wyamee.data.loader;

import org.junit.Test;

import com.wyamee.data.NewsArticle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBException;

public class NewsArticleLoaderTest {

	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	
	private static class NewsArticleLoaderMock extends NewsArticleLoader {
		
		public static int iterations;
		
		public NewsArticleLoaderMock() {
			super("");
		}

		protected File[] discoverArticleFiles(String directory) throws FileNotFoundException  {
			File[] files = new File[iterations];
			for (int i = 0; i < iterations; i++) {
				files[i] = new File("");
			}
			return files;
		}
	}
	
	private static class NewsArticleLoaderAllMock extends NewsArticleLoaderMock {
		
		protected NewsArticle loadAndMarshallArticle(File articleFile) throws JAXBException {
			return null;
		}
	}
	
	@Test
	public void testIteration() {
		int iterations = 10;
		NewsArticleLoaderMock.iterations = iterations;
		NewsArticleLoaderAllMock mock = new NewsArticleLoaderAllMock();
		
		int counter = 0;
		while (mock.hasNext()) {
			mock.next();
			counter++;
		}
		assertEquals(counter, iterations);
	}
	
	@Test
	public void testXMLMarshalling() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("articles/unmarshalling_test.xml").getFile());
		NewsArticleLoaderMock mock = new NewsArticleLoaderMock();
		NewsArticle article = mock.loadAndMarshallArticle(file);

		String headline = "'One of the reasons I am enjoying my retirement is because I have "
			+ "not dived into anything yet'";
		String subheadline = "Paul O'Connell talks teamwork, Ireland's bright future and why "
			+ "his next battle can wait a while";
		
		assertEquals(article.getSourceMetaData().getPublicationName(), "The Sunday Times");
		assertEquals(article.getSourceMetaData().getPublicationAcronym(), "STS");
		assertEquals(article.getSourceMetaData().getPublicationSubSource(), "Sport");
		assertEquals(article.getSourceMetaData().getEdition(), "01");
		assertEquals(article.getSourceMetaData().getRegion(), "ulster");
		assertEquals(article.getSourceMetaData().getPublicationDate(), format.parse("20161009"));
		assertEquals(article.getSourceMetaData().getDay(), "Sunday");
		assertEquals(article.getSourceMetaData().getPageSection(), "Sport");
		assertArrayEquals(article.getSourceMetaData().getPageNumbers(), new int[] {4, 5});
		assertArrayEquals(article.getSourceMetaData().getArticleArea(), new int[] {1093, 861});
		assertArrayEquals(article.getSourceMetaData().getArticlePercentage(), new int[] {56, 43});
		
		assertEquals(article.getArticle().getDescriptiveMetaData().getAuthor(), "David Walsh");
		assertEquals(article.getArticle().getDescriptiveMetaData().getByline(), "David Walsh CHIEF SPORTS WRITER");
		assertEquals(article.getArticle().getDescriptiveMetaData().getHeadline(), headline);
		assertEquals(article.getArticle().getDescriptiveMetaData().getNlaArticleId(), 117975792L);
		assertEquals(article.getArticle().getDescriptiveMetaData().getRightsStatus(), 0);
		assertEquals(article.getArticle().getDescriptiveMetaData().getSubHeadline(), subheadline);
		assertEquals(article.getArticle().getDescriptiveMetaData().getWordCount(), 1740);
		
		assertEquals(article.getArticle().getDataContent().getBody().split("\\.")[0].trim(), 
			"<P>There is a paragraph in Paul O'Connell's book that offers a bedside seat to the "
			+ "closing of a sporting life");
		assertEquals(article.getArticle().getDataContent().getPhoto().getCaption(), 
				"Driven: O'Connell presented Europe's players with their shirts");
	}
}
