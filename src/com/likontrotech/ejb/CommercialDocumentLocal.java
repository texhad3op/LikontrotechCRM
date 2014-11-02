package com.likontrotech.ejb;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import com.likontrotech.ejb.base.BaseLocal;
import com.likontrotech.ejb.entities.CommercialDocument;
import com.likontrotech.ejb.entities.CommercialDocumentLine;
import com.likontrotech.ejb.entities.CommercialDocumentType;
import com.likontrotech.ejb.entities.Event;

@Local
public interface CommercialDocumentLocal extends BaseLocal<CommercialDocument> {
	CommercialDocument getCommercialDocumentByEventId(Event event);

	CommercialDocument find(Object id);

	void create(Event mailEvent, CommercialDocumentType type, byte[] pdf,
			List<CommercialDocumentLine> elements, BigDecimal price,
			BigDecimal priceWithDiscount, Integer discount);

	List<CommercialDocument> getCommercialDocuments(Event event);
	// void migratePdfToCommercialDocument();
}
