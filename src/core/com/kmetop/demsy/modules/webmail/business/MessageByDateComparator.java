package com.kmetop.demsy.modules.webmail.business;

import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

public class MessageByDateComparator<T extends Message> extends ReversibleComparator<T> {

	private static final long serialVersionUID = 5556264714168711793L;

	protected static final Log LOG = Logs.getLog(MessageByDateComparator.class);

	public MessageByDateComparator() {
	}

	public MessageByDateComparator(boolean reverse) {
		this.reverse = reverse;
	}

	public int compare(T m1, T m2) {
		int rueck = 0;

		try {

			Date dateEins = m1.getSentDate();
			Date dateZwei = m2.getSentDate();

			boolean einsDa = (dateEins != null);
			boolean zweiDa = (dateZwei != null);

			if (einsDa && zweiDa) {
				rueck = dateEins.compareTo(dateZwei);
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
			LOG.error("[compare] Problem getting the sent-dates.", me);
			return (0);
		}
	}

}
