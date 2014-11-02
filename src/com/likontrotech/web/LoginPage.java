package com.likontrotech.web;

import javax.ejb.EJB;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.PropertyModel;

import com.likontrotech.ejb.WorkerLocal;
import com.likontrotech.ejb.entities.Worker;

public class LoginPage extends WebPage {
	
	@EJB(name = "WorkerEJB")
	public WorkerLocal workerLocal;		
	
	public LikontrotechCRMSession getLikontrotechCRMSession() {
		return (LikontrotechCRMSession) getSession();
	}	
	
	String login;
	String password;	
	public LoginPage() {
		add(new StyleSheetReference("stylesheet2", BasePage.class, "css/bootstrap.min.css"));
		final TextField<String> loginField = new TextField<String>("login", new PropertyModel<String>(this, "login"));
		final PasswordTextField passwordField = new PasswordTextField("password", new PropertyModel<String>(this, "password"));
		
		Form<Worker> loginForm = new Form<Worker>("loginForm") {
			@Override
			protected void onSubmit() {
				Worker worker = workerLocal.getWorker(LoginPage.this.login, LoginPage.this.password);
				if(null == worker)System.out.println("!!!!!!!!");
				else{
					getLikontrotechCRMSession().setWorker(worker);
					setResponsePage(CompaniesPage.class);					
				}

			}
		};
		add(loginForm);
		loginForm.add(loginField);
		loginForm.add(passwordField);		
	}
}

