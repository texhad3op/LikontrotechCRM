package com.likontrotech.web.catalogadmin;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.likontrotech.ejb.entities.Attribute;
import com.likontrotech.ejb.entities.CatalogElement;

public class AttributesPage extends TreeBasePage {

	IModel<List<Attribute>> modelAttributes = null;
	Integer count = null;
	ListView<Attribute> lv = null;
	final TextField<String> name = new TextField<String>("name", new Model<String>(""));
	Integer cnt = 1;

	public AttributesPage() {
		super();

		modelAttributes = new LoadableDetachableModel<List<Attribute>>() {
			@Override
			protected List<Attribute> load() {
				return attributeLocal.findAll(getLikontrotechCRMSession().getCatalogElement());
			}
		};

		count = modelAttributes.getObject().size();
		lv = new ListView<Attribute>("attributesList", modelAttributes) {
			
			@Override
			protected void populateItem(ListItem<Attribute> item) {
				Attribute attribute = item.getModelObject();
				item.add(new Label("name", attribute.getName()));
				item.add(new Label("number", String.valueOf(AttributesPage.this.cnt)));
				Link<Attribute> deleteLink = new Link<Attribute>("deleteLink", new Model<Attribute>(attribute)) {
					@Override
					public void onClick() {
						Attribute attribute = getModelObject();
						attributeLocal.remove(attribute);
						setResponsePage(AttributesPage.class);
					}
				};
				item.add(deleteLink);

				Link<Attribute> editLink = new Link<Attribute>("editLink", new Model<Attribute>(attribute)) {
					@Override
					public void onClick() {
						Attribute attribute = getModelObject();
						getLikontrotechCRMSession().setEdit(true);
						name.setModelObject(attribute.getName());
						getLikontrotechCRMSession().setAttribute(attribute);

					}
				};
				item.add(editLink);

				Link<Attribute> showForCommercialOfferLink = new Link<Attribute>("showForCommercialOfferLink", new Model<Attribute>(attribute)) {
					@Override
					public void onClick() {
						cnt = 1;
						Attribute attribute = getModelObject();
						attribute.setShowForCommercialOffer(!attribute.isShowForCommercialOffer());
						attributeLocal.edit(attribute);
						modelAttributes.detach();
					}
				};
				showForCommercialOfferLink.add(new Label("showForCommercialOfferLabel",attribute.isShowForCommercialOffer()?"y":"not"));
				item.add(showForCommercialOfferLink);

				Link<Attribute> showForInvoiceLink = new Link<Attribute>("showForInvoiceLink", new Model<Attribute>(attribute)) {
					@Override
					public void onClick() {
						cnt = 1;
						Attribute attribute = getModelObject();
						attribute.setShowForInvoice(!attribute.isShowForInvoice());
						attributeLocal.edit(attribute);
						modelAttributes.detach();
					}
				};
				showForInvoiceLink.add(new Label("showForInvoiceLabel",attribute.isShowForInvoice()?"y":"not"));
				item.add(showForInvoiceLink);				
				
				item.add(new ChangePositionLink<Attribute>("bottomLink", new Model<Attribute>(attribute), AttributesPage.this.cnt) {
					@Override
					public void onClick() {
						Attribute attribute = getModelObject();
						attributeLocal.moveBottom(attribute);
						modelAttributes.detach();
						AttributesPage.this.cnt = 1;						
					}

					@Override
					public boolean isVisible() {
						return !count.equals(current);
					}
				});

				item.add(new ChangePositionLink<Attribute>("topLink", new Model<Attribute>(attribute), AttributesPage.this.cnt) {
					@Override
					public void onClick() {
						Attribute attribute = getModelObject();
						attributeLocal.moveTop(attribute);
						modelAttributes.detach();
						AttributesPage.this.cnt = 1;						
					}

					@Override
					public boolean isVisible() {
						return 1 != current;
					}
				});

				AttributesPage.this.cnt++;
			}
		};

		add(lv);

		Form<Attribute> form = new Form<Attribute>("form") {
			@Override
			protected void onSubmit() {
				if (false == getLikontrotechCRMSession().isEdit()) {
					CatalogElement catalogElement = getLikontrotechCRMSession().getCatalogElement();
					Attribute attribute = new Attribute();
					attribute.setName(name.getConvertedInput());
					attribute.setCatalogElement(catalogElement);
					attribute.setOrderNumber(AttributesPage.this.cnt);
					attributeLocal.create(attribute);
					attribute.setOrderNumber(attribute.getId().intValue());
					attributeLocal.edit(attribute);					
					AttributesPage.this.cnt = 1;
					modelAttributes.detach();
					count = modelAttributes.getObject().size();
				} else {
					getLikontrotechCRMSession().setEdit(false);
					Attribute attribute = attributeLocal.find(getLikontrotechCRMSession().getAttribute().getId());
					attribute.setName(name.getModelObject());
					attributeLocal.edit(attribute);
					AttributesPage.this.cnt = 1;
					modelAttributes.detach();
					count = modelAttributes.getObject().size();
				}
			}
		};
		form.add(name);
		add(form);

		tree.getTreeState().expandAll();

		add(new Label("selectedElement", new Model<String>(null == getLikontrotechCRMSession().getCatalogElement() ? ""
				: getLikontrotechCRMSession().getCatalogElement().getName())));
	}

	public void onClick(Object object, BaseTree bt, AjaxRequestTarget target) {
		modelAttributes.detach();
		DefaultMutableTreeNode dtn = (DefaultMutableTreeNode) object;
		CatalogElement ce = (CatalogElement) dtn.getUserObject();
		getLikontrotechCRMSession().setCatalogElement(ce);
		getLikontrotechCRMSession().setObject(object);
		setResponsePage(AttributesPage.class);
	}

	protected List<CatalogElement> getCatalogItems() {
		List<CatalogElement> els = catalogLocal.findSuperNodes();
		getLikontrotechCRMSession().setObject(els.get(0));
		return els;
	}
}
