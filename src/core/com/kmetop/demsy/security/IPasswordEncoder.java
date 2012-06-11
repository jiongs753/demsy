package com.kmetop.demsy.security;

public interface IPasswordEncoder {

	String encodePassword(String rawPass, Object salt);

	boolean isValidPassword(String encPass, String rawPass, Object salt);
}
