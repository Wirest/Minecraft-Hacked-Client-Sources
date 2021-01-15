package me.aristhena.utils;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Maps;

public enum ChatColor
{
  BLACK(
  
    '0', 0),  DARK_BLUE(
  
    '1', 1),  DARK_GREEN(
  
    '2', 2),  DARK_AQUA(
  
    '3', 3),  DARK_RED(
  
    '4', 4),  DARK_PURPLE(
  
    '5', 5),  GOLD(
  
    '6', 6),  GRAY(
  
    '7', 7),  DARK_GRAY(
  
    '8', 8),  BLUE(
  
    '9', 9),  GREEN(
  
    'a', 10),  AQUA(
  
    'b', 11),  RED(
  
    'c', 12),  LIGHT_PURPLE(
  
    'd', 13),  YELLOW(
  
    'e', 14),  WHITE(
  
    'f', 15),  MAGIC(
  
    'k', 16, true),  BOLD(
  
    'l', 17, true),  STRIKETHROUGH(
  
    'm', 18, true),  UNDERLINE(
  
    'n', 19, true),  ITALIC(
  
    'o', 20, true),  RESET(
  
    'r', 21);
  
  public static final char COLOR_CHAR = '§';
  private static final Pattern STRIP_COLOR_PATTERN;
  private final int intCode;
  private final char code;
  private final boolean isFormat;
  private final String toString;
  private static final Map<Integer, ChatColor> BY_ID;
  private static final Map<Character, ChatColor> BY_CHAR;
  
  private ChatColor(char code, int intCode)
  {
    this(code, intCode, false);
  }
  
  private ChatColor(char code, int intCode, boolean isFormat)
  {
    this.code = code;
    this.intCode = intCode;
    this.isFormat = isFormat;
    this.toString = new String(new char[] { '§', code });
  }
  
  public char getChar()
  {
    return this.code;
  }
  
  public String toString()
  {
    return this.toString;
  }
  
  public boolean isFormat()
  {
    return this.isFormat;
  }
  
  public boolean isColor()
  {
    return (!this.isFormat) && (this != RESET);
  }
  
  public static ChatColor getByChar(char code)
  {
    return (ChatColor)BY_CHAR.get(Character.valueOf(code));
  }
  
  public static ChatColor getByChar(String code)
  {
    Validate.notNull(code, "Code cannot be null", new Object[0]);
    Validate.isTrue(code.length() > 0, "Code must have at least one char", new Object[0]);
    return (ChatColor)BY_CHAR.get(Character.valueOf(code.charAt(0)));
  }
  
  public static String stripColor(String input)
  {
    if (input == null) {
      return null;
    }
    return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
  }
  
  public static String translateAlternateColorCodes(char altColorChar, String textToTranslate)
  {
    char[] b = textToTranslate.toCharArray();
    for (int i = 0; i < b.length - 1; i++) {
      if ((b[i] == altColorChar) && ("0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[(i + 1)]) > -1))
      {
        b[i] = '§';
        b[(i + 1)] = Character.toLowerCase(b[(i + 1)]);
      }
    }
    return new String(b);
  }
  
  public static String getLastColors(String input)
  {
    String result = "";
    int length = input.length();
    for (int index = length - 1; index > -1; index--)
    {
      char section = input.charAt(index);
      if ((section == '§') && (index < length - 1))
      {
        char c = input.charAt(index + 1);
        ChatColor color = getByChar(c);
        if (color != null)
        {
          result = color.toString() + result;
          if ((color.isColor()) || (color.equals(RESET))) {
            break;
          }
        }
      }
    }
    return result;
  }
  
  static
  {
    STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");
    
    BY_ID = Maps.newHashMap();
    BY_CHAR = Maps.newHashMap();
    ChatColor[] arrayOfChatColor;
    int j = (arrayOfChatColor = values()).length;
    for (int i = 0; i < j; i++)
    {
      ChatColor color = arrayOfChatColor[i];
      BY_ID.put(Integer.valueOf(color.intCode), color);
      BY_CHAR.put(Character.valueOf(color.code), color);
    }
  }
}

