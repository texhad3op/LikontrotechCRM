package com.likontrotech.ejb;

import java.util.List;

import javax.ejb.Local;

import com.likontrotech.ejb.base.BaseLocal;
import com.likontrotech.ejb.entities.CatalogElement;

@Local
public interface CatalogLocal extends BaseLocal<CatalogElement> {
	public CatalogElement find(Object id);

	List<CatalogElement> findSuperNodes(String findString);

	List<CatalogElement> findSuperNodes();

	List<CatalogElement> findAllOfCatalogElement(CatalogElement catalogElement, String findString);

	void changeState(CatalogElement catalogElement, Integer type);

	void delete(CatalogElement catalogElement);

	void delete2(CatalogElement catalogElement);

	void moveBottom(CatalogElement ce);

	void moveTop(CatalogElement ce);
}
