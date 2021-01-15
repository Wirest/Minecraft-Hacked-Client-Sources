/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.collection.IntObjectMap.Entry;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHttp2FrameWriter
/*     */   implements Http2FrameWriter, Http2FrameSizePolicy, Http2FrameWriter.Configuration
/*     */ {
/*     */   private static final String STREAM_ID = "Stream ID";
/*     */   private static final String STREAM_DEPENDENCY = "Stream Dependency";
/*  72 */   private static final ByteBuf ZERO_BUFFER = Unpooled.buffer(255).writeZero(255);
/*     */   private final Http2HeadersEncoder headersEncoder;
/*     */   private int maxFrameSize;
/*     */   
/*     */   public DefaultHttp2FrameWriter()
/*     */   {
/*  78 */     this(new DefaultHttp2HeadersEncoder());
/*     */   }
/*     */   
/*     */   public DefaultHttp2FrameWriter(Http2HeadersEncoder headersEncoder) {
/*  82 */     this.headersEncoder = headersEncoder;
/*  83 */     this.maxFrameSize = 16384;
/*     */   }
/*     */   
/*     */   public Http2FrameWriter.Configuration configuration()
/*     */   {
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public Http2HeaderTable headerTable()
/*     */   {
/*  93 */     return this.headersEncoder.configuration().headerTable();
/*     */   }
/*     */   
/*     */   public Http2FrameSizePolicy frameSizePolicy()
/*     */   {
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public void maxFrameSize(int max) throws Http2Exception
/*     */   {
/* 103 */     if (!Http2CodecUtil.isMaxFrameSizeValid(max)) {
/* 104 */       throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Invalid MAX_FRAME_SIZE specified in sent settings: %d", new Object[] { Integer.valueOf(max) });
/*     */     }
/* 106 */     this.maxFrameSize = max;
/*     */   }
/*     */   
/*     */   public int maxFrameSize()
/*     */   {
/* 111 */     return this.maxFrameSize;
/*     */   }
/*     */   
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */   public ChannelFuture writeData(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endStream, ChannelPromise promise)
/*     */   {
/* 120 */     boolean releaseData = true;
/* 121 */     ByteBuf buf = null;
/* 122 */     Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
/*     */     try
/*     */     {
/* 125 */       verifyStreamId(streamId, "Stream ID");
/* 126 */       verifyPadding(padding);
/*     */       
/* 128 */       Http2Flags flags = new Http2Flags().paddingPresent(padding > 0).endOfStream(endStream);
/*     */       
/* 130 */       int payloadLength = data.readableBytes() + padding + flags.getPaddingPresenceFieldLength();
/* 131 */       verifyPayloadLength(payloadLength);
/*     */       
/* 133 */       buf = ctx.alloc().buffer(10);
/* 134 */       Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)0, flags, streamId);
/* 135 */       writePaddingLength(buf, padding);
/* 136 */       ctx.write(buf, promiseAggregator.newPromise());
/*     */       
/*     */ 
/* 139 */       releaseData = false;
/* 140 */       ctx.write(data, promiseAggregator.newPromise());
/*     */       
/* 142 */       if (padding > 0) {
/* 143 */         ctx.write(ZERO_BUFFER.slice(0, padding).retain(), promiseAggregator.newPromise());
/*     */       }
/* 145 */       return promiseAggregator.doneAllocatingPromises();
/*     */     } catch (Throwable t) {
/* 147 */       if (releaseData) {
/* 148 */         data.release();
/*     */       }
/* 150 */       return promiseAggregator.setFailure(t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise)
/*     */   {
/* 157 */     return writeHeadersInternal(ctx, streamId, headers, padding, endStream, false, 0, (short)0, false, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream, ChannelPromise promise)
/*     */   {
/* 165 */     return writeHeadersInternal(ctx, streamId, headers, padding, endStream, true, streamDependency, weight, exclusive, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writePriority(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive, ChannelPromise promise)
/*     */   {
/*     */     try
/*     */     {
/* 173 */       verifyStreamId(streamId, "Stream ID");
/* 174 */       verifyStreamId(streamDependency, "Stream Dependency");
/* 175 */       verifyWeight(weight);
/*     */       
/* 177 */       ByteBuf buf = ctx.alloc().buffer(14);
/* 178 */       Http2CodecUtil.writeFrameHeaderInternal(buf, 5, (byte)2, new Http2Flags(), streamId);
/* 179 */       long word1 = exclusive ? 0x80000000 | streamDependency : streamDependency;
/* 180 */       Http2CodecUtil.writeUnsignedInt(word1, buf);
/*     */       
/* 182 */       buf.writeByte(weight - 1);
/* 183 */       return ctx.write(buf, promise);
/*     */     } catch (Throwable t) {
/* 185 */       return promise.setFailure(t);
/*     */     }
/*     */   }
/*     */   
/*     */   public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise)
/*     */   {
/*     */     try
/*     */     {
/* 193 */       verifyStreamId(streamId, "Stream ID");
/* 194 */       verifyErrorCode(errorCode);
/*     */       
/* 196 */       ByteBuf buf = ctx.alloc().buffer(13);
/* 197 */       Http2CodecUtil.writeFrameHeaderInternal(buf, 4, (byte)3, new Http2Flags(), streamId);
/* 198 */       Http2CodecUtil.writeUnsignedInt(errorCode, buf);
/* 199 */       return ctx.write(buf, promise);
/*     */     } catch (Throwable t) {
/* 201 */       return promise.setFailure(t);
/*     */     }
/*     */   }
/*     */   
/*     */   public ChannelFuture writeSettings(ChannelHandlerContext ctx, Http2Settings settings, ChannelPromise promise)
/*     */   {
/*     */     try
/*     */     {
/* 209 */       ObjectUtil.checkNotNull(settings, "settings");
/* 210 */       int payloadLength = 6 * settings.size();
/* 211 */       ByteBuf buf = ctx.alloc().buffer(9 + settings.size() * 6);
/* 212 */       Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)4, new Http2Flags(), 0);
/* 213 */       for (IntObjectMap.Entry<Long> entry : settings.entries()) {
/* 214 */         Http2CodecUtil.writeUnsignedShort(entry.key(), buf);
/* 215 */         Http2CodecUtil.writeUnsignedInt(((Long)entry.value()).longValue(), buf);
/*     */       }
/* 217 */       return ctx.write(buf, promise);
/*     */     } catch (Throwable t) {
/* 219 */       return promise.setFailure(t);
/*     */     }
/*     */   }
/*     */   
/*     */   public ChannelFuture writeSettingsAck(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */   {
/*     */     try {
/* 226 */       ByteBuf buf = ctx.alloc().buffer(9);
/* 227 */       Http2CodecUtil.writeFrameHeaderInternal(buf, 0, (byte)4, new Http2Flags().ack(true), 0);
/* 228 */       return ctx.write(buf, promise);
/*     */     } catch (Throwable t) {
/* 230 */       return promise.setFailure(t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writePing(ChannelHandlerContext ctx, boolean ack, ByteBuf data, ChannelPromise promise)
/*     */   {
/* 237 */     boolean releaseData = true;
/* 238 */     Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
/*     */     try
/*     */     {
/* 241 */       Http2Flags flags = ack ? new Http2Flags().ack(true) : new Http2Flags();
/* 242 */       ByteBuf buf = ctx.alloc().buffer(9);
/* 243 */       Http2CodecUtil.writeFrameHeaderInternal(buf, data.readableBytes(), (byte)6, flags, 0);
/* 244 */       ctx.write(buf, promiseAggregator.newPromise());
/*     */       
/*     */ 
/* 247 */       releaseData = false;
/* 248 */       ctx.write(data, promiseAggregator.newPromise());
/*     */       
/* 250 */       return promiseAggregator.doneAllocatingPromises();
/*     */     } catch (Throwable t) {
/* 252 */       if (releaseData) {
/* 253 */         data.release();
/*     */       }
/* 255 */       return promiseAggregator.setFailure(t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writePushPromise(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding, ChannelPromise promise)
/*     */   {
/* 262 */     ByteBuf headerBlock = null;
/* 263 */     Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
/*     */     try
/*     */     {
/* 266 */       verifyStreamId(streamId, "Stream ID");
/* 267 */       verifyStreamId(promisedStreamId, "Promised Stream ID");
/* 268 */       verifyPadding(padding);
/*     */       
/*     */ 
/* 271 */       headerBlock = ctx.alloc().buffer();
/* 272 */       this.headersEncoder.encodeHeaders(headers, headerBlock);
/*     */       
/*     */ 
/* 275 */       Http2Flags flags = new Http2Flags().paddingPresent(padding > 0);
/*     */       
/* 277 */       nonFragmentLength = 4 + padding + flags.getPaddingPresenceFieldLength();
/* 278 */       int maxFragmentLength = this.maxFrameSize - nonFragmentLength;
/* 279 */       ByteBuf fragment = headerBlock.readSlice(Math.min(headerBlock.readableBytes(), maxFragmentLength)).retain();
/*     */       
/*     */ 
/* 282 */       flags.endOfHeaders(!headerBlock.isReadable());
/*     */       
/* 284 */       int payloadLength = fragment.readableBytes() + nonFragmentLength;
/* 285 */       ByteBuf buf = ctx.alloc().buffer(14);
/* 286 */       Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)5, flags, streamId);
/* 287 */       writePaddingLength(buf, padding);
/*     */       
/*     */ 
/* 290 */       buf.writeInt(promisedStreamId);
/* 291 */       ctx.write(buf, promiseAggregator.newPromise());
/*     */       
/*     */ 
/* 294 */       ctx.write(fragment, promiseAggregator.newPromise());
/*     */       
/* 296 */       if (padding > 0) {
/* 297 */         ctx.write(ZERO_BUFFER.slice(0, padding).retain(), promiseAggregator.newPromise());
/*     */       }
/*     */       
/* 300 */       if (!flags.endOfHeaders()) {
/* 301 */         writeContinuationFrames(ctx, streamId, headerBlock, padding, promiseAggregator);
/*     */       }
/*     */       
/* 304 */       return promiseAggregator.doneAllocatingPromises();
/*     */     } catch (Throwable t) { int nonFragmentLength;
/* 306 */       return promiseAggregator.setFailure(t);
/*     */     } finally {
/* 308 */       if (headerBlock != null) {
/* 309 */         headerBlock.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeGoAway(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData, ChannelPromise promise)
/*     */   {
/* 317 */     boolean releaseData = true;
/* 318 */     Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
/*     */     try
/*     */     {
/* 321 */       verifyStreamOrConnectionId(lastStreamId, "Last Stream ID");
/* 322 */       verifyErrorCode(errorCode);
/*     */       
/* 324 */       int payloadLength = 8 + debugData.readableBytes();
/* 325 */       ByteBuf buf = ctx.alloc().buffer(17);
/* 326 */       Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)7, new Http2Flags(), 0);
/* 327 */       buf.writeInt(lastStreamId);
/* 328 */       Http2CodecUtil.writeUnsignedInt(errorCode, buf);
/* 329 */       ctx.write(buf, promiseAggregator.newPromise());
/*     */       
/* 331 */       releaseData = false;
/* 332 */       ctx.write(debugData, promiseAggregator.newPromise());
/* 333 */       return promiseAggregator.doneAllocatingPromises();
/*     */     } catch (Throwable t) {
/* 335 */       if (releaseData) {
/* 336 */         debugData.release();
/*     */       }
/* 338 */       return promiseAggregator.setFailure(t);
/*     */     }
/*     */   }
/*     */   
/*     */   public ChannelFuture writeWindowUpdate(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement, ChannelPromise promise)
/*     */   {
/*     */     try
/*     */     {
/* 346 */       verifyStreamOrConnectionId(streamId, "Stream ID");
/* 347 */       verifyWindowSizeIncrement(windowSizeIncrement);
/*     */       
/* 349 */       ByteBuf buf = ctx.alloc().buffer(13);
/* 350 */       Http2CodecUtil.writeFrameHeaderInternal(buf, 4, (byte)8, new Http2Flags(), streamId);
/* 351 */       buf.writeInt(windowSizeIncrement);
/* 352 */       return ctx.write(buf, promise);
/*     */     } catch (Throwable t) {
/* 354 */       return promise.setFailure(t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload, ChannelPromise promise)
/*     */   {
/* 361 */     boolean releaseData = true;
/* 362 */     Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
/*     */     try
/*     */     {
/* 365 */       verifyStreamOrConnectionId(streamId, "Stream ID");
/* 366 */       ByteBuf buf = ctx.alloc().buffer(9);
/* 367 */       Http2CodecUtil.writeFrameHeaderInternal(buf, payload.readableBytes(), frameType, flags, streamId);
/* 368 */       ctx.write(buf, promiseAggregator.newPromise());
/*     */       
/* 370 */       releaseData = false;
/* 371 */       ctx.write(payload, promiseAggregator.newPromise());
/* 372 */       return promiseAggregator.doneAllocatingPromises();
/*     */     } catch (Throwable t) {
/* 374 */       if (releaseData) {
/* 375 */         payload.release();
/*     */       }
/* 377 */       return promiseAggregator.setFailure(t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private ChannelFuture writeHeadersInternal(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, boolean hasPriority, int streamDependency, short weight, boolean exclusive, ChannelPromise promise)
/*     */   {
/* 384 */     ByteBuf headerBlock = null;
/* 385 */     Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
/*     */     try
/*     */     {
/* 388 */       verifyStreamId(streamId, "Stream ID");
/* 389 */       if (hasPriority) {
/* 390 */         verifyStreamOrConnectionId(streamDependency, "Stream Dependency");
/* 391 */         verifyPadding(padding);
/* 392 */         verifyWeight(weight);
/*     */       }
/*     */       
/*     */ 
/* 396 */       headerBlock = ctx.alloc().buffer();
/* 397 */       this.headersEncoder.encodeHeaders(headers, headerBlock);
/*     */       
/* 399 */       Http2Flags flags = new Http2Flags().endOfStream(endStream).priorityPresent(hasPriority).paddingPresent(padding > 0);
/*     */       
/*     */ 
/*     */ 
/* 403 */       nonFragmentBytes = padding + flags.getNumPriorityBytes() + flags.getPaddingPresenceFieldLength();
/* 404 */       int maxFragmentLength = this.maxFrameSize - nonFragmentBytes;
/* 405 */       ByteBuf fragment = headerBlock.readSlice(Math.min(headerBlock.readableBytes(), maxFragmentLength)).retain();
/*     */       
/*     */ 
/*     */ 
/* 409 */       flags.endOfHeaders(!headerBlock.isReadable());
/*     */       
/* 411 */       int payloadLength = fragment.readableBytes() + nonFragmentBytes;
/* 412 */       ByteBuf buf = ctx.alloc().buffer(15);
/* 413 */       Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)1, flags, streamId);
/* 414 */       writePaddingLength(buf, padding);
/*     */       long word1;
/* 416 */       if (hasPriority) {
/* 417 */         word1 = exclusive ? 0x80000000 | streamDependency : streamDependency;
/* 418 */         Http2CodecUtil.writeUnsignedInt(word1, buf);
/*     */         
/*     */ 
/* 421 */         buf.writeByte(weight - 1);
/*     */       }
/* 423 */       ctx.write(buf, promiseAggregator.newPromise());
/*     */       
/*     */ 
/* 426 */       ctx.write(fragment, promiseAggregator.newPromise());
/*     */       
/* 428 */       if (padding > 0) {
/* 429 */         ctx.write(ZERO_BUFFER.slice(0, padding).retain(), promiseAggregator.newPromise());
/*     */       }
/*     */       
/* 432 */       if (!flags.endOfHeaders()) {
/* 433 */         writeContinuationFrames(ctx, streamId, headerBlock, padding, promiseAggregator);
/*     */       }
/*     */       
/* 436 */       return promiseAggregator.doneAllocatingPromises();
/*     */     } catch (Throwable t) { int nonFragmentBytes;
/* 438 */       return promiseAggregator.setFailure(t);
/*     */     } finally {
/* 440 */       if (headerBlock != null) {
/* 441 */         headerBlock.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ChannelFuture writeContinuationFrames(ChannelHandlerContext ctx, int streamId, ByteBuf headerBlock, int padding, Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator)
/*     */   {
/* 451 */     Http2Flags flags = new Http2Flags().paddingPresent(padding > 0);
/* 452 */     int nonFragmentLength = padding + flags.getPaddingPresenceFieldLength();
/* 453 */     int maxFragmentLength = this.maxFrameSize - nonFragmentLength;
/*     */     
/* 455 */     if (maxFragmentLength <= 0) {
/* 456 */       return promiseAggregator.setFailure(new IllegalArgumentException("Padding [" + padding + "] is too large for max frame size [" + this.maxFrameSize + "]"));
/*     */     }
/*     */     
/*     */ 
/* 460 */     if (headerBlock.isReadable())
/*     */     {
/* 462 */       ByteBuf paddingBuf = padding > 0 ? ZERO_BUFFER.slice(0, padding) : null;
/* 463 */       int fragmentReadableBytes = Math.min(headerBlock.readableBytes(), maxFragmentLength);
/* 464 */       int payloadLength = fragmentReadableBytes + nonFragmentLength;
/* 465 */       ByteBuf buf = ctx.alloc().buffer(10);
/* 466 */       Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)9, flags, streamId);
/* 467 */       writePaddingLength(buf, padding);
/*     */       do
/*     */       {
/* 470 */         fragmentReadableBytes = Math.min(headerBlock.readableBytes(), maxFragmentLength);
/* 471 */         ByteBuf fragment = headerBlock.readSlice(fragmentReadableBytes).retain();
/*     */         
/* 473 */         payloadLength = fragmentReadableBytes + nonFragmentLength;
/* 474 */         if (headerBlock.isReadable()) {
/* 475 */           ctx.write(buf.retain(), promiseAggregator.newPromise());
/*     */         }
/*     */         else {
/* 478 */           flags = flags.endOfHeaders(true);
/* 479 */           buf.release();
/* 480 */           buf = ctx.alloc().buffer(10);
/* 481 */           Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)9, flags, streamId);
/* 482 */           writePaddingLength(buf, padding);
/* 483 */           ctx.write(buf, promiseAggregator.newPromise());
/*     */         }
/*     */         
/* 486 */         ctx.write(fragment, promiseAggregator.newPromise());
/*     */         
/*     */ 
/* 489 */         if (paddingBuf != null) {
/* 490 */           ctx.write(paddingBuf.retain(), promiseAggregator.newPromise());
/*     */         }
/* 492 */       } while (headerBlock.isReadable());
/*     */     }
/* 494 */     return promiseAggregator;
/*     */   }
/*     */   
/*     */   private static void writePaddingLength(ByteBuf buf, int paddingLength) {
/* 498 */     if (paddingLength > 0)
/*     */     {
/* 500 */       buf.writeByte(paddingLength);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void verifyStreamId(int streamId, String argumentName) {
/* 505 */     if (streamId <= 0) {
/* 506 */       throw new IllegalArgumentException(argumentName + " must be > 0");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void verifyStreamOrConnectionId(int streamId, String argumentName) {
/* 511 */     if (streamId < 0) {
/* 512 */       throw new IllegalArgumentException(argumentName + " must be >= 0");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void verifyPadding(int padding) {
/* 517 */     if ((padding < 0) || (padding > 255)) {
/* 518 */       throw new IllegalArgumentException("Invalid padding value: " + padding);
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyPayloadLength(int payloadLength) {
/* 523 */     if (payloadLength > this.maxFrameSize) {
/* 524 */       throw new IllegalArgumentException("Total payload length " + payloadLength + " exceeds max frame length.");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void verifyWeight(short weight)
/*     */   {
/* 530 */     if ((weight < 1) || (weight > 256)) {
/* 531 */       throw new IllegalArgumentException("Invalid weight: " + weight);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void verifyErrorCode(long errorCode) {
/* 536 */     if ((errorCode < 0L) || (errorCode > 4294967295L)) {
/* 537 */       throw new IllegalArgumentException("Invalid errorCode: " + errorCode);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void verifyWindowSizeIncrement(int windowSizeIncrement) {
/* 542 */     if (windowSizeIncrement < 0) {
/* 543 */       throw new IllegalArgumentException("WindowSizeIncrement must be >= 0");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\DefaultHttp2FrameWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */