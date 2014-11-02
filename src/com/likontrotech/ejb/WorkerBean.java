package com.likontrotech.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.likontrotech.ejb.base.BaseBean;
import com.likontrotech.ejb.entities.Worker;

@Stateless(name = "WorkerEJB")
public class WorkerBean extends BaseBean<Worker> implements WorkerLocal {

	public Worker find(Object id) {
		return em.find(Worker.class, id);
	}

	public List<Worker> findAll() {
		return em.createQuery("select object(o) from Worker as o order by o.lastName, o.firstName").getResultList();
	}

	public void remove(Long id) {
		Query query = em.createQuery("delete from Worker as worker where worker.id = :id");
		query.setParameter("id", id);
		query.executeUpdate();
	}

	public Worker getWorker(String login, String password) {
		List<Worker> list = em.createQuery("select object(w) from Worker as w where w.userLogin = :l and w.userPassword = :p")
				.setParameter("l", login).setParameter("p", password).getResultList();
		if (0 == list.size())
			return null;
		return list.get(0);
	}
}
