package com.likontrotech.web.catalogadmin;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.web.catalog.CatalogElementComponentShort;

public class TreePage extends TreeBasePage {
	Integer cnt = 1;
	Integer count = 1;

	public TreePage() {
		super();

		final TextField<String> name = new TextField<String>("name", new Model<String>(""));

		final IModel<List<CatalogElementComponentShort>> modelCatalogElements = new LoadableDetachableModel<List<CatalogElementComponentShort>>() {
			@Override
			protected List<CatalogElementComponentShort> load() {
				List<CatalogElementComponentShort> catalogElementComponentShortList = new ArrayList<CatalogElementComponentShort>();
				List<CatalogElement> catalogElements = catalogLocal.findAllOfCatalogElement(getLikontrotechCRMSession().getParentCatalogElement(), "");
				for (CatalogElement catalogElement : catalogElements) {
					CatalogElementComponentShort vc = new CatalogElementComponentShort(catalogElement);
					catalogElementComponentShortList.add(vc);
				}

				return catalogElementComponentShortList;
			}
		};
		count = modelCatalogElements.getObject().size();
		ListView<CatalogElementComponentShort> catalogElementList = new ListView<CatalogElementComponentShort>("catalogElementList",
				modelCatalogElements) {
			@Override
			protected void populateItem(ListItem<CatalogElementComponentShort> item) {
				CatalogElementComponentShort visualComponent = item.getModelObject();
				item.add(visualComponent);
				visualComponent.add(new ChangePositionLink<CatalogElementComponentShort>("bottomLink",
						new Model<CatalogElementComponentShort>(visualComponent), TreePage.this.cnt) {
					@Override
					public void onClick() {
						CatalogElementComponentShort visualComponent = getModelObject();
						CatalogElement catalogElement = visualComponent.getCatalogElement();
						catalogLocal.moveBottom(catalogElement);
						modelCatalogElements.detach();
						TreePage.this.cnt = 1;
						setResponsePage(TreePage.class);
					}

					@Override
					public boolean isVisible() {
						return !count.equals(current);
					}
				});

				visualComponent.add(new ChangePositionLink<CatalogElementComponentShort>("topLink",
						new Model<CatalogElementComponentShort>(visualComponent), TreePage.this.cnt) {
					@Override
					public void onClick() {
						CatalogElementComponentShort visualComponent = getModelObject();
						CatalogElement catalogElement = visualComponent.getCatalogElement();
						catalogLocal.moveTop(catalogElement);
						modelCatalogElements.detach();
						TreePage.this.cnt = 1;
						setResponsePage(TreePage.class);
					}

					@Override
					public boolean isVisible() {
						return 1 != current;
					}
				});

				TreePage.this.cnt++;
			}
		};
		add(catalogElementList);
		Form<CatalogElement> form = new Form<CatalogElement>("form") {
			@Override
			protected void onSubmit() {
				if (false == getLikontrotechCRMSession().isEdit()) {
					CatalogElement parent = getLikontrotechCRMSession().getParentCatalogElement();

					if (parent.getType().equals(new Integer(1))) {
						parent.setType(0);
						catalogLocal.edit(parent);
					}

					CatalogElement catalogElement = new CatalogElement();
					catalogElement.setName(name.getConvertedInput());
					catalogElement.setType(1);
					catalogElement.setParentId(getLikontrotechCRMSession().getParentCatalogElement().getId());
					catalogElement.setIsShown(true);
					catalogLocal.create(catalogElement);
					catalogElement.setOrderNumber(catalogElement.getId().intValue());
					catalogLocal.edit(catalogElement);
					setResponsePage(TreePage.class);
				} else {
					getLikontrotechCRMSession().setEdit(false);
					CatalogElement ce = catalogLocal.find(getLikontrotechCRMSession().getParentCatalogElement().getId());
					getLikontrotechCRMSession().setParentCatalogElement(ce);
					ce.setName(name.getModelObject());
					catalogLocal.edit(ce);
					setResponsePage(TreePage.class);
				}
			}
		};
		form.add(name);
		add(form);
		tree.getTreeState().expandAll();

		add(new Label("selectedElement", new Model<String>(null == getLikontrotechCRMSession().getParentCatalogElement() ? ""
				: getLikontrotechCRMSession().getParentCatalogElement().getName())));

		final int type = getLikontrotechCRMSession().getParentCatalogElement().getType();
		Link<CatalogElement> changeStateLink = new Link<CatalogElement>("changeStateLink", new Model(getLikontrotechCRMSession()
				.getParentCatalogElement())) {
			@Override
			public void onClick() {
				// CatalogElement catalogElement = getModelObject();
				catalogLocal.changeState(getLikontrotechCRMSession().getParentCatalogElement(), 0 == type ? 1 : 0);
				getLikontrotechCRMSession().setParentCatalogElement(
						catalogLocal.find(getLikontrotechCRMSession().getParentCatalogElement().getId()));
				setResponsePage(TreePage.class);
			}
		};
		changeStateLink.add(new Label("changeStatelabel", 0 == type ? "make node" : "make super"));
		add(changeStateLink);

		Link<CatalogElement> deleteLink = new Link<CatalogElement>("deleteLink", new Model(getLikontrotechCRMSession()
				.getParentCatalogElement())) {
			@Override
			public void onClick() {
				CatalogElement catalogElement = getModelObject();
				catalogLocal.delete(catalogElement);
				getLikontrotechCRMSession().setParentCatalogElement(catalogLocal.find(0l));
				setResponsePage(TreePage.class);
			}
		};
		add(deleteLink);

		Link<CatalogElement> editLink = new Link<CatalogElement>("editLink", new Model(getLikontrotechCRMSession()
				.getParentCatalogElement())) {
			@Override
			public void onClick() {
				CatalogElement catalogElement = getModelObject();
				getLikontrotechCRMSession().setEdit(true);
				name.setModelObject(getLikontrotechCRMSession().getParentCatalogElement().getName());
			}
		};
		add(editLink);
		// ///////////////
		
		IModel<CatalogElement> modelParentElement = new LoadableDetachableModel<CatalogElement>() {
			@Override
			protected CatalogElement load() {
				if (0 != getLikontrotechCRMSession().getParentCatalogElement().getId())
					return catalogLocal.find(getLikontrotechCRMSession().getParentCatalogElement().getId());
				else
					return null;
			}
		};		
		
		Link<CatalogElement> link = new Link<CatalogElement>("parentElementLink", modelParentElement) {
			@Override
			public void onClick() {
				getLikontrotechCRMSession().setParentCatalogElement(catalogLocal.find(getModelObject().getParentId()));
				setResponsePage(TreePage.class);
			}

			@Override
			public boolean isVisible() {
				return null == getModelObject()?false:null != getModelObject().getParentId();
			}
		};

		link.add(new Label("parentElementLabel", null == modelParentElement.getObject()?"":modelParentElement.getObject().getName()));
		add(link);
		// ///////////////
	}

	public void onClick(Object obj, BaseTree bt, AjaxRequestTarget tar) {
		DefaultMutableTreeNode dtn = (DefaultMutableTreeNode) obj;
		CatalogElement ce = (CatalogElement) dtn.getUserObject();
		getLikontrotechCRMSession().setParentCatalogElement(ce);
		setResponsePage(TreePage.class);
	}

	protected List<CatalogElement> getCatalogItems() {
		return catalogLocal.findAll();
	}
}
