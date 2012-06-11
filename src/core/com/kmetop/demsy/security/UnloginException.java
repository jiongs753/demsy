package com.kmetop.demsy.security;

public class UnloginException extends SecurityException {

	private static final long serialVersionUID = -7480728064068398328L;

	private String redirect;

	public UnloginException() {
		super();
	}

	public UnloginException(Throwable e) {
		super(e);
	}

	public UnloginException(String msg, Throwable e) {
		super(msg, e);
	}

	public UnloginException(String fmt, Object... args) {
		super(String.format(fmt, args));
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
}
