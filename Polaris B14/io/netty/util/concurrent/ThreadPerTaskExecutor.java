/*    */ package io.netty.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.ThreadFactory;
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
/*    */ public final class ThreadPerTaskExecutor
/*    */   implements Executor
/*    */ {
/*    */   private final ThreadFactory threadFactory;
/*    */   
/*    */   public ThreadPerTaskExecutor(ThreadFactory threadFactory)
/*    */   {
/* 25 */     if (threadFactory == null) {
/* 26 */       throw new NullPointerException("threadFactory");
/*    */     }
/* 28 */     this.threadFactory = threadFactory;
/*    */   }
/*    */   
/*    */   public void execute(Runnable command)
/*    */   {
/* 33 */     this.threadFactory.newThread(command).start();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\ThreadPerTaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */