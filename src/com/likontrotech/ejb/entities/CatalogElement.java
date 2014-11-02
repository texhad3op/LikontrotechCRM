package com.likontrotech.ejb.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.likontrotech.ejb.base.BaseEntity;

@Entity
@Table(name = "catalog_elements")
@EntityListeners(com.likontrotech.ejb.CatalogElementMonitor.class)
public class CatalogElement extends BaseEntity {

	public CatalogElement() {
		this(0l, "", 0, 0l, 1);
	}

	public CatalogElement(Long id, String name, int type, Long parentId,
			Integer orderNumber) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.parentId = parentId;
		this.orderNumber = orderNumber;
	}

	@Id
	@GeneratedValue(generator = "catalogElementSeqName")
	@GenericGenerator(name = "catalogElementSeqName", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "catalog_element_id_seq") })
	private Long id;

	@OneToOne
	@JoinColumn(name = "picturet_id")
	private Picture picture;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private Integer type;

	@Column(name = "parent_id")
	private Long parentId;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "isShown")
	private Boolean isShown;

	private transient BigDecimal realPrice;

	private transient Integer quantity;

	private transient boolean showPicture = true;

	private transient List<CatalogElement> children = new ArrayList<CatalogElement>();

	@Column(name = "orderNumber")
	private Integer orderNumber;

	@Column(name = "quantityOnWarehouse")
	private Integer quantityOnWarehouse;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return null == id?0:id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object other) {
		if (null == other)
			return false;
		if (!(other instanceof CatalogElement))
			return false;
		if (((CatalogElement) other).getId().equals(getId()))
			return true;
		else
			return false;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Boolean getIsShown() {
		return isShown;
	}

	public void setIsShown(Boolean isShown) {
		this.isShown = isShown;
	}

	public boolean isShowPicture() {
		return showPicture;
	}

	public void setShowPicture(boolean showPicture) {
		this.showPicture = showPicture;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<CatalogElement> getChildren() {
		return children;
	}

	public void setChildren(List<CatalogElement> children) {
		this.children = children;
	}

	public Integer getQuantityOnWarehouse() {
		return quantityOnWarehouse;
	}

	public void setQuantityOnWarehouse(Integer quantityOnWarehouse) {
		this.quantityOnWarehouse = quantityOnWarehouse;
	}

}
