package com.kmetop.demsy.security.encoding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class MessageDigestPasswordEncoder extends BaseDigestPasswordEncoder {

	private final String algorithm;

	public MessageDigestPasswordEncoder(String algorithm) {
		this(algorithm, false);
	}

	public MessageDigestPasswordEncoder(String algorithm, boolean encodeHashAsBase64) throws IllegalArgumentException {
		this.algorithm = algorithm;
		setEncodeHashAsBase64(encodeHashAsBase64);
		// Validity Check
		getMessageDigest();
	}

	public String encodePassword(String rawPass, Object salt) {
		String saltedPass = mergePasswordAndSalt(rawPass, salt, false);

		MessageDigest messageDigest = getMessageDigest();

		byte[] digest = messageDigest.digest(saltedPass.getBytes());

		if (getEncodeHashAsBase64()) {
			return new String(Base64.encodeBase64(digest));
		} else {
			return new String(Hex.encodeHex(digest));
		}
	}

	protected final MessageDigest getMessageDigest() throws IllegalArgumentException {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm [" + algorithm + "]");
		}
	}

	public boolean isValidPassword(String encPass, String rawPass, Object salt) {
		String pass1 = "" + encPass;
		String pass2 = encodePassword(rawPass, salt);

		return pass1.equals(pass2);
	}

	public String getAlgorithm() {
		return algorithm;
	}
}