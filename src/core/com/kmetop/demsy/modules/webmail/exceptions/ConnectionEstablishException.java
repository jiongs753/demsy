package com.kmetop.demsy.modules.webmail.exceptions;

public class ConnectionEstablishException extends Exception {

	private static final long serialVersionUID = 5537038375284009080L;

	private String host = null;

	private int port = (-1);

	public ConnectionEstablishException(String message, String host, int port) {
		super(message);
		this.host = host;
		this.port = port;
	}

	public ConnectionEstablishException(String message, Throwable cause, String host, int port) {
		super(message, cause);
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

}
