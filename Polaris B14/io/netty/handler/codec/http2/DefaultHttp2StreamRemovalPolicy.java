/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.EventLoop;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
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
/*     */ public class DefaultHttp2StreamRemovalPolicy
/*     */   extends ChannelHandlerAdapter
/*     */   implements Http2StreamRemovalPolicy, Runnable
/*     */ {
/*  36 */   private static final long GARBAGE_COLLECTION_INTERVAL = TimeUnit.SECONDS.toNanos(5L);
/*     */   
/*  38 */   public DefaultHttp2StreamRemovalPolicy() { this.garbage = new ArrayDeque(); }
/*     */   
/*     */ 
/*     */ 
/*     */   public void handlerAdded(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*  45 */     this.timerFuture = ctx.channel().eventLoop().scheduleWithFixedDelay(this, GARBAGE_COLLECTION_INTERVAL, GARBAGE_COLLECTION_INTERVAL, TimeUnit.NANOSECONDS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handlerRemoved(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*  54 */     if (this.timerFuture != null) {
/*  55 */       this.timerFuture.cancel(false);
/*  56 */       this.timerFuture = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setAction(Http2StreamRemovalPolicy.Action action)
/*     */   {
/*  62 */     this.action = action;
/*     */   }
/*     */   
/*     */   public void markForRemoval(Http2Stream stream)
/*     */   {
/*  67 */     this.garbage.add(new Garbage(stream));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private final Queue<Garbage> garbage;
/*     */   
/*     */   public void run()
/*     */   {
/*  76 */     if ((this.garbage.isEmpty()) || (this.action == null)) {
/*  77 */       return;
/*     */     }
/*     */     
/*  80 */     long time = System.nanoTime();
/*     */     for (;;) {
/*  82 */       Garbage next = (Garbage)this.garbage.peek();
/*  83 */       if (next == null) {
/*     */         break;
/*     */       }
/*  86 */       if (time - next.removalTime <= GARBAGE_COLLECTION_INTERVAL) break;
/*  87 */       this.garbage.remove();
/*  88 */       this.action.removeStream(next.stream);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private ScheduledFuture<?> timerFuture;
/*     */   
/*     */   private Http2StreamRemovalPolicy.Action action;
/*     */   
/*     */   private static final class Garbage
/*     */   {
/*  99 */     private final long removalTime = System.nanoTime();
/*     */     private final Http2Stream stream;
/*     */     
/*     */     Garbage(Http2Stream stream) {
/* 103 */       this.stream = stream;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\DefaultHttp2StreamRemovalPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */