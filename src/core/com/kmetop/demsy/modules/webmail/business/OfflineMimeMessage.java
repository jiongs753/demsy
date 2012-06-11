package com.kmetop.demsy.modules.webmail.business;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class OfflineMimeMessage extends MimeMessage {

	private String spamLevel = null;

	public OfflineMimeMessage(MimeMessage mimeMessage) throws MessagingException {
		super(mimeMessage);
	}

	public OfflineMimeMessage(Session session) throws MessagingException {
		super(session);
	}

	public String getSpamLevel() {
		return this.spamLevel;
	}

	public void setSpamLevel(String spamLevel) {
		this.spamLevel = spamLevel;
	}

	public void setMessageNumber(int messageNumber) {
		this.msgnum = messageNumber;
	}

	public Address[] getFrom() throws MessagingException {
		try {
			Address[] fromArray = super.getFrom();

			if (fromArray == null) {
				fromArray = new Address[0];
			}

			return (fromArray);
		} catch (Exception e) {

			e.printStackTrace();
			return (new Address[0]);
		}
	}

}
