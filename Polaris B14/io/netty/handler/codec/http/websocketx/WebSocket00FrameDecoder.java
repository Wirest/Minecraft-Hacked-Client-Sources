/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ReplayingDecoder;
/*     */ import io.netty.handler.codec.TooLongFrameException;
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
/*     */ public class WebSocket00FrameDecoder
/*     */   extends ReplayingDecoder<Void>
/*     */   implements WebSocketFrameDecoder
/*     */ {
/*     */   static final int DEFAULT_MAX_FRAME_SIZE = 16384;
/*     */   private final long maxFrameSize;
/*     */   private boolean receivedClosingHandshake;
/*     */   
/*     */   public WebSocket00FrameDecoder()
/*     */   {
/*  41 */     this(16384);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebSocket00FrameDecoder(int maxFrameSize)
/*     */   {
/*  52 */     this.maxFrameSize = maxFrameSize;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/*  58 */     if (this.receivedClosingHandshake) {
/*  59 */       in.skipBytes(actualReadableBytes());
/*  60 */       return;
/*     */     }
/*     */     
/*     */ 
/*  64 */     byte type = in.readByte();
/*     */     WebSocketFrame frame;
/*  66 */     WebSocketFrame frame; if ((type & 0x80) == 128)
/*     */     {
/*  68 */       frame = decodeBinaryFrame(ctx, type, in);
/*     */     }
/*     */     else {
/*  71 */       frame = decodeTextFrame(ctx, in);
/*     */     }
/*     */     
/*  74 */     if (frame != null) {
/*  75 */       out.add(frame);
/*     */     }
/*     */   }
/*     */   
/*     */   private WebSocketFrame decodeBinaryFrame(ChannelHandlerContext ctx, byte type, ByteBuf buffer) {
/*  80 */     long frameSize = 0L;
/*  81 */     int lengthFieldSize = 0;
/*     */     byte b;
/*     */     do {
/*  84 */       b = buffer.readByte();
/*  85 */       frameSize <<= 7;
/*  86 */       frameSize |= b & 0x7F;
/*  87 */       if (frameSize > this.maxFrameSize) {
/*  88 */         throw new TooLongFrameException();
/*     */       }
/*  90 */       lengthFieldSize++;
/*  91 */       if (lengthFieldSize > 8)
/*     */       {
/*  93 */         throw new TooLongFrameException();
/*     */       }
/*  95 */     } while ((b & 0x80) == 128);
/*     */     
/*  97 */     if ((type == -1) && (frameSize == 0L)) {
/*  98 */       this.receivedClosingHandshake = true;
/*  99 */       return new CloseWebSocketFrame();
/*     */     }
/* 101 */     ByteBuf payload = ByteBufUtil.readBytes(ctx.alloc(), buffer, (int)frameSize);
/* 102 */     return new BinaryWebSocketFrame(payload);
/*     */   }
/*     */   
/*     */   private WebSocketFrame decodeTextFrame(ChannelHandlerContext ctx, ByteBuf buffer) {
/* 106 */     int ridx = buffer.readerIndex();
/* 107 */     int rbytes = actualReadableBytes();
/* 108 */     int delimPos = buffer.indexOf(ridx, ridx + rbytes, (byte)-1);
/* 109 */     if (delimPos == -1)
/*     */     {
/* 111 */       if (rbytes > this.maxFrameSize)
/*     */       {
/* 113 */         throw new TooLongFrameException();
/*     */       }
/*     */       
/* 116 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 120 */     int frameSize = delimPos - ridx;
/* 121 */     if (frameSize > this.maxFrameSize) {
/* 122 */       throw new TooLongFrameException();
/*     */     }
/*     */     
/* 125 */     ByteBuf binaryData = ByteBufUtil.readBytes(ctx.alloc(), buffer, frameSize);
/* 126 */     buffer.skipBytes(1);
/*     */     
/* 128 */     int ffDelimPos = binaryData.indexOf(binaryData.readerIndex(), binaryData.writerIndex(), (byte)-1);
/* 129 */     if (ffDelimPos >= 0) {
/* 130 */       binaryData.release();
/* 131 */       throw new IllegalArgumentException("a text frame should not contain 0xFF.");
/*     */     }
/*     */     
/* 134 */     return new TextWebSocketFrame(binaryData);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocket00FrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */