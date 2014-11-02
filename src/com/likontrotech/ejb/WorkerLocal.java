package com.likontrotech.ejb;

import java.util.List;

import javax.ejb.Local;

import com.likontrotech.ejb.base.BaseLocal;
import com.likontrotech.ejb.entities.Worker;

@Local
public interface WorkerLocal extends BaseLocal<Worker> {
	public Worker find(Object id);

	public List<Worker> findAll();

	void remove(Long id);
	
	Worker getWorker(String login, String password);	
}
