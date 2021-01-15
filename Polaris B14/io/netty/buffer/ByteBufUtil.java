/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.Recycler.Handle;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ByteBufUtil
/*     */ {
/*  41 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ByteBufUtil.class);
/*     */   
/*  43 */   private static final char[] HEXDUMP_TABLE = new char['Ѐ'];
/*     */   
/*     */   static final ByteBufAllocator DEFAULT_ALLOCATOR;
/*     */   private static final int THREAD_LOCAL_BUFFER_SIZE;
/*     */   
/*     */   static
/*     */   {
/*  50 */     char[] DIGITS = "0123456789abcdef".toCharArray();
/*  51 */     for (int i = 0; i < 256; i++) {
/*  52 */       HEXDUMP_TABLE[(i << 1)] = DIGITS[(i >>> 4 & 0xF)];
/*  53 */       HEXDUMP_TABLE[((i << 1) + 1)] = DIGITS[(i & 0xF)];
/*     */     }
/*     */     
/*  56 */     String allocType = SystemPropertyUtil.get("io.netty.allocator.type", PlatformDependent.isAndroid() ? "unpooled" : "pooled");
/*     */     
/*  58 */     allocType = allocType.toLowerCase(Locale.US).trim();
/*     */     
/*     */     ByteBufAllocator alloc;
/*  61 */     if ("unpooled".equals(allocType)) {
/*  62 */       ByteBufAllocator alloc = UnpooledByteBufAllocator.DEFAULT;
/*  63 */       logger.debug("-Dio.netty.allocator.type: {}", allocType);
/*  64 */     } else if ("pooled".equals(allocType)) {
/*  65 */       ByteBufAllocator alloc = PooledByteBufAllocator.DEFAULT;
/*  66 */       logger.debug("-Dio.netty.allocator.type: {}", allocType);
/*     */     } else {
/*  68 */       alloc = PooledByteBufAllocator.DEFAULT;
/*  69 */       logger.debug("-Dio.netty.allocator.type: pooled (unknown: {})", allocType);
/*     */     }
/*     */     
/*  72 */     DEFAULT_ALLOCATOR = alloc;
/*     */     
/*  74 */     THREAD_LOCAL_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.threadLocalDirectBufferSize", 65536);
/*  75 */     logger.debug("-Dio.netty.threadLocalDirectBufferSize: {}", Integer.valueOf(THREAD_LOCAL_BUFFER_SIZE));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String hexDump(ByteBuf buffer)
/*     */   {
/*  83 */     return hexDump(buffer, buffer.readerIndex(), buffer.readableBytes());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String hexDump(ByteBuf buffer, int fromIndex, int length)
/*     */   {
/*  91 */     if (length < 0) {
/*  92 */       throw new IllegalArgumentException("length: " + length);
/*     */     }
/*  94 */     if (length == 0) {
/*  95 */       return "";
/*     */     }
/*     */     
/*  98 */     int endIndex = fromIndex + length;
/*  99 */     char[] buf = new char[length << 1];
/*     */     
/* 101 */     int srcIdx = fromIndex;
/* 102 */     for (int dstIdx = 0; 
/* 103 */         srcIdx < endIndex; dstIdx += 2) {
/* 104 */       System.arraycopy(HEXDUMP_TABLE, buffer.getUnsignedByte(srcIdx) << 1, buf, dstIdx, 2);srcIdx++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 109 */     return new String(buf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String hexDump(byte[] array)
/*     */   {
/* 117 */     return hexDump(array, 0, array.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String hexDump(byte[] array, int fromIndex, int length)
/*     */   {
/* 125 */     if (length < 0) {
/* 126 */       throw new IllegalArgumentException("length: " + length);
/*     */     }
/* 128 */     if (length == 0) {
/* 129 */       return "";
/*     */     }
/*     */     
/* 132 */     int endIndex = fromIndex + length;
/* 133 */     char[] buf = new char[length << 1];
/*     */     
/* 135 */     int srcIdx = fromIndex;
/* 136 */     for (int dstIdx = 0; 
/* 137 */         srcIdx < endIndex; dstIdx += 2) {
/* 138 */       System.arraycopy(HEXDUMP_TABLE, (array[srcIdx] & 0xFF) << 1, buf, dstIdx, 2);srcIdx++;
/*     */     }
/*     */     
/* 141 */     return new String(buf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int hashCode(ByteBuf buffer)
/*     */   {
/* 149 */     int aLen = buffer.readableBytes();
/* 150 */     int intCount = aLen >>> 2;
/* 151 */     int byteCount = aLen & 0x3;
/*     */     
/* 153 */     int hashCode = 1;
/* 154 */     int arrayIndex = buffer.readerIndex();
/* 155 */     if (buffer.order() == ByteOrder.BIG_ENDIAN) {
/* 156 */       for (int i = intCount; i > 0; i--) {
/* 157 */         hashCode = 31 * hashCode + buffer.getInt(arrayIndex);
/* 158 */         arrayIndex += 4;
/*     */       }
/*     */     } else {
/* 161 */       for (int i = intCount; i > 0; i--) {
/* 162 */         hashCode = 31 * hashCode + swapInt(buffer.getInt(arrayIndex));
/* 163 */         arrayIndex += 4;
/*     */       }
/*     */     }
/*     */     
/* 167 */     for (int i = byteCount; i > 0; i--) {
/* 168 */       hashCode = 31 * hashCode + buffer.getByte(arrayIndex++);
/*     */     }
/*     */     
/* 171 */     if (hashCode == 0) {
/* 172 */       hashCode = 1;
/*     */     }
/*     */     
/* 175 */     return hashCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean equals(ByteBuf bufferA, ByteBuf bufferB)
/*     */   {
/* 184 */     int aLen = bufferA.readableBytes();
/* 185 */     if (aLen != bufferB.readableBytes()) {
/* 186 */       return false;
/*     */     }
/*     */     
/* 189 */     int longCount = aLen >>> 3;
/* 190 */     int byteCount = aLen & 0x7;
/*     */     
/* 192 */     int aIndex = bufferA.readerIndex();
/* 193 */     int bIndex = bufferB.readerIndex();
/*     */     
/* 195 */     if (bufferA.order() == bufferB.order()) {
/* 196 */       for (int i = longCount; i > 0; i--) {
/* 197 */         if (bufferA.getLong(aIndex) != bufferB.getLong(bIndex)) {
/* 198 */           return false;
/*     */         }
/* 200 */         aIndex += 8;
/* 201 */         bIndex += 8;
/*     */       }
/*     */     } else {
/* 204 */       for (int i = longCount; i > 0; i--) {
/* 205 */         if (bufferA.getLong(aIndex) != swapLong(bufferB.getLong(bIndex))) {
/* 206 */           return false;
/*     */         }
/* 208 */         aIndex += 8;
/* 209 */         bIndex += 8;
/*     */       }
/*     */     }
/*     */     
/* 213 */     for (int i = byteCount; i > 0; i--) {
/* 214 */       if (bufferA.getByte(aIndex) != bufferB.getByte(bIndex)) {
/* 215 */         return false;
/*     */       }
/* 217 */       aIndex++;
/* 218 */       bIndex++;
/*     */     }
/*     */     
/* 221 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int compare(ByteBuf bufferA, ByteBuf bufferB)
/*     */   {
/* 229 */     int aLen = bufferA.readableBytes();
/* 230 */     int bLen = bufferB.readableBytes();
/* 231 */     int minLength = Math.min(aLen, bLen);
/* 232 */     int uintCount = minLength >>> 2;
/* 233 */     int byteCount = minLength & 0x3;
/*     */     
/* 235 */     int aIndex = bufferA.readerIndex();
/* 236 */     int bIndex = bufferB.readerIndex();
/*     */     
/* 238 */     if (bufferA.order() == bufferB.order()) {
/* 239 */       for (int i = uintCount; i > 0; i--) {
/* 240 */         long va = bufferA.getUnsignedInt(aIndex);
/* 241 */         long vb = bufferB.getUnsignedInt(bIndex);
/* 242 */         if (va > vb) {
/* 243 */           return 1;
/*     */         }
/* 245 */         if (va < vb) {
/* 246 */           return -1;
/*     */         }
/* 248 */         aIndex += 4;
/* 249 */         bIndex += 4;
/*     */       }
/*     */     } else {
/* 252 */       for (int i = uintCount; i > 0; i--) {
/* 253 */         long va = bufferA.getUnsignedInt(aIndex);
/* 254 */         long vb = swapInt(bufferB.getInt(bIndex)) & 0xFFFFFFFF;
/* 255 */         if (va > vb) {
/* 256 */           return 1;
/*     */         }
/* 258 */         if (va < vb) {
/* 259 */           return -1;
/*     */         }
/* 261 */         aIndex += 4;
/* 262 */         bIndex += 4;
/*     */       }
/*     */     }
/*     */     
/* 266 */     for (int i = byteCount; i > 0; i--) {
/* 267 */       short va = bufferA.getUnsignedByte(aIndex);
/* 268 */       short vb = bufferB.getUnsignedByte(bIndex);
/* 269 */       if (va > vb) {
/* 270 */         return 1;
/*     */       }
/* 272 */       if (va < vb) {
/* 273 */         return -1;
/*     */       }
/* 275 */       aIndex++;
/* 276 */       bIndex++;
/*     */     }
/*     */     
/* 279 */     return aLen - bLen;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int indexOf(ByteBuf buffer, int fromIndex, int toIndex, byte value)
/*     */   {
/* 287 */     if (fromIndex <= toIndex) {
/* 288 */       return firstIndexOf(buffer, fromIndex, toIndex, value);
/*     */     }
/* 290 */     return lastIndexOf(buffer, fromIndex, toIndex, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static short swapShort(short value)
/*     */   {
/* 298 */     return Short.reverseBytes(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int swapMedium(int value)
/*     */   {
/* 305 */     int swapped = value << 16 & 0xFF0000 | value & 0xFF00 | value >>> 16 & 0xFF;
/* 306 */     if ((swapped & 0x800000) != 0) {
/* 307 */       swapped |= 0xFF000000;
/*     */     }
/* 309 */     return swapped;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int swapInt(int value)
/*     */   {
/* 316 */     return Integer.reverseBytes(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static long swapLong(long value)
/*     */   {
/* 323 */     return Long.reverseBytes(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf readBytes(ByteBufAllocator alloc, ByteBuf buffer, int length)
/*     */   {
/* 330 */     boolean release = true;
/* 331 */     ByteBuf dst = alloc.buffer(length);
/*     */     try {
/* 333 */       buffer.readBytes(dst);
/* 334 */       release = false;
/* 335 */       return dst;
/*     */     } finally {
/* 337 */       if (release) {
/* 338 */         dst.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static int firstIndexOf(ByteBuf buffer, int fromIndex, int toIndex, byte value) {
/* 344 */     fromIndex = Math.max(fromIndex, 0);
/* 345 */     if ((fromIndex >= toIndex) || (buffer.capacity() == 0)) {
/* 346 */       return -1;
/*     */     }
/*     */     
/* 349 */     for (int i = fromIndex; i < toIndex; i++) {
/* 350 */       if (buffer.getByte(i) == value) {
/* 351 */         return i;
/*     */       }
/*     */     }
/*     */     
/* 355 */     return -1;
/*     */   }
/*     */   
/*     */   private static int lastIndexOf(ByteBuf buffer, int fromIndex, int toIndex, byte value) {
/* 359 */     fromIndex = Math.min(fromIndex, buffer.capacity());
/* 360 */     if ((fromIndex < 0) || (buffer.capacity() == 0)) {
/* 361 */       return -1;
/*     */     }
/*     */     
/* 364 */     for (int i = fromIndex - 1; i >= toIndex; i--) {
/* 365 */       if (buffer.getByte(i) == value) {
/* 366 */         return i;
/*     */       }
/*     */     }
/*     */     
/* 370 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int writeUtf8(ByteBuf buf, CharSequence seq)
/*     */   {
/* 380 */     if (buf == null) {
/* 381 */       throw new NullPointerException("buf");
/*     */     }
/* 383 */     if (seq == null) {
/* 384 */       throw new NullPointerException("seq");
/*     */     }
/*     */     
/* 387 */     int len = seq.length();
/* 388 */     int maxSize = len * 3;
/* 389 */     buf.ensureWritable(maxSize);
/* 390 */     if ((buf instanceof AbstractByteBuf))
/*     */     {
/* 392 */       AbstractByteBuf buffer = (AbstractByteBuf)buf;
/* 393 */       int oldWriterIndex = buffer.writerIndex;
/* 394 */       int writerIndex = oldWriterIndex;
/*     */       
/*     */ 
/*     */ 
/* 398 */       for (int i = 0; i < len; i++) {
/* 399 */         char c = seq.charAt(i);
/* 400 */         if (c < '') {
/* 401 */           buffer._setByte(writerIndex++, (byte)c);
/* 402 */         } else if (c < 'ࠀ') {
/* 403 */           buffer._setByte(writerIndex++, (byte)(0xC0 | c >> '\006'));
/* 404 */           buffer._setByte(writerIndex++, (byte)(0x80 | c & 0x3F));
/*     */         } else {
/* 406 */           buffer._setByte(writerIndex++, (byte)(0xE0 | c >> '\f'));
/* 407 */           buffer._setByte(writerIndex++, (byte)(0x80 | c >> '\006' & 0x3F));
/* 408 */           buffer._setByte(writerIndex++, (byte)(0x80 | c & 0x3F));
/*     */         }
/*     */       }
/*     */       
/* 412 */       buffer.writerIndex = writerIndex;
/* 413 */       return writerIndex - oldWriterIndex;
/*     */     }
/*     */     
/*     */ 
/* 417 */     byte[] bytes = seq.toString().getBytes(CharsetUtil.UTF_8);
/* 418 */     buf.writeBytes(bytes);
/* 419 */     return bytes.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int writeAscii(ByteBuf buf, CharSequence seq)
/*     */   {
/* 430 */     if (buf == null) {
/* 431 */       throw new NullPointerException("buf");
/*     */     }
/* 433 */     if (seq == null) {
/* 434 */       throw new NullPointerException("seq");
/*     */     }
/*     */     
/* 437 */     int len = seq.length();
/* 438 */     buf.ensureWritable(len);
/* 439 */     if ((buf instanceof AbstractByteBuf))
/*     */     {
/* 441 */       AbstractByteBuf buffer = (AbstractByteBuf)buf;
/* 442 */       int writerIndex = buffer.writerIndex;
/*     */       
/*     */ 
/*     */ 
/* 446 */       for (int i = 0; i < len; i++) {
/* 447 */         buffer._setByte(writerIndex++, (byte)seq.charAt(i));
/*     */       }
/*     */       
/* 450 */       buffer.writerIndex = writerIndex;
/*     */     }
/*     */     else
/*     */     {
/* 454 */       buf.writeBytes(seq.toString().getBytes(CharsetUtil.US_ASCII));
/*     */     }
/* 456 */     return len;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf encodeString(ByteBufAllocator alloc, CharBuffer src, Charset charset)
/*     */   {
/* 464 */     return encodeString0(alloc, false, src, charset);
/*     */   }
/*     */   
/*     */   static ByteBuf encodeString0(ByteBufAllocator alloc, boolean enforceHeap, CharBuffer src, Charset charset) {
/* 468 */     CharsetEncoder encoder = CharsetUtil.getEncoder(charset);
/* 469 */     int length = (int)(src.remaining() * encoder.maxBytesPerChar());
/* 470 */     boolean release = true;
/*     */     ByteBuf dst;
/* 472 */     ByteBuf dst; if (enforceHeap) {
/* 473 */       dst = alloc.heapBuffer(length);
/*     */     } else {
/* 475 */       dst = alloc.buffer(length);
/*     */     }
/*     */     try {
/* 478 */       ByteBuffer dstBuf = dst.internalNioBuffer(0, length);
/* 479 */       int pos = dstBuf.position();
/* 480 */       CoderResult cr = encoder.encode(src, dstBuf, true);
/* 481 */       if (!cr.isUnderflow()) {
/* 482 */         cr.throwException();
/*     */       }
/* 484 */       cr = encoder.flush(dstBuf);
/* 485 */       if (!cr.isUnderflow()) {
/* 486 */         cr.throwException();
/*     */       }
/* 488 */       dst.writerIndex(dst.writerIndex() + dstBuf.position() - pos);
/* 489 */       release = false;
/* 490 */       return dst;
/*     */     } catch (CharacterCodingException x) {
/* 492 */       throw new IllegalStateException(x);
/*     */     } finally {
/* 494 */       if (release) {
/* 495 */         dst.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static String decodeString(ByteBuffer src, Charset charset) {
/* 501 */     CharsetDecoder decoder = CharsetUtil.getDecoder(charset);
/* 502 */     CharBuffer dst = CharBuffer.allocate((int)(src.remaining() * decoder.maxCharsPerByte()));
/*     */     try
/*     */     {
/* 505 */       CoderResult cr = decoder.decode(src, dst, true);
/* 506 */       if (!cr.isUnderflow()) {
/* 507 */         cr.throwException();
/*     */       }
/* 509 */       cr = decoder.flush(dst);
/* 510 */       if (!cr.isUnderflow()) {
/* 511 */         cr.throwException();
/*     */       }
/*     */     } catch (CharacterCodingException x) {
/* 514 */       throw new IllegalStateException(x);
/*     */     }
/* 516 */     return dst.flip().toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf threadLocalDirectBuffer()
/*     */   {
/* 525 */     if (THREAD_LOCAL_BUFFER_SIZE <= 0) {
/* 526 */       return null;
/*     */     }
/*     */     
/* 529 */     if (PlatformDependent.hasUnsafe()) {
/* 530 */       return ThreadLocalUnsafeDirectByteBuf.newInstance();
/*     */     }
/* 532 */     return ThreadLocalDirectByteBuf.newInstance();
/*     */   }
/*     */   
/*     */   static final class ThreadLocalUnsafeDirectByteBuf
/*     */     extends UnpooledUnsafeDirectByteBuf
/*     */   {
/* 538 */     private static final Recycler<ThreadLocalUnsafeDirectByteBuf> RECYCLER = new Recycler()
/*     */     {
/*     */ 
/*     */       protected ByteBufUtil.ThreadLocalUnsafeDirectByteBuf newObject(Recycler.Handle handle) {
/* 542 */         return new ByteBufUtil.ThreadLocalUnsafeDirectByteBuf(handle, null); }
/*     */     };
/*     */     private final Recycler.Handle handle;
/*     */     
/*     */     static ThreadLocalUnsafeDirectByteBuf newInstance() {
/* 547 */       ThreadLocalUnsafeDirectByteBuf buf = (ThreadLocalUnsafeDirectByteBuf)RECYCLER.get();
/* 548 */       buf.setRefCnt(1);
/* 549 */       return buf;
/*     */     }
/*     */     
/*     */ 
/*     */     private ThreadLocalUnsafeDirectByteBuf(Recycler.Handle handle)
/*     */     {
/* 555 */       super(256, Integer.MAX_VALUE);
/* 556 */       this.handle = handle;
/*     */     }
/*     */     
/*     */     protected void deallocate()
/*     */     {
/* 561 */       if (capacity() > ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE) {
/* 562 */         super.deallocate();
/*     */       } else {
/* 564 */         clear();
/* 565 */         RECYCLER.recycle(this, this.handle);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ThreadLocalDirectByteBuf extends UnpooledDirectByteBuf
/*     */   {
/* 572 */     private static final Recycler<ThreadLocalDirectByteBuf> RECYCLER = new Recycler()
/*     */     {
/*     */ 
/* 575 */       protected ByteBufUtil.ThreadLocalDirectByteBuf newObject(Recycler.Handle handle) { return new ByteBufUtil.ThreadLocalDirectByteBuf(handle, null); }
/*     */     };
/*     */     private final Recycler.Handle handle;
/*     */     
/*     */     static ThreadLocalDirectByteBuf newInstance() {
/* 580 */       ThreadLocalDirectByteBuf buf = (ThreadLocalDirectByteBuf)RECYCLER.get();
/* 581 */       buf.setRefCnt(1);
/* 582 */       return buf;
/*     */     }
/*     */     
/*     */ 
/*     */     private ThreadLocalDirectByteBuf(Recycler.Handle handle)
/*     */     {
/* 588 */       super(256, Integer.MAX_VALUE);
/* 589 */       this.handle = handle;
/*     */     }
/*     */     
/*     */     protected void deallocate()
/*     */     {
/* 594 */       if (capacity() > ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE) {
/* 595 */         super.deallocate();
/*     */       } else {
/* 597 */         clear();
/* 598 */         RECYCLER.recycle(this, this.handle);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\ByteBufUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */