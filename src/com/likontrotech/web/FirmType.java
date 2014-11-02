package com.likontrotech.web;

import java.io.Serializable;

public class FirmType implements Serializable {
	Integer id;
	String name;

	public FirmType(Integer id,	String name){
		this.id = id;
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
