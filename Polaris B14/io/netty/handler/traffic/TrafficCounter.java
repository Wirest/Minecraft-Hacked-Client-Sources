/*     */ package io.netty.handler.traffic;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
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
/*     */ public class TrafficCounter
/*     */ {
/*  39 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(TrafficCounter.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long milliSecondFromNano()
/*     */   {
/*  46 */     return System.nanoTime() / 1000000L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private final AtomicLong currentWrittenBytes = new AtomicLong();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private final AtomicLong currentReadBytes = new AtomicLong();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private long writingTime;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private long readingTime;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  72 */   private final AtomicLong cumulativeWrittenBytes = new AtomicLong();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  77 */   private final AtomicLong cumulativeReadBytes = new AtomicLong();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private long lastCumulativeTime;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private long lastWriteThroughput;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private long lastReadThroughput;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  97 */   final AtomicLong lastTime = new AtomicLong();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private volatile long lastWrittenBytes;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private volatile long lastReadBytes;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private volatile long lastWritingTime;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private volatile long lastReadingTime;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 122 */   private final AtomicLong realWrittenBytes = new AtomicLong();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private long realWriteThroughput;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 132 */   final AtomicLong checkInterval = new AtomicLong(1000L);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final String name;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final AbstractTrafficShapingHandler trafficShapingHandler;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final ScheduledExecutorService executor;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Runnable monitor;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   volatile ScheduledFuture<?> scheduledFuture;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   volatile boolean monitorActive;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class TrafficMonitoringTask
/*     */     implements Runnable
/*     */   {
/*     */     private final AbstractTrafficShapingHandler trafficShapingHandler1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private final TrafficCounter counter;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected TrafficMonitoringTask(AbstractTrafficShapingHandler trafficShapingHandler, TrafficCounter counter)
/*     */     {
/* 189 */       this.trafficShapingHandler1 = trafficShapingHandler;
/* 190 */       this.counter = counter;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 195 */       if (!this.counter.monitorActive) {
/* 196 */         return;
/*     */       }
/* 198 */       this.counter.resetAccounting(TrafficCounter.milliSecondFromNano());
/* 199 */       if (this.trafficShapingHandler1 != null) {
/* 200 */         this.trafficShapingHandler1.doAccounting(this.counter);
/*     */       }
/* 202 */       this.counter.scheduledFuture = this.counter.executor.schedule(this, this.counter.checkInterval.get(), TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void start()
/*     */   {
/* 211 */     if (this.monitorActive) {
/* 212 */       return;
/*     */     }
/* 214 */     this.lastTime.set(milliSecondFromNano());
/* 215 */     long localCheckInterval = this.checkInterval.get();
/*     */     
/* 217 */     if ((localCheckInterval > 0L) && (this.executor != null)) {
/* 218 */       this.monitorActive = true;
/* 219 */       this.monitor = new TrafficMonitoringTask(this.trafficShapingHandler, this);
/* 220 */       this.scheduledFuture = this.executor.schedule(this.monitor, localCheckInterval, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void stop()
/*     */   {
/* 229 */     if (!this.monitorActive) {
/* 230 */       return;
/*     */     }
/* 232 */     this.monitorActive = false;
/* 233 */     resetAccounting(milliSecondFromNano());
/* 234 */     if (this.trafficShapingHandler != null) {
/* 235 */       this.trafficShapingHandler.doAccounting(this);
/*     */     }
/* 237 */     if (this.scheduledFuture != null) {
/* 238 */       this.scheduledFuture.cancel(true);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   synchronized void resetAccounting(long newLastTime)
/*     */   {
/* 248 */     long interval = newLastTime - this.lastTime.getAndSet(newLastTime);
/* 249 */     if (interval == 0L)
/*     */     {
/* 251 */       return;
/*     */     }
/* 253 */     if ((logger.isDebugEnabled()) && (interval > checkInterval() << 1)) {
/* 254 */       logger.debug("Acct schedule not ok: " + interval + " > 2*" + checkInterval() + " from " + this.name);
/*     */     }
/* 256 */     this.lastReadBytes = this.currentReadBytes.getAndSet(0L);
/* 257 */     this.lastWrittenBytes = this.currentWrittenBytes.getAndSet(0L);
/* 258 */     this.lastReadThroughput = (this.lastReadBytes * 1000L / interval);
/*     */     
/* 260 */     this.lastWriteThroughput = (this.lastWrittenBytes * 1000L / interval);
/*     */     
/* 262 */     this.realWriteThroughput = (this.realWrittenBytes.getAndSet(0L) * 1000L / interval);
/* 263 */     this.lastWritingTime = Math.max(this.lastWritingTime, this.writingTime);
/* 264 */     this.lastReadingTime = Math.max(this.lastReadingTime, this.readingTime);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public TrafficCounter(AbstractTrafficShapingHandler trafficShapingHandler, ScheduledExecutorService executor, String name, long checkInterval)
/*     */   {
/* 283 */     if (trafficShapingHandler == null) {
/* 284 */       throw new IllegalArgumentException("TrafficShapingHandler must not be null");
/*     */     }
/* 286 */     this.trafficShapingHandler = trafficShapingHandler;
/* 287 */     this.executor = executor;
/* 288 */     this.name = name;
/*     */     
/* 290 */     this.lastCumulativeTime = System.currentTimeMillis();
/* 291 */     this.writingTime = milliSecondFromNano();
/* 292 */     this.readingTime = this.writingTime;
/* 293 */     this.lastWritingTime = this.writingTime;
/* 294 */     this.lastReadingTime = this.writingTime;
/* 295 */     configure(checkInterval);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void configure(long newcheckInterval)
/*     */   {
/* 304 */     long newInterval = newcheckInterval / 10L * 10L;
/* 305 */     if (this.checkInterval.getAndSet(newInterval) != newInterval) {
/* 306 */       if (newInterval <= 0L) {
/* 307 */         stop();
/*     */         
/* 309 */         this.lastTime.set(milliSecondFromNano());
/*     */       }
/*     */       else {
/* 312 */         start();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void bytesRecvFlowControl(long recv)
/*     */   {
/* 324 */     this.currentReadBytes.addAndGet(recv);
/* 325 */     this.cumulativeReadBytes.addAndGet(recv);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void bytesWriteFlowControl(long write)
/*     */   {
/* 335 */     this.currentWrittenBytes.addAndGet(write);
/* 336 */     this.cumulativeWrittenBytes.addAndGet(write);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void bytesRealWriteFlowControl(long write)
/*     */   {
/* 346 */     this.realWrittenBytes.addAndGet(write);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long checkInterval()
/*     */   {
/* 354 */     return this.checkInterval.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long lastReadThroughput()
/*     */   {
/* 361 */     return this.lastReadThroughput;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long lastWriteThroughput()
/*     */   {
/* 368 */     return this.lastWriteThroughput;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long lastReadBytes()
/*     */   {
/* 375 */     return this.lastReadBytes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long lastWrittenBytes()
/*     */   {
/* 382 */     return this.lastWrittenBytes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long currentReadBytes()
/*     */   {
/* 389 */     return this.currentReadBytes.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long currentWrittenBytes()
/*     */   {
/* 396 */     return this.currentWrittenBytes.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long lastTime()
/*     */   {
/* 403 */     return this.lastTime.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long cumulativeWrittenBytes()
/*     */   {
/* 410 */     return this.cumulativeWrittenBytes.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long cumulativeReadBytes()
/*     */   {
/* 417 */     return this.cumulativeReadBytes.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long lastCumulativeTime()
/*     */   {
/* 425 */     return this.lastCumulativeTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AtomicLong getRealWrittenBytes()
/*     */   {
/* 432 */     return this.realWrittenBytes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getRealWriteThroughput()
/*     */   {
/* 439 */     return this.realWriteThroughput;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetCumulativeTime()
/*     */   {
/* 447 */     this.lastCumulativeTime = System.currentTimeMillis();
/* 448 */     this.cumulativeReadBytes.set(0L);
/* 449 */     this.cumulativeWrittenBytes.set(0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String name()
/*     */   {
/* 456 */     return this.name;
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
/*     */   @Deprecated
/*     */   public long readTimeToWait(long size, long limitTraffic, long maxTime)
/*     */   {
/* 473 */     return readTimeToWait(size, limitTraffic, maxTime, milliSecondFromNano());
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
/*     */ 
/*     */   public long readTimeToWait(long size, long limitTraffic, long maxTime, long now)
/*     */   {
/* 490 */     bytesRecvFlowControl(size);
/* 491 */     if ((size == 0L) || (limitTraffic == 0L)) {
/* 492 */       return 0L;
/*     */     }
/* 494 */     long lastTimeCheck = this.lastTime.get();
/* 495 */     long sum = this.currentReadBytes.get();
/* 496 */     long localReadingTime = this.readingTime;
/* 497 */     long lastRB = this.lastReadBytes;
/* 498 */     long interval = now - lastTimeCheck;
/* 499 */     long pastDelay = Math.max(this.lastReadingTime - lastTimeCheck, 0L);
/* 500 */     if (interval > 10L)
/*     */     {
/* 502 */       long time = sum * 1000L / limitTraffic - interval + pastDelay;
/* 503 */       if (time > 10L) {
/* 504 */         if (logger.isDebugEnabled()) {
/* 505 */           logger.debug("Time: " + time + ':' + sum + ':' + interval + ':' + pastDelay);
/*     */         }
/* 507 */         if ((time > maxTime) && (now + time - localReadingTime > maxTime)) {
/* 508 */           time = maxTime;
/*     */         }
/* 510 */         this.readingTime = Math.max(localReadingTime, now + time);
/* 511 */         return time;
/*     */       }
/* 513 */       this.readingTime = Math.max(localReadingTime, now);
/* 514 */       return 0L;
/*     */     }
/*     */     
/* 517 */     long lastsum = sum + lastRB;
/* 518 */     long lastinterval = interval + this.checkInterval.get();
/* 519 */     long time = lastsum * 1000L / limitTraffic - lastinterval + pastDelay;
/* 520 */     if (time > 10L) {
/* 521 */       if (logger.isDebugEnabled()) {
/* 522 */         logger.debug("Time: " + time + ':' + lastsum + ':' + lastinterval + ':' + pastDelay);
/*     */       }
/* 524 */       if ((time > maxTime) && (now + time - localReadingTime > maxTime)) {
/* 525 */         time = maxTime;
/*     */       }
/* 527 */       this.readingTime = Math.max(localReadingTime, now + time);
/* 528 */       return time;
/*     */     }
/* 530 */     this.readingTime = Math.max(localReadingTime, now);
/* 531 */     return 0L;
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
/*     */   @Deprecated
/*     */   public long writeTimeToWait(long size, long limitTraffic, long maxTime)
/*     */   {
/* 548 */     return writeTimeToWait(size, limitTraffic, maxTime, milliSecondFromNano());
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
/*     */ 
/*     */   public long writeTimeToWait(long size, long limitTraffic, long maxTime, long now)
/*     */   {
/* 565 */     bytesWriteFlowControl(size);
/* 566 */     if ((size == 0L) || (limitTraffic == 0L)) {
/* 567 */       return 0L;
/*     */     }
/* 569 */     long lastTimeCheck = this.lastTime.get();
/* 570 */     long sum = this.currentWrittenBytes.get();
/* 571 */     long lastWB = this.lastWrittenBytes;
/* 572 */     long localWritingTime = this.writingTime;
/* 573 */     long pastDelay = Math.max(this.lastWritingTime - lastTimeCheck, 0L);
/* 574 */     long interval = now - lastTimeCheck;
/* 575 */     if (interval > 10L)
/*     */     {
/* 577 */       long time = sum * 1000L / limitTraffic - interval + pastDelay;
/* 578 */       if (time > 10L) {
/* 579 */         if (logger.isDebugEnabled()) {
/* 580 */           logger.debug("Time: " + time + ':' + sum + ':' + interval + ':' + pastDelay);
/*     */         }
/* 582 */         if ((time > maxTime) && (now + time - localWritingTime > maxTime)) {
/* 583 */           time = maxTime;
/*     */         }
/* 585 */         this.writingTime = Math.max(localWritingTime, now + time);
/* 586 */         return time;
/*     */       }
/* 588 */       this.writingTime = Math.max(localWritingTime, now);
/* 589 */       return 0L;
/*     */     }
/*     */     
/* 592 */     long lastsum = sum + lastWB;
/* 593 */     long lastinterval = interval + this.checkInterval.get();
/* 594 */     long time = lastsum * 1000L / limitTraffic - lastinterval + pastDelay;
/* 595 */     if (time > 10L) {
/* 596 */       if (logger.isDebugEnabled()) {
/* 597 */         logger.debug("Time: " + time + ':' + lastsum + ':' + lastinterval + ':' + pastDelay);
/*     */       }
/* 599 */       if ((time > maxTime) && (now + time - localWritingTime > maxTime)) {
/* 600 */         time = maxTime;
/*     */       }
/* 602 */       this.writingTime = Math.max(localWritingTime, now + time);
/* 603 */       return time;
/*     */     }
/* 605 */     this.writingTime = Math.max(localWritingTime, now);
/* 606 */     return 0L;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 611 */     return 165 + "Monitor " + this.name + " Current Speed Read: " + (this.lastReadThroughput >> 10) + " KB/s, " + "Asked Write: " + (this.lastWriteThroughput >> 10) + " KB/s, " + "Real Write: " + (this.realWriteThroughput >> 10) + " KB/s, " + "Current Read: " + (this.currentReadBytes.get() >> 10) + " KB, " + "Current asked Write: " + (this.currentWrittenBytes.get() >> 10) + " KB, " + "Current real Write: " + (this.realWrittenBytes.get() >> 10) + " KB";
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\traffic\TrafficCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */