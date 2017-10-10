package com.wyamee.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;

public class SourceMetaData {
	
	private enum PropertyNames {
		
		PUBLICATION_NAME("Publication_Name"),
		PUBLICATION_ACRONYM("Publication_Acronym"),
		PUBLICATION_SUBSOURCE("Publication_SubSource"),
		PUBLICATION_EDITION("Edition"),
		PUBLICATION_REGION("Region"),
		PUBLICATION_DATE("Publication_Date"),
		PUBLICATION_DAY("Day"),
		PAGE_SECTION("Page_Section"),
		PAGE_NUMBERS("Page_Numbers"),
		ARTICLE_AREA("Article_Area"),
		ORIGIN_LEFT("Origin_left"),
		ORIGIN_TOP("Origin_top"),
		WIDTH("Width"),
		HEIGHT("Height"),
		ARTICLE_PERCENTAGE("Article_Percentage");
		
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

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private List<Property> properties;
	private String publicationName;
	private String publicationAcronym;
	private String publicationSubSource;
	private String edition;
	private String region;
	private Date publicationDate;
	private String day;
	private String pageSection;
	private int[] pageNumbers;
	private int[] articleArea;
	private int[] articlePercentage;

	public String getPublicationName() {
		return publicationName;
	}

	public String getPublicationAcronym() {
		return publicationAcronym;
	}

	public String getPublicationSubSource() {
		return publicationSubSource;
	}

	public String getEdition() {
		return edition;
	}

	public String getRegion() {
		return region;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public String getDay() {
		return day;
	}

	public String getPageSection() {
		return pageSection;
	}

	public int[] getPageNumbers() {
		return pageNumbers;
	}

	public int[] getArticleArea() {
		return articleArea;
	}

	public int[] getArticlePercentage() {
		return articlePercentage;
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
				case PUBLICATION_NAME:
					publicationName = property.getValue();
					break;
				case PUBLICATION_ACRONYM:
					publicationAcronym = property.getValue();
					break;
				case PUBLICATION_SUBSOURCE:
					publicationSubSource = property.getValue();
					break;
				case PUBLICATION_EDITION:
					edition = property.getValue();
					break;
				case PUBLICATION_REGION:
					region = property.getValue();
					break;
				case PUBLICATION_DATE:
					try {
						publicationDate = dateFormat.parse(property.getValue());
					}
					catch (ParseException e) {
						System.out.println("Unable to parse date with format: " + property.getValue());
					}
					break;
				case PUBLICATION_DAY:
					day = property.getValue();
					break;
				case PAGE_SECTION:
					pageSection = property.getValue();
					break;
				case PAGE_NUMBERS:
					pageNumbers = createIntArrayFromString(property.getValue());
					break;
				case ARTICLE_AREA:
					articleArea = createIntArrayFromString(property.getValue());
					break;
				case ARTICLE_PERCENTAGE:
					articlePercentage = createIntArrayFromString(property.getValue());
					break;
				default:
					break;
			}
		}
	}
	
	private int[] createIntArrayFromString(String numbersAsString) {
		String[] parts = numbersAsString.split(",");
		int[] numberArray = new int[2];
		if (parts.length == 2) {
			numberArray[0] = Integer.parseInt(parts[0]);
			numberArray[1] = Integer.parseInt(parts[1]);
		}
		return numberArray;
	}
}
