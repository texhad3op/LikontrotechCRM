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

@Stateless(name = "GenerateProformaInvoiceEJB")
public class GenerateProformaInvoiceBean implements GenerateProformaInvoiceLocal {

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
		document.addTitle("IsankKomercinis pasiulymas");
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
//		addEmptyLine(preface, 1);
		// Lets write a big header
		preface.add(new Paragraph("IŠANKSTINIO APMOKĖJIMO SĄSKAITA", catFont));
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
		preface = new Paragraph();

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		PdfPCell cell;
		
		cell = new PdfPCell(new Paragraph("Prekių (paslaugų) tiekėjas:", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("Prekių (paslaugų) pirkėjas:", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph("UAB \"Likontrotech\"", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(company.getName(), smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph("Įmonės kodas: 121345545", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("Įmonės kodas: "+company.getCompanyCode(), smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("PVM mokėtojo kodas: LT213455417", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("PVM mokėtojo kodas: "+company.getVatCode(), smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);		
		
		cell = new PdfPCell(new Paragraph("Adresas: Minties g 48-88, LT-09220, Vilnius 51", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("Adresas: "+company.getAddress(), smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);	

		cell = new PdfPCell(new Paragraph("Tel: +37061559680", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);			
		
		cell = new PdfPCell(new Paragraph("a/s: LT644010042400021439", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);	
		
		cell = new PdfPCell(new Paragraph("Bankas: AB DnB", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);			
		
		cell = new PdfPCell(new Paragraph("Vilniaus skyrius", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);			
		
		cell = new PdfPCell(new Paragraph("Banko kodas: 40100", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("", smallFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);			

		Paragraph parag = new Paragraph();
		addEmptyLine(parag, 1);

		preface.add(table);
		document.add(preface);
		document.add(parag);

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
		String sums3 = String.valueOf(doubleSum);		
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
		document.add(parag);

		parag = new Paragraph();
		String sumForGeneration = sums3.replaceFirst(",", ".");
//		sumForGeneration = sumForGeneration.substring(0, sumForGeneration.indexOf(" "));
		parag.add(new Paragraph("Suma žodžiais: "+SumAsWords.getSumAsWors(Double.valueOf(sumForGeneration).floatValue()),
				smallFont));
		parag.setAlignment(Element.ALIGN_CENTER);
		document.add(parag);
		
		parag = new Paragraph();		
		addEmptyLine(parag, 1);
		document.add(parag);

		parag = new Paragraph();		
		addEmptyLine(parag, 1);
		document.add(parag);
		
		parag = new Paragraph();
		parag.add(new Paragraph("Direktorius Mark Podolinskij", smallFont));
		parag.setAlignment(Element.ALIGN_LEFT);
		addEmptyLine(parag, 1);
		document.add(parag);
	}

	private void AddPair(PdfPTable tab, String s1, String s2) {
		PdfPCell cell = new PdfPCell(new Paragraph(s1, smallFont));
		cell.setBorderColor(BaseColor.WHITE);
		tab.addCell(cell);

		cell = new PdfPCell(new Paragraph(s2, smallFont));
		// cell.setNoWrap(true);
		cell.setBorderColor(BaseColor.WHITE);
		tab.addCell(cell);
	}

	private PdfPTable createList(CatalogElement catalogElement,
			List<Object[]> objects) {
		PdfPTable tab = new PdfPTable(new float[] { 3f, 2f });
		// AddPair(tab, "Modelis:", catalogElement.getName());
		for (Object[] object : objects) {
			Attribute attribute = (Attribute) object[1];
			if (attribute.isShowForInvoice()) {
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
		PdfPTable table = new PdfPTable( new float[] { 2f, 12f, 3f, 2f, 3f });
		table.setWidthPercentage(105);

		PdfPCell c1 = new PdfPCell(new Paragraph("Eil. Nr.", smallFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setFixedHeight(22);
		table.addCell(c1);

		Paragraph ph = new Paragraph("Prekės ar paslaugos pavadinimas",
				simpleFont);
		c1 = new PdfPCell(ph);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Paragraph("Kiekis", smallFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		
		c1 = new PdfPCell(new Paragraph("Kaina, Lt", smallFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Paragraph("Suma, Lt:", smallFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		table.setHeaderRows(1);

		int i = 1;
		for (CommercialDocumentLine commercialDocumentLine : elements) {
			
			CatalogElement catalogElement = catalogLocal.find(commercialDocumentLine.getCatalogElementId());
			
			List<Object[]> objects = attributeValueLocal
					.getAttributValuesForInvoice(catalogElement);
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

		priceCell.addElement(ph);
		priceCell.addElement(createList(catalogElement, objects));
		priceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(priceCell);

		showPrice(table, Kaina, Kiekis);
	}

	private void showPrice(PdfPTable table, Double price, Integer amount) {

		PdfPCell c = new PdfPCell(new Paragraph(amount.toString(), simpleFont));
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		c.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(c);		
		
		c = new PdfPCell(new Paragraph(String.format(
				Locale.getDefault(), "%.2f", price), simpleFont));
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

