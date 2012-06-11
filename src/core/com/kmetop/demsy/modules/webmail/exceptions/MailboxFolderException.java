package com.kmetop.demsy.modules.webmail.exceptions;

public class MailboxFolderException extends Exception {

	private static final long serialVersionUID = 4158788849930892133L;

	private String folderName = null;

	public MailboxFolderException(String message, String folderName) {
		super(message);
		this.folderName = folderName;
	}

	public MailboxFolderException(String message, Throwable cause, String folderName) {
		super(message, cause);
		this.folderName = folderName;
	}

	public String getFolderName() {
		return folderName;
	}

}
