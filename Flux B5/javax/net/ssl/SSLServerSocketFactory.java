package javax.net.ssl;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Security;
import javax.net.ServerSocketFactory;

public abstract class SSLServerSocketFactory extends ServerSocketFactory {
   private static SSLServerSocketFactory a;
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

   public static synchronized ServerSocketFactory getDefault() {
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

            Class var5 = b != null ? b : (b = class$("com.sun.net.ssl.internal.ssl.SSLServerSocketFactoryImpl"));
            if (var1 != var5) {
               a = new DefaultSSLServerSocketFactory();
            } else {
               a = (SSLServerSocketFactory)var1.newInstance();
            }
         } catch (Exception var4) {
            a = new DefaultSSLServerSocketFactory();
         }
      }

      return a;
   }

   public abstract String[] getDefaultCipherSuites();

   private static String a() {
      String var0 = (String)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return Security.getProperty("ssl.ServerSocketFactory.provider");
         }
      });
      if (var0 == null) {
         var0 = "com.sun.net.ssl.internal.ssl.SSLServerSocketFactoryImpl";
      }

      return var0;
   }

   public abstract String[] getSupportedCipherSuites();
}
