package javax.net.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;

class DefaultSSLServerSocketFactory extends SSLServerSocketFactory {
   public ServerSocket createServerSocket(int var1) throws IOException {
      throw new SocketException("no SSL Server Sockets");
   }

   public ServerSocket createServerSocket(int var1, int var2) throws IOException {
      throw new SocketException("no SSL Server Sockets");
   }

   public ServerSocket createServerSocket(int var1, int var2, InetAddress var3) throws IOException {
      throw new SocketException("no SSL Server Sockets");
   }

   public String[] getDefaultCipherSuites() {
      return new String[0];
   }

   public String[] getSupportedCipherSuites() {
      return new String[0];
   }
}
