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
/*    */ class PerMessageDeflateDecoder
/*    */   extends DeflateDecoder
/*    */ {
/*    */   private boolean compressing;
/*    */   
/*    */   public PerMessageDeflateDecoder(boolean noContext)
/*    */   {
/* 39 */     super(noContext);
/*    */   }
/*    */   
/*    */   public boolean acceptInboundMessage(Object msg) throws Exception
/*    */   {
/* 44 */     return ((!(msg instanceof TextWebSocketFrame)) && (!(msg instanceof BinaryWebSocketFrame))) || (((((WebSocketFrame)msg).rsv() & 0x4) > 0) || (((msg instanceof ContinuationWebSocketFrame)) && (this.compressing)));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected int newRsv(WebSocketFrame msg)
/*    */   {
/* 52 */     return (msg.rsv() & 0x4) > 0 ? msg.rsv() ^ 0x4 : msg.rsv();
/*    */   }
/*    */   
/*    */ 
/*    */   protected boolean appendFrameTail(WebSocketFrame msg)
/*    */   {
/* 58 */     return msg.isFinalFragment();
/*    */   }
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out)
/*    */     throws Exception
/*    */   {
/* 64 */     super.decode(ctx, msg, out);
/*    */     
/* 66 */     if (msg.isFinalFragment()) {
/* 67 */       this.compressing = false;
/* 68 */     } else if (((msg instanceof TextWebSocketFrame)) || ((msg instanceof BinaryWebSocketFrame))) {
/* 69 */       this.compressing = true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\PerMessageDeflateDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */