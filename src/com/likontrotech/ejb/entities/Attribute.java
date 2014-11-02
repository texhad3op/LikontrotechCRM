package com.likontrotech.ejb.entities;

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
@Table(name = "attributes")
public class Attribute extends BaseEntity {

	public Attribute() {
	}

	@Id
	@GeneratedValue(generator = "attributeSeqName")
	@GenericGenerator(name = "attributeSeqName", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "attribute_id_seq") })
	private Long id;

	@Column(name = "name")
	private String name;

	@ManyToOne
	@JoinColumn(name = "catalog_element_id")
	private CatalogElement catalogElement;

	@Column(name = "orderNumber")
	private Integer orderNumber;

	@Column(name = "showForCommercialOffer")
	private boolean showForCommercialOffer;	

	@Column(name = "showForInvoice")
	private boolean showForInvoice;		

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CatalogElement getCatalogElement() {
		return catalogElement;
	}

	public void setCatalogElement(CatalogElement catalogElement) {
		this.catalogElement = catalogElement;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public boolean isShowForCommercialOffer() {
		return showForCommercialOffer;
	}

	public void setShowForCommercialOffer(boolean showForCommercialOffer) {
		this.showForCommercialOffer = showForCommercialOffer;
	}

	public boolean isShowForInvoice() {
		return showForInvoice;
	}

	public void setShowForInvoice(boolean showForInvoice) {
		this.showForInvoice = showForInvoice;
	}

}
