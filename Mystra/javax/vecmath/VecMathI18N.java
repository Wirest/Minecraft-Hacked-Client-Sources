package javax.vecmath;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

class VecMathI18N {
   static String getString(String var0) {
      String var1;
      try {
         var1 = ResourceBundle.getBundle("javax.vecmath.ExceptionStrings").getString(var0);
      } catch (MissingResourceException var3) {
         System.err.println("VecMathI18N: Error looking up: " + var0);
         var1 = var0;
      }

      return var1;
   }
}
