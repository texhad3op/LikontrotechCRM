package com.likontrotech.ejb;

import java.util.List;

import javax.ejb.Stateless;

import com.likontrotech.ejb.base.BaseBean;
import com.likontrotech.ejb.entities.CommercialDocumentLine;
import com.likontrotech.ejb.entities.CommercialDocumentType;

@Stateless(name = "CommercialDocumentLineEJB")
public class CommercialDocumentLineBean extends
		BaseBean<CommercialDocumentLine> implements CommercialDocumentLineLocal {
	public List<CommercialDocumentLine> findAll() {
		return em.createQuery(
				"select object(o) from CommercialDocumentLine as cd")
				.getResultList();
	}

	public List<CommercialDocumentLine> getCommercialDocumentLines(
			Long commercialDocumentId) {

		return em
				.createQuery(
						"select object(cdl) from CommercialDocumentLine as cdl "
								+ "where cdl.commercialDocumentId = :commercialDocumentId order by orderNumber")
				.setParameter("commercialDocumentId", commercialDocumentId)
				.getResultList();
	}
}
