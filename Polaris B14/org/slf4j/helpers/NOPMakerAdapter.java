/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.slf4j.spi.MDCAdapter;
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
/*    */ public class NOPMakerAdapter
/*    */   implements MDCAdapter
/*    */ {
/*    */   public void clear() {}
/*    */   
/*    */   public String get(String key)
/*    */   {
/* 22 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public void put(String key, String val) {}
/*    */   
/*    */   public void remove(String key) {}
/*    */   
/*    */   public Map getCopyOfContextMap()
/*    */   {
/* 32 */     return null;
/*    */   }
/*    */   
/*    */   public void setContextMap(Map contextMap) {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\helpers\NOPMakerAdapter.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */