package com.kmetop.demsy.modules.webmail.business;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.kmetop.demsy.modules.webmail.spi.Constants;

public class OverviewOfflineMimeMessage extends OfflineMimeMessage {
	private String subjectTooltip = Constants.LEERSTRING;

	public OverviewOfflineMimeMessage(MimeMessage mimeMessage) throws MessagingException {
		super(mimeMessage);
	}

	public OverviewOfflineMimeMessage(Session session) throws MessagingException {
		super(session);
	}

	public String getSubjectTooltip() {
		return subjectTooltip;
	}

	public void setSubjectTooltip(String subjectTooltip) {
		this.subjectTooltip = subjectTooltip;
	}

}
