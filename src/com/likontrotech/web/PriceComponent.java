package com.likontrotech.web;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.likontrotech.ejb.entities.CatalogElement;

public class PriceComponent extends Panel {
	TextField price;
	TextField quantity;
	CatalogElement catalogElement;
	CheckBox showPicture;

	String subId;

	public PriceComponent(CatalogElement catalogElement) {
		super("component");
		this.catalogElement = catalogElement;
		subId = catalogElement.getName();
		add(price = new TextField("price", new Model(null == catalogElement.getRealPrice() ? "0" : String.valueOf(catalogElement
				.getRealPrice()))));
		add(quantity = new TextField("quantity", new Model(null == catalogElement.getQuantity() ? "0" : String.valueOf(catalogElement
				.getQuantity()))));
		add(new Label("nameLabel", subId));
		add(new Label("priceOld", null == catalogElement.getPrice() ? "0" : String.valueOf(catalogElement.getPrice())));
		add(showPicture = new CheckBox("showPicture", new Model(catalogElement.isShowPicture())));
	}

	public LikontrotechCRMSession getLikontrotechCRMSession() {
		return (LikontrotechCRMSession) getSession();
	}

	public float getPrice() {
		float ret = 0;
		try {
			ret = Float.parseFloat(null == price.getModel().getObject() ? "0" : (String) price.getModel().getObject().toString());
		} catch (NumberFormatException e) {

		}
		return ret;
	}

	public int getQuantity() {
		int ret = 0;
		try {
			ret = Integer.parseInt(null == quantity.getModel().getObject() ? "0" : (String) quantity.getModel().getObject().toString());
		} catch (NumberFormatException e) {

		}
		return ret;
	}

	public String getSubId() {
		return subId;
	}

	public TextField getTextField() {
		return price;
	}

	public CatalogElement getCatalogElement() {
		return catalogElement;
	}

	public void setCatalogElement(CatalogElement catalogElement) {
		this.catalogElement = catalogElement;
	}

	public CheckBox getShowPicture() {
		return showPicture;
	}

	public void setShowPicture(CheckBox showPicture) {
		this.showPicture = showPicture;
	}

}
