/*     */ package io.netty.handler.traffic;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import java.util.ArrayDeque;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChannelTrafficShapingHandler
/*     */   extends AbstractTrafficShapingHandler
/*     */ {
/*  66 */   private final ArrayDeque<ToSend> messagesQueue = new ArrayDeque();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private long queueSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelTrafficShapingHandler(long writeLimit, long readLimit, long checkInterval, long maxTime)
/*     */   {
/*  84 */     super(writeLimit, readLimit, checkInterval, maxTime);
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
/*     */   public ChannelTrafficShapingHandler(long writeLimit, long readLimit, long checkInterval)
/*     */   {
/* 101 */     super(writeLimit, readLimit, checkInterval);
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
/*     */   public ChannelTrafficShapingHandler(long writeLimit, long readLimit)
/*     */   {
/* 115 */     super(writeLimit, readLimit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelTrafficShapingHandler(long checkInterval)
/*     */   {
/* 127 */     super(checkInterval);
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 132 */     TrafficCounter trafficCounter = new TrafficCounter(this, ctx.executor(), "ChannelTC" + ctx.channel().hashCode(), this.checkInterval);
/*     */     
/* 134 */     setTrafficCounter(trafficCounter);
/* 135 */     trafficCounter.start();
/* 136 */     super.handlerAdded(ctx);
/*     */   }
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 141 */     this.trafficCounter.stop();
/*     */     
/* 143 */     synchronized (this) {
/* 144 */       if (ctx.channel().isActive()) {
/* 145 */         for (ToSend toSend : this.messagesQueue) {
/* 146 */           long size = calculateSize(toSend.toSend);
/* 147 */           this.trafficCounter.bytesRealWriteFlowControl(size);
/* 148 */           this.queueSize -= size;
/* 149 */           ctx.write(toSend.toSend, toSend.promise);
/*     */         }
/*     */       } else {
/* 152 */         for (ToSend toSend : this.messagesQueue) {
/* 153 */           if ((toSend.toSend instanceof ByteBuf)) {
/* 154 */             ((ByteBuf)toSend.toSend).release();
/*     */           }
/*     */         }
/*     */       }
/* 158 */       this.messagesQueue.clear();
/*     */     }
/* 160 */     releaseWriteSuspended(ctx);
/* 161 */     releaseReadSuspended(ctx);
/* 162 */     super.handlerRemoved(ctx);
/*     */   }
/*     */   
/*     */   private static final class ToSend {
/*     */     final long relativeTimeAction;
/*     */     final Object toSend;
/*     */     final ChannelPromise promise;
/*     */     
/*     */     private ToSend(long delay, Object toSend, ChannelPromise promise) {
/* 171 */       this.relativeTimeAction = delay;
/* 172 */       this.toSend = toSend;
/* 173 */       this.promise = promise;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void submitWrite(final ChannelHandlerContext ctx, Object msg, long size, long delay, long now, ChannelPromise promise)
/*     */   {
/*     */     ToSend newToSend;
/*     */     
/* 183 */     synchronized (this) {
/* 184 */       if ((delay == 0L) && (this.messagesQueue.isEmpty())) {
/* 185 */         this.trafficCounter.bytesRealWriteFlowControl(size);
/* 186 */         ctx.write(msg, promise);
/* 187 */         return;
/*     */       }
/* 189 */       newToSend = new ToSend(delay + now, msg, promise, null);
/* 190 */       this.messagesQueue.addLast(newToSend);
/* 191 */       this.queueSize += size;
/* 192 */       checkWriteSuspend(ctx, delay, this.queueSize);
/*     */     }
/* 194 */     final long futureNow = newToSend.relativeTimeAction;
/* 195 */     ctx.executor().schedule(new Runnable()
/*     */     {
/*     */ 
/* 198 */       public void run() { ChannelTrafficShapingHandler.this.sendAllValid(ctx, futureNow); } }, delay, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void sendAllValid(ChannelHandlerContext ctx, long now)
/*     */   {
/* 205 */     synchronized (this) {
/* 206 */       for (ToSend newToSend = (ToSend)this.messagesQueue.pollFirst(); 
/* 207 */           newToSend != null; newToSend = (ToSend)this.messagesQueue.pollFirst()) {
/* 208 */         if (newToSend.relativeTimeAction <= now) {
/* 209 */           long size = calculateSize(newToSend.toSend);
/* 210 */           this.trafficCounter.bytesRealWriteFlowControl(size);
/* 211 */           this.queueSize -= size;
/* 212 */           ctx.write(newToSend.toSend, newToSend.promise);
/*     */         } else {
/* 214 */           this.messagesQueue.addFirst(newToSend);
/* 215 */           break;
/*     */         }
/*     */       }
/* 218 */       if (this.messagesQueue.isEmpty()) {
/* 219 */         releaseWriteSuspended(ctx);
/*     */       }
/*     */     }
/* 222 */     ctx.flush();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long queueSize()
/*     */   {
/* 229 */     return this.queueSize;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\traffic\ChannelTrafficShapingHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */