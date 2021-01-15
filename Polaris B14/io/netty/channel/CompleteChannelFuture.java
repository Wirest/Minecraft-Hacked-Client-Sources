/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.concurrent.CompleteFuture;
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
/*     */ abstract class CompleteChannelFuture
/*     */   extends CompleteFuture<Void>
/*     */   implements ChannelFuture
/*     */ {
/*     */   private final Channel channel;
/*     */   
/*     */   protected CompleteChannelFuture(Channel channel, EventExecutor executor)
/*     */   {
/*  37 */     super(executor);
/*  38 */     if (channel == null) {
/*  39 */       throw new NullPointerException("channel");
/*     */     }
/*  41 */     this.channel = channel;
/*     */   }
/*     */   
/*     */   protected EventExecutor executor()
/*     */   {
/*  46 */     EventExecutor e = super.executor();
/*  47 */     if (e == null) {
/*  48 */       return channel().eventLoop();
/*     */     }
/*  50 */     return e;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> listener)
/*     */   {
/*  56 */     super.addListener(listener);
/*  57 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelFuture addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners)
/*     */   {
/*  62 */     super.addListeners(listeners);
/*  63 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelFuture removeListener(GenericFutureListener<? extends Future<? super Void>> listener)
/*     */   {
/*  68 */     super.removeListener(listener);
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelFuture removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners)
/*     */   {
/*  74 */     super.removeListeners(listeners);
/*  75 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelFuture syncUninterruptibly()
/*     */   {
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelFuture sync() throws InterruptedException
/*     */   {
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelFuture await() throws InterruptedException
/*     */   {
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelFuture awaitUninterruptibly()
/*     */   {
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public Channel channel()
/*     */   {
/* 100 */     return this.channel;
/*     */   }
/*     */   
/*     */   public Void getNow()
/*     */   {
/* 105 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isVoid()
/*     */   {
/* 110 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\CompleteChannelFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */