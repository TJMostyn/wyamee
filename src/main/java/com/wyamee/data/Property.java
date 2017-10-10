package com.wyamee.data;

import javax.xml.bind.annotation.XmlAttribute;

public class Property {
	
	private String formalName;
	private String value;
	
	public String getFormalName() {
		return formalName;
	}
	
	@XmlAttribute(name="FormalName")
	public void setFormalName(String formalName) {
		this.formalName = formalName;
	}
	
	public String getValue() {
		return value;
	}

	@XmlAttribute(name="Value")
	public void setValue(String value) {
		this.value = value;
	}
}
