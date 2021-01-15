/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.CharsetUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContinuationWebSocketFrame
/*     */   extends WebSocketFrame
/*     */ {
/*     */   public ContinuationWebSocketFrame()
/*     */   {
/*  32 */     this(Unpooled.buffer(0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContinuationWebSocketFrame(ByteBuf binaryData)
/*     */   {
/*  42 */     super(binaryData);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContinuationWebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData)
/*     */   {
/*  56 */     super(finalFragment, rsv, binaryData);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContinuationWebSocketFrame(boolean finalFragment, int rsv, String text)
/*     */   {
/*  70 */     this(finalFragment, rsv, fromText(text));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String text()
/*     */   {
/*  77 */     return content().toString(CharsetUtil.UTF_8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ByteBuf fromText(String text)
/*     */   {
/*  87 */     if ((text == null) || (text.isEmpty())) {
/*  88 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/*  90 */     return Unpooled.copiedBuffer(text, CharsetUtil.UTF_8);
/*     */   }
/*     */   
/*     */ 
/*     */   public ContinuationWebSocketFrame copy()
/*     */   {
/*  96 */     return new ContinuationWebSocketFrame(isFinalFragment(), rsv(), content().copy());
/*     */   }
/*     */   
/*     */   public ContinuationWebSocketFrame duplicate()
/*     */   {
/* 101 */     return new ContinuationWebSocketFrame(isFinalFragment(), rsv(), content().duplicate());
/*     */   }
/*     */   
/*     */   public ContinuationWebSocketFrame retain()
/*     */   {
/* 106 */     super.retain();
/* 107 */     return this;
/*     */   }
/*     */   
/*     */   public ContinuationWebSocketFrame retain(int increment)
/*     */   {
/* 112 */     super.retain(increment);
/* 113 */     return this;
/*     */   }
/*     */   
/*     */   public ContinuationWebSocketFrame touch()
/*     */   {
/* 118 */     super.touch();
/* 119 */     return this;
/*     */   }
/*     */   
/*     */   public ContinuationWebSocketFrame touch(Object hint)
/*     */   {
/* 124 */     super.touch(hint);
/* 125 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\ContinuationWebSocketFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */