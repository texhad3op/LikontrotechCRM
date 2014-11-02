package com.likontrotech.web.reports;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

import com.likontrotech.ejb.entities.Event;
import com.likontrotech.web.BasePage;

public class EventViewPage extends BasePage {

	public EventViewPage() {

		setDefaultModel(new Model<Event>() {
			@Override
			public Event getObject() {
				return getLikontrotechCRMSession().getEvent();
			}
		});
		Event event = ((Event)EventViewPage.this.getDefaultModel().getObject());
		add(new Label("state", event.getType().toString()));
		add(new Label("description", event.getDescription()));
		add(new Label("date", String.valueOf(event.getEventTime())));		

		
		add(new Link("back") {
			@Override
			public void onClick() {
				setResponsePage(ReportOneMonthPage.class);
			}
		});

	}
}
