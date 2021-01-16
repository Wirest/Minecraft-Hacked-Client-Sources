package javax.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

class DefaultSocketFactory extends SocketFactory {
   public Socket createSocket(String var1, int var2) throws IOException, UnknownHostException {
      return new Socket(var1, var2);
   }

   public Socket createSocket(String var1, int var2, InetAddress var3, int var4) throws IOException, UnknownHostException {
      return new Socket(var1, var2, var3, var4);
   }

   public Socket createSocket(InetAddress var1, int var2) throws IOException {
      return new Socket(var1, var2);
   }

   public Socket createSocket(InetAddress var1, int var2, InetAddress var3, int var4) throws IOException {
      return new Socket(var1, var2, var3, var4);
   }
}
