package javax.net.ssl;

import java.util.EventObject;
import javax.security.cert.X509Certificate;

public class HandshakeCompletedEvent extends EventObject {
   private transient SSLSession a;

   public HandshakeCompletedEvent(SSLSocket var1, SSLSession var2) {
      super(var1);
      this.a = var2;
   }

   public String getCipherSuite() {
      return this.a.getCipherSuite();
   }

   public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
      return this.a.getPeerCertificateChain();
   }

   public SSLSession getSession() {
      return this.a;
   }

   public SSLSocket getSocket() {
      return (SSLSocket)this.getSource();
   }
}
