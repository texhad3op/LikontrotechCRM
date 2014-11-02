package com.likontrotech.web;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.likontrotech.ejb.Utils;
import com.likontrotech.ejb.entities.CommercialDocumentType;
import com.likontrotech.ejb.entities.PaymentType;

public class CartPageCommercialOffer extends AbstractCartPage {

	DropDownChoice<Integer> suggestionValidDaysType;
	DropDownChoice<Integer> paymentDaysType;
	DropDownChoice<Integer> daysType;
	DropDownChoice<PaymentType> paymentTypes;
	CheckBox showPictures;
	CheckBox showText;

	public CartPageCommercialOffer(final BasePage modalWindowPage,
			final ModalWindow window) {
		super(modalWindowPage, window);
	}

	@Override
	protected void saveValues() {
		getLikontrotechCRMSession().setDays(daysType.getConvertedInput());
		getLikontrotechCRMSession().setPaymentType(
				paymentTypes.getConvertedInput());
		getLikontrotechCRMSession().setPaymentDays(
				paymentDaysType.getConvertedInput());
		getLikontrotechCRMSession().setSuggestionValidDays(
				suggestionValidDaysType.getConvertedInput());
	}

	@Override
	protected void addComponents() {
		form.add(paymentTypes = new DropDownChoice<PaymentType>("paymentType",
				new Model<PaymentType>(getLikontrotechCRMSession()
						.getPaymentType()), Utils.paymentTypes,
				new ChoiceRenderer<PaymentType>("name", "id")));
		form.add(daysType = new DropDownChoice<Integer>("daysType",
				new Model<Integer>(getLikontrotechCRMSession().getDays()),
				Utils.daysTypes));
		form.add(paymentDaysType = new DropDownChoice<Integer>(
				"paymentDaysType", new Model<Integer>(
						getLikontrotechCRMSession().getPaymentDays()),
				Utils.daysTypes));
		form.add(suggestionValidDaysType = new DropDownChoice<Integer>(
				"suggestionValidDays", new Model<Integer>(
						getLikontrotechCRMSession().getSuggestionValidDays()),
				Utils.suggestionValidDays));
		form.add(showPictures = new CheckBox("showPictures", new PropertyModel(
				getLikontrotechCRMSession(), "showPictures")));
		form.add(showText = new CheckBox("showText", new PropertyModel(
				getLikontrotechCRMSession(), "showText")));
	}
	
	@Override
	public CommercialDocumentType getCommercialDocumentType() {
		return Utils.getCommercialDocumentType(CommercialDocumentType.COMMERCIAL_OFFER);
	}	
}
