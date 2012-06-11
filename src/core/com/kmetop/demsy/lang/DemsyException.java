package com.kmetop.demsy.lang;

public class DemsyException extends RuntimeException {
	private static final long serialVersionUID = 5608067870963671246L;

	public DemsyException() {
		super();
	}

	public DemsyException(Throwable e) {
		super(e);
	}

	public DemsyException(String msg, Throwable e) {
		super(msg, e);
	}

	public DemsyException(String fmt, Object... args) {
		super(String.format(fmt, args));
	}
}
