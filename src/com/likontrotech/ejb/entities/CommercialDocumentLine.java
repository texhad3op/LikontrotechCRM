package com.likontrotech.ejb.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.likontrotech.ejb.base.BaseEntity;

@Entity
@Table(name = "commercial_document_lines")
public class CommercialDocumentLine extends BaseEntity {

	@Id
	@GeneratedValue(generator = "commercialDocumentLineSeqName")
	@GenericGenerator(name = "commercialDocumentLineSeqName", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "commercial_document_line_id_seq") })
	private Long id;

	@Column(name = "catalog_element_id")
	private Long catalogElementId;

	@Column(name = "order_number")
	private Integer orderNumber;	
	
	@Column(name = "real_price")
	private BigDecimal realPrice;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "commercial_document_id")
	private Long commercialDocumentId;	
	
	private transient boolean showPicture = true;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCatalogElementId() {
		return catalogElementId;
	}

	public void setCatalogElementId(Long catalogElementId) {
		this.catalogElementId = catalogElementId;
	}

	public BigDecimal getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(BigDecimal realPrice) {
		this.realPrice = realPrice;
	}

	public Integer getQuantity() {
		return null == quantity?0:quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Long getCommercialDocumentId() {
		return commercialDocumentId;
	}

	public void setCommercialDocumentId(Long commercialDocumentId) {
		this.commercialDocumentId = commercialDocumentId;
	}

	public boolean isShowPicture() {
		return showPicture;
	}

	public void setShowPicture(boolean showPicture) {
		this.showPicture = showPicture;
	}
	
}
