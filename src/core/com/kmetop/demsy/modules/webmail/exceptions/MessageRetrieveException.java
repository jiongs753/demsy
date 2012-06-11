package com.kmetop.demsy.modules.webmail.exceptions;

public class MessageRetrieveException extends Exception {
	private static final long serialVersionUID = -3709479124876059382L;

	public MessageRetrieveException() {
		super();
	}

	public MessageRetrieveException(String message) {
		super(message);
	}

	public MessageRetrieveException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageRetrieveException(Throwable cause) {
		super(cause);
	}

}
