package com.likontrotech.web;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.Representative;

public class RepresentativesPage extends BasePage {
	boolean isEdit = false;

	public RepresentativesPage() {

		final IModel<List<Representative>> modelRepresentatives = new LoadableDetachableModel<List<Representative>>() {
			@Override
			protected List<Representative> load() {
				return representativeLocal.getRepresentativesOfCompany(getLikontrotechCRMSession().getCompany());
			}
		};
		setDefaultModel(new Model<Representative>() {
			@Override
			public Representative getObject() {
				return getLikontrotechCRMSession().getRepresentative();
			}
		});
		Link<Company> l = new Link<Company>("firm") {
			@Override
			public void onClick() {
				setResponsePage(EventsPage.class);
			}
		};
		add(l);
		
		l.add(new Label("firmName", getLikontrotechCRMSession().getCompany().getName()));
		add(new Link<Company>("newRepresentativeLink") {
			@Override
			public void onClick() {
				Representative r = new Representative();
				r.setCompany(getLikontrotechCRMSession().getCompany());
				r.setIsdefault(false);
				getLikontrotechCRMSession().setRepresentative(r);
				isEdit = true;				
			}
		});		
		add(new ListView<Representative>("representativesList", modelRepresentatives) {
			@Override
			protected void populateItem(ListItem<Representative> item) {
				Representative representative = item.getModelObject();
				item.add(new Label("name", null == representative.getName() ? "" : representative.getName()));
				item.add(new Label("surname", null == representative.getSurname() ? "" : representative.getSurname()));
				item.add(new Link<Representative>("setDefault", item.getModel()) {
					@Override
					public void onClick() {
						Representative representative = getModelObject();
						representativeLocal.setRepresentativeAsDefault(representative);
						modelRepresentatives.detach();
					}
					@Override
					public boolean isVisible(){
						Representative representative = getModelObject();	
						return !representative.getIsdefault();
					}
				});				
				item.add(new Link<Representative>("edit", item.getModel()) {
					@Override
					public void onClick() {
						Representative representative = getModelObject();
						getLikontrotechCRMSession().setRepresentative(representative);
						isEdit = true;
					}
				});
				item.add(new Link<Representative>("view", item.getModel()) {
					@Override
					public void onClick() {
						Representative representative = getModelObject();
						getLikontrotechCRMSession().setRepresentative(representative);
						isEdit = false;						
					}
				});				
				item.add(new Link<Representative>("delete", item.getModel()) {
					@Override
					public void onClick() {
						Representative representative = getModelObject();
						representativeLocal.remove(representative.getId());
						modelRepresentatives.detach();

					}
					@Override
					public boolean isVisible(){
						Representative representative = getModelObject();	
						return !representative.getIsdefault();
					}					
				});
			}
		});

		final TextField<String> name = new TextField<String>("name", new PropertyModel<String>(getDefaultModel(), "name"));
		final TextField<String> surname = new TextField<String>("surname", new PropertyModel<String>(getDefaultModel(), "surname"));
		final TextArea<String> description = new TextArea<String>("description",
				new PropertyModel<String>(getDefaultModel(), "description"));
		final TextField<String> phone1 = new TextField<String>("phone1", new PropertyModel<String>(getDefaultModel(), "phone1"));
		final TextField<String> phone2 = new TextField<String>("phone2", new PropertyModel<String>(getDefaultModel(), "phone2"));
		final TextField<String> phone3 = new TextField<String>("phone3", new PropertyModel<String>(getDefaultModel(), "phone3"));
		final TextField<String> mail = new TextField<String>("mail", new PropertyModel<String>(getDefaultModel(), "mail"));
		final TextField<String> cellular = new TextField<String>("cellular", new PropertyModel<String>(getDefaultModel(), "cellular"));
		final TextField<String> fax = new TextField<String>("fax", new PropertyModel<String>(getDefaultModel(), "fax"));

		Form<Representative> form = new Form<Representative>("form") {
			@Override
			protected void onSubmit() {
				if (null == getLikontrotechCRMSession().getCompany().getId()) {
					Representative cRepresentative = (Representative) RepresentativesPage.this.getDefaultModel().getObject();
					representativeLocal.create(cRepresentative);
				} else {
					Representative cRepresentative = (Representative) RepresentativesPage.this.getDefaultModel().getObject();
					representativeLocal.edit(cRepresentative);
				}
				modelRepresentatives.detach();
				isEdit = false;
			}

			@Override
			public boolean isVisible() {
				return isEdit;
			}
		};
		form.add(name);
		form.add(surname);
		form.add(description);
		form.add(phone1);
		form.add(phone2);
		form.add(phone3);
		form.add(mail);
		form.add(cellular);
		form.add(fax);

		add(form);
		
		
		final TextField<String> name1 = new TextField<String>("name1", new PropertyModel<String>(getDefaultModel(), "name"));
		final TextField<String> surname1 = new TextField<String>("surname1", new PropertyModel<String>(getDefaultModel(), "surname"));
		final TextArea<String> description1 = new TextArea<String>("description1",
				new PropertyModel<String>(getDefaultModel(), "description"));
		final TextField<String> phone11 = new TextField<String>("phone11", new PropertyModel<String>(getDefaultModel(), "phone1"));
		final TextField<String> phone12 = new TextField<String>("phone12", new PropertyModel<String>(getDefaultModel(), "phone2"));
		final TextField<String> phone13 = new TextField<String>("phone13", new PropertyModel<String>(getDefaultModel(), "phone3"));
		final TextField<String> mail1 = new TextField<String>("mail1", new PropertyModel<String>(getDefaultModel(), "mail"));
		final TextField<String> cellular1 = new TextField<String>("cellular1", new PropertyModel<String>(getDefaultModel(), "cellular"));
		final TextField<String> fax1 = new TextField<String>("fax1", new PropertyModel<String>(getDefaultModel(), "fax"));

		Form<Representative> form1 = new Form<Representative>("form1") {
			@Override
			protected void onSubmit() {
				if (null == getLikontrotechCRMSession().getCompany().getId()) {
					Representative cRepresentative = (Representative) RepresentativesPage.this.getDefaultModel().getObject();
					representativeLocal.create(cRepresentative);
				} else {
					Representative cRepresentative = (Representative) RepresentativesPage.this.getDefaultModel().getObject();
					representativeLocal.edit(cRepresentative);
				}
				modelRepresentatives.detach();
				isEdit = false;
			}

			@Override
			public boolean isVisible() {
				return !isEdit && null != getLikontrotechCRMSession().getRepresentative();
			}
		};
		form1.add(name1);
		form1.add(surname1);
		form1.add(description1);
		form1.add(phone11);
		form1.add(phone12);
		form1.add(phone13);
		form1.add(mail1);
		form1.add(cellular1);
		form1.add(fax1);

		add(form1);		
		
	}
}
