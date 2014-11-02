package com.likontrotech.web.reports;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.likontrotech.ejb.entities.Event;
import com.likontrotech.web.BasePage;
import com.likontrotech.web.ConstantsUtil;

public class ReportOneMonthPage extends BasePage{
	public ReportOneMonthPage() {
		final IModel modelEvents = new LoadableDetachableModel() {
			@Override
			protected Object load() {
				return eventLocal.findAllOfCompany(getLikontrotechCRMSession().getCompany(), getLikontrotechCRMSession().getDate());
			}
		};

		add(new Label("firm", getLikontrotechCRMSession().getCompany().getName()));
		add(new Label("address", getLikontrotechCRMSession().getCompany().getAddress()));	
		add(new Label("companyCode", getLikontrotechCRMSession().getCompany().getCompanyCode()));
		add(new Label("vatCode", getLikontrotechCRMSession().getCompany().getVatCode()));		
		add(new ExternalLink("site", "http://"+getLikontrotechCRMSession().getCompany().getSite(), getLikontrotechCRMSession().getCompany().getSite()));
		
		add(new Label("postIndex", getLikontrotechCRMSession().getCompany().getPostIndex()));			
		add(new Label("description", getLikontrotechCRMSession().getCompany().getDescription()));
		add(new Label("type", getLikontrotechCRMSession().getCompany().getType().getName()));	
		add(new Label("date", getLikontrotechCRMSession().getDate()));
		add(new Link<Event>("home") {
			@Override
			public void onClick() {
				setResponsePage(ReportByCompanyAndMonthPage.class);
			}
		});
	
		
		
		add(new ListView<Event>("eventsList", modelEvents) {
			@Override
			protected void populateItem(ListItem<Event> item) {
				Event event = item.getModelObject();
				item.add(new Label("eventTime", ConstantsUtil.sdf.format((java.util.Date) event.getEventTime())));				
				Link<Event> link = new Link<Event>("edit", item.getModel()) {
					@Override
					public void onClick() {
						Event even = getModelObject();
						getLikontrotechCRMSession().setEvent(even);						
						setResponsePage(EventViewPage.class);
					}
				};

				link.add(new Label("description", event.getDescription()));
				item.add(link);
			}
		});		
	}
}
