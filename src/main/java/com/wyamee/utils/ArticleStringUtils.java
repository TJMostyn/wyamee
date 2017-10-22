package com.wyamee.utils;

import com.wyamee.data.NewsArticle;

public class ArticleStringUtils {

	public static String getConcatenatedDocumentText(NewsArticle article, boolean toLower) {
		
		String headline = article.getArticle().getDescriptiveMetaData().getHeadline();
		if (toLower) headline = headline.toLowerCase();
		
		String subHeadline = article.getArticle().getDescriptiveMetaData().getSubHeadline();
		if (toLower) subHeadline = subHeadline.toLowerCase();
		
		String content = article.getArticle().getDataContent().getBody();
		if (toLower) content = content.toLowerCase();
		
		StringBuilder builder = new StringBuilder();
		builder.append(headline);
		builder.append(". ");
		builder.append(subHeadline);
		builder.append(". ");
		builder.append(content);
		builder.append(". ");
		return builder.toString();
	}
}
