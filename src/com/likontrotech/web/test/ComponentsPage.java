package com.likontrotech.web.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import com.likontrotech.ejb.entities.Company;

public class ComponentsPage extends WebPage
{
	List<Component> components;
	public ComponentsPage()
	{
		
		Form<Company> form = new Form<Company>("form") {
			@Override
			protected void onSubmit() {
				for(Component component:components){
					System.out.println("vals:"+component.getSubId()+"    "+component.getValue());
				}
			}
		};		
		
		components = new ArrayList<Component>();
//		components.add( new Component("aa") );
//		components.add( new Component("bb") );
		form.add( new ListView( "components", components )
		{			
			protected void populateItem(ListItem item)
			{
				item.add((Component)item.getModelObject() );
			}
		});
		add(form);
		
	}
}
