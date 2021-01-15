/*     */ package io.netty.handler.ipfilter;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import java.net.SocketAddress;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractRemoteAddressFilter<T extends SocketAddress>
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*     */   public void channelRegistered(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*  42 */     handleNewChannel(ctx);
/*  43 */     ctx.fireChannelRegistered();
/*     */   }
/*     */   
/*     */   public void channelActive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/*  48 */     if (!handleNewChannel(ctx)) {
/*  49 */       throw new IllegalStateException("cannot determine to accept or reject a channel: " + ctx.channel());
/*     */     }
/*  51 */     ctx.fireChannelActive();
/*     */   }
/*     */   
/*     */   private boolean handleNewChannel(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*  57 */     T remoteAddress = ctx.channel().remoteAddress();
/*     */     
/*     */ 
/*  60 */     if (remoteAddress == null) {
/*  61 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  66 */     ctx.pipeline().remove(this);
/*     */     
/*  68 */     if (accept(ctx, remoteAddress)) {
/*  69 */       channelAccepted(ctx, remoteAddress);
/*     */     } else {
/*  71 */       ChannelFuture rejectedFuture = channelRejected(ctx, remoteAddress);
/*  72 */       if (rejectedFuture != null) {
/*  73 */         rejectedFuture.addListener(ChannelFutureListener.CLOSE);
/*     */       } else {
/*  75 */         ctx.close();
/*     */       }
/*     */     }
/*     */     
/*  79 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean accept(ChannelHandlerContext paramChannelHandlerContext, T paramT)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void channelAccepted(ChannelHandlerContext ctx, T remoteAddress) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ChannelFuture channelRejected(ChannelHandlerContext ctx, T remoteAddress)
/*     */   {
/* 107 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ipfilter\AbstractRemoteAddressFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */