package javax.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

class DefaultServerSocketFactory extends ServerSocketFactory {
   public ServerSocket createServerSocket(int var1) throws IOException {
      return new ServerSocket(var1);
   }

   public ServerSocket createServerSocket(int var1, int var2) throws IOException {
      return new ServerSocket(var1, var2);
   }

   public ServerSocket createServerSocket(int var1, int var2, InetAddress var3) throws IOException {
      return new ServerSocket(var1, var2, var3);
   }
}
