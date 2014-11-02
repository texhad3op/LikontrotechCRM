package com.likontrotech.web;

import javax.ejb.EJB;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.resources.StyleSheetReference;

import com.likontrotech.ejb.AttributeLocal;
import com.likontrotech.ejb.AttributeValueLocal;
import com.likontrotech.ejb.CatalogLocal;
import com.likontrotech.ejb.CommercialDocumentLineLocal;
import com.likontrotech.ejb.CommercialDocumentLocal;
import com.likontrotech.ejb.CompanyLocal;
import com.likontrotech.ejb.EventLocal;
import com.likontrotech.ejb.GenerateCommercialOfferLocal;
import com.likontrotech.ejb.MailEventLocal;
import com.likontrotech.ejb.PictureLocal;
import com.likontrotech.ejb.RepresentativeLocal;
import com.likontrotech.ejb.SendMailLocal;
import com.likontrotech.ejb.SerialCodeLocal;
import com.likontrotech.ejb.WarehouseLocal;
import com.likontrotech.ejb.WorkerLocal;
import com.likontrotech.ejb.entities.CatalogElement;
import com.likontrotech.ejb.entities.Worker;
import com.likontrotech.web.catalog.ShowCatalogPage;
import com.likontrotech.web.catalogadmin.AttributesPage;
import com.likontrotech.web.catalogadmin.CatalogElementFillingPage;
import com.likontrotech.web.catalogadmin.TreePage;
import com.likontrotech.web.reports.ReportByCompanyAndMonthPage;

public abstract class BasePage extends WebPage {

	@EJB(name = "CompanyEJB")
	public CompanyLocal companyLocal;

	@EJB(name = "EventEJB")
	public EventLocal eventLocal;

	@EJB(name = "CatalogEJB")
	public CatalogLocal catalogLocal;

	@EJB(name = "AttributeEJB")
	public AttributeLocal attributeLocal;

	@EJB(name = "AttributeValueEJB")
	public AttributeValueLocal attributeValueLocal;

	@EJB(name = "PictureEJB")
	public PictureLocal pictureLocal;

	@EJB(name = "SendMailEJB")
	public SendMailLocal sendMailLocal;

	@EJB(name = "GenerateSuggestionlEJB")
	public GenerateCommercialOfferLocal generateSuggestionLocal;

	@EJB(name = "WarehouseEJB")
	public WarehouseLocal warehouseLocal;

	@EJB(name = "RepresentativeEJB")
	public RepresentativeLocal representativeLocal;

	@EJB(name = "WorkerEJB")
	public WorkerLocal workerLocal;

	@EJB(name = "MailEventEJB")
	public MailEventLocal mailEventLocal;	
	
	@EJB(name = "CommercialDocumentEJB")
	public CommercialDocumentLocal commercialDocumentLocal;		
	
	@EJB(name = "CommercialDocumentLineEJB")
	public CommercialDocumentLineLocal commercialDocumentLineLocal;			
	
	@EJB(name = "SerialCodeLocalEJB")
	public SerialCodeLocal serialCodeLocal;		
	
	
	public LikontrotechCRMSession getLikontrotechCRMSession() {
		return (LikontrotechCRMSession) getSession();
	}

	public BasePage() {
		if (null == getLikontrotechCRMSession().getWorker())
			setResponsePage(LoginPage.class);
		add(new StyleSheetReference("stylesheet2", BasePage.class, "css/bootstrap.min.css"));
		add(new Label("logged", null == getLikontrotechCRMSession().getWorker() ? "" : getLikontrotechCRMSession().getWorker()
				.getFirstName() + " " + getLikontrotechCRMSession().getWorker().getLastName()));
		add(new Link<Worker>("logout") {
			@Override
			public void onClick() {
				getLikontrotechCRMSession().setWorker(null);
				setResponsePage(LoginPage.class);
			}
		});

		MenuComponent catalog = new MenuComponent("catalog", "Katalogo struktūra") {
			@Override
			public void onClick2() {
				CatalogElement catalogElement = catalogLocal.find(0l);
				getLikontrotechCRMSession().setParentCatalogElement(catalogElement);
				getLikontrotechCRMSession().setCatalogElement(catalogElement);				
				setResponsePage(TreePage.class);
			}
		};
		add(catalog);

		MenuComponent linkAttributes = new MenuComponent("attributes", "Katalogo atributai") {
			@Override
			public void onClick2() {
				CatalogElement catalogElement = catalogLocal.find(0l);
				getLikontrotechCRMSession().setCatalogElement(catalogElement);
				setResponsePage(AttributesPage.class);
			}
		};
		add(linkAttributes);

		MenuComponent linkFilling = new MenuComponent("filling", "Katalogo užpildymas") {
			@Override
			public void onClick2() {
				CatalogElement catalogElement = catalogLocal.find(0l);
				getLikontrotechCRMSession().setCatalogElement(catalogElement);
				getLikontrotechCRMSession().setCopyParameterValuesMode(false);
				getLikontrotechCRMSession().setCatalogElementParameterValuesSource(null);
				setResponsePage(CatalogElementFillingPage.class);
			}
		};
		add(linkFilling);

		MenuComponent linkReport = new MenuComponent("report", "Ataskaita") {
			@Override
			public void onClick2() {
				setResponsePage(ReportByCompanyAndMonthPage.class);
			}
			
			@Override
			public boolean isVisible() {
				return false;
			}			
		};
		add(linkReport);

		MenuComponent catalogPageLink = new MenuComponent("catalogPageLink", "Katalogas") {
			@Override
			public void onClick2() {
				CatalogElement catalogElement = catalogLocal.find(0l);
				getLikontrotechCRMSession().setCatalogElement(catalogElement);
				setResponsePage(ShowCatalogPage.class);
			}
		};
		add(catalogPageLink);

		MenuComponent crm = new MenuComponent("crm", "LikontrotechCRM") {
			@Override
			public void onClick2() {
				setResponsePage(CompaniesPage.class);
			}
		};
		add(crm);

		MenuComponent warehouse = new MenuComponent("warehouse", "Sandelis") {
			@Override
			public void onClick2() {
				setResponsePage(WarehousePage.class);
			}
			
			@Override
			public boolean isVisible() {
				return false;
			}			
		};
		add(warehouse);

		MenuComponent serialNumberControl = new MenuComponent("serialNumberControl", "Serija") {
			@Override
			public void onClick2() {
				setResponsePage(SerialCodeControlPage.class);
			}		
		};
		add(serialNumberControl);		
		
//		final ModalWindow modal1;
//		add(modal1 = new ModalWindow("modal1"));
//
//		modal1.setPageMapName("modal-1");
//		modal1.setCookieName("modal-1");
//
//		modal1.setPageCreator(new ModalWindow.PageCreator() {
//			public Page createPage() {
//				return new CartPage(BasePage.this, modal1);
//			}
//		});
//
//		add(new AjaxLink("showModal1") {
//			@Override
//			public void onClick(AjaxRequestTarget target) {
//				modal1.show(target);
//			}
//			@Override
//			public boolean isVisible(){
//				return false;
//			}
//		});

	}
}
