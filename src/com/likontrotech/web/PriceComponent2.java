package com.likontrotech.web;

import javax.ejb.EJB;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.likontrotech.ejb.CatalogLocal;
import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.ejb.entities.CommercialDocumentLine;
import com.likontrotech.ejb.entities.CommercialDocumentType;

public class PriceComponent2 extends Panel {
	TextField exclusivePrice;
	TextField quantity;
	CommercialDocumentLine commercialDocumentLine;
	CatalogElement catalogElement;
	CheckBox showPicture;
	String subId;
	CommercialDocumentType commercialDocumentType;

	AbstractCartPage.PriceComponentExecutor priceComponentExecutor;

	@EJB(name = "CatalogEJB")
	public CatalogLocal catalogLocal;

	public PriceComponent2(CommercialDocumentLine commercialDocumentLine,
			CommercialDocumentType commercialDocumentType,
			AbstractCartPage.PriceComponentExecutor priceComponentExecutor) {
		super("component");

		this.commercialDocumentType = commercialDocumentType;
		this.priceComponentExecutor = priceComponentExecutor;

		this.catalogElement = catalogLocal.find(commercialDocumentLine
				.getCatalogElementId());
		this.commercialDocumentLine = commercialDocumentLine;
		subId = catalogElement.getName();

		if (null == commercialDocumentLine.getRealPrice())
			commercialDocumentLine.setRealPrice(catalogElement.getPrice());

		add(exclusivePrice = new TextField("exclusivePrice", new PropertyModel(
				commercialDocumentLine, "realPrice")));

		add(quantity = new TextField("quantity", new PropertyModel(
				commercialDocumentLine, "quantity")));

		add(new Label("nameLabel", subId));
		add(new Label("priceOld", null == catalogElement.getPrice() ? "0"
				: String.valueOf(catalogElement.getPrice())));
		add(showPicture = new CheckBox("showPicture", new PropertyModel(
				commercialDocumentLine, "showPicture")){
			public boolean isVisible(){
				return PriceComponent2.this.commercialDocumentType.getId() == CommercialDocumentType.COMMERCIAL_OFFER;
			}
		});

		Link<CommercialDocumentLine> deletelink = new Link<CommercialDocumentLine>(
				"deletelink", new Model(commercialDocumentLine)) {
			@Override
			public void onClick() {
				getLikontrotechCRMSession().getLines().remove(
						PriceComponent2.this.commercialDocumentLine);
				PriceComponent2.this.priceComponentExecutor.execute();
			}
		};
		add(deletelink);
	}

	public LikontrotechCRMSession getLikontrotechCRMSession() {
		return (LikontrotechCRMSession) getSession();
	}

	public float getPrice() {
		float ret = 0;
		try {
			ret = Float.parseFloat(null == exclusivePrice.getModel()
					.getObject() ? "0" : (String) exclusivePrice.getModel()
					.getObject().toString());
		} catch (NumberFormatException e) {

		}
		return ret;
	}

	public int getQuantity() {
		int ret = 0;
		try {
			ret = Integer
					.parseInt(null == quantity.getModel().getObject() ? "0"
							: (String) quantity.getModel().getObject()
									.toString());
		} catch (NumberFormatException e) {

		}
		return ret;
	}

	public String getSubId() {
		return subId;
	}

	public TextField getTextField() {
		return exclusivePrice;
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

	public CommercialDocumentLine getCommercialDocumentLine() {
		return commercialDocumentLine;
	}

	public void setCommercialDocumentLine(
			CommercialDocumentLine commercialDocumentLine) {
		this.commercialDocumentLine = commercialDocumentLine;
	}

}
