/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ public class I18n
/*    */ {
/*    */   private static Locale i18nLocale;
/*    */   
/*    */   static void setLocale(Locale i18nLocaleIn)
/*    */   {
/*  9 */     i18nLocale = i18nLocaleIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String format(String translateKey, Object... parameters)
/*    */   {
/* 17 */     return i18nLocale.formatMessage(translateKey, parameters);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\I18n.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */