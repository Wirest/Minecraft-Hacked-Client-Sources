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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultEventExecutorGroup
/*    */   extends MultithreadEventExecutorGroup
/*    */ {
/*    */   public DefaultEventExecutorGroup(int nEventExecutors)
/*    */   {
/* 32 */     this(nEventExecutors, (Executor)null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DefaultEventExecutorGroup(int nEventExecutors, Executor executor)
/*    */   {
/* 43 */     super(nEventExecutors, executor, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DefaultEventExecutorGroup(int nEventExecutors, ExecutorServiceFactory executorServiceFactory)
/*    */   {
/* 54 */     super(nEventExecutors, executorServiceFactory, new Object[0]);
/*    */   }
/*    */   
/*    */   protected EventExecutor newChild(Executor executor, Object... args) throws Exception
/*    */   {
/* 59 */     return new DefaultEventExecutor(this, executor);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\DefaultEventExecutorGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */