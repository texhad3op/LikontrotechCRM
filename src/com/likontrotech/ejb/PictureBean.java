package com.likontrotech.ejb;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.likontrotech.ejb.base.BaseBean;
import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.ejb.entities.Picture;

@Stateless(name = "PictureEJB")
public class PictureBean extends BaseBean<Picture> implements PictureLocal {

	public Picture find(Object id) {
		return em.find(Picture.class, id);
	}

	public List<Picture> findAll() {
		return em.createQuery(
				"select object(o) from Picture as o order by o.eventTime")
				.getResultList();
	}

	public void remove(Long id) {
		Query query = em
				.createQuery("delete from Picture as picture where picture.id = :id");
		query.setParameter("id", id);
		query.executeUpdate();
	}
	
	public BigInteger getPictureId(CatalogElement catalogElement) {
		String sql = "select ce.picturet_id from catalog_elements ce where ce.id = :ce_id";
		Query q = em.createNativeQuery(sql).setParameter("ce_id", catalogElement.getId());
		return (BigInteger)q.getSingleResult();
	}		
}