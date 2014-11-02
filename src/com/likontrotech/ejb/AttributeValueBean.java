package com.likontrotech.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.likontrotech.ejb.base.BaseBean;
import com.likontrotech.ejb.entities.Attribute;
import com.likontrotech.ejb.entities.AttributeValue;
import com.likontrotech.ejb.entities.CatalogElement;

@Stateless(name = "AttributeValueEJB")
public class AttributeValueBean extends BaseBean<AttributeValue> implements
		AttributeValueLocal {

	public AttributeValue find(Object id) {
		return em.find(AttributeValue.class, id);
	}

	public List<AttributeValue> findAll() {
		return em
				.createQuery(
						"select object(o) from AttributeValue as o order by o.eventTime")
				.getResultList();
	}

	public void remove(Long id) {
		Query query = em
				.createQuery("delete from AttributeValue as attributeValue where attributeValue.id = :id");
		query.setParameter("id", id);
		query.executeUpdate();
	}

	public AttributeValue load(CatalogElement catalogElement,
			Attribute attribute) {
		try {
			return (AttributeValue) em
					.createQuery(
							"select object(av) from AttributeValue as av where av.catalogElement.id = :catalogElementId and av.attribute.id = :attributeId")
					.setParameter("catalogElementId", catalogElement.getId())
					.setParameter("attributeId", attribute.getId())
					.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	public List<AttributeValue> getAttributValues(CatalogElement catalogElement) {
		return (List<AttributeValue>) em
				.createQuery(
						"select av from AttributeValue as av where av.catalogElement.id = :catalogElementId")
				.setParameter("catalogElementId", catalogElement.getId())
				.getResultList();
	}

	public List<Object[]> getAttributValues2(CatalogElement catalogElement) {
		return (List<Object[]>) em
				.createQuery(
						"select av,attribute from AttributeValue as av inner join av.attribute as attribute where av.catalogElement.id = :catalogElementId order by attribute.orderNumber")
				.setParameter("catalogElementId", catalogElement.getId())
				.getResultList();
	}

	public List<Object[]> getAttributValuesForCommercialOffer(
			CatalogElement catalogElement) {
		return (List<Object[]>) em
				.createQuery(
						"select av,attribute from AttributeValue as av inner join av.attribute as attribute where av.catalogElement.id = :catalogElementId and attribute.showForCommercialOffer = true order by attribute.orderNumber")
				.setParameter("catalogElementId", catalogElement.getId())
				.getResultList();
	}
	
	public List<Object[]> getAttributValuesForInvoice(
			CatalogElement catalogElement) {
		return (List<Object[]>) em
				.createQuery(
						"select av,attribute from AttributeValue as av inner join av.attribute as attribute where av.catalogElement.id = :catalogElementId and attribute.showForInvoice = true order by attribute.orderNumber")
				.setParameter("catalogElementId", catalogElement.getId())
				.getResultList();
	}	
}
