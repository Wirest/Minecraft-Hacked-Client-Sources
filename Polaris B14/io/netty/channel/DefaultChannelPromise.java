/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.concurrent.DefaultPromise;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultChannelPromise
/*     */   extends DefaultPromise<Void>
/*     */   implements ChannelPromise, ChannelFlushPromiseNotifier.FlushCheckpoint
/*     */ {
/*     */   private final Channel channel;
/*     */   private long checkpoint;
/*     */   
/*     */   public DefaultChannelPromise(Channel channel)
/*     */   {
/*  40 */     this.channel = channel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultChannelPromise(Channel channel, EventExecutor executor)
/*     */   {
/*  50 */     super(executor);
/*  51 */     this.channel = channel;
/*     */   }
/*     */   
/*     */   protected EventExecutor executor()
/*     */   {
/*  56 */     EventExecutor e = super.executor();
/*  57 */     if (e == null) {
/*  58 */       return channel().eventLoop();
/*     */     }
/*  60 */     return e;
/*     */   }
/*     */   
/*     */ 
/*     */   public Channel channel()
/*     */   {
/*  66 */     return this.channel;
/*     */   }
/*     */   
/*     */   public ChannelPromise setSuccess()
/*     */   {
/*  71 */     return setSuccess(null);
/*     */   }
/*     */   
/*     */   public ChannelPromise setSuccess(Void result)
/*     */   {
/*  76 */     super.setSuccess(result);
/*  77 */     return this;
/*     */   }
/*     */   
/*     */   public boolean trySuccess()
/*     */   {
/*  82 */     return trySuccess(null);
/*     */   }
/*     */   
/*     */   public ChannelPromise setFailure(Throwable cause)
/*     */   {
/*  87 */     super.setFailure(cause);
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelPromise addListener(GenericFutureListener<? extends Future<? super Void>> listener)
/*     */   {
/*  93 */     super.addListener(listener);
/*  94 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelPromise addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners)
/*     */   {
/*  99 */     super.addListeners(listeners);
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelPromise removeListener(GenericFutureListener<? extends Future<? super Void>> listener)
/*     */   {
/* 105 */     super.removeListener(listener);
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelPromise removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners)
/*     */   {
/* 111 */     super.removeListeners(listeners);
/* 112 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelPromise sync() throws InterruptedException
/*     */   {
/* 117 */     super.sync();
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelPromise syncUninterruptibly()
/*     */   {
/* 123 */     super.syncUninterruptibly();
/* 124 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelPromise await() throws InterruptedException
/*     */   {
/* 129 */     super.await();
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelPromise awaitUninterruptibly()
/*     */   {
/* 135 */     super.awaitUninterruptibly();
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public long flushCheckpoint()
/*     */   {
/* 141 */     return this.checkpoint;
/*     */   }
/*     */   
/*     */   public void flushCheckpoint(long checkpoint)
/*     */   {
/* 146 */     this.checkpoint = checkpoint;
/*     */   }
/*     */   
/*     */   public ChannelPromise promise()
/*     */   {
/* 151 */     return this;
/*     */   }
/*     */   
/*     */   protected void checkDeadLock()
/*     */   {
/* 156 */     if (channel().isRegistered()) {
/* 157 */       super.checkDeadLock();
/*     */     }
/*     */   }
/*     */   
/*     */   public ChannelPromise unvoid()
/*     */   {
/* 163 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isVoid()
/*     */   {
/* 168 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\DefaultChannelPromise.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */