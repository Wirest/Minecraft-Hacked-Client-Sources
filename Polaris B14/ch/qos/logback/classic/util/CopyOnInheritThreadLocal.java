/*    */ package ch.qos.logback.classic.util;
/*    */ 
/*    */ import java.util.HashMap;
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
/*    */ 
/*    */ public class CopyOnInheritThreadLocal
/*    */   extends InheritableThreadLocal<HashMap<String, String>>
/*    */ {
/*    */   protected HashMap<String, String> childValue(HashMap<String, String> parentValue)
/*    */   {
/* 33 */     if (parentValue == null) {
/* 34 */       return null;
/*    */     }
/* 36 */     return new HashMap(parentValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\util\CopyOnInheritThreadLocal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */