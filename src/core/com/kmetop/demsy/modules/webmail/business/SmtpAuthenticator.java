package com.kmetop.demsy.modules.webmail.business;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SmtpAuthenticator extends Authenticator {

	private PasswordAuthentication passwordAuthentication = null;

	public SmtpAuthenticator(String smptAuthUser, String smtpAuthPass) {
		this.passwordAuthentication = new PasswordAuthentication(smptAuthUser, smtpAuthPass);
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return (this.passwordAuthentication);
	}

}
