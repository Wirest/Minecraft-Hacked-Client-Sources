/*     */ package io.netty.handler.traffic;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.Attribute;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ScheduledExecutorService;
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
/*     */ @ChannelHandler.Sharable
/*     */ public class GlobalChannelTrafficShapingHandler
/*     */   extends AbstractTrafficShapingHandler
/*     */ {
/*  88 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(GlobalChannelTrafficShapingHandler.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  93 */   final ConcurrentMap<Integer, PerChannel> channelQueues = PlatformDependent.newConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  98 */   private final AtomicLong queuesSize = new AtomicLong();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 103 */   private final AtomicLong cumulativeWrittenBytes = new AtomicLong();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 108 */   private final AtomicLong cumulativeReadBytes = new AtomicLong();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */   volatile long maxGlobalWriteSize = 419430400L;
/*     */   
/*     */ 
/*     */   private volatile long writeChannelLimit;
/*     */   
/*     */ 
/*     */   private volatile long readChannelLimit;
/*     */   
/*     */ 
/*     */   private static final float DEFAULT_DEVIATION = 0.1F;
/*     */   
/*     */ 
/*     */   private static final float MAX_DEVIATION = 0.4F;
/*     */   
/*     */ 
/*     */   private static final float DEFAULT_SLOWDOWN = 0.4F;
/*     */   
/*     */ 
/*     */   private static final float DEFAULT_ACCELERATION = -0.1F;
/*     */   
/*     */ 
/*     */   private volatile float maxDeviation;
/*     */   
/*     */ 
/*     */   private volatile float accelerationFactor;
/*     */   
/*     */   private volatile float slowDownFactor;
/*     */   
/*     */   private volatile boolean readDeviationActive;
/*     */   
/*     */   private volatile boolean writeDeviationActive;
/*     */   
/*     */ 
/*     */   void createGlobalTrafficCounter(ScheduledExecutorService executor)
/*     */   {
/* 149 */     setMaxDeviation(0.1F, 0.4F, -0.1F);
/* 150 */     if (executor == null) {
/* 151 */       throw new IllegalArgumentException("Executor must not be null");
/*     */     }
/* 153 */     TrafficCounter tc = new GlobalChannelTrafficCounter(this, executor, "GlobalChannelTC", this.checkInterval);
/* 154 */     setTrafficCounter(tc);
/* 155 */     tc.start();
/*     */   }
/*     */   
/*     */   int userDefinedWritabilityIndex()
/*     */   {
/* 160 */     return 3;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GlobalChannelTrafficShapingHandler(ScheduledExecutorService executor, long writeGlobalLimit, long readGlobalLimit, long writeChannelLimit, long readChannelLimit, long checkInterval, long maxTime)
/*     */   {
/* 186 */     super(writeGlobalLimit, readGlobalLimit, checkInterval, maxTime);
/* 187 */     createGlobalTrafficCounter(executor);
/* 188 */     this.writeChannelLimit = writeChannelLimit;
/* 189 */     this.readChannelLimit = readChannelLimit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GlobalChannelTrafficShapingHandler(ScheduledExecutorService executor, long writeGlobalLimit, long readGlobalLimit, long writeChannelLimit, long readChannelLimit, long checkInterval)
/*     */   {
/* 213 */     super(writeGlobalLimit, readGlobalLimit, checkInterval);
/* 214 */     this.writeChannelLimit = writeChannelLimit;
/* 215 */     this.readChannelLimit = readChannelLimit;
/* 216 */     createGlobalTrafficCounter(executor);
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
/*     */ 
/*     */   public GlobalChannelTrafficShapingHandler(ScheduledExecutorService executor, long writeGlobalLimit, long readGlobalLimit, long writeChannelLimit, long readChannelLimit)
/*     */   {
/* 236 */     super(writeGlobalLimit, readGlobalLimit);
/* 237 */     this.writeChannelLimit = writeChannelLimit;
/* 238 */     this.readChannelLimit = readChannelLimit;
/* 239 */     createGlobalTrafficCounter(executor);
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
/*     */   public GlobalChannelTrafficShapingHandler(ScheduledExecutorService executor, long checkInterval)
/*     */   {
/* 252 */     super(checkInterval);
/* 253 */     createGlobalTrafficCounter(executor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GlobalChannelTrafficShapingHandler(ScheduledExecutorService executor)
/*     */   {
/* 263 */     createGlobalTrafficCounter(executor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public float maxDeviation()
/*     */   {
/* 270 */     return this.maxDeviation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public float accelerationFactor()
/*     */   {
/* 277 */     return this.accelerationFactor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public float slowDownFactor()
/*     */   {
/* 284 */     return this.slowDownFactor;
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
/*     */   public void setMaxDeviation(float maxDeviation, float slowDownFactor, float accelerationFactor)
/*     */   {
/* 299 */     if (maxDeviation > 0.4F) {
/* 300 */       throw new IllegalArgumentException("maxDeviation must be <= 0.4");
/*     */     }
/* 302 */     if (slowDownFactor < 0.0F) {
/* 303 */       throw new IllegalArgumentException("slowDownFactor must be >= 0");
/*     */     }
/* 305 */     if (accelerationFactor > 0.0F) {
/* 306 */       throw new IllegalArgumentException("accelerationFactor must be <= 0");
/*     */     }
/* 308 */     this.maxDeviation = maxDeviation;
/* 309 */     this.accelerationFactor = (1.0F + accelerationFactor);
/* 310 */     this.slowDownFactor = (1.0F + slowDownFactor);
/*     */   }
/*     */   
/*     */   private void computeDeviationCumulativeBytes()
/*     */   {
/* 315 */     long maxWrittenBytes = 0L;
/* 316 */     long maxReadBytes = 0L;
/* 317 */     long minWrittenBytes = Long.MAX_VALUE;
/* 318 */     long minReadBytes = Long.MAX_VALUE;
/* 319 */     for (PerChannel perChannel : this.channelQueues.values()) {
/* 320 */       long value = perChannel.channelTrafficCounter.cumulativeWrittenBytes();
/* 321 */       if (maxWrittenBytes < value) {
/* 322 */         maxWrittenBytes = value;
/*     */       }
/* 324 */       if (minWrittenBytes > value) {
/* 325 */         minWrittenBytes = value;
/*     */       }
/* 327 */       value = perChannel.channelTrafficCounter.cumulativeReadBytes();
/* 328 */       if (maxReadBytes < value) {
/* 329 */         maxReadBytes = value;
/*     */       }
/* 331 */       if (minReadBytes > value) {
/* 332 */         minReadBytes = value;
/*     */       }
/*     */     }
/* 335 */     boolean multiple = this.channelQueues.size() > 1;
/* 336 */     this.readDeviationActive = ((multiple) && (minReadBytes < maxReadBytes / 2L));
/* 337 */     this.writeDeviationActive = ((multiple) && (minWrittenBytes < maxWrittenBytes / 2L));
/* 338 */     this.cumulativeWrittenBytes.set(maxWrittenBytes);
/* 339 */     this.cumulativeReadBytes.set(maxReadBytes);
/*     */   }
/*     */   
/*     */   protected void doAccounting(TrafficCounter counter)
/*     */   {
/* 344 */     computeDeviationCumulativeBytes();
/* 345 */     super.doAccounting(counter);
/*     */   }
/*     */   
/*     */   private long computeBalancedWait(float maxLocal, float maxGlobal, long wait) {
/* 349 */     if (maxGlobal == 0.0F)
/*     */     {
/* 351 */       return wait;
/*     */     }
/* 353 */     float ratio = maxLocal / maxGlobal;
/*     */     
/* 355 */     if (ratio > this.maxDeviation) {
/* 356 */       if (ratio < 1.0F - this.maxDeviation) {
/* 357 */         return wait;
/*     */       }
/* 359 */       ratio = this.slowDownFactor;
/* 360 */       if (wait < 10L) {
/* 361 */         wait = 10L;
/*     */       }
/*     */     }
/*     */     else {
/* 365 */       ratio = this.accelerationFactor;
/*     */     }
/* 367 */     return ((float)wait * ratio);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getMaxGlobalWriteSize()
/*     */   {
/* 374 */     return this.maxGlobalWriteSize;
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
/*     */   public void setMaxGlobalWriteSize(long maxGlobalWriteSize)
/*     */   {
/* 388 */     if (maxGlobalWriteSize <= 0L) {
/* 389 */       throw new IllegalArgumentException("maxGlobalWriteSize must be positive");
/*     */     }
/* 391 */     this.maxGlobalWriteSize = maxGlobalWriteSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long queuesSize()
/*     */   {
/* 398 */     return this.queuesSize.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void configureChannel(long newWriteLimit, long newReadLimit)
/*     */   {
/* 406 */     this.writeChannelLimit = newWriteLimit;
/* 407 */     this.readChannelLimit = newReadLimit;
/* 408 */     long now = TrafficCounter.milliSecondFromNano();
/* 409 */     for (PerChannel perChannel : this.channelQueues.values()) {
/* 410 */       perChannel.channelTrafficCounter.resetAccounting(now);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getWriteChannelLimit()
/*     */   {
/* 418 */     return this.writeChannelLimit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setWriteChannelLimit(long writeLimit)
/*     */   {
/* 425 */     this.writeChannelLimit = writeLimit;
/* 426 */     long now = TrafficCounter.milliSecondFromNano();
/* 427 */     for (PerChannel perChannel : this.channelQueues.values()) {
/* 428 */       perChannel.channelTrafficCounter.resetAccounting(now);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getReadChannelLimit()
/*     */   {
/* 436 */     return this.readChannelLimit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setReadChannelLimit(long readLimit)
/*     */   {
/* 443 */     this.readChannelLimit = readLimit;
/* 444 */     long now = TrafficCounter.milliSecondFromNano();
/* 445 */     for (PerChannel perChannel : this.channelQueues.values()) {
/* 446 */       perChannel.channelTrafficCounter.resetAccounting(now);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void release()
/*     */   {
/* 454 */     this.trafficCounter.stop();
/*     */   }
/*     */   
/*     */   private PerChannel getOrSetPerChannel(ChannelHandlerContext ctx)
/*     */   {
/* 459 */     Channel channel = ctx.channel();
/* 460 */     Integer key = Integer.valueOf(channel.hashCode());
/* 461 */     PerChannel perChannel = (PerChannel)this.channelQueues.get(key);
/* 462 */     if (perChannel == null) {
/* 463 */       perChannel = new PerChannel();
/* 464 */       perChannel.messagesQueue = new ArrayDeque();
/*     */       
/* 466 */       perChannel.channelTrafficCounter = new TrafficCounter(this, null, "ChannelTC" + ctx.channel().hashCode(), this.checkInterval);
/*     */       
/* 468 */       perChannel.queueSize = 0L;
/* 469 */       perChannel.lastReadTimestamp = TrafficCounter.milliSecondFromNano();
/* 470 */       perChannel.lastWriteTimestamp = perChannel.lastReadTimestamp;
/* 471 */       this.channelQueues.put(key, perChannel);
/*     */     }
/* 473 */     return perChannel;
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 478 */     getOrSetPerChannel(ctx);
/* 479 */     this.trafficCounter.resetCumulativeTime();
/* 480 */     super.handlerAdded(ctx);
/*     */   }
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 485 */     this.trafficCounter.resetCumulativeTime();
/* 486 */     Channel channel = ctx.channel();
/* 487 */     Integer key = Integer.valueOf(channel.hashCode());
/* 488 */     PerChannel perChannel = (PerChannel)this.channelQueues.remove(key);
/* 489 */     if (perChannel != null)
/*     */     {
/* 491 */       synchronized (perChannel) {
/* 492 */         if (channel.isActive()) {
/* 493 */           for (ToSend toSend : perChannel.messagesQueue) {
/* 494 */             long size = calculateSize(toSend.toSend);
/* 495 */             this.trafficCounter.bytesRealWriteFlowControl(size);
/* 496 */             perChannel.channelTrafficCounter.bytesRealWriteFlowControl(size);
/* 497 */             perChannel.queueSize -= size;
/* 498 */             this.queuesSize.addAndGet(-size);
/* 499 */             ctx.write(toSend.toSend, toSend.promise);
/*     */           }
/*     */         } else {
/* 502 */           this.queuesSize.addAndGet(-perChannel.queueSize);
/* 503 */           for (ToSend toSend : perChannel.messagesQueue) {
/* 504 */             if ((toSend.toSend instanceof ByteBuf)) {
/* 505 */               ((ByteBuf)toSend.toSend).release();
/*     */             }
/*     */           }
/*     */         }
/* 509 */         perChannel.messagesQueue.clear();
/*     */       }
/*     */     }
/* 512 */     releaseWriteSuspended(ctx);
/* 513 */     releaseReadSuspended(ctx);
/* 514 */     super.handlerRemoved(ctx);
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/* 519 */     long size = calculateSize(msg);
/* 520 */     long now = TrafficCounter.milliSecondFromNano();
/* 521 */     if (size > 0L)
/*     */     {
/* 523 */       long waitGlobal = this.trafficCounter.readTimeToWait(size, getReadLimit(), this.maxTime, now);
/* 524 */       Integer key = Integer.valueOf(ctx.channel().hashCode());
/* 525 */       PerChannel perChannel = (PerChannel)this.channelQueues.get(key);
/* 526 */       long wait = 0L;
/* 527 */       if (perChannel != null) {
/* 528 */         wait = perChannel.channelTrafficCounter.readTimeToWait(size, this.readChannelLimit, this.maxTime, now);
/* 529 */         if (this.readDeviationActive)
/*     */         {
/*     */ 
/* 532 */           long maxLocalRead = perChannel.channelTrafficCounter.cumulativeReadBytes();
/* 533 */           long maxGlobalRead = this.cumulativeReadBytes.get();
/* 534 */           if (maxLocalRead <= 0L) {
/* 535 */             maxLocalRead = 0L;
/*     */           }
/* 537 */           if (maxGlobalRead < maxLocalRead) {
/* 538 */             maxGlobalRead = maxLocalRead;
/*     */           }
/* 540 */           wait = computeBalancedWait((float)maxLocalRead, (float)maxGlobalRead, wait);
/*     */         }
/*     */       }
/* 543 */       if (wait < waitGlobal) {
/* 544 */         wait = waitGlobal;
/*     */       }
/* 546 */       wait = checkWaitReadTime(ctx, wait, now);
/* 547 */       if (wait >= 10L)
/*     */       {
/*     */ 
/* 550 */         ChannelConfig config = ctx.channel().config();
/* 551 */         if (logger.isDebugEnabled()) {
/* 552 */           logger.debug("Read Suspend: " + wait + ':' + config.isAutoRead() + ':' + isHandlerActive(ctx));
/*     */         }
/*     */         
/* 555 */         if ((config.isAutoRead()) && (isHandlerActive(ctx))) {
/* 556 */           config.setAutoRead(false);
/* 557 */           ctx.attr(READ_SUSPENDED).set(Boolean.valueOf(true));
/*     */           
/*     */ 
/* 560 */           Attribute<Runnable> attr = ctx.attr(REOPEN_TASK);
/* 561 */           Runnable reopenTask = (Runnable)attr.get();
/* 562 */           if (reopenTask == null) {
/* 563 */             reopenTask = new AbstractTrafficShapingHandler.ReopenReadTimerTask(ctx);
/* 564 */             attr.set(reopenTask);
/*     */           }
/* 566 */           ctx.executor().schedule(reopenTask, wait, TimeUnit.MILLISECONDS);
/* 567 */           if (logger.isDebugEnabled()) {
/* 568 */             logger.debug("Suspend final status => " + config.isAutoRead() + ':' + isHandlerActive(ctx) + " will reopened at: " + wait);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 574 */     informReadOperation(ctx, now);
/* 575 */     ctx.fireChannelRead(msg);
/*     */   }
/*     */   
/*     */   protected long checkWaitReadTime(ChannelHandlerContext ctx, long wait, long now)
/*     */   {
/* 580 */     Integer key = Integer.valueOf(ctx.channel().hashCode());
/* 581 */     PerChannel perChannel = (PerChannel)this.channelQueues.get(key);
/* 582 */     if ((perChannel != null) && 
/* 583 */       (wait > this.maxTime) && (now + wait - perChannel.lastReadTimestamp > this.maxTime)) {
/* 584 */       wait = this.maxTime;
/*     */     }
/*     */     
/* 587 */     return wait;
/*     */   }
/*     */   
/*     */   protected void informReadOperation(ChannelHandlerContext ctx, long now)
/*     */   {
/* 592 */     Integer key = Integer.valueOf(ctx.channel().hashCode());
/* 593 */     PerChannel perChannel = (PerChannel)this.channelQueues.get(key);
/* 594 */     if (perChannel != null) {
/* 595 */       perChannel.lastReadTimestamp = now;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ToSend {
/*     */     final long relativeTimeAction;
/*     */     final Object toSend;
/*     */     final ChannelPromise promise;
/*     */     final long size;
/*     */     
/*     */     private ToSend(long delay, Object toSend, long size, ChannelPromise promise) {
/* 606 */       this.relativeTimeAction = delay;
/* 607 */       this.toSend = toSend;
/* 608 */       this.size = size;
/* 609 */       this.promise = promise;
/*     */     }
/*     */   }
/*     */   
/*     */   protected long maximumCumulativeWrittenBytes() {
/* 614 */     return this.cumulativeWrittenBytes.get();
/*     */   }
/*     */   
/*     */   protected long maximumCumulativeReadBytes() {
/* 618 */     return this.cumulativeReadBytes.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<TrafficCounter> channelTrafficCounters()
/*     */   {
/* 626 */     new AbstractCollection()
/*     */     {
/*     */       public Iterator<TrafficCounter> iterator() {
/* 629 */         new Iterator() {
/* 630 */           final Iterator<GlobalChannelTrafficShapingHandler.PerChannel> iter = GlobalChannelTrafficShapingHandler.this.channelQueues.values().iterator();
/*     */           
/*     */           public boolean hasNext() {
/* 633 */             return this.iter.hasNext();
/*     */           }
/*     */           
/*     */           public TrafficCounter next() {
/* 637 */             return ((GlobalChannelTrafficShapingHandler.PerChannel)this.iter.next()).channelTrafficCounter;
/*     */           }
/*     */           
/*     */           public void remove() {
/* 641 */             throw new UnsupportedOperationException();
/*     */           }
/*     */         };
/*     */       }
/*     */       
/*     */       public int size() {
/* 647 */         return GlobalChannelTrafficShapingHandler.this.channelQueues.size();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 655 */     long size = calculateSize(msg);
/* 656 */     long now = TrafficCounter.milliSecondFromNano();
/* 657 */     if (size > 0L)
/*     */     {
/* 659 */       long waitGlobal = this.trafficCounter.writeTimeToWait(size, getWriteLimit(), this.maxTime, now);
/* 660 */       Integer key = Integer.valueOf(ctx.channel().hashCode());
/* 661 */       PerChannel perChannel = (PerChannel)this.channelQueues.get(key);
/* 662 */       long wait = 0L;
/* 663 */       if (perChannel != null) {
/* 664 */         wait = perChannel.channelTrafficCounter.writeTimeToWait(size, this.writeChannelLimit, this.maxTime, now);
/* 665 */         if (this.writeDeviationActive)
/*     */         {
/*     */ 
/* 668 */           long maxLocalWrite = perChannel.channelTrafficCounter.cumulativeWrittenBytes();
/* 669 */           long maxGlobalWrite = this.cumulativeWrittenBytes.get();
/* 670 */           if (maxLocalWrite <= 0L) {
/* 671 */             maxLocalWrite = 0L;
/*     */           }
/* 673 */           if (maxGlobalWrite < maxLocalWrite) {
/* 674 */             maxGlobalWrite = maxLocalWrite;
/*     */           }
/* 676 */           wait = computeBalancedWait((float)maxLocalWrite, (float)maxGlobalWrite, wait);
/*     */         }
/*     */       }
/* 679 */       if (wait < waitGlobal) {
/* 680 */         wait = waitGlobal;
/*     */       }
/* 682 */       if (wait >= 10L) {
/* 683 */         if (logger.isDebugEnabled()) {
/* 684 */           logger.debug("Write suspend: " + wait + ':' + ctx.channel().config().isAutoRead() + ':' + isHandlerActive(ctx));
/*     */         }
/*     */         
/* 687 */         submitWrite(ctx, msg, size, wait, now, promise);
/* 688 */         return;
/*     */       }
/*     */     }
/*     */     
/* 692 */     submitWrite(ctx, msg, size, 0L, now, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void submitWrite(final ChannelHandlerContext ctx, Object msg, long size, long writedelay, long now, ChannelPromise promise)
/*     */   {
/* 699 */     Channel channel = ctx.channel();
/* 700 */     Integer key = Integer.valueOf(channel.hashCode());
/* 701 */     PerChannel perChannel = (PerChannel)this.channelQueues.get(key);
/* 702 */     if (perChannel == null)
/*     */     {
/*     */ 
/* 705 */       perChannel = getOrSetPerChannel(ctx);
/*     */     }
/*     */     
/* 708 */     long delay = writedelay;
/* 709 */     boolean globalSizeExceeded = false;
/*     */     ToSend newToSend;
/* 711 */     synchronized (perChannel) {
/* 712 */       if ((writedelay == 0L) && (perChannel.messagesQueue.isEmpty())) {
/* 713 */         this.trafficCounter.bytesRealWriteFlowControl(size);
/* 714 */         perChannel.channelTrafficCounter.bytesRealWriteFlowControl(size);
/* 715 */         ctx.write(msg, promise);
/* 716 */         perChannel.lastWriteTimestamp = now;
/* 717 */         return;
/*     */       }
/* 719 */       if ((delay > this.maxTime) && (now + delay - perChannel.lastWriteTimestamp > this.maxTime)) {
/* 720 */         delay = this.maxTime;
/*     */       }
/* 722 */       newToSend = new ToSend(delay + now, msg, size, promise, null);
/* 723 */       perChannel.messagesQueue.addLast(newToSend);
/* 724 */       perChannel.queueSize += size;
/* 725 */       this.queuesSize.addAndGet(size);
/* 726 */       checkWriteSuspend(ctx, delay, perChannel.queueSize);
/* 727 */       if (this.queuesSize.get() > this.maxGlobalWriteSize) {
/* 728 */         globalSizeExceeded = true;
/*     */       }
/*     */     }
/* 731 */     if (globalSizeExceeded) {
/* 732 */       setUserDefinedWritability(ctx, false);
/*     */     }
/* 734 */     final long futureNow = newToSend.relativeTimeAction;
/* 735 */     final PerChannel forSchedule = perChannel;
/* 736 */     ctx.executor().schedule(new Runnable()
/*     */     {
/*     */ 
/* 739 */       public void run() { GlobalChannelTrafficShapingHandler.this.sendAllValid(ctx, forSchedule, futureNow); } }, delay, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void sendAllValid(ChannelHandlerContext ctx, PerChannel perChannel, long now)
/*     */   {
/* 746 */     synchronized (perChannel) {
/* 747 */       for (ToSend newToSend = (ToSend)perChannel.messagesQueue.pollFirst(); 
/* 748 */           newToSend != null; newToSend = (ToSend)perChannel.messagesQueue.pollFirst()) {
/* 749 */         if (newToSend.relativeTimeAction <= now) {
/* 750 */           long size = newToSend.size;
/* 751 */           this.trafficCounter.bytesRealWriteFlowControl(size);
/* 752 */           perChannel.channelTrafficCounter.bytesRealWriteFlowControl(size);
/* 753 */           perChannel.queueSize -= size;
/* 754 */           this.queuesSize.addAndGet(-size);
/* 755 */           ctx.write(newToSend.toSend, newToSend.promise);
/* 756 */           perChannel.lastWriteTimestamp = now;
/*     */         } else {
/* 758 */           perChannel.messagesQueue.addFirst(newToSend);
/* 759 */           break;
/*     */         }
/*     */       }
/* 762 */       if (perChannel.messagesQueue.isEmpty()) {
/* 763 */         releaseWriteSuspended(ctx);
/*     */       }
/*     */     }
/* 766 */     ctx.flush();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 771 */     return 340 + super.toString() + " Write Channel Limit: " + this.writeChannelLimit + " Read Channel Limit: " + this.readChannelLimit;
/*     */   }
/*     */   
/*     */   static final class PerChannel
/*     */   {
/*     */     ArrayDeque<GlobalChannelTrafficShapingHandler.ToSend> messagesQueue;
/*     */     TrafficCounter channelTrafficCounter;
/*     */     long queueSize;
/*     */     long lastWriteTimestamp;
/*     */     long lastReadTimestamp;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\traffic\GlobalChannelTrafficShapingHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */