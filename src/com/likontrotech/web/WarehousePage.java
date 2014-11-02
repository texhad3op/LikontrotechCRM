package com.likontrotech.web;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.ejb.entities.WarehouseOperation;
import com.likontrotech.web.catalogadmin.TreeBasePage;

public class WarehousePage extends TreeBasePage {

	int cnt = 1;

	@SuppressWarnings("all")
	public WarehousePage() {

		final IModel modelWarehouseOperations = new LoadableDetachableModel() {
			@Override
			protected Object load() {
				return warehouseLocal.getWarehouseOperations();
			}
		};

		add(new ListView<Object[]>("warehouseOperationList", modelWarehouseOperations) {
			@Override
			protected void populateItem(ListItem<Object[]> item) {
				Object[] tuple = item.getModelObject();
				WarehouseOperation operation = (WarehouseOperation)tuple[0];
				CatalogElement catalogElement = (CatalogElement)tuple[1];				
				item.add(new Label("number", String.valueOf(WarehousePage.this.cnt++)));
				item.add(new Label("data", ConstantsUtil.sdf.format((java.util.Date) operation.getOperationDate())));
				item.add(new Label("quantity", String.valueOf(operation.getQuantity())));
				item.add(new Label("catalogElement", catalogElement.getName()));				
				item.add(new Label("operation", operation.getType().getName()));				
			}
		});
		tree.getTreeState().expandAll();
	}

	@Override
	public void onClick(Object obj, BaseTree bt, AjaxRequestTarget tar) {
		DefaultMutableTreeNode dtn = (DefaultMutableTreeNode) obj;
		CatalogElement ce = (CatalogElement) dtn.getUserObject();
		getLikontrotechCRMSession().setCatalogElement(ce);
		setResponsePage(WarehousePage.class);
	}

	@Override
	protected List<CatalogElement> getCatalogItems() {
		return catalogLocal.findAll();
	}
}
