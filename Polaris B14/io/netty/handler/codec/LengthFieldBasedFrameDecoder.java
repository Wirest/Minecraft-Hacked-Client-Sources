/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LengthFieldBasedFrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private final ByteOrder byteOrder;
/*     */   private final int maxFrameLength;
/*     */   private final int lengthFieldOffset;
/*     */   private final int lengthFieldLength;
/*     */   private final int lengthFieldEndOffset;
/*     */   private final int lengthAdjustment;
/*     */   private final int initialBytesToStrip;
/*     */   private final boolean failFast;
/*     */   private boolean discardingTooLongFrame;
/*     */   private long tooLongFrameLength;
/*     */   private long bytesToDiscard;
/*     */   
/*     */   public LengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength)
/*     */   {
/* 213 */     this(maxFrameLength, lengthFieldOffset, lengthFieldLength, 0, 0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public LengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip)
/*     */   {
/* 236 */     this(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast)
/*     */   {
/* 268 */     this(ByteOrder.BIG_ENDIAN, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LengthFieldBasedFrameDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast)
/*     */   {
/* 301 */     if (byteOrder == null) {
/* 302 */       throw new NullPointerException("byteOrder");
/*     */     }
/*     */     
/* 305 */     if (maxFrameLength <= 0) {
/* 306 */       throw new IllegalArgumentException("maxFrameLength must be a positive integer: " + maxFrameLength);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 311 */     if (lengthFieldOffset < 0) {
/* 312 */       throw new IllegalArgumentException("lengthFieldOffset must be a non-negative integer: " + lengthFieldOffset);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 317 */     if (initialBytesToStrip < 0) {
/* 318 */       throw new IllegalArgumentException("initialBytesToStrip must be a non-negative integer: " + initialBytesToStrip);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 323 */     if (lengthFieldOffset > maxFrameLength - lengthFieldLength) {
/* 324 */       throw new IllegalArgumentException("maxFrameLength (" + maxFrameLength + ") " + "must be equal to or greater than " + "lengthFieldOffset (" + lengthFieldOffset + ") + " + "lengthFieldLength (" + lengthFieldLength + ").");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 331 */     this.byteOrder = byteOrder;
/* 332 */     this.maxFrameLength = maxFrameLength;
/* 333 */     this.lengthFieldOffset = lengthFieldOffset;
/* 334 */     this.lengthFieldLength = lengthFieldLength;
/* 335 */     this.lengthAdjustment = lengthAdjustment;
/* 336 */     this.lengthFieldEndOffset = (lengthFieldOffset + lengthFieldLength);
/* 337 */     this.initialBytesToStrip = initialBytesToStrip;
/* 338 */     this.failFast = failFast;
/*     */   }
/*     */   
/*     */   protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/* 343 */     Object decoded = decode(ctx, in);
/* 344 */     if (decoded != null) {
/* 345 */       out.add(decoded);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object decode(ChannelHandlerContext ctx, ByteBuf in)
/*     */     throws Exception
/*     */   {
/* 358 */     if (this.discardingTooLongFrame) {
/* 359 */       long bytesToDiscard = this.bytesToDiscard;
/* 360 */       int localBytesToDiscard = (int)Math.min(bytesToDiscard, in.readableBytes());
/* 361 */       in.skipBytes(localBytesToDiscard);
/* 362 */       bytesToDiscard -= localBytesToDiscard;
/* 363 */       this.bytesToDiscard = bytesToDiscard;
/*     */       
/* 365 */       failIfNecessary(false);
/*     */     }
/*     */     
/* 368 */     if (in.readableBytes() < this.lengthFieldEndOffset) {
/* 369 */       return null;
/*     */     }
/*     */     
/* 372 */     int actualLengthFieldOffset = in.readerIndex() + this.lengthFieldOffset;
/* 373 */     long frameLength = getUnadjustedFrameLength(in, actualLengthFieldOffset, this.lengthFieldLength, this.byteOrder);
/*     */     
/* 375 */     if (frameLength < 0L) {
/* 376 */       in.skipBytes(this.lengthFieldEndOffset);
/* 377 */       throw new CorruptedFrameException("negative pre-adjustment length field: " + frameLength);
/*     */     }
/*     */     
/*     */ 
/* 381 */     frameLength += this.lengthAdjustment + this.lengthFieldEndOffset;
/*     */     
/* 383 */     if (frameLength < this.lengthFieldEndOffset) {
/* 384 */       in.skipBytes(this.lengthFieldEndOffset);
/* 385 */       throw new CorruptedFrameException("Adjusted frame length (" + frameLength + ") is less " + "than lengthFieldEndOffset: " + this.lengthFieldEndOffset);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 390 */     if (frameLength > this.maxFrameLength) {
/* 391 */       long discard = frameLength - in.readableBytes();
/* 392 */       this.tooLongFrameLength = frameLength;
/*     */       
/* 394 */       if (discard < 0L)
/*     */       {
/* 396 */         in.skipBytes((int)frameLength);
/*     */       }
/*     */       else {
/* 399 */         this.discardingTooLongFrame = true;
/* 400 */         this.bytesToDiscard = discard;
/* 401 */         in.skipBytes(in.readableBytes());
/*     */       }
/* 403 */       failIfNecessary(true);
/* 404 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 408 */     int frameLengthInt = (int)frameLength;
/* 409 */     if (in.readableBytes() < frameLengthInt) {
/* 410 */       return null;
/*     */     }
/*     */     
/* 413 */     if (this.initialBytesToStrip > frameLengthInt) {
/* 414 */       in.skipBytes(frameLengthInt);
/* 415 */       throw new CorruptedFrameException("Adjusted frame length (" + frameLength + ") is less " + "than initialBytesToStrip: " + this.initialBytesToStrip);
/*     */     }
/*     */     
/*     */ 
/* 419 */     in.skipBytes(this.initialBytesToStrip);
/*     */     
/*     */ 
/* 422 */     int readerIndex = in.readerIndex();
/* 423 */     int actualFrameLength = frameLengthInt - this.initialBytesToStrip;
/* 424 */     ByteBuf frame = extractFrame(ctx, in, readerIndex, actualFrameLength);
/* 425 */     in.readerIndex(readerIndex + actualFrameLength);
/* 426 */     return frame;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected long getUnadjustedFrameLength(ByteBuf buf, int offset, int length, ByteOrder order)
/*     */   {
/* 438 */     buf = buf.order(order);
/*     */     long frameLength;
/* 440 */     switch (length) {
/*     */     case 1: 
/* 442 */       frameLength = buf.getUnsignedByte(offset);
/* 443 */       break;
/*     */     case 2: 
/* 445 */       frameLength = buf.getUnsignedShort(offset);
/* 446 */       break;
/*     */     case 3: 
/* 448 */       frameLength = buf.getUnsignedMedium(offset);
/* 449 */       break;
/*     */     case 4: 
/* 451 */       frameLength = buf.getUnsignedInt(offset);
/* 452 */       break;
/*     */     case 8: 
/* 454 */       frameLength = buf.getLong(offset);
/* 455 */       break;
/*     */     case 5: case 6: case 7: default: 
/* 457 */       throw new DecoderException("unsupported lengthFieldLength: " + this.lengthFieldLength + " (expected: 1, 2, 3, 4, or 8)");
/*     */     }
/*     */     
/* 460 */     return frameLength;
/*     */   }
/*     */   
/*     */   private void failIfNecessary(boolean firstDetectionOfTooLongFrame) {
/* 464 */     if (this.bytesToDiscard == 0L)
/*     */     {
/*     */ 
/* 467 */       long tooLongFrameLength = this.tooLongFrameLength;
/* 468 */       this.tooLongFrameLength = 0L;
/* 469 */       this.discardingTooLongFrame = false;
/* 470 */       if ((!this.failFast) || ((this.failFast) && (firstDetectionOfTooLongFrame)))
/*     */       {
/* 472 */         fail(tooLongFrameLength);
/*     */       }
/*     */       
/*     */     }
/* 476 */     else if ((this.failFast) && (firstDetectionOfTooLongFrame)) {
/* 477 */       fail(this.tooLongFrameLength);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length)
/*     */   {
/* 494 */     ByteBuf frame = ctx.alloc().buffer(length);
/* 495 */     frame.writeBytes(buffer, index, length);
/* 496 */     return frame;
/*     */   }
/*     */   
/*     */   private void fail(long frameLength) {
/* 500 */     if (frameLength > 0L) {
/* 501 */       throw new TooLongFrameException("Adjusted frame length exceeds " + this.maxFrameLength + ": " + frameLength + " - discarded");
/*     */     }
/*     */     
/*     */ 
/* 505 */     throw new TooLongFrameException("Adjusted frame length exceeds " + this.maxFrameLength + " - discarding");
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\LengthFieldBasedFrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */