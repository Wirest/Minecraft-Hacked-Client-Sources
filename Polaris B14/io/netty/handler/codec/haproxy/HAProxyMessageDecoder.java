/*     */ package io.netty.handler.codec.haproxy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.util.CharsetUtil;
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
/*     */ public class HAProxyMessageDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private static final int V1_MAX_LENGTH = 108;
/*     */   private static final int V2_MAX_LENGTH = 65551;
/*     */   private static final int V2_MIN_LENGTH = 232;
/*     */   private static final int V2_MAX_TLV = 65319;
/*     */   private static final int DELIMITER_LENGTH = 2;
/*  60 */   private static final byte[] BINARY_PREFIX = { 13, 10, 13, 10, 0, 13, 10, 81, 85, 73, 84, 10 };
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
/*  78 */   private static final int BINARY_PREFIX_LENGTH = BINARY_PREFIX.length;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean discarding;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int discardedBytes;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean finished;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  98 */   private int version = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int v2MaxHeaderSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HAProxyMessageDecoder()
/*     */   {
/* 110 */     this.v2MaxHeaderSize = 65551;
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
/*     */   public HAProxyMessageDecoder(int maxTlvSize)
/*     */   {
/* 124 */     if (maxTlvSize < 1) {
/* 125 */       this.v2MaxHeaderSize = 232;
/* 126 */     } else if (maxTlvSize > 65319) {
/* 127 */       this.v2MaxHeaderSize = 65551;
/*     */     } else {
/* 129 */       int calcMax = maxTlvSize + 232;
/* 130 */       if (calcMax > 65551) {
/* 131 */         this.v2MaxHeaderSize = 65551;
/*     */       } else {
/* 133 */         this.v2MaxHeaderSize = calcMax;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int findVersion(ByteBuf buffer)
/*     */   {
/* 143 */     int n = buffer.readableBytes();
/*     */     
/* 145 */     if (n < 13) {
/* 146 */       return -1;
/*     */     }
/*     */     
/* 149 */     int idx = buffer.readerIndex();
/*     */     
/* 151 */     for (int i = 0; i < BINARY_PREFIX_LENGTH; i++) {
/* 152 */       byte b = buffer.getByte(idx + i);
/* 153 */       if (b != BINARY_PREFIX[i]) {
/* 154 */         return 1;
/*     */       }
/*     */     }
/*     */     
/* 158 */     return buffer.getByte(idx + BINARY_PREFIX_LENGTH);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int findEndOfHeader(ByteBuf buffer)
/*     */   {
/* 166 */     int n = buffer.readableBytes();
/*     */     
/*     */ 
/* 169 */     if (n < 16) {
/* 170 */       return -1;
/*     */     }
/*     */     
/* 173 */     int offset = buffer.readerIndex() + 14;
/*     */     
/*     */ 
/* 176 */     int totalHeaderBytes = 16 + buffer.getUnsignedShort(offset);
/*     */     
/*     */ 
/* 179 */     if (n >= totalHeaderBytes) {
/* 180 */       return totalHeaderBytes;
/*     */     }
/* 182 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int findEndOfLine(ByteBuf buffer)
/*     */   {
/* 191 */     int n = buffer.writerIndex();
/* 192 */     for (int i = buffer.readerIndex(); i < n; i++) {
/* 193 */       byte b = buffer.getByte(i);
/* 194 */       if ((b == 13) && (i < n - 1) && (buffer.getByte(i + 1) == 10)) {
/* 195 */         return i;
/*     */       }
/*     */     }
/* 198 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isSingleDecode()
/*     */   {
/* 205 */     return true;
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/* 210 */     super.channelRead(ctx, msg);
/* 211 */     if (this.finished) {
/* 212 */       ctx.pipeline().remove(this);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 219 */     if ((this.version == -1) && 
/* 220 */       ((this.version = findVersion(in)) == -1)) {
/*     */       return;
/*     */     }
/*     */     
/*     */     ByteBuf decoded;
/*     */     
/*     */     ByteBuf decoded;
/* 227 */     if (this.version == 1) {
/* 228 */       decoded = decodeLine(ctx, in);
/*     */     } else {
/* 230 */       decoded = decodeStruct(ctx, in);
/*     */     }
/*     */     
/* 233 */     if (decoded != null) {
/* 234 */       this.finished = true;
/*     */       try {
/* 236 */         if (this.version == 1) {
/* 237 */           out.add(HAProxyMessage.decodeHeader(decoded.toString(CharsetUtil.US_ASCII)));
/*     */         } else {
/* 239 */           out.add(HAProxyMessage.decodeHeader(decoded));
/*     */         }
/*     */       } catch (HAProxyProtocolException e) {
/* 242 */         fail(ctx, null, e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ByteBuf decodeStruct(ChannelHandlerContext ctx, ByteBuf buffer)
/*     */     throws Exception
/*     */   {
/* 257 */     int eoh = findEndOfHeader(buffer);
/* 258 */     if (!this.discarding) {
/* 259 */       if (eoh >= 0) {
/* 260 */         int length = eoh - buffer.readerIndex();
/* 261 */         if (length > this.v2MaxHeaderSize) {
/* 262 */           buffer.readerIndex(eoh);
/* 263 */           failOverLimit(ctx, length);
/* 264 */           return null;
/*     */         }
/* 266 */         return buffer.readSlice(length);
/*     */       }
/* 268 */       int length = buffer.readableBytes();
/* 269 */       if (length > this.v2MaxHeaderSize) {
/* 270 */         this.discardedBytes = length;
/* 271 */         buffer.skipBytes(length);
/* 272 */         this.discarding = true;
/* 273 */         failOverLimit(ctx, "over " + this.discardedBytes);
/*     */       }
/* 275 */       return null;
/*     */     }
/*     */     
/* 278 */     if (eoh >= 0) {
/* 279 */       buffer.readerIndex(eoh);
/* 280 */       this.discardedBytes = 0;
/* 281 */       this.discarding = false;
/*     */     } else {
/* 283 */       this.discardedBytes = buffer.readableBytes();
/* 284 */       buffer.skipBytes(this.discardedBytes);
/*     */     }
/* 286 */     return null;
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
/*     */   private ByteBuf decodeLine(ChannelHandlerContext ctx, ByteBuf buffer)
/*     */     throws Exception
/*     */   {
/* 300 */     int eol = findEndOfLine(buffer);
/* 301 */     if (!this.discarding) {
/* 302 */       if (eol >= 0) {
/* 303 */         int length = eol - buffer.readerIndex();
/* 304 */         if (length > 108) {
/* 305 */           buffer.readerIndex(eol + 2);
/* 306 */           failOverLimit(ctx, length);
/* 307 */           return null;
/*     */         }
/* 309 */         ByteBuf frame = buffer.readSlice(length);
/* 310 */         buffer.skipBytes(2);
/* 311 */         return frame;
/*     */       }
/* 313 */       int length = buffer.readableBytes();
/* 314 */       if (length > 108) {
/* 315 */         this.discardedBytes = length;
/* 316 */         buffer.skipBytes(length);
/* 317 */         this.discarding = true;
/* 318 */         failOverLimit(ctx, "over " + this.discardedBytes);
/*     */       }
/* 320 */       return null;
/*     */     }
/*     */     
/* 323 */     if (eol >= 0) {
/* 324 */       int delimLength = buffer.getByte(eol) == 13 ? 2 : 1;
/* 325 */       buffer.readerIndex(eol + delimLength);
/* 326 */       this.discardedBytes = 0;
/* 327 */       this.discarding = false;
/*     */     } else {
/* 329 */       this.discardedBytes = buffer.readableBytes();
/* 330 */       buffer.skipBytes(this.discardedBytes);
/*     */     }
/* 332 */     return null;
/*     */   }
/*     */   
/*     */   private void failOverLimit(ChannelHandlerContext ctx, int length)
/*     */   {
/* 337 */     failOverLimit(ctx, String.valueOf(length));
/*     */   }
/*     */   
/*     */   private void failOverLimit(ChannelHandlerContext ctx, String length) {
/* 341 */     int maxLength = this.version == 1 ? 108 : this.v2MaxHeaderSize;
/* 342 */     fail(ctx, "header length (" + length + ") exceeds the allowed maximum (" + maxLength + ')', null);
/*     */   }
/*     */   
/*     */   private void fail(ChannelHandlerContext ctx, String errMsg, Throwable t) {
/* 346 */     this.finished = true;
/* 347 */     ctx.close();
/*     */     HAProxyProtocolException ppex;
/* 349 */     HAProxyProtocolException ppex; if ((errMsg != null) && (t != null)) {
/* 350 */       ppex = new HAProxyProtocolException(errMsg, t); } else { HAProxyProtocolException ppex;
/* 351 */       if (errMsg != null) {
/* 352 */         ppex = new HAProxyProtocolException(errMsg); } else { HAProxyProtocolException ppex;
/* 353 */         if (t != null) {
/* 354 */           ppex = new HAProxyProtocolException(t);
/*     */         } else
/* 356 */           ppex = new HAProxyProtocolException();
/*     */       } }
/* 358 */     throw ppex;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\haproxy\HAProxyMessageDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */