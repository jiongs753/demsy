package com.kmetop.demsy.util;

public interface PathMatcher {
	boolean isPattern(String path);

	boolean match(String pattern, String path);

	boolean matchStart(String pattern, String path);

	String extractPathWithinPattern(String pattern, String path);

}
