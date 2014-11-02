package com.likontrotech.ejb.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.likontrotech.ejb.Utils;
import com.likontrotech.ejb.base.BaseEntity;

@Entity
@Table(name = "companies")
public class Company extends BaseEntity {

	@Id
	@GeneratedValue(generator = "companiesSeqName")
	@GenericGenerator(name = "companiesSeqName", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "companies_id_seq") })
	private Long id;

	@Column(name = "name", length = 500, nullable = true)
	private String name;

	@Column(name = "description", length = 2000, nullable = true)
	private String description;

	@Column(name = "address", length = 50, nullable = true)
	private String address;

	@Column(name = "companyCode", length = 50, nullable = true)
	private String companyCode;

	@Column(name = "vatCode", length = 50, nullable = true)
	private String vatCode;

	@Column(name = "site", length = 50, nullable = true)
	private String site;

	@Column(name = "postIndex", length = 50, nullable = true)
	private String postIndex;

	@Column(name = "type")
	private Integer type = 0;

	public CompanyType getType() {
		return Utils.getCompanyType(type);
	}

	public void setType(CompanyType companyType) {
		this.type = companyType.getId();
	}

	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private List<Representative> representatives = new ArrayList<Representative>();

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getPostIndex() {
		return postIndex;
	}

	public void setPostIndex(String postIndex) {
		this.postIndex = postIndex;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getVatCode() {
		return vatCode;
	}

	public void setVatCode(String vatCode) {
		this.vatCode = vatCode;
	}

	public List<Representative> getRepresentatives() {
		return representatives;
	}

	public void setRepresentatives(List<Representative> representatives) {
		this.representatives = representatives;
	}
}
