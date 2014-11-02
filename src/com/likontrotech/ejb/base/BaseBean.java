package com.likontrotech.ejb.base;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

public abstract class BaseBean<T extends BaseEntity> {
	@PersistenceContext
	public EntityManager em;
	
	public T find(Object id) {
		return (T)em.find(BaseEntity.class , id);
	}
	
	public void create(T t) {
		t.setRegistered(new Timestamp(System.currentTimeMillis()));
		em.persist(t);
	}
	
	public void edit(T t) {
		em.merge(t);
	}

	public void remove(T t) {
		em.remove(em.merge(t));
	}	
	

}
