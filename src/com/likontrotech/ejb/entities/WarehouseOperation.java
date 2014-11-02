package com.likontrotech.ejb.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.likontrotech.ejb.Utils;
import com.likontrotech.ejb.base.BaseEntity;

@Entity
@Table(name = "warehouse_operations")
public class WarehouseOperation extends BaseEntity {

	public WarehouseOperation() {
	}

	@Id
	@GeneratedValue(generator = "warehouseOperationSeqName")
	@GenericGenerator(name = "warehouseOperationSeqName", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "operation_id_seq") })
	private Long id;

	@Column(name = "operationDate")
	private Timestamp operationDate;

	@ManyToOne
	@JoinColumn(name = "catalogElementId")
	private CatalogElement catalogElement;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "quantityFinal")
	private Integer quantityFinal;

	@Column(name = "type")
	private Integer type = 0;

	public WarehouseOperationType getType() {
		return Utils.getWarehouseOperationType(type);
	}

	public void setType(WarehouseOperationType warehouseOperationType) {
		this.type = warehouseOperationType.getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Timestamp operationDate) {
		this.operationDate = operationDate;
	}

	public CatalogElement getCatalogElement() {
		return catalogElement;
	}

	public void setCatalogElement(CatalogElement catalogElement) {
		this.catalogElement = catalogElement;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getQuantityFinal() {
		return quantityFinal;
	}

	public void setQuantityFinal(Integer quantityFinal) {
		this.quantityFinal = quantityFinal;
	}

}
