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
/*    */ public class PongWebSocketFrame
/*    */   extends WebSocketFrame
/*    */ {
/*    */   public PongWebSocketFrame()
/*    */   {
/* 30 */     super(Unpooled.buffer(0));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PongWebSocketFrame(ByteBuf binaryData)
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
/*    */   public PongWebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData)
/*    */   {
/* 54 */     super(finalFragment, rsv, binaryData);
/*    */   }
/*    */   
/*    */   public PongWebSocketFrame copy()
/*    */   {
/* 59 */     return new PongWebSocketFrame(isFinalFragment(), rsv(), content().copy());
/*    */   }
/*    */   
/*    */   public PongWebSocketFrame duplicate()
/*    */   {
/* 64 */     return new PongWebSocketFrame(isFinalFragment(), rsv(), content().duplicate());
/*    */   }
/*    */   
/*    */   public PongWebSocketFrame retain()
/*    */   {
/* 69 */     super.retain();
/* 70 */     return this;
/*    */   }
/*    */   
/*    */   public PongWebSocketFrame retain(int increment)
/*    */   {
/* 75 */     super.retain(increment);
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public PongWebSocketFrame touch()
/*    */   {
/* 81 */     super.touch();
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   public PongWebSocketFrame touch(Object hint)
/*    */   {
/* 87 */     super.touch(hint);
/* 88 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\PongWebSocketFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */