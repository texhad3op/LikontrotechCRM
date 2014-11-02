package com.likontrotech.web;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

import com.likontrotech.ejb.Utils;
import com.likontrotech.ejb.entities.CommercialDocumentType;

public class CartPageProformaInvoice extends AbstractCartPage {

	public CartPageProformaInvoice(final BasePage modalWindowPage,
			final ModalWindow window) {
		super(modalWindowPage, window);

	}

	@Override
	public CommercialDocumentType getCommercialDocumentType() {
		return Utils.getCommercialDocumentType(CommercialDocumentType.PROFORMA_INVOICE);
	}
}
