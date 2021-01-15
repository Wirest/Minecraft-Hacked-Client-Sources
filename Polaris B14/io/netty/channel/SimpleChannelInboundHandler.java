/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.TypeParameterMatcher;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SimpleChannelInboundHandler<I>
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*     */   private final TypeParameterMatcher matcher;
/*     */   private final boolean autoRelease;
/*     */   
/*     */   protected SimpleChannelInboundHandler()
/*     */   {
/*  57 */     this(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SimpleChannelInboundHandler(boolean autoRelease)
/*     */   {
/*  67 */     this.matcher = TypeParameterMatcher.find(this, SimpleChannelInboundHandler.class, "I");
/*  68 */     this.autoRelease = autoRelease;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected SimpleChannelInboundHandler(Class<? extends I> inboundMessageType)
/*     */   {
/*  75 */     this(inboundMessageType, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SimpleChannelInboundHandler(Class<? extends I> inboundMessageType, boolean autoRelease)
/*     */   {
/*  86 */     this.matcher = TypeParameterMatcher.get(inboundMessageType);
/*  87 */     this.autoRelease = autoRelease;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean acceptInboundMessage(Object msg)
/*     */     throws Exception
/*     */   {
/*  95 */     return this.matcher.match(msg);
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/* 100 */     boolean release = true;
/*     */     try {
/* 102 */       if (acceptInboundMessage(msg))
/*     */       {
/* 104 */         I imsg = (I)msg;
/* 105 */         messageReceived(ctx, imsg);
/*     */       } else {
/* 107 */         release = false;
/* 108 */         ctx.fireChannelRead(msg);
/*     */       }
/*     */     } finally {
/* 111 */       if ((this.autoRelease) && (release)) {
/* 112 */         ReferenceCountUtil.release(msg);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract void messageReceived(ChannelHandlerContext paramChannelHandlerContext, I paramI)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\SimpleChannelInboundHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */