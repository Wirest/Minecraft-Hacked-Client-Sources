/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.util.concurrent.DefaultExecutorServiceFactory;
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
/*    */ public class DefaultEventLoop
/*    */   extends SingleThreadEventLoop
/*    */ {
/*    */   public DefaultEventLoop()
/*    */   {
/* 24 */     this((EventLoopGroup)null);
/*    */   }
/*    */   
/*    */   public DefaultEventLoop(Executor executor) {
/* 28 */     this(null, executor);
/*    */   }
/*    */   
/*    */   public DefaultEventLoop(EventLoopGroup parent) {
/* 32 */     this(parent, new DefaultExecutorServiceFactory(DefaultEventLoop.class).newExecutorService(1));
/*    */   }
/*    */   
/*    */   public DefaultEventLoop(EventLoopGroup parent, Executor executor) {
/* 36 */     super(parent, executor, true);
/*    */   }
/*    */   
/*    */   protected void run()
/*    */   {
/* 41 */     Runnable task = takeTask();
/* 42 */     if (task != null) {
/* 43 */       task.run();
/* 44 */       updateLastExecutionTime();
/*    */     }
/*    */     
/* 47 */     if (confirmShutdown()) {
/* 48 */       cleanupAndTerminate(true);
/*    */     } else {
/* 50 */       scheduleExecution();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\DefaultEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */