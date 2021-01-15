/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public class StringUtils
/*    */ {
/*  7 */   private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String ticksToElapsedTime(int ticks)
/*    */   {
/* 14 */     int i = ticks / 20;
/* 15 */     int j = i / 60;
/* 16 */     i %= 60;
/* 17 */     return j + ":" + i;
/*    */   }
/*    */   
/*    */   public static String stripControlCodes(String p_76338_0_)
/*    */   {
/* 22 */     return patternControlCode.matcher(p_76338_0_).replaceAll("");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isNullOrEmpty(String string)
/*    */   {
/* 30 */     return org.apache.commons.lang3.StringUtils.isEmpty(string);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\StringUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */