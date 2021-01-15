/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public abstract class MultithreadEventExecutorGroup
/*     */   extends AbstractEventExecutorGroup
/*     */ {
/*     */   private final EventExecutor[] children;
/*     */   private final Set<EventExecutor> readonlyChildren;
/*  34 */   private final AtomicInteger childIndex = new AtomicInteger();
/*  35 */   private final AtomicInteger terminatedChildren = new AtomicInteger();
/*  36 */   private final Promise<?> terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final EventExecutorChooser chooser;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MultithreadEventExecutorGroup(int nEventExecutors, ExecutorServiceFactory executorServiceFactory, Object... args)
/*     */   {
/*  52 */     this(nEventExecutors, executorServiceFactory != null ? executorServiceFactory.newExecutorService(nEventExecutors) : null, true, args);
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
/*     */   protected MultithreadEventExecutorGroup(int nEventExecutors, Executor executor, Object... args)
/*     */   {
/*  68 */     this(nEventExecutors, executor, false, args);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private MultithreadEventExecutorGroup(int nEventExecutors, Executor executor, boolean shutdownExecutor, Object... args)
/*     */   {
/*  75 */     if (nEventExecutors <= 0) {
/*  76 */       throw new IllegalArgumentException(String.format("nEventExecutors: %d (expected: > 0)", new Object[] { Integer.valueOf(nEventExecutors) }));
/*     */     }
/*     */     
/*     */ 
/*  80 */     if (executor == null) {
/*  81 */       executor = newDefaultExecutorService(nEventExecutors);
/*  82 */       shutdownExecutor = true;
/*     */     }
/*     */     
/*  85 */     this.children = new EventExecutor[nEventExecutors];
/*  86 */     if (isPowerOfTwo(this.children.length)) {
/*  87 */       this.chooser = new PowerOfTwoEventExecutorChooser(null);
/*     */     } else {
/*  89 */       this.chooser = new GenericEventExecutorChooser(null);
/*     */     }
/*     */     
/*  92 */     for (int i = 0; i < nEventExecutors; i++) {
/*  93 */       boolean success = false;
/*     */       try {
/*  95 */         this.children[i] = newChild(executor, args);
/*  96 */         success = true; } catch (Exception e) { int j;
/*     */         int j;
/*     */         EventExecutor e;
/*  99 */         throw new IllegalStateException("failed to create a child event loop", e);
/*     */       } finally {
/* 101 */         if (!success) {
/* 102 */           for (int j = 0; j < i; j++) {
/* 103 */             this.children[j].shutdownGracefully();
/*     */           }
/*     */           
/* 106 */           for (int j = 0; j < i; j++) {
/* 107 */             EventExecutor e = this.children[j];
/*     */             try {
/* 109 */               while (!e.isTerminated()) {
/* 110 */                 e.awaitTermination(2147483647L, TimeUnit.SECONDS);
/*     */               }
/*     */             }
/*     */             catch (InterruptedException interrupted) {
/* 114 */               Thread.currentThread().interrupt();
/* 115 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 122 */     final boolean shutdownExecutor0 = shutdownExecutor;
/* 123 */     final Executor executor0 = executor;
/* 124 */     FutureListener<Object> terminationListener = new FutureListener()
/*     */     {
/*     */       public void operationComplete(Future<Object> future) throws Exception {
/* 127 */         if (MultithreadEventExecutorGroup.this.terminatedChildren.incrementAndGet() == MultithreadEventExecutorGroup.this.children.length) {
/* 128 */           MultithreadEventExecutorGroup.this.terminationFuture.setSuccess(null);
/* 129 */           if (shutdownExecutor0)
/*     */           {
/*     */ 
/* 132 */             ((ExecutorService)executor0).shutdown();
/*     */           }
/*     */         }
/*     */       }
/*     */     };
/*     */     
/* 138 */     for (EventExecutor e : this.children) {
/* 139 */       e.terminationFuture().addListener(terminationListener);
/*     */     }
/*     */     
/* 142 */     Set<EventExecutor> childrenSet = new LinkedHashSet(this.children.length);
/* 143 */     Collections.addAll(childrenSet, this.children);
/* 144 */     this.readonlyChildren = Collections.unmodifiableSet(childrenSet);
/*     */   }
/*     */   
/*     */   protected ExecutorService newDefaultExecutorService(int nEventExecutors) {
/* 148 */     return new DefaultExecutorServiceFactory(getClass()).newExecutorService(nEventExecutors);
/*     */   }
/*     */   
/*     */   public EventExecutor next()
/*     */   {
/* 153 */     return this.chooser.next();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int executorCount()
/*     */   {
/* 161 */     return this.children.length;
/*     */   }
/*     */   
/*     */ 
/*     */   public final <E extends EventExecutor> Set<E> children()
/*     */   {
/* 167 */     return this.readonlyChildren;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract EventExecutor newChild(Executor paramExecutor, Object... paramVarArgs)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
/*     */   {
/* 179 */     for (EventExecutor l : this.children) {
/* 180 */       l.shutdownGracefully(quietPeriod, timeout, unit);
/*     */     }
/* 182 */     return terminationFuture();
/*     */   }
/*     */   
/*     */   public Future<?> terminationFuture()
/*     */   {
/* 187 */     return this.terminationFuture;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void shutdown()
/*     */   {
/* 193 */     for (EventExecutor l : this.children) {
/* 194 */       l.shutdown();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isShuttingDown()
/*     */   {
/* 200 */     for (EventExecutor l : this.children) {
/* 201 */       if (!l.isShuttingDown()) {
/* 202 */         return false;
/*     */       }
/*     */     }
/* 205 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isShutdown()
/*     */   {
/* 210 */     for (EventExecutor l : this.children) {
/* 211 */       if (!l.isShutdown()) {
/* 212 */         return false;
/*     */       }
/*     */     }
/* 215 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isTerminated()
/*     */   {
/* 220 */     for (EventExecutor l : this.children) {
/* 221 */       if (!l.isTerminated()) {
/* 222 */         return false;
/*     */       }
/*     */     }
/* 225 */     return true;
/*     */   }
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit)
/*     */     throws InterruptedException
/*     */   {
/* 231 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 232 */     for (EventExecutor l : this.children) {
/*     */       for (;;) {
/* 234 */         long timeLeft = deadline - System.nanoTime();
/* 235 */         if (timeLeft <= 0L) {
/*     */           break label84;
/*     */         }
/* 238 */         if (l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS))
/*     */           break;
/*     */       }
/*     */     }
/*     */     label84:
/* 243 */     return isTerminated();
/*     */   }
/*     */   
/*     */   private static boolean isPowerOfTwo(int val) {
/* 247 */     return (val & -val) == val;
/*     */   }
/*     */   
/*     */   private static abstract interface EventExecutorChooser {
/*     */     public abstract EventExecutor next();
/*     */   }
/*     */   
/*     */   private final class PowerOfTwoEventExecutorChooser implements MultithreadEventExecutorGroup.EventExecutorChooser {
/*     */     private PowerOfTwoEventExecutorChooser() {}
/*     */     
/* 257 */     public EventExecutor next() { return MultithreadEventExecutorGroup.this.children[(MultithreadEventExecutorGroup.this.childIndex.getAndIncrement() & MultithreadEventExecutorGroup.this.children.length - 1)]; }
/*     */   }
/*     */   
/*     */   private final class GenericEventExecutorChooser implements MultithreadEventExecutorGroup.EventExecutorChooser {
/*     */     private GenericEventExecutorChooser() {}
/*     */     
/*     */     public EventExecutor next() {
/* 264 */       return MultithreadEventExecutorGroup.this.children[Math.abs(MultithreadEventExecutorGroup.this.childIndex.getAndIncrement() % MultithreadEventExecutorGroup.this.children.length)];
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\MultithreadEventExecutorGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */