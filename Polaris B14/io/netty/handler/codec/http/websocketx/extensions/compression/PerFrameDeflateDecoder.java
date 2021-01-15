/*    */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*    */ 
/*    */ import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
/*    */ import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
/*    */ import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
/*    */ import io.netty.handler.codec.http.websocketx.WebSocketFrame;
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
/*    */ class PerFrameDeflateDecoder
/*    */   extends DeflateDecoder
/*    */ {
/*    */   public PerFrameDeflateDecoder(boolean noContext)
/*    */   {
/* 34 */     super(noContext);
/*    */   }
/*    */   
/*    */   public boolean acceptInboundMessage(Object msg) throws Exception
/*    */   {
/* 39 */     return (((msg instanceof TextWebSocketFrame)) || ((msg instanceof BinaryWebSocketFrame)) || ((msg instanceof ContinuationWebSocketFrame))) && ((((WebSocketFrame)msg).rsv() & 0x4) > 0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected int newRsv(WebSocketFrame msg)
/*    */   {
/* 47 */     return msg.rsv() ^ 0x4;
/*    */   }
/*    */   
/*    */   protected boolean appendFrameTail(WebSocketFrame msg)
/*    */   {
/* 52 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\PerFrameDeflateDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */