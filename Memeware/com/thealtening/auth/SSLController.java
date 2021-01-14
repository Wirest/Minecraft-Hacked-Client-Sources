
package com.thealtening.auth;

import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public final class SSLController {
    private static final TrustManager[] ALL_TRUSTING_TRUST_MANAGER = new TrustManager[]{new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }};
    private static final HostnameVerifier ALTENING_HOSTING_VERIFIER = (hostname, session) -> hostname.equals("authserver.thealtening.com") || hostname.equals("sessionserver.thealtening.com");
    private final SSLSocketFactory allTrustingFactory;
    private final SSLSocketFactory originalFactory;
    private final HostnameVerifier originalHostVerifier;

    public SSLController() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, ALL_TRUSTING_TRUST_MANAGER, new SecureRandom());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        this.allTrustingFactory = sc.getSocketFactory();
        this.originalFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
        this.originalHostVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
    }

    public void enableCertificateValidation() {
        this.updateCertificateValidation(this.originalFactory, this.originalHostVerifier);
    }

    public void disableCertificateValidation() {
        this.updateCertificateValidation(this.allTrustingFactory, ALTENING_HOSTING_VERIFIER);
    }

    private void updateCertificateValidation(SSLSocketFactory factory, HostnameVerifier hostnameVerifier) {
        HttpsURLConnection.setDefaultSSLSocketFactory(factory);
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    }

}

