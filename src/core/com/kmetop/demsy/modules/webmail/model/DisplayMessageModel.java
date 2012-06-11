package com.kmetop.demsy.modules.webmail.model;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

import com.kmetop.demsy.modules.webmail.spi.Lifecycle;

public class DisplayMessageModel implements Lifecycle, Serializable {

	private static final long serialVersionUID = -5647805002355682456L;

	private Message originMessage = null;

	private List displayParts = null;

	private Map inlineParts = null;

	private Map multiparts = null;

	private boolean selected = false;

	public Message getOriginMessage() {
		return originMessage;
	}

	public void setOriginMessage(Message originMessage) {
		this.originMessage = originMessage;
	}

	public List getDisplayParts() {
		return displayParts;
	}

	public void setDisplayParts(List displayParts) {
		this.displayParts = displayParts;
	}

	public Map getInlineParts() {
		return inlineParts;
	}

	public void setInlineParts(Map inlineParts) {
		this.inlineParts = inlineParts;
	}

	public Map getMultiparts() {
		return this.multiparts;
	}

	public void setMultiparts(Map multiparts) {
		this.multiparts = multiparts;
	}

	public boolean isSelected() {
		return (this.selected);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Address[] getToRecipients() {
		try {
			return (this.originMessage.getRecipients(Message.RecipientType.TO));
		} catch (Exception e) {
			e.printStackTrace();
			return (null);
		}
	}

	public Address[] getCcRecipients() {
		try {
			return (this.originMessage.getRecipients(Message.RecipientType.CC));
		} catch (MessagingException e) {
			e.printStackTrace();
			return (null);
		}
	}

	public Address[] getBccRecipients() {
		try {
			return (this.originMessage.getRecipients(Message.RecipientType.BCC));
		} catch (MessagingException e) {
			e.printStackTrace();
			return (null);
		}
	}

	public String getMessageSource() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			this.originMessage.writeTo(baos);

			return (baos.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return (e.getMessage());
		}
	}

	public List getAllHeaderLinesAsList() {
		try {
			List<String> allHeaderLines = new ArrayList<String>();
			Enumeration headerEnum = this.originMessage.getAllHeaders();

			while (headerEnum.hasMoreElements()) {

				Header currentHeader = (Header) headerEnum.nextElement();
				String headerLine = currentHeader.getName() + ": " + currentHeader.getValue();

				allHeaderLines.add(headerLine);
			}

			return (allHeaderLines);
		} catch (MessagingException e) {
			throw (new RuntimeException("Konnte Header-Lines nicht beziehen.", e));
		}
	}

	public void reset() {

		this.originMessage = null;
		this.displayParts = null;
		this.inlineParts = null;
		this.multiparts = null;
		this.selected = false;
	}

	public void destroy() {
		this.reset();
	}

}
