/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.MessageToMessageEncoder;
/*     */ import io.netty.handler.codec.TooLongFrameException;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.List;
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
/*     */ public class WebSocket08FrameEncoder
/*     */   extends MessageToMessageEncoder<WebSocketFrame>
/*     */   implements WebSocketFrameEncoder
/*     */ {
/*  75 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocket08FrameEncoder.class);
/*     */   
/*     */ 
/*     */   private static final byte OPCODE_CONT = 0;
/*     */   
/*     */ 
/*     */   private static final byte OPCODE_TEXT = 1;
/*     */   
/*     */ 
/*     */   private static final byte OPCODE_BINARY = 2;
/*     */   
/*     */ 
/*     */   private static final byte OPCODE_CLOSE = 8;
/*     */   
/*     */ 
/*     */   private static final byte OPCODE_PING = 9;
/*     */   
/*     */ 
/*     */   private static final byte OPCODE_PONG = 10;
/*     */   
/*     */   private static final int GATHERING_WRITE_TRESHOLD = 1024;
/*     */   
/*     */   private final boolean maskPayload;
/*     */   
/*     */ 
/*     */   public WebSocket08FrameEncoder(boolean maskPayload)
/*     */   {
/* 102 */     this.maskPayload = maskPayload;
/*     */   }
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception
/*     */   {
/* 107 */     ByteBuf data = msg.content();
/*     */     
/*     */     byte opcode;
/*     */     
/* 111 */     if ((msg instanceof TextWebSocketFrame)) {
/* 112 */       opcode = 1; } else { byte opcode;
/* 113 */       if ((msg instanceof PingWebSocketFrame)) {
/* 114 */         opcode = 9; } else { byte opcode;
/* 115 */         if ((msg instanceof PongWebSocketFrame)) {
/* 116 */           opcode = 10; } else { byte opcode;
/* 117 */           if ((msg instanceof CloseWebSocketFrame)) {
/* 118 */             opcode = 8; } else { byte opcode;
/* 119 */             if ((msg instanceof BinaryWebSocketFrame)) {
/* 120 */               opcode = 2; } else { byte opcode;
/* 121 */               if ((msg instanceof ContinuationWebSocketFrame)) {
/* 122 */                 opcode = 0;
/*     */               } else
/* 124 */                 throw new UnsupportedOperationException("Cannot encode frame of type: " + msg.getClass().getName());
/*     */             } } } } }
/*     */     byte opcode;
/* 127 */     int length = data.readableBytes();
/*     */     
/* 129 */     if (logger.isDebugEnabled()) {
/* 130 */       logger.debug("Encoding WebSocket Frame opCode=" + opcode + " length=" + length);
/*     */     }
/*     */     
/* 133 */     int b0 = 0;
/* 134 */     if (msg.isFinalFragment()) {
/* 135 */       b0 |= 0x80;
/*     */     }
/* 137 */     b0 |= msg.rsv() % 8 << 4;
/* 138 */     b0 |= opcode % 128;
/*     */     
/* 140 */     if ((opcode == 9) && (length > 125)) {
/* 141 */       throw new TooLongFrameException("invalid payload for PING (payload length must be <= 125, was " + length);
/*     */     }
/*     */     
/*     */ 
/* 145 */     boolean release = true;
/* 146 */     ByteBuf buf = null;
/*     */     try {
/* 148 */       int maskLength = this.maskPayload ? 4 : 0;
/* 149 */       if (length <= 125) {
/* 150 */         int size = 2 + maskLength;
/* 151 */         if ((this.maskPayload) || (length <= 1024)) {
/* 152 */           size += length;
/*     */         }
/* 154 */         buf = ctx.alloc().buffer(size);
/* 155 */         buf.writeByte(b0);
/* 156 */         byte b = (byte)(this.maskPayload ? 0x80 | (byte)length : (byte)length);
/* 157 */         buf.writeByte(b);
/* 158 */       } else if (length <= 65535) {
/* 159 */         int size = 4 + maskLength;
/* 160 */         if ((this.maskPayload) || (length <= 1024)) {
/* 161 */           size += length;
/*     */         }
/* 163 */         buf = ctx.alloc().buffer(size);
/* 164 */         buf.writeByte(b0);
/* 165 */         buf.writeByte(this.maskPayload ? 254 : 126);
/* 166 */         buf.writeByte(length >>> 8 & 0xFF);
/* 167 */         buf.writeByte(length & 0xFF);
/*     */       } else {
/* 169 */         int size = 10 + maskLength;
/* 170 */         if ((this.maskPayload) || (length <= 1024)) {
/* 171 */           size += length;
/*     */         }
/* 173 */         buf = ctx.alloc().buffer(size);
/* 174 */         buf.writeByte(b0);
/* 175 */         buf.writeByte(this.maskPayload ? 255 : 127);
/* 176 */         buf.writeLong(length);
/*     */       }
/*     */       
/*     */ 
/* 180 */       if (this.maskPayload) {
/* 181 */         int random = (int)(Math.random() * 2.147483647E9D);
/* 182 */         byte[] mask = ByteBuffer.allocate(4).putInt(random).array();
/* 183 */         buf.writeBytes(mask);
/*     */         
/* 185 */         ByteOrder srcOrder = data.order();
/* 186 */         ByteOrder dstOrder = buf.order();
/*     */         
/* 188 */         int counter = 0;
/* 189 */         int i = data.readerIndex();
/* 190 */         int end = data.writerIndex();
/*     */         
/* 192 */         if (srcOrder == dstOrder)
/*     */         {
/*     */ 
/*     */ 
/* 196 */           int intMask = (mask[0] & 0xFF) << 24 | (mask[1] & 0xFF) << 16 | (mask[2] & 0xFF) << 8 | mask[3] & 0xFF;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 203 */           if (srcOrder == ByteOrder.LITTLE_ENDIAN) {
/* 204 */             intMask = Integer.reverseBytes(intMask);
/*     */           }
/* 207 */           for (; 
/* 207 */               i + 3 < end; i += 4) {
/* 208 */             int intData = data.getInt(i);
/* 209 */             buf.writeInt(intData ^ intMask);
/*     */           }
/*     */         }
/* 212 */         for (; i < end; i++) {
/* 213 */           byte byteData = data.getByte(i);
/* 214 */           buf.writeByte(byteData ^ mask[(counter++ % 4)]);
/*     */         }
/* 216 */         out.add(buf);
/*     */       }
/* 218 */       else if (buf.writableBytes() >= data.readableBytes())
/*     */       {
/* 220 */         buf.writeBytes(data);
/* 221 */         out.add(buf);
/*     */       } else {
/* 223 */         out.add(buf);
/* 224 */         out.add(data.retain());
/*     */       }
/*     */       
/* 227 */       release = false;
/*     */     } finally {
/* 229 */       if ((release) && (buf != null)) {
/* 230 */         buf.release();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocket08FrameEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */