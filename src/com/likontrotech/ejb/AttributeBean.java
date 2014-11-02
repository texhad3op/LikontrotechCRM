package com.likontrotech.ejb;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.likontrotech.ejb.base.BaseBean;
import com.likontrotech.ejb.entities.Attribute;
import com.likontrotech.ejb.entities.CatalogElement;

@Stateless(name = "AttributeEJB")
public class AttributeBean extends BaseBean<Attribute> implements
		AttributeLocal {

	public Attribute find(Object id) {
		return em.find(Attribute.class, id);
	}

	public List<Attribute> findAll() {
		return em.createQuery(
				"select object(o) from Attribute as o order by o.eventTime")
				.getResultList();
	}

	public void remove(Long id) {
		Query query = em
				.createQuery("delete from Attribute as event where event.id = :id");
		query.setParameter("id", id);
		query.executeUpdate();
	}

	public List<Attribute> findAll(CatalogElement catalogElement) {
		return (List<Attribute>) em
				.createQuery(
						"select object(o) from Attribute as o where"
								+ " o.catalogElement.id = :catalogElementId order by o.orderNumber")
				.setParameter("catalogElementId", catalogElement.getId())
				.getResultList();
	}

//	public BigInteger getAttributesFirstIdCatalogElement(
//			CatalogElement catalogElement) {
//		BigInteger cnt = (BigInteger) em
//				.createNativeQuery(
//						"SELECT id FROM attributes where catalog_element_id = :catalog_element_id order by ordernumber asc limit 1")
//				.setParameter("catalog_element_id", catalogElement.getId())
//				.getSingleResult();
//		return cnt;
//	}
//
//	public BigInteger getAttributesLastIdCatalogElement(
//			CatalogElement catalogElement) {
//		BigInteger cnt = (BigInteger) em
//				.createNativeQuery(
//						"SELECT id FROM attributes where catalog_element_id = :catalog_element_id order by ordernumber desc limit 1")
//				.setParameter("catalog_element_id", catalogElement.getId())
//				.getSingleResult();
//		return cnt;
//	}	
	
	public BigInteger getAttributesCountOfCatalogElement(
			CatalogElement catalogElement) {
		BigInteger cnt = (BigInteger) em
				.createNativeQuery(
						"SELECT count(id) as cnt FROM attributes where catalog_element_id = :catalog_element_id")
				.setParameter("catalog_element_id", catalogElement.getId())
				.getSingleResult();
		return cnt;
	}	
	
	public void moveBottom(Attribute attr) {
		Attribute attribute = em.find(Attribute.class, attr.getId());
		List<Attribute> attributes = (List<Attribute>) em
				.createQuery(
						"select object(attr) from Attribute as attr where"
								+ " attr.catalogElement.id = :catalogElementId and attr.orderNumber > :secondNumber order by attr.orderNumber asc")
				.setParameter("catalogElementId",
						attribute.getCatalogElement().getId())
				.setParameter("secondNumber", attribute.getOrderNumber())
				.getResultList();
		
		Attribute secondAttribute = attributes.get(0);
		Integer tmp = attribute.getOrderNumber();
		attribute.setOrderNumber(secondAttribute.getOrderNumber());
		secondAttribute.setOrderNumber(tmp);
		em.persist(attribute);
		em.persist(secondAttribute);
	}

	public void moveTop(Attribute attr) {
		Attribute attribute = em.find(Attribute.class, attr.getId());
		List<Attribute> attributes = (List<Attribute>) em
				.createQuery(
						"select object(attr) from Attribute as attr where"
								+ " attr.catalogElement.id = :catalogElementId and attr.orderNumber < :secondNumber order by attr.orderNumber desc")
				.setParameter("catalogElementId",
						attribute.getCatalogElement().getId())
				.setParameter("secondNumber", attribute.getOrderNumber())
				.getResultList();
		
		Attribute secondAttribute = attributes.get(0);
		Integer tmp = attribute.getOrderNumber();
		attribute.setOrderNumber(secondAttribute.getOrderNumber());
		secondAttribute.setOrderNumber(tmp);
		em.persist(attribute);
		em.persist(secondAttribute);
	}	
}
