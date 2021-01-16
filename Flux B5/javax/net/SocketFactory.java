package javax.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class SocketFactory {
   private static SocketFactory theFactory;
   // $FF: synthetic field
   static Class class$javax$net$SocketFactory;

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public abstract Socket createSocket(String var1, int var2) throws IOException, UnknownHostException;

   public abstract Socket createSocket(String var1, int var2, InetAddress var3, int var4) throws IOException, UnknownHostException;

   public abstract Socket createSocket(InetAddress var1, int var2) throws IOException;

   public abstract Socket createSocket(InetAddress var1, int var2, InetAddress var3, int var4) throws IOException;

   public static SocketFactory getDefault() {
      if (theFactory == null) {
         Class var0 = class$javax$net$SocketFactory != null ? class$javax$net$SocketFactory : (class$javax$net$SocketFactory = class$("javax.net.SocketFactory"));
         synchronized(var0){}

         try {
            theFactory = new DefaultSocketFactory();
         } catch (Throwable var2) {
            throw var2;
         }
      }

      return theFactory;
   }
}
