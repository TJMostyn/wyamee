package com.wyamee.data;

import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;

public class DescriptiveMetaData {

	private enum PropertyNames {
		
		WORD_COUNT("Wordcount"),
		RIGHTS_STATUS("Rights_Status"),
		NLA_ARTICLE_ID("NLAArticleID");
		
		private String xmlName;
		
		private PropertyNames(String xmlName) {
			this.xmlName = xmlName;
		}
		
		private String getXmlName() {
			return xmlName;
		}
		
		public static PropertyNames load(String xmlName) {
			for (PropertyNames propertyName : PropertyNames.values()) {
				if (xmlName.equalsIgnoreCase(propertyName.getXmlName())) {
					return propertyName;
				}
			}
			throw new IllegalArgumentException("Unknown property name");
		}
	}
	
	private String headline;
	private String subHeadline;
	private String byline;
	private String author;
	private int wordCount;
	private int rightsStatus;
	private long nlaArticleId;
	private List<Property> properties;
	
	public String getHeadline() {
		return headline;
	}

	@XmlElement(name="HeadLine")
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	
	public String getSubHeadline() {
		return subHeadline;
	}

	@XmlElement(name="SubHeadLine")
	public void setSubHeadline(String subHeadline) {
		this.subHeadline = subHeadline;
	}
	
	public String getByline() {
		return byline;
	}

	@XmlElement(name="ByLine")
	public void setByline(String byline) {
		this.byline = byline;
	}
	
	public String getAuthor() {
		return author;
	}

	@XmlElement(name="Author")
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public int getWordCount() {
		return wordCount;
	}
	
	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
	
	public int getRightsStatus() {
		return rightsStatus;
	}
	
	public void setRightsStatus(int rightsStatus) {
		this.rightsStatus = rightsStatus;
	}
	
	public long getNlaArticleId() {
		return nlaArticleId;
	}
	
	public void setNlaArticleId(long nlaArticleId) {
		this.nlaArticleId = nlaArticleId;
	}
	
	public List<Property> getProperties() {
		return properties;
	}

	@XmlElement(name="Property")
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	
	public void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		
		for (Property property : properties) {
			PropertyNames propertyName = PropertyNames.load(property.getFormalName());
			switch (propertyName) {
				case WORD_COUNT:
					wordCount = Integer.parseInt(property.getValue());
					break;
				case RIGHTS_STATUS:
					rightsStatus = Integer.parseInt(property.getValue());
					break;
				case NLA_ARTICLE_ID:
					nlaArticleId = Long.parseLong(property.getValue());
					break;
				default:
					break;
			}
		}
	}
}