package javax.net.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

class DefaultSSLSocketFactory extends SSLSocketFactory {
   public Socket createSocket(String var1, int var2) throws IOException {
      throw new SocketException("SSL implementation not available");
   }

   public Socket createSocket(String var1, int var2, InetAddress var3, int var4) throws IOException {
      throw new SocketException("SSL implementation not available");
   }

   public Socket createSocket(InetAddress var1, int var2) throws IOException {
      throw new SocketException("SSL implementation not available");
   }

   public Socket createSocket(InetAddress var1, int var2, InetAddress var3, int var4) throws IOException {
      throw new SocketException("SSL implementation not available");
   }

   public Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws IOException {
      throw new SocketException("SSL implementation not available");
   }

   public String[] getDefaultCipherSuites() {
      return new String[0];
   }

   public String[] getSupportedCipherSuites() {
      return new String[0];
   }
}
