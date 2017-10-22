package com.wyamee.data.loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.wyamee.data.NewsArticle;

public class NewsArticleLoader implements Iterator<NewsArticle> {

	private JAXBContext jaxBContext;
	private File[] articleFiles;
	private int index;
	
	public NewsArticleLoader(String directory) {
			
		try {
			articleFiles = discoverArticleFiles(directory);
			jaxBContext = JAXBContext.newInstance(NewsArticle.class);
		}
		catch (FileNotFoundException | JAXBException e) {
			throw new NewsArticleLoaderException(directory, e);
		}
	}

	@Override
	public boolean hasNext() {
		return index < articleFiles.length;
	}

	@Override
	public NewsArticle next() {
		File articleName = articleFiles[index++];
		try {
			return loadAndMarshallArticle(articleName);
		}
		catch (JAXBException e) {
			throw new NewsArticleLoaderException(articleName.getName(), e);
		}
	}
	
	public int getNumberArticles() {
		return articleFiles.length;
	}
	
	protected File[] discoverArticleFiles(String directory) throws FileNotFoundException {
		
		File articleDirectory = new File(directory);
		if (! articleDirectory.isDirectory()) {
			throw new FileNotFoundException(directory + " is not a valid directory");
		}
		
		return articleDirectory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".xml");
			}
		});
	}
	
	protected NewsArticle loadAndMarshallArticle(File articleFile) throws JAXBException {
        Unmarshaller unmarshaller = jaxBContext.createUnmarshaller();
        return (NewsArticle) unmarshaller.unmarshal(articleFile);
	}
}
