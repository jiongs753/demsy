package com.kmetop.demsy.modules.webmail.exceptions;

public class MessageMovementException extends Exception {

	private static final long serialVersionUID = 5693305569212745310L;

	public MessageMovementException() {
		super();
	}

	public MessageMovementException(String message) {
		super(message);
	}

	public MessageMovementException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageMovementException(Throwable cause) {
		super(cause);
	}

}
