package com.kmetop.demsy.lang;

public class Status {
	private boolean success;

	private String message;

	private String redirect;

	private Object data;

	private String nextToken;

	public Status(boolean s) {
		this.success = s;
	}

	public Status(boolean s, String msg) {
		this.success = s;
		this.message = msg;
	}

	public Status(boolean s, String msg, String redirect) {
		this.success = s;
		this.message = msg;
		this.redirect = redirect;
	}

	public Status(boolean s, String msg, String redirect, Object data) {
		this.success = s;
		this.message = msg;
		this.redirect = redirect;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public Object getData() {
		return data;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public String getNextToken() {
		return nextToken;
	}

	public void setNextToken(String nextToken) {
		this.nextToken = nextToken;
	}
}
