package javax.net.ssl;

import java.io.IOException;
import java.net.Socket;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Security;
import javax.net.SocketFactory;

public abstract class SSLSocketFactory extends SocketFactory {
   private static SSLSocketFactory a;
   // $FF: synthetic field
   static Class b;

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public abstract Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws IOException;

   public static synchronized SocketFactory getDefault() {
      if (a == null) {
         String var0 = a();

         try {
            Class var1 = null;

            try {
               var1 = Class.forName(var0);
            } catch (ClassNotFoundException var3) {
               ClassLoader var2 = ClassLoader.getSystemClassLoader();
               if (var2 != null) {
                  var1 = var2.loadClass(var0);
               }
            }

            Class var5 = b != null ? b : (b = class$("com.sun.net.ssl.internal.ssl.SSLSocketFactoryImpl"));
            if (var1 != var5) {
               a = new DefaultSSLSocketFactory();
            } else {
               a = (SSLSocketFactory)var1.newInstance();
            }
         } catch (Exception var4) {
            a = new DefaultSSLSocketFactory();
         }
      }

      return a;
   }

   public abstract String[] getDefaultCipherSuites();

   private static String a() {
      String var0 = (String)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return Security.getProperty("ssl.SocketFactory.provider");
         }
      });
      if (var0 == null) {
         var0 = "com.sun.net.ssl.internal.ssl.SSLSocketFactoryImpl";
      }

      return var0;
   }

   public abstract String[] getSupportedCipherSuites();
}
