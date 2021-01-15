/*     */ package io.netty.handler.timeout;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import java.util.concurrent.ScheduledFuture;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IdleStateHandler
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*  98 */   private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
/*     */   
/*     */   private final long readerIdleTimeNanos;
/*     */   
/*     */   private final long writerIdleTimeNanos;
/*     */   private final long allIdleTimeNanos;
/*     */   volatile ScheduledFuture<?> readerIdleTimeout;
/*     */   volatile long lastReadTime;
/* 106 */   private boolean firstReaderIdleEvent = true;
/*     */   
/*     */   volatile ScheduledFuture<?> writerIdleTimeout;
/*     */   volatile long lastWriteTime;
/* 110 */   private boolean firstWriterIdleEvent = true;
/*     */   
/*     */   volatile ScheduledFuture<?> allIdleTimeout;
/* 113 */   private boolean firstAllIdleEvent = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private volatile int state;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IdleStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds)
/*     */   {
/* 138 */     this(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds, TimeUnit.SECONDS);
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
/*     */   public IdleStateHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit)
/*     */   {
/* 164 */     if (unit == null) {
/* 165 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/* 168 */     if (readerIdleTime <= 0L) {
/* 169 */       this.readerIdleTimeNanos = 0L;
/*     */     } else {
/* 171 */       this.readerIdleTimeNanos = Math.max(unit.toNanos(readerIdleTime), MIN_TIMEOUT_NANOS);
/*     */     }
/* 173 */     if (writerIdleTime <= 0L) {
/* 174 */       this.writerIdleTimeNanos = 0L;
/*     */     } else {
/* 176 */       this.writerIdleTimeNanos = Math.max(unit.toNanos(writerIdleTime), MIN_TIMEOUT_NANOS);
/*     */     }
/* 178 */     if (allIdleTime <= 0L) {
/* 179 */       this.allIdleTimeNanos = 0L;
/*     */     } else {
/* 181 */       this.allIdleTimeNanos = Math.max(unit.toNanos(allIdleTime), MIN_TIMEOUT_NANOS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getReaderIdleTimeInMillis()
/*     */   {
/* 190 */     return TimeUnit.NANOSECONDS.toMillis(this.readerIdleTimeNanos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getWriterIdleTimeInMillis()
/*     */   {
/* 198 */     return TimeUnit.NANOSECONDS.toMillis(this.writerIdleTimeNanos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getAllIdleTimeInMillis()
/*     */   {
/* 206 */     return TimeUnit.NANOSECONDS.toMillis(this.allIdleTimeNanos);
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 211 */     if ((ctx.channel().isActive()) && (ctx.channel().isRegistered()))
/*     */     {
/*     */ 
/* 214 */       initialize(ctx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void handlerRemoved(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 223 */     destroy();
/*     */   }
/*     */   
/*     */   public void channelRegistered(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 229 */     if (ctx.channel().isActive()) {
/* 230 */       initialize(ctx);
/*     */     }
/* 232 */     super.channelRegistered(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void channelActive(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 240 */     initialize(ctx);
/* 241 */     super.channelActive(ctx);
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 246 */     destroy();
/* 247 */     super.channelInactive(ctx);
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/* 252 */     this.lastReadTime = System.nanoTime();
/* 253 */     this.firstReaderIdleEvent = (this.firstAllIdleEvent = 1);
/* 254 */     ctx.fireChannelRead(msg);
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 259 */     ChannelPromise unvoid = promise.unvoid();
/* 260 */     unvoid.addListener(new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture future) throws Exception {
/* 263 */         IdleStateHandler.this.lastWriteTime = System.nanoTime();
/* 264 */         IdleStateHandler.this.firstWriterIdleEvent = IdleStateHandler.access$102(IdleStateHandler.this, true);
/*     */       }
/* 266 */     });
/* 267 */     ctx.write(msg, unvoid);
/*     */   }
/*     */   
/*     */ 
/*     */   private void initialize(ChannelHandlerContext ctx)
/*     */   {
/* 273 */     switch (this.state) {
/*     */     case 1: 
/*     */     case 2: 
/* 276 */       return;
/*     */     }
/*     */     
/* 279 */     this.state = 1;
/*     */     
/* 281 */     EventExecutor loop = ctx.executor();
/*     */     
/* 283 */     this.lastReadTime = (this.lastWriteTime = System.nanoTime());
/* 284 */     if (this.readerIdleTimeNanos > 0L) {
/* 285 */       this.readerIdleTimeout = loop.schedule(new ReaderIdleTimeoutTask(ctx), this.readerIdleTimeNanos, TimeUnit.NANOSECONDS);
/*     */     }
/*     */     
/*     */ 
/* 289 */     if (this.writerIdleTimeNanos > 0L) {
/* 290 */       this.writerIdleTimeout = loop.schedule(new WriterIdleTimeoutTask(ctx), this.writerIdleTimeNanos, TimeUnit.NANOSECONDS);
/*     */     }
/*     */     
/*     */ 
/* 294 */     if (this.allIdleTimeNanos > 0L) {
/* 295 */       this.allIdleTimeout = loop.schedule(new AllIdleTimeoutTask(ctx), this.allIdleTimeNanos, TimeUnit.NANOSECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void destroy()
/*     */   {
/* 302 */     this.state = 2;
/*     */     
/* 304 */     if (this.readerIdleTimeout != null) {
/* 305 */       this.readerIdleTimeout.cancel(false);
/* 306 */       this.readerIdleTimeout = null;
/*     */     }
/* 308 */     if (this.writerIdleTimeout != null) {
/* 309 */       this.writerIdleTimeout.cancel(false);
/* 310 */       this.writerIdleTimeout = null;
/*     */     }
/* 312 */     if (this.allIdleTimeout != null) {
/* 313 */       this.allIdleTimeout.cancel(false);
/* 314 */       this.allIdleTimeout = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt)
/*     */     throws Exception
/*     */   {
/* 323 */     ctx.fireUserEventTriggered(evt);
/*     */   }
/*     */   
/*     */   private final class ReaderIdleTimeoutTask implements Runnable
/*     */   {
/*     */     private final ChannelHandlerContext ctx;
/*     */     
/*     */     ReaderIdleTimeoutTask(ChannelHandlerContext ctx) {
/* 331 */       this.ctx = ctx;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 336 */       if (!this.ctx.channel().isOpen()) {
/* 337 */         return;
/*     */       }
/*     */       
/* 340 */       long currentTime = System.nanoTime();
/* 341 */       long lastReadTime = IdleStateHandler.this.lastReadTime;
/* 342 */       long nextDelay = IdleStateHandler.this.readerIdleTimeNanos - (currentTime - lastReadTime);
/* 343 */       if (nextDelay <= 0L)
/*     */       {
/* 345 */         IdleStateHandler.this.readerIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.this.readerIdleTimeNanos, TimeUnit.NANOSECONDS);
/*     */         try {
/*     */           IdleStateEvent event;
/*     */           IdleStateEvent event;
/* 349 */           if (IdleStateHandler.this.firstReaderIdleEvent) {
/* 350 */             IdleStateHandler.this.firstReaderIdleEvent = false;
/* 351 */             event = IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT;
/*     */           } else {
/* 353 */             event = IdleStateEvent.READER_IDLE_STATE_EVENT;
/*     */           }
/* 355 */           IdleStateHandler.this.channelIdle(this.ctx, event);
/*     */         } catch (Throwable t) {
/* 357 */           this.ctx.fireExceptionCaught(t);
/*     */         }
/*     */       }
/*     */       else {
/* 361 */         IdleStateHandler.this.readerIdleTimeout = this.ctx.executor().schedule(this, nextDelay, TimeUnit.NANOSECONDS);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class WriterIdleTimeoutTask implements Runnable
/*     */   {
/*     */     private final ChannelHandlerContext ctx;
/*     */     
/*     */     WriterIdleTimeoutTask(ChannelHandlerContext ctx) {
/* 371 */       this.ctx = ctx;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 376 */       if (!this.ctx.channel().isOpen()) {
/* 377 */         return;
/*     */       }
/*     */       
/* 380 */       long currentTime = System.nanoTime();
/* 381 */       long lastWriteTime = IdleStateHandler.this.lastWriteTime;
/* 382 */       long nextDelay = IdleStateHandler.this.writerIdleTimeNanos - (currentTime - lastWriteTime);
/* 383 */       if (nextDelay <= 0L)
/*     */       {
/* 385 */         IdleStateHandler.this.writerIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.this.writerIdleTimeNanos, TimeUnit.NANOSECONDS);
/*     */         try {
/*     */           IdleStateEvent event;
/*     */           IdleStateEvent event;
/* 389 */           if (IdleStateHandler.this.firstWriterIdleEvent) {
/* 390 */             IdleStateHandler.this.firstWriterIdleEvent = false;
/* 391 */             event = IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT;
/*     */           } else {
/* 393 */             event = IdleStateEvent.WRITER_IDLE_STATE_EVENT;
/*     */           }
/* 395 */           IdleStateHandler.this.channelIdle(this.ctx, event);
/*     */         } catch (Throwable t) {
/* 397 */           this.ctx.fireExceptionCaught(t);
/*     */         }
/*     */       }
/*     */       else {
/* 401 */         IdleStateHandler.this.writerIdleTimeout = this.ctx.executor().schedule(this, nextDelay, TimeUnit.NANOSECONDS);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class AllIdleTimeoutTask implements Runnable
/*     */   {
/*     */     private final ChannelHandlerContext ctx;
/*     */     
/*     */     AllIdleTimeoutTask(ChannelHandlerContext ctx) {
/* 411 */       this.ctx = ctx;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 416 */       if (!this.ctx.channel().isOpen()) {
/* 417 */         return;
/*     */       }
/*     */       
/* 420 */       long currentTime = System.nanoTime();
/* 421 */       long lastIoTime = Math.max(IdleStateHandler.this.lastReadTime, IdleStateHandler.this.lastWriteTime);
/* 422 */       long nextDelay = IdleStateHandler.this.allIdleTimeNanos - (currentTime - lastIoTime);
/* 423 */       if (nextDelay <= 0L)
/*     */       {
/*     */ 
/* 426 */         IdleStateHandler.this.allIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.this.allIdleTimeNanos, TimeUnit.NANOSECONDS);
/*     */         try {
/*     */           IdleStateEvent event;
/*     */           IdleStateEvent event;
/* 430 */           if (IdleStateHandler.this.firstAllIdleEvent) {
/* 431 */             IdleStateHandler.this.firstAllIdleEvent = false;
/* 432 */             event = IdleStateEvent.FIRST_ALL_IDLE_STATE_EVENT;
/*     */           } else {
/* 434 */             event = IdleStateEvent.ALL_IDLE_STATE_EVENT;
/*     */           }
/* 436 */           IdleStateHandler.this.channelIdle(this.ctx, event);
/*     */         } catch (Throwable t) {
/* 438 */           this.ctx.fireExceptionCaught(t);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 443 */         IdleStateHandler.this.allIdleTimeout = this.ctx.executor().schedule(this, nextDelay, TimeUnit.NANOSECONDS);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\timeout\IdleStateHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */