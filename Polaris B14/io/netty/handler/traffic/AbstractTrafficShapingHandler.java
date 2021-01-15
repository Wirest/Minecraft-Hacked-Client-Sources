/*     */ package io.netty.handler.traffic;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.Attribute;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractTrafficShapingHandler
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*  49 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractTrafficShapingHandler.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final long DEFAULT_CHECK_INTERVAL = 1000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final long DEFAULT_MAX_TIME = 15000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final long DEFAULT_MAX_SIZE = 4194304L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final long MINIMAL_WAIT = 10L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TrafficCounter trafficCounter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private volatile long writeLimit;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private volatile long readLimit;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  91 */   protected volatile long maxTime = 15000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  96 */   protected volatile long checkInterval = 1000L;
/*     */   
/*  98 */   static final AttributeKey<Boolean> READ_SUSPENDED = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".READ_SUSPENDED");
/*     */   
/* 100 */   static final AttributeKey<Runnable> REOPEN_TASK = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".REOPEN_TASK");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 106 */   volatile long maxWriteDelay = 4000L;
/*     */   
/*     */ 
/*     */ 
/* 110 */   volatile long maxWriteSize = 4194304L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final int userDefinedWritabilityIndex;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int CHANNEL_DEFAULT_USER_DEFINED_WRITABILITY_INDEX = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int GLOBAL_DEFAULT_USER_DEFINED_WRITABILITY_INDEX = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int GLOBALCHANNEL_DEFAULT_USER_DEFINED_WRITABILITY_INDEX = 3;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void setTrafficCounter(TrafficCounter newTrafficCounter)
/*     */   {
/* 138 */     this.trafficCounter = newTrafficCounter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int userDefinedWritabilityIndex()
/*     */   {
/* 149 */     if ((this instanceof GlobalChannelTrafficShapingHandler))
/* 150 */       return 3;
/* 151 */     if ((this instanceof GlobalTrafficShapingHandler)) {
/* 152 */       return 2;
/*     */     }
/* 154 */     return 1;
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
/*     */   protected AbstractTrafficShapingHandler(long writeLimit, long readLimit, long checkInterval, long maxTime)
/*     */   {
/* 171 */     if (maxTime <= 0L) {
/* 172 */       throw new IllegalArgumentException("maxTime must be positive");
/*     */     }
/*     */     
/* 175 */     this.userDefinedWritabilityIndex = userDefinedWritabilityIndex();
/* 176 */     this.writeLimit = writeLimit;
/* 177 */     this.readLimit = readLimit;
/* 178 */     this.checkInterval = checkInterval;
/* 179 */     this.maxTime = maxTime;
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
/*     */   protected AbstractTrafficShapingHandler(long writeLimit, long readLimit, long checkInterval)
/*     */   {
/* 193 */     this(writeLimit, readLimit, checkInterval, 15000L);
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
/*     */   protected AbstractTrafficShapingHandler(long writeLimit, long readLimit)
/*     */   {
/* 206 */     this(writeLimit, readLimit, 1000L, 15000L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractTrafficShapingHandler()
/*     */   {
/* 214 */     this(0L, 0L, 1000L, 15000L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractTrafficShapingHandler(long checkInterval)
/*     */   {
/* 226 */     this(0L, 0L, checkInterval, 15000L);
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
/*     */   public void configure(long newWriteLimit, long newReadLimit, long newCheckInterval)
/*     */   {
/* 243 */     configure(newWriteLimit, newReadLimit);
/* 244 */     configure(newCheckInterval);
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
/*     */   public void configure(long newWriteLimit, long newReadLimit)
/*     */   {
/* 259 */     this.writeLimit = newWriteLimit;
/* 260 */     this.readLimit = newReadLimit;
/* 261 */     if (this.trafficCounter != null) {
/* 262 */       this.trafficCounter.resetAccounting(TrafficCounter.milliSecondFromNano());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void configure(long newCheckInterval)
/*     */   {
/* 272 */     this.checkInterval = newCheckInterval;
/* 273 */     if (this.trafficCounter != null) {
/* 274 */       this.trafficCounter.configure(this.checkInterval);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getWriteLimit()
/*     */   {
/* 282 */     return this.writeLimit;
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
/*     */   public void setWriteLimit(long writeLimit)
/*     */   {
/* 295 */     this.writeLimit = writeLimit;
/* 296 */     if (this.trafficCounter != null) {
/* 297 */       this.trafficCounter.resetAccounting(TrafficCounter.milliSecondFromNano());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getReadLimit()
/*     */   {
/* 305 */     return this.readLimit;
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
/*     */   public void setReadLimit(long readLimit)
/*     */   {
/* 318 */     this.readLimit = readLimit;
/* 319 */     if (this.trafficCounter != null) {
/* 320 */       this.trafficCounter.resetAccounting(TrafficCounter.milliSecondFromNano());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getCheckInterval()
/*     */   {
/* 328 */     return this.checkInterval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCheckInterval(long checkInterval)
/*     */   {
/* 335 */     this.checkInterval = checkInterval;
/* 336 */     if (this.trafficCounter != null) {
/* 337 */       this.trafficCounter.configure(checkInterval);
/*     */     }
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
/*     */   public void setMaxTimeWait(long maxTime)
/*     */   {
/* 353 */     if (maxTime <= 0L) {
/* 354 */       throw new IllegalArgumentException("maxTime must be positive");
/*     */     }
/* 356 */     this.maxTime = maxTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getMaxTimeWait()
/*     */   {
/* 363 */     return this.maxTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getMaxWriteDelay()
/*     */   {
/* 370 */     return this.maxWriteDelay;
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
/*     */   public void setMaxWriteDelay(long maxWriteDelay)
/*     */   {
/* 384 */     if (maxWriteDelay <= 0L) {
/* 385 */       throw new IllegalArgumentException("maxWriteDelay must be positive");
/*     */     }
/* 387 */     this.maxWriteDelay = maxWriteDelay;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getMaxWriteSize()
/*     */   {
/* 394 */     return this.maxWriteSize;
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
/*     */   public void setMaxWriteSize(long maxWriteSize)
/*     */   {
/* 410 */     this.maxWriteSize = maxWriteSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doAccounting(TrafficCounter counter) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class ReopenReadTimerTask
/*     */     implements Runnable
/*     */   {
/*     */     final ChannelHandlerContext ctx;
/*     */     
/*     */ 
/*     */ 
/*     */     ReopenReadTimerTask(ChannelHandlerContext ctx)
/*     */     {
/* 430 */       this.ctx = ctx;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 435 */       ChannelConfig config = this.ctx.channel().config();
/* 436 */       if ((!config.isAutoRead()) && (AbstractTrafficShapingHandler.isHandlerActive(this.ctx)))
/*     */       {
/*     */ 
/* 439 */         if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
/* 440 */           AbstractTrafficShapingHandler.logger.debug("Not unsuspend: " + config.isAutoRead() + ':' + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
/*     */         }
/*     */         
/* 443 */         this.ctx.attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(Boolean.valueOf(false));
/*     */       }
/*     */       else {
/* 446 */         if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
/* 447 */           if ((config.isAutoRead()) && (!AbstractTrafficShapingHandler.isHandlerActive(this.ctx))) {
/* 448 */             AbstractTrafficShapingHandler.logger.debug("Unsuspend: " + config.isAutoRead() + ':' + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
/*     */           }
/*     */           else {
/* 451 */             AbstractTrafficShapingHandler.logger.debug("Normal unsuspend: " + config.isAutoRead() + ':' + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
/*     */           }
/*     */         }
/*     */         
/* 455 */         this.ctx.attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(Boolean.valueOf(false));
/* 456 */         config.setAutoRead(true);
/* 457 */         this.ctx.channel().read();
/*     */       }
/* 459 */       if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
/* 460 */         AbstractTrafficShapingHandler.logger.debug("Unsupsend final status => " + config.isAutoRead() + ':' + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void releaseReadSuspended(ChannelHandlerContext ctx)
/*     */   {
/* 470 */     ctx.attr(READ_SUSPENDED).set(Boolean.valueOf(false));
/* 471 */     ctx.channel().config().setAutoRead(true);
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/* 476 */     long size = calculateSize(msg);
/* 477 */     long now = TrafficCounter.milliSecondFromNano();
/* 478 */     if (size > 0L)
/*     */     {
/* 480 */       long wait = this.trafficCounter.readTimeToWait(size, this.readLimit, this.maxTime, now);
/* 481 */       wait = checkWaitReadTime(ctx, wait, now);
/* 482 */       if (wait >= 10L)
/*     */       {
/*     */ 
/* 485 */         ChannelConfig config = ctx.channel().config();
/* 486 */         if (logger.isDebugEnabled()) {
/* 487 */           logger.debug("Read suspend: " + wait + ':' + config.isAutoRead() + ':' + isHandlerActive(ctx));
/*     */         }
/*     */         
/* 490 */         if ((config.isAutoRead()) && (isHandlerActive(ctx))) {
/* 491 */           config.setAutoRead(false);
/* 492 */           ctx.attr(READ_SUSPENDED).set(Boolean.valueOf(true));
/*     */           
/*     */ 
/* 495 */           Attribute<Runnable> attr = ctx.attr(REOPEN_TASK);
/* 496 */           Runnable reopenTask = (Runnable)attr.get();
/* 497 */           if (reopenTask == null) {
/* 498 */             reopenTask = new ReopenReadTimerTask(ctx);
/* 499 */             attr.set(reopenTask);
/*     */           }
/* 501 */           ctx.executor().schedule(reopenTask, wait, TimeUnit.MILLISECONDS);
/* 502 */           if (logger.isDebugEnabled()) {
/* 503 */             logger.debug("Suspend final status => " + config.isAutoRead() + ':' + isHandlerActive(ctx) + " will reopened at: " + wait);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 509 */     informReadOperation(ctx, now);
/* 510 */     ctx.fireChannelRead(msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   long checkWaitReadTime(ChannelHandlerContext ctx, long wait, long now)
/*     */   {
/* 521 */     return wait;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void informReadOperation(ChannelHandlerContext ctx, long now) {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected static boolean isHandlerActive(ChannelHandlerContext ctx)
/*     */   {
/* 533 */     Boolean suspended = (Boolean)ctx.attr(READ_SUSPENDED).get();
/* 534 */     return (suspended == null) || (Boolean.FALSE.equals(suspended));
/*     */   }
/*     */   
/*     */   public void read(ChannelHandlerContext ctx)
/*     */   {
/* 539 */     if (isHandlerActive(ctx))
/*     */     {
/* 541 */       ctx.read();
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 548 */     long size = calculateSize(msg);
/* 549 */     long now = TrafficCounter.milliSecondFromNano();
/* 550 */     if (size > 0L)
/*     */     {
/* 552 */       long wait = this.trafficCounter.writeTimeToWait(size, this.writeLimit, this.maxTime, now);
/* 553 */       if (wait >= 10L) {
/* 554 */         if (logger.isDebugEnabled()) {
/* 555 */           logger.debug("Write suspend: " + wait + ':' + ctx.channel().config().isAutoRead() + ':' + isHandlerActive(ctx));
/*     */         }
/*     */         
/* 558 */         submitWrite(ctx, msg, size, wait, now, promise);
/* 559 */         return;
/*     */       }
/*     */     }
/*     */     
/* 563 */     submitWrite(ctx, msg, size, 0L, now, promise);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected void submitWrite(ChannelHandlerContext ctx, Object msg, long delay, ChannelPromise promise)
/*     */   {
/* 569 */     submitWrite(ctx, msg, calculateSize(msg), delay, TrafficCounter.milliSecondFromNano(), promise);
/*     */   }
/*     */   
/*     */ 
/*     */   abstract void submitWrite(ChannelHandlerContext paramChannelHandlerContext, Object paramObject, long paramLong1, long paramLong2, long paramLong3, ChannelPromise paramChannelPromise);
/*     */   
/*     */   public void channelRegistered(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 578 */     setUserDefinedWritability(ctx, true);
/* 579 */     super.channelRegistered(ctx);
/*     */   }
/*     */   
/*     */   void setUserDefinedWritability(ChannelHandlerContext ctx, boolean writable) {
/* 583 */     ChannelOutboundBuffer cob = ctx.channel().unsafe().outboundBuffer();
/* 584 */     if (cob != null) {
/* 585 */       cob.setUserDefinedWritability(this.userDefinedWritabilityIndex, writable);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void checkWriteSuspend(ChannelHandlerContext ctx, long delay, long queueSize)
/*     */   {
/* 596 */     if ((queueSize > this.maxWriteSize) || (delay > this.maxWriteDelay)) {
/* 597 */       setUserDefinedWritability(ctx, false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   void releaseWriteSuspended(ChannelHandlerContext ctx)
/*     */   {
/* 604 */     setUserDefinedWritability(ctx, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TrafficCounter trafficCounter()
/*     */   {
/* 612 */     return this.trafficCounter;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 617 */     StringBuilder builder = new StringBuilder(290).append("TrafficShaping with Write Limit: ").append(this.writeLimit).append(" Read Limit: ").append(this.readLimit).append(" CheckInterval: ").append(this.checkInterval).append(" maxDelay: ").append(this.maxWriteDelay).append(" maxSize: ").append(this.maxWriteSize).append(" and Counter: ");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 624 */     if (this.trafficCounter != null) {
/* 625 */       builder.append(this.trafficCounter);
/*     */     } else {
/* 627 */       builder.append("none");
/*     */     }
/* 629 */     return builder.toString();
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
/*     */   protected long calculateSize(Object msg)
/*     */   {
/* 642 */     if ((msg instanceof ByteBuf)) {
/* 643 */       return ((ByteBuf)msg).readableBytes();
/*     */     }
/* 645 */     if ((msg instanceof ByteBufHolder)) {
/* 646 */       return ((ByteBufHolder)msg).content().readableBytes();
/*     */     }
/* 648 */     return -1L;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\traffic\AbstractTrafficShapingHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */