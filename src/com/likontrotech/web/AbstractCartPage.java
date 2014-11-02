package com.likontrotech.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import com.likontrotech.ejb.CatalogLocal;
import com.likontrotech.ejb.entities.CommercialDocumentLine;
import com.likontrotech.ejb.entities.CommercialDocumentType;

public abstract class AbstractCartPage extends WebPage {

	@EJB(name = "CatalogEJB")
	public CatalogLocal catalogLocal;

	public interface PriceComponentExecutor extends Serializable {
		public void execute();
	}

	IModel<List<PriceComponent2>> modelSelectedElements = new LoadableDetachableModel<List<PriceComponent2>>() {
		@Override
		protected List<PriceComponent2> load() {
			List<PriceComponent2> components = new ArrayList<PriceComponent2>();
			for (CommercialDocumentLine commercialDocumentLine : getLikontrotechCRMSession()
					.getLines())
				components.add(new PriceComponent2(commercialDocumentLine, getCommercialDocumentType(),
						new AbstractCartPage.PriceComponentExecutor() {
							@Override
							public void execute() {
								modelSelectedElements.detach();
							}
						}));
			return components;
		}
	};

	public LikontrotechCRMSession getLikontrotechCRMSession() {
		return (LikontrotechCRMSession) getSession();
	}

	final IModel<BigDecimal> totalPriceModel = new LoadableDetachableModel<BigDecimal>() {
		@Override
		protected BigDecimal load() {
			float price = 0;
			for (PriceComponent2 priceComponent : modelSelectedElements
					.getObject())
				price += priceComponent.getPrice()
						* priceComponent.getQuantity();
			getLikontrotechCRMSession().setPrice(new BigDecimal(price));
			return new BigDecimal(price+getDelivericeingPrice());
		}
	};

	final IModel<BigDecimal> totalPriceWithDiscountModel = new LoadableDetachableModel<BigDecimal>() {
		@Override
		protected BigDecimal load() {
			BigDecimal finalPrice = totalPriceModel
					.getObject()
					.divide(new BigDecimal(100))
					.multiply(
							new BigDecimal(
									100 - (null == getLikontrotechCRMSession()
											.getDiscount() ? 0
											: getLikontrotechCRMSession()
													.getDiscount())));
			getLikontrotechCRMSession().setPriceWithDiscount(finalPrice);
			return finalPrice;
		}
	};

	final TextField discountText = new TextField("discount", new PropertyModel(
			getLikontrotechCRMSession(), "discount"));
	Form form;

	public AbstractCartPage(final BasePage modalWindowPage,
			final ModalWindow window) {
		add(new Label("notSelected", "Nėra pasirinktų elementų") {
			@Override
			public boolean isVisible() {
				return 0 == getLikontrotechCRMSession().getLines().size();
			}
		});

		form = new Form("priceForm") {
			@Override
			protected void onSubmit() {
				getLikontrotechCRMSession().setPrice(
						totalPriceModel.getObject());
				getLikontrotechCRMSession().setPriceWithDiscount(
						totalPriceWithDiscountModel.getObject());
				getLikontrotechCRMSession().setDiscount(
						Integer.parseInt(discountText.getValue()));

				saveValues();

			}
		};
		add(form);

		form.add(new ListView<PriceComponent2>("components",
				modelSelectedElements) {
			protected void populateItem(final ListItem<PriceComponent2> item) {
				item.add(item.getModelObject());
			}
		});

		form.add(discountText);
		form.add(new Label("price", totalPriceModel));
		form.add(new Label("finalPrice", totalPriceWithDiscountModel));
		addComponents();
	}

	abstract public CommercialDocumentType getCommercialDocumentType();
	
	protected void addComponents() {
	}

	protected void saveValues() {
	}
	
	protected float getDelivericeingPrice() {
		return 0;
	}	
}
