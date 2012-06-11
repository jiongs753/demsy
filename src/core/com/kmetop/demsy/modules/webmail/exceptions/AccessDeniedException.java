package com.kmetop.demsy.modules.webmail.exceptions;

public class AccessDeniedException extends Exception {

	private static final long serialVersionUID = 4209087821626315511L;

	public AccessDeniedException() {
		super();
	}

	public AccessDeniedException(String message) {
		super(message);
	}

	public AccessDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccessDeniedException(Throwable cause) {
		super(cause);
	}

}
