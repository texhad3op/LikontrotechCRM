package com.likontrotech.web;

import java.io.OutputStream;

import javax.ejb.EJB;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.WebPage;

import com.likontrotech.ejb.AttributeValueLocal;
import com.likontrotech.ejb.GenerateProformaInvoiceLocal;

public class GenerateProformaInvoice extends WebPage {

	@EJB(name = "AttributeValueEJB")
	public AttributeValueLocal attributeValueLocal;

	@EJB(name = "GenerateProformaInvoiceEJB")
	public GenerateProformaInvoiceLocal generateProformaInvoiceLocal;

	public GenerateProformaInvoice() {

		try {

			RequestCycle.get().getResponse().setContentType("application/pdf");
			OutputStream os = RequestCycle.get().getResponse().getOutputStream();

			generateProformaInvoiceLocal.generatePdf(getLikontrotechCRMSession().getLines(), getLikontrotechCRMSession().getDays(),
					getLikontrotechCRMSession().getPaymentDays(), getLikontrotechCRMSession().getSuggestionValidDays(),
					getLikontrotechCRMSession().getPaymentType(), getLikontrotechCRMSession().getCompany(), getLikontrotechCRMSession()
							.getPriceWithDiscount(), getLikontrotechCRMSession().getDiscount(), getLikontrotechCRMSession().isShowPictures(), getLikontrotechCRMSession().isShowText(), os);

			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private LikontrotechCRMSession getLikontrotechCRMSession() {
		return (LikontrotechCRMSession) getSession();
	}
}

