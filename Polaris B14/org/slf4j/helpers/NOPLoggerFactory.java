/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.Logger;
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
/*    */ public class NOPLoggerFactory
/*    */   implements ILoggerFactory
/*    */ {
/*    */   public Logger getLogger(String name)
/*    */   {
/* 46 */     return NOPLogger.NOP_LOGGER;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\helpers\NOPLoggerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */