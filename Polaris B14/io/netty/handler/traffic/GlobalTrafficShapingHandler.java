/*     */ package io.netty.handler.traffic;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.ArrayDeque;
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
/*     */ 
/*     */ @ChannelHandler.Sharable
/*     */ public class GlobalTrafficShapingHandler
/*     */   extends AbstractTrafficShapingHandler
/*     */ {
/*  82 */   private final ConcurrentMap<Integer, PerChannel> channelQueues = PlatformDependent.newConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  87 */   private final AtomicLong queuesSize = new AtomicLong();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */   long maxGlobalWriteSize = 419430400L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void createGlobalTrafficCounter(ScheduledExecutorService executor)
/*     */   {
/* 106 */     if (executor == null) {
/* 107 */       throw new NullPointerException("executor");
/*     */     }
/* 109 */     TrafficCounter tc = new TrafficCounter(this, executor, "GlobalTC", this.checkInterval);
/* 110 */     setTrafficCounter(tc);
/* 111 */     tc.start();
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
/*     */   public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long writeLimit, long readLimit, long checkInterval, long maxTime)
/*     */   {
/* 131 */     super(writeLimit, readLimit, checkInterval, maxTime);
/* 132 */     createGlobalTrafficCounter(executor);
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
/*     */   public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long writeLimit, long readLimit, long checkInterval)
/*     */   {
/* 151 */     super(writeLimit, readLimit, checkInterval);
/* 152 */     createGlobalTrafficCounter(executor);
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
/*     */   public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long writeLimit, long readLimit)
/*     */   {
/* 168 */     super(writeLimit, readLimit);
/* 169 */     createGlobalTrafficCounter(executor);
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
/*     */   public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long checkInterval)
/*     */   {
/* 183 */     super(checkInterval);
/* 184 */     createGlobalTrafficCounter(executor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GlobalTrafficShapingHandler(EventExecutor executor)
/*     */   {
/* 195 */     createGlobalTrafficCounter(executor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getMaxGlobalWriteSize()
/*     */   {
/* 202 */     return this.maxGlobalWriteSize;
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
/*     */   public void setMaxGlobalWriteSize(long maxGlobalWriteSize)
/*     */   {
/* 217 */     this.maxGlobalWriteSize = maxGlobalWriteSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long queuesSize()
/*     */   {
/* 224 */     return this.queuesSize.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void release()
/*     */   {
/* 231 */     this.trafficCounter.stop();
/*     */   }
/*     */   
/*     */   private PerChannel getOrSetPerChannel(ChannelHandlerContext ctx)
/*     */   {
/* 236 */     Channel channel = ctx.channel();
/* 237 */     Integer key = Integer.valueOf(channel.hashCode());
/* 238 */     PerChannel perChannel = (PerChannel)this.channelQueues.get(key);
/* 239 */     if (perChannel == null) {
/* 240 */       perChannel = new PerChannel(null);
/* 241 */       perChannel.messagesQueue = new ArrayDeque();
/* 242 */       perChannel.queueSize = 0L;
/* 243 */       perChannel.lastReadTimestamp = TrafficCounter.milliSecondFromNano();
/* 244 */       perChannel.lastWriteTimestamp = perChannel.lastReadTimestamp;
/* 245 */       this.channelQueues.put(key, perChannel);
/*     */     }
/* 247 */     return perChannel;
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 252 */     getOrSetPerChannel(ctx);
/* 253 */     super.handlerAdded(ctx);
/*     */   }
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 258 */     Channel channel = ctx.channel();
/* 259 */     Integer key = Integer.valueOf(channel.hashCode());
/* 260 */     PerChannel perChannel = (PerChannel)this.channelQueues.remove(key);
/* 261 */     if (perChannel != null)
/*     */     {
/* 263 */       synchronized (perChannel) {
/* 264 */         if (channel.isActive()) {
/* 265 */           for (ToSend toSend : perChannel.messagesQueue) {
/* 266 */             long size = calculateSize(toSend.toSend);
/* 267 */             this.trafficCounter.bytesRealWriteFlowControl(size);
/* 268 */             perChannel.queueSize -= size;
/* 269 */             this.queuesSize.addAndGet(-size);
/* 270 */             ctx.write(toSend.toSend, toSend.promise);
/*     */           }
/*     */         } else {
/* 273 */           this.queuesSize.addAndGet(-perChannel.queueSize);
/* 274 */           for (ToSend toSend : perChannel.messagesQueue) {
/* 275 */             if ((toSend.toSend instanceof ByteBuf)) {
/* 276 */               ((ByteBuf)toSend.toSend).release();
/*     */             }
/*     */           }
/*     */         }
/* 280 */         perChannel.messagesQueue.clear();
/*     */       }
/*     */     }
/* 283 */     releaseWriteSuspended(ctx);
/* 284 */     releaseReadSuspended(ctx);
/* 285 */     super.handlerRemoved(ctx);
/*     */   }
/*     */   
/*     */   long checkWaitReadTime(ChannelHandlerContext ctx, long wait, long now)
/*     */   {
/* 290 */     Integer key = Integer.valueOf(ctx.channel().hashCode());
/* 291 */     PerChannel perChannel = (PerChannel)this.channelQueues.get(key);
/* 292 */     if ((perChannel != null) && 
/* 293 */       (wait > this.maxTime) && (now + wait - perChannel.lastReadTimestamp > this.maxTime)) {
/* 294 */       wait = this.maxTime;
/*     */     }
/*     */     
/* 297 */     return wait;
/*     */   }
/*     */   
/*     */   void informReadOperation(ChannelHandlerContext ctx, long now)
/*     */   {
/* 302 */     Integer key = Integer.valueOf(ctx.channel().hashCode());
/* 303 */     PerChannel perChannel = (PerChannel)this.channelQueues.get(key);
/* 304 */     if (perChannel != null) {
/* 305 */       perChannel.lastReadTimestamp = now;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ToSend {
/*     */     final long relativeTimeAction;
/*     */     final Object toSend;
/*     */     final long size;
/*     */     final ChannelPromise promise;
/*     */     
/*     */     private ToSend(long delay, Object toSend, long size, ChannelPromise promise) {
/* 316 */       this.relativeTimeAction = delay;
/* 317 */       this.toSend = toSend;
/* 318 */       this.size = size;
/* 319 */       this.promise = promise;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void submitWrite(final ChannelHandlerContext ctx, Object msg, long size, long writedelay, long now, ChannelPromise promise)
/*     */   {
/* 327 */     Channel channel = ctx.channel();
/* 328 */     Integer key = Integer.valueOf(channel.hashCode());
/* 329 */     PerChannel perChannel = (PerChannel)this.channelQueues.get(key);
/* 330 */     if (perChannel == null)
/*     */     {
/*     */ 
/* 333 */       perChannel = getOrSetPerChannel(ctx);
/*     */     }
/*     */     
/* 336 */     long delay = writedelay;
/* 337 */     boolean globalSizeExceeded = false;
/*     */     ToSend newToSend;
/* 339 */     synchronized (perChannel) {
/* 340 */       if ((writedelay == 0L) && (perChannel.messagesQueue.isEmpty())) {
/* 341 */         this.trafficCounter.bytesRealWriteFlowControl(size);
/* 342 */         ctx.write(msg, promise);
/* 343 */         perChannel.lastWriteTimestamp = now;
/* 344 */         return;
/*     */       }
/* 346 */       if ((delay > this.maxTime) && (now + delay - perChannel.lastWriteTimestamp > this.maxTime)) {
/* 347 */         delay = this.maxTime;
/*     */       }
/* 349 */       newToSend = new ToSend(delay + now, msg, size, promise, null);
/* 350 */       perChannel.messagesQueue.addLast(newToSend);
/* 351 */       perChannel.queueSize += size;
/* 352 */       this.queuesSize.addAndGet(size);
/* 353 */       checkWriteSuspend(ctx, delay, perChannel.queueSize);
/* 354 */       if (this.queuesSize.get() > this.maxGlobalWriteSize) {
/* 355 */         globalSizeExceeded = true;
/*     */       }
/*     */     }
/* 358 */     if (globalSizeExceeded) {
/* 359 */       setUserDefinedWritability(ctx, false);
/*     */     }
/* 361 */     final long futureNow = newToSend.relativeTimeAction;
/* 362 */     final PerChannel forSchedule = perChannel;
/* 363 */     ctx.executor().schedule(new Runnable()
/*     */     {
/*     */ 
/* 366 */       public void run() { GlobalTrafficShapingHandler.this.sendAllValid(ctx, forSchedule, futureNow); } }, delay, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void sendAllValid(ChannelHandlerContext ctx, PerChannel perChannel, long now)
/*     */   {
/* 373 */     synchronized (perChannel) {
/* 374 */       for (ToSend newToSend = (ToSend)perChannel.messagesQueue.pollFirst(); 
/* 375 */           newToSend != null; newToSend = (ToSend)perChannel.messagesQueue.pollFirst()) {
/* 376 */         if (newToSend.relativeTimeAction <= now) {
/* 377 */           long size = newToSend.size;
/* 378 */           this.trafficCounter.bytesRealWriteFlowControl(size);
/* 379 */           perChannel.queueSize -= size;
/* 380 */           this.queuesSize.addAndGet(-size);
/* 381 */           ctx.write(newToSend.toSend, newToSend.promise);
/* 382 */           perChannel.lastWriteTimestamp = now;
/*     */         } else {
/* 384 */           perChannel.messagesQueue.addFirst(newToSend);
/* 385 */           break;
/*     */         }
/*     */       }
/* 388 */       if (perChannel.messagesQueue.isEmpty()) {
/* 389 */         releaseWriteSuspended(ctx);
/*     */       }
/*     */     }
/* 392 */     ctx.flush();
/*     */   }
/*     */   
/*     */   private static final class PerChannel
/*     */   {
/*     */     ArrayDeque<GlobalTrafficShapingHandler.ToSend> messagesQueue;
/*     */     long queueSize;
/*     */     long lastWriteTimestamp;
/*     */     long lastReadTimestamp;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\traffic\GlobalTrafficShapingHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */