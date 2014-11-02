package com.likontrotech.ejb.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.likontrotech.ejb.base.BaseEntity;

@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity {

	@Id
	@GeneratedValue(generator = "picturesSeqName")
	@GenericGenerator(name = "picturesSeqName", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "pictures_id_seq") })
	private Long id;

	@Column(name = "name")
	private String name;

	@Lob
	@Column(name = "picture")
	@Basic(fetch = FetchType.LAZY)
	private byte[] picture;

	@Column(name = "mineType")
	private String mineType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public String getMimeType() {
		return mineType;
	}

	public void setMineType(String mineType) {
		this.mineType = mineType;
	}

}
