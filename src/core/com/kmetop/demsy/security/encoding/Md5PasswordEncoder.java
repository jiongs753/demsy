package com.kmetop.demsy.security.encoding;

public class Md5PasswordEncoder extends MessageDigestPasswordEncoder {

	public Md5PasswordEncoder() {
		super("MD5");
	}
}