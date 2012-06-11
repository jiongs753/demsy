package com.kmetop.demsy.security.encoding;

public abstract class BaseDigestPasswordEncoder extends BasePasswordEncoder {

	private boolean encodeHashAsBase64 = false;

	public boolean getEncodeHashAsBase64() {
		return encodeHashAsBase64;
	}

	public void setEncodeHashAsBase64(boolean encodeHashAsBase64) {
		this.encodeHashAsBase64 = encodeHashAsBase64;
	}
}