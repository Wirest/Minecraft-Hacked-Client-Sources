package saint.utilities;

import java.util.Random;

public final class MathUtils {
   private static final Random rng = new Random();

   private MathUtils() {
   }

   public static double predictPos(double cur, double prev) {
      return cur + (cur - prev) * (double)(PingTimer.getTime() * 10.0F / 2.0F);
   }

   public static Random getRng() {
      return rng;
   }

   public static float getRandom() {
      return rng.nextFloat();
   }

   public static int getRandom(int cap) {
      return rng.nextInt(cap);
   }

   public static int getRandom(int floor, int cap) {
      return floor + rng.nextInt(cap - floor + 1);
   }

   public static float clampValue(float value, float floor, float cap) {
      if (value < floor) {
         return floor;
      } else {
         return value > cap ? cap : value;
      }
   }

   public static float getSimilarity(String string1, String string2) {
      int halflen = Math.min(string1.length(), string2.length()) / 2 + Math.min(string1.length(), string2.length()) % 2;
      StringBuffer common1 = getCommonCharacters(string1, string2, halflen);
      StringBuffer common2 = getCommonCharacters(string2, string1, halflen);
      if (common1.length() != 0 && common2.length() != 0) {
         if (common1.length() != common2.length()) {
            return 0.0F;
         } else {
            int transpositions = 0;
            int n = common1.length();

            for(int i = 0; i < n; ++i) {
               if (common1.charAt(i) != common2.charAt(i)) {
                  ++transpositions;
               }
            }

            transpositions = (int)((float)transpositions / 2.0F);
            return ((float)common1.length() / (float)string1.length() + (float)common2.length() / (float)string2.length() + (float)(common1.length() - transpositions) / (float)common1.length()) / 3.0F;
         }
      } else {
         return 0.0F;
      }
   }

   private static StringBuffer getCommonCharacters(String string1, String string2, int distanceSep) {
      StringBuffer returnCommons = new StringBuffer();
      StringBuffer copy = new StringBuffer(string2);
      int n = string1.length();
      int m = string2.length();

      for(int i = 0; i < n; ++i) {
         char ch = string1.charAt(i);
         boolean foundIt = false;

         for(int j = Math.max(0, i - distanceSep); !foundIt && j < Math.min(i + distanceSep, m - 1); ++j) {
            if (copy.charAt(j) == ch) {
               foundIt = true;
               returnCommons.append(ch);
               copy.setCharAt(j, '\u0000');
            }
         }
      }

      return returnCommons;
   }
}
