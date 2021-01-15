/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.util.concurrent.SingleThreadEventExecutor;
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
/*    */ public abstract class SingleThreadEventLoop
/*    */   extends SingleThreadEventExecutor
/*    */   implements EventLoop
/*    */ {
/* 30 */   private final ChannelHandlerInvoker invoker = new DefaultChannelHandlerInvoker(this);
/*    */   
/*    */   protected SingleThreadEventLoop(EventLoopGroup parent, Executor executor, boolean addTaskWakesUp) {
/* 33 */     super(parent, executor, addTaskWakesUp);
/*    */   }
/*    */   
/*    */   public EventLoopGroup parent()
/*    */   {
/* 38 */     return (EventLoopGroup)super.parent();
/*    */   }
/*    */   
/*    */   public EventLoop next()
/*    */   {
/* 43 */     return (EventLoop)super.next();
/*    */   }
/*    */   
/*    */   public ChannelHandlerInvoker asInvoker()
/*    */   {
/* 48 */     return this.invoker;
/*    */   }
/*    */   
/*    */   public ChannelFuture register(Channel channel)
/*    */   {
/* 53 */     return register(channel, new DefaultChannelPromise(channel, this));
/*    */   }
/*    */   
/*    */   public ChannelFuture register(Channel channel, ChannelPromise promise)
/*    */   {
/* 58 */     if (channel == null) {
/* 59 */       throw new NullPointerException("channel");
/*    */     }
/* 61 */     if (promise == null) {
/* 62 */       throw new NullPointerException("promise");
/*    */     }
/*    */     
/* 65 */     channel.unsafe().register(this, promise);
/* 66 */     return promise;
/*    */   }
/*    */   
/*    */   protected boolean wakesUpForTask(Runnable task)
/*    */   {
/* 71 */     return !(task instanceof NonWakeupRunnable);
/*    */   }
/*    */   
/*    */   public EventLoop unwrap()
/*    */   {
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   static abstract interface NonWakeupRunnable
/*    */     extends Runnable
/*    */   {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\SingleThreadEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */