package com.likontrotech.ejb;

import java.util.List;

import javax.ejb.Stateless;

import com.likontrotech.ejb.base.BaseBean;
import com.likontrotech.ejb.entities.WarehouseOperation;

@Stateless(name = "WarehouseEJB")
public class WarehouseBean extends BaseBean<WarehouseOperation> implements WarehouseLocal {

	@Override
	public WarehouseOperation find(Object id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WarehouseOperation> findAll() {
		return em.createQuery("select object(wo) from WarehouseOperation as wo order by wo.registered").getResultList();
	}

	public List<Object[]> getWarehouseOperations() {
		return (List<Object[]>)em
				.createQuery("select wo, catalogElement from WarehouseOperation as wo inner join wo.catalogElement as catalogElement order by wo.registered desc")						
				.getResultList();
	}		
	
	
}
