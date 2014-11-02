package com.likontrotech.web;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;

public class PictureProviderPage extends BasePage {

//	public PictureProviderPage(PageParameters parameters) {
//		Picture picture = pictureLocal.find(parameters.getLong("id", 16l));
//
//		RequestCycle.get().getResponse().setContentType("image/jpeg");
//		OutputStream os = RequestCycle.get().getResponse().getOutputStream();
//		try {
//			os.write(picture.getPicture());
//			os.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
	
	
	public PictureProviderPage(PageParameters parameters) {

		final String query = parameters.getString("id");
		final BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2 = img.createGraphics();
		g2.setColor(Color.WHITE);
		g2.drawString(query, img.getWidth() / 2, img.getHeight() / 2);

		//RequestCycle.get().getResponse().setContentType("image/png");
		OutputStream os = RequestCycle.get().getResponse().getOutputStream();
		try {
			//os.write(img.getGraphics(). );
			ImageIO.write(img, "PNG", os);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}	

}