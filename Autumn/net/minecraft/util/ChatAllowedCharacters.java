package net.minecraft.util;

public class ChatAllowedCharacters {
   public static final char[] allowedCharactersArray = new char[]{'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':'};

   public static boolean isAllowedCharacter(char character) {
      return character != 167 && character >= ' ' && character != 127;
   }

   public static String filterAllowedCharacters(String input) {
      StringBuilder stringbuilder = new StringBuilder();
      char[] var2 = input.toCharArray();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char c0 = var2[var4];
         if (isAllowedCharacter(c0)) {
            stringbuilder.append(c0);
         }
      }

      return stringbuilder.toString();
   }
}
