package com.wyamee.nlp;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CorpusStatsTest {

	private class TestCorpusStats extends CorpusStats {
		
		protected File loadFileFromResources() {
			ClassLoader classLoader = getClass().getClassLoader();
			return new File(classLoader.getResource("models/test-corpus-stats.csv").getFile());
		}
	}
	
	@Test
	public void testInstanceCount() {
		CorpusStats stats = new TestCorpusStats();
		assertEquals(stats.getInstanceCount("a"), 1);
		assertEquals(stats.getInstanceCount("c"), 3);
		assertEquals(stats.getInstanceCount("e"), 5);
	}
	
	@Test
	public void testIDF() {
		CorpusStats stats = new TestCorpusStats();
		assertEquals(stats.getIDF("a"), 9.1D, 0.001);
		assertEquals(stats.getIDF("c"), 7.3, 0.001);
		assertEquals(stats.getIDF("e"), 5.5, 0.001);
	}
	
	@Test
	public void testMaxIDF() {
		CorpusStats stats = new TestCorpusStats();
		assertEquals(stats.getMaxIDF(), 9.1D, 0.001);
	}
}
