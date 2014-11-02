package com.likontrotech.ejb;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import com.likontrotech.ejb.entities.CommercialDocumentLine;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.PaymentType;

@Local
public interface GenerateProformaInvoiceLocal {
	void generatePdf(List<CommercialDocumentLine> elements, Integer days,
			Integer paymentDays, Integer suggestionValidDays,
			PaymentType paymentType, Company company, BigDecimal price,
			Integer discount, boolean showPictures, boolean showText,
			OutputStream os)
			throws Exception;
}
