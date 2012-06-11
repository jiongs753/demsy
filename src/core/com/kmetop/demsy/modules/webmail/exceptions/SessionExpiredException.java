package com.kmetop.demsy.modules.webmail.exceptions;

public class SessionExpiredException extends Exception {

	private static final long serialVersionUID = -668952224495318411L;

	public SessionExpiredException() {
		super();
	}

	public SessionExpiredException(String message) {
		super(message);
	}

	public SessionExpiredException(String message, Throwable cause) {
		super(message, cause);
	}

	public SessionExpiredException(Throwable cause) {
		super(cause);
	}

}
