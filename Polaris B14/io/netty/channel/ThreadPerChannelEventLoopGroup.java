/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.concurrent.AbstractEventExecutorGroup;
/*     */ import io.netty.util.concurrent.DefaultPromise;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.concurrent.GlobalEventExecutor;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.concurrent.ThreadPerTaskExecutor;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.Collections;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ThreadFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadPerChannelEventLoopGroup
/*     */   extends AbstractEventExecutorGroup
/*     */   implements EventLoopGroup
/*     */ {
/*     */   private final Object[] childArgs;
/*     */   private final int maxChannels;
/*     */   final Executor executor;
/*  48 */   final Set<EventLoop> activeChildren = Collections.newSetFromMap(PlatformDependent.newConcurrentHashMap());
/*     */   
/*  50 */   private final Set<EventLoop> readOnlyActiveChildren = Collections.unmodifiableSet(this.activeChildren);
/*  51 */   final Queue<EventLoop> idleChildren = new ConcurrentLinkedQueue();
/*     */   
/*     */   private final ChannelException tooManyChannels;
/*     */   private volatile boolean shuttingDown;
/*  55 */   private final Promise<?> terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
/*  56 */   private final FutureListener<Object> childTerminationListener = new FutureListener()
/*     */   {
/*     */     public void operationComplete(Future<Object> future) throws Exception
/*     */     {
/*  60 */       if (ThreadPerChannelEventLoopGroup.this.isTerminated()) {
/*  61 */         ThreadPerChannelEventLoopGroup.this.terminationFuture.trySuccess(null);
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */   protected ThreadPerChannelEventLoopGroup()
/*     */   {
/*  70 */     this(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ThreadPerChannelEventLoopGroup(int maxChannels)
/*     */   {
/*  83 */     this(maxChannels, Executors.defaultThreadFactory(), new Object[0]);
/*     */   }
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
/*     */   protected ThreadPerChannelEventLoopGroup(int maxChannels, ThreadFactory threadFactory, Object... args)
/*     */   {
/*  99 */     this(maxChannels, new ThreadPerTaskExecutor(threadFactory), args);
/*     */   }
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
/*     */   protected ThreadPerChannelEventLoopGroup(int maxChannels, Executor executor, Object... args)
/*     */   {
/* 115 */     if (maxChannels < 0) {
/* 116 */       throw new IllegalArgumentException(String.format("maxChannels: %d (expected: >= 0)", new Object[] { Integer.valueOf(maxChannels) }));
/*     */     }
/*     */     
/* 119 */     if (executor == null) {
/* 120 */       throw new NullPointerException("executor");
/*     */     }
/*     */     
/* 123 */     if (args == null) {
/* 124 */       this.childArgs = EmptyArrays.EMPTY_OBJECTS;
/*     */     } else {
/* 126 */       this.childArgs = ((Object[])args.clone());
/*     */     }
/*     */     
/* 129 */     this.maxChannels = maxChannels;
/* 130 */     this.executor = executor;
/*     */     
/* 132 */     this.tooManyChannels = new ChannelException("too many channels (max: " + maxChannels + ')');
/* 133 */     this.tooManyChannels.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/*     */   }
/*     */   
/*     */ 
/*     */   protected EventLoop newChild(Object... args)
/*     */     throws Exception
/*     */   {
/* 140 */     return new ThreadPerChannelEventLoop(this);
/*     */   }
/*     */   
/*     */ 
/*     */   public <E extends EventExecutor> Set<E> children()
/*     */   {
/* 146 */     return this.readOnlyActiveChildren;
/*     */   }
/*     */   
/*     */   public EventLoop next()
/*     */   {
/* 151 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
/*     */   {
/* 156 */     this.shuttingDown = true;
/*     */     
/* 158 */     for (EventLoop l : this.activeChildren) {
/* 159 */       l.shutdownGracefully(quietPeriod, timeout, unit);
/*     */     }
/* 161 */     for (EventLoop l : this.idleChildren) {
/* 162 */       l.shutdownGracefully(quietPeriod, timeout, unit);
/*     */     }
/*     */     
/*     */ 
/* 166 */     if (isTerminated()) {
/* 167 */       this.terminationFuture.trySuccess(null);
/*     */     }
/*     */     
/* 170 */     return terminationFuture();
/*     */   }
/*     */   
/*     */   public Future<?> terminationFuture()
/*     */   {
/* 175 */     return this.terminationFuture;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void shutdown()
/*     */   {
/* 181 */     this.shuttingDown = true;
/*     */     
/* 183 */     for (EventLoop l : this.activeChildren) {
/* 184 */       l.shutdown();
/*     */     }
/* 186 */     for (EventLoop l : this.idleChildren) {
/* 187 */       l.shutdown();
/*     */     }
/*     */     
/*     */ 
/* 191 */     if (isTerminated()) {
/* 192 */       this.terminationFuture.trySuccess(null);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isShuttingDown()
/*     */   {
/* 198 */     for (EventLoop l : this.activeChildren) {
/* 199 */       if (!l.isShuttingDown()) {
/* 200 */         return false;
/*     */       }
/*     */     }
/* 203 */     for (EventLoop l : this.idleChildren) {
/* 204 */       if (!l.isShuttingDown()) {
/* 205 */         return false;
/*     */       }
/*     */     }
/* 208 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isShutdown()
/*     */   {
/* 213 */     for (EventLoop l : this.activeChildren) {
/* 214 */       if (!l.isShutdown()) {
/* 215 */         return false;
/*     */       }
/*     */     }
/* 218 */     for (EventLoop l : this.idleChildren) {
/* 219 */       if (!l.isShutdown()) {
/* 220 */         return false;
/*     */       }
/*     */     }
/* 223 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isTerminated()
/*     */   {
/* 228 */     for (EventLoop l : this.activeChildren) {
/* 229 */       if (!l.isTerminated()) {
/* 230 */         return false;
/*     */       }
/*     */     }
/* 233 */     for (EventLoop l : this.idleChildren) {
/* 234 */       if (!l.isTerminated()) {
/* 235 */         return false;
/*     */       }
/*     */     }
/* 238 */     return true;
/*     */   }
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit)
/*     */     throws InterruptedException
/*     */   {
/* 244 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 245 */     for (EventLoop l : this.activeChildren) {
/*     */       for (;;) {
/* 247 */         long timeLeft = deadline - System.nanoTime();
/* 248 */         if (timeLeft <= 0L) {
/* 249 */           return isTerminated();
/*     */         }
/* 251 */         if (l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS)) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 256 */     for (EventLoop l : this.idleChildren) {
/*     */       for (;;) {
/* 258 */         long timeLeft = deadline - System.nanoTime();
/* 259 */         if (timeLeft <= 0L) {
/* 260 */           return isTerminated();
/*     */         }
/* 262 */         if (l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS)) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 267 */     return isTerminated();
/*     */   }
/*     */   
/*     */   public ChannelFuture register(Channel channel)
/*     */   {
/* 272 */     if (channel == null) {
/* 273 */       throw new NullPointerException("channel");
/*     */     }
/*     */     try {
/* 276 */       EventLoop l = nextChild();
/* 277 */       return l.register(channel, new DefaultChannelPromise(channel, l));
/*     */     } catch (Throwable t) {
/* 279 */       return new FailedChannelFuture(channel, GlobalEventExecutor.INSTANCE, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public ChannelFuture register(Channel channel, ChannelPromise promise)
/*     */   {
/* 285 */     if (channel == null) {
/* 286 */       throw new NullPointerException("channel");
/*     */     }
/*     */     try {
/* 289 */       return nextChild().register(channel, promise);
/*     */     } catch (Throwable t) {
/* 291 */       promise.setFailure(t); }
/* 292 */     return promise;
/*     */   }
/*     */   
/*     */   private EventLoop nextChild() throws Exception
/*     */   {
/* 297 */     if (this.shuttingDown) {
/* 298 */       throw new RejectedExecutionException("shutting down");
/*     */     }
/*     */     
/* 301 */     EventLoop loop = (EventLoop)this.idleChildren.poll();
/* 302 */     if (loop == null) {
/* 303 */       if ((this.maxChannels > 0) && (this.activeChildren.size() >= this.maxChannels)) {
/* 304 */         throw this.tooManyChannels;
/*     */       }
/* 306 */       loop = newChild(this.childArgs);
/* 307 */       loop.terminationFuture().addListener(this.childTerminationListener);
/*     */     }
/* 309 */     this.activeChildren.add(loop);
/* 310 */     return loop;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ThreadPerChannelEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */