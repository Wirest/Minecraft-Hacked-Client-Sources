package moonx.ohare.client.utils.thealtening.utilities;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLVerification {

	private boolean verified = false;

	public void verify() {

		if (!verified) {
			bypassSSL();
			whitelistTheAltening();
			verified = true;
		}

	}

	private void bypassSSL() {
		TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager() {
					@Override
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					@Override
					public void checkClientTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {
					}

					@Override
					public void checkServerTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {
					}
				}
		};
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}
	}

	private void whitelistTheAltening() {

		HttpsURLConnection.setDefaultHostnameVerifier(
				(hostname, sslSession) -> hostname.equals("authserver.thealtening.com") || hostname.equals("sessionserver.thealtening.com"));

	}
}
