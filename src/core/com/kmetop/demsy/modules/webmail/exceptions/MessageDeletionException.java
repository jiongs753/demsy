package com.kmetop.demsy.modules.webmail.exceptions;

public class MessageDeletionException extends Exception {

	private static final long serialVersionUID = 5693305569212800310L;

	public MessageDeletionException() {
		super();
	}

	public MessageDeletionException(String message) {
		super(message);
	}

	public MessageDeletionException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageDeletionException(Throwable cause) {
		super(cause);
	}

}
