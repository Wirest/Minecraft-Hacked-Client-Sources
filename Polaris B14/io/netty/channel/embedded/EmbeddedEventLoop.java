/*     */ package io.netty.channel.embedded;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelHandlerInvoker;
/*     */ import io.netty.channel.ChannelHandlerInvokerUtil;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.util.concurrent.AbstractScheduledEventExecutor;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class EmbeddedEventLoop
/*     */   extends AbstractScheduledEventExecutor
/*     */   implements ChannelHandlerInvoker, EventLoop
/*     */ {
/*  39 */   private final Queue<Runnable> tasks = new ArrayDeque(2);
/*     */   
/*     */   public EventLoop unwrap()
/*     */   {
/*  43 */     return this;
/*     */   }
/*     */   
/*     */   public EventLoopGroup parent()
/*     */   {
/*  48 */     return (EventLoopGroup)super.parent();
/*     */   }
/*     */   
/*     */   public EventLoop next()
/*     */   {
/*  53 */     return (EventLoop)super.next();
/*     */   }
/*     */   
/*     */   public void execute(Runnable command)
/*     */   {
/*  58 */     if (command == null) {
/*  59 */       throw new NullPointerException("command");
/*     */     }
/*  61 */     this.tasks.add(command);
/*     */   }
/*     */   
/*     */   void runTasks() {
/*     */     for (;;) {
/*  66 */       Runnable task = (Runnable)this.tasks.poll();
/*  67 */       if (task == null) {
/*     */         break;
/*     */       }
/*     */       
/*  71 */       task.run();
/*     */     }
/*     */   }
/*     */   
/*     */   long runScheduledTasks() {
/*  76 */     long time = AbstractScheduledEventExecutor.nanoTime();
/*     */     for (;;) {
/*  78 */       Runnable task = pollScheduledTask(time);
/*  79 */       if (task == null) {
/*  80 */         return nextScheduledTaskNano();
/*     */       }
/*     */       
/*  83 */       task.run();
/*     */     }
/*     */   }
/*     */   
/*     */   long nextScheduledTask() {
/*  88 */     return nextScheduledTaskNano();
/*     */   }
/*     */   
/*     */   protected void cancelScheduledTasks()
/*     */   {
/*  93 */     super.cancelScheduledTasks();
/*     */   }
/*     */   
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
/*     */   {
/*  98 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Future<?> terminationFuture()
/*     */   {
/* 103 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void shutdown()
/*     */   {
/* 109 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isShuttingDown()
/*     */   {
/* 114 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isShutdown()
/*     */   {
/* 119 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isTerminated()
/*     */   {
/* 124 */     return false;
/*     */   }
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit)
/*     */   {
/* 129 */     return false;
/*     */   }
/*     */   
/*     */   public ChannelFuture register(Channel channel)
/*     */   {
/* 134 */     return register(channel, new DefaultChannelPromise(channel, this));
/*     */   }
/*     */   
/*     */   public ChannelFuture register(Channel channel, ChannelPromise promise)
/*     */   {
/* 139 */     channel.unsafe().register(this, promise);
/* 140 */     return promise;
/*     */   }
/*     */   
/*     */   public boolean inEventLoop()
/*     */   {
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   public boolean inEventLoop(Thread thread)
/*     */   {
/* 150 */     return true;
/*     */   }
/*     */   
/*     */   public ChannelHandlerInvoker asInvoker()
/*     */   {
/* 155 */     return this;
/*     */   }
/*     */   
/*     */   public EventExecutor executor()
/*     */   {
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public void invokeChannelRegistered(ChannelHandlerContext ctx)
/*     */   {
/* 165 */     ChannelHandlerInvokerUtil.invokeChannelRegisteredNow(ctx);
/*     */   }
/*     */   
/*     */   public void invokeChannelUnregistered(ChannelHandlerContext ctx)
/*     */   {
/* 170 */     ChannelHandlerInvokerUtil.invokeChannelUnregisteredNow(ctx);
/*     */   }
/*     */   
/*     */   public void invokeChannelActive(ChannelHandlerContext ctx)
/*     */   {
/* 175 */     ChannelHandlerInvokerUtil.invokeChannelActiveNow(ctx);
/*     */   }
/*     */   
/*     */   public void invokeChannelInactive(ChannelHandlerContext ctx)
/*     */   {
/* 180 */     ChannelHandlerInvokerUtil.invokeChannelInactiveNow(ctx);
/*     */   }
/*     */   
/*     */   public void invokeExceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/*     */   {
/* 185 */     ChannelHandlerInvokerUtil.invokeExceptionCaughtNow(ctx, cause);
/*     */   }
/*     */   
/*     */   public void invokeUserEventTriggered(ChannelHandlerContext ctx, Object event)
/*     */   {
/* 190 */     ChannelHandlerInvokerUtil.invokeUserEventTriggeredNow(ctx, event);
/*     */   }
/*     */   
/*     */   public void invokeChannelRead(ChannelHandlerContext ctx, Object msg)
/*     */   {
/* 195 */     ChannelHandlerInvokerUtil.invokeChannelReadNow(ctx, msg);
/*     */   }
/*     */   
/*     */   public void invokeChannelReadComplete(ChannelHandlerContext ctx)
/*     */   {
/* 200 */     ChannelHandlerInvokerUtil.invokeChannelReadCompleteNow(ctx);
/*     */   }
/*     */   
/*     */   public void invokeChannelWritabilityChanged(ChannelHandlerContext ctx)
/*     */   {
/* 205 */     ChannelHandlerInvokerUtil.invokeChannelWritabilityChangedNow(ctx);
/*     */   }
/*     */   
/*     */   public void invokeBind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise)
/*     */   {
/* 210 */     ChannelHandlerInvokerUtil.invokeBindNow(ctx, localAddress, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void invokeConnect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */   {
/* 217 */     ChannelHandlerInvokerUtil.invokeConnectNow(ctx, remoteAddress, localAddress, promise);
/*     */   }
/*     */   
/*     */   public void invokeDisconnect(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */   {
/* 222 */     ChannelHandlerInvokerUtil.invokeDisconnectNow(ctx, promise);
/*     */   }
/*     */   
/*     */   public void invokeClose(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */   {
/* 227 */     ChannelHandlerInvokerUtil.invokeCloseNow(ctx, promise);
/*     */   }
/*     */   
/*     */   public void invokeDeregister(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */   {
/* 232 */     ChannelHandlerInvokerUtil.invokeDeregisterNow(ctx, promise);
/*     */   }
/*     */   
/*     */   public void invokeRead(ChannelHandlerContext ctx)
/*     */   {
/* 237 */     ChannelHandlerInvokerUtil.invokeReadNow(ctx);
/*     */   }
/*     */   
/*     */   public void invokeWrite(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*     */   {
/* 242 */     ChannelHandlerInvokerUtil.invokeWriteNow(ctx, msg, promise);
/*     */   }
/*     */   
/*     */   public void invokeFlush(ChannelHandlerContext ctx)
/*     */   {
/* 247 */     ChannelHandlerInvokerUtil.invokeFlushNow(ctx);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\embedded\EmbeddedEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */