package com.kmetop.demsy.modules.webmail;

import com.kmetop.demsy.modules.webmail.business.DefaultMailboxConnection;
import com.kmetop.demsy.modules.webmail.business.DemsyTrustManager;
import com.kmetop.demsy.modules.webmail.model.LoginModel;

public class MailboxConnectionFactory {
	private static MailboxConnectionFactory mailboxConnectionFactory = new MailboxConnectionFactory();

	private MailboxConnectionFactory() {
	}

	public static MailboxConnectionFactory getInstance() {
		return (mailboxConnectionFactory);
	}

	public MailboxConnection createMailboxConnection(LoginModel loginData,
			DemsyTrustManager trustManager) throws InstantiationException {
		return (new DefaultMailboxConnection(loginData, trustManager));
	}

}
