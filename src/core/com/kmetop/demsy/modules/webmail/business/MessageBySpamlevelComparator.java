package com.kmetop.demsy.modules.webmail.business;

import javax.mail.Message;

import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

public class MessageBySpamlevelComparator<T extends Message> extends ReversibleComparator<T> {

	private static final long serialVersionUID = 5556264714168733793L;

	protected static final Log LOG = Logs.getLog(MessageBySpamlevelComparator.class);

	public MessageBySpamlevelComparator() {
	}

	public MessageBySpamlevelComparator(boolean reverse) {
		this.reverse = reverse;
	}

	public int compare(T m1, T m2) {
		int rueck = 0;

		String slEins = null;
		String slZwei = null;

		if (m1 instanceof OfflineMimeMessage) {
			slEins = ((OfflineMimeMessage) m1).getSpamLevel();
		}
		if (m2 instanceof OfflineMimeMessage) {
			slZwei = ((OfflineMimeMessage) m2).getSpamLevel();
		}

		boolean einsDa = (slEins != null);
		boolean zweiDa = (slZwei != null);

		if (einsDa && zweiDa) {
			rueck = slEins.length() - slZwei.length();
		} else if ((!einsDa) && zweiDa) {
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
	}

}
