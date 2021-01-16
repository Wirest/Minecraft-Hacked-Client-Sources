package javax.security.cert;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.Principal;
import java.security.Security;
import java.util.Date;

public abstract class X509Certificate extends Certificate {
   private static final String X509_PROVIDER = "cert.provider.x509v1";
   private static String X509Provider = null;
   // $FF: synthetic field
   static Class class$java$io$InputStream;

   static {
      try {
         X509Provider = Security.getProperty("cert.provider.x509v1");
      } catch (SecurityException var0) {
         ;
      }

   }

   public abstract void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException;

   public abstract void checkValidity(Date var1) throws CertificateExpiredException, CertificateNotYetValidException;

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   private static final X509Certificate getInst(Object var0) throws CertificateException {
      String var1 = X509Provider;
      if (var1 == null) {
         var1 = "com.sun.security.cert.internal.x509.X509V1CertImpl";
      }

      try {
         Class[] var2 = null;
         if (var0 instanceof InputStream) {
            var2 = new Class[]{class$java$io$InputStream != null ? class$java$io$InputStream : (class$java$io$InputStream = class$("java.io.InputStream"))};
         } else {
            if (!(var0 instanceof byte[])) {
               throw new CertificateException("Unsupported argument type");
            }

            var2 = new Class[]{var0.getClass()};
         }

         Class var3 = Class.forName(var1);
         Constructor var4 = var3.getConstructor(var2);
         Object var5 = var4.newInstance(var0);
         return (X509Certificate)var5;
      } catch (ClassNotFoundException var6) {
         throw new CertificateException("Could not find class: " + var6);
      } catch (IllegalAccessException var7) {
         throw new CertificateException("Could not access class: " + var7);
      } catch (InstantiationException var8) {
         throw new CertificateException("Problems instantiating: " + var8);
      } catch (InvocationTargetException var9) {
         throw new CertificateException("InvocationTargetException: " + var9.getTargetException());
      } catch (NoSuchMethodException var10) {
         throw new CertificateException("Could not find class method: " + var10.getMessage());
      }
   }

   public static final X509Certificate getInstance(InputStream var0) throws CertificateException {
      return getInst(var0);
   }

   public static final X509Certificate getInstance(byte[] var0) throws CertificateException {
      return getInst(var0);
   }

   public abstract Principal getIssuerDN();

   public abstract Date getNotAfter();

   public abstract Date getNotBefore();

   public abstract BigInteger getSerialNumber();

   public abstract String getSigAlgName();

   public abstract String getSigAlgOID();

   public abstract byte[] getSigAlgParams();

   public abstract Principal getSubjectDN();

   public abstract int getVersion();
}
