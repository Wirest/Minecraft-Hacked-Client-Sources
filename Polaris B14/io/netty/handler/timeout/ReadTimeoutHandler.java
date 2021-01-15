/*     */ package io.netty.handler.timeout;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
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
/*     */ public class ReadTimeoutHandler
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*  64 */   private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
/*     */   
/*     */ 
/*     */   private final long timeoutNanos;
/*     */   
/*     */ 
/*     */   private volatile ScheduledFuture<?> timeout;
/*     */   
/*     */ 
/*     */   private volatile long lastReadTime;
/*     */   
/*     */   private volatile int state;
/*     */   
/*     */   private boolean closed;
/*     */   
/*     */ 
/*     */   public ReadTimeoutHandler(int timeoutSeconds)
/*     */   {
/*  82 */     this(timeoutSeconds, TimeUnit.SECONDS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReadTimeoutHandler(long timeout, TimeUnit unit)
/*     */   {
/*  94 */     if (unit == null) {
/*  95 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/*  98 */     if (timeout <= 0L) {
/*  99 */       this.timeoutNanos = 0L;
/*     */     } else {
/* 101 */       this.timeoutNanos = Math.max(unit.toNanos(timeout), MIN_TIMEOUT_NANOS);
/*     */     }
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 107 */     if ((ctx.channel().isActive()) && (ctx.channel().isRegistered()))
/*     */     {
/*     */ 
/* 110 */       initialize(ctx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void handlerRemoved(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 119 */     destroy();
/*     */   }
/*     */   
/*     */   public void channelRegistered(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 125 */     if (ctx.channel().isActive()) {
/* 126 */       initialize(ctx);
/*     */     }
/* 128 */     super.channelRegistered(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void channelActive(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 136 */     initialize(ctx);
/* 137 */     super.channelActive(ctx);
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 142 */     destroy();
/* 143 */     super.channelInactive(ctx);
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/* 148 */     this.lastReadTime = System.nanoTime();
/* 149 */     ctx.fireChannelRead(msg);
/*     */   }
/*     */   
/*     */ 
/*     */   private void initialize(ChannelHandlerContext ctx)
/*     */   {
/* 155 */     switch (this.state) {
/*     */     case 1: 
/*     */     case 2: 
/* 158 */       return;
/*     */     }
/*     */     
/* 161 */     this.state = 1;
/*     */     
/* 163 */     this.lastReadTime = System.nanoTime();
/* 164 */     if (this.timeoutNanos > 0L) {
/* 165 */       this.timeout = ctx.executor().schedule(new ReadTimeoutTask(ctx), this.timeoutNanos, TimeUnit.NANOSECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void destroy()
/*     */   {
/* 172 */     this.state = 2;
/*     */     
/* 174 */     if (this.timeout != null) {
/* 175 */       this.timeout.cancel(false);
/* 176 */       this.timeout = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void readTimedOut(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 184 */     if (!this.closed) {
/* 185 */       ctx.fireExceptionCaught(ReadTimeoutException.INSTANCE);
/* 186 */       ctx.close();
/* 187 */       this.closed = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class ReadTimeoutTask implements Runnable
/*     */   {
/*     */     private final ChannelHandlerContext ctx;
/*     */     
/*     */     ReadTimeoutTask(ChannelHandlerContext ctx) {
/* 196 */       this.ctx = ctx;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 201 */       if (!this.ctx.channel().isOpen()) {
/* 202 */         return;
/*     */       }
/*     */       
/* 205 */       long currentTime = System.nanoTime();
/* 206 */       long nextDelay = ReadTimeoutHandler.this.timeoutNanos - (currentTime - ReadTimeoutHandler.this.lastReadTime);
/* 207 */       if (nextDelay <= 0L)
/*     */       {
/* 209 */         ReadTimeoutHandler.this.timeout = this.ctx.executor().schedule(this, ReadTimeoutHandler.this.timeoutNanos, TimeUnit.NANOSECONDS);
/*     */         try {
/* 211 */           ReadTimeoutHandler.this.readTimedOut(this.ctx);
/*     */         } catch (Throwable t) {
/* 213 */           this.ctx.fireExceptionCaught(t);
/*     */         }
/*     */       }
/*     */       else {
/* 217 */         ReadTimeoutHandler.this.timeout = this.ctx.executor().schedule(this, nextDelay, TimeUnit.NANOSECONDS);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\timeout\ReadTimeoutHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */