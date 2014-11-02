package com.likontrotech.ejb;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.Query;

import com.likontrotech.ejb.base.BaseLocal;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.CompanyType;


@Local
public interface CompanyLocal extends BaseLocal<Company> {
	List<Object[]> search(String companyName, CompanyType companyType);	
	List<Object[]> yearReport(final List<String> months);
	void convertCompanies();
	void deleteCompany(Company company);
	Query getQuery();	
	List<Company> findAll(int first, int count);	
	List<Object[]> getList(String companyName, CompanyType companyType, int first, int count);
	int getCount(String companyName, CompanyType companyType);
}