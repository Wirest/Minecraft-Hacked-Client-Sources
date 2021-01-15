/*    */ package ch.qos.logback.classic.net;
/*    */ 
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
/*    */ import ch.qos.logback.core.spi.LifeCycle;
/*    */ import java.util.concurrent.ExecutorService;
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
/*    */ public abstract class ReceiverBase
/*    */   extends ContextAwareBase
/*    */   implements LifeCycle
/*    */ {
/*    */   private boolean started;
/*    */   
/*    */   public final void start()
/*    */   {
/* 34 */     if (isStarted()) return;
/* 35 */     if (getContext() == null) {
/* 36 */       throw new IllegalStateException("context not set");
/*    */     }
/* 38 */     if (shouldStart()) {
/* 39 */       getContext().getExecutorService().execute(getRunnableTask());
/* 40 */       this.started = true;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final void stop()
/*    */   {
/* 48 */     if (!isStarted()) return;
/*    */     try {
/* 50 */       onStop();
/*    */     }
/*    */     catch (RuntimeException ex) {
/* 53 */       addError("on stop: " + ex, ex);
/*    */     }
/* 55 */     this.started = false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final boolean isStarted()
/*    */   {
/* 62 */     return this.started;
/*    */   }
/*    */   
/*    */   protected abstract boolean shouldStart();
/*    */   
/*    */   protected abstract void onStop();
/*    */   
/*    */   protected abstract Runnable getRunnableTask();
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\ReceiverBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */