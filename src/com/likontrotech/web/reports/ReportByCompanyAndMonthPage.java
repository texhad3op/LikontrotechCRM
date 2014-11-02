package com.likontrotech.web.reports;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.likontrotech.ejb.entities.Event;
import com.likontrotech.web.BasePage;
import com.likontrotech.web.CompaniesPage;

public class ReportByCompanyAndMonthPage extends BasePage {
	public ReportByCompanyAndMonthPage() {
		
		
		final List<String> months = new ArrayList<String>();
		months.add("2010-09");
		months.add("2010-10");		
		months.add("2010-11");		
		months.add("2010-12");	
		months.add("2011-01");		
		months.add("2011-02");		
		months.add("2011-03");			
		months.add("2011-04");	
		months.add("2011-05");		
		months.add("2011-06");		
		months.add("2011-07");		
		months.add("2011-08");	
		months.add("2011-09");			
		months.add("2011-10");	
		months.add("2011-11");			
		months.add("2011-12");			
		final IModel<List<Object[]>> eventsCompaniesModel = new LoadableDetachableModel<List<Object[]>>() {
			@Override
			protected List<Object[]> load() {
				return companyLocal.yearReport(months);
			}
		};		
		
		add(new ListView<Object[]>("eventsList", eventsCompaniesModel) {
			@Override
			protected void populateItem(ListItem<Object[]> item) {
				Object[] tuple = item.getModelObject();
				item.add(new Label("company", String.valueOf(tuple[1])));
				item.add(getLink(item, "2010-09", 2));
				item.add(getLink(item, "2010-10", 3));				
				item.add(getLink(item, "2010-11", 4));				
				item.add(getLink(item, "2010-12", 5));
				item.add(getLink(item, "2011-01", 6));				
				item.add(getLink(item, "2011-02", 7));				
				item.add(getLink(item, "2011-03", 8));		
				item.add(getLink(item, "2011-04", 9));
				item.add(getLink(item, "2011-05", 10));				
				item.add(getLink(item, "2011-06", 11));		
				item.add(getLink(item, "2011-07", 12));				
				item.add(getLink(item, "2011-08", 13));
				item.add(getLink(item, "2011-09", 14));				
				item.add(getLink(item, "2011-10", 15));
				item.add(getLink(item, "2011-11", 16));				
				item.add(getLink(item, "2011-12", 17));				
			}
		});				
		
		add(new Link<Event>("firms") {
			@Override
			public void onClick() {
				setResponsePage(CompaniesPage.class);
			}
		});
	
	}
	
	private Link<Object[]> getLink(ListItem<Object[]> item, final String date, int i){
		Object[] tuple = item.getModelObject();		
		Link<Object[]> link = new Link<Object[]>(date+"link",item.getModel()) {
			@Override
			public void onClick() {
				Object[] t = getModelObject();
				getLikontrotechCRMSession().setCompany(companyLocal.find(Long.parseLong(String.valueOf(t[0]))));
				getLikontrotechCRMSession().setDate(date);				
				setResponsePage(ReportOneMonthPage.class);
			}
		};
		String cnt = String.valueOf(tuple[i]);
		link.add(new Label(date+"label", "0".equals(cnt)?"":cnt));	
		item.add(link);
		return link;
	}
}

