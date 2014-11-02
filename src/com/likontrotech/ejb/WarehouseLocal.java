package com.likontrotech.ejb;

import java.util.List;

import javax.ejb.Local;

import com.likontrotech.ejb.base.BaseLocal;
import com.likontrotech.ejb.entities.WarehouseOperation;

@Local
public interface WarehouseLocal extends BaseLocal<WarehouseOperation> {
	List<Object[]> getWarehouseOperations();
}