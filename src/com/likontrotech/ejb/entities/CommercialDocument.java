package com.likontrotech.ejb.entities;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.likontrotech.ejb.Utils;
import com.likontrotech.ejb.base.BaseEntity;

@Entity
@Table(name = "commercial_documents")
public class CommercialDocument extends BaseEntity {
	@Id
	@GeneratedValue(generator = "commercialDocumentSeqName")
	@GenericGenerator(name = "commercialDocumentSeqName", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "commercial_document_id_seq") })
	private Long id;

	@Column(name = "type")
	private Integer type = CommercialDocumentType.COMMERCIAL_OFFER;

	public CommercialDocumentType getCommercialDocumentType() {
		return Utils.getCommercialDocumentType(type);
	}

	public void setCommercialDocumentType(CommercialDocumentType commercialDocumentType) {
		this.type = commercialDocumentType.getId();
	}	
	
	@ManyToOne
	@JoinColumn(name = "event_id")
	private Event event;
	
	
	@Column(name = "price")	
	private BigDecimal price;
	
	@Column(name = "real_price")		
	private BigDecimal realPrice;
	
	@Column(name = "discount")		
	private BigDecimal discount;
	
	@Lob
	@Column(name = "formedPdf")
	@Basic(fetch = FetchType.LAZY)
	private byte[] formedPdf;

	public Long getId() {
		return id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getFormedPdf() {
		return formedPdf;
	}

	public void setFormedPdf(byte[] formedPdf) {
		this.formedPdf = formedPdf;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(BigDecimal realPrice) {
		this.realPrice = realPrice;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
}
