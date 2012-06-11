package com.kmetop.demsy.modules.webmail.exceptions;

public class LogoutException extends Exception {

	private static final long serialVersionUID = 2121434747823576896L;

	public LogoutException() {
		super();
	}

	public LogoutException(String message) {
		super(message);
	}

	public LogoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public LogoutException(Throwable cause) {
		super(cause);
	}

}
