package com.likontrotech.ejb;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.likontrotech.ejb.entities.CommercialDocumentLine;
import com.likontrotech.ejb.entities.CommercialDocumentType;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.Event;
import com.likontrotech.ejb.entities.EventType;
import com.likontrotech.ejb.entities.PaymentType;
import com.likontrotech.ejb.entities.Representative;

@Stateless(name = "MailEventEJB")
public class MailEventBean implements MailEventLocal {
	@EJB(name = "GenerateCommercialOfferEJB")
	public GenerateCommercialOfferLocal generateCommercialOfferLocal;

	@EJB(name = "GenerateProformaInvoiceEJB")
	public GenerateProformaInvoiceLocal generateProformaInvoiceLocal;

	@EJB(name = "GenerateInvoiceEJB")
	public GenerateInvoiceLocal generateInvoiceLocal;

	@EJB(name = "EventEJB")
	public EventLocal eventLocal;

	@EJB(name = "SendMailEJB")
	public SendMailLocal sendMailLocal;

	@EJB(name = "CommercialDocumentEJB")
	public CommercialDocumentLocal commercialDocumentLocal;

	public void createMailEventAndCommercialOffer(Boolean toSend,
			String description, Representative representative,
			List<CommercialDocumentLine> selectedElements, Integer days,
			Integer paymentDays, Integer suggestionValidDays,
			PaymentType paymentType, Company company, BigDecimal price,
			BigDecimal priceWithDiscount, Integer discount,
			boolean showPictures, boolean showText, String mail)
			throws Exception {
		Event mailEvent = new Event();
		mailEvent.setDescription(description);
		mailEvent.setEventTime(new Timestamp(System.currentTimeMillis()));
		mailEvent.setRepresentative(representative);
		mailEvent.setType(Utils.getEventType(EventType.REPORT));
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		generateCommercialOfferLocal.generatePdf(selectedElements, days,
				paymentDays, suggestionValidDays, paymentType, company,
				priceWithDiscount, discount, showPictures, showText, buffer);

		eventLocal.create(mailEvent);
		if (toSend)
			sendMailLocal.sendEMail("Likontrotech pasiulymas", new String(
					description.getBytes("UTF-8")), mail, buffer.toByteArray(),
					"LikontrotechPasiulymas.pdf", "application/pdf");

		commercialDocumentLocal
				.create(mailEvent,
						Utils.getCommercialDocumentType(CommercialDocumentType.COMMERCIAL_OFFER),
						buffer.toByteArray(), selectedElements, price,
						priceWithDiscount, discount);
	}

	public void createMailEventAndProformaInvoice(Boolean toSend,
			String description, Representative representative,
			List<CommercialDocumentLine> selectedElements, Integer days,
			Integer paymentDays, Integer suggestionValidDays,
			PaymentType paymentType, Company company, BigDecimal price,
			BigDecimal priceWithDiscount, Integer discount,
			boolean showPictures, boolean showText, String mail)
			throws Exception {
		Event mailEvent = new Event();
		mailEvent.setDescription(description);
		mailEvent.setEventTime(new Timestamp(System.currentTimeMillis()));
		mailEvent.setRepresentative(representative);
		mailEvent.setType(Utils.getEventType(EventType.REPORT));
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		generateProformaInvoiceLocal.generatePdf(selectedElements, days,
				paymentDays, suggestionValidDays, paymentType, company,
				priceWithDiscount, discount, showPictures, showText, buffer);

		eventLocal.create(mailEvent);
		if (toSend)
			sendMailLocal.sendEMail(
					"Likontrotech saskaita isankstiniam apmokejimui",
					new String(description.getBytes("UTF-8")), mail,
					buffer.toByteArray(),
					"LikontrotechIsankstiniamApmokejimui.pdf",
					"application/pdf");

		commercialDocumentLocal
				.create(mailEvent,
						Utils.getCommercialDocumentType(CommercialDocumentType.PROFORMA_INVOICE),
						buffer.toByteArray(), selectedElements, price,
						priceWithDiscount, discount);
	}

	public void createMailEventAndInvoice(Boolean toSend, String description,
			Representative representative,
			List<CommercialDocumentLine> selectedElements, Integer days,
			Integer paymentDays, Integer suggestionValidDays,
			PaymentType paymentType, Company company, BigDecimal price,
			BigDecimal priceWithDiscount, Integer discount,
			boolean showPictures, boolean showText, String mail,
			String position, String firstName, String lastName,
			Timestamp payBefore, boolean addDeliveringPrice,
			BigDecimal deliveringPrice) throws Exception {
		Event mailEvent = new Event();
		mailEvent.setDescription(description);
		mailEvent.setEventTime(new Timestamp(System.currentTimeMillis()));
		mailEvent.setRepresentative(representative);
		mailEvent.setType(Utils.getEventType(EventType.REPORT));
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		generateInvoiceLocal.generatePdf(true, selectedElements, days,
				paymentDays, suggestionValidDays, paymentType, company,
				priceWithDiscount, discount, showPictures, showText, position,
				firstName, lastName, payBefore, addDeliveringPrice,
				deliveringPrice, buffer);

		eventLocal.create(mailEvent);
		if (toSend)
			sendMailLocal.sendEMail("Likontrotech saskaita faktura",
					new String(description.getBytes("UTF-8")), mail,
					buffer.toByteArray(), "LikontrotechSaskaitaFaktura.pdf",
					"application/pdf");

		commercialDocumentLocal
				.create(mailEvent,
						Utils.getCommercialDocumentType(CommercialDocumentType.INVOICE),
						buffer.toByteArray(), selectedElements, price,
						priceWithDiscount, discount);
	}

	public void createEventWithAttachedFile(String description,
			String fileName, byte[] bytes, String mimeType,
			Representative representative, String mail) throws Exception {
		Event mailEvent = new Event();
		mailEvent.setDescription(description);
		mailEvent.setEventTime(new Timestamp(System.currentTimeMillis()));
		mailEvent.setRepresentative(representative);
		mailEvent.setType(Utils.getEventType(EventType.REPORT));
		mailEvent.setMimeType(mimeType);
		mailEvent.setAttachedFile(bytes);

		eventLocal.create(mailEvent);
		sendMailLocal.sendEMail("Likontrotech",
				new String(description.getBytes("UTF-8")), mail, bytes,
				fileName, getMimeType(fileName));
	}

	private String getMimeType(String fileName) {
		String fileNameUpperCase = fileName.toUpperCase();
		if (fileNameUpperCase.endsWith(".PDF"))
			return "application/pdf";
		else if (fileNameUpperCase.endsWith(".GIF"))
			return "image/gif";
		else if (fileNameUpperCase.endsWith(".JPG"))
			return "image/jpeg";
		else if (fileNameUpperCase.endsWith(".PNG"))
			return "image/png";
		else if (fileNameUpperCase.endsWith(".XLSX"))
			return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		else if (fileNameUpperCase.endsWith(".DOCX"))
			return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		else if (fileNameUpperCase.endsWith(".XLS"))
			return "application/vnd.ms-excel";
		else if (fileNameUpperCase.endsWith(".PPT"))
			return "application/vnd.ms-powerpoint";
		else if (fileNameUpperCase.endsWith(".DWG"))
			return "application/acad";
		else
			return "application/octet-stream";
	}
}
