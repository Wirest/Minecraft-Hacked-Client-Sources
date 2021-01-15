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
/*     */ public final class ImmediateEventExecutor
/*     */   extends AbstractEventExecutor
/*     */ {
/*  25 */   public static final ImmediateEventExecutor INSTANCE = new ImmediateEventExecutor();
/*     */   
/*  27 */   private final Future<?> terminationFuture = new FailedFuture(GlobalEventExecutor.INSTANCE, new UnsupportedOperationException());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean inEventLoop()
/*     */   {
/*  36 */     return true;
/*     */   }
/*     */   
/*     */   public boolean inEventLoop(Thread thread)
/*     */   {
/*  41 */     return true;
/*     */   }
/*     */   
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
/*     */   {
/*  46 */     return terminationFuture();
/*     */   }
/*     */   
/*     */   public Future<?> terminationFuture()
/*     */   {
/*  51 */     return this.terminationFuture;
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public void shutdown() {}
/*     */   
/*     */   public boolean isShuttingDown()
/*     */   {
/*  60 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isShutdown()
/*     */   {
/*  65 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isTerminated()
/*     */   {
/*  70 */     return false;
/*     */   }
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit)
/*     */   {
/*  75 */     return false;
/*     */   }
/*     */   
/*     */   public void execute(Runnable command)
/*     */   {
/*  80 */     if (command == null) {
/*  81 */       throw new NullPointerException("command");
/*     */     }
/*  83 */     command.run();
/*     */   }
/*     */   
/*     */   public <V> Promise<V> newPromise()
/*     */   {
/*  88 */     return new ImmediatePromise(this);
/*     */   }
/*     */   
/*     */   public <V> ProgressivePromise<V> newProgressivePromise()
/*     */   {
/*  93 */     return new ImmediateProgressivePromise(this);
/*     */   }
/*     */   
/*     */   static class ImmediatePromise<V> extends DefaultPromise<V> {
/*     */     ImmediatePromise(EventExecutor executor) {
/*  98 */       super();
/*     */     }
/*     */     
/*     */     protected void checkDeadLock() {}
/*     */   }
/*     */   
/*     */   static class ImmediateProgressivePromise<V>
/*     */     extends DefaultProgressivePromise<V>
/*     */   {
/*     */     ImmediateProgressivePromise(EventExecutor executor)
/*     */     {
/* 109 */       super();
/*     */     }
/*     */     
/*     */     protected void checkDeadLock() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\ImmediateEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */