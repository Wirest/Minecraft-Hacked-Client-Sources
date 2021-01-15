/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public enum EnumChatFormatting
/*     */ {
/*  12 */   BLACK("BLACK", '0', 0), 
/*  13 */   DARK_BLUE("DARK_BLUE", '1', 1), 
/*  14 */   DARK_GREEN("DARK_GREEN", '2', 2), 
/*  15 */   DARK_AQUA("DARK_AQUA", '3', 3), 
/*  16 */   DARK_RED("DARK_RED", '4', 4), 
/*  17 */   DARK_PURPLE("DARK_PURPLE", '5', 5), 
/*  18 */   GOLD("GOLD", '6', 6), 
/*  19 */   GRAY("GRAY", '7', 7), 
/*  20 */   DARK_GRAY("DARK_GRAY", '8', 8), 
/*  21 */   BLUE("BLUE", '9', 9), 
/*  22 */   GREEN("GREEN", 'a', 10), 
/*  23 */   AQUA("AQUA", 'b', 11), 
/*  24 */   RED("RED", 'c', 12), 
/*  25 */   LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13), 
/*  26 */   YELLOW("YELLOW", 'e', 14), 
/*  27 */   WHITE("WHITE", 'f', 15), 
/*  28 */   OBFUSCATED("OBFUSCATED", 'k', true), 
/*  29 */   BOLD("BOLD", 'l', true), 
/*  30 */   STRIKETHROUGH("STRIKETHROUGH", 'm', true), 
/*  31 */   UNDERLINE("UNDERLINE", 'n', true), 
/*  32 */   ITALIC("ITALIC", 'o', true), 
/*  33 */   RESET("RESET", 'r', -1);
/*     */   
/*     */ 
/*     */ 
/*     */   private static final Map<String, EnumChatFormatting> nameMapping;
/*     */   
/*     */ 
/*     */   private static final Pattern formattingCodePattern;
/*     */   
/*     */ 
/*     */   private final String name;
/*     */   
/*     */ 
/*     */   private final char formattingCode;
/*     */   
/*     */ 
/*     */   private final boolean fancyStyling;
/*     */   
/*     */ 
/*     */   private final String controlString;
/*     */   
/*     */ 
/*     */   private final int colorIndex;
/*     */   
/*     */ 
/*     */ 
/*     */   private static String func_175745_c(String p_175745_0_)
/*     */   {
/*  61 */     return p_175745_0_.toLowerCase().replaceAll("[^a-z]", "");
/*     */   }
/*     */   
/*     */   private EnumChatFormatting(String formattingName, char formattingCodeIn, int colorIndex)
/*     */   {
/*  66 */     this(formattingName, formattingCodeIn, false, colorIndex);
/*     */   }
/*     */   
/*     */   private EnumChatFormatting(String formattingName, char formattingCodeIn, boolean fancyStylingIn)
/*     */   {
/*  71 */     this(formattingName, formattingCodeIn, fancyStylingIn, -1);
/*     */   }
/*     */   
/*     */   private EnumChatFormatting(String formattingName, char formattingCodeIn, boolean fancyStylingIn, int colorIndex)
/*     */   {
/*  76 */     this.name = formattingName;
/*  77 */     this.formattingCode = formattingCodeIn;
/*  78 */     this.fancyStyling = fancyStylingIn;
/*  79 */     this.colorIndex = colorIndex;
/*  80 */     this.controlString = ("§" + formattingCodeIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getColorIndex()
/*     */   {
/*  88 */     return this.colorIndex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFancyStyling()
/*     */   {
/*  96 */     return this.fancyStyling;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isColor()
/*     */   {
/* 104 */     return (!this.fancyStyling) && (this != RESET);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFriendlyName()
/*     */   {
/* 112 */     return name().toLowerCase();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 117 */     return this.controlString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getTextWithoutFormattingCodes(String text)
/*     */   {
/* 125 */     return text == null ? null : formattingCodePattern.matcher(text).replaceAll("");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EnumChatFormatting getValueByName(String friendlyName)
/*     */   {
/* 133 */     return friendlyName == null ? null : (EnumChatFormatting)nameMapping.get(func_175745_c(friendlyName));
/*     */   }
/*     */   
/*     */   public static EnumChatFormatting func_175744_a(int p_175744_0_)
/*     */   {
/* 138 */     if (p_175744_0_ < 0)
/*     */     {
/* 140 */       return RESET;
/*     */     }
/*     */     
/*     */     EnumChatFormatting[] arrayOfEnumChatFormatting;
/* 144 */     int j = (arrayOfEnumChatFormatting = values()).length; for (int i = 0; i < j; i++) { EnumChatFormatting enumchatformatting = arrayOfEnumChatFormatting[i];
/*     */       
/* 146 */       if (enumchatformatting.getColorIndex() == p_175744_0_)
/*     */       {
/* 148 */         return enumchatformatting;
/*     */       }
/*     */     }
/*     */     
/* 152 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static Collection<String> getValidValues(boolean p_96296_0_, boolean p_96296_1_)
/*     */   {
/* 158 */     List<String> list = Lists.newArrayList();
/*     */     EnumChatFormatting[] arrayOfEnumChatFormatting;
/* 160 */     int j = (arrayOfEnumChatFormatting = values()).length; for (int i = 0; i < j; i++) { EnumChatFormatting enumchatformatting = arrayOfEnumChatFormatting[i];
/*     */       
/* 162 */       if (((!enumchatformatting.isColor()) || (p_96296_0_)) && ((!enumchatformatting.isFancyStyling()) || (p_96296_1_)))
/*     */       {
/* 164 */         list.add(enumchatformatting.getFriendlyName());
/*     */       }
/*     */     }
/*     */     
/* 168 */     return list;
/*     */   }
/*     */   
/*     */   static
/*     */   {
/*  35 */     nameMapping = Maps.newHashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  41 */     formattingCodePattern = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     EnumChatFormatting[] arrayOfEnumChatFormatting;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */     int j = (arrayOfEnumChatFormatting = values()).length; for (int i = 0; i < j; i++) { EnumChatFormatting enumchatformatting = arrayOfEnumChatFormatting[i];
/*     */       
/* 174 */       nameMapping.put(func_175745_c(enumchatformatting.name), enumchatformatting);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\EnumChatFormatting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */