package com.likontrotech.ejb;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Local;

import com.likontrotech.ejb.base.BaseLocal;
import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.ejb.entities.Picture;

@Local
public interface PictureLocal extends BaseLocal<Picture> {
	Picture find(Object id);

	List<Picture> findAll();

	void remove(Long id);

	BigInteger getPictureId(CatalogElement catalogElement);
}
