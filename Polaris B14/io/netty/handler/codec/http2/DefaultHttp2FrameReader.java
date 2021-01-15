/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
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
/*     */ public class DefaultHttp2FrameReader
/*     */   implements Http2FrameReader, Http2FrameSizePolicy, Http2FrameReader.Configuration
/*     */ {
/*     */   private final Http2HeadersDecoder headersDecoder;
/*     */   
/*     */   private static enum State
/*     */   {
/*  51 */     FRAME_HEADER, 
/*  52 */     FRAME_PAYLOAD, 
/*  53 */     ERROR;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */   
/*  58 */   private State state = State.FRAME_HEADER;
/*     */   private byte frameType;
/*     */   private int streamId;
/*     */   private Http2Flags flags;
/*     */   private int payloadLength;
/*     */   private HeadersContinuation headersContinuation;
/*     */   private int maxFrameSize;
/*     */   
/*     */   public DefaultHttp2FrameReader() {
/*  67 */     this(new DefaultHttp2HeadersDecoder());
/*     */   }
/*     */   
/*     */   public DefaultHttp2FrameReader(Http2HeadersDecoder headersDecoder) {
/*  71 */     this.headersDecoder = headersDecoder;
/*  72 */     this.maxFrameSize = 16384;
/*     */   }
/*     */   
/*     */   public Http2HeaderTable headerTable()
/*     */   {
/*  77 */     return this.headersDecoder.configuration().headerTable();
/*     */   }
/*     */   
/*     */   public Http2FrameReader.Configuration configuration()
/*     */   {
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public Http2FrameSizePolicy frameSizePolicy()
/*     */   {
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public void maxFrameSize(int max) throws Http2Exception
/*     */   {
/*  92 */     if (!Http2CodecUtil.isMaxFrameSizeValid(max)) {
/*  93 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Invalid MAX_FRAME_SIZE specified in sent settings: %d", new Object[] { Integer.valueOf(max) });
/*     */     }
/*     */     
/*  96 */     this.maxFrameSize = max;
/*     */   }
/*     */   
/*     */   public int maxFrameSize()
/*     */   {
/* 101 */     return this.maxFrameSize;
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 106 */     if (this.headersContinuation != null) {
/* 107 */       this.headersContinuation.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void readFrame(ChannelHandlerContext ctx, ByteBuf input, Http2FrameListener listener) throws Http2Exception
/*     */   {
/*     */     try
/*     */     {
/* 115 */       while (input.isReadable()) {
/* 116 */         switch (this.state) {
/*     */         case FRAME_HEADER: 
/* 118 */           processHeaderState(input);
/* 119 */           if (this.state == State.FRAME_HEADER)
/*     */           {
/* 121 */             return;
/*     */           }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         case FRAME_PAYLOAD: 
/* 130 */           processPayloadState(ctx, input, listener);
/* 131 */           if (this.state == State.FRAME_PAYLOAD) {
/*     */             return;
/*     */           }
/*     */           
/*     */           break;
/*     */         case ERROR: 
/* 137 */           input.skipBytes(input.readableBytes());
/* 138 */           return;
/*     */         default: 
/* 140 */           throw new IllegalStateException("Should never get here");
/*     */         }
/*     */       }
/*     */     } catch (Http2Exception e) {
/* 144 */       this.state = State.ERROR;
/* 145 */       throw e;
/*     */     } catch (RuntimeException e) {
/* 147 */       this.state = State.ERROR;
/* 148 */       throw e;
/*     */     } catch (Error e) {
/* 150 */       this.state = State.ERROR;
/* 151 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */   private void processHeaderState(ByteBuf in) throws Http2Exception {
/* 156 */     if (in.readableBytes() < 9)
/*     */     {
/* 158 */       return;
/*     */     }
/*     */     
/*     */ 
/* 162 */     this.payloadLength = in.readUnsignedMedium();
/* 163 */     if (this.payloadLength > this.maxFrameSize) {
/* 164 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Frame length: %d exceeds maximum: %d", new Object[] { Integer.valueOf(this.payloadLength), Integer.valueOf(this.maxFrameSize) });
/*     */     }
/* 166 */     this.frameType = in.readByte();
/* 167 */     this.flags = new Http2Flags(in.readUnsignedByte());
/* 168 */     this.streamId = Http2CodecUtil.readUnsignedInt(in);
/*     */     
/* 170 */     switch (this.frameType) {
/*     */     case 0: 
/* 172 */       verifyDataFrame();
/* 173 */       break;
/*     */     case 1: 
/* 175 */       verifyHeadersFrame();
/* 176 */       break;
/*     */     case 2: 
/* 178 */       verifyPriorityFrame();
/* 179 */       break;
/*     */     case 3: 
/* 181 */       verifyRstStreamFrame();
/* 182 */       break;
/*     */     case 4: 
/* 184 */       verifySettingsFrame();
/* 185 */       break;
/*     */     case 5: 
/* 187 */       verifyPushPromiseFrame();
/* 188 */       break;
/*     */     case 6: 
/* 190 */       verifyPingFrame();
/* 191 */       break;
/*     */     case 7: 
/* 193 */       verifyGoAwayFrame();
/* 194 */       break;
/*     */     case 8: 
/* 196 */       verifyWindowUpdateFrame();
/* 197 */       break;
/*     */     case 9: 
/* 199 */       verifyContinuationFrame();
/* 200 */       break;
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 207 */     this.state = State.FRAME_PAYLOAD;
/*     */   }
/*     */   
/*     */   private void processPayloadState(ChannelHandlerContext ctx, ByteBuf in, Http2FrameListener listener) throws Http2Exception
/*     */   {
/* 212 */     if (in.readableBytes() < this.payloadLength)
/*     */     {
/* 214 */       return;
/*     */     }
/*     */     
/*     */ 
/* 218 */     ByteBuf payload = in.readSlice(this.payloadLength);
/*     */     
/*     */ 
/* 221 */     switch (this.frameType) {
/*     */     case 0: 
/* 223 */       readDataFrame(ctx, payload, listener);
/* 224 */       break;
/*     */     case 1: 
/* 226 */       readHeadersFrame(ctx, payload, listener);
/* 227 */       break;
/*     */     case 2: 
/* 229 */       readPriorityFrame(ctx, payload, listener);
/* 230 */       break;
/*     */     case 3: 
/* 232 */       readRstStreamFrame(ctx, payload, listener);
/* 233 */       break;
/*     */     case 4: 
/* 235 */       readSettingsFrame(ctx, payload, listener);
/* 236 */       break;
/*     */     case 5: 
/* 238 */       readPushPromiseFrame(ctx, payload, listener);
/* 239 */       break;
/*     */     case 6: 
/* 241 */       readPingFrame(ctx, payload, listener);
/* 242 */       break;
/*     */     case 7: 
/* 244 */       readGoAwayFrame(ctx, payload, listener);
/* 245 */       break;
/*     */     case 8: 
/* 247 */       readWindowUpdateFrame(ctx, payload, listener);
/* 248 */       break;
/*     */     case 9: 
/* 250 */       readContinuationFrame(payload, listener);
/* 251 */       break;
/*     */     default: 
/* 253 */       readUnknownFrame(ctx, payload, listener);
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 258 */     this.state = State.FRAME_HEADER;
/*     */   }
/*     */   
/*     */   private void verifyDataFrame() throws Http2Exception {
/* 262 */     verifyNotProcessingHeaders();
/* 263 */     verifyPayloadLength(this.payloadLength);
/*     */     
/* 265 */     if (this.payloadLength < this.flags.getPaddingPresenceFieldLength()) {
/* 266 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Frame length %d too small.", new Object[] { Integer.valueOf(this.payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyHeadersFrame() throws Http2Exception
/*     */   {
/* 272 */     verifyNotProcessingHeaders();
/* 273 */     verifyPayloadLength(this.payloadLength);
/*     */     
/* 275 */     int requiredLength = this.flags.getPaddingPresenceFieldLength() + this.flags.getNumPriorityBytes();
/* 276 */     if (this.payloadLength < requiredLength) {
/* 277 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Frame length too small." + this.payloadLength, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyPriorityFrame() throws Http2Exception
/*     */   {
/* 283 */     verifyNotProcessingHeaders();
/*     */     
/* 285 */     if (this.payloadLength != 5) {
/* 286 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Invalid frame length %d.", new Object[] { Integer.valueOf(this.payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyRstStreamFrame() throws Http2Exception
/*     */   {
/* 292 */     verifyNotProcessingHeaders();
/*     */     
/* 294 */     if (this.payloadLength != 4) {
/* 295 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Invalid frame length %d.", new Object[] { Integer.valueOf(this.payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifySettingsFrame() throws Http2Exception
/*     */   {
/* 301 */     verifyNotProcessingHeaders();
/* 302 */     verifyPayloadLength(this.payloadLength);
/* 303 */     if (this.streamId != 0) {
/* 304 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "A stream ID must be zero.", new Object[0]);
/*     */     }
/* 306 */     if ((this.flags.ack()) && (this.payloadLength > 0)) {
/* 307 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Ack settings frame must have an empty payload.", new Object[0]);
/*     */     }
/* 309 */     if (this.payloadLength % 6 > 0) {
/* 310 */       throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Frame length %d invalid.", new Object[] { Integer.valueOf(this.payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyPushPromiseFrame() throws Http2Exception {
/* 315 */     verifyNotProcessingHeaders();
/* 316 */     verifyPayloadLength(this.payloadLength);
/*     */     
/*     */ 
/*     */ 
/* 320 */     int minLength = this.flags.getPaddingPresenceFieldLength() + 4;
/* 321 */     if (this.payloadLength < minLength) {
/* 322 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Frame length %d too small.", new Object[] { Integer.valueOf(this.payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyPingFrame() throws Http2Exception
/*     */   {
/* 328 */     verifyNotProcessingHeaders();
/* 329 */     if (this.streamId != 0) {
/* 330 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "A stream ID must be zero.", new Object[0]);
/*     */     }
/* 332 */     if (this.payloadLength != 8) {
/* 333 */       throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Frame length %d incorrect size for ping.", new Object[] { Integer.valueOf(this.payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyGoAwayFrame() throws Http2Exception
/*     */   {
/* 339 */     verifyNotProcessingHeaders();
/* 340 */     verifyPayloadLength(this.payloadLength);
/*     */     
/* 342 */     if (this.streamId != 0) {
/* 343 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "A stream ID must be zero.", new Object[0]);
/*     */     }
/* 345 */     if (this.payloadLength < 8) {
/* 346 */       throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Frame length %d too small.", new Object[] { Integer.valueOf(this.payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyWindowUpdateFrame() throws Http2Exception {
/* 351 */     verifyNotProcessingHeaders();
/* 352 */     verifyStreamOrConnectionId(this.streamId, "Stream ID");
/*     */     
/* 354 */     if (this.payloadLength != 4) {
/* 355 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Invalid frame length %d.", new Object[] { Integer.valueOf(this.payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyContinuationFrame() throws Http2Exception
/*     */   {
/* 361 */     verifyPayloadLength(this.payloadLength);
/*     */     
/* 363 */     if (this.headersContinuation == null) {
/* 364 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Received %s frame but not currently processing headers.", new Object[] { Byte.valueOf(this.frameType) });
/*     */     }
/*     */     
/*     */ 
/* 368 */     if (this.streamId != this.headersContinuation.getStreamId()) {
/* 369 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Continuation stream ID does not match pending headers. Expected %d, but received %d.", new Object[] { Integer.valueOf(this.headersContinuation.getStreamId()), Integer.valueOf(this.streamId) });
/*     */     }
/*     */     
/*     */ 
/* 373 */     if (this.payloadLength < this.flags.getPaddingPresenceFieldLength()) {
/* 374 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Frame length %d too small for padding.", new Object[] { Integer.valueOf(this.payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void readDataFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener)
/*     */     throws Http2Exception
/*     */   {
/* 381 */     short padding = readPadding(payload);
/*     */     
/*     */ 
/*     */ 
/* 385 */     int dataLength = payload.readableBytes() - padding;
/* 386 */     if (dataLength < 0) {
/* 387 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Frame payload too small for padding.", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/* 391 */     ByteBuf data = payload.readSlice(dataLength);
/* 392 */     listener.onDataRead(ctx, this.streamId, data, padding, this.flags.endOfStream());
/* 393 */     payload.skipBytes(payload.readableBytes());
/*     */   }
/*     */   
/*     */   private void readHeadersFrame(final ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception
/*     */   {
/* 398 */     final int headersStreamId = this.streamId;
/* 399 */     final Http2Flags headersFlags = this.flags;
/* 400 */     final int padding = readPadding(payload);
/*     */     
/*     */ 
/*     */ 
/* 404 */     if (this.flags.priorityPresent()) {
/* 405 */       long word1 = payload.readUnsignedInt();
/* 406 */       final boolean exclusive = (word1 & 0x80000000) != 0L;
/* 407 */       final int streamDependency = (int)(word1 & 0x7FFFFFFF);
/* 408 */       final short weight = (short)(payload.readUnsignedByte() + 1);
/* 409 */       ByteBuf fragment = payload.readSlice(payload.readableBytes() - padding);
/*     */       
/*     */ 
/* 412 */       this.headersContinuation = new HeadersContinuation(headersStreamId, ctx)
/*     */       {
/*     */         public int getStreamId() {
/* 415 */           return headersStreamId;
/*     */         }
/*     */         
/*     */         public void processFragment(boolean endOfHeaders, ByteBuf fragment, Http2FrameListener listener)
/*     */           throws Http2Exception
/*     */         {
/* 421 */           DefaultHttp2FrameReader.HeadersBlockBuilder hdrBlockBuilder = headersBlockBuilder();
/* 422 */           hdrBlockBuilder.addFragment(fragment, ctx.alloc(), endOfHeaders);
/* 423 */           if (endOfHeaders) {
/* 424 */             listener.onHeadersRead(ctx, headersStreamId, hdrBlockBuilder.headers(), streamDependency, weight, exclusive, padding, headersFlags.endOfStream());
/*     */             
/* 426 */             close();
/*     */           }
/*     */           
/*     */         }
/*     */         
/* 431 */       };
/* 432 */       this.headersContinuation.processFragment(this.flags.endOfHeaders(), fragment, listener);
/* 433 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 438 */     this.headersContinuation = new HeadersContinuation(headersStreamId, ctx)
/*     */     {
/*     */       public int getStreamId() {
/* 441 */         return headersStreamId;
/*     */       }
/*     */       
/*     */       public void processFragment(boolean endOfHeaders, ByteBuf fragment, Http2FrameListener listener)
/*     */         throws Http2Exception
/*     */       {
/* 447 */         DefaultHttp2FrameReader.HeadersBlockBuilder hdrBlockBuilder = headersBlockBuilder();
/* 448 */         hdrBlockBuilder.addFragment(fragment, ctx.alloc(), endOfHeaders);
/* 449 */         if (endOfHeaders) {
/* 450 */           listener.onHeadersRead(ctx, headersStreamId, hdrBlockBuilder.headers(), padding, headersFlags.endOfStream());
/*     */           
/* 452 */           close();
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 457 */     };
/* 458 */     ByteBuf fragment = payload.readSlice(payload.readableBytes() - padding);
/* 459 */     this.headersContinuation.processFragment(this.flags.endOfHeaders(), fragment, listener);
/*     */   }
/*     */   
/*     */   private void readPriorityFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception
/*     */   {
/* 464 */     long word1 = payload.readUnsignedInt();
/* 465 */     boolean exclusive = (word1 & 0x80000000) != 0L;
/* 466 */     int streamDependency = (int)(word1 & 0x7FFFFFFF);
/* 467 */     short weight = (short)(payload.readUnsignedByte() + 1);
/* 468 */     listener.onPriorityRead(ctx, this.streamId, streamDependency, weight, exclusive);
/*     */   }
/*     */   
/*     */   private void readRstStreamFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception
/*     */   {
/* 473 */     long errorCode = payload.readUnsignedInt();
/* 474 */     listener.onRstStreamRead(ctx, this.streamId, errorCode);
/*     */   }
/*     */   
/*     */   private void readSettingsFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception
/*     */   {
/* 479 */     if (this.flags.ack()) {
/* 480 */       listener.onSettingsAckRead(ctx);
/*     */     } else {
/* 482 */       int numSettings = this.payloadLength / 6;
/* 483 */       Http2Settings settings = new Http2Settings();
/* 484 */       for (int index = 0; index < numSettings; index++) {
/* 485 */         int id = payload.readUnsignedShort();
/* 486 */         long value = payload.readUnsignedInt();
/*     */         try {
/* 488 */           settings.put(id, Long.valueOf(value));
/*     */         } catch (IllegalArgumentException e) {
/* 490 */           switch (id) {
/*     */           case 5: 
/* 492 */             throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, e, e.getMessage(), new Object[0]); }
/*     */         }
/* 494 */         throw Http2Exception.connectionError(Http2Error.FLOW_CONTROL_ERROR, e, e.getMessage(), new Object[0]);
/*     */         
/* 496 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, e, e.getMessage(), new Object[0]);
/*     */       }
/*     */       
/*     */ 
/* 500 */       listener.onSettingsRead(ctx, settings);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readPushPromiseFrame(final ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception
/*     */   {
/* 506 */     final int pushPromiseStreamId = this.streamId;
/* 507 */     final int padding = readPadding(payload);
/* 508 */     final int promisedStreamId = Http2CodecUtil.readUnsignedInt(payload);
/*     */     
/*     */ 
/* 511 */     this.headersContinuation = new HeadersContinuation(pushPromiseStreamId, ctx)
/*     */     {
/*     */       public int getStreamId() {
/* 514 */         return pushPromiseStreamId;
/*     */       }
/*     */       
/*     */       public void processFragment(boolean endOfHeaders, ByteBuf fragment, Http2FrameListener listener)
/*     */         throws Http2Exception
/*     */       {
/* 520 */         headersBlockBuilder().addFragment(fragment, ctx.alloc(), endOfHeaders);
/* 521 */         if (endOfHeaders) {
/* 522 */           Http2Headers headers = headersBlockBuilder().headers();
/* 523 */           listener.onPushPromiseRead(ctx, pushPromiseStreamId, promisedStreamId, headers, padding);
/*     */           
/* 525 */           close();
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 530 */     };
/* 531 */     ByteBuf fragment = payload.readSlice(payload.readableBytes() - padding);
/* 532 */     this.headersContinuation.processFragment(this.flags.endOfHeaders(), fragment, listener);
/*     */   }
/*     */   
/*     */   private void readPingFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception
/*     */   {
/* 537 */     ByteBuf data = payload.readSlice(payload.readableBytes());
/* 538 */     if (this.flags.ack()) {
/* 539 */       listener.onPingAckRead(ctx, data);
/*     */     } else {
/* 541 */       listener.onPingRead(ctx, data);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void readGoAwayFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception
/*     */   {
/* 547 */     int lastStreamId = Http2CodecUtil.readUnsignedInt(payload);
/* 548 */     long errorCode = payload.readUnsignedInt();
/* 549 */     ByteBuf debugData = payload.readSlice(payload.readableBytes());
/* 550 */     listener.onGoAwayRead(ctx, lastStreamId, errorCode, debugData);
/*     */   }
/*     */   
/*     */   private void readWindowUpdateFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception
/*     */   {
/* 555 */     int windowSizeIncrement = Http2CodecUtil.readUnsignedInt(payload);
/* 556 */     if (windowSizeIncrement == 0) {
/* 557 */       throw Http2Exception.streamError(this.streamId, Http2Error.PROTOCOL_ERROR, "Received WINDOW_UPDATE with delta 0 for stream: %d", new Object[] { Integer.valueOf(this.streamId) });
/*     */     }
/*     */     
/* 560 */     listener.onWindowUpdateRead(ctx, this.streamId, windowSizeIncrement);
/*     */   }
/*     */   
/*     */   private void readContinuationFrame(ByteBuf payload, Http2FrameListener listener)
/*     */     throws Http2Exception
/*     */   {
/* 566 */     ByteBuf continuationFragment = payload.readSlice(payload.readableBytes());
/* 567 */     this.headersContinuation.processFragment(this.flags.endOfHeaders(), continuationFragment, listener);
/*     */   }
/*     */   
/*     */   private void readUnknownFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener)
/*     */   {
/* 572 */     payload = payload.readSlice(payload.readableBytes());
/* 573 */     listener.onUnknownFrame(ctx, this.frameType, this.streamId, this.flags, payload);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private short readPadding(ByteBuf payload)
/*     */   {
/* 580 */     if (!this.flags.paddingPresent()) {
/* 581 */       return 0;
/*     */     }
/* 583 */     return payload.readUnsignedByte();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private abstract class HeadersContinuation
/*     */   {
/* 592 */     private final DefaultHttp2FrameReader.HeadersBlockBuilder builder = new DefaultHttp2FrameReader.HeadersBlockBuilder(DefaultHttp2FrameReader.this);
/*     */     
/*     */ 
/*     */ 
/*     */     private HeadersContinuation() {}
/*     */     
/*     */ 
/*     */ 
/*     */     abstract int getStreamId();
/*     */     
/*     */ 
/*     */ 
/*     */     abstract void processFragment(boolean paramBoolean, ByteBuf paramByteBuf, Http2FrameListener paramHttp2FrameListener)
/*     */       throws Http2Exception;
/*     */     
/*     */ 
/*     */     final DefaultHttp2FrameReader.HeadersBlockBuilder headersBlockBuilder()
/*     */     {
/* 610 */       return this.builder;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     final void close()
/*     */     {
/* 617 */       this.builder.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected class HeadersBlockBuilder
/*     */   {
/*     */     private ByteBuf headerBlock;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected HeadersBlockBuilder() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     final void addFragment(ByteBuf fragment, ByteBufAllocator alloc, boolean endOfHeaders)
/*     */     {
/* 638 */       if (this.headerBlock == null) {
/* 639 */         if (endOfHeaders)
/*     */         {
/*     */ 
/* 642 */           this.headerBlock = fragment.retain();
/*     */         } else {
/* 644 */           this.headerBlock = alloc.buffer(fragment.readableBytes());
/* 645 */           this.headerBlock.writeBytes(fragment);
/*     */         }
/* 647 */         return;
/*     */       }
/* 649 */       if (this.headerBlock.isWritable(fragment.readableBytes()))
/*     */       {
/* 651 */         this.headerBlock.writeBytes(fragment);
/*     */       }
/*     */       else {
/* 654 */         ByteBuf buf = alloc.buffer(this.headerBlock.readableBytes() + fragment.readableBytes());
/* 655 */         buf.writeBytes(this.headerBlock);
/* 656 */         buf.writeBytes(fragment);
/* 657 */         this.headerBlock.release();
/* 658 */         this.headerBlock = buf;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     Http2Headers headers()
/*     */       throws Http2Exception
/*     */     {
/*     */       try
/*     */       {
/* 668 */         return DefaultHttp2FrameReader.this.headersDecoder.decodeHeaders(this.headerBlock);
/*     */       } finally {
/* 670 */         close();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     void close()
/*     */     {
/* 678 */       if (this.headerBlock != null) {
/* 679 */         this.headerBlock.release();
/* 680 */         this.headerBlock = null;
/*     */       }
/*     */       
/*     */ 
/* 684 */       DefaultHttp2FrameReader.this.headersContinuation = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyNotProcessingHeaders() throws Http2Exception {
/* 689 */     if (this.headersContinuation != null) {
/* 690 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Received frame of type %s while processing headers.", new Object[] { Byte.valueOf(this.frameType) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyPayloadLength(int payloadLength) throws Http2Exception {
/* 695 */     if (payloadLength > this.maxFrameSize) {
/* 696 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Total payload length %d exceeds max frame length.", new Object[] { Integer.valueOf(payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private static void verifyStreamOrConnectionId(int streamId, String argumentName) throws Http2Exception
/*     */   {
/* 702 */     if (streamId < 0) {
/* 703 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "%s must be >= 0", new Object[] { argumentName });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\DefaultHttp2FrameReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */