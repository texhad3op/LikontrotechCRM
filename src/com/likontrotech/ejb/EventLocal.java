package com.likontrotech.ejb;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import com.likontrotech.ejb.base.BaseLocal;
import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.Event;
import com.likontrotech.ejb.entities.PaymentType;
import com.likontrotech.ejb.entities.Representative;

@Local
public interface EventLocal extends BaseLocal<Event> {
	public Event find(Object id);

	public List<Event> findAll();

	public List<Object[]> findAllOfCompany(Company company);

	public List<Event> findAllOfCompany(Company company, String date);

	void remove(Long id);

	List<Object[]> findAllScheduledEvents();

	Event getLastEvent(Company company);
}
