package javax.net.ssl;

import javax.security.cert.X509Certificate;

public interface SSLSession {
   String getCipherSuite();

   long getCreationTime();

   byte[] getId();

   long getLastAccessedTime();

   X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException;

   String getPeerHost();

   SSLSessionContext getSessionContext();

   Object getValue(String var1);

   String[] getValueNames();

   void invalidate();

   void putValue(String var1, Object var2);

   void removeValue(String var1);
}
