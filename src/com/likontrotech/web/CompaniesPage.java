package com.likontrotech.web;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.likontrotech.ejb.CompanyLocal;
import com.likontrotech.ejb.Utils;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.CompanyType;
import com.likontrotech.ejb.entities.Representative;

public class CompaniesPage extends BasePage {

	boolean companiesFound = true;

	
	
	class ContactDataProvider implements IDataProvider<Object[]>{

	    public Iterator<Object[]> iterator(int first, int count)
	    {
	        return companyLocal.getList(companyName, companyType,first, count).iterator();
	    }


	    public int size()
	    {
	        return companyLocal.getCount(companyName, companyType);
	    }


	    public IModel<Object[]> model(Object[] object)
	    {
	        return new Model(object);
	    }

	    public void detach()
	    {
	    }

	}	
	
	
	
	@SuppressWarnings("all")
	public CompaniesPage() {
		DataView<Object[]> dataView = new DataView<Object[]>("companiesList", new ContactDataProvider()) {
			@Override
			protected void populateItem(final Item<Object[]> item) {
				Object[] tuple = item.getModelObject();

				Link<Object[]> link = new Link<Object[]>("namelink", item.getModel()) {
					@Override
					public void onClick() {
						Object[] t = getModelObject();
						BigInteger bi = (BigInteger) t[0];
						Company cc = companyLocal.find(bi.longValue());
						Representative representative = representativeLocal.getRepresentativeOfCompany(cc);
						getLikontrotechCRMSession().setCompany(cc);
						getLikontrotechCRMSession().setRepresentative(representative);
						setResponsePage(EventsPage.class);
					}
				};
				link.add(new Label("namelabel", (String) tuple[1]));
				item.add(link);

				Link<Object[]> linkLastEvent = new Link<Object[]>("lasteventlink", item.getModel()) {
					@Override
					public void onClick() {
						Object[] t2 = getModelObject();
						Long cid = ((BigInteger) t2[0]).longValue();
						Company cc = companyLocal.find(cid);
						getLikontrotechCRMSession().setCompany(cc);
						getLikontrotechCRMSession().setEvent(eventLocal.getLastEvent(cc));
						setResponsePage(EventEditPage.class);
					}
				};
				String lastEventLabel = null == tuple[2] ? "" : (String) tuple[2];
				linkLastEvent.add(new Label("lasteventlabel", lastEventLabel));
				item.add(linkLastEvent);
				String lastEventDate = null == tuple[3] ? "" : ConstantsUtil.sdf.format(tuple[3]);
				item.add(new Label("lastEventDate", lastEventDate));
			}
		};		
		
		
		dataView.setItemsPerPage(50);
		add(dataView);

		add(new PagingNavigator("navigator", dataView));
		
		add(new Link<Company>("newFirm") {
			@Override
			public void onClick() {
				Company company = new Company();
				company.setRegistered(new Timestamp(System.currentTimeMillis()));
				getLikontrotechCRMSession().setCompany(company);
				getLikontrotechCRMSession().setRepresentative(null);
				setResponsePage(FirmEditPage.class);
			}

		});

		add(new Label("companiesFound", "Nėra įmonų atitinkančių jūsų užklausai") {
			@Override
			public boolean isVisible() {
				return !companiesFound;
			}
		});

		final IModel modelScheduledEvents = new LoadableDetachableModel() {
			@Override
			protected Object load() {
				return eventLocal.findAllScheduledEvents();
			}
		};

		add(new ListView<Object[]>("scheduledEventsList", modelScheduledEvents) {
			@Override
			protected void populateItem(ListItem<Object[]> item) {
				Object[] tuple = item.getModelObject();

				final BigInteger eventId = (BigInteger) tuple[0];
				final java.util.Date d = new java.util.Date(((Timestamp) tuple[1]).getTime());
				item.add(new Label("data", ConstantsUtil.sdf.format(d)));
				final String companyName = (String) tuple[2];
				final String eventDescription = (String) tuple[3];
				final BigInteger companyId = (BigInteger) tuple[4];
				Link<Object[]> linkCompany = new Link<Object[]>("companylink", item.getModel()) {
					@Override
					public void onClick() {
						getLikontrotechCRMSession().setCompany(companyLocal.find(companyId.longValue()));
						setResponsePage(EventsPage.class);
					}
				};
				linkCompany.add(new Label("companylabel", companyName));
				item.add(linkCompany);

				Link<Object[]> linkEvent = new Link<Object[]>("namelink", item.getModel()) {
					@Override
					public void onClick() {
						getLikontrotechCRMSession().setCompany(companyLocal.find(companyId.longValue()));
						getLikontrotechCRMSession().setEvent(eventLocal.find(eventId.longValue()));
						setResponsePage(EventEditPage.class);
					}
				};
				linkEvent.add(new Label("namelabel", eventDescription));
				item.add(linkEvent);
			}
		});

		Form<Company> form = new Form<Company>("findCompanyForm") {
			@Override
			protected void onSubmit() {
//				modelCompanies.detach();
			}
		};
		add(form);
		final TextField<String> companyNameText = new TextField<String>("companyName", new PropertyModel<String>(this, "companyName"));
		form.add(companyNameText);

		DropDownChoice<CompanyType> dd2 = new DropDownChoice<CompanyType>("companyType", new PropertyModel(this, "companyType"),
				Utils.companyTypes, new ChoiceRenderer("name", "id"));
		dd2.setNullValid(true);
		form.add(dd2);
	}

	CompanyType companyType;
	String companyName;
}
