/*     */ package io.netty.util.concurrent;
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
/*     */ public class DefaultProgressivePromise<V>
/*     */   extends DefaultPromise<V>
/*     */   implements ProgressivePromise<V>
/*     */ {
/*     */   public DefaultProgressivePromise(EventExecutor executor)
/*     */   {
/*  30 */     super(executor);
/*     */   }
/*     */   
/*     */   protected DefaultProgressivePromise() {}
/*     */   
/*     */   public ProgressivePromise<V> setProgress(long progress, long total)
/*     */   {
/*  37 */     if (total < 0L)
/*     */     {
/*  39 */       total = -1L;
/*  40 */       if (progress < 0L) {
/*  41 */         throw new IllegalArgumentException("progress: " + progress + " (expected: >= 0)");
/*     */       }
/*  43 */     } else if ((progress < 0L) || (progress > total)) {
/*  44 */       throw new IllegalArgumentException("progress: " + progress + " (expected: 0 <= progress <= total (" + total + "))");
/*     */     }
/*     */     
/*     */ 
/*  48 */     if (isDone()) {
/*  49 */       throw new IllegalStateException("complete already");
/*     */     }
/*     */     
/*  52 */     notifyProgressiveListeners(progress, total);
/*  53 */     return this;
/*     */   }
/*     */   
/*     */   public boolean tryProgress(long progress, long total)
/*     */   {
/*  58 */     if (total < 0L) {
/*  59 */       total = -1L;
/*  60 */       if ((progress < 0L) || (isDone())) {
/*  61 */         return false;
/*     */       }
/*  63 */     } else if ((progress < 0L) || (progress > total) || (isDone())) {
/*  64 */       return false;
/*     */     }
/*     */     
/*  67 */     notifyProgressiveListeners(progress, total);
/*  68 */     return true;
/*     */   }
/*     */   
/*     */   public ProgressivePromise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener)
/*     */   {
/*  73 */     super.addListener(listener);
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public ProgressivePromise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners)
/*     */   {
/*  79 */     super.addListeners(listeners);
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public ProgressivePromise<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener)
/*     */   {
/*  85 */     super.removeListener(listener);
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public ProgressivePromise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners)
/*     */   {
/*  91 */     super.removeListeners(listeners);
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public ProgressivePromise<V> sync() throws InterruptedException
/*     */   {
/*  97 */     super.sync();
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public ProgressivePromise<V> syncUninterruptibly()
/*     */   {
/* 103 */     super.syncUninterruptibly();
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public ProgressivePromise<V> await() throws InterruptedException
/*     */   {
/* 109 */     super.await();
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public ProgressivePromise<V> awaitUninterruptibly()
/*     */   {
/* 115 */     super.awaitUninterruptibly();
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public ProgressivePromise<V> setSuccess(V result)
/*     */   {
/* 121 */     super.setSuccess(result);
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public ProgressivePromise<V> setFailure(Throwable cause)
/*     */   {
/* 127 */     super.setFailure(cause);
/* 128 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\DefaultProgressivePromise.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */