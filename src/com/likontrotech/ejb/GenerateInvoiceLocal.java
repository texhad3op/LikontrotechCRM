package com.likontrotech.ejb;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Local;

import com.likontrotech.ejb.entities.CommercialDocumentLine;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.PaymentType;

@Local
public interface GenerateInvoiceLocal {
	void generatePdf(boolean real, List<CommercialDocumentLine> elements,
			Integer days, Integer paymentDays, Integer suggestionValidDays,
			PaymentType paymentType, Company company, BigDecimal price,
			Integer discount, boolean showPictures, boolean showText,
			String position, String firstName, String lastName,
			Timestamp payBefore, boolean addDeliveringPrice,
			BigDecimal deliveringPrice, OutputStream os) throws Exception;
}
