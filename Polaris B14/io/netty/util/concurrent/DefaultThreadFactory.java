/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.ThreadFactory;
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
/*     */ public class DefaultThreadFactory
/*     */   implements ThreadFactory
/*     */ {
/*  30 */   private static final AtomicInteger poolId = new AtomicInteger();
/*     */   
/*  32 */   private final AtomicInteger nextId = new AtomicInteger();
/*     */   private final String prefix;
/*     */   private final boolean daemon;
/*     */   private final int priority;
/*     */   
/*     */   public DefaultThreadFactory(Class<?> poolType) {
/*  38 */     this(poolType, false, 5);
/*     */   }
/*     */   
/*     */   public DefaultThreadFactory(String poolName) {
/*  42 */     this(poolName, false, 5);
/*     */   }
/*     */   
/*     */   public DefaultThreadFactory(Class<?> poolType, boolean daemon) {
/*  46 */     this(poolType, daemon, 5);
/*     */   }
/*     */   
/*     */   public DefaultThreadFactory(String poolName, boolean daemon) {
/*  50 */     this(poolName, daemon, 5);
/*     */   }
/*     */   
/*     */   public DefaultThreadFactory(Class<?> poolType, int priority) {
/*  54 */     this(poolType, false, priority);
/*     */   }
/*     */   
/*     */   public DefaultThreadFactory(String poolName, int priority) {
/*  58 */     this(poolName, false, priority);
/*     */   }
/*     */   
/*     */   public DefaultThreadFactory(Class<?> poolType, boolean daemon, int priority) {
/*  62 */     this(toPoolName(poolType), daemon, priority);
/*     */   }
/*     */   
/*     */   private static String toPoolName(Class<?> poolType) {
/*  66 */     if (poolType == null) {
/*  67 */       throw new NullPointerException("poolType");
/*     */     }
/*     */     
/*  70 */     String poolName = StringUtil.simpleClassName(poolType);
/*  71 */     switch (poolName.length()) {
/*     */     case 0: 
/*  73 */       return "unknown";
/*     */     case 1: 
/*  75 */       return poolName.toLowerCase(Locale.US);
/*     */     }
/*  77 */     if ((Character.isUpperCase(poolName.charAt(0))) && (Character.isLowerCase(poolName.charAt(1)))) {
/*  78 */       return Character.toLowerCase(poolName.charAt(0)) + poolName.substring(1);
/*     */     }
/*  80 */     return poolName;
/*     */   }
/*     */   
/*     */ 
/*     */   public DefaultThreadFactory(String poolName, boolean daemon, int priority)
/*     */   {
/*  86 */     if (poolName == null) {
/*  87 */       throw new NullPointerException("poolName");
/*     */     }
/*  89 */     if ((priority < 1) || (priority > 10)) {
/*  90 */       throw new IllegalArgumentException("priority: " + priority + " (expected: Thread.MIN_PRIORITY <= priority <= Thread.MAX_PRIORITY)");
/*     */     }
/*     */     
/*     */ 
/*  94 */     this.prefix = (poolName + '-' + poolId.incrementAndGet() + '-');
/*  95 */     this.daemon = daemon;
/*  96 */     this.priority = priority;
/*     */   }
/*     */   
/*     */   public Thread newThread(Runnable r)
/*     */   {
/* 101 */     Thread t = newThread(new DefaultRunnableDecorator(r), this.prefix + this.nextId.incrementAndGet());
/*     */     try {
/* 103 */       if (t.isDaemon()) {
/* 104 */         if (!this.daemon) {
/* 105 */           t.setDaemon(false);
/*     */         }
/*     */       }
/* 108 */       else if (this.daemon) {
/* 109 */         t.setDaemon(true);
/*     */       }
/*     */       
/*     */ 
/* 113 */       if (t.getPriority() != this.priority) {
/* 114 */         t.setPriority(this.priority);
/*     */       }
/*     */     }
/*     */     catch (Exception ignored) {}
/*     */     
/* 119 */     return t;
/*     */   }
/*     */   
/*     */   protected Thread newThread(Runnable r, String name) {
/* 123 */     return new FastThreadLocalThread(r, name);
/*     */   }
/*     */   
/*     */   private static final class DefaultRunnableDecorator implements Runnable
/*     */   {
/*     */     private final Runnable r;
/*     */     
/*     */     DefaultRunnableDecorator(Runnable r) {
/* 131 */       this.r = r;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/*     */       try {
/* 137 */         this.r.run();
/*     */       } finally {
/* 139 */         FastThreadLocal.removeAll();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\DefaultThreadFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */