package com.wyamee.data;

import javax.xml.bind.annotation.XmlElement;

public class Photo {

	private String caption;

	public String getCaption() {
		return caption;
	}

	@XmlElement(name="Caption")
	public void setCaption(String caption) {
		this.caption = caption;
	}
}
