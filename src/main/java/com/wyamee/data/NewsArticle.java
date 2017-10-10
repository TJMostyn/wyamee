package com.wyamee.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="NLAML")
public class NewsArticle {

	private SourceMetaData sourceMetaData;
	private Article article;

	public SourceMetaData getSourceMetaData() {
		return sourceMetaData;
	}

	@XmlElement(name="SourceMetaData")
	public void setSourceMetaData(SourceMetaData sourceMetaData) {
		this.sourceMetaData = sourceMetaData;
	}

	public Article getArticle() {
		return article;
	}

	@XmlElement(name="Article")
	public void setArticle(Article article) {
		this.article = article;
	}
}
