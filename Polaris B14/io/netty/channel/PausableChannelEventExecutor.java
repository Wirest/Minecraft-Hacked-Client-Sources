/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.EventExecutorGroup;
/*     */ import io.netty.util.concurrent.PausableEventExecutor;
/*     */ import io.netty.util.concurrent.ProgressivePromise;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.concurrent.ScheduledFuture;
/*     */ import io.netty.util.internal.CallableEventExecutorAdapter;
/*     */ import io.netty.util.internal.RunnableEventExecutorAdapter;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ 
/*     */ 
/*     */ abstract class PausableChannelEventExecutor
/*     */   implements PausableEventExecutor, ChannelHandlerInvoker
/*     */ {
/*     */   abstract Channel channel();
/*     */   
/*     */   abstract ChannelHandlerInvoker unwrapInvoker();
/*     */   
/*     */   public void invokeFlush(ChannelHandlerContext ctx)
/*     */   {
/*  46 */     unwrapInvoker().invokeFlush(ctx);
/*     */   }
/*     */   
/*     */   public EventExecutor executor()
/*     */   {
/*  51 */     return this;
/*     */   }
/*     */   
/*     */   public void invokeChannelRegistered(ChannelHandlerContext ctx)
/*     */   {
/*  56 */     unwrapInvoker().invokeChannelRegistered(ctx);
/*     */   }
/*     */   
/*     */   public void invokeChannelUnregistered(ChannelHandlerContext ctx)
/*     */   {
/*  61 */     unwrapInvoker().invokeChannelUnregistered(ctx);
/*     */   }
/*     */   
/*     */   public void invokeChannelActive(ChannelHandlerContext ctx)
/*     */   {
/*  66 */     unwrapInvoker().invokeChannelActive(ctx);
/*     */   }
/*     */   
/*     */   public void invokeChannelInactive(ChannelHandlerContext ctx)
/*     */   {
/*  71 */     unwrapInvoker().invokeChannelInactive(ctx);
/*     */   }
/*     */   
/*     */   public void invokeExceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/*     */   {
/*  76 */     unwrapInvoker().invokeExceptionCaught(ctx, cause);
/*     */   }
/*     */   
/*     */   public void invokeUserEventTriggered(ChannelHandlerContext ctx, Object event)
/*     */   {
/*  81 */     unwrapInvoker().invokeUserEventTriggered(ctx, event);
/*     */   }
/*     */   
/*     */   public void invokeChannelRead(ChannelHandlerContext ctx, Object msg)
/*     */   {
/*  86 */     unwrapInvoker().invokeChannelRead(ctx, msg);
/*     */   }
/*     */   
/*     */   public void invokeChannelReadComplete(ChannelHandlerContext ctx)
/*     */   {
/*  91 */     unwrapInvoker().invokeChannelReadComplete(ctx);
/*     */   }
/*     */   
/*     */   public void invokeChannelWritabilityChanged(ChannelHandlerContext ctx)
/*     */   {
/*  96 */     unwrapInvoker().invokeChannelWritabilityChanged(ctx);
/*     */   }
/*     */   
/*     */   public void invokeBind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise)
/*     */   {
/* 101 */     unwrapInvoker().invokeBind(ctx, localAddress, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public void invokeConnect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */   {
/* 107 */     unwrapInvoker().invokeConnect(ctx, remoteAddress, localAddress, promise);
/*     */   }
/*     */   
/*     */   public void invokeDisconnect(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */   {
/* 112 */     unwrapInvoker().invokeDisconnect(ctx, promise);
/*     */   }
/*     */   
/*     */   public void invokeClose(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */   {
/* 117 */     unwrapInvoker().invokeClose(ctx, promise);
/*     */   }
/*     */   
/*     */   public void invokeDeregister(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */   {
/* 122 */     unwrapInvoker().invokeDeregister(ctx, promise);
/*     */   }
/*     */   
/*     */   public void invokeRead(ChannelHandlerContext ctx)
/*     */   {
/* 127 */     unwrapInvoker().invokeRead(ctx);
/*     */   }
/*     */   
/*     */   public void invokeWrite(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*     */   {
/* 132 */     unwrapInvoker().invokeWrite(ctx, msg, promise);
/*     */   }
/*     */   
/*     */   public EventExecutor next()
/*     */   {
/* 137 */     return unwrap().next();
/*     */   }
/*     */   
/*     */   public <E extends EventExecutor> Set<E> children()
/*     */   {
/* 142 */     return unwrap().children();
/*     */   }
/*     */   
/*     */   public EventExecutorGroup parent()
/*     */   {
/* 147 */     return unwrap().parent();
/*     */   }
/*     */   
/*     */   public boolean inEventLoop()
/*     */   {
/* 152 */     return unwrap().inEventLoop();
/*     */   }
/*     */   
/*     */   public boolean inEventLoop(Thread thread)
/*     */   {
/* 157 */     return unwrap().inEventLoop(thread);
/*     */   }
/*     */   
/*     */   public <V> Promise<V> newPromise()
/*     */   {
/* 162 */     return unwrap().newPromise();
/*     */   }
/*     */   
/*     */   public <V> ProgressivePromise<V> newProgressivePromise()
/*     */   {
/* 167 */     return unwrap().newProgressivePromise();
/*     */   }
/*     */   
/*     */   public <V> io.netty.util.concurrent.Future<V> newSucceededFuture(V result)
/*     */   {
/* 172 */     return unwrap().newSucceededFuture(result);
/*     */   }
/*     */   
/*     */   public <V> io.netty.util.concurrent.Future<V> newFailedFuture(Throwable cause)
/*     */   {
/* 177 */     return unwrap().newFailedFuture(cause);
/*     */   }
/*     */   
/*     */   public boolean isShuttingDown()
/*     */   {
/* 182 */     return unwrap().isShuttingDown();
/*     */   }
/*     */   
/*     */   public io.netty.util.concurrent.Future<?> shutdownGracefully()
/*     */   {
/* 187 */     return unwrap().shutdownGracefully();
/*     */   }
/*     */   
/*     */   public io.netty.util.concurrent.Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
/*     */   {
/* 192 */     return unwrap().shutdownGracefully(quietPeriod, timeout, unit);
/*     */   }
/*     */   
/*     */   public io.netty.util.concurrent.Future<?> terminationFuture()
/*     */   {
/* 197 */     return unwrap().terminationFuture();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void shutdown()
/*     */   {
/* 203 */     unwrap().shutdown();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public List<Runnable> shutdownNow()
/*     */   {
/* 209 */     return unwrap().shutdownNow();
/*     */   }
/*     */   
/*     */   public io.netty.util.concurrent.Future<?> submit(Runnable task)
/*     */   {
/* 214 */     if (!isAcceptingNewTasks()) {
/* 215 */       throw new RejectedExecutionException();
/*     */     }
/* 217 */     return unwrap().submit(task);
/*     */   }
/*     */   
/*     */   public <T> io.netty.util.concurrent.Future<T> submit(Runnable task, T result)
/*     */   {
/* 222 */     if (!isAcceptingNewTasks()) {
/* 223 */       throw new RejectedExecutionException();
/*     */     }
/* 225 */     return unwrap().submit(task, result);
/*     */   }
/*     */   
/*     */   public <T> io.netty.util.concurrent.Future<T> submit(Callable<T> task)
/*     */   {
/* 230 */     if (!isAcceptingNewTasks()) {
/* 231 */       throw new RejectedExecutionException();
/*     */     }
/* 233 */     return unwrap().submit(task);
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
/*     */   {
/* 238 */     if (!isAcceptingNewTasks()) {
/* 239 */       throw new RejectedExecutionException();
/*     */     }
/*     */     
/* 242 */     return unwrap().schedule(new ChannelRunnableEventExecutor(channel(), command), delay, unit);
/*     */   }
/*     */   
/*     */   public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit)
/*     */   {
/* 247 */     if (!isAcceptingNewTasks()) {
/* 248 */       throw new RejectedExecutionException();
/*     */     }
/* 250 */     return unwrap().schedule(new ChannelCallableEventExecutor(channel(), callable), delay, unit);
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
/*     */   {
/* 255 */     if (!isAcceptingNewTasks()) {
/* 256 */       throw new RejectedExecutionException();
/*     */     }
/* 258 */     return unwrap().scheduleAtFixedRate(new ChannelRunnableEventExecutor(channel(), command), initialDelay, period, unit);
/*     */   }
/*     */   
/*     */ 
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
/*     */   {
/* 264 */     if (!isAcceptingNewTasks()) {
/* 265 */       throw new RejectedExecutionException();
/*     */     }
/* 267 */     return unwrap().scheduleWithFixedDelay(new ChannelRunnableEventExecutor(channel(), command), initialDelay, delay, unit);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isShutdown()
/*     */   {
/* 273 */     return unwrap().isShutdown();
/*     */   }
/*     */   
/*     */   public boolean isTerminated()
/*     */   {
/* 278 */     return unwrap().isTerminated();
/*     */   }
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
/*     */   {
/* 283 */     return unwrap().awaitTermination(timeout, unit);
/*     */   }
/*     */   
/*     */   public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
/*     */     throws InterruptedException
/*     */   {
/* 289 */     if (!isAcceptingNewTasks()) {
/* 290 */       throw new RejectedExecutionException();
/*     */     }
/* 292 */     return unwrap().invokeAll(tasks);
/*     */   }
/*     */   
/*     */   public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
/*     */     throws InterruptedException
/*     */   {
/* 298 */     if (!isAcceptingNewTasks()) {
/* 299 */       throw new RejectedExecutionException();
/*     */     }
/* 301 */     return unwrap().invokeAll(tasks, timeout, unit);
/*     */   }
/*     */   
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
/*     */   {
/* 306 */     if (!isAcceptingNewTasks()) {
/* 307 */       throw new RejectedExecutionException();
/*     */     }
/* 309 */     return (T)unwrap().invokeAny(tasks);
/*     */   }
/*     */   
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
/*     */     throws InterruptedException, ExecutionException, TimeoutException
/*     */   {
/* 315 */     if (!isAcceptingNewTasks()) {
/* 316 */       throw new RejectedExecutionException();
/*     */     }
/* 318 */     return (T)unwrap().invokeAny(tasks, timeout, unit);
/*     */   }
/*     */   
/*     */   public void execute(Runnable command)
/*     */   {
/* 323 */     if (!isAcceptingNewTasks()) {
/* 324 */       throw new RejectedExecutionException();
/*     */     }
/* 326 */     unwrap().execute(command);
/*     */   }
/*     */   
/*     */   public void close() throws Exception
/*     */   {
/* 331 */     unwrap().close();
/*     */   }
/*     */   
/*     */   private static final class ChannelCallableEventExecutor<V> implements CallableEventExecutorAdapter<V>
/*     */   {
/*     */     final Channel channel;
/*     */     final Callable<V> callable;
/*     */     
/*     */     ChannelCallableEventExecutor(Channel channel, Callable<V> callable) {
/* 340 */       this.channel = channel;
/* 341 */       this.callable = callable;
/*     */     }
/*     */     
/*     */     public EventExecutor executor()
/*     */     {
/* 346 */       return this.channel.eventLoop();
/*     */     }
/*     */     
/*     */     public Callable unwrap()
/*     */     {
/* 351 */       return this.callable;
/*     */     }
/*     */     
/*     */     public V call() throws Exception
/*     */     {
/* 356 */       return (V)this.callable.call();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ChannelRunnableEventExecutor implements RunnableEventExecutorAdapter
/*     */   {
/*     */     final Channel channel;
/*     */     final Runnable runnable;
/*     */     
/*     */     ChannelRunnableEventExecutor(Channel channel, Runnable runnable) {
/* 366 */       this.channel = channel;
/* 367 */       this.runnable = runnable;
/*     */     }
/*     */     
/*     */     public EventExecutor executor()
/*     */     {
/* 372 */       return this.channel.eventLoop();
/*     */     }
/*     */     
/*     */     public Runnable unwrap()
/*     */     {
/* 377 */       return this.runnable;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 382 */       this.runnable.run();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\PausableChannelEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */