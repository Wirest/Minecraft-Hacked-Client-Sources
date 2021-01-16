package org.m0jang.crystal.Utils;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang3.Validate;

public enum ChatColor {
   BLACK('0', 0),
   DARK_BLUE('1', 1),
   DARK_GREEN('2', 2),
   DARK_AQUA('3', 3),
   DARK_RED('4', 4),
   DARK_PURPLE('5', 5),
   GOLD('6', 6),
   GRAY('7', 7),
   DARK_GRAY('8', 8),
   BLUE('9', 9),
   GREEN('a', 10),
   AQUA('b', 11),
   RED('c', 12),
   LIGHT_PURPLE('d', 13),
   YELLOW('e', 14),
   WHITE('f', 15),
   MAGIC('k', 16, true),
   BOLD('l', 17, true),
   STRIKETHROUGH('m', 18, true),
   UNDERLINE('n', 19, true),
   ITALIC('o', 20, true),
   RESET('r', 21);

   public static final char COLOR_CHAR = '\247';
   private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('\247') + "[0-9A-FK-OR]");
   private final int intCode;
   private final char code;
   private final boolean isFormat;
   private final String toString;
   private static final Map BY_ID = Maps.newHashMap();
   private static final Map BY_CHAR = Maps.newHashMap();

   static {
      ChatColor[] var3;
      int var2 = (var3 = values()).length;

      for(int var1 = 0; var1 < var2; ++var1) {
         ChatColor color = var3[var1];
         BY_ID.put(color.intCode, color);
         BY_CHAR.put(color.code, color);
      }

   }

   private ChatColor(char code, int intCode) {
      this(code, intCode, false);
   }

   private ChatColor(char code, int intCode, boolean isFormat) {
      this.code = code;
      this.intCode = intCode;
      this.isFormat = isFormat;
      this.toString = new String(new char[]{'\247', code});
   }

   public char getChar() {
      return this.code;
   }

   public String toString() {
      return this.toString;
   }

   public boolean isFormat() {
      return this.isFormat;
   }

   public boolean isColor() {
      return !this.isFormat && this != RESET;
   }

   public static ChatColor getByChar(char code) {
      return (ChatColor)BY_CHAR.get(code);
   }

   public static ChatColor getByChar(String code) {
      Validate.notNull(code, "Code cannot be null");
      Validate.isTrue(code.length() > 0, "Code must have at least one char");
      return (ChatColor)BY_CHAR.get(code.charAt(0));
   }

   public static String stripColor(String input) {
      return input == null ? null : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
   }

   public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
      char[] b = textToTranslate.toCharArray();

      for(int i = 0; i < b.length - 1; ++i) {
         if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
            b[i] = '\247';
            b[i + 1] = Character.toLowerCase(b[i + 1]);
         }
      }

      return new String(b);
   }

   public static String getLastColors(String input) {
      String result = "";
      int length = input.length();

      for(int index = length - 1; index > -1; --index) {
         char section = input.charAt(index);
         ChatColor color;
         if (section == '\247' && index < length - 1 && (color = getByChar(input.charAt(index + 1))) != null) {
            result = color.toString() + result;
            if (color.isColor() || color.equals(RESET)) {
               break;
            }
         }
      }

      return result;
   }
}
