/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.EmptyArrays;
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
/*     */ public class CloseWebSocketFrame
/*     */   extends WebSocketFrame
/*     */ {
/*     */   public CloseWebSocketFrame()
/*     */   {
/*  32 */     super(Unpooled.buffer(0));
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
/*     */   public CloseWebSocketFrame(int statusCode, String reasonText)
/*     */   {
/*  45 */     this(true, 0, statusCode, reasonText);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CloseWebSocketFrame(boolean finalFragment, int rsv)
/*     */   {
/*  57 */     this(finalFragment, rsv, Unpooled.buffer(0));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public CloseWebSocketFrame(boolean finalFragment, int rsv, int statusCode, String reasonText)
/*     */   {
/*  74 */     super(finalFragment, rsv, newBinaryData(statusCode, reasonText));
/*     */   }
/*     */   
/*     */   private static ByteBuf newBinaryData(int statusCode, String reasonText) {
/*  78 */     byte[] reasonBytes = EmptyArrays.EMPTY_BYTES;
/*  79 */     if (reasonText != null) {
/*  80 */       reasonBytes = reasonText.getBytes(CharsetUtil.UTF_8);
/*     */     }
/*     */     
/*  83 */     ByteBuf binaryData = Unpooled.buffer(2 + reasonBytes.length);
/*  84 */     binaryData.writeShort(statusCode);
/*  85 */     if (reasonBytes.length > 0) {
/*  86 */       binaryData.writeBytes(reasonBytes);
/*     */     }
/*     */     
/*  89 */     binaryData.readerIndex(0);
/*  90 */     return binaryData;
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
/*     */   public CloseWebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData)
/*     */   {
/* 104 */     super(finalFragment, rsv, binaryData);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int statusCode()
/*     */   {
/* 112 */     ByteBuf binaryData = content();
/* 113 */     if ((binaryData == null) || (binaryData.capacity() == 0)) {
/* 114 */       return -1;
/*     */     }
/*     */     
/* 117 */     binaryData.readerIndex(0);
/* 118 */     int statusCode = binaryData.readShort();
/* 119 */     binaryData.readerIndex(0);
/*     */     
/* 121 */     return statusCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String reasonText()
/*     */   {
/* 129 */     ByteBuf binaryData = content();
/* 130 */     if ((binaryData == null) || (binaryData.capacity() <= 2)) {
/* 131 */       return "";
/*     */     }
/*     */     
/* 134 */     binaryData.readerIndex(2);
/* 135 */     String reasonText = binaryData.toString(CharsetUtil.UTF_8);
/* 136 */     binaryData.readerIndex(0);
/*     */     
/* 138 */     return reasonText;
/*     */   }
/*     */   
/*     */   public CloseWebSocketFrame copy()
/*     */   {
/* 143 */     return new CloseWebSocketFrame(isFinalFragment(), rsv(), content().copy());
/*     */   }
/*     */   
/*     */   public CloseWebSocketFrame duplicate()
/*     */   {
/* 148 */     return new CloseWebSocketFrame(isFinalFragment(), rsv(), content().duplicate());
/*     */   }
/*     */   
/*     */   public CloseWebSocketFrame retain()
/*     */   {
/* 153 */     super.retain();
/* 154 */     return this;
/*     */   }
/*     */   
/*     */   public CloseWebSocketFrame retain(int increment)
/*     */   {
/* 159 */     super.retain(increment);
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public CloseWebSocketFrame touch()
/*     */   {
/* 165 */     super.touch();
/* 166 */     return this;
/*     */   }
/*     */   
/*     */   public CloseWebSocketFrame touch(Object hint)
/*     */   {
/* 171 */     super.touch(hint);
/* 172 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\CloseWebSocketFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */