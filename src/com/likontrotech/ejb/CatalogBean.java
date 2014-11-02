package com.likontrotech.ejb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.likontrotech.ejb.base.BaseBean;
import com.likontrotech.ejb.entities.Attribute;
import com.likontrotech.ejb.entities.CatalogElement;

@Stateless(name = "CatalogEJB")
public class CatalogBean extends BaseBean<CatalogElement> implements CatalogLocal {

	public CatalogElement find(Object id) {
		CatalogElement o = em.find(CatalogElement.class, id);
		em.refresh(o);
		return o;
	}

	public List<CatalogElement> findAll() {

		List<CatalogElement> res = new ArrayList<CatalogElement>();
		List<Object[]> objs = em
				.createNativeQuery(
						"WITH RECURSIVE subcatalog_elements AS"
								+ " (SELECT * FROM catalog_elements WHERE id = 0"
								+ "   UNION ALL"
								+ "   SELECT el.* FROM catalog_elements AS el, subcatalog_elements AS sel "
								+ " WHERE el.parent_id = sel.id "
								+ " )SELECT id,name,type,parent_id,ordernumber FROM subcatalog_elements where id != 0 and isShown = true order by parent_id,ordernumber nulls first")
				.getResultList();
		for (Object[] obj : objs) {
			CatalogElement catalogElement = new CatalogElement();
			catalogElement.setId(Long.parseLong(String.valueOf(obj[0])));
			catalogElement.setName(String.valueOf(obj[1]));
			catalogElement.setType(Integer.parseInt(String.valueOf(obj[2])));
			catalogElement.setParentId(Long.parseLong(String.valueOf(obj[3])));
			res.add(catalogElement);
		}
		return res;
	}

	public List<CatalogElement> findSuperNodes(String findString) {
		if (null == findString)
			return findSuperNodes();
		else
			return findFilteredSuperNodes(findString);
	}

	public List<CatalogElement> findSuperNodes() {
		List<CatalogElement> res = new ArrayList<CatalogElement>();
		List<Object[]> objs = em
				.createNativeQuery(
						"WITH RECURSIVE subcatalog_elements AS"
								+ " (SELECT * FROM catalog_elements WHERE id = 0"
								+ "   UNION ALL"
								+ "   SELECT el.* FROM catalog_elements AS el, subcatalog_elements AS sel "
								+ "     WHERE el.parent_id = sel.id "
								+ " )SELECT id,name,type,parent_id,ordernumber FROM subcatalog_elements where id != 0 and type = 0 and isShown = true order by parent_id,ordernumber asc nulls first")
				.getResultList();
		for (Object[] obj : objs) {
			CatalogElement catalogElement = new CatalogElement();
			catalogElement.setId(Long.parseLong(String.valueOf(obj[0])));
			catalogElement.setName(String.valueOf(obj[1]));
			catalogElement.setType(Integer.parseInt(String.valueOf(obj[2])));
			catalogElement.setParentId(Long.parseLong(String.valueOf(obj[3])));
			catalogElement.setOrderNumber(Integer.parseInt(String.valueOf(obj[4])));			
			res.add(catalogElement);
		}
		return res;
	}

	private List<CatalogElement> findFilteredSuperNodes(String findString) {
		String ids = getIds(findAllIdsByHierahy(findString));
		List<CatalogElement> res = new ArrayList<CatalogElement>();
		List<Object[]> objs = em
				.createNativeQuery(
						"WITH RECURSIVE subcatalog_elements AS"
								+ " ( SELECT * FROM catalog_elements WHERE id in ("
								+ ids
								+ ")"
								+ "   UNION ALL"
								+ "   SELECT el.* FROM catalog_elements AS el, subcatalog_elements AS sel"
								+ "     WHERE el.id = sel.parent_id"
								+ " )SELECT id,name,type,parent_id,ordernumber FROM subcatalog_elements where id != 0 and type = 0 and isShown = true order by parent_id,ordernumber asc nulls first")
				.getResultList();
		for (Object[] obj : objs) {
			CatalogElement catalogElement = new CatalogElement();
			catalogElement.setId(Long.parseLong(String.valueOf(obj[0])));
			catalogElement.setName(String.valueOf(obj[1]));
			catalogElement.setType(Integer.parseInt(String.valueOf(obj[2])));
			catalogElement.setParentId(Long.parseLong(String.valueOf(obj[3])));
			catalogElement.setOrderNumber(Integer.parseInt(String.valueOf(obj[4])));				
			res.add(catalogElement);
		}
		return res;
	}

	private String getIds(List<BigInteger> ids) {
		if (0 == ids.size())
			return "-1";
		else if (1 == ids.size())
			return String.valueOf(ids.get(0));
		else {
			String ret = "-1";
			for (BigInteger id : ids) {
				ret += "," + id;
			}
			return ret;
		}
	}

	private List<BigInteger> findAllIdsByHierahy(String findString) {
		List<BigInteger> ids = (List<BigInteger>) em.createNativeQuery(
				"select id from catalog_elements where isShown = true and UPPER(name) like UPPER('%" + findString + "%') and id != 0")
				.getResultList();
		ids.add(new BigInteger("0"));
		return ids;
	}

	public List<CatalogElement> findAllOfCatalogElement(CatalogElement catalogElement, String findString) {
		String qry = "select ce from CatalogElement as ce where ce.parentId = :parentCatalogElementId and ce.isShown = true";
		if (null != findString && "".equals(findString))
			qry += " and UPPER(ce.name) like UPPER('%" + findString + "%')";
		qry += " order by ce.orderNumber";
		return (List<CatalogElement>) em.createQuery(qry).setParameter("parentCatalogElementId", catalogElement.getId()).getResultList();
	}

	public void changeState(CatalogElement catalogElement, Integer type) {
		if (1 == type) {// node
			if (false == hasChildren(catalogElement)) {
				CatalogElement ce = em.find(CatalogElement.class, catalogElement.getId());
				ce.setType(1);
				em.persist(ce);
			}
		} else {// super
			CatalogElement ce = em.find(CatalogElement.class, catalogElement.getId());
			ce.setType(0);
			em.persist(ce);
		}
	}

	public boolean hasChildren(CatalogElement catalogElement) {
		List<Attribute> list = (List<Attribute>) em
				.createQuery("select object(o) from CatalogElement as o where o.parentId = :catalogElementId and o.isShown = true")
				.setParameter("catalogElementId", catalogElement.getId()).getResultList();
		if (0 == list.size())
			return false;
		else
			return true;
	}

	public void delete(CatalogElement catalogElement) {
		if (false == hasChildren(catalogElement)) {
			em.remove(em.find(CatalogElement.class, catalogElement.getId()));
		}
	}

	public void delete2(CatalogElement catalogElement) {
		removeAttributeValuesOfCatalogElement(catalogElement);
		deleteSelf(catalogElement);
	}

	private void deleteSelf(CatalogElement catalogElement) {
		Query query = em.createNativeQuery("delete from catalog_elements where id = :id");
		query.setParameter("id", catalogElement.getId());
		query.executeUpdate();
	}

	public void removeAttributeValuesOfCatalogElement(CatalogElement catalogElement) {
		Query query = em.createNativeQuery("delete from attributes_values where catalog_element_id = :id");
		query.setParameter("id", catalogElement.getId());
		query.executeUpdate();
	}

	public void moveBottom(CatalogElement ce) {
		CatalogElement catalogElement = em.find(CatalogElement.class, ce.getId());
		List<CatalogElement> catalogElements = (List<CatalogElement>) em
				.createQuery(
						"select object(ce) from CatalogElement as ce where"
								+ " ce.parentId = :parentId and ce.orderNumber > :secondNumber order by ce.orderNumber asc")
				.setParameter("parentId", catalogElement.getParentId()).setParameter("secondNumber", catalogElement.getOrderNumber())
				.getResultList();

		CatalogElement secondCatalogElement = catalogElements.get(0);
		Integer tmp = catalogElement.getOrderNumber();
		catalogElement.setOrderNumber(secondCatalogElement.getOrderNumber());
		secondCatalogElement.setOrderNumber(tmp);
		em.persist(catalogElement);
		em.persist(secondCatalogElement);
	}

	public void moveTop(CatalogElement ce) {
		CatalogElement catalogElement = em.find(CatalogElement.class, ce.getId());
		List<CatalogElement> catalogElements = (List<CatalogElement>) em
				.createQuery(
						"select object(ce) from CatalogElement as ce where"
								+ " ce.parentId = :parentId and ce.orderNumber < :secondNumber order by ce.orderNumber desc")
				.setParameter("parentId", catalogElement.getParentId()).setParameter("secondNumber", catalogElement.getOrderNumber())
				.getResultList();

		CatalogElement secondCatalogElement = catalogElements.get(0);
		Integer tmp = catalogElement.getOrderNumber();
		catalogElement.setOrderNumber(secondCatalogElement.getOrderNumber());
		secondCatalogElement.setOrderNumber(tmp);
		em.persist(catalogElement);
		em.persist(secondCatalogElement);
	}
}
