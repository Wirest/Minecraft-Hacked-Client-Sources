/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.handler.codec.CorruptedFrameException;
/*     */ import io.netty.handler.codec.TooLongFrameException;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*     */ public class WebSocket08FrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */   implements WebSocketFrameDecoder
/*     */ {
/*     */   static enum State
/*     */   {
/*  79 */     READING_FIRST, 
/*  80 */     READING_SECOND, 
/*  81 */     READING_SIZE, 
/*  82 */     MASKING_KEY, 
/*  83 */     PAYLOAD, 
/*  84 */     CORRUPT;
/*     */     
/*     */     private State() {} }
/*  87 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocket08FrameDecoder.class);
/*     */   
/*     */   private static final byte OPCODE_CONT = 0;
/*     */   
/*     */   private static final byte OPCODE_TEXT = 1;
/*     */   
/*     */   private static final byte OPCODE_BINARY = 2;
/*     */   private static final byte OPCODE_CLOSE = 8;
/*     */   private static final byte OPCODE_PING = 9;
/*     */   private static final byte OPCODE_PONG = 10;
/*     */   private final long maxFramePayloadLength;
/*     */   private final boolean allowExtensions;
/*     */   private final boolean expectMaskedFrames;
/*     */   private final boolean allowMaskMismatch;
/*     */   private int fragmentedFramesCount;
/*     */   private boolean frameFinalFlag;
/*     */   private boolean frameMasked;
/*     */   private int frameRsv;
/*     */   private int frameOpcode;
/*     */   private long framePayloadLength;
/*     */   private byte[] maskingKey;
/*     */   private int framePayloadLen1;
/*     */   private boolean receivedClosingHandshake;
/* 110 */   private State state = State.READING_FIRST;
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
/*     */   public WebSocket08FrameDecoder(boolean expectMaskedFrames, boolean allowExtensions, int maxFramePayloadLength)
/*     */   {
/* 125 */     this(expectMaskedFrames, allowExtensions, maxFramePayloadLength, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebSocket08FrameDecoder(boolean expectMaskedFrames, boolean allowExtensions, int maxFramePayloadLength, boolean allowMaskMismatch)
/*     */   {
/* 145 */     this.expectMaskedFrames = expectMaskedFrames;
/* 146 */     this.allowMaskMismatch = allowMaskMismatch;
/* 147 */     this.allowExtensions = allowExtensions;
/* 148 */     this.maxFramePayloadLength = maxFramePayloadLength;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 155 */     if (this.receivedClosingHandshake) {
/* 156 */       in.skipBytes(actualReadableBytes()); return;
/*     */     }
/*     */     byte b;
/* 159 */     switch (this.state) {
/*     */     case READING_FIRST: 
/* 161 */       if (!in.isReadable()) {
/* 162 */         return;
/*     */       }
/*     */       
/* 165 */       this.framePayloadLength = 0L;
/*     */       
/*     */ 
/* 168 */       b = in.readByte();
/* 169 */       this.frameFinalFlag = ((b & 0x80) != 0);
/* 170 */       this.frameRsv = ((b & 0x70) >> 4);
/* 171 */       this.frameOpcode = (b & 0xF);
/*     */       
/* 173 */       if (logger.isDebugEnabled()) {
/* 174 */         logger.debug("Decoding WebSocket Frame opCode={}", Integer.valueOf(this.frameOpcode));
/*     */       }
/*     */       
/* 177 */       this.state = State.READING_SECOND;
/*     */     case READING_SECOND: 
/* 179 */       if (!in.isReadable()) {
/* 180 */         return;
/*     */       }
/*     */       
/* 183 */       b = in.readByte();
/* 184 */       this.frameMasked = ((b & 0x80) != 0);
/* 185 */       this.framePayloadLen1 = (b & 0x7F);
/*     */       
/* 187 */       if ((this.frameRsv != 0) && (!this.allowExtensions)) {
/* 188 */         protocolViolation(ctx, "RSV != 0 and no extension negotiated, RSV:" + this.frameRsv);
/* 189 */         return;
/*     */       }
/*     */       
/* 192 */       if ((!this.allowMaskMismatch) && (this.expectMaskedFrames != this.frameMasked)) {
/* 193 */         protocolViolation(ctx, "received a frame that is not masked as expected");
/* 194 */         return;
/*     */       }
/*     */       
/* 197 */       if (this.frameOpcode > 7)
/*     */       {
/*     */ 
/* 200 */         if (!this.frameFinalFlag) {
/* 201 */           protocolViolation(ctx, "fragmented control frame");
/* 202 */           return;
/*     */         }
/*     */         
/*     */ 
/* 206 */         if (this.framePayloadLen1 > 125) {
/* 207 */           protocolViolation(ctx, "control frame with payload length > 125 octets");
/* 208 */           return;
/*     */         }
/*     */         
/*     */ 
/* 212 */         if ((this.frameOpcode != 8) && (this.frameOpcode != 9) && (this.frameOpcode != 10))
/*     */         {
/* 214 */           protocolViolation(ctx, "control frame using reserved opcode " + this.frameOpcode);
/* 215 */           return;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 221 */         if ((this.frameOpcode == 8) && (this.framePayloadLen1 == 1)) {
/* 222 */           protocolViolation(ctx, "received close control frame with payload len 1");
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 227 */         if ((this.frameOpcode != 0) && (this.frameOpcode != 1) && (this.frameOpcode != 2))
/*     */         {
/* 229 */           protocolViolation(ctx, "data frame using reserved opcode " + this.frameOpcode);
/* 230 */           return;
/*     */         }
/*     */         
/*     */ 
/* 234 */         if ((this.fragmentedFramesCount == 0) && (this.frameOpcode == 0)) {
/* 235 */           protocolViolation(ctx, "received continuation data frame outside fragmented message");
/* 236 */           return;
/*     */         }
/*     */         
/*     */ 
/* 240 */         if ((this.fragmentedFramesCount != 0) && (this.frameOpcode != 0) && (this.frameOpcode != 9)) {
/* 241 */           protocolViolation(ctx, "received non-continuation data frame while inside fragmented message");
/*     */           
/* 243 */           return;
/*     */         }
/*     */       }
/*     */       
/* 247 */       this.state = State.READING_SIZE;
/*     */     
/*     */ 
/*     */     case READING_SIZE: 
/* 251 */       if (this.framePayloadLen1 == 126) {
/* 252 */         if (in.readableBytes() < 2) {
/* 253 */           return;
/*     */         }
/* 255 */         this.framePayloadLength = in.readUnsignedShort();
/* 256 */         if (this.framePayloadLength < 126L) {
/* 257 */           protocolViolation(ctx, "invalid data frame length (not using minimal length encoding)");
/*     */         }
/*     */       }
/* 260 */       else if (this.framePayloadLen1 == 127) {
/* 261 */         if (in.readableBytes() < 8) {
/* 262 */           return;
/*     */         }
/* 264 */         this.framePayloadLength = in.readLong();
/*     */         
/*     */ 
/*     */ 
/* 268 */         if (this.framePayloadLength < 65536L) {
/* 269 */           protocolViolation(ctx, "invalid data frame length (not using minimal length encoding)");
/*     */         }
/*     */       }
/*     */       else {
/* 273 */         this.framePayloadLength = this.framePayloadLen1;
/*     */       }
/*     */       
/* 276 */       if (this.framePayloadLength > this.maxFramePayloadLength) {
/* 277 */         protocolViolation(ctx, "Max frame length of " + this.maxFramePayloadLength + " has been exceeded.");
/* 278 */         return;
/*     */       }
/*     */       
/* 281 */       if (logger.isDebugEnabled()) {
/* 282 */         logger.debug("Decoding WebSocket Frame length={}", Long.valueOf(this.framePayloadLength));
/*     */       }
/*     */       
/* 285 */       this.state = State.MASKING_KEY;
/*     */     case MASKING_KEY: 
/* 287 */       if (this.frameMasked) {
/* 288 */         if (in.readableBytes() < 4) {
/* 289 */           return;
/*     */         }
/* 291 */         if (this.maskingKey == null) {
/* 292 */           this.maskingKey = new byte[4];
/*     */         }
/* 294 */         in.readBytes(this.maskingKey);
/*     */       }
/* 296 */       this.state = State.PAYLOAD;
/*     */     case PAYLOAD: 
/* 298 */       if (in.readableBytes() < this.framePayloadLength) {
/* 299 */         return;
/*     */       }
/*     */       
/* 302 */       ByteBuf payloadBuffer = null;
/*     */       try {
/* 304 */         payloadBuffer = ByteBufUtil.readBytes(ctx.alloc(), in, toFrameLength(this.framePayloadLength));
/*     */         
/*     */ 
/*     */ 
/* 308 */         this.state = State.READING_FIRST;
/*     */         
/*     */ 
/* 311 */         if (this.frameMasked) {
/* 312 */           unmask(payloadBuffer);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 317 */         if (this.frameOpcode == 9) {
/* 318 */           out.add(new PingWebSocketFrame(this.frameFinalFlag, this.frameRsv, payloadBuffer));
/* 319 */           payloadBuffer = null; return;
/*     */         }
/*     */         
/* 322 */         if (this.frameOpcode == 10) {
/* 323 */           out.add(new PongWebSocketFrame(this.frameFinalFlag, this.frameRsv, payloadBuffer));
/* 324 */           payloadBuffer = null; return;
/*     */         }
/*     */         
/* 327 */         if (this.frameOpcode == 8) {
/* 328 */           this.receivedClosingHandshake = true;
/* 329 */           checkCloseFrameBody(ctx, payloadBuffer);
/* 330 */           out.add(new CloseWebSocketFrame(this.frameFinalFlag, this.frameRsv, payloadBuffer));
/* 331 */           payloadBuffer = null; return;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 337 */         if (this.frameFinalFlag)
/*     */         {
/*     */ 
/* 340 */           if (this.frameOpcode != 9) {
/* 341 */             this.fragmentedFramesCount = 0;
/*     */           }
/*     */         }
/*     */         else {
/* 345 */           this.fragmentedFramesCount += 1;
/*     */         }
/*     */         
/*     */ 
/* 349 */         if (this.frameOpcode == 1) {
/* 350 */           out.add(new TextWebSocketFrame(this.frameFinalFlag, this.frameRsv, payloadBuffer));
/* 351 */           payloadBuffer = null; return;
/*     */         }
/* 353 */         if (this.frameOpcode == 2) {
/* 354 */           out.add(new BinaryWebSocketFrame(this.frameFinalFlag, this.frameRsv, payloadBuffer));
/* 355 */           payloadBuffer = null; return;
/*     */         }
/* 357 */         if (this.frameOpcode == 0) {
/* 358 */           out.add(new ContinuationWebSocketFrame(this.frameFinalFlag, this.frameRsv, payloadBuffer));
/*     */           
/* 360 */           payloadBuffer = null; return;
/*     */         }
/*     */         
/* 363 */         throw new UnsupportedOperationException("Cannot decode web socket frame with opcode: " + this.frameOpcode);
/*     */       }
/*     */       finally
/*     */       {
/* 367 */         if (payloadBuffer != null) {
/* 368 */           payloadBuffer.release();
/*     */         }
/*     */       }
/*     */     case CORRUPT: 
/* 372 */       if (in.isReadable())
/*     */       {
/*     */ 
/* 375 */         in.readByte();
/*     */       }
/* 377 */       return;
/*     */     }
/* 379 */     throw new Error("Shouldn't reach here.");
/*     */   }
/*     */   
/*     */   private void unmask(ByteBuf frame)
/*     */   {
/* 384 */     int i = frame.readerIndex();
/* 385 */     int end = frame.writerIndex();
/*     */     
/* 387 */     ByteOrder order = frame.order();
/*     */     
/*     */ 
/*     */ 
/* 391 */     int intMask = (this.maskingKey[0] & 0xFF) << 24 | (this.maskingKey[1] & 0xFF) << 16 | (this.maskingKey[2] & 0xFF) << 8 | this.maskingKey[3] & 0xFF;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 398 */     if (order == ByteOrder.LITTLE_ENDIAN) {
/* 399 */       intMask = Integer.reverseBytes(intMask);
/*     */     }
/* 402 */     for (; 
/* 402 */         i + 3 < end; i += 4) {
/* 403 */       int unmasked = frame.getInt(i) ^ intMask;
/* 404 */       frame.setInt(i, unmasked);
/*     */     }
/* 406 */     for (; i < end; i++) {
/* 407 */       frame.setByte(i, frame.getByte(i) ^ this.maskingKey[(i % 4)]);
/*     */     }
/*     */   }
/*     */   
/*     */   private void protocolViolation(ChannelHandlerContext ctx, String reason) {
/* 412 */     protocolViolation(ctx, new CorruptedFrameException(reason));
/*     */   }
/*     */   
/*     */   private void protocolViolation(ChannelHandlerContext ctx, CorruptedFrameException ex) {
/* 416 */     this.state = State.CORRUPT;
/* 417 */     if (ctx.channel().isActive()) { Object closeMessage;
/*     */       Object closeMessage;
/* 419 */       if (this.receivedClosingHandshake) {
/* 420 */         closeMessage = Unpooled.EMPTY_BUFFER;
/*     */       } else {
/* 422 */         closeMessage = new CloseWebSocketFrame(1002, null);
/*     */       }
/* 424 */       ctx.writeAndFlush(closeMessage).addListener(ChannelFutureListener.CLOSE);
/*     */     }
/* 426 */     throw ex;
/*     */   }
/*     */   
/*     */   private static int toFrameLength(long l) {
/* 430 */     if (l > 2147483647L) {
/* 431 */       throw new TooLongFrameException("Length:" + l);
/*     */     }
/* 433 */     return (int)l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void checkCloseFrameBody(ChannelHandlerContext ctx, ByteBuf buffer)
/*     */   {
/* 440 */     if ((buffer == null) || (!buffer.isReadable())) {
/* 441 */       return;
/*     */     }
/* 443 */     if (buffer.readableBytes() == 1) {
/* 444 */       protocolViolation(ctx, "Invalid close frame body");
/*     */     }
/*     */     
/*     */ 
/* 448 */     int idx = buffer.readerIndex();
/* 449 */     buffer.readerIndex(0);
/*     */     
/*     */ 
/* 452 */     int statusCode = buffer.readShort();
/* 453 */     if (((statusCode >= 0) && (statusCode <= 999)) || ((statusCode >= 1004) && (statusCode <= 1006)) || ((statusCode >= 1012) && (statusCode <= 2999)))
/*     */     {
/* 455 */       protocolViolation(ctx, "Invalid close frame getStatus code: " + statusCode);
/*     */     }
/*     */     
/*     */ 
/* 459 */     if (buffer.isReadable()) {
/*     */       try {
/* 461 */         new Utf8Validator().check(buffer);
/*     */       } catch (CorruptedFrameException ex) {
/* 463 */         protocolViolation(ctx, ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 468 */     buffer.readerIndex(idx);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocket08FrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */