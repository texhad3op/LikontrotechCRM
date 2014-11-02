package com.likontrotech.ejb;

import javax.ejb.Local;

@Local
public interface SendMailLocal {
	void sendEMail(String subject, String text, String to, byte[] attachement, String fileName, String mimeType);
	 void sendEMail(String subject, String text, String to);
}
