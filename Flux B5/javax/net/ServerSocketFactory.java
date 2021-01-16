package javax.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public abstract class ServerSocketFactory {
   private static ServerSocketFactory theFactory;
   // $FF: synthetic field
   static Class class$javax$net$ServerSocketFactory;

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public abstract ServerSocket createServerSocket(int var1) throws IOException;

   public abstract ServerSocket createServerSocket(int var1, int var2) throws IOException;

   public abstract ServerSocket createServerSocket(int var1, int var2, InetAddress var3) throws IOException;

   public static ServerSocketFactory getDefault() {
      if (theFactory == null) {
         Class var0 = class$javax$net$ServerSocketFactory != null ? class$javax$net$ServerSocketFactory : (class$javax$net$ServerSocketFactory = class$("javax.net.ServerSocketFactory"));
         synchronized(var0){}

         try {
            theFactory = new DefaultServerSocketFactory();
         } catch (Throwable var2) {
            throw var2;
         }
      }

      return theFactory;
   }
}
