package com.kmetop.demsy.modules.webmail.exceptions;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class DemsyCertificateException extends CertificateException {
	private static final long serialVersionUID = 5916241713232215246L;

	private Certificate[] certs = null;

	private CertificateException cause = null;

	public DemsyCertificateException(Certificate[] certs, CertificateException cause) {
		this.certs = certs;
		this.cause = cause;
	}

	public CertificateException getCause() {
		return cause;
	}

	public Certificate[] getCerts() {

		return certs;
	}

}
