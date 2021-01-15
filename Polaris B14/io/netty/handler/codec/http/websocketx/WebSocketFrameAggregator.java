/*    */ package io.netty.handler.codec.http.websocketx;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.handler.codec.MessageAggregator;
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
/*    */ public class WebSocketFrameAggregator
/*    */   extends MessageAggregator<WebSocketFrame, WebSocketFrame, ContinuationWebSocketFrame, WebSocketFrame>
/*    */ {
/*    */   public WebSocketFrameAggregator(int maxContentLength)
/*    */   {
/* 38 */     super(maxContentLength);
/*    */   }
/*    */   
/*    */   protected boolean isStartMessage(WebSocketFrame msg) throws Exception
/*    */   {
/* 43 */     return ((msg instanceof TextWebSocketFrame)) || ((msg instanceof BinaryWebSocketFrame));
/*    */   }
/*    */   
/*    */   protected boolean isContentMessage(WebSocketFrame msg) throws Exception
/*    */   {
/* 48 */     return msg instanceof ContinuationWebSocketFrame;
/*    */   }
/*    */   
/*    */   protected boolean isLastContentMessage(ContinuationWebSocketFrame msg) throws Exception
/*    */   {
/* 53 */     return (isContentMessage(msg)) && (msg.isFinalFragment());
/*    */   }
/*    */   
/*    */   protected boolean isAggregated(WebSocketFrame msg) throws Exception
/*    */   {
/* 58 */     if (msg.isFinalFragment()) {
/* 59 */       return !isContentMessage(msg);
/*    */     }
/*    */     
/* 62 */     return (!isStartMessage(msg)) && (!isContentMessage(msg));
/*    */   }
/*    */   
/*    */   protected boolean hasContentLength(WebSocketFrame start) throws Exception
/*    */   {
/* 67 */     return false;
/*    */   }
/*    */   
/*    */   protected long contentLength(WebSocketFrame start) throws Exception
/*    */   {
/* 72 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   protected Object newContinueResponse(WebSocketFrame start) throws Exception
/*    */   {
/* 77 */     return null;
/*    */   }
/*    */   
/*    */   protected WebSocketFrame beginAggregation(WebSocketFrame start, ByteBuf content) throws Exception
/*    */   {
/* 82 */     if ((start instanceof TextWebSocketFrame)) {
/* 83 */       return new TextWebSocketFrame(true, start.rsv(), content);
/*    */     }
/*    */     
/* 86 */     if ((start instanceof BinaryWebSocketFrame)) {
/* 87 */       return new BinaryWebSocketFrame(true, start.rsv(), content);
/*    */     }
/*    */     
/*    */ 
/* 91 */     throw new Error();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketFrameAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */