package rip.autumn.utils;

public final class StringUtils {
   public static String formatEnum(String string) {
      String[] split = string.replace('_', ' ').split(" ");
      StringBuilder sb = new StringBuilder();
      if (split.length == 0) {
         return "";
      } else {
         String[] var3 = split;
         int var4 = split.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String str = var3[var5];
            sb.append(str.toUpperCase(), 0, 1).append(str.toLowerCase().substring(1)).append(" ");
         }

         return sb.toString().trim();
      }
   }
}
