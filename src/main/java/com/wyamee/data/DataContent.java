package com.wyamee.data;

import javax.xml.bind.annotation.XmlElement;

public class DataContent {

	private String body;
	private Photo photo;

	public String getBody() {
		return body;
	}

	@XmlElement(name="body")
	public void setBody(String body) {
		this.body = body;
	}

	public Photo getPhoto() {
		return photo;
	}

	@XmlElement(name="Photo")
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
}
