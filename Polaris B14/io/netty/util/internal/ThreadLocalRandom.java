/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public final class ThreadLocalRandom
/*     */   extends Random
/*     */ {
/*  63 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ThreadLocalRandom.class);
/*     */   
/*  65 */   private static final AtomicLong seedUniquifier = new AtomicLong();
/*     */   
/*  67 */   private static volatile long initialSeedUniquifier = SystemPropertyUtil.getLong("io.netty.initialSeedUniquifier", 0L);
/*     */   
/*     */   private static final Thread seedGeneratorThread;
/*     */   private static final BlockingQueue<byte[]> seedQueue;
/*     */   private static final long seedGeneratorStartTime;
/*     */   private static volatile long seedGeneratorEndTime;
/*     */   
/*     */   static
/*     */   {
/*  76 */     if (initialSeedUniquifier == 0L)
/*     */     {
/*     */ 
/*  79 */       seedGeneratorThread = new Thread("initialSeedUniquifierGenerator")
/*     */       {
/*     */         public void run() {
/*  82 */           SecureRandom random = new SecureRandom();
/*  83 */           byte[] seed = random.generateSeed(8);
/*  84 */           ThreadLocalRandom.access$002(System.nanoTime());
/*  85 */           ThreadLocalRandom.seedQueue.add(seed);
/*     */         }
/*  87 */       };
/*  88 */       seedGeneratorThread.setDaemon(true);
/*  89 */       seedGeneratorThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
/*     */       {
/*     */         public void uncaughtException(Thread t, Throwable e) {
/*  92 */           ThreadLocalRandom.logger.debug("An exception has been raised by {}", t.getName(), e);
/*     */         }
/*     */         
/*  95 */       });
/*  96 */       seedQueue = new LinkedBlockingQueue();
/*  97 */       seedGeneratorStartTime = System.nanoTime();
/*  98 */       seedGeneratorThread.start();
/*     */     } else {
/* 100 */       seedGeneratorThread = null;
/* 101 */       seedQueue = null;
/* 102 */       seedGeneratorStartTime = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setInitialSeedUniquifier(long initialSeedUniquifier) {
/* 107 */     initialSeedUniquifier = initialSeedUniquifier;
/*     */   }
/*     */   
/*     */   public static long getInitialSeedUniquifier()
/*     */   {
/* 112 */     long initialSeedUniquifier = initialSeedUniquifier;
/* 113 */     if (initialSeedUniquifier != 0L) {
/* 114 */       return initialSeedUniquifier;
/*     */     }
/*     */     
/* 117 */     synchronized (ThreadLocalRandom.class) {
/* 118 */       initialSeedUniquifier = initialSeedUniquifier;
/* 119 */       if (initialSeedUniquifier != 0L) {
/* 120 */         return initialSeedUniquifier;
/*     */       }
/*     */       
/*     */ 
/* 124 */       long timeoutSeconds = 3L;
/* 125 */       long deadLine = seedGeneratorStartTime + TimeUnit.SECONDS.toNanos(3L);
/* 126 */       boolean interrupted = false;
/*     */       for (;;) {
/* 128 */         long waitTime = deadLine - System.nanoTime();
/*     */         try { byte[] seed;
/*     */           byte[] seed;
/* 131 */           if (waitTime <= 0L) {
/* 132 */             seed = (byte[])seedQueue.poll();
/*     */           } else {
/* 134 */             seed = (byte[])seedQueue.poll(waitTime, TimeUnit.NANOSECONDS);
/*     */           }
/*     */           
/* 137 */           if (seed != null) {
/* 138 */             initialSeedUniquifier = (seed[0] & 0xFF) << 56 | (seed[1] & 0xFF) << 48 | (seed[2] & 0xFF) << 40 | (seed[3] & 0xFF) << 32 | (seed[4] & 0xFF) << 24 | (seed[5] & 0xFF) << 16 | (seed[6] & 0xFF) << 8 | seed[7] & 0xFF;
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 147 */             break;
/*     */           }
/*     */         } catch (InterruptedException e) {
/* 150 */           interrupted = true;
/* 151 */           logger.warn("Failed to generate a seed from SecureRandom due to an InterruptedException.");
/* 152 */           break;
/*     */         }
/*     */         
/* 155 */         if (waitTime <= 0L) {
/* 156 */           seedGeneratorThread.interrupt();
/* 157 */           logger.warn("Failed to generate a seed from SecureRandom within {} seconds. Not enough entrophy?", Long.valueOf(3L));
/*     */           
/*     */ 
/*     */ 
/* 161 */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 166 */       initialSeedUniquifier ^= 0x3255ECDC33BAE119;
/* 167 */       initialSeedUniquifier ^= Long.reverse(System.nanoTime());
/*     */       
/* 169 */       initialSeedUniquifier = initialSeedUniquifier;
/*     */       
/* 171 */       if (interrupted)
/*     */       {
/* 173 */         Thread.currentThread().interrupt();
/*     */         
/*     */ 
/*     */ 
/* 177 */         seedGeneratorThread.interrupt();
/*     */       }
/*     */       
/* 180 */       if (seedGeneratorEndTime == 0L) {
/* 181 */         seedGeneratorEndTime = System.nanoTime();
/*     */       }
/*     */       
/* 184 */       return initialSeedUniquifier;
/*     */     }
/*     */   }
/*     */   
/*     */   private static long newSeed() {
/*     */     for (;;) {
/* 190 */       long current = seedUniquifier.get();
/* 191 */       long actualCurrent = current != 0L ? current : getInitialSeedUniquifier();
/*     */       
/*     */ 
/* 194 */       long next = actualCurrent * 181783497276652981L;
/*     */       
/* 196 */       if (seedUniquifier.compareAndSet(current, next)) {
/* 197 */         if ((current == 0L) && (logger.isDebugEnabled())) {
/* 198 */           if (seedGeneratorEndTime != 0L) {
/* 199 */             logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x (took %d ms)", new Object[] { Long.valueOf(actualCurrent), Long.valueOf(TimeUnit.NANOSECONDS.toMillis(seedGeneratorEndTime - seedGeneratorStartTime)) }));
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 204 */             logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x", new Object[] { Long.valueOf(actualCurrent) }));
/*     */           }
/*     */         }
/* 207 */         return next ^ System.nanoTime();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final long multiplier = 25214903917L;
/*     */   
/*     */   private static final long addend = 11L;
/*     */   
/*     */   private static final long mask = 281474976710655L;
/*     */   
/*     */   private long rnd;
/*     */   
/*     */   boolean initialized;
/*     */   
/*     */   private long pad0;
/*     */   
/*     */   private long pad1;
/*     */   
/*     */   private long pad2;
/*     */   
/*     */   private long pad3;
/*     */   
/*     */   private long pad4;
/*     */   
/*     */   private long pad5;
/*     */   private long pad6;
/*     */   private long pad7;
/*     */   private static final long serialVersionUID = -5851777807851030925L;
/*     */   ThreadLocalRandom()
/*     */   {
/* 239 */     super(newSeed());
/* 240 */     this.initialized = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ThreadLocalRandom current()
/*     */   {
/* 249 */     return InternalThreadLocalMap.get().random();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSeed(long seed)
/*     */   {
/* 259 */     if (this.initialized) {
/* 260 */       throw new UnsupportedOperationException();
/*     */     }
/* 262 */     this.rnd = ((seed ^ 0x5DEECE66D) & 0xFFFFFFFFFFFF);
/*     */   }
/*     */   
/*     */   protected int next(int bits) {
/* 266 */     this.rnd = (this.rnd * 25214903917L + 11L & 0xFFFFFFFFFFFF);
/* 267 */     return (int)(this.rnd >>> 48 - bits);
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
/*     */   public int nextInt(int least, int bound)
/*     */   {
/* 281 */     if (least >= bound) {
/* 282 */       throw new IllegalArgumentException();
/*     */     }
/* 284 */     return nextInt(bound - least) + least;
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
/*     */   public long nextLong(long n)
/*     */   {
/* 297 */     if (n <= 0L) {
/* 298 */       throw new IllegalArgumentException("n must be positive");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 306 */     long offset = 0L;
/* 307 */     while (n >= 2147483647L) {
/* 308 */       int bits = next(2);
/* 309 */       long half = n >>> 1;
/* 310 */       long nextn = (bits & 0x2) == 0 ? half : n - half;
/* 311 */       if ((bits & 0x1) == 0) {
/* 312 */         offset += n - nextn;
/*     */       }
/* 314 */       n = nextn;
/*     */     }
/* 316 */     return offset + nextInt((int)n);
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
/*     */   public long nextLong(long least, long bound)
/*     */   {
/* 330 */     if (least >= bound) {
/* 331 */       throw new IllegalArgumentException();
/*     */     }
/* 333 */     return nextLong(bound - least) + least;
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
/*     */   public double nextDouble(double n)
/*     */   {
/* 346 */     if (n <= 0.0D) {
/* 347 */       throw new IllegalArgumentException("n must be positive");
/*     */     }
/* 349 */     return nextDouble() * n;
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
/*     */   public double nextDouble(double least, double bound)
/*     */   {
/* 363 */     if (least >= bound) {
/* 364 */       throw new IllegalArgumentException();
/*     */     }
/* 366 */     return nextDouble() * (bound - least) + least;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\ThreadLocalRandom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */