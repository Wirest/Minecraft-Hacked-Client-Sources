/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.Unpooled;
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
/*     */ public class SpdyFrameDecoder
/*     */ {
/*     */   private final int spdyVersion;
/*     */   private final int maxChunkSize;
/*     */   private final SpdyFrameDecoderDelegate delegate;
/*     */   private State state;
/*     */   private byte flags;
/*     */   private int length;
/*     */   private int streamId;
/*     */   private int numSettings;
/*     */   
/*     */   private static enum State
/*     */   {
/*  64 */     READ_COMMON_HEADER, 
/*  65 */     READ_DATA_FRAME, 
/*  66 */     READ_SYN_STREAM_FRAME, 
/*  67 */     READ_SYN_REPLY_FRAME, 
/*  68 */     READ_RST_STREAM_FRAME, 
/*  69 */     READ_SETTINGS_FRAME, 
/*  70 */     READ_SETTING, 
/*  71 */     READ_PING_FRAME, 
/*  72 */     READ_GOAWAY_FRAME, 
/*  73 */     READ_HEADERS_FRAME, 
/*  74 */     READ_WINDOW_UPDATE_FRAME, 
/*  75 */     READ_HEADER_BLOCK, 
/*  76 */     DISCARD_FRAME, 
/*  77 */     FRAME_ERROR;
/*     */     
/*     */ 
/*     */     private State() {}
/*     */   }
/*     */   
/*     */   public SpdyFrameDecoder(SpdyVersion spdyVersion, SpdyFrameDecoderDelegate delegate)
/*     */   {
/*  85 */     this(spdyVersion, delegate, 8192);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SpdyFrameDecoder(SpdyVersion spdyVersion, SpdyFrameDecoderDelegate delegate, int maxChunkSize)
/*     */   {
/*  92 */     if (spdyVersion == null) {
/*  93 */       throw new NullPointerException("spdyVersion");
/*     */     }
/*  95 */     if (delegate == null) {
/*  96 */       throw new NullPointerException("delegate");
/*     */     }
/*  98 */     if (maxChunkSize <= 0) {
/*  99 */       throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + maxChunkSize);
/*     */     }
/*     */     
/* 102 */     this.spdyVersion = spdyVersion.getVersion();
/* 103 */     this.delegate = delegate;
/* 104 */     this.maxChunkSize = maxChunkSize;
/* 105 */     this.state = State.READ_COMMON_HEADER;
/*     */   }
/*     */   
/*     */   public void decode(ByteBuf buffer)
/*     */   {
/*     */     for (;;) {
/*     */       boolean last;
/*     */       int statusCode;
/* 113 */       switch (this.state) {
/*     */       case READ_COMMON_HEADER: 
/* 115 */         if (buffer.readableBytes() < 8) {
/* 116 */           return;
/*     */         }
/*     */         
/* 119 */         int frameOffset = buffer.readerIndex();
/* 120 */         int flagsOffset = frameOffset + 4;
/* 121 */         int lengthOffset = frameOffset + 5;
/* 122 */         buffer.skipBytes(8);
/*     */         
/* 124 */         boolean control = (buffer.getByte(frameOffset) & 0x80) != 0;
/*     */         
/*     */         int version;
/*     */         int type;
/* 128 */         if (control)
/*     */         {
/* 130 */           int version = SpdyCodecUtil.getUnsignedShort(buffer, frameOffset) & 0x7FFF;
/* 131 */           int type = SpdyCodecUtil.getUnsignedShort(buffer, frameOffset + 2);
/* 132 */           this.streamId = 0;
/*     */         }
/*     */         else {
/* 135 */           version = this.spdyVersion;
/* 136 */           type = 0;
/* 137 */           this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, frameOffset);
/*     */         }
/*     */         
/* 140 */         this.flags = buffer.getByte(flagsOffset);
/* 141 */         this.length = SpdyCodecUtil.getUnsignedMedium(buffer, lengthOffset);
/*     */         
/*     */ 
/* 144 */         if (version != this.spdyVersion) {
/* 145 */           this.state = State.FRAME_ERROR;
/* 146 */           this.delegate.readFrameError("Invalid SPDY Version");
/* 147 */         } else if (!isValidFrameHeader(this.streamId, type, this.flags, this.length)) {
/* 148 */           this.state = State.FRAME_ERROR;
/* 149 */           this.delegate.readFrameError("Invalid Frame Error");
/*     */         } else {
/* 151 */           this.state = getNextState(type, this.length);
/*     */         }
/* 153 */         break;
/*     */       
/*     */       case READ_DATA_FRAME: 
/* 156 */         if (this.length == 0) {
/* 157 */           this.state = State.READ_COMMON_HEADER;
/* 158 */           this.delegate.readDataFrame(this.streamId, hasFlag(this.flags, (byte)1), Unpooled.buffer(0));
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 163 */           int dataLength = Math.min(this.maxChunkSize, this.length);
/*     */           
/*     */ 
/* 166 */           if (buffer.readableBytes() < dataLength) {
/* 167 */             return;
/*     */           }
/*     */           
/* 170 */           ByteBuf data = buffer.alloc().buffer(dataLength);
/* 171 */           data.writeBytes(buffer, dataLength);
/* 172 */           this.length -= dataLength;
/*     */           
/* 174 */           if (this.length == 0) {
/* 175 */             this.state = State.READ_COMMON_HEADER;
/*     */           }
/*     */           
/* 178 */           last = (this.length == 0) && (hasFlag(this.flags, (byte)1));
/*     */           
/* 180 */           this.delegate.readDataFrame(this.streamId, last, data); }
/* 181 */         break;
/*     */       
/*     */       case READ_SYN_STREAM_FRAME: 
/* 184 */         if (buffer.readableBytes() < 10) {
/* 185 */           return;
/*     */         }
/*     */         
/* 188 */         int offset = buffer.readerIndex();
/* 189 */         this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, offset);
/* 190 */         int associatedToStreamId = SpdyCodecUtil.getUnsignedInt(buffer, offset + 4);
/* 191 */         byte priority = (byte)(buffer.getByte(offset + 8) >> 5 & 0x7);
/* 192 */         last = hasFlag(this.flags, (byte)1);
/* 193 */         boolean unidirectional = hasFlag(this.flags, (byte)2);
/* 194 */         buffer.skipBytes(10);
/* 195 */         this.length -= 10;
/*     */         
/* 197 */         if (this.streamId == 0) {
/* 198 */           this.state = State.FRAME_ERROR;
/* 199 */           this.delegate.readFrameError("Invalid SYN_STREAM Frame");
/*     */         } else {
/* 201 */           this.state = State.READ_HEADER_BLOCK;
/* 202 */           this.delegate.readSynStreamFrame(this.streamId, associatedToStreamId, priority, last, unidirectional);
/*     */         }
/* 204 */         break;
/*     */       
/*     */       case READ_SYN_REPLY_FRAME: 
/* 207 */         if (buffer.readableBytes() < 4) {
/* 208 */           return;
/*     */         }
/*     */         
/* 211 */         this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
/* 212 */         last = hasFlag(this.flags, (byte)1);
/*     */         
/* 214 */         buffer.skipBytes(4);
/* 215 */         this.length -= 4;
/*     */         
/* 217 */         if (this.streamId == 0) {
/* 218 */           this.state = State.FRAME_ERROR;
/* 219 */           this.delegate.readFrameError("Invalid SYN_REPLY Frame");
/*     */         } else {
/* 221 */           this.state = State.READ_HEADER_BLOCK;
/* 222 */           this.delegate.readSynReplyFrame(this.streamId, last);
/*     */         }
/* 224 */         break;
/*     */       
/*     */       case READ_RST_STREAM_FRAME: 
/* 227 */         if (buffer.readableBytes() < 8) {
/* 228 */           return;
/*     */         }
/*     */         
/* 231 */         this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
/* 232 */         statusCode = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex() + 4);
/* 233 */         buffer.skipBytes(8);
/*     */         
/* 235 */         if ((this.streamId == 0) || (statusCode == 0)) {
/* 236 */           this.state = State.FRAME_ERROR;
/* 237 */           this.delegate.readFrameError("Invalid RST_STREAM Frame");
/*     */         } else {
/* 239 */           this.state = State.READ_COMMON_HEADER;
/* 240 */           this.delegate.readRstStreamFrame(this.streamId, statusCode);
/*     */         }
/* 242 */         break;
/*     */       
/*     */       case READ_SETTINGS_FRAME: 
/* 245 */         if (buffer.readableBytes() < 4) {
/* 246 */           return;
/*     */         }
/*     */         
/* 249 */         boolean clear = hasFlag(this.flags, (byte)1);
/*     */         
/* 251 */         this.numSettings = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
/* 252 */         buffer.skipBytes(4);
/* 253 */         this.length -= 4;
/*     */         
/*     */ 
/* 256 */         if (((this.length & 0x7) != 0) || (this.length >> 3 != this.numSettings)) {
/* 257 */           this.state = State.FRAME_ERROR;
/* 258 */           this.delegate.readFrameError("Invalid SETTINGS Frame");
/*     */         } else {
/* 260 */           this.state = State.READ_SETTING;
/* 261 */           this.delegate.readSettingsFrame(clear);
/*     */         }
/* 263 */         break;
/*     */       
/*     */       case READ_SETTING: 
/* 266 */         if (this.numSettings == 0) {
/* 267 */           this.state = State.READ_COMMON_HEADER;
/* 268 */           this.delegate.readSettingsEnd();
/*     */         }
/*     */         else
/*     */         {
/* 272 */           if (buffer.readableBytes() < 8) {
/* 273 */             return;
/*     */           }
/*     */           
/* 276 */           byte settingsFlags = buffer.getByte(buffer.readerIndex());
/* 277 */           int id = SpdyCodecUtil.getUnsignedMedium(buffer, buffer.readerIndex() + 1);
/* 278 */           int value = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex() + 4);
/* 279 */           boolean persistValue = hasFlag(settingsFlags, (byte)1);
/* 280 */           boolean persisted = hasFlag(settingsFlags, (byte)2);
/* 281 */           buffer.skipBytes(8);
/*     */           
/* 283 */           this.numSettings -= 1;
/*     */           
/* 285 */           this.delegate.readSetting(id, value, persistValue, persisted); }
/* 286 */         break;
/*     */       
/*     */       case READ_PING_FRAME: 
/* 289 */         if (buffer.readableBytes() < 4) {
/* 290 */           return;
/*     */         }
/*     */         
/* 293 */         int pingId = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex());
/* 294 */         buffer.skipBytes(4);
/*     */         
/* 296 */         this.state = State.READ_COMMON_HEADER;
/* 297 */         this.delegate.readPingFrame(pingId);
/* 298 */         break;
/*     */       
/*     */       case READ_GOAWAY_FRAME: 
/* 301 */         if (buffer.readableBytes() < 8) {
/* 302 */           return;
/*     */         }
/*     */         
/* 305 */         int lastGoodStreamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
/* 306 */         statusCode = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex() + 4);
/* 307 */         buffer.skipBytes(8);
/*     */         
/* 309 */         this.state = State.READ_COMMON_HEADER;
/* 310 */         this.delegate.readGoAwayFrame(lastGoodStreamId, statusCode);
/* 311 */         break;
/*     */       
/*     */       case READ_HEADERS_FRAME: 
/* 314 */         if (buffer.readableBytes() < 4) {
/* 315 */           return;
/*     */         }
/*     */         
/* 318 */         this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
/* 319 */         last = hasFlag(this.flags, (byte)1);
/*     */         
/* 321 */         buffer.skipBytes(4);
/* 322 */         this.length -= 4;
/*     */         
/* 324 */         if (this.streamId == 0) {
/* 325 */           this.state = State.FRAME_ERROR;
/* 326 */           this.delegate.readFrameError("Invalid HEADERS Frame");
/*     */         } else {
/* 328 */           this.state = State.READ_HEADER_BLOCK;
/* 329 */           this.delegate.readHeadersFrame(this.streamId, last);
/*     */         }
/* 331 */         break;
/*     */       
/*     */       case READ_WINDOW_UPDATE_FRAME: 
/* 334 */         if (buffer.readableBytes() < 8) {
/* 335 */           return;
/*     */         }
/*     */         
/* 338 */         this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
/* 339 */         int deltaWindowSize = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex() + 4);
/* 340 */         buffer.skipBytes(8);
/*     */         
/* 342 */         if (deltaWindowSize == 0) {
/* 343 */           this.state = State.FRAME_ERROR;
/* 344 */           this.delegate.readFrameError("Invalid WINDOW_UPDATE Frame");
/*     */         } else {
/* 346 */           this.state = State.READ_COMMON_HEADER;
/* 347 */           this.delegate.readWindowUpdateFrame(this.streamId, deltaWindowSize);
/*     */         }
/* 349 */         break;
/*     */       
/*     */       case READ_HEADER_BLOCK: 
/* 352 */         if (this.length == 0) {
/* 353 */           this.state = State.READ_COMMON_HEADER;
/* 354 */           this.delegate.readHeaderBlockEnd();
/*     */         }
/*     */         else
/*     */         {
/* 358 */           if (!buffer.isReadable()) {
/* 359 */             return;
/*     */           }
/*     */           
/* 362 */           int compressedBytes = Math.min(buffer.readableBytes(), this.length);
/* 363 */           ByteBuf headerBlock = buffer.alloc().buffer(compressedBytes);
/* 364 */           headerBlock.writeBytes(buffer, compressedBytes);
/* 365 */           this.length -= compressedBytes;
/*     */           
/* 367 */           this.delegate.readHeaderBlock(headerBlock); }
/* 368 */         break;
/*     */       
/*     */       case DISCARD_FRAME: 
/* 371 */         int numBytes = Math.min(buffer.readableBytes(), this.length);
/* 372 */         buffer.skipBytes(numBytes);
/* 373 */         this.length -= numBytes;
/* 374 */         if (this.length == 0) {
/* 375 */           this.state = State.READ_COMMON_HEADER;
/*     */         }
/*     */         else
/* 378 */           return;
/*     */         break;
/*     */       case FRAME_ERROR: 
/* 381 */         buffer.skipBytes(buffer.readableBytes());
/* 382 */         return;
/*     */       
/*     */       default: 
/* 385 */         throw new Error("Shouldn't reach here.");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean hasFlag(byte flags, byte flag) {
/* 391 */     return (flags & flag) != 0;
/*     */   }
/*     */   
/*     */   private static State getNextState(int type, int length) {
/* 395 */     switch (type) {
/*     */     case 0: 
/* 397 */       return State.READ_DATA_FRAME;
/*     */     
/*     */     case 1: 
/* 400 */       return State.READ_SYN_STREAM_FRAME;
/*     */     
/*     */     case 2: 
/* 403 */       return State.READ_SYN_REPLY_FRAME;
/*     */     
/*     */     case 3: 
/* 406 */       return State.READ_RST_STREAM_FRAME;
/*     */     
/*     */     case 4: 
/* 409 */       return State.READ_SETTINGS_FRAME;
/*     */     
/*     */     case 6: 
/* 412 */       return State.READ_PING_FRAME;
/*     */     
/*     */     case 7: 
/* 415 */       return State.READ_GOAWAY_FRAME;
/*     */     
/*     */     case 8: 
/* 418 */       return State.READ_HEADERS_FRAME;
/*     */     
/*     */     case 9: 
/* 421 */       return State.READ_WINDOW_UPDATE_FRAME;
/*     */     }
/*     */     
/* 424 */     if (length != 0) {
/* 425 */       return State.DISCARD_FRAME;
/*     */     }
/* 427 */     return State.READ_COMMON_HEADER;
/*     */   }
/*     */   
/*     */ 
/*     */   private static boolean isValidFrameHeader(int streamId, int type, byte flags, int length)
/*     */   {
/* 433 */     switch (type) {
/*     */     case 0: 
/* 435 */       return streamId != 0;
/*     */     
/*     */     case 1: 
/* 438 */       return length >= 10;
/*     */     
/*     */     case 2: 
/* 441 */       return length >= 4;
/*     */     
/*     */     case 3: 
/* 444 */       return (flags == 0) && (length == 8);
/*     */     
/*     */     case 4: 
/* 447 */       return length >= 4;
/*     */     
/*     */     case 6: 
/* 450 */       return length == 4;
/*     */     
/*     */     case 7: 
/* 453 */       return length == 8;
/*     */     
/*     */     case 8: 
/* 456 */       return length >= 4;
/*     */     
/*     */     case 9: 
/* 459 */       return length == 8;
/*     */     }
/*     */     
/* 462 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyFrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */