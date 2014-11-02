package com.likontrotech.web;

import java.io.OutputStream;

import javax.ejb.EJB;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.WebPage;

import com.likontrotech.ejb.AttributeValueLocal;
import com.likontrotech.ejb.GenerateInvoiceLocal;

public class GenerateInvoice extends WebPage {

	@EJB(name = "AttributeValueEJB")
	public AttributeValueLocal attributeValueLocal;

	@EJB(name = "GenerateInvoiceEJB")
	public GenerateInvoiceLocal generateInvoiceLocal;

	public GenerateInvoice() {

		try {

			RequestCycle.get().getResponse().setContentType("application/pdf");
			OutputStream os = RequestCycle.get().getResponse()
					.getOutputStream();

			generateInvoiceLocal.generatePdf(false, getLikontrotechCRMSession()
					.getLines(), getLikontrotechCRMSession().getDays(),
					getLikontrotechCRMSession().getPaymentDays(),
					getLikontrotechCRMSession().getSuggestionValidDays(),
					getLikontrotechCRMSession().getPaymentType(),
					getLikontrotechCRMSession().getCompany(),
					getLikontrotechCRMSession().getPriceWithDiscount(),
					getLikontrotechCRMSession().getDiscount(),
					getLikontrotechCRMSession().isShowPictures(),
					getLikontrotechCRMSession().isShowText(),
					getLikontrotechCRMSession().getWorker().getType().getName(),
					getLikontrotechCRMSession().getWorker().getFirstName(),
					getLikontrotechCRMSession().getWorker().getLastName(),	
					getLikontrotechCRMSession().getPayBefore(),
					getLikontrotechCRMSession().isAddDeliveringPrice(),
					getLikontrotechCRMSession().getDeliveringPrice(),
					os);

			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private LikontrotechCRMSession getLikontrotechCRMSession() {
		return (LikontrotechCRMSession) getSession();
	}
}
