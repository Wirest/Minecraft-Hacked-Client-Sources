/*    */ package io.netty.handler.codec.http.websocketx;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.DefaultByteBufHolder;
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public abstract class WebSocketFrame
/*    */   extends DefaultByteBufHolder
/*    */ {
/*    */   private final boolean finalFragment;
/*    */   private final int rsv;
/*    */   
/*    */   protected WebSocketFrame(ByteBuf binaryData)
/*    */   {
/* 39 */     this(true, 0, binaryData);
/*    */   }
/*    */   
/*    */   protected WebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData) {
/* 43 */     super(binaryData);
/* 44 */     this.finalFragment = finalFragment;
/* 45 */     this.rsv = rsv;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isFinalFragment()
/*    */   {
/* 53 */     return this.finalFragment;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int rsv()
/*    */   {
/* 60 */     return this.rsv;
/*    */   }
/*    */   
/*    */ 
/*    */   public abstract WebSocketFrame copy();
/*    */   
/*    */ 
/*    */   public abstract WebSocketFrame duplicate();
/*    */   
/*    */   public String toString()
/*    */   {
/* 71 */     return StringUtil.simpleClassName(this) + "(data: " + content() + ')';
/*    */   }
/*    */   
/*    */   public WebSocketFrame retain()
/*    */   {
/* 76 */     super.retain();
/* 77 */     return this;
/*    */   }
/*    */   
/*    */   public WebSocketFrame retain(int increment)
/*    */   {
/* 82 */     super.retain(increment);
/* 83 */     return this;
/*    */   }
/*    */   
/*    */   public WebSocketFrame touch()
/*    */   {
/* 88 */     super.touch();
/* 89 */     return this;
/*    */   }
/*    */   
/*    */   public WebSocketFrame touch(Object hint)
/*    */   {
/* 94 */     super.touch(hint);
/* 95 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */