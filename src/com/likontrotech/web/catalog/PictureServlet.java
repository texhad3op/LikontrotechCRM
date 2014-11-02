package com.likontrotech.web.catalog;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.likontrotech.ejb.PictureLocal;
import com.likontrotech.ejb.entities.Picture;

public class PictureServlet extends HttpServlet {

	@EJB(name = "PictureEJB")
	public PictureLocal pictureLocal;

	private static BufferedImage buffer = new BufferedImage(1, 1,
			BufferedImage.TYPE_INT_RGB);

	@Override
	public void init() {

	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	protected void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Picture picture = pictureLocal
				.find(new Long(request.getParameter("id")));

		if (null != picture) {
			OutputStream os = response.getOutputStream();
			response.setContentType(picture.getMimeType());
			os.write(picture.getPicture());
			os.close();
		} else {
			response.setContentType("image/png");
			OutputStream os = response.getOutputStream();
			ImageIO.write(buffer, "png", os);
			os.close();

		}
	}
}
