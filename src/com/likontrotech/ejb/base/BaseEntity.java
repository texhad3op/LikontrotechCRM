package com.likontrotech.ejb.base;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity implements Serializable{
	@Column(name = "registered")
	private Timestamp registered = new Timestamp(System.currentTimeMillis());
	public Timestamp getRegistered(){
		return registered;
	}

	public void setRegistered(Timestamp registered){
		this.registered = registered;
	}
}
