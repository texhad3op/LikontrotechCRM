package com.likontrotech.ejb;

import javax.ejb.Local;


@Local
public interface SerialCodeLocal {
	String getSerialCodeAndIncrement();
	Integer getSerialCode();
	void setSerialCode(Integer value);
	String getSerialCodeString();	
}
