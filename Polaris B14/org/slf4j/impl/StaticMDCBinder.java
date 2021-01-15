/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import ch.qos.logback.classic.util.LogbackMDCAdapter;
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
/*    */ public class StaticMDCBinder
/*    */ {
/* 32 */   public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public MDCAdapter getMDCA()
/*    */   {
/* 42 */     return new LogbackMDCAdapter();
/*    */   }
/*    */   
/*    */   public String getMDCAdapterClassStr() {
/* 46 */     return LogbackMDCAdapter.class.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\impl\StaticMDCBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */