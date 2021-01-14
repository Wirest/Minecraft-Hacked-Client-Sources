package rip.autumn.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MathUtils {
   public static double round(double num, double increment) {
      if (increment < 0.0D) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal bd = new BigDecimal(num);
         bd = bd.setScale((int)increment, RoundingMode.HALF_UP);
         return bd.doubleValue();
      }
   }

   public static double randomNumber(double max, double min) {
      return Math.random() * (max - min) + min;
   }
}
