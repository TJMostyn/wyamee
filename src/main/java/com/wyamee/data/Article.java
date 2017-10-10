package com.wyamee.data;

import javax.xml.bind.annotation.XmlElement;

public class Article {

	private DescriptiveMetaData descriptiveMetaData;
	private DataContent dataContent;
	
	public DescriptiveMetaData getDescriptiveMetaData() {
		return descriptiveMetaData;
	}

	@XmlElement(name="DescriptiveMetadata")
	public void setDescriptiveMetaData(DescriptiveMetaData descriptiveMetaData) {
		this.descriptiveMetaData = descriptiveMetaData;
	}
	
	public DataContent getDataContent() {
		return dataContent;
	}

	@XmlElement(name="DataContent")
	public void setDataContent(DataContent dataContent) {
		this.dataContent = dataContent;
	}
}
