package com.likontrotech.web.test;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.likontrotech.ejb.entities.Attribute;
import com.likontrotech.ejb.entities.AttributeValue;

public class Component extends Panel {
	TextField textField; 
	String subId;
	Attribute attribute;
	AttributeValue attributeValue;
	
	public Component(Attribute attribute, AttributeValue attributeValue) {
		super("component");
		this.attribute = attribute;
		this.attributeValue = attributeValue;
		subId = attribute.getName();
		add(textField = new TextField("name", new Model(null == attributeValue?"":attributeValue.getValue())));
		add(new Label("nameLabel", subId));

	}
	
	public String getValue(){
		return (String)textField.getConvertedInput();
	}

	public String getSubId(){
		return subId;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public AttributeValue getAttributeValue() {
		return attributeValue;
	}

}
