package com.likontrotech.web.catalog;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.model.IModel;

public abstract class CollapseLink<T> extends AjaxFallbackLink<T> {
	public CollapseLink(String id, IModel<T> model) {
		super(id, model);
		setVisible(true);
		setOutputMarkupId(true);
	}
}