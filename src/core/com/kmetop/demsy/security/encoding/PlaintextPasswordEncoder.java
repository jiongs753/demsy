package com.kmetop.demsy.security.encoding;

public class PlaintextPasswordEncoder extends BasePasswordEncoder {

	private boolean ignorePasswordCase = false;

	public String encodePassword(String rawPass, Object salt) {
		return mergePasswordAndSalt(rawPass, salt, true);
	}

	public boolean isIgnorePasswordCase() {
		return ignorePasswordCase;
	}

	public boolean isValidPassword(String encPass, String rawPass, Object salt) {
		String pass1 = encPass + "";

		String pass2 = mergePasswordAndSalt(rawPass, salt, false);

		if (!ignorePasswordCase) {
			return pass1.equals(pass2);
		} else {
			return pass1.equalsIgnoreCase(pass2);
		}
	}

	public String[] obtainPasswordAndSalt(String password) {
		return demergePasswordAndSalt(password);
	}

	public void setIgnorePasswordCase(boolean ignorePasswordCase) {
		this.ignorePasswordCase = ignorePasswordCase;
	}
}