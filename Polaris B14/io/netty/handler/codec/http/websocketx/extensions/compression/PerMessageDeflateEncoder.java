/*    */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
/*    */ import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
/*    */ import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
/*    */ import io.netty.handler.codec.http.websocketx.WebSocketFrame;
/*    */ import java.util.List;
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
/*    */ class PerMessageDeflateEncoder
/*    */   extends DeflateEncoder
/*    */ {
/*    */   private boolean compressing;
/*    */   
/*    */   public PerMessageDeflateEncoder(int compressionLevel, int windowSize, boolean noContext)
/*    */   {
/* 41 */     super(compressionLevel, windowSize, noContext);
/*    */   }
/*    */   
/*    */   public boolean acceptOutboundMessage(Object msg) throws Exception
/*    */   {
/* 46 */     return ((!(msg instanceof TextWebSocketFrame)) && (!(msg instanceof BinaryWebSocketFrame))) || (((((WebSocketFrame)msg).rsv() & 0x4) == 0) || (((msg instanceof ContinuationWebSocketFrame)) && (this.compressing)));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected int rsv(WebSocketFrame msg)
/*    */   {
/* 54 */     return ((msg instanceof TextWebSocketFrame)) || ((msg instanceof BinaryWebSocketFrame)) ? msg.rsv() | 0x4 : msg.rsv();
/*    */   }
/*    */   
/*    */ 
/*    */   protected boolean removeFrameTail(WebSocketFrame msg)
/*    */   {
/* 60 */     return msg.isFinalFragment();
/*    */   }
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out)
/*    */     throws Exception
/*    */   {
/* 66 */     super.encode(ctx, msg, out);
/*    */     
/* 68 */     if (msg.isFinalFragment()) {
/* 69 */       this.compressing = false;
/* 70 */     } else if (((msg instanceof TextWebSocketFrame)) || ((msg instanceof BinaryWebSocketFrame))) {
/* 71 */       this.compressing = true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\PerMessageDeflateEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */