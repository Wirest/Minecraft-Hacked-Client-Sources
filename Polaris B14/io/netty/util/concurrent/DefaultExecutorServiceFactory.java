/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.chmv8.ForkJoinPool;
/*     */ import io.netty.util.internal.chmv8.ForkJoinPool.ForkJoinWorkerThreadFactory;
/*     */ import io.netty.util.internal.chmv8.ForkJoinWorkerThread;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.ExecutorService;
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
/*     */ public final class DefaultExecutorServiceFactory
/*     */   implements ExecutorServiceFactory
/*     */ {
/*  49 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultExecutorServiceFactory.class);
/*     */   
/*     */ 
/*  52 */   private static final AtomicInteger executorId = new AtomicInteger();
/*     */   
/*     */ 
/*     */   private final String namePrefix;
/*     */   
/*     */ 
/*     */   public DefaultExecutorServiceFactory(Class<?> clazzNamePrefix)
/*     */   {
/*  60 */     this(toName(clazzNamePrefix));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DefaultExecutorServiceFactory(String namePrefix)
/*     */   {
/*  67 */     this.namePrefix = namePrefix;
/*     */   }
/*     */   
/*     */   public ExecutorService newExecutorService(int parallelism)
/*     */   {
/*  72 */     ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory = new DefaultForkJoinWorkerThreadFactory(this.namePrefix + '-' + executorId.getAndIncrement());
/*     */     
/*     */ 
/*  75 */     return new ForkJoinPool(parallelism, threadFactory, DefaultUncaughtExceptionHandler.INSTANCE, true);
/*     */   }
/*     */   
/*     */   private static String toName(Class<?> clazz) {
/*  79 */     if (clazz == null) {
/*  80 */       throw new NullPointerException("clazz");
/*     */     }
/*     */     
/*  83 */     String clazzName = StringUtil.simpleClassName(clazz);
/*  84 */     switch (clazzName.length()) {
/*     */     case 0: 
/*  86 */       return "unknown";
/*     */     case 1: 
/*  88 */       return clazzName.toLowerCase(Locale.US);
/*     */     }
/*  90 */     if ((Character.isUpperCase(clazzName.charAt(0))) && (Character.isLowerCase(clazzName.charAt(1)))) {
/*  91 */       return Character.toLowerCase(clazzName.charAt(0)) + clazzName.substring(1);
/*     */     }
/*  93 */     return clazzName;
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class DefaultUncaughtExceptionHandler
/*     */     implements Thread.UncaughtExceptionHandler
/*     */   {
/* 100 */     private static final DefaultUncaughtExceptionHandler INSTANCE = new DefaultUncaughtExceptionHandler();
/*     */     
/*     */     public void uncaughtException(Thread t, Throwable e)
/*     */     {
/* 104 */       if (DefaultExecutorServiceFactory.logger.isErrorEnabled()) {
/* 105 */         DefaultExecutorServiceFactory.logger.error("Uncaught exception in thread: {}", t.getName(), e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class DefaultForkJoinWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory
/*     */   {
/* 112 */     private final AtomicInteger idx = new AtomicInteger();
/*     */     private final String namePrefix;
/*     */     
/*     */     DefaultForkJoinWorkerThreadFactory(String namePrefix) {
/* 116 */       this.namePrefix = namePrefix;
/*     */     }
/*     */     
/*     */ 
/*     */     public ForkJoinWorkerThread newThread(ForkJoinPool pool)
/*     */     {
/* 122 */       ForkJoinWorkerThread thread = new DefaultExecutorServiceFactory.DefaultForkJoinWorkerThread(pool);
/* 123 */       thread.setName(this.namePrefix + '-' + this.idx.getAndIncrement());
/* 124 */       thread.setPriority(10);
/* 125 */       return thread;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class DefaultForkJoinWorkerThread extends ForkJoinWorkerThread implements FastThreadLocalAccess
/*     */   {
/*     */     private InternalThreadLocalMap threadLocalMap;
/*     */     
/*     */     DefaultForkJoinWorkerThread(ForkJoinPool pool)
/*     */     {
/* 135 */       super();
/*     */     }
/*     */     
/*     */     public InternalThreadLocalMap threadLocalMap()
/*     */     {
/* 140 */       return this.threadLocalMap;
/*     */     }
/*     */     
/*     */     public void setThreadLocalMap(InternalThreadLocalMap threadLocalMap)
/*     */     {
/* 145 */       this.threadLocalMap = threadLocalMap;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\DefaultExecutorServiceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */