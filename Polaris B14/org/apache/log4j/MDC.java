/*    */ package org.apache.log4j;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import java.util.Map;
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
/*    */ public class MDC
/*    */ {
/*    */   public static void put(String key, String value)
/*    */   {
/* 25 */     org.slf4j.MDC.put(key, value);
/*    */   }
/*    */   
/*    */   public static void put(String key, Object value) {
/* 29 */     if (value != null) {
/* 30 */       put(key, value.toString());
/*    */     } else {
/* 32 */       put(key, null);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Object get(String key) {
/* 37 */     return org.slf4j.MDC.get(key);
/*    */   }
/*    */   
/*    */   public static void remove(String key) {
/* 41 */     org.slf4j.MDC.remove(key);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void clear() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public static Hashtable getContext()
/*    */   {
/* 56 */     Map map = org.slf4j.MDC.getCopyOfContextMap();
/*    */     
/* 58 */     if (map != null) {
/* 59 */       return new Hashtable(map);
/*    */     }
/* 61 */     return new Hashtable();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\apache\log4j\MDC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */