package com.kmetop.demsy.lang;

public class ConfigException extends RuntimeException {

	private static final long serialVersionUID = -7480728064068398328L;

	private String redirect;

	public ConfigException() {
		super();
	}

	public ConfigException(Throwable e) {
		super(e);
	}

	public ConfigException(String msg, Throwable e) {
		super(msg, e);
	}

	public ConfigException(String fmt, Object... args) {
		super(String.format(fmt, args));
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
}
