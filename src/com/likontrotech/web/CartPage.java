package com.likontrotech.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.likontrotech.ejb.CatalogLocal;
import com.likontrotech.ejb.Utils;
import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.ejb.entities.PaymentType;

public class CartPage extends WebPage {
	@EJB(name = "CatalogEJB")
	public CatalogLocal catalogLocal;
	List<PriceComponent> components = new ArrayList<PriceComponent>();

	IModel<List<PriceComponent>> modelSelectedElements = new LoadableDetachableModel<List<PriceComponent>>() {
		@Override
		protected List<PriceComponent> load() {
//			if (0 == components.size())
//				for (CatalogElement catalogElement : getLikontrotechCRMSession()
//						.getSelectedElements())
//					components.add(new PriceComponent(catalogElement));
			return components;
		}
	};

//	IModel<List<CatalogElement>> modelSelectedElements2 = new LoadableDetachableModel<List<CatalogElement>>() {
//		@Override
//		protected List<CatalogElement> load() {
//			return getLikontrotechCRMSession().getSelectedElements();
//		}
//	};

	public LikontrotechCRMSession getLikontrotechCRMSession() {
		return (LikontrotechCRMSession) getSession();
	}

	public CartPage(final BasePage modalWindowPage, final ModalWindow window) {
//		add(new Label("notSelected", "Nėra pasirinktų elementų") {
//			@Override
//			public boolean isVisible() {
//				return 0 == getLikontrotechCRMSession().getSelectedElements()
//						.size();
//			}
//		});

		final IModel<BigDecimal> totalPriceModel = new LoadableDetachableModel<BigDecimal>() {
			@Override
			protected BigDecimal load() {
				float price = 0;
				for (PriceComponent priceComponent : components)
					price += priceComponent.getPrice()
							* priceComponent.getQuantity();
				getLikontrotechCRMSession().setPrice(new BigDecimal(price));
				return new BigDecimal(price);
			}
		};
		
		final IModel<BigDecimal> totalPriceWithDiscountModel = new LoadableDetachableModel<BigDecimal>() {
			@Override
			protected BigDecimal load() {
				BigDecimal finalPrice = totalPriceModel.getObject()
						.divide(new BigDecimal(100))
						.multiply(new BigDecimal(100 - (null == getLikontrotechCRMSession().getDiscount()?0:getLikontrotechCRMSession().getDiscount())));
				getLikontrotechCRMSession().setPriceWithDiscount(finalPrice);
				return finalPrice;
			}
		};

		final DropDownChoice<Integer> suggestionValidDaysType = new DropDownChoice<Integer>(
				"suggestionValidDays", new Model<Integer>(
						getLikontrotechCRMSession().getSuggestionValidDays()),
				Utils.suggestionValidDays);

		final DropDownChoice<Integer> paymentDaysType = new DropDownChoice<Integer>(
				"paymentDaysType", new Model<Integer>(
						getLikontrotechCRMSession().getPaymentDays()),
				Utils.daysTypes);

		final DropDownChoice<Integer> daysType = new DropDownChoice<Integer>(
				"daysType", new Model<Integer>(getLikontrotechCRMSession()
						.getDays()), Utils.daysTypes);

		final DropDownChoice<PaymentType> paymentTypes = new DropDownChoice<PaymentType>(
				"paymentType", new Model<PaymentType>(
						getLikontrotechCRMSession().getPaymentType()),
				Utils.paymentTypes, new ChoiceRenderer<PaymentType>("name",
						"id"));

		final CheckBox showPictures = new CheckBox("showPictures",
				new PropertyModel(getLikontrotechCRMSession(), "showPictures"));

		final CheckBox showText = new CheckBox("showText", new PropertyModel(
				getLikontrotechCRMSession(), "showText"));

		final TextField discountText = new TextField("discount", new PropertyModel(
				getLikontrotechCRMSession(), "discount"));
		
		
//		Form form = new Form("priceForm") {
//			@Override
//			protected void onSubmit() {
//				int i = 0;
//				for (PriceComponent priceComponent : components) {
//					getLikontrotechCRMSession()
//							.getSelectedElements()
//							.get(i)
//							.setRealPrice(
//									new BigDecimal(priceComponent.getPrice()));
//					getLikontrotechCRMSession().getSelectedElements().get(i)
//							.setQuantity(priceComponent.getQuantity());
//					getLikontrotechCRMSession()
//							.getSelectedElements()
//							.get(i)
//							.setShowPicture(
//									priceComponent.getShowPicture()
//											.getModelObject());
//					i++;
//				}
//				getLikontrotechCRMSession().setPrice(
//						totalPriceModel.getObject());
//				getLikontrotechCRMSession().setPriceWithDiscount(
//						totalPriceWithDiscountModel.getObject());
//				getLikontrotechCRMSession().setDiscount(
//						getLikontrotechCRMSession().getDiscount());
//				getLikontrotechCRMSession().setDays(
//						daysType.getConvertedInput());
//				getLikontrotechCRMSession().setPaymentType(
//						paymentTypes.getConvertedInput());
//				getLikontrotechCRMSession().setPaymentDays(
//						paymentDaysType.getConvertedInput());
//				getLikontrotechCRMSession().setSuggestionValidDays(
//						suggestionValidDaysType.getConvertedInput());
//			}
//		};
//		add(form);
//
//		form.add(new ListView<PriceComponent>("components",
//				modelSelectedElements) {
//			protected void populateItem(final ListItem<PriceComponent> item) {
//				item.add(item.getModelObject());
//			}
//		});
//
//		form.add(paymentTypes);
//		form.add(daysType);
//		form.add(paymentDaysType);
//		form.add(suggestionValidDaysType);
//
//		form.add(new ListView<CatalogElement>("selectedElementsList",
//				modelSelectedElements2) {
//			@Override
//			protected void populateItem(ListItem<CatalogElement> item) {
//				final CatalogElement catalogElement = item.getModelObject();
//				Link<CatalogElement> deletelink = new Link<CatalogElement>(
//						"deletelink", item.getModel()) {
//					@Override
//					public void onClick() {
//						getLikontrotechCRMSession().getSelectedElements()
//								.remove(catalogElement);
//						Iterator<PriceComponent> it = components.iterator();
//						while (it.hasNext()) {
//							PriceComponent priceComponent = it.next();
//							if (catalogElement.getId().equals(
//									priceComponent.getCatalogElement().getId()))
//								it.remove();
//						}
//						modelSelectedElements2.detach();
//						modelSelectedElements.detach();
//					}
//				};
//				item.add(deletelink);
//			}
//		});
//		form.add(discountText);
//		form.add(new Label("price", totalPriceModel));
//		form.add(new Label("finalPrice", totalPriceWithDiscountModel));
//		form.add(showPictures);
//		form.add(showText);
	}
}
