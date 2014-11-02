package com.likontrotech.ejb;

import java.util.List;

import javax.ejb.Local;

import com.likontrotech.ejb.base.BaseLocal;
import com.likontrotech.ejb.entities.CommercialDocumentLine;

@Local
public interface CommercialDocumentLineLocal extends
		BaseLocal<CommercialDocumentLine> {
	public List<CommercialDocumentLine> getCommercialDocumentLines(
			Long commercialDocumentId);
}
