package com.likontrotech.web.catalog;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.web.LikontrotechCRMSession;
import com.likontrotech.web.catalogadmin.TreePage;

public class CatalogElementComponentShort extends Panel {

	protected CatalogElement catalogElement;

	public CatalogElementComponentShort(CatalogElement catalogElement) {
		super("component");
		this.catalogElement = catalogElement;

		Link<CatalogElement> link = new Link<CatalogElement>("into", new Model(this.catalogElement)) {
			@Override
			public void onClick() {
				getLikontrotechCRMSession().setParentCatalogElement(getModelObject());
				setResponsePage(TreePage.class);
			}
		};
		link.add(new Label("catalogElementName", catalogElement.getName()));
		add(link);

	}
	public CatalogElement getCatalogElement() {
		return catalogElement;
	}
	
	private LikontrotechCRMSession getLikontrotechCRMSession() {
		return (LikontrotechCRMSession) getSession();
	}	
}
