/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.util.concurrent.ExecutorServiceFactory;
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
/*    */ 
/*    */ public class DefaultEventLoopGroup
/*    */   extends MultithreadEventLoopGroup
/*    */ {
/*    */   public DefaultEventLoopGroup()
/*    */   {
/* 34 */     this(0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DefaultEventLoopGroup(int nEventLoops)
/*    */   {
/* 45 */     this(nEventLoops, (Executor)null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DefaultEventLoopGroup(int nEventLoops, Executor executor)
/*    */   {
/* 57 */     super(nEventLoops, executor, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DefaultEventLoopGroup(int nEventLoops, ExecutorServiceFactory executorServiceFactory)
/*    */   {
/* 70 */     super(nEventLoops, executorServiceFactory, new Object[0]);
/*    */   }
/*    */   
/*    */   protected EventLoop newChild(Executor executor, Object... args) throws Exception
/*    */   {
/* 75 */     return new DefaultEventLoop(this, executor);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\DefaultEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */