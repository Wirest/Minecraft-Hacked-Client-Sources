/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelHandlerAppender;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelPipeline;
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
/*    */ public final class HttpServerCodec
/*    */   extends ChannelHandlerAppender
/*    */   implements HttpServerUpgradeHandler.SourceCodec
/*    */ {
/*    */   public HttpServerCodec()
/*    */   {
/* 40 */     this(4096, 8192, 8192);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public HttpServerCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize)
/*    */   {
/* 47 */     super(new ChannelHandler[] { new HttpRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize), new HttpResponseEncoder() });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public HttpServerCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders)
/*    */   {
/* 54 */     super(new ChannelHandler[] { new HttpRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders), new HttpResponseEncoder() });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void upgradeFrom(ChannelHandlerContext ctx)
/*    */   {
/* 64 */     ctx.pipeline().remove(HttpRequestDecoder.class);
/* 65 */     ctx.pipeline().remove(HttpResponseEncoder.class);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public HttpResponseEncoder encoder()
/*    */   {
/* 72 */     return (HttpResponseEncoder)handlerAt(1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public HttpRequestDecoder decoder()
/*    */   {
/* 79 */     return (HttpRequestDecoder)handlerAt(0);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpServerCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */