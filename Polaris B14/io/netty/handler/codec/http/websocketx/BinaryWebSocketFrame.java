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
/*    */ public class BinaryWebSocketFrame
/*    */   extends WebSocketFrame
/*    */ {
/*    */   public BinaryWebSocketFrame()
/*    */   {
/* 30 */     super(Unpooled.buffer(0));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BinaryWebSocketFrame(ByteBuf binaryData)
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
/*    */   public BinaryWebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData)
/*    */   {
/* 54 */     super(finalFragment, rsv, binaryData);
/*    */   }
/*    */   
/*    */   public BinaryWebSocketFrame copy()
/*    */   {
/* 59 */     return new BinaryWebSocketFrame(isFinalFragment(), rsv(), content().copy());
/*    */   }
/*    */   
/*    */   public BinaryWebSocketFrame duplicate()
/*    */   {
/* 64 */     return new BinaryWebSocketFrame(isFinalFragment(), rsv(), content().duplicate());
/*    */   }
/*    */   
/*    */   public BinaryWebSocketFrame retain()
/*    */   {
/* 69 */     super.retain();
/* 70 */     return this;
/*    */   }
/*    */   
/*    */   public BinaryWebSocketFrame retain(int increment)
/*    */   {
/* 75 */     super.retain(increment);
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public BinaryWebSocketFrame touch()
/*    */   {
/* 81 */     super.touch();
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   public BinaryWebSocketFrame touch(Object hint)
/*    */   {
/* 87 */     super.touch(hint);
/* 88 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\BinaryWebSocketFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */