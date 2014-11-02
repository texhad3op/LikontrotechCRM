package com.likontrotech.web.catalogadmin;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkTree;

import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.web.BasePage;

public abstract class TreeBasePage extends BasePage {

	public abstract void onClick(Object obj, BaseTree bt, AjaxRequestTarget tar);

	protected abstract List<CatalogElement> getCatalogItems();

	protected List<Object> rootList = new ArrayList<Object>();
	protected List<Object> lastList = new ArrayList<Object>();
	protected List<Object> foundList = new ArrayList<Object>();

	protected BaseTree tree;

	public TreeBasePage() {

		tree = new LinkTree("tree", createTreeModel()) {

			@Override
			protected void onNodeLinkClicked(Object object, BaseTree baseTree, AjaxRequestTarget ajaxRequestTarget) {
				onClick(object, baseTree, ajaxRequestTarget);

			}

			@Override
			protected void populateTreeItem(WebMarkupContainer container, int level) {
			    super.populateTreeItem(container, level);
			    javax.swing.tree.DefaultMutableTreeNode tn = ( javax.swing.tree.DefaultMutableTreeNode)
			    		container.getDefaultModelObject();
			    CatalogElement ce = (CatalogElement)tn.getUserObject();
			    CatalogElement sce = getLikontrotechCRMSession().getCatalogElement();
			    if(null != ce && null != sce)
			    if(sce.getId().equals(ce.getId()))
			    container.add(new SimpleAttributeModifier("class", "example5"));

			}
			
		};

		add(tree);

	}

	protected TreeModel createTreeModel() {

		List<CatalogElement> sourceList = getCatalogItems();

		CatalogElement root = new CatalogElement(0l, "ROOT", 0, -1l, 1);
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(root);

		for (CatalogElement ce : sourceList) {
			DefaultMutableTreeNode node = findParent(ce, rootNode);
			if (null != node)
				node.add(new DefaultMutableTreeNode(ce));
		}

		TreeModel model = new DefaultTreeModel(rootNode);
		return model;
	}

	public DefaultMutableTreeNode findParent(CatalogElement forElement, DefaultMutableTreeNode beginElement) {
		if (((CatalogElement) beginElement.getUserObject()).getId().equals(forElement.getParentId()))
			return beginElement;
		else {
			Enumeration<DefaultMutableTreeNode> enumeration = beginElement.children();
			DefaultMutableTreeNode intermediate = null;
			while (enumeration.hasMoreElements()) {
				DefaultMutableTreeNode newBeginElement = enumeration.nextElement();
				intermediate = findParent(forElement, newBeginElement);
				if (null != intermediate)
					return intermediate;
			}
		}
		return null;
	}
}
