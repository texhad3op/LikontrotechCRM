package com.likontrotech.web;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.joda.time.format.DateTimeFormatter;

import com.likontrotech.ejb.Utils;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.CompanyType;
import com.likontrotech.ejb.entities.Representative;

public class FirmEditPage extends BasePage {

	CompanyType type;

	public FirmEditPage() {

		setDefaultModel(new Model<Company>() {
			@Override
			public Company getObject() {
				return getLikontrotechCRMSession().getCompany();
			}
		});

		final IModel<Representative> modelRepresentative = new LoadableDetachableModel<Representative>() {
			@Override
			protected Representative load() {
				return representativeLocal.getRepresentativeOfCompany(getLikontrotechCRMSession().getCompany());
			}
		};

		final IModel<List<Representative>> modelRepresentatives = new LoadableDetachableModel<List<Representative>>() {
			@Override
			protected List<Representative> load() {
				return representativeLocal.getRepresentativesOfCompany(getLikontrotechCRMSession().getCompany());
			}
		};

		final TextField<String> name = new TextField<String>("name", new PropertyModel<String>(getDefaultModel(), "name"));
		final TextArea<String> description = new TextArea<String>("description",
				new PropertyModel<String>(getDefaultModel(), "description"));

		final TextField<String> site = new TextField<String>("site", new PropertyModel<String>(getDefaultModel(), "site"));

		final TextField<String> postIndex = new TextField<String>("postIndex", new PropertyModel<String>(getDefaultModel(), "postIndex"));

		final TextField<String> address = new TextField<String>("address", new PropertyModel<String>(getDefaultModel(), "address"));

		final TextField<String> companyCode = new TextField<String>("companyCode", new PropertyModel<String>(getDefaultModel(),
				"companyCode"));

		final TextField<String> vatCode = new TextField<String>("vatCode", new PropertyModel<String>(getDefaultModel(), "vatCode"));

		final DateTextField dateTextField = new DateTextField("registered", new PropertyModel(getDefaultModel(), "registered"),
				new DateConverter(true) {

					@Override
					public DateTimeFormatter getFormat() {
						return null;// return DateTimeFormatter.
									// forPattern(getDatePattern());
					}

					@Override
					public String getDatePattern() {
						return "yyyy-MM-dd";
					}

					public Date convertToObject(String value, Locale locale) {
						Timestamp ts = new Timestamp(System.currentTimeMillis());
						Timestamp ts2 = java.sql.Timestamp.valueOf(value + " " + ts.getHours() + ":" + ts.getMinutes() + ":"
								+ ts.getSeconds());
						return ts2;
					}

					public String convertToString(Object value, Locale locale) {
						return value.toString().substring(0, 10);
					}
				}) {
			@Override
			public Locale getLocale() {
				return ConstantsUtil.getLocaleLt();
			}
		};

		List<Boolean> categories = Arrays.asList(Boolean.TRUE, Boolean.FALSE);

		DropDownChoice<CompanyType> dropDownChoiceTypes = new DropDownChoice<CompanyType>("type", new PropertyModel(getDefaultModel(),
				"type"), Utils.companyTypes, new ChoiceRenderer("name", "id"));

		final DropDownChoice<Representative> dropDownChoiceRepresentatives = new DropDownChoice<Representative>("representative", 
				modelRepresentative, modelRepresentatives,  new ChoiceRenderer("fullName", "id"));

		Form<Company> form = new Form<Company>("form") {
			@Override
			protected void onSubmit() {
				if (null == getLikontrotechCRMSession().getCompany().getId()) {
					Company cModel = (Company) FirmEditPage.this.getDefaultModel().getObject();
					companyLocal.create(cModel);
					Representative r = new Representative();
					r.setName("");
					r.setSurname("");
					r.setIsdefault(true);
					r.setCompany(cModel);
					representativeLocal.create(r);						
				} else {
					Company cModel = (Company) FirmEditPage.this.getDefaultModel().getObject();
					representativeLocal.setRepresentativeAsDefault(dropDownChoiceRepresentatives.getConvertedInput());
					companyLocal.edit(cModel);
					modelRepresentative.detach();
					modelRepresentatives.detach();
				}
				setResponsePage(EventsPage.class);
			}
		};

		form.add(name);
		form.add(description);
		form.add(site);
		form.add(address);
		form.add(companyCode);
		form.add(vatCode);
		form.add(postIndex);
		form.add(dropDownChoiceTypes);
		form.add(dropDownChoiceRepresentatives);
		name.setRequired(true);
		dateTextField.add(new DatePicker());
		form.add(dateTextField);
		form.add(new Link<Company>("representativesLink") {
			@Override
			public void onClick() {
				setResponsePage(RepresentativesPage.class);
			}
		});	

		add(form);
		add(new Link<Company>("firm") {
			@Override
			public void onClick() {
				if(null == getLikontrotechCRMSession().getCompany().getId()) setResponsePage(CompaniesPage.class);
				else setResponsePage(EventsPage.class);
			}
		});
	}

	public LikontrotechCRMSession getLikontrotechCRMSession() {
		return (LikontrotechCRMSession) getSession();
	}
}
