package com.likontrotech.ejb.base;

import java.util.List;

public interface BaseLocal<T> {

	void create(T t);

	void edit(T t);

	void remove(T t);

	T find(Object id);

	List<T> findAll();
}
