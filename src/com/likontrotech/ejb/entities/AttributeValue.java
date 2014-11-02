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
@Table(name = "attributes_values")
public class AttributeValue extends BaseEntity{

	public AttributeValue(){
	}
	
	@Id
	@GeneratedValue(generator = "attributeValueSeqName")
	@GenericGenerator(name = "attributeValueSeqName", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "attribute_val_id_seq") })
	private Long id;	
	
	@Column(name = "value")
	private String value;    

    @ManyToOne
    @JoinColumn(name="catalog_element_id")
    private CatalogElement catalogElement;	
    
    @ManyToOne
    @JoinColumn(name="attribute_id")
    private Attribute attribute;	    
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}	

}

