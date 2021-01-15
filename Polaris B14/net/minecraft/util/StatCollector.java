/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class StatCollector
/*    */ {
/*  5 */   private static StringTranslate localizedName = ;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 11 */   private static StringTranslate fallbackTranslator = new StringTranslate();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String translateToLocal(String key)
/*    */   {
/* 18 */     return localizedName.translateKey(key);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String translateToLocalFormatted(String key, Object... format)
/*    */   {
/* 26 */     return localizedName.translateKeyFormat(key, format);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String translateToFallback(String key)
/*    */   {
/* 35 */     return fallbackTranslator.translateKey(key);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean canTranslate(String key)
/*    */   {
/* 43 */     return localizedName.isKeyTranslated(key);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static long getLastTranslationUpdateTimeInMilliseconds()
/*    */   {
/* 51 */     return localizedName.getLastUpdateTimeInMilliseconds();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\StatCollector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */