package com.kmetop.demsy.security;

import com.kmetop.demsy.lang.DemsyException;

public class SecurityException extends DemsyException {

	private static final long serialVersionUID = -6634480503807002472L;

	private int code;

	public SecurityException() {
		super();
	}

	public SecurityException(int code) {
		super();
		this.code = code;
	}

	public SecurityException(Throwable e) {
		super(e);
	}

	public SecurityException(String msg, Throwable e) {
		super(msg, e);
	}

	public SecurityException(String fmt, Object... args) {
		super(String.format(fmt, args));
	}

	public int getCode() {
		return code;
	}
}
