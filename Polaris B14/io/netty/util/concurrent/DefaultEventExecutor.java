/*    */ package io.netty.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.Executor;
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
/*    */ public final class DefaultEventExecutor
/*    */   extends SingleThreadEventExecutor
/*    */ {
/*    */   public DefaultEventExecutor()
/*    */   {
/* 27 */     this((EventExecutorGroup)null);
/*    */   }
/*    */   
/*    */   public DefaultEventExecutor(Executor executor) {
/* 31 */     this(null, executor);
/*    */   }
/*    */   
/*    */   public DefaultEventExecutor(EventExecutorGroup parent) {
/* 35 */     this(parent, new DefaultExecutorServiceFactory(DefaultEventExecutor.class).newExecutorService(1));
/*    */   }
/*    */   
/*    */   public DefaultEventExecutor(EventExecutorGroup parent, Executor executor) {
/* 39 */     super(parent, executor, true);
/*    */   }
/*    */   
/*    */   protected void run()
/*    */   {
/* 44 */     Runnable task = takeTask();
/* 45 */     if (task != null) {
/* 46 */       task.run();
/* 47 */       updateLastExecutionTime();
/*    */     }
/*    */     
/* 50 */     if (confirmShutdown()) {
/* 51 */       cleanupAndTerminate(true);
/*    */     } else {
/* 53 */       scheduleExecution();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\DefaultEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */