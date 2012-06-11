/*
 * @(#)JavamailUtils.java 1.00 2007/08/31 Copyright (c) 2007, Stephan Sann
 * 31.08.2007 ssann Vers. 1.0 created
 */

package com.kmetop.demsy.modules.webmail.util;

import java.util.Properties;

import javax.mail.Session;

import com.kmetop.demsy.modules.webmail.business.DemsyTrustManager;
import com.kmetop.demsy.modules.webmail.business.SmtpAuthenticator;
import com.kmetop.demsy.modules.webmail.enums.MailTransportProtocolEnum;
import com.kmetop.demsy.modules.webmail.model.SmtpConnectionModel;

/**
 * Stellt Helfermethoden im Javamail-Kontext zur Verfuegung.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public class JavamailUtils {

	/**
	 * Liefert Properties, die fuer Mail-Sessions benoetigt werden. Zunaechst
	 * wird versucht, einen Clone der System-Properties zu liefern. Wenn das
	 * nicht klappt (Security-Manager), dann wird ein leeres Properties-Objekt
	 * zurueckgegeben.
	 * 
	 * @return <code>Properties</code>-Objekt
	 */
	public static Properties getProperties() {

		try {

			return ((Properties) System.getProperties().clone());
		} catch (Exception e) {

			return (new Properties());
		}
	}

	/**
	 * Erstellt eine javax.mail.Session (fuer eine MimeMessage).
	 * 
	 * @param smtpConnection
	 *            <code>SmtpConnectionBean</code>, aus der die Infos fuer die
	 *            Session kommen.
	 * @param trustManager
	 *            TrustManager for secure connections.
	 * @return <code>Session</code>-Objekt gemaess SmtpConnection
	 */
	public static Session assembleJavaxMailSession(SmtpConnectionModel smtpConnection,
			DemsyTrustManager trustManager) throws Exception {

		// Welches Protokoll?
		String proto = smtpConnection.isSslConnection() ? MailTransportProtocolEnum.SMTP_SSL
				.getProtocolId() : MailTransportProtocolEnum.SMTP.getProtocolId();

		// Properties holen und Werte aus der Bean setzen
		Properties props = getProperties();

		props.setProperty("mail.transport.protocol", proto);
		props.setProperty("mail." + proto + ".host", smtpConnection.getSmtpHost());
		props.setProperty("mail." + proto + ".port", smtpConnection.getSmtpPort());
		props.setProperty("mail." + proto + ".quitwait", "false");

		// TODO:是否需要使用安全连接
		// SSL or TLS
		// if (smtpConnection.isSslConnection() ||
		// smtpConnection.isTlsConnection()) {
		//
		// MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
		// socketFactory.setTrustManagers(new TrustManager[] { trustManager });
		// props.put("mail." + proto + ".ssl.socketFactory", socketFactory);
		//
		// // Bei SSL noch ein zusaetzliche Property...
		// if (smtpConnection.isSslConnection()) {
		//
		// props.setProperty("mail.smtps.socketFactory.fallback", "false");
		// }
		//
		// // Bei TLS noch ein zusaetzliche Property...
		// if (smtpConnection.isTlsConnection()) {
		//
		// props.setProperty("mail." + proto + ".starttls.enable", "true");
		// }
		// }

		// SMTP-Auth?
		SmtpAuthenticator smtpAuthenticator = null;
		String smtpAuthUser = smtpConnection.getSmtpAuthUser();
		String smtpAuthPass = smtpConnection.getSmtpAuthPass();

		if ((smtpAuthUser != null) && (smtpAuthPass.length() >= 1) && (smtpAuthPass != null)
				&& (smtpAuthPass.length() >= 1)) {

			smtpAuthenticator = new SmtpAuthenticator(smtpAuthUser, smtpAuthPass);

			// Authentifizierung erzwingen
			props.setProperty("mail." + proto + ".auth", "true");
		}

		Session session = Session.getInstance(props, smtpAuthenticator);

		// Just in case...
		// session.setDebug(true);

		return (session);
	}

}
