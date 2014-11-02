package com.likontrotech.ejb;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import com.likontrotech.ejb.base.BaseBean;
import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.ejb.entities.CommercialDocument;
import com.likontrotech.ejb.entities.CommercialDocumentLine;
import com.likontrotech.ejb.entities.CommercialDocumentType;
import com.likontrotech.ejb.entities.Event;

@Stateless(name = "CommercialDocumentEJB")
public class CommercialDocumentBean extends BaseBean<CommercialDocument>
		implements CommercialDocumentLocal {

	@EJB(name = "CommercialDocumentLineEJB")
	public CommercialDocumentLineLocal commercialDocumentLineLocal;

	@EJB(name = "EventEJB")
	public EventLocal eventLocal;

	public CommercialDocument find(Object id) {
		return em.find(CommercialDocument.class, id);
	}

	public List<CommercialDocument> findAll() {
		return em.createQuery("select object(o) from CommercialDocument as cd")
				.getResultList();
	}

	public CommercialDocument getCommercialDocumentByEventId(Event event) {
		try {
			return (CommercialDocument) em
					.createQuery(
							"select object(cd) from CommercialDocument cd where cd.event.id = :eventId")
					.setParameter("eventId", event.getId()).getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	public void create(Event mailEvent, CommercialDocumentType type,
			byte[] pdf, List<CommercialDocumentLine> elements, BigDecimal price,
			BigDecimal priceWithDiscount, Integer discount) {

		CommercialDocument commercialDocument = new CommercialDocument();
		commercialDocument.setCommercialDocumentType(type);
		commercialDocument.setEvent(mailEvent);
		commercialDocument.setFormedPdf(pdf);

		commercialDocument.setPrice(price);
		commercialDocument.setRealPrice(priceWithDiscount);
		commercialDocument.setDiscount(new BigDecimal(null == discount?0:discount));
		create(commercialDocument);

		int order = 1;
		for (CommercialDocumentLine commercialDocumentLine : elements) {
			commercialDocumentLine.setId(null);
			commercialDocumentLine.setOrderNumber(order);
			commercialDocumentLine.setCommercialDocumentId(commercialDocument.getId());
			commercialDocumentLineLocal.create(commercialDocumentLine);
			order++;
		}

	}

	// public void migratePdfToCommercialDocument(){
	// List<Event> events = eventLocal.findAll();
	// for(Event event:events){
	// CommercialDocument commercialDocument = new CommercialDocument();
	// commercialDocument.setCommercialDocumentType(Utils.getCommercialDocumentType(CommercialDocumentType.COMMERCIAL_OFFER));
	// commercialDocument.setDiscount(new BigDecimal(0));
	// commercialDocument.setEvent(event);
	// //commercialDocument.setFormedPdf(event.getSentPdf());
	// commercialDocument.setPrice(new BigDecimal(0));
	// commercialDocument.setRealPrice(new BigDecimal(0));
	// create(commercialDocument);
	// }
	// }

	public List<CommercialDocument> getCommercialDocuments(Event event) {
		return em
				.createQuery(
						"select object(cd) from CommercialDocument as cd where cd.event.id = :id")
				.setParameter("id", event.getId()).getResultList();
	}

}
