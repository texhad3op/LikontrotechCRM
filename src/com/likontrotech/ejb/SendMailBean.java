package com.likontrotech.ejb;

import javax.ejb.Stateless;

import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.MultiPartEmail;

@Stateless(name = "SendMailEJB")
public class SendMailBean implements SendMailLocal {

	public SendMailBean() {
	}

	public void sendEMail(String subject, String text, String to,
			byte[] attachement, String fileName, String mimeType) {

		try {
			MultiPartEmail email = new MultiPartEmail();
			email.setHostName("smtp.mail.yahoo.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator("likontrotech1",
					"mpodol"));
			email.setTLS(true);
			email.addTo(to);
			email.setFrom("likontrotech1@yahoo.com");
			email.setSubject(subject);
			email.setMsg(text);

			ByteArrayDataSource bads = new ByteArrayDataSource(attachement,
					mimeType);
			email.attach(bads, fileName, "likontrotech");

			email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendEMail(String subject, String text, String to) {
		sendEMail(subject, text, to, null, null, null);
	}

}
