/*    */ package io.netty.handler.codec.http.websocketx;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
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
/*    */ public class PingWebSocketFrame
/*    */   extends WebSocketFrame
/*    */ {
/*    */   public PingWebSocketFrame()
/*    */   {
/* 30 */     super(true, 0, Unpooled.buffer(0));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PingWebSocketFrame(ByteBuf binaryData)
/*    */   {
/* 40 */     super(binaryData);
/*    */   }
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
/*    */   public PingWebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData)
/*    */   {
/* 54 */     super(finalFragment, rsv, binaryData);
/*    */   }
/*    */   
/*    */   public PingWebSocketFrame copy()
/*    */   {
/* 59 */     return new PingWebSocketFrame(isFinalFragment(), rsv(), content().copy());
/*    */   }
/*    */   
/*    */   public PingWebSocketFrame duplicate()
/*    */   {
/* 64 */     return new PingWebSocketFrame(isFinalFragment(), rsv(), content().duplicate());
/*    */   }
/*    */   
/*    */   public PingWebSocketFrame retain()
/*    */   {
/* 69 */     super.retain();
/* 70 */     return this;
/*    */   }
/*    */   
/*    */   public PingWebSocketFrame retain(int increment)
/*    */   {
/* 75 */     super.retain(increment);
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public PingWebSocketFrame touch()
/*    */   {
/* 81 */     super.touch();
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   public PingWebSocketFrame touch(Object hint)
/*    */   {
/* 87 */     super.touch(hint);
/* 88 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\PingWebSocketFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */