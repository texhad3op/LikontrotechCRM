package com.likontrotech.ejb;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;

import com.likontrotech.ejb.entities.CatalogElement;

public class CatalogElementMonitor {
	@PersistenceContext
	public EntityManager em;	
	public CatalogElementMonitor(){
		
	}
	
	@PrePersist
	public void preCatalogElement(CatalogElement catalogElement) {
		System.out.println(catalogElement.getId());
		System.out.println();
	}
	@PostPersist	
	public void postCatalogElement(CatalogElement atalogElement) {
//		System.out.println(atalogElement.getId());
//		System.out.println();
//		atalogElement.setOrderNumber(atalogElement.getId().intValue());
//		em.createNativeQuery("update catalog_elements set oder_number = id where id = :id").setParameter("id", atalogElement.getId());
//		System.out.println();
	}	
}
