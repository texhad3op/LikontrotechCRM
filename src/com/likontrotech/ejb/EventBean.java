package com.likontrotech.ejb;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.likontrotech.ejb.base.BaseBean;
import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.Event;
import com.likontrotech.ejb.entities.PaymentType;
import com.likontrotech.ejb.entities.Representative;

@Stateless(name = "EventEJB")
public class EventBean extends BaseBean<Event> implements EventLocal {

	public Event find(Object id) {
		return em.find(Event.class, id);
	}

	public List<Event> findAll() {
		return em
				.createQuery(
						"select object(o) from Event as o where o.sentPdf is not null order by o.eventTime")
				.getResultList();
	}

	public List<Object[]> findAllOfCompany(Company company) {
		return em
				.createNativeQuery(
						"	select event.id as eventId, event.time as eventTime, event.description as description, r.name, r.surname"
								+ " from events as event inner join representatives r on r.id = event.representative_id"
								+ " inner join companies as company on company.id = r.company_id"
								+ " where company.id = :companyId"
								+ " order by event.time desc")
				.setParameter("companyId", company.getId()).getResultList();
	}

	public void remove(Long id) {
		Query query1 = em
				.createNativeQuery("delete from commercial_document_lines where commercial_document_id in (select id from commercial_documents where event_id = :id)");
		query1.setParameter("id", id);
		query1.executeUpdate();

		Query query2 = em
				.createNativeQuery("delete from commercial_documents where event_id = :id");
		query2.setParameter("id", id);
		query2.executeUpdate();

		Query query = em
				.createQuery("delete from Event as event where event.id = :id");
		query.setParameter("id", id);
		query.executeUpdate();
	}

	public List<Object[]> findAllScheduledEvents() {
		return em
				.createNativeQuery(
						"	select event.id as eventId,event.time as eventTime, company.name as companyName,event.description as description, company.id as companyId"
								+ " from events as event inner join representatives r on r.id = event.representative_id"
								+ " inner join companies as company on company.id = r.company_id"
								+ " where event.type = 1 and CAST(event.time AS date) <= current_date order by event.time")
				.getResultList();

	}

	public List<Event> findAllOfCompany(Company company, String date) {
		return em
				.createQuery(
						"select object(o) from Event as o where o.company.id = :id and to_char(o.eventTime, 'YYYY-MM') = '"
								+ date + "') order by o.eventTime")
				.setParameter("id", company.getId()).getResultList();
	}

	public Event getLastEvent(Company company) {
		Object o = em
				.createNativeQuery(
						"select e.id from events e inner join representatives r on e.representative_id = r.id"
								+ " inner join companies c on c.id = r.company_id where c.id = :companyId and e.type != 1 order by e.time desc")
				.setParameter("companyId", company.getId()).setMaxResults(1)
				.getSingleResult();
		if (null == o)
			return null;
		return find(((BigInteger) o).longValue());
	}

}
