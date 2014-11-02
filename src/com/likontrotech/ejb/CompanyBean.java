package com.likontrotech.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.likontrotech.ejb.base.BaseBean;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.CompanyType;

@Stateless(name = "CompanyEJB")
public class CompanyBean extends BaseBean<Company> implements CompanyLocal {

	@EJB(name = "RepresentativeEJB")
	public RepresentativeLocal representativeLocal;	
	
	@EJB(name = "EventEJB")
	public EventLocal eventLocal;	
	
	public Company find(Object id) {
		return em.find(Company.class, id);
	}

	public List<Company> findAll() {
		return em.createQuery("select object(o) from Company as o order by o.name")
				.getResultList();
	}

	public List<Company> findAll(int first, int count) {
		return em.createQuery("select object(o) from Company as o order by o.name").setFirstResult(first).setMaxResults(count)
				.getResultList();
	}	
	
	public Query getQuery() {
		return em.createQuery("select object(o) from Company as o order by o.name");
	}	
	
	public int getCount(String companyName, CompanyType companyType) {
		String s1 = "select count(comp.id)" +
				" from companies comp where 1=1";
		String s2 = null == companyName?"":" and UPPER(comp.name) like '%"+companyName.toUpperCase()+"%'";
		String s3 = null == companyType?"":" and comp.type = '"+companyType.getId()+"'";		
		Query q = em.createNativeQuery(s1+s2+s3);
		return Integer.parseInt(q.getSingleResult().toString());
	}		
	
	public List<Object[]> search(String companyName, CompanyType companyType) {
		String s1 = "select comp.id, comp.name," +
				" (select ev.description from representatives r inner join events ev on (r.id = ev.representative_id)" +
				" where r.company_id = comp.id and ev.type != 1 order by ev.time desc limit 1) lastevent "+ 
				", (select ev.time from representatives r inner join events ev on (r.id = ev.representative_id)" +
				" where r.company_id = comp.id and ev.type != 1 order by ev.time desc limit 1) lasteventdate "+
				" from companies comp where 1=1";
		String s2 = null == companyName?"":" and UPPER(comp.name) like '%"+companyName.toUpperCase()+"%'";
		String s3 = null == companyType?"":" and comp.type = '"+companyType.getId()+"'";		
		String s4 = " order by comp.name"; 
		Query q = em.createNativeQuery(s1+s2+s3+s4);
		return q.getResultList();
	}	
	
	public List<Object[]> yearReport(final List<String> months) {
		
		String pattern = ",(select count(e.id) from events e where e.company_id = c.id and to_char(e.time, 'YYYY-MM') = 'strd') as \"strd\"";
		
		String rez = "";
		for(String month:months){
			rez += pattern.replaceAll("strd", month);
		}
		String s1 = "select c.id, c.name"+ rez+" from companies c where 1=1 order by c.name";
		Query q = em.createNativeQuery(s1);
		return q.getResultList();
	}		

	public List<Object[]> getList(String companyName, CompanyType companyType, int first, int count) {
		String s1 = "select comp.id, comp.name," +
				" (select ev.description from representatives r inner join events ev on (r.id = ev.representative_id)" +
				" where r.company_id = comp.id and ev.type != 1 order by ev.time desc limit 1) lastevent "+ 
				", (select ev.time from representatives r inner join events ev on (r.id = ev.representative_id)" +
				" where r.company_id = comp.id and ev.type != 1 order by ev.time desc limit 1) lasteventdate "+
				" from companies comp where 1=1";
		String s2 = null == companyName?"":" and UPPER(comp.name) like '%"+companyName.toUpperCase()+"%'";
		String s3 = null == companyType?"":" and comp.type = '"+companyType.getId()+"'";		
		String s4 = " order by comp.name"; 
		Query q = em.createNativeQuery(s1+s2+s3+s4);
		q.setFirstResult(first);
		q.setMaxResults(count);
		return q.getResultList();
	}		
	
	public void deleteCompany(Company company){
		representativeLocal.removeRepresentativesOfCompany(company);
		remove(company);
	}
	
	public void convertCompanies() {
//		List<Company> companies =  em.createQuery("select object(o) from Company as o order by o.name")
//				.getResultList();
//		
//		for(Company c:companies){
//			Representative r = new Representative();
//			r.setCellular(c.getCellular());
//			r.setFax(c.getFax());
//			r.setIsdefault(true);
//			r.setMail(c.getMail());
//			r.setName(c.getName());
//			r.setPhone1(c.getPhone1());
//			r.setPhone2(c.getPhone2());			
//			r.setPhone3(c.getPhone3());
//			r.setName(c.getRepresentativeName());
//			r.setSurname(c.getRepresentativeSurname());
//			r.setRegistered(new Timestamp(System.currentTimeMillis()));
//			r.setCompany(c);
//			
//			for(Event event:c.getEvents()){
//				event.setRepresentative(r);
//				eventLocal.edit(event);
//			}
//			
//			representativeLocal.create(r);
//		}
		
		
	}	
}
