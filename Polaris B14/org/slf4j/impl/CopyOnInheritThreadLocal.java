/*    */ package org.slf4j.impl;
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
/*    */ public class CopyOnInheritThreadLocal
/*    */   extends InheritableThreadLocal<HashMap<String, String>>
/*    */ {
/*    */   protected HashMap<String, String> childValue(HashMap<String, String> parentValue)
/*    */   {
/* 20 */     if (parentValue == null) {
/* 21 */       return null;
/*    */     }
/* 23 */     HashMap<String, String> hm = new HashMap(parentValue);
/* 24 */     return hm;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\impl\CopyOnInheritThreadLocal.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */