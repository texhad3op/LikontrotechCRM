package com.likontrotech.web.catalog;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.likontrotech.ejb.CommercialDocumentLocal;

public class SpecialServlet extends HttpServlet {

	@EJB(name = "CommercialDocumentEJB")
	public CommercialDocumentLocal commercialDocumentLocal;



	@Override
	public void init() {

	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String servlet = request.getServletPath();
//		if(servlet.equals("/migrate"))
//		commercialDocumentLocal.migratePdfToCommercialDocument();
	}

}

