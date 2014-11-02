package com.likontrotech.ejb;

import java.util.List;

import javax.ejb.Local;

import com.likontrotech.ejb.base.BaseLocal;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.Representative;

@Local
public interface RepresentativeLocal extends BaseLocal<Representative> {
	public Representative find(Object id);

	public List<Representative> findAll();

	void remove(Long id);

	Representative getRepresentativeOfCompany(Company company);

	List<Representative> getRepresentativesOfCompany(Company company);

	void setRepresentativeAsDefault(Representative representative);
	void removeRepresentativesOfCompany(Company company);	
}
