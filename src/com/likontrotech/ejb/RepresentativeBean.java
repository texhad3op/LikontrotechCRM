package com.likontrotech.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.likontrotech.ejb.base.BaseBean;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.Representative;

@Stateless(name = "RepresentativeEJB")
public class RepresentativeBean extends BaseBean<Representative> implements RepresentativeLocal {

	public Representative find(Object id) {
		return em.find(Representative.class, id);
	}

	public List<Representative> findAll() {
		return em.createQuery("select object(o) from Representative as o order by o.eventTime").getResultList();
	}

	public void remove(Long id) {
		Query query = em.createQuery("delete from Representative as r where r.id = :id");
		query.setParameter("id", id);
		query.executeUpdate();
	}
	
	public void removeRepresentativesOfCompany(Company company) {
		Query query = em.createQuery("delete from Representative as r where r.company.id = :id");
		query.setParameter("id", company.getId());
		query.executeUpdate();
	}
	
	public Representative getRepresentativeOfCompany(Company company) {
		try{
		return (Representative) em
				.createQuery("select object(r) from Representative as r where r.company.id = :companyId and r.isdefault = true")
				.setParameter("companyId", company.getId()).getSingleResult();
		}catch(NoResultException nre){
			return null;
		}
	}

	public List<Representative> getRepresentativesOfCompany(Company company) {
		return (List<Representative>) em.createQuery("select object(r) from Representative as r where r.company.id = :companyId")
				.setParameter("companyId", company.getId()).getResultList();
	}
	
	public void setRepresentativeAsDefault(Representative representative){
		Company company = representative.getCompany();
		List<Representative> representatives = getRepresentativesOfCompany(company);
		for(Representative rep:representatives){
			rep.setIsdefault(false);
			edit(rep);
		}
		representative.setIsdefault(true);
		edit(representative);
	}
}
