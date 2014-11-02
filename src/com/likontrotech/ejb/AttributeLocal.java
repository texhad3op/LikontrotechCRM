package com.likontrotech.ejb;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Local;

import com.likontrotech.ejb.base.BaseLocal;
import com.likontrotech.ejb.entities.Attribute;
import com.likontrotech.ejb.entities.CatalogElement;

@Local
public interface AttributeLocal extends BaseLocal<Attribute> {
	public Attribute find(Object id);

	public List<Attribute> findAll();

	void remove(Long id);

	List<Attribute> findAll(CatalogElement catalogElement);
	
//	BigInteger getAttributesFirstIdCatalogElement(CatalogElement catalogElement);
//	
//	BigInteger getAttributesLastIdCatalogElement(CatalogElement catalogElement);	

	BigInteger getAttributesCountOfCatalogElement(CatalogElement catalogElement);
	
	void moveTop(Attribute attr);	
	
	void moveBottom(Attribute attr);		
}
