package com.likontrotech.web.catalogadmin;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.lang.Bytes;

import com.likontrotech.ejb.entities.Attribute;
import com.likontrotech.ejb.entities.AttributeValue;
import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.ejb.entities.Picture;
import com.likontrotech.web.LikontrotechCRMApplication;
import com.likontrotech.web.test.Component;

public class CatalogElementFillingPage extends TreeBasePage {
	List<Component> components;
	IModel modelAttributes = null;
	private FileUploadField fileUploadField;

	BigDecimal price;
	Integer quantityOnWarehouse;
	TextField<BigDecimal> priceText = new TextField<BigDecimal>("price", new PropertyModel<BigDecimal>(this, "price")){
		public boolean isVisible(){
			return 	null == getLikontrotechCRMSession().getCatalogElement()?false:new Integer(1).equals(getLikontrotechCRMSession().getCatalogElement().getType());
		}
	};
	TextField<Integer> quantityOnWarehouseText = new TextField<Integer>("quantityOnWarehouse", new PropertyModel<Integer>(this, "quantityOnWarehouse")){
		public boolean isVisible(){
			return 	null == getLikontrotechCRMSession().getCatalogElement()?false:new Integer(1).equals(getLikontrotechCRMSession().getCatalogElement().getType());
		}
	};	
	Image dynamicImage;
	Picture picture = null;
	Label nameLabel = null;

	public CatalogElementFillingPage() {
		super();

		price = (null == getLikontrotechCRMSession().getCatalogElement() ? new BigDecimal(0) : (null == getLikontrotechCRMSession()
				.getCatalogElement().getPrice() ? new BigDecimal(0) : getLikontrotechCRMSession().getCatalogElement().getPrice()));
		quantityOnWarehouse = (null == getLikontrotechCRMSession().getCatalogElement() ? new Integer(0) : (null == getLikontrotechCRMSession()
				.getCatalogElement().getQuantityOnWarehouse() ? new Integer(0) : getLikontrotechCRMSession().getCatalogElement().getQuantityOnWarehouse()));
		modelAttributes = new LoadableDetachableModel() {
			@Override
			protected Object load() {
				if (null == getLikontrotechCRMSession().getCatalogElement())
					return null;
				List<Attribute> attrs = attributeLocal.findAll(getLikontrotechCRMSession().getCatalogElement());
				return attrs;
			}
		};

		Form<Attribute> form = new Form<Attribute>("form") {
			@Override
			protected void onSubmit() {

				if (null == getLikontrotechCRMSession().getCatalogElementParameterValuesSource()) {
					for (Component component : components) {
						if (null == component.getAttributeValue()) {
							if (null != component.getValue() && !"".equals(component.getValue())) {
								AttributeValue attributeValue = new AttributeValue();
								attributeValue.setAttribute(component.getAttribute());
								attributeValue.setCatalogElement(getLikontrotechCRMSession().getCatalogElement());
								attributeValue.setValue(component.getValue());
								attributeValueLocal.create(attributeValue);
							}
						} else {
							AttributeValue attributeValue = component.getAttributeValue();
							attributeValue.setValue(component.getValue());
							attributeValueLocal.edit(attributeValue);
						}
					}
				} else {
					List<AttributeValue> listSource = attributeValueLocal.getAttributValues(getLikontrotechCRMSession()
							.getCatalogElementParameterValuesSource());
					List<AttributeValue> listAcceptor = attributeValueLocal.getAttributValues(getLikontrotechCRMSession()
							.getCatalogElement());
					for (AttributeValue sourceValue : listSource) {
						String key = sourceValue.getAttribute().getName();
						AttributeValue target = null;
						for (AttributeValue acceptorValue : listAcceptor) {
							if (key.equals(acceptorValue.getAttribute().getName()))
								target = acceptorValue;
						}
						if (null == target) {
							AttributeValue attributeValue = new AttributeValue();

							attributeValue.setAttribute(sourceValue.getAttribute());
							attributeValue.setCatalogElement(getLikontrotechCRMSession().getCatalogElement());
							attributeValue.setValue(sourceValue.getValue());
							attributeValueLocal.create(attributeValue);
						} else {
							target.setValue(sourceValue.getValue());
							attributeValueLocal.edit(target);
						}
					}
					getLikontrotechCRMSession().setCatalogElementParameterValuesSource(null);
					getLikontrotechCRMSession().setCopyParameterValuesMode(false);
				}

				final FileUpload upload = fileUploadField.getFileUpload();
				if (upload != null) {

					if (0 != getLikontrotechCRMSession().getCatalogElement().getId()) {
						BigInteger pictureId = pictureLocal.getPictureId(getLikontrotechCRMSession().getCatalogElement());
						getLikontrotechCRMSession().getCatalogElement().setPicture(null);
						catalogLocal.edit(getLikontrotechCRMSession().getCatalogElement());
						if (null != pictureId)
							pictureLocal.remove(pictureId.longValue());
					}

					// Create a new file
					File newFile = new File(getUploadFolder(), upload.getClientFileName());

					// Check new file, delete if it already existed
					checkFileExists(newFile);
					try {
						// Save to new file
						newFile.createNewFile();
						upload.writeTo(newFile);

						Picture picture = new Picture();
						picture.setMineType(getMimeType(upload.getClientFileName()));
						picture.setName(upload.getClientFileName());
						picture.setPicture(upload.getBytes());
						pictureLocal.create(picture);

						getLikontrotechCRMSession().getCatalogElement().setPicture(picture);
						catalogLocal.edit(getLikontrotechCRMSession().getCatalogElement());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				getLikontrotechCRMSession().setCatalogElementParameterValuesSource(null);
				getLikontrotechCRMSession().setCopyParameterValuesMode(false);
				getLikontrotechCRMSession().getCatalogElement().setPrice(price);
				getLikontrotechCRMSession().getCatalogElement().setQuantityOnWarehouse(quantityOnWarehouse);				
				catalogLocal.edit(getLikontrotechCRMSession().getCatalogElement());
				setResponsePage(CatalogElementFillingPage.class);
			}

			private String getMimeType(String fileName) {
				String mimeType = "";
				String extracted = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
				if ("jpg".equals(extracted))
					mimeType = "image/jpeg";
				else if ("png".equals(extracted))
					mimeType = "image/png";
				else if ("gif".equals(extracted))
					mimeType = "image/gif";
				return mimeType;
			}
		};

		components = new ArrayList<Component>();

		List<Attribute> attrs = new ArrayList<Attribute>();
		if (null != getLikontrotechCRMSession().getParentCatalogElement())
			attrs = attributeLocal.findAll(getLikontrotechCRMSession().getParentCatalogElement());
		for (Attribute attribute : attrs) {
			AttributeValue attributeValue = attributeValueLocal.load(getLikontrotechCRMSession().getCatalogElement(), attribute);
			components.add(new Component(attribute, attributeValue));
		}

		form.add(new ListView<Component>("components", components) {
			protected void populateItem(ListItem<Component> item) {
				item.add(item.getModelObject());
			}
		});

		form.setMultiPart(true);
		form.add(fileUploadField = new FileUploadField("fileInput"));
		priceText.setConvertedInput(price);
		form.add(priceText);	
		quantityOnWarehouseText.setConvertedInput(quantityOnWarehouse);			
		form.add(quantityOnWarehouseText);
		
		form.setMaxSize(Bytes.kilobytes(1000));
		add(form);

		if (null != getLikontrotechCRMSession().getCatalogElement())
			if (0 != getLikontrotechCRMSession().getCatalogElement().getId()) {
				BigInteger pictureId = pictureLocal.getPictureId(getLikontrotechCRMSession().getCatalogElement());
				if (null != pictureId)
					picture = pictureLocal.find(pictureId.longValue());
			}

		form.add(nameLabel = new Label("hasPicture", null == picture ? "--" : picture.getName()));
		dynamicImage = new Image("picture", null == picture ? "0" : String.valueOf(picture.getId())) {
			@Override
			public boolean isVisible() {
				return null != picture;
			}
		};
		dynamicImage.add(new AttributeModifier("src", true, new AbstractReadOnlyModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public final Object getObject() {
				String idd = String.valueOf(picture.getId());
				return "pictureServlet?id=" + idd;
			}

		}));
		dynamicImage.setOutputMarkupId(true);
		form.add(new Link<Attribute>("deletePicture") {
			@Override
			public void onClick() {
				CatalogElement ce = getLikontrotechCRMSession().getCatalogElement();
				ce.setPicture(null);
				catalogLocal.edit(ce);
				pictureLocal.remove(picture);
				picture = null;
				nameLabel.setDefaultModelObject("");
			}

			@Override
			public boolean isVisible() {
				return null != picture;
			}
		});
		form.add(dynamicImage);

		tree.getTreeState().expandAll();
		tree.nodeSelected(getLikontrotechCRMSession().getObject());

		add(new Label("selectedElement", new Model<String>(null == getLikontrotechCRMSession().getCatalogElement() ? ""
				: getLikontrotechCRMSession().getCatalogElement().getName())));

		add(new Link<Attribute>("copyLink") {
			@Override
			public void onClick() {
				getLikontrotechCRMSession().setCopyParameterValuesMode(true);
			}

			@Override
			public boolean isVisible() {
				return null != getLikontrotechCRMSession().getCatalogElement() && !getLikontrotechCRMSession().isCopyParameterValuesMode();
			}
		});

		add(new Label("selectLabel", "select other element") {
			@Override
			public boolean isVisible() {
				return getLikontrotechCRMSession().isCopyParameterValuesMode();
			}
		});

		add(new Label("otherSelectedElement", new Model<String>(null == getLikontrotechCRMSession()
				.getCatalogElementParameterValuesSource() ? "" : getLikontrotechCRMSession().getCatalogElementParameterValuesSource()
				.getName())) {
			@Override
			public boolean isVisible() {
				return null != getLikontrotechCRMSession().getCatalogElementParameterValuesSource();
			}
		});

		final IModel attributeValuesModel = new LoadableDetachableModel() {
			@Override
			protected Object load() {
				CatalogElement ce = getLikontrotechCRMSession().getCatalogElementParameterValuesSource();
				return null != ce ? attributeValueLocal.getAttributValues(ce) : null;
			}
		};

		add(new ListView<AttributeValue>("attributeValuesList", attributeValuesModel) {
			@Override
			protected void populateItem(ListItem<AttributeValue> item) {
				AttributeValue attributeValue = item.getModelObject();
				item.add(new Label("attributeValue", attributeValue.getValue()));
			}

			@Override
			public boolean isVisible() {
				return null != getLikontrotechCRMSession().getCatalogElementParameterValuesSource();
			}
		});

		add(new Link<Attribute>("delete") {
			@Override
			public void onClick() {
				CatalogElement ce = catalogLocal.find(getLikontrotechCRMSession().getCatalogElement().getParentId());
				catalogLocal.delete2(getLikontrotechCRMSession().getCatalogElement());
				getLikontrotechCRMSession().setCatalogElement(ce);
				setResponsePage(CatalogElementFillingPage.class);
			}

			@Override
			public boolean isVisible() {
				CatalogElement ce = getLikontrotechCRMSession().getCatalogElement();
				return null == ce ? false : (ce.getType().equals(new Integer(1)) ? true : false);
			}
		});
	}

	public void onClick(Object object, BaseTree bt, AjaxRequestTarget target) {
		modelAttributes.detach();
		DefaultMutableTreeNode dtn = (DefaultMutableTreeNode) object;
		CatalogElement selectedElement = (CatalogElement) dtn.getUserObject();

		if (getLikontrotechCRMSession().isCopyParameterValuesMode()) {
			if (getLikontrotechCRMSession().getCatalogElement().getParentId().equals(selectedElement.getParentId()))
				getLikontrotechCRMSession().setCatalogElementParameterValuesSource(selectedElement);
			setResponsePage(CatalogElementFillingPage.class);
		} else {
//			if (selectedElement.getType().equals(new Integer(1))) {
				CatalogElement parentElement = catalogLocal.find(selectedElement.getParentId());
				selectedElement = catalogLocal.find(selectedElement.getId());
				getLikontrotechCRMSession().setCatalogElement(selectedElement);
				getLikontrotechCRMSession().setParentCatalogElement(parentElement);
//			} 
//			else {
//				getLikontrotechCRMSession().setCatalogElement(null);
//				getLikontrotechCRMSession().setParentCatalogElement(null);
//			}
			setResponsePage(CatalogElementFillingPage.class);
		}
	}

	protected List<CatalogElement> getCatalogItems() {
		List<CatalogElement> els = catalogLocal.findAll();
		getLikontrotechCRMSession().setObject(els.get(0));
		return els;
	}

	private Folder getUploadFolder() {
		return ((LikontrotechCRMApplication) Application.get()).getUploadFolder();
	}

	private void checkFileExists(File newFile) {
		if (newFile.exists()) {
			// Try to delete the file
			if (!Files.remove(newFile)) {
				throw new IllegalStateException("Unable to overwrite " + newFile.getAbsolutePath());
			}
		}
	}
}
