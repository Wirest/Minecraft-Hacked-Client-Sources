/*     */ package io.netty.util.concurrent;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CompleteFuture<V>
/*     */   extends AbstractFuture<V>
/*     */ {
/*     */   private final EventExecutor executor;
/*     */   
/*     */   protected CompleteFuture(EventExecutor executor)
/*     */   {
/*  34 */     this.executor = executor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected EventExecutor executor()
/*     */   {
/*  41 */     return this.executor;
/*     */   }
/*     */   
/*     */   public Future<V> addListener(GenericFutureListener<? extends Future<? super V>> listener)
/*     */   {
/*  46 */     if (listener == null) {
/*  47 */       throw new NullPointerException("listener");
/*     */     }
/*  49 */     DefaultPromise.notifyListener(executor(), this, listener);
/*  50 */     return this;
/*     */   }
/*     */   
/*     */   public Future<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners)
/*     */   {
/*  55 */     if (listeners == null) {
/*  56 */       throw new NullPointerException("listeners");
/*     */     }
/*  58 */     for (GenericFutureListener<? extends Future<? super V>> l : listeners) {
/*  59 */       if (l == null) {
/*     */         break;
/*     */       }
/*  62 */       DefaultPromise.notifyListener(executor(), this, l);
/*     */     }
/*  64 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Future<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener)
/*     */   {
/*  70 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Future<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners)
/*     */   {
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public Future<V> await() throws InterruptedException
/*     */   {
/*  81 */     if (Thread.interrupted()) {
/*  82 */       throw new InterruptedException();
/*     */     }
/*  84 */     return this;
/*     */   }
/*     */   
/*     */   public boolean await(long timeout, TimeUnit unit) throws InterruptedException
/*     */   {
/*  89 */     if (Thread.interrupted()) {
/*  90 */       throw new InterruptedException();
/*     */     }
/*  92 */     return true;
/*     */   }
/*     */   
/*     */   public Future<V> sync() throws InterruptedException
/*     */   {
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public Future<V> syncUninterruptibly()
/*     */   {
/* 102 */     return this;
/*     */   }
/*     */   
/*     */   public boolean await(long timeoutMillis) throws InterruptedException
/*     */   {
/* 107 */     if (Thread.interrupted()) {
/* 108 */       throw new InterruptedException();
/*     */     }
/* 110 */     return true;
/*     */   }
/*     */   
/*     */   public Future<V> awaitUninterruptibly()
/*     */   {
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public boolean awaitUninterruptibly(long timeout, TimeUnit unit)
/*     */   {
/* 120 */     return true;
/*     */   }
/*     */   
/*     */   public boolean awaitUninterruptibly(long timeoutMillis)
/*     */   {
/* 125 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isDone()
/*     */   {
/* 130 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isCancellable()
/*     */   {
/* 135 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isCancelled()
/*     */   {
/* 140 */     return false;
/*     */   }
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning)
/*     */   {
/* 145 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\CompleteFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */