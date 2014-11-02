package com.likontrotech.ejb;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.likontrotech.ejb.entities.Attribute;
import com.likontrotech.ejb.entities.AttributeValue;
import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.ejb.entities.CommercialDocumentLine;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.PaymentType;

@Stateless(name = "GenerateCommercialOfferEJB")
public class GenerateCommercialOfferBean implements GenerateCommercialOfferLocal {

	@EJB(name = "AttributeValueEJB")
	public AttributeValueLocal attributeValueLocal;
	
	@EJB(name = "CatalogEJB")
	public CatalogLocal catalogLocal;

	private Font catFont;
	private Font boldFont;
	private Font italicFont;
	private Font simpleFont;
	private Font redFont;
	private Font subFont;
	private Font subTimes;
	private Font smallBold;
	private Font smallFont;
	private Font uFont;

	Integer days;
	Integer paymentDays;
	PaymentType paymentType;
	Integer suggestionValidDays;
	Company company;
	BigDecimal price;
	Integer discount;
	boolean showPictures;
	boolean showText;

	public void CreateLTFonts() throws DocumentException, IOException {
		BaseFont basef = BaseFont.createFont("c:\\Windows\\fonts\\times.ttf",
				"Cp1257", false);
		BaseFont basetimes = BaseFont.createFont(
				"c:\\Windows\\fonts\\times.ttf", "Cp1257", false);

		catFont = new Font(basef, 16, Font.BOLD);
		boldFont = new Font(basef, 12, Font.BOLD);
		italicFont = new Font(basef, 12, Font.ITALIC);
		simpleFont = new Font(basef, 11);
		redFont = new Font(basef, 12, Font.NORMAL, BaseColor.RED);
		subFont = new Font(basef, 14, Font.BOLD);
		subTimes = new Font(basetimes, 14, Font.BOLD);
		smallBold = new Font(basef, 10, Font.BOLD);
		smallFont = new Font(basef, 9);
		uFont = new Font(basef, 10, Font.UNDERLINE);
	}

	private void addMetaData(Document document) {
		document.addTitle("Komercinis pasiulymas");
		document.addSubject("UAB Likontrotech komercinis pasiulymas");
		document.addKeywords("Likontrotech, komercinis, pasiulymas");
		document.addAuthor("UAB Likontrotech");
		document.addCreator("UAB Likontrotech");
	}

	private void addTitlePage(Document document) throws DocumentException,
			IOException {
		Paragraph preface = new Paragraph();
		preface.setAlignment(Element.ALIGN_CENTER);
		// We add one empty line
		addEmptyLine(preface, 1);
		// Lets write a big header
		preface.add(new Paragraph("UAB \"Likontrotech\"", catFont));
		preface.add(new Paragraph("Į. k. 121345545", simpleFont));
		preface.add(new Paragraph("PVM mokėtojo kodas LT213455417", simpleFont));
		preface.add(new Paragraph("Mob.tel.: +37061559680", simpleFont));
		preface.add(new Paragraph("El. paštas: likontrotech1@yahoo.com",
				simpleFont));
		preface.add(new Paragraph("Adresas: Minties 48-88 LT-09220, Vilnius",
				simpleFont));
		addEmptyLine(preface, 1);
		document.add(preface);
		preface = new Paragraph();

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph("Bankas: a/s LT644010042400021439",
				smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("Pirkėjas:", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("AB DNB Bankas", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(company.getName(), smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("Banko kodas 40100", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(" ", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		preface.add(table);
		document.add(preface);

		preface = new Paragraph();
		preface.setAlignment(Element.ALIGN_CENTER);
		preface.add(new Paragraph("Komercinis pasiūlymas", subFont));
		addEmptyLine(preface, 1);
		
		
//		Date d = new Date();
//		SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd");
//		String s = f.format(d);
//		preface.add(new Paragraph(" " + s + " ", boldFont));

//		DateFormat dateFormat = new SimpleDateFormat("yyyy MMMM dd",new Locale("LT", "lt"));
		Calendar cal = Calendar.getInstance();
//		String date = dateFormat.format(cal.getTime());
//		date = date.replaceFirst(" ", "-");
//		date = date.replaceFirst(" ", "=");
//		date = date.replaceFirst("-", " m. ");		
//		date = date.replaceFirst("=", " mėn. ");	
//		date += " d.";
		preface.add(new Paragraph(Utils.getDateForDocument(cal), simpleFont));
		
		addEmptyLine(preface, 1);
		document.add(preface);

	}

	private String normalizePrice(String in) {
		int s = in.indexOf(",");
		int e = in.indexOf(" Lt");
		if (2 == e - s)
			in = in.replaceAll(" Lt", "0 Lt");
		else if (-1 == in.indexOf(","))
			in = in.replaceAll(" Lt", ",00 Lt");
		return in;
	}

	private void addFooter(Document document) throws DocumentException,
			UnsupportedEncodingException {
		Paragraph parag = new Paragraph();
		addEmptyLine(parag, 1);
		BigDecimal hundred = new BigDecimal(.21);

		BigDecimal vat = price.multiply(hundred);
		NumberFormat n = NumberFormat
				.getCurrencyInstance(new Locale("LT", "lt"));
		double doubleVat = vat.doubleValue();
		String prices = n.format(price);
		String vats = n.format(doubleVat);

		BigDecimal sum = price.add(vat);
		double doubleSum = sum.doubleValue();
		String sums = n.format(doubleSum);

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		PdfPCell cell;
		if (!new Integer(0).equals(discount)) {
			cell = new PdfPCell(new Paragraph("Nuolaida:", smallFont));
			cell.setBorder(0);
			cell.setPaddingLeft(100);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph(discount.toString()+" %", smallFont));
			cell.setBorder(0);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
		}

		cell = new PdfPCell(new Paragraph("Iš viso:", smallFont));
		cell.setBorder(0);
		cell.setPaddingLeft(100);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(normalizePrice(prices), smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("PVM (21%):", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(normalizePrice(vats), smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("Suma apmokėjimui:", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(normalizePrice(sums), smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);

		parag.add(table);

		addEmptyLine(parag, 1);
		if (showText) {
			parag.add(new Paragraph(
					"1. Kainos nurodytos be metrologinės patikros bei transportavimo išlaidų",
					smallFont));
			String res = paymentType.getName();
			if (2 == paymentType.getId())
				res = res.replaceFirst("N", String.valueOf(paymentDays));
			parag.add(new Paragraph("2. Apmokėjimas: " + res, smallFont));
			parag.add(new Paragraph(
					"3. TIEKIMO TERMINAI : "
							+ days
							+ " dienų bėgyje po komercinio pasiūlymo patvirtinimo rašto gavimo",
					smallFont));
			if (0 != suggestionValidDays)
				parag.add(new Paragraph("4. Pasiūlymas galioja "
						+ suggestionValidDays + " darbo dienas", smallFont));

		}
		addEmptyLine(parag, 1);
		document.add(parag);

		parag = new Paragraph();
		parag.add(new Paragraph(
				"Pagarbiai,",
				smallFont));
		parag.setAlignment(Element.ALIGN_LEFT);
		document.add(parag);
		
		parag = new Paragraph();
		parag.add(new Paragraph(
				"UAB Likontrotech direktorius Mark Podolinskij",
				smallFont));
		parag.setAlignment(Element.ALIGN_LEFT);
		document.add(parag);
		

		parag = new Paragraph();
		parag.add(new Paragraph(
				"Daugiau matavimo prietaisų ir paslaugų pramoninės metrologijos srityje pasiūlymų rasite :",
				smallFont));
		parag.setAlignment(Element.ALIGN_CENTER);
		document.add(parag);
		parag = new Paragraph();
		parag.add(new Paragraph("www.likontrotech.lt", uFont));
		parag.setAlignment(Element.ALIGN_RIGHT);
		addEmptyLine(parag, 1);
		document.add(parag);
	}

	private void AddPair(PdfPTable tab, String s1, String s2) {
		PdfPCell cell = new PdfPCell(new Paragraph(s1, smallFont));
		cell.setBorderColor(BaseColor.WHITE);
		tab.addCell(cell);
		cell = new PdfPCell(new Paragraph(s2, smallFont));
		cell.setBorderColor(BaseColor.WHITE);
		tab.addCell(cell);
	}

	private PdfPTable createList(CatalogElement catalogElement,
			List<Object[]> objects) {
		PdfPTable tab = new PdfPTable(new float[] { 3f, 2f });
		// AddPair(tab, "Modelis:", catalogElement.getName());
		for (Object[] object : objects) {
			Attribute attribute = (Attribute) object[1];
			if (attribute.isShowForCommercialOffer()) {
				AttributeValue attributeValue = (AttributeValue) object[0];
				AddPair(tab, attribute.getName(), attributeValue.getValue());
			}
		}
		return tab;
	}

	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private void addContent(Document document, List<CommercialDocumentLine> elements)
			throws DocumentException, MalformedURLException, IOException {
		Paragraph parag = new Paragraph();
		createTable(parag, elements);
		document.add(parag);
	}

	private void createTable(Paragraph parag, List<CommercialDocumentLine> elements)
			throws BadElementException, MalformedURLException, IOException {
		PdfPTable table = new PdfPTable(showPictures ? new float[] { 2f, 12f,
				9f, 3f, 2f, 3f } : new float[] { 2f, 12f, 3f, 2f, 3f });
		table.setWidthPercentage(105);

		PdfPCell c1 = new PdfPCell(new Paragraph("Eil. Nr.", smallFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setFixedHeight(22);
		table.addCell(c1);

		Paragraph ph = new Paragraph("Prietaiso pavadinimas ir duomenys",
				simpleFont);
		c1 = new PdfPCell(ph);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		if (showPictures) {
			c1 = new PdfPCell(new Phrase("", simpleFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);
		}

		c1 = new PdfPCell(new Paragraph("Vnt. kaina, Lt", smallFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		c1 = new PdfPCell(new Paragraph("Kiekis", smallFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		c1 = new PdfPCell(new Paragraph("Suma be PVM, Lt:", smallFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		table.setHeaderRows(1);

		int i = 1;
		for (CommercialDocumentLine commercialDocumentLine : elements) {
			
			CatalogElement catalogElement = catalogLocal.find(commercialDocumentLine.getCatalogElementId());
			catalogElement.setShowPicture(commercialDocumentLine.isShowPicture());
			List<Object[]> objects = attributeValueLocal
					.getAttributValuesForCommercialOffer(catalogElement);
			addProductInfo(
					i++,
					commercialDocumentLine, catalogElement,
					null == commercialDocumentLine.getRealPrice() ? 0 : commercialDocumentLine
							.getRealPrice().doubleValue(),
					objects,
					null == commercialDocumentLine.getQuantity() ? 0 : commercialDocumentLine
							.getQuantity(), table);
		}

		parag.add(table);
	}

	private void addProductInfo(Integer Nr, CommercialDocumentLine commercialDocumentLine, CatalogElement catalogElement,
			Double Kaina, List<Object[]> objects, Integer Kiekis,
			PdfPTable table) throws BadElementException, MalformedURLException,
			IOException {
		PdfPCell c1 = new PdfPCell(new Phrase(Nr.toString()));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(c1);

		Paragraph ph = new Paragraph();

		PdfPCell priceCell = new PdfPCell();
		if (!commercialDocumentLine.isShowPicture())
			priceCell.setColspan(2);
		priceCell.addElement(ph);
		priceCell.addElement(createList(catalogElement, objects));
		priceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(priceCell);
		if (showPictures && catalogElement.isShowPicture()) {
			PdfPCell imageCell = null;
			if (null != catalogElement.getPicture()) {
				Image image = Image.getInstance(catalogElement.getPicture()
						.getPicture());
				imageCell = new PdfPCell(image, true);
			} else
				imageCell = new PdfPCell(new Phrase(" "));

			imageCell.setPadding(1);
			imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(imageCell);
		}

		showPrice(table, Kaina, Kiekis);
	}

	private void showPrice(PdfPTable table, Double price, Integer amount) {

		PdfPCell c = new PdfPCell(new Paragraph(String.format(
				Locale.getDefault(), "%.2f", price), simpleFont));
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		c.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(c);

		c = new PdfPCell(new Paragraph(amount.toString(), simpleFont));
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		c.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(c);

		c = new PdfPCell(new Paragraph(String.format(Locale.getDefault(),
				"%.2f", price * amount), simpleFont));
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		c.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(c);
	}

	public void generatePdf(List<CommercialDocumentLine> elements, Integer days,
			Integer paymentDays, Integer suggestionValidDays,
			PaymentType paymentType, Company company, BigDecimal price,
			Integer discount, boolean showPictures, boolean showText,
			OutputStream os) throws Exception {
		this.days = (null == days ? Utils.getDays(0) : days);
		this.paymentType = (null == paymentType ? Utils.getPaymentType(0)
				: paymentType);
		this.paymentDays = (null == paymentDays ? Utils.getDays(0)
				: paymentDays);
		this.suggestionValidDays = (null == suggestionValidDays ? Utils
				.getSuggestionValidDays(0) : suggestionValidDays);
		this.company = company;
		this.price = null == price ? new BigDecimal(0) : price;
		this.discount = null == discount ? new Integer(0) : discount;
		this.showPictures = showPictures;
		this.showText = showText;
		CreateLTFonts();
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter.getInstance(document, os);
		document.open();
		addMetaData(document);
		addTitlePage(document);
		addContent(document, elements);
		addFooter(document);
		document.close();

	}
}
