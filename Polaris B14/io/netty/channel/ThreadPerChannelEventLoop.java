/*    */ package io.netty.channel;
/*    */ 
/*    */ import java.util.Queue;
/*    */ import java.util.Set;
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
/*    */ public class ThreadPerChannelEventLoop
/*    */   extends SingleThreadEventLoop
/*    */ {
/*    */   private final ThreadPerChannelEventLoopGroup parent;
/*    */   private Channel ch;
/*    */   
/*    */   public ThreadPerChannelEventLoop(ThreadPerChannelEventLoopGroup parent)
/*    */   {
/* 29 */     super(parent, parent.executor, true);
/* 30 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public ChannelFuture register(Channel channel, ChannelPromise promise)
/*    */   {
/* 35 */     super.register(channel, promise).addListener(new ChannelFutureListener()
/*    */     {
/*    */       public void operationComplete(ChannelFuture future) throws Exception {
/* 38 */         if (future.isSuccess()) {
/* 39 */           ThreadPerChannelEventLoop.this.ch = future.channel();
/*    */         } else {
/* 41 */           ThreadPerChannelEventLoop.this.deregister();
/*    */         }
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */   protected void run()
/*    */   {
/*    */     for (;;) {
/* 50 */       Runnable task = takeTask();
/* 51 */       if (task != null) {
/* 52 */         task.run();
/* 53 */         updateLastExecutionTime();
/*    */       }
/*    */       
/* 56 */       Channel ch = this.ch;
/* 57 */       if (isShuttingDown()) {
/* 58 */         if (ch != null) {
/* 59 */           ch.unsafe().close(ch.unsafe().voidPromise());
/*    */         }
/* 61 */         if (confirmShutdown()) {
/* 62 */           cleanupAndTerminate(true);
/*    */         }
/*    */         
/*    */       }
/* 66 */       else if (ch != null)
/*    */       {
/* 68 */         if (!ch.isRegistered()) {
/* 69 */           runAllTasks();
/* 70 */           deregister();
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   protected void deregister()
/*    */   {
/* 78 */     this.ch = null;
/* 79 */     this.parent.activeChildren.remove(this);
/* 80 */     this.parent.idleChildren.add(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ThreadPerChannelEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */