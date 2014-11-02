/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.likontrotech.web;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

import com.likontrotech.ejb.Utils;
import com.likontrotech.ejb.entities.Attribute;
import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.ejb.entities.CommercialDocument;
import com.likontrotech.ejb.entities.CommercialDocumentLine;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.Event;
import com.likontrotech.ejb.entities.PaymentType;
import com.likontrotech.ejb.entities.Representative;
import com.likontrotech.ejb.entities.Worker;

/**
 * 
 * @author TEXHAD3OP
 */
public class LikontrotechCRMSession extends WebSession {
	private Company company;
	private List<Company> companies;
	private Event event;
	private CommercialDocument commercialOffer;	
	private CommercialDocument proformaInvoice;	
	private CommercialDocument invoice;		
	
	private CatalogElement catalogElement;
	private CatalogElement parentCatalogElement;
	private String date;
	private Object object;
	private boolean edit;
	private Attribute attribute;
	private CatalogElement catalogElementParameterValuesSource;
	private Boolean copyParameterValuesMode = false;
	private String findString;
	private BigDecimal price;
	private BigDecimal priceWithDiscount;	
	private Integer discount;	
//	private List<CatalogElement> selectedElements = new ArrayList<CatalogElement>();
	private List<CommercialDocumentLine> lines = new ArrayList<CommercialDocumentLine>();
	private Integer days = Utils.getDays(0);
	private PaymentType paymentType = Utils.getPaymentType(0);
	private Integer paymentDays = Utils.getDays(0);
	private Integer suggestionValidDays = Utils.getSuggestionValidDays(0);
	private String selectedMenuPunkt = "crm";
	private Representative representative;
	private Worker worker;
	private boolean showPictures = true;
	private boolean showText = true;
	private Timestamp payBefore = new Timestamp(System.currentTimeMillis());
	private boolean addDeliveringPrice = false;
	private BigDecimal deliveringPrice = new BigDecimal(0);
	
	public Integer getSuggestionValidDays() {
		return suggestionValidDays;
	}

	public void setSuggestionValidDays(Integer suggestionValidDays) {
		this.suggestionValidDays = suggestionValidDays;
	}

	protected LikontrotechCRMSession(Request request) {
		super(request);
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public CatalogElement getParentCatalogElement() {
		return parentCatalogElement;
	}

	public void setParentCatalogElement(CatalogElement parentCatalogElement) {
		this.parentCatalogElement = parentCatalogElement;
	}

	public CatalogElement getCatalogElement() {
		return catalogElement;
	}

	public void setCatalogElement(CatalogElement catalogElement) {
		this.catalogElement = catalogElement;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public CatalogElement getCatalogElementParameterValuesSource() {
		return catalogElementParameterValuesSource;
	}

	public void setCatalogElementParameterValuesSource(CatalogElement catalogElementParameterValuesSource) {
		this.catalogElementParameterValuesSource = catalogElementParameterValuesSource;
	}

	public Boolean isCopyParameterValuesMode() {
		return copyParameterValuesMode;
	}

	public void setCopyParameterValuesMode(Boolean copyParameterValuesMode) {
		this.copyParameterValuesMode = copyParameterValuesMode;
	}

	public String getFindString() {
		return findString;
	}

	public void setFindString(String findString) {
		this.findString = findString;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPriceWithDiscount() {
		return priceWithDiscount;
	}

	public void setPriceWithDiscount(BigDecimal priceWithDiscount) {
		this.priceWithDiscount = priceWithDiscount;
	}

	public Integer getDiscount() {
		return null == discount?0:discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getPaymentDays() {
		return paymentDays;
	}

	public void setPaymentDays(Integer paymentDays) {
		this.paymentDays = paymentDays;
	}

	public String getSelectedMenuPunkt() {
		return selectedMenuPunkt;
	}

	public void setSelectedMenuPunkt(String selectedMenuPunkt) {
		this.selectedMenuPunkt = selectedMenuPunkt;
	}

	public Representative getRepresentative() {
		return representative;
	}

	public void setRepresentative(Representative representative) {
		this.representative = representative;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public boolean isShowPictures() {
		return showPictures;
	}

	public void setShowPictures(boolean showPictures) {
		this.showPictures = showPictures;
	}

	public boolean isShowText() {
		return showText;
	}

	public void setShowText(boolean showText) {
		this.showText = showText;
	}

	public CommercialDocument getCommercialOffer() {
		return commercialOffer;
	}

	public void setCommercialOffer(CommercialDocument commercialOffer) {
		this.commercialOffer = commercialOffer;
	}

	public CommercialDocument getProformaInvoice() {
		return proformaInvoice;
	}

	public void setProformaInvoice(CommercialDocument proformaInvoice) {
		this.proformaInvoice = proformaInvoice;
	}

	public CommercialDocument getInvoice() {
		return invoice;
	}

	public void setInvoice(CommercialDocument invoice) {
		this.invoice = invoice;
	}

	public List<CommercialDocumentLine> getLines() {
		return lines;
	}

	public void setLines(List<CommercialDocumentLine> lines) {
		this.lines = lines;
	}

	public Timestamp getPayBefore() {
		return payBefore;
	}

	public void setPayBefore(Timestamp payBefore) {
		this.payBefore = payBefore;
	}

	public boolean isAddDeliveringPrice() {
		return addDeliveringPrice;
	}

	public void setAddDeliveringPrice(boolean addDeliveringPrice) {
		this.addDeliveringPrice = addDeliveringPrice;
	}

	public BigDecimal getDeliveringPrice() {
		return deliveringPrice;
	}

	public void setDeliveringPrice(BigDecimal deliveringPrice) {
		this.deliveringPrice = deliveringPrice;
	}

}
