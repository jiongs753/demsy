package com.kmetop.demsy.modules.webmail.business;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;

import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

public class MessageBySenderComparator<T extends Message> extends ReversibleComparator<T> {

	private static final long serialVersionUID = 5556264714168821793L;

	protected static final Log LOG = Logs.getLog(MessageBySenderComparator.class);

	public MessageBySenderComparator() {
	}

	public MessageBySenderComparator(boolean reverse) {
		this.reverse = reverse;
	}

	public int compare(T m1, T m2) {
		int rueck = 0;

		try {
			Address[] senderEins = m1.getFrom();
			Address[] senderZwei = m2.getFrom();

			boolean einsDa = ((senderEins != null) && (senderEins.length >= 1));
			boolean zweiDa = ((senderZwei != null) && (senderZwei.length >= 1));

			if (einsDa && zweiDa) {
				String einsCleaned = senderEins[0].toString().replaceAll("\"", "");
				String zweiCleaned = senderZwei[0].toString().replaceAll("\"", "");
				rueck = einsCleaned.compareToIgnoreCase(zweiCleaned);
			}

			else if ((!einsDa) && zweiDa) {
				rueck = (-50);
			} else if (einsDa && (!zweiDa)) {
				rueck = 50;
			} else {
				rueck = 0;
			}

			if (this.reverse) {
				rueck = rueck * (-1);
			}

			return (rueck);
		} catch (MessagingException me) {
			LOG.error("[compare] Problem getting the sender.", me);
			return (0);
		}
	}

}
