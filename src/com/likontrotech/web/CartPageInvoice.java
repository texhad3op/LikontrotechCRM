package com.likontrotech.web;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.joda.time.format.DateTimeFormatter;

import com.likontrotech.ejb.Utils;
import com.likontrotech.ejb.entities.CommercialDocumentType;

public class CartPageInvoice extends AbstractCartPage {

	DateTextField dateTextField;
	CheckBox addDeliveringPrice;
	TextField deliveringPrice;
	public CartPageInvoice(final BasePage modalWindowPage,
			final ModalWindow window) {
		super(modalWindowPage, window);

	}

	@Override
	public CommercialDocumentType getCommercialDocumentType() {
		return Utils.getCommercialDocumentType(CommercialDocumentType.INVOICE);
	}
	@Override	
	protected void addComponents() {
		dateTextField = new DateTextField("payBeforeDate",
				new Model(getLikontrotechCRMSession().getPayBefore()),
				new DateConverter(true) {

					@Override
					public DateTimeFormatter getFormat() {
						return null;
					}

					@Override
					public String getDatePattern() {
						return "yyyy-MM-dd";
					}

					public Date convertToObject(String value, Locale locale) {
						Timestamp ts = new Timestamp(System.currentTimeMillis());
						Timestamp ts2 = java.sql.Timestamp.valueOf(value + " "
								+ ts.getHours() + ":" + ts.getMinutes() + ":"
								+ ts.getSeconds());
						return ts2;
					}

					public String convertToString(Object value, Locale locale) {
						return value.toString().substring(0, 10);
					}

					@Override
					public Locale getLocale() {
						return ConstantsUtil.getLocaleLt();
					}

				}) {

			@Override
			public Locale getLocale() {
				return ConstantsUtil.getLocaleLt();
			}
		};	
		dateTextField.add(new DatePicker());
		form.add(dateTextField);
		form.add(addDeliveringPrice = new CheckBox("addDeliveringPrice", new PropertyModel(
				getLikontrotechCRMSession(), "addDeliveringPrice")));
		form.add(deliveringPrice = new TextField("deliveringPrice", new PropertyModel(
				getLikontrotechCRMSession(), "deliveringPrice")));		
	}
	
	@Override
	protected void saveValues() {
		getLikontrotechCRMSession().setPayBefore(new Timestamp(dateTextField.getConvertedInput().getTime()));
		getLikontrotechCRMSession().setDeliveringPrice(new BigDecimal(deliveringPrice.getConvertedInput().toString()));		
		getLikontrotechCRMSession().setAddDeliveringPrice(addDeliveringPrice.getConvertedInput());	
	}

	@Override	
	protected float getDelivericeingPrice() {
		return getLikontrotechCRMSession().getDeliveringPrice().floatValue();
	}		
	 
}