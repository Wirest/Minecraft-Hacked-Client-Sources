/*    */ package ch.qos.logback.classic.spi;
/*    */ 
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
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
/*    */ public class LoggerContextAwareBase
/*    */   extends ContextAwareBase
/*    */   implements LoggerContextAware
/*    */ {
/*    */   public void setLoggerContext(LoggerContext context)
/*    */   {
/* 28 */     super.setContext(context);
/*    */   }
/*    */   
/*    */ 
/*    */   public void setContext(Context context)
/*    */   {
/* 34 */     if (((context instanceof LoggerContext)) || (context == null)) {
/* 35 */       super.setContext(context);
/*    */     } else {
/* 37 */       throw new IllegalArgumentException("LoggerContextAwareBase only accepts contexts of type c.l.classic.LoggerContext");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LoggerContext getLoggerContext()
/*    */   {
/* 47 */     return (LoggerContext)this.context;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\LoggerContextAwareBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */