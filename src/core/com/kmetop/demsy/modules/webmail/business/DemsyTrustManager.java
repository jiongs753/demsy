package com.kmetop.demsy.modules.webmail.business;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.kmetop.demsy.modules.webmail.exceptions.DemsyCertificateException;

public class DemsyTrustManager implements X509TrustManager {
	private X509TrustManager adapteeTrustManager = null;

	private X509Certificate[] acceptedCerts = null;

	public DemsyTrustManager() {
		try {

			TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
			tmf.init((KeyStore) null);
			adapteeTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
		} catch (Exception e) {

			throw (new RuntimeException(e.getMessage(), e));
		}
	}

	public X509Certificate[] getAcceptedCerts() {

		return acceptedCerts;
	}

	public void setAcceptedCerts(X509Certificate[] acceptedCerts) {

		this.acceptedCerts = acceptedCerts;
	}

	public void checkClientTrusted(X509Certificate[] certs, String authType)
			throws CertificateException {

		try {

			this.adapteeTrustManager.checkClientTrusted(certs, authType);
		} catch (CertificateException ce) {

			throw (new DemsyCertificateException(certs, ce));
		}
	}

	public void checkServerTrusted(X509Certificate[] certs, String authType)
			throws CertificateException {

		try {

			this.adapteeTrustManager.checkServerTrusted(certs, authType);
		} catch (CertificateException ce) {

			if ((this.acceptedCerts == null) || (!Arrays.equals(this.acceptedCerts, certs))) {

				throw (new DemsyCertificateException(certs, ce));
			}
		}
	}

	public X509Certificate[] getAcceptedIssuers() {

		return (adapteeTrustManager.getAcceptedIssuers());
	}

}
