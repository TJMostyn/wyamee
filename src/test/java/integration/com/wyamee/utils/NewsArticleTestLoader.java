package integration.com.wyamee.utils;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.wyamee.data.NewsArticle;
import com.wyamee.data.loader.NewsArticleLoaderException;
import com.wyamee.utils.PropertiesHelper;

public class NewsArticleTestLoader {

	public static PropertiesHelper properties = PropertiesHelper.getInstance();
	
	public static NewsArticle loadRandomArticle() {
		File directory = new File(properties.getNewsArticleDirectory());
		int index = ThreadLocalRandom.current().nextInt(0, directory.listFiles().length);
		File testFile = directory.listFiles()[index];
		return loadAndMarshallArticle(testFile);
	}
	
	protected static NewsArticle loadAndMarshallArticle(File articleFile) {
		try {
			JAXBContext jaxBContext = JAXBContext.newInstance(NewsArticle.class);
	        Unmarshaller unmarshaller = jaxBContext.createUnmarshaller();
	        return (NewsArticle) unmarshaller.unmarshal(articleFile);
		}
		catch (JAXBException e) {
			throw new NewsArticleLoaderException(articleFile.getName(), e);
		}
	}
}
