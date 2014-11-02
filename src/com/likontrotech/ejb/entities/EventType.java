package com.likontrotech.ejb.entities;


public class EventType extends ClassifierType {
	public static int REPORT = 0;
	public static int PLANNED = 1;	
	public EventType(int id, String name) {
		super(id, name);
	}
}
