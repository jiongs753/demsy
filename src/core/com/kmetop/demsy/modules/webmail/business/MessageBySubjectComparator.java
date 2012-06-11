package com.kmetop.demsy.modules.webmail.business;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.oro.text.perl.Perl5Util;

import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

public class MessageBySubjectComparator<T extends Message> extends ReversibleComparator<T> {
	private static final long serialVersionUID = 5556264934168711793L;
	protected static final Log LOG = Logs.getLog(MessageBySubjectComparator.class);

	private Perl5Util perl5Util = new Perl5Util();

	public MessageBySubjectComparator() {
	}

	public MessageBySubjectComparator(boolean reverse) {
		this.reverse = reverse;
	}

	public int compare(T m1, T m2) {
		int rueck = 0;

		try {

			String subjectEins = m1.getSubject();
			String subjectZwei = m2.getSubject();

			boolean einsDa = (subjectEins != null);
			boolean zweiDa = (subjectZwei != null);

			if (einsDa && zweiDa) {
				if (perl5Util.match("#^[a-zA-Z]{2,3}:#", subjectEins)) {
					subjectEins = perl5Util.substitute("s#^[a-zA-Z]{2,3}:\\s*##", subjectEins);
				}
				if (perl5Util.match("#^[a-zA-Z]{2,3}:#", subjectZwei)) {
					subjectZwei = perl5Util.substitute("s#^[a-zA-Z]{2,3}:\\s*##", subjectZwei);
				}
				rueck = subjectEins.trim().compareToIgnoreCase(subjectZwei.trim());
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
		} catch (MessagingException me) {
			LOG.error("[compare] Problem getting the subjects.", me);
			return (0);
		}
	}

}
