/*    */ package ch.qos.logback.classic.pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClassNameOnlyAbbreviator
/*    */   implements Abbreviator
/*    */ {
/*    */   public String abbreviate(String fqClassName)
/*    */   {
/* 30 */     int lastIndex = fqClassName.lastIndexOf('.');
/* 31 */     if (lastIndex != -1) {
/* 32 */       return fqClassName.substring(lastIndex + 1, fqClassName.length());
/*    */     }
/* 34 */     return fqClassName;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\pattern\ClassNameOnlyAbbreviator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */