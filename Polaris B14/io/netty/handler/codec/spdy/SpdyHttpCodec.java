/*    */ package io.netty.handler.codec.spdy;
/*    */ 
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelHandlerAppender;
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
/*    */ public final class SpdyHttpCodec
/*    */   extends ChannelHandlerAppender
/*    */ {
/*    */   public SpdyHttpCodec(SpdyVersion version, int maxContentLength)
/*    */   {
/* 28 */     super(new ChannelHandler[] { new SpdyHttpDecoder(version, maxContentLength), new SpdyHttpEncoder(version) });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public SpdyHttpCodec(SpdyVersion version, int maxContentLength, boolean validateHttpHeaders)
/*    */   {
/* 35 */     super(new ChannelHandler[] { new SpdyHttpDecoder(version, maxContentLength, validateHttpHeaders), new SpdyHttpEncoder(version) });
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyHttpCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */