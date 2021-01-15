/*    */ package io.netty.handler.logging;
/*    */ 
/*    */ import io.netty.util.internal.logging.InternalLogLevel;
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
/*    */ public enum LogLevel
/*    */ {
/* 24 */   TRACE(InternalLogLevel.TRACE), 
/* 25 */   DEBUG(InternalLogLevel.DEBUG), 
/* 26 */   INFO(InternalLogLevel.INFO), 
/* 27 */   WARN(InternalLogLevel.WARN), 
/* 28 */   ERROR(InternalLogLevel.ERROR);
/*    */   
/*    */   private final InternalLogLevel internalLevel;
/*    */   
/*    */   private LogLevel(InternalLogLevel internalLevel) {
/* 33 */     this.internalLevel = internalLevel;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   InternalLogLevel toInternalLevel()
/*    */   {
/* 42 */     return this.internalLevel;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\logging\LogLevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */