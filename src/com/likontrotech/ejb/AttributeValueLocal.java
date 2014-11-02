package com.likontrotech.ejb;

import java.util.List;

import javax.ejb.Local;

import com.likontrotech.ejb.base.BaseLocal;
import com.likontrotech.ejb.entities.Attribute;
import com.likontrotech.ejb.entities.AttributeValue;
import com.likontrotech.ejb.entities.CatalogElement;

@Local
public interface AttributeValueLocal extends BaseLocal<AttributeValue> {
	AttributeValue find(Object id);

	List<AttributeValue> findAll();

	void remove(Long id);
	
	AttributeValue load(CatalogElement catalogElement, Attribute attribute);
	List<AttributeValue> getAttributValues(CatalogElement catalogElement);	
	
	List<Object[]> getAttributValues2(CatalogElement catalogElement);
	List<Object[]> getAttributValuesForCommercialOffer(CatalogElement catalogElement);
	List<Object[]> getAttributValuesForInvoice(CatalogElement catalogElement);		
}
