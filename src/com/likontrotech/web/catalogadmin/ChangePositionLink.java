package com.likontrotech.web.catalogadmin;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import com.likontrotech.ejb.entities.Attribute;

public abstract class ChangePositionLink<T> extends Link<T> {
	public Integer current;

	public ChangePositionLink(String id, IModel<T> model, Integer current) {
		super(id, model);
		this.current = current;
	}
}