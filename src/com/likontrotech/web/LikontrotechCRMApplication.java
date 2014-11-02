package com.likontrotech.web;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.wicket.Application;
import org.apache.wicket.Request;
import org.apache.wicket.Resource;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.wicketstuff.javaee.injection.JavaEEComponentInjector;

import com.likontrotech.ejb.CommercialDocumentLocal;
import com.likontrotech.ejb.EventLocal;
import com.likontrotech.ejb.PictureLocal;
import com.likontrotech.ejb.entities.CommercialDocument;
import com.likontrotech.ejb.entities.Event;
import com.likontrotech.ejb.entities.Picture;

public class LikontrotechCRMApplication extends WebApplication {

	@EJB(name = "PictureEJB")
	public PictureLocal pictureLocal;

	private Folder uploadFolder = null;

	public LikontrotechCRMApplication() {
	}

	@Override
	protected void init() {
		addComponentInstantiationListener(new JavaEEComponentInjector(this));
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		getRequestCycleSettings().setResponseRequestEncoding("UTF-8");

		mountBookmarkablePage("/Branch/categories.xml", CategoriesXML.class);
		mountBookmarkablePage("/previewCommercialOffer", GenerateCommercialOffer.class);
		mountBookmarkablePage("/previewProformaInvoice", GenerateProformaInvoice.class);		
		mountBookmarkablePage("/previewInvoice", GenerateInvoice.class);		

		mountBookmarkablePage("/graps", GrPage.class);

		mountBookmarkablePage("/picture", PictureProviderPage.class);

		getResourceSettings().setThrowExceptionOnMissingResource(false);
		uploadFolder = new Folder(System.getProperty("java.io.tmpdir"), "wicket-uploads");
		uploadFolder.mkdirs();

		final String resourceKey = "DYN_IMG_KEY";
		final String queryParm = "id";

		getSharedResources().add(resourceKey, new Resource() {
			@Override
			public IResourceStream getResourceStream() {
				final String query = getParameters().getString(queryParm);
				final BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
				final Graphics2D g2 = img.createGraphics();
				g2.setColor(Color.WHITE);
				g2.drawString(query, img.getWidth() / 2, img.getHeight() / 2);
				return new AbstractResourceStreamWriter() {
					public String getContentType() {
						return "image/png";
					}

					public void write(OutputStream output) {
						try {
							ImageIO.write(img, "png", output);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				};
			}
		});
		mountSharedResource("/resource", Application.class.getName() + "/" + resourceKey);

		final String resourceKey2 = "DYN_IMG_KEY2";
		final String queryParm2 = "id";

		getSharedResources().add(resourceKey2, new Resource() {
			@Override
			public IResourceStream getResourceStream() {
				final String query = getParameters().getString(queryParm2);
				PictureLocal pictureLocal = getPictureLocal();
				final Picture picture = pictureLocal.find(new Long(query));

				// return the image as a PNG stream
				return new AbstractResourceStreamWriter() {
					public String getContentType() {
						return "image/jpeg";
					}

					public void write(OutputStream output) {
						try {
							output.write(picture.getPicture());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
			}
		});
		mountSharedResource("/resource2", Application.class.getName() + "/" + resourceKey2);

		
		final String pdfResourceKey = "PDF_KEY";		
		getSharedResources().add(pdfResourceKey, new Resource() {
			@Override
			public IResourceStream getResourceStream() {
				final String query = getParameters().getString("documentId");
				CommercialDocumentLocal commercialDocumentLocal = getCommercialDocumentLocal();	
				final CommercialDocument commercialDocument = commercialDocumentLocal.find(new Long(query));
				return new AbstractResourceStreamWriter() {
					public String getContentType() {
						return "application/pdf";
					}

					public void write(OutputStream output) {
						try {
							output.write(commercialDocument.getFormedPdf());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
			}
		});
		mountSharedResource("/sentPdf", Application.class.getName() + "/" + pdfResourceKey);		
		
		final String attachedFileResourceKey = "ATTACHED_FILE_KEY";		
		getSharedResources().add(attachedFileResourceKey, new Resource() {
			@Override
			public IResourceStream getResourceStream() {
				final String query = getParameters().getString("id");
				
				EventLocal eventLocal = getEventLocal();
				final Event event = eventLocal.find(new Long(query));
				return new AbstractResourceStreamWriter() {
					public String getContentType() {
						return event.getMimeType();
					}

					public void write(OutputStream output) {
						try {
							output.write(event.getAttachedFile());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
			}
		});
		mountSharedResource("/attachedFile", Application.class.getName() + "/" + attachedFileResourceKey);			
		
	}

	@Override
	public Class<? extends WebPage> getHomePage() {
		return CompaniesPage.class;
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new LikontrotechCRMSession(request);
	}

	public Folder getUploadFolder() {
		return uploadFolder;
	}

	private PictureLocal getPictureLocal() {
		Context context;
		PictureLocal pictureLocal = null;
		try {
			context = new InitialContext();
			pictureLocal = (PictureLocal) context.lookup("LikontrotechCRM/PictureEJB/local");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return pictureLocal;
	}
	
	private EventLocal getEventLocal() {
		Context context;
		EventLocal eventLocal = null;
		try {
			context = new InitialContext();
			eventLocal = (EventLocal) context.lookup("LikontrotechCRM/EventEJB/local");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return eventLocal;
	}	
	private CommercialDocumentLocal getCommercialDocumentLocal() {
		Context context;
		CommercialDocumentLocal commercialDocumentLocal = null;
		try {
			context = new InitialContext();
			commercialDocumentLocal = (CommercialDocumentLocal) context.lookup("LikontrotechCRM/CommercialDocumentEJB/local");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return commercialDocumentLocal;
	}	

}