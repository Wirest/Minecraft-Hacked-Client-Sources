package org.m0jang.crystal.Utils;

public class StringUtils {
   public static String UpperFirst(String input) {
      return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
   }
}
