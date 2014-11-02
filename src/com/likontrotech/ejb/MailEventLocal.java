package com.likontrotech.ejb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Local;

import com.likontrotech.ejb.entities.CommercialDocumentLine;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.PaymentType;
import com.likontrotech.ejb.entities.Representative;

@Local
public interface MailEventLocal {
	void createMailEventAndCommercialOffer(Boolean toSend, String description,
			Representative representative,
			List<CommercialDocumentLine> selectedElements, Integer days,
			Integer paymentDays, Integer suggestionValidDays,
			PaymentType paymentType, Company company, BigDecimal price,
			BigDecimal priceWithDiscount, Integer discount,
			boolean showPictures, boolean showText, String mail)
			throws Exception;

	void createMailEventAndProformaInvoice(Boolean toSend, String description,
			Representative representative,
			List<CommercialDocumentLine> selectedElements, Integer days,
			Integer paymentDays, Integer suggestionValidDays,
			PaymentType paymentType, Company company, BigDecimal price,
			BigDecimal priceWithDiscount, Integer discount,
			boolean showPictures, boolean showText, String mail)
			throws Exception;

	void createMailEventAndInvoice(Boolean toSend, String description,
			Representative representative,
			List<CommercialDocumentLine> selectedElements, Integer days,
			Integer paymentDays, Integer suggestionValidDays,
			PaymentType paymentType, Company company, BigDecimal price,
			BigDecimal priceWithDiscount, Integer discount,
			boolean showPictures, boolean showText, String mail,
			String position, String firstName, String lastName,
			Timestamp payBefore, boolean addDeliveringPrice,
			BigDecimal deliveringPrice) throws Exception;

	void createEventWithAttachedFile(String description, String fileName,
			byte[] bytes, String mimeType, Representative representative,
			String mail) throws Exception;
}
