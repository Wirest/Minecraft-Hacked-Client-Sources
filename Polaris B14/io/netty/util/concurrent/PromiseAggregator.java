/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PromiseAggregator<V, F extends Future<V>>
/*     */   implements GenericFutureListener<F>
/*     */ {
/*     */   private final Promise<?> aggregatePromise;
/*     */   private final boolean failPending;
/*     */   private Set<Promise<V>> pendingPromises;
/*     */   
/*     */   public PromiseAggregator(Promise<Void> aggregatePromise, boolean failPending)
/*     */   {
/*  43 */     if (aggregatePromise == null) {
/*  44 */       throw new NullPointerException("aggregatePromise");
/*     */     }
/*  46 */     this.aggregatePromise = aggregatePromise;
/*  47 */     this.failPending = failPending;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PromiseAggregator(Promise<Void> aggregatePromise)
/*     */   {
/*  55 */     this(aggregatePromise, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @SafeVarargs
/*     */   public final PromiseAggregator<V, F> add(Promise<V>... promises)
/*     */   {
/*  63 */     if (promises == null) {
/*  64 */       throw new NullPointerException("promises");
/*     */     }
/*  66 */     if (promises.length == 0) {
/*  67 */       return this;
/*     */     }
/*  69 */     synchronized (this) {
/*  70 */       if (this.pendingPromises == null) { int size;
/*     */         int size;
/*  72 */         if (promises.length > 1) {
/*  73 */           size = promises.length;
/*     */         } else {
/*  75 */           size = 2;
/*     */         }
/*  77 */         this.pendingPromises = new LinkedHashSet(size);
/*     */       }
/*  79 */       for (Promise<V> p : promises)
/*  80 */         if (p != null)
/*     */         {
/*     */ 
/*  83 */           this.pendingPromises.add(p);
/*  84 */           p.addListener(this);
/*     */         }
/*     */     }
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public synchronized void operationComplete(F future) throws Exception
/*     */   {
/*  92 */     if (this.pendingPromises == null) {
/*  93 */       this.aggregatePromise.setSuccess(null);
/*     */     } else {
/*  95 */       this.pendingPromises.remove(future);
/*  96 */       Throwable cause; if (!future.isSuccess()) {
/*  97 */         cause = future.cause();
/*  98 */         this.aggregatePromise.setFailure(cause);
/*  99 */         if (this.failPending) {
/* 100 */           for (Promise<V> pendingFuture : this.pendingPromises) {
/* 101 */             pendingFuture.setFailure(cause);
/*     */           }
/*     */         }
/*     */       }
/* 105 */       else if (this.pendingPromises.isEmpty()) {
/* 106 */         this.aggregatePromise.setSuccess(null);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\PromiseAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */