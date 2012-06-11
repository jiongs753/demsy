package com.kmetop.demsy.modules.webmail.business;

import java.io.Serializable;
import java.util.Comparator;

import javax.mail.Folder;

import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

public class FolderByFullNameComparator implements Comparator<Folder>, Serializable {

	private static final long serialVersionUID = -5513920870856710489L;

	protected static final Log LOG = Logs.getLog(FolderByFullNameComparator.class);

	public int compare(Folder o1, Folder o2) {
		int rueck = 0;

		try {

			boolean einsDa = (o1 != null);
			boolean zweiDa = (o2 != null);

			if (einsDa && zweiDa) {

				String fullNameFolderEins = ((Folder) o1).getFullName();
				String fullNameFolderZwei = ((Folder) o2).getFullName();

				rueck = fullNameFolderEins.compareToIgnoreCase(fullNameFolderZwei);
			}

			else if ((!einsDa) && zweiDa) {

				rueck = (-50);
			} else if (einsDa && (!zweiDa)) {

				rueck = 50;
			} else {

				rueck = 0;
			}

			return (rueck);
		} catch (Exception e) {
			LOG.error("[compare] Problem beim Beziehen der Folder.", e);
			return (0);
		}
	}

}
