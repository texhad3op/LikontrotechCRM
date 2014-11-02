package com.likontrotech.web.catalog;

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
import com.likontrotech.web.catalogadmin.ChangePositionLink;

import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.web.catalogadmin.TreeBasePage;

public class ShowCatalogPage extends TreeBasePage {
	Integer cnt = 1;
	Integer count = null;	
	final TextField<String> findString = new TextField<String>("findString", new Model<String>(null == getLikontrotechCRMSession()
			.getFindString() ? "" : getLikontrotechCRMSession().getFindString()));

	public ShowCatalogPage() {
		super();
		tree.getTreeState().expandAll();
		final IModel<List<CatalogElementComponent>> modelCatalogElements = new LoadableDetachableModel<List<CatalogElementComponent>>() {
			@Override
			protected List<CatalogElementComponent> load() {
				List<CatalogElementComponent> catalogElementComponents = new ArrayList<CatalogElementComponent>();
				List<CatalogElement> catalogElements = catalogLocal.findAllOfCatalogElement(
						getLikontrotechCRMSession().getCatalogElement(), "");
				for (CatalogElement catalogElement : catalogElements) {
					List<Object[]> attributeValues = attributeValueLocal.getAttributValues2(catalogElement);
					CatalogElementComponent catalogElementComponent = new CatalogElementComponent(catalogElement, attributeValues);
					catalogElementComponents.add(catalogElementComponent);
				}

				return catalogElementComponents;
			}
		};
		count = modelCatalogElements.getObject().size();
		IModel<CatalogElement> modelParentElement = new LoadableDetachableModel<CatalogElement>() {
			@Override
			protected CatalogElement load() {
				if (0 != getLikontrotechCRMSession().getCatalogElement().getId())
					return catalogLocal.find(getLikontrotechCRMSession().getCatalogElement().getParentId());
				else
					return null;
			}
		};

		ListView<CatalogElementComponent> catalogElementList = new ListView<CatalogElementComponent>("catalogElementList", modelCatalogElements) {
			@Override
			protected void populateItem(ListItem<CatalogElementComponent> item) {
				CatalogElementComponent visualComponent = item.getModelObject();
				item.add(visualComponent);
				visualComponent.add(new ChangePositionLink<CatalogElementComponent>("bottomLink", new Model<CatalogElementComponent>(visualComponent), ShowCatalogPage.this.cnt) {
					@Override
					public void onClick() {
						CatalogElementComponent visualComponent = getModelObject();
						CatalogElement catalogElement = visualComponent.getCatalogElement();
						catalogLocal.moveBottom(catalogElement);
						modelCatalogElements.detach();
						ShowCatalogPage.this.cnt = 1;
						setResponsePage(ShowCatalogPage.class);
					}

					@Override
					public boolean isVisible() {
						return !count.equals(current);
					}
				});

				visualComponent.add(new ChangePositionLink<CatalogElementComponent>("topLink", new Model<CatalogElementComponent>(visualComponent), ShowCatalogPage.this.cnt) {
					@Override
					public void onClick() {
						CatalogElementComponent visualComponent = getModelObject();
						CatalogElement catalogElement = visualComponent.getCatalogElement();
						catalogLocal.moveTop(catalogElement);
						modelCatalogElements.detach();
						ShowCatalogPage.this.cnt = 1;	
						setResponsePage(ShowCatalogPage.class);
					}

					@Override
					public boolean isVisible() {
						return 1 != current;
					}
				});

				ShowCatalogPage.this.cnt++;				
			}
		};
		add(catalogElementList);

		add(new Label("selectedElement", getLikontrotechCRMSession().getCatalogElement().getName()));

		// ///////////////
		Link<CatalogElement> link = new Link<CatalogElement>("parentElementLink", modelParentElement) {
			@Override
			public void onClick() {
				getLikontrotechCRMSession().setCatalogElement(getModelObject());
				setResponsePage(ShowCatalogPage.class);
			}

			@Override
			public boolean isVisible() {
				return null == getModelObject()?false:0 != getModelObject().getId();
			}
		};

		link.add(new Label("parentElementLabel", null == modelParentElement.getObject()?"":modelParentElement.getObject().getName()));
		add(link);
		// ///////////////

		Form form = new Form("findForm") {
			@Override
			protected void onSubmit() {
				getLikontrotechCRMSession().setFindString(findString.getConvertedInput());
				setResponsePage(ShowCatalogPage.class);
			}
		};
		add(form);

		form.add(findString);
		form.add(new Link("clearFilter") {
			@Override
			public void onClick() {
				getLikontrotechCRMSession().setFindString(null);
				setResponsePage(ShowCatalogPage.class);
			}
		});
	}

	public void onClick(Object obj, BaseTree bt, AjaxRequestTarget tar) {
		DefaultMutableTreeNode dtn = (DefaultMutableTreeNode) obj;
		CatalogElement ce = (CatalogElement) dtn.getUserObject();
		getLikontrotechCRMSession().setCatalogElement(ce);
		setResponsePage(ShowCatalogPage.class);
	}

	protected List<CatalogElement> getCatalogItems() {
		return catalogLocal.findSuperNodes(getLikontrotechCRMSession().getFindString());
	}
}
