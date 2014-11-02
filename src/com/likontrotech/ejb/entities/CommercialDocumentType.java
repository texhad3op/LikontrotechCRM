package com.likontrotech.ejb.entities;

public class CommercialDocumentType extends ClassifierType {
	public static int COMMERCIAL_OFFER = 0;
	public static int PROFORMA_INVOICE = 1;	
	public static int INVOICE = 2;		
	public CommercialDocumentType(int id, String name) {
		super(id, name);
	}
}
