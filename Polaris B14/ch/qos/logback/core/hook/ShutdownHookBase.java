/*    */ package ch.qos.logback.core.hook;
/*    */ 
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.ContextBase;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ShutdownHookBase
/*    */   extends ContextAwareBase
/*    */   implements ShutdownHook
/*    */ {
/*    */   protected void stop()
/*    */   {
/* 34 */     addInfo("Logback context being closed via shutdown hook");
/*    */     
/* 36 */     Context hookContext = getContext();
/* 37 */     if ((hookContext instanceof ContextBase)) {
/* 38 */       ContextBase context = (ContextBase)hookContext;
/* 39 */       context.stop();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\hook\ShutdownHookBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */