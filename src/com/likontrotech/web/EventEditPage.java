package com.likontrotech.web;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.joda.time.format.DateTimeFormatter;

import com.likontrotech.ejb.Utils;
import com.likontrotech.ejb.entities.CommercialDocument;
import com.likontrotech.ejb.entities.CommercialDocumentType;
import com.likontrotech.ejb.entities.Company;
import com.likontrotech.ejb.entities.Event;
import com.likontrotech.ejb.entities.EventType;

public class EventEditPage extends BasePage {
	Locale locale = new Locale("Lt");
	static Pattern patternMail = Pattern
			.compile("^[\\w-]+(\\.[\\w-]+)*@([A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*?\\.[A-Za-z]{2,6}|(\\d{1,3}\\.){3}\\d{1,3})(:\\d{4})?$");

	FileUploadField fileUploadField;
	WebMarkupContainer container = null;
	Label labelOk = null;
	Label labelFail = null;
	Boolean messageStatus = null;
	Boolean showCommercialDocument = false;
	Boolean sco = true;
	Boolean spi = true;
	Boolean si = true;

	public EventEditPage() {

		List<CommercialDocument> documents = commercialDocumentLocal
				.getCommercialDocuments(getLikontrotechCRMSession().getEvent());
		for (CommercialDocument document : documents) {
			if (Utils
					.getCommercialDocumentType(CommercialDocumentType.COMMERCIAL_OFFER) == document
					.getCommercialDocumentType())
				getLikontrotechCRMSession().setCommercialOffer(document);
			else if (Utils
					.getCommercialDocumentType(CommercialDocumentType.PROFORMA_INVOICE) == document
					.getCommercialDocumentType())
				getLikontrotechCRMSession().setProformaInvoice(document);
			else if (Utils
					.getCommercialDocumentType(CommercialDocumentType.INVOICE) == document
					.getCommercialDocumentType())
				getLikontrotechCRMSession().setInvoice(document);
		}
		container = new WebMarkupContainer("container");
		container.setVisible(false);
		add(container);
		container.add(labelOk = new Label("labelOk",
				"Paštas buvo sekmingai išsiūstas."));
		container.add(labelFail = new Label("labelFail", ""));

		final String failMessage = "Paštas NEBUVO sekmingai išsiūstas.";
		setDefaultModel(new Model<Event>() {
			@Override
			public Event getObject() {
				return getLikontrotechCRMSession().getEvent();
			}
		});

		Link<Company> linkCompany = new Link<Company>("companylink",
				new Model<Company>(getLikontrotechCRMSession().getCompany())) {
			@Override
			public void onClick() {
				Company company = getModelObject();
				getLikontrotechCRMSession().setCompany(company);
				setResponsePage(EventsPage.class);
			}
		};
		add(new Label("companylabel", getLikontrotechCRMSession().getCompany()
				.getName()));
		add(linkCompany);

		add(new Link<Event>("firms") {
			@Override
			public void onClick() {
				setResponsePage(CompaniesPage.class);
			}
		});

		final TextArea<String> description = new TextArea<String>(
				"description", new PropertyModel<String>(getDefaultModel(),
						"description"));
		description.setRequired(true);
		final DateTextField dateTextField = new DateTextField("eventTime",
				new PropertyModel(getDefaultModel(), "eventTime"),
				new DateConverter(true) {

					@Override
					public DateTimeFormatter getFormat() {
						return null;
					}

					@Override
					public String getDatePattern() {
						return "yyyy-MM-dd";
					}

					public Date convertToObject(String value, Locale locale) {
						Timestamp ts = new Timestamp(System.currentTimeMillis());
						Timestamp ts2 = java.sql.Timestamp.valueOf(value + " "
								+ ts.getHours() + ":" + ts.getMinutes() + ":"
								+ ts.getSeconds());
						return ts2;
					}

					public String convertToString(Object value, Locale locale) {
						return value.toString().substring(0, 10);
					}

					@Override
					public Locale getLocale() {
						return ConstantsUtil.getLocaleLt();
					}

				}) {

			@Override
			public Locale getLocale() {
				return ConstantsUtil.getLocaleLt();
			}
		};

		DropDownChoice<EventType> dropDownChoiceTypes = new DropDownChoice<EventType>(
				"type", new PropertyModel(getDefaultModel(), "type"),
				Utils.eventTypes, new ChoiceRenderer("name", "id"));

		Form<Event> form = new Form<Event>("form");
		form.add(description);
		dateTextField.add(new DatePicker());
		form.add(dateTextField);
		dropDownChoiceTypes.setRequired(true);
		form.add(dropDownChoiceTypes);
		form.setMultiPart(true);
		form.add(fileUploadField = new FileUploadField("fileInput") {
			@Override
			public boolean isVisible() {
				return null == getLikontrotechCRMSession().getEvent().getId();
			}
		});

		add(form);

		form.add(new Button("saveEvent", new Model("Įrašyti")) {
			@Override
			public void onSubmit() {
				if (null == getLikontrotechCRMSession().getEvent().getId()) {
					Event cEvent = (Event) EventEditPage.this.getDefaultModel()
							.getObject();
					cEvent.setRepresentative(representativeLocal
							.getRepresentativeOfCompany(getLikontrotechCRMSession()
									.getCompany()));

					eventLocal.create(cEvent);
					setResponsePage(EventsPage.class);
				} else {
					Event cEvent = (Event) EventEditPage.this.getDefaultModel()
							.getObject();
					cEvent.setRepresentative(representativeLocal
							.getRepresentativeOfCompany(getLikontrotechCRMSession()
									.getCompany()));
					eventLocal.edit(cEvent);
					setResponsePage(EventsPage.class);
				}
			}

			public boolean isVisible() {
				return null == getLikontrotechCRMSession().getEvent().getId();
			}
		});

		boolean mail = isMailCorrect(((Event) EventEditPage.this
				.getDefaultModel().getObject()).getRepresentative().getMail());

		boolean isVisible = null == getLikontrotechCRMSession().getEvent()
				.getId() && 0 != getLikontrotechCRMSession().getLines().size();

		showCommercialDocument = mail && isVisible;

		CheckBox sendMailCommercialOffer = new CheckBox(
				"sendMailCommercialOffer", new PropertyModel(this, "sco")) {
			@Override
			public boolean isVisible() {
				return showCommercialDocument;
			}
		};
		form.add(sendMailCommercialOffer);

		form.add(new Button("saveCommercialOffer", new Model("Siųsti")) {
			@Override
			public boolean isVisible() {
				return showCommercialDocument;
			}

			@Override
			public void onSubmit() {
				try {
					mailEventLocal
							.createMailEventAndCommercialOffer(
									sco,
									description.getValue().replaceAll("&quot;",
											"\""),
									representativeLocal
											.getRepresentativeOfCompany(getLikontrotechCRMSession()
													.getCompany()),
									getLikontrotechCRMSession().getLines(),
									getLikontrotechCRMSession().getDays(),
									getLikontrotechCRMSession()
											.getPaymentDays(),
									getLikontrotechCRMSession()
											.getSuggestionValidDays(),
									getLikontrotechCRMSession()
											.getPaymentType(),
									getLikontrotechCRMSession().getCompany(),
									getLikontrotechCRMSession().getPrice(),
									getLikontrotechCRMSession()
											.getPriceWithDiscount(),
									getLikontrotechCRMSession().getDiscount(),
									getLikontrotechCRMSession()
											.isShowPictures(),
									getLikontrotechCRMSession().isShowText(),
									((Event) EventEditPage.this
											.getDefaultModel().getObject())
											.getRepresentative().getMail());
					container.setVisible(true);
					container.add(new SimpleAttributeModifier("style",
							"background-color:#00ff00;"));
					labelOk.setVisible(true);
					labelFail.setVisible(false);
				} catch (Exception e) {
					container.setVisible(true);
					container.add(new SimpleAttributeModifier("style",
							"background-color:#ff0000;"));
					labelOk.setVisible(false);
					labelFail.setVisible(true);
					labelFail.setDefaultModelObject(failMessage
							+ e.getMessage());
				}
			}
		});

		CheckBox sendMailProformaInvoice = new CheckBox(
				"sendMailProformaInvoice", new PropertyModel(this, "spi")) {
			@Override
			public boolean isVisible() {
				return showCommercialDocument;
			}
		};
		form.add(sendMailProformaInvoice);

		form.add(new Button("saveProformaInvoice", new Model("Siųsti")) {
			@Override
			public boolean isVisible() {
				return showCommercialDocument;
			}

			@Override
			public void onSubmit() {
				try {
					mailEventLocal
							.createMailEventAndProformaInvoice(
									spi,
									description.getValue().replaceAll("&quot;",
											"\""),
									representativeLocal
											.getRepresentativeOfCompany(getLikontrotechCRMSession()
													.getCompany()),
									getLikontrotechCRMSession().getLines(),
									getLikontrotechCRMSession().getDays(),
									getLikontrotechCRMSession()
											.getPaymentDays(),
									getLikontrotechCRMSession()
											.getSuggestionValidDays(),
									getLikontrotechCRMSession()
											.getPaymentType(),
									getLikontrotechCRMSession().getCompany(),
									getLikontrotechCRMSession().getPrice(),
									getLikontrotechCRMSession()
											.getPriceWithDiscount(),
									getLikontrotechCRMSession().getDiscount(),
									getLikontrotechCRMSession()
											.isShowPictures(),
									getLikontrotechCRMSession().isShowText(),
									((Event) EventEditPage.this
											.getDefaultModel().getObject())
											.getRepresentative().getMail());
					container.setVisible(true);
					container.add(new SimpleAttributeModifier("style",
							"background-color:#00ff00;"));
					labelOk.setVisible(true);
					labelFail.setVisible(false);
				} catch (Exception e) {
					container.setVisible(true);
					container.add(new SimpleAttributeModifier("style",
							"background-color:#ff0000;"));
					labelOk.setVisible(false);
					labelFail.setVisible(true);
					labelFail.setDefaultModelObject(failMessage
							+ e.getMessage());
				}
			}
		});

		CheckBox sendMailInvoice = new CheckBox("sendMailInvoice",
				new PropertyModel(this, "si")) {
			@Override
			public boolean isVisible() {
				return showCommercialDocument;
			}
		};
		form.add(sendMailInvoice);
		form.add(new Button("saveInvoice", new Model("Siųsti")) {
			@Override
			public boolean isVisible() {
				return showCommercialDocument;
			}

			@Override
			public void onSubmit() {
				try {
					mailEventLocal
							.createMailEventAndInvoice(
									si,
									description.getValue().replaceAll("&quot;",
											"\""),
									representativeLocal
											.getRepresentativeOfCompany(getLikontrotechCRMSession()
													.getCompany()),
									getLikontrotechCRMSession().getLines(),
									getLikontrotechCRMSession().getDays(),
									getLikontrotechCRMSession()
											.getPaymentDays(),
									getLikontrotechCRMSession()
											.getSuggestionValidDays(),
									getLikontrotechCRMSession()
											.getPaymentType(),
									getLikontrotechCRMSession().getCompany(),
									getLikontrotechCRMSession().getPrice(),
									getLikontrotechCRMSession()
											.getPriceWithDiscount(),
									getLikontrotechCRMSession().getDiscount(),
									getLikontrotechCRMSession()
											.isShowPictures(),
									getLikontrotechCRMSession().isShowText(),
									((Event) EventEditPage.this
											.getDefaultModel().getObject())
											.getRepresentative().getMail(),
									getLikontrotechCRMSession().getWorker()
											.getType().getName(),
									getLikontrotechCRMSession().getWorker()
											.getFirstName(),
									getLikontrotechCRMSession().getWorker()
											.getLastName(),
									getLikontrotechCRMSession().getPayBefore(),
									getLikontrotechCRMSession()
											.isAddDeliveringPrice(),
									getLikontrotechCRMSession()
											.getDeliveringPrice()

							);
					container.setVisible(true);
					container.add(new SimpleAttributeModifier("style",
							"background-color:#00ff00;"));
					labelOk.setVisible(true);
					labelFail.setVisible(false);
				} catch (Exception e) {
					container.setVisible(true);
					container.add(new SimpleAttributeModifier("style",
							"background-color:#ff0000;"));
					labelOk.setVisible(false);
					labelFail.setVisible(true);
					labelFail.setDefaultModelObject(failMessage
							+ e.getMessage());
				}
			}
		});

		form.add(new Button("saveAndSendAttachedFile", new Model("Siųsti")) {
			@Override
			public boolean isVisible() {
				return isMailCorrect(((Event) EventEditPage.this
						.getDefaultModel().getObject()).getRepresentative()
						.getMail())
						&& null == getLikontrotechCRMSession().getEvent()
								.getId();
			}

			@Override
			public void onSubmit() {
				try {
					final FileUpload upload = fileUploadField.getFileUpload();
					File newFile = new File(getUploadFolder(), upload
							.getClientFileName());

					checkFileExists(newFile);
					newFile.createNewFile();
					upload.writeTo(newFile);

					mailEventLocal.createEventWithAttachedFile(
							description.getValue().replaceAll("&quot;", "\""),
							upload.getClientFileName(),
							upload.getBytes(),
							upload.getContentType(),
							representativeLocal
									.getRepresentativeOfCompany(getLikontrotechCRMSession()
											.getCompany()),
							((Event) EventEditPage.this.getDefaultModel()
									.getObject()).getRepresentative().getMail());
					container.setVisible(true);
					container.add(new SimpleAttributeModifier("style",
							"background-color:#00ff00;"));
					labelOk.setVisible(true);
					labelFail.setVisible(false);
				} catch (Exception e) {
					container.setVisible(true);
					container.add(new SimpleAttributeModifier("style",
							"background-color:#ff0000;"));
					labelOk.setVisible(false);
					labelFail.setVisible(true);
					labelFail.setDefaultModelObject(failMessage
							+ e.getMessage());
				}
			}
		});

		add(new FeedbackPanel("feedback"));

		Link<Event> toCartCommercialOffer = new Link<Event>(
				"toCartCommercialOffer", new Model<Event>(
						getLikontrotechCRMSession().getEvent())) {
			@Override
			public void onClick() {

				getLikontrotechCRMSession()
						.setLines(
								commercialDocumentLineLocal
										.getCommercialDocumentLines(getLikontrotechCRMSession()
												.getCommercialOffer().getId()));
			}

			@Override
			public boolean isVisible() {

				return null != getLikontrotechCRMSession().getEvent().getId()
						&& null != getLikontrotechCRMSession()
								.getCommercialOffer();
			}
		};
		form.add(toCartCommercialOffer);

		Link<Event> toCartProformaInvoice = new Link<Event>(
				"toCartProformaInvoice", new Model<Event>(
						getLikontrotechCRMSession().getEvent())) {
			@Override
			public void onClick() {

				getLikontrotechCRMSession()
						.setLines(
								commercialDocumentLineLocal
										.getCommercialDocumentLines(getLikontrotechCRMSession()
												.getProformaInvoice().getId()));
			}

			@Override
			public boolean isVisible() {

				return null != getLikontrotechCRMSession().getEvent().getId()
						&& null != getLikontrotechCRMSession()
								.getProformaInvoice();
			}
		};
		form.add(toCartProformaInvoice);

		Link<Event> toCartInvoice = new Link<Event>("toCartInvoice",
				new Model<Event>(getLikontrotechCRMSession().getEvent())) {
			@Override
			public void onClick() {

				getLikontrotechCRMSession()
						.setLines(
								commercialDocumentLineLocal
										.getCommercialDocumentLines(getLikontrotechCRMSession()
												.getInvoice().getId()));
			}

			@Override
			public boolean isVisible() {

				return null != getLikontrotechCRMSession().getEvent().getId()
						&& null != getLikontrotechCRMSession().getInvoice();
			}
		};
		form.add(toCartInvoice);

		final ModalWindow modalCommercialOffer;
		form.add(modalCommercialOffer = new ModalWindow("modalCommercialOffer"));
		modalCommercialOffer.setPageMapName("modalCommercialOffer");
		modalCommercialOffer.setCookieName("modalCommercialOffer");
		modalCommercialOffer.setPageCreator(new ModalWindow.PageCreator() {
			public Page createPage() {
				return new CartPageCommercialOffer(EventEditPage.this,
						modalCommercialOffer);
			}
		});
		form.add(new AjaxLink("showModalCommercialOffer") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalCommercialOffer.show(target);
			}

			@Override
			public boolean isVisible() {
				return null == getLikontrotechCRMSession().getEvent().getId();
			}
		});

		final ModalWindow modalProformaInvoice;
		form.add(modalProformaInvoice = new ModalWindow("modalProformaInvoice"));

		modalProformaInvoice.setPageMapName("modalProformaInvoice");
		modalProformaInvoice.setCookieName("modalProformaInvoice");

		modalProformaInvoice.setPageCreator(new ModalWindow.PageCreator() {
			public Page createPage() {
				return new CartPageProformaInvoice(EventEditPage.this,
						modalProformaInvoice);
			}
		});

		form.add(new AjaxLink("showModalProformaInvoice") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalProformaInvoice.show(target);
			}

			@Override
			public boolean isVisible() {
				return null == getLikontrotechCRMSession().getEvent().getId();
			}
		});

		final ModalWindow modalInvoice;
		form.add(modalInvoice = new ModalWindow("modalInvoice"));

		modalInvoice.setPageMapName("modalInvoice");
		modalInvoice.setCookieName("modalInvoice");

		modalInvoice.setPageCreator(new ModalWindow.PageCreator() {
			public Page createPage() {
				return new CartPageInvoice(EventEditPage.this, modalInvoice);
			}
		});

		form.add(new AjaxLink("showModalInvoice") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalInvoice.show(target);
			}

			@Override
			public boolean isVisible() {
				return null == getLikontrotechCRMSession().getEvent().getId();
			}
		});

		if (null == getLikontrotechCRMSession().getEvent().getId()) { // new
																		// Event

			form.add(new ExternalLink("previewCommercialOffer",
					"previewCommercialOffer",
					"Peržiūrėti komercinį pasiūlymą PDF") {
				@Override
				public boolean isVisible() {
					boolean mail = isMailCorrect(((Event) EventEditPage.this
							.getDefaultModel().getObject()).getRepresentative()
							.getMail());

					boolean isVisible = mail
							&& null == getLikontrotechCRMSession().getEvent()
									.getId()
							&& 0 != getLikontrotechCRMSession().getLines()
									.size();

					return isVisible;
				}
			});

			form.add(new ExternalLink("previewProformaInvoice",
					"previewProformaInvoice",
					"Peržiūrėti sąskaita išank. apmokėjimui PDF") {
				@Override
				public boolean isVisible() {
					boolean mail = isMailCorrect(((Event) EventEditPage.this
							.getDefaultModel().getObject()).getRepresentative()
							.getMail());

					boolean isVisible = mail
							&& null == getLikontrotechCRMSession().getEvent()
									.getId()
							&& 0 != getLikontrotechCRMSession().getLines()
									.size();

					return isVisible;
				}
			});

			form.add(new ExternalLink("previewInvoice", "previewInvoice",
					"Peržiūrėti sąskaitą-faktūrą PDF") {
				@Override
				public boolean isVisible() {
					boolean mail = isMailCorrect(((Event) EventEditPage.this
							.getDefaultModel().getObject()).getRepresentative()
							.getMail());

					boolean isVisible = mail
							&& null == getLikontrotechCRMSession().getEvent()
									.getId()
							&& 0 != getLikontrotechCRMSession().getLines()
									.size();

					return isVisible;
				}
			});

			form.add(new ExternalLink("sentAttachedFile", "attachedFile?id="
					+ getLikontrotechCRMSession().getEvent().getId(),
					"Peržiūrėti prikabintą bylą") {

				@Override
				public boolean isVisible() {
					return false;

				}
			});

		} else { // view old Event

			form.add(new ExternalLink("sentAttachedFile", "attachedFile?id="
					+ getLikontrotechCRMSession().getEvent().getId(),
					"Peržiūrėti prikabintą bylą") {

				@Override
				public boolean isVisible() {
					return null != getLikontrotechCRMSession().getEvent()
							.getAttachedFile();

				}
			});

			form.add(new ExternalLink(
					"previewCommercialOffer",
					"sentPdf?documentId="
							+ (null != getLikontrotechCRMSession()
									.getCommercialOffer() ? getLikontrotechCRMSession()
									.getCommercialOffer().getId() : "-1"),
					"Peržiūrėti išsiūstą PDF") {
				@Override
				public boolean isVisible() {
					return null != getLikontrotechCRMSession()
							.getCommercialOffer();

				}
			});

			form.add(new ExternalLink(
					"previewProformaInvoice",
					"sentPdf?documentId="
							+ (null != getLikontrotechCRMSession()
									.getProformaInvoice() ? getLikontrotechCRMSession()
									.getProformaInvoice().getId() : "-1"),
					"Peržiūrėti išsiūstą PDF") {
				@Override
				public boolean isVisible() {
					return null != getLikontrotechCRMSession()
							.getProformaInvoice();

				}
			});

			form.add(new ExternalLink(
					"previewInvoice",
					"sentPdf?documentId="
							+ (null != getLikontrotechCRMSession().getInvoice() ? getLikontrotechCRMSession()
									.getInvoice().getId() : "-1"),
					"Peržiūrėti išsiūstą PDF") {
				@Override
				public boolean isVisible() {
					return null != getLikontrotechCRMSession().getInvoice();

				}
			});
		}

		add(container);

	}

	private Folder getUploadFolder() {
		return ((LikontrotechCRMApplication) Application.get())
				.getUploadFolder();
	}

	private void checkFileExists(File newFile) {
		if (newFile.exists()) {
			// Try to delete the file
			if (!Files.remove(newFile)) {
				throw new IllegalStateException("Unable to overwrite "
						+ newFile.getAbsolutePath());
			}
		}
	}

	private boolean isMailCorrect(String mail) {
		if (null == mail)
			return false;
		return patternMail.matcher(mail).matches();
	}
}
