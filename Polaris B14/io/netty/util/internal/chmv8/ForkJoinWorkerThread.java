/*     */ package io.netty.util.internal.chmv8;
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
/*     */ public class ForkJoinWorkerThread
/*     */   extends Thread
/*     */ {
/*     */   final ForkJoinPool pool;
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
/*     */   final ForkJoinPool.WorkQueue workQueue;
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
/*     */   protected ForkJoinWorkerThread(ForkJoinPool pool)
/*     */   {
/*  66 */     super("aForkJoinWorkerThread");
/*  67 */     this.pool = pool;
/*  68 */     this.workQueue = pool.registerWorker(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ForkJoinPool getPool()
/*     */   {
/*  77 */     return this.pool;
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
/*     */   public int getPoolIndex()
/*     */   {
/*  91 */     return this.workQueue.poolIndex >>> 1;
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
/*     */   protected void onStart() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onTermination(Throwable exception) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/* 123 */     Throwable exception = null;
/*     */     try {
/* 125 */       onStart();
/* 126 */       this.pool.runWorker(this.workQueue);
/*     */     } catch (Throwable ex) {
/* 128 */       exception = ex;
/*     */     } finally {
/*     */       try {
/* 131 */         onTermination(exception);
/*     */       } catch (Throwable ex) {
/* 133 */         if (exception == null)
/* 134 */           exception = ex;
/*     */       } finally {
/* 136 */         this.pool.deregisterWorker(this, exception);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\chmv8\ForkJoinWorkerThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */