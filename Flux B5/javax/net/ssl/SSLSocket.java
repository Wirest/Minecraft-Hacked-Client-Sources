package javax.net.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class SSLSocket extends Socket {
   protected SSLSocket() {
   }

   protected SSLSocket(String var1, int var2) throws IOException, UnknownHostException {
      super(var1, var2);
   }

   protected SSLSocket(String var1, int var2, InetAddress var3, int var4) throws IOException, UnknownHostException {
      super(var1, var2, var3, var4);
   }

   protected SSLSocket(InetAddress var1, int var2) throws IOException, UnknownHostException {
      super(var1, var2);
   }

   protected SSLSocket(InetAddress var1, int var2, InetAddress var3, int var4) throws IOException, UnknownHostException {
      super(var1, var2, var3, var4);
   }

   public abstract void addHandshakeCompletedListener(HandshakeCompletedListener var1);

   public abstract boolean getEnableSessionCreation();

   public abstract String[] getEnabledCipherSuites();

   public abstract boolean getNeedClientAuth();

   public abstract SSLSession getSession();

   public abstract String[] getSupportedCipherSuites();

   public abstract boolean getUseClientMode();

   public abstract void removeHandshakeCompletedListener(HandshakeCompletedListener var1);

   public abstract void setEnableSessionCreation(boolean var1);

   public abstract void setEnabledCipherSuites(String[] var1);

   public abstract void setNeedClientAuth(boolean var1);

   public abstract void setUseClientMode(boolean var1);

   public abstract void startHandshake() throws IOException;
}
