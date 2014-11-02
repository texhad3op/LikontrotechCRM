package com.likontrotech.ejb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.likontrotech.ejb.base.BaseEntity;

@Entity
@Table(name = "representatives")
public class Representative extends BaseEntity {
	public Representative() {
	}

	@Id
	@GeneratedValue(generator = "representativeSeqName")
	@GenericGenerator(name = "representativeSeqName", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "representative_id_seq") })
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "surname")
	private String surname;

	@Column(name = "description", length = 2000, nullable = true)
	private String description;

	@Column(name = "phone1", length = 50, nullable = true)
	private String phone1;

	@Column(name = "phone2", length = 50, nullable = true)
	private String phone2;

	@Column(name = "phone3", length = 50, nullable = true)
	private String phone3;

	@Column(name = "mail", length = 200, nullable = true)
	private String mail;

	@Column(name = "cellular", length = 50, nullable = true)
	private String cellular;

	@Column(name = "fax", length = 50, nullable = true)
	private String fax;

	@Column(name = "isdefault", nullable = false)
	private Boolean isdefault;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return null == name ? "" : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return null == surname ? "" : surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getPhone3() {
		return phone3;
	}

	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getCellular() {
		return cellular;
	}

	public void setCellular(String cellular) {
		this.cellular = cellular;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Boolean getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(Boolean isdefault) {
		this.isdefault = isdefault;
	}

	public String getFullName() {
		return getName() + " " + getSurname();
	}

}
