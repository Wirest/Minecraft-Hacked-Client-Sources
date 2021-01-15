/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.util.internal.logging.InternalLogger;
/*    */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ChannelHandler.Sharable
/*    */ public abstract class ChannelInitializer<C extends Channel>
/*    */   extends ChannelHandlerAdapter
/*    */ {
/* 52 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChannelInitializer.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract void initChannel(C paramC)
/*    */     throws Exception;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final void channelRegistered(ChannelHandlerContext ctx)
/*    */     throws Exception
/*    */   {
/* 66 */     ChannelPipeline pipeline = ctx.pipeline();
/* 67 */     boolean success = false;
/*    */     try {
/* 69 */       initChannel(ctx.channel());
/* 70 */       pipeline.remove(this);
/* 71 */       ctx.fireChannelRegistered();
/* 72 */       success = true;
/*    */     } catch (Throwable t) {
/* 74 */       logger.warn("Failed to initialize a channel. Closing: " + ctx.channel(), t);
/*    */     } finally {
/* 76 */       if (pipeline.context(this) != null) {
/* 77 */         pipeline.remove(this);
/*    */       }
/* 79 */       if (!success) {
/* 80 */         ctx.close();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ChannelInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */