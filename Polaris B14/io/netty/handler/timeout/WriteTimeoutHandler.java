/*     */ package io.netty.handler.timeout;
/*     */ 
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
/*     */ public class WriteTimeoutHandler
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*  67 */   private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
/*     */   
/*     */ 
/*     */ 
/*     */   private final long timeoutNanos;
/*     */   
/*     */ 
/*     */   private boolean closed;
/*     */   
/*     */ 
/*     */ 
/*     */   public WriteTimeoutHandler(int timeoutSeconds)
/*     */   {
/*  80 */     this(timeoutSeconds, TimeUnit.SECONDS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WriteTimeoutHandler(long timeout, TimeUnit unit)
/*     */   {
/*  92 */     if (unit == null) {
/*  93 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/*  96 */     if (timeout <= 0L) {
/*  97 */       this.timeoutNanos = 0L;
/*     */     } else {
/*  99 */       this.timeoutNanos = Math.max(unit.toNanos(timeout), MIN_TIMEOUT_NANOS);
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 105 */     if (this.timeoutNanos > 0L) {
/* 106 */       promise = promise.unvoid();
/* 107 */       scheduleTimeout(ctx, promise);
/*     */     }
/* 109 */     ctx.write(msg, promise);
/*     */   }
/*     */   
/*     */   private void scheduleTimeout(final ChannelHandlerContext ctx, final ChannelPromise future)
/*     */   {
/* 114 */     final ScheduledFuture<?> sf = ctx.executor().schedule(new Runnable()
/*     */     {
/*     */ 
/*     */       public void run()
/*     */       {
/*     */ 
/* 120 */         if (!future.isDone())
/*     */           try {
/* 122 */             WriteTimeoutHandler.this.writeTimedOut(ctx);
/*     */           } catch (Throwable t) {
/* 124 */             ctx.fireExceptionCaught(t); } } }, this.timeoutNanos, TimeUnit.NANOSECONDS);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 131 */     future.addListener(new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture future) throws Exception {
/* 134 */         sf.cancel(false);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   protected void writeTimedOut(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 143 */     if (!this.closed) {
/* 144 */       ctx.fireExceptionCaught(WriteTimeoutException.INSTANCE);
/* 145 */       ctx.close();
/* 146 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\timeout\WriteTimeoutHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */