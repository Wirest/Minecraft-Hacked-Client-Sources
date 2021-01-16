package javax.security.cert;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;

public abstract class Certificate {
   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Certificate)) {
         return false;
      } else {
         try {
            byte[] var2 = this.getEncoded();
            byte[] var3 = ((Certificate)var1).getEncoded();
            if (var2.length != var3.length) {
               return false;
            } else {
               for(int var4 = 0; var4 < var2.length; ++var4) {
                  if (var2[var4] != var3[var4]) {
                     return false;
                  }
               }

               return true;
            }
         } catch (CertificateException var5) {
            return false;
         }
      }
   }

   public abstract byte[] getEncoded() throws CertificateEncodingException;

   public abstract PublicKey getPublicKey();

   public int hashCode() {
      int var1 = 0;

      try {
         byte[] var2 = this.getEncoded();

         for(int var3 = 1; var3 < var2.length; ++var3) {
            var1 += var2[var3] * var3;
         }

         return var1;
      } catch (CertificateException var4) {
         return var1;
      }
   }

   public abstract String toString();

   public abstract void verify(PublicKey var1) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException;

   public abstract void verify(PublicKey var1, String var2) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException;
}
