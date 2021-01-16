package javax.net.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public abstract class SSLServerSocket extends ServerSocket {
   protected SSLServerSocket(int var1) throws IOException {
      super(var1);
   }

   protected SSLServerSocket(int var1, int var2) throws IOException {
      super(var1, var2);
   }

   protected SSLServerSocket(int var1, int var2, InetAddress var3) throws IOException {
      super(var1, var2, var3);
   }

   public abstract boolean getEnableSessionCreation();

   public abstract String[] getEnabledCipherSuites();

   public abstract boolean getNeedClientAuth();

   public abstract String[] getSupportedCipherSuites();

   public abstract boolean getUseClientMode();

   public abstract void setEnableSessionCreation(boolean var1);

   public abstract void setEnabledCipherSuites(String[] var1);

   public abstract void setNeedClientAuth(boolean var1);

   public abstract void setUseClientMode(boolean var1);
}
