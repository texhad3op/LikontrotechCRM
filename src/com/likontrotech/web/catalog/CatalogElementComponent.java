package com.likontrotech.web.catalog;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.EJB;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;

import com.likontrotech.ejb.PictureLocal;
import com.likontrotech.ejb.entities.Attribute;
import com.likontrotech.ejb.entities.AttributeValue;
import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.ejb.entities.CommercialDocumentLine;
import com.likontrotech.web.LikontrotechCRMSession;

public class CatalogElementComponent extends Panel {

	@EJB(name = "PictureEJB")
	public PictureLocal pictureLocal;

	protected CatalogElement catalogElement;
	protected List<Object[]> attributeValues;

	Image dynamicImage;

	public CatalogElementComponent(CatalogElement catalogElement, List<Object[]> attributeValues) {
		super("component");
		this.catalogElement = catalogElement;
		this.attributeValues = attributeValues;

		CollapseLink<CatalogElement> collapseLink = new CollapseLink<CatalogElement>("addToSuggestionLink", new Model<CatalogElement>(
				this.catalogElement)) {

			@Override
			public void onClick(final AjaxRequestTarget ajaxRequestTarget) {

				if (ajaxRequestTarget != null) {
					setVisible(false);
					ajaxRequestTarget.addComponent(this);
					getModelObject().setRealPrice(getModelObject().getPrice());
					getModelObject().setQuantity(1);
//					getLikontrotechCRMSession().getSelectedElements().add(getModelObject());
					CommercialDocumentLine commercialDocumentLine = new CommercialDocumentLine();
					commercialDocumentLine.setCatalogElementId(getModelObject().getId());
					commercialDocumentLine.setQuantity(1);
					getLikontrotechCRMSession().getLines().add(commercialDocumentLine);					
				}
			}

			@Override
			public boolean isVisible() {
				return !isAlreadySelected(getLikontrotechCRMSession().getLines(), getModelObject())
						&& getModelObject().getType().equals(new Integer(1));
			}
		};
		collapseLink.setOutputMarkupId(true);
		add(collapseLink);

		Link<CatalogElement> link = new Link<CatalogElement>("into", new Model(this.catalogElement)) {
			@Override
			public void onClick() {
				getLikontrotechCRMSession().setCatalogElement(getModelObject());
				setResponsePage(ShowCatalogPage.class);
			}

			@Override
			public boolean isVisible() {
				return getModelObject().getType().equals(new Integer(0));
			}
		};
		link.add(new Label("catalogElementName", catalogElement.getName()));
		add(link);

		add(new Label("catalogElementNameLeaf", catalogElement.getName()) {
			@Override
			public boolean isVisible() {
				return CatalogElementComponent.this.catalogElement.getType().equals(new Integer(1));
			}
		});

		add(new Label("price", String.valueOf(this.catalogElement.getPrice())));
		add(new Label("quantityOnWarehouse", String.valueOf(this.catalogElement.getQuantityOnWarehouse())));
		add(new ListView<Object[]>("attributeValueList", attributeValues) {
			@Override
			protected void populateItem(ListItem<Object[]> item) {
				Object[] attributeValueElement = item.getModelObject();
				AttributeValue attributeValue = (AttributeValue) attributeValueElement[0];
				Attribute attribute = (Attribute) attributeValueElement[1];
				item.add(new Label("attributeName", attribute.getName()));
				item.add(new Label("attributeValue", attributeValue.getValue()));

			}
		});

		BigInteger pictureId = pictureLocal.getPictureId(catalogElement);
		if (null == pictureId)
			pictureId = new BigInteger("0");
		dynamicImage = new Image("picture", String.valueOf(pictureId)) {
			@Override
			public boolean isVisible() {
				return !getDefaultModelObject().equals(new BigInteger("0"));
			}
		};
		dynamicImage.add(new AttributeModifier("src", true, new AbstractReadOnlyModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public final Object getObject() {
				String idd = CatalogElementComponent.this.dynamicImage.getDefaultModelObjectAsString();
				return "pictureServlet?id=" + idd;
			}

		}));
		dynamicImage.setOutputMarkupId(true);
		add(dynamicImage);
	}

	private boolean isAlreadySelected(List<CommercialDocumentLine> list, CatalogElement catalogElement) {
		for(CommercialDocumentLine commercialDocumentLine:list)
			if(commercialDocumentLine.getCatalogElementId().equals(catalogElement.getId()))
			return true;
		return false;
	}

	private LikontrotechCRMSession getLikontrotechCRMSession() {
		return (LikontrotechCRMSession) getSession();
	}

	public CatalogElement getCatalogElement() {
		return catalogElement;
	}
}
