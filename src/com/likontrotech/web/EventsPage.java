package com.likontrotech.web;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.likontrotech.ejb.Utils;
import com.likontrotech.ejb.entities.CommercialDocument;
import com.likontrotech.ejb.entities.CommercialDocumentType;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.Event;
import com.likontrotech.ejb.entities.Representative;

public class EventsPage extends BasePage {
	public EventsPage() {

		final IModel<List<Object[]>> modelEvents = new LoadableDetachableModel<List<Object[]>>() {
			@Override
			protected List<Object[]> load() {
				return eventLocal.findAllOfCompany(getLikontrotechCRMSession()
						.getCompany());
			}
		};

		final IModel<Representative> modelRepresentative = new LoadableDetachableModel<Representative>() {
			@Override
			protected Representative load() {
				return representativeLocal
						.getRepresentativeOfCompany(getLikontrotechCRMSession()
								.getCompany());
			}
		};

		add(new Label("firm", getLikontrotechCRMSession().getCompany()
				.getName()));
		add(new Label("address", getLikontrotechCRMSession().getCompany()
				.getAddress()));

		add(new Label("representativeName", modelRepresentative.getObject()
				.getName()));
		add(new Label("representativeSurname", modelRepresentative.getObject()
				.getSurname()));
		add(new Label("cellular", modelRepresentative.getObject().getCellular()));
		add(new Label("phone1", modelRepresentative.getObject().getPhone1()));
		add(new Label("phone2", modelRepresentative.getObject().getPhone2()));
		add(new Label("phone3", modelRepresentative.getObject().getPhone3()));
		add(new Label("mail", modelRepresentative.getObject().getMail()));

		add(new Label("companyCode", getLikontrotechCRMSession().getCompany()
				.getCompanyCode()));
		add(new Label("vatCode", getLikontrotechCRMSession().getCompany()
				.getVatCode()));
		add(new ExternalLink("site", "http://"
				+ getLikontrotechCRMSession().getCompany().getSite(),
				getLikontrotechCRMSession().getCompany().getSite()));
		add(new Label("postIndex", getLikontrotechCRMSession().getCompany()
				.getPostIndex()));
		add(new Label("description", getLikontrotechCRMSession().getCompany()
				.getDescription()));
		add(new Label("type", getLikontrotechCRMSession().getCompany()
				.getType().getName()));
		add(new Link<Event>("firms") {
			@Override
			public void onClick() {
				setResponsePage(CompaniesPage.class);
			}
		});

		add(new Link<Company>("newEvent") {
			@Override
			public void onClick() {
				Event event = new Event();
				event.setRepresentative(representativeLocal
						.getRepresentativeOfCompany(getLikontrotechCRMSession()
								.getCompany()));
				event.setEventTime(new Timestamp(System.currentTimeMillis()));
				getLikontrotechCRMSession().setEvent(event);
				setResponsePage(EventEditPage.class);
			}
		});

		add(new Link<Company>("representativesLink") {
			@Override
			public void onClick() {
				getLikontrotechCRMSession()
						.setRepresentative(
								representativeLocal
										.getRepresentativeOfCompany(getLikontrotechCRMSession()
												.getCompany()));
				setResponsePage(RepresentativesPage.class);
			}
		});

		add(new Link<Company>("edit") {
			@Override
			public void onClick() {
				setResponsePage(FirmEditPage.class);
			}
		});

		add(new Link<Company>("delete") {
			@Override
			public void onClick() {
				companyLocal.deleteCompany(getLikontrotechCRMSession()
						.getCompany());
				setResponsePage(CompaniesPage.class);
			}
		});

		add(new ListView<Object[]>("eventsList", modelEvents) {
			@Override
			protected void populateItem(ListItem<Object[]> item) {
				Object[] tuple = item.getModelObject();

				item.add(new Label("eventTime", ConstantsUtil.sdf
						.format((java.util.Date) tuple[1])));
				Link<Object[]> link = new Link<Object[]>("edit",
						item.getModel()) {
					@Override
					public void onClick() {
						Object[] tuple1 = getModelObject();

						long eventId = new BigInteger(String.valueOf(tuple1[0]))
								.longValue();
						Event event = eventLocal.find(eventId);
						getLikontrotechCRMSession().setEvent(event);
						setResponsePage(EventEditPage.class);
					}
				};

				link.add(new Label("description", 50 < String.valueOf(tuple[2])
						.length() ? String.valueOf(tuple[2]).substring(0, 50)+"..."
						: String.valueOf(tuple[2])));
				item.add(link);

				item.add(new Label("rname", null == tuple[3] ? "" : String
						.valueOf(tuple[3])));
				item.add(new Label("rsurname", null == tuple[4] ? "" : String
						.valueOf(tuple[4])));

				item.add(new Link<Object[]>("delete", item.getModel()) {
					@Override
					public void onClick() {
						Object[] tuple1 = getModelObject();
						eventLocal.remove(new BigInteger(String
								.valueOf(tuple1[0])).longValue());
						modelEvents.detach();
					}
				});
			}
		});
	}
}
