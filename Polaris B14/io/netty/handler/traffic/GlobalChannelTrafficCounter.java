/*     */ package io.netty.handler.traffic;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ public class GlobalChannelTrafficCounter
/*     */   extends TrafficCounter
/*     */ {
/*     */   public GlobalChannelTrafficCounter(GlobalChannelTrafficShapingHandler trafficShapingHandler, ScheduledExecutorService executor, String name, long checkInterval)
/*     */   {
/*  38 */     super(trafficShapingHandler, executor, name, checkInterval);
/*  39 */     if (executor == null) {
/*  40 */       throw new IllegalArgumentException("Executor must not be null");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class MixedTrafficMonitoringTask
/*     */     implements Runnable
/*     */   {
/*     */     private final GlobalChannelTrafficShapingHandler trafficShapingHandler1;
/*     */     
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
/*     */     MixedTrafficMonitoringTask(GlobalChannelTrafficShapingHandler trafficShapingHandler, TrafficCounter counter)
/*     */     {
/*  66 */       this.trafficShapingHandler1 = trafficShapingHandler;
/*  67 */       this.counter = counter;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/*  72 */       if (!this.counter.monitorActive) {
/*  73 */         return;
/*     */       }
/*  75 */       long newLastTime = TrafficCounter.milliSecondFromNano();
/*  76 */       this.counter.resetAccounting(newLastTime);
/*  77 */       for (GlobalChannelTrafficShapingHandler.PerChannel perChannel : this.trafficShapingHandler1.channelQueues.values()) {
/*  78 */         perChannel.channelTrafficCounter.resetAccounting(newLastTime);
/*     */       }
/*  80 */       this.trafficShapingHandler1.doAccounting(this.counter);
/*  81 */       this.counter.scheduledFuture = this.counter.executor.schedule(this, this.counter.checkInterval.get(), TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void start()
/*     */   {
/*  91 */     if (this.monitorActive) {
/*  92 */       return;
/*     */     }
/*  94 */     this.lastTime.set(milliSecondFromNano());
/*  95 */     long localCheckInterval = this.checkInterval.get();
/*  96 */     if (localCheckInterval > 0L) {
/*  97 */       this.monitorActive = true;
/*  98 */       this.monitor = new MixedTrafficMonitoringTask((GlobalChannelTrafficShapingHandler)this.trafficShapingHandler, this);
/*  99 */       this.scheduledFuture = this.executor.schedule(this.monitor, localCheckInterval, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void stop()
/*     */   {
/* 109 */     if (!this.monitorActive) {
/* 110 */       return;
/*     */     }
/* 112 */     this.monitorActive = false;
/* 113 */     resetAccounting(milliSecondFromNano());
/* 114 */     this.trafficShapingHandler.doAccounting(this);
/* 115 */     if (this.scheduledFuture != null) {
/* 116 */       this.scheduledFuture.cancel(true);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void resetCumulativeTime()
/*     */   {
/* 123 */     for (GlobalChannelTrafficShapingHandler.PerChannel perChannel : ((GlobalChannelTrafficShapingHandler)this.trafficShapingHandler).channelQueues.values()) {
/* 124 */       perChannel.channelTrafficCounter.resetCumulativeTime();
/*     */     }
/* 126 */     super.resetCumulativeTime();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\traffic\GlobalChannelTrafficCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */