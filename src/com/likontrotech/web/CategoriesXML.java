package com.likontrotech.web;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.WebPage;

public class CategoriesXML extends WebPage {

	public CategoriesXML() {
		StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<markers>");
		builder.append("<marker lat=\"");
		builder.append("aaa");
		builder.append("\" lng=\"");
		builder.append("bbb");
		builder.append("\"");
		builder.append(" address=\"");
		builder.append("adr");
		builder.append("\" category=\"branch\" name=\"");
		builder.append("nm");
		builder.append("\"/>");

		builder.append("</markers>");

		RequestCycle.get().getResponse().setContentType("text/xml");
		OutputStream os = RequestCycle.get().getResponse().getOutputStream();
		try {
			os.write(builder.toString().getBytes());
			os.close();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
