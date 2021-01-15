/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.GatheringByteChannel;
/*     */ import java.nio.channels.ScatteringByteChannel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnpooledDirectByteBuf
/*     */   extends AbstractReferenceCountedByteBuf
/*     */ {
/*     */   private final ByteBufAllocator alloc;
/*     */   private ByteBuffer buffer;
/*     */   private ByteBuffer tmpNioBuf;
/*     */   private int capacity;
/*     */   private boolean doNotFree;
/*     */   
/*     */   protected UnpooledDirectByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity)
/*     */   {
/*  50 */     super(maxCapacity);
/*  51 */     if (alloc == null) {
/*  52 */       throw new NullPointerException("alloc");
/*     */     }
/*  54 */     if (initialCapacity < 0) {
/*  55 */       throw new IllegalArgumentException("initialCapacity: " + initialCapacity);
/*     */     }
/*  57 */     if (maxCapacity < 0) {
/*  58 */       throw new IllegalArgumentException("maxCapacity: " + maxCapacity);
/*     */     }
/*  60 */     if (initialCapacity > maxCapacity) {
/*  61 */       throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[] { Integer.valueOf(initialCapacity), Integer.valueOf(maxCapacity) }));
/*     */     }
/*     */     
/*     */ 
/*  65 */     this.alloc = alloc;
/*  66 */     setByteBuffer(ByteBuffer.allocateDirect(initialCapacity));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected UnpooledDirectByteBuf(ByteBufAllocator alloc, ByteBuffer initialBuffer, int maxCapacity)
/*     */   {
/*  75 */     super(maxCapacity);
/*  76 */     if (alloc == null) {
/*  77 */       throw new NullPointerException("alloc");
/*     */     }
/*  79 */     if (initialBuffer == null) {
/*  80 */       throw new NullPointerException("initialBuffer");
/*     */     }
/*  82 */     if (!initialBuffer.isDirect()) {
/*  83 */       throw new IllegalArgumentException("initialBuffer is not a direct buffer.");
/*     */     }
/*  85 */     if (initialBuffer.isReadOnly()) {
/*  86 */       throw new IllegalArgumentException("initialBuffer is a read-only buffer.");
/*     */     }
/*     */     
/*  89 */     int initialCapacity = initialBuffer.remaining();
/*  90 */     if (initialCapacity > maxCapacity) {
/*  91 */       throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[] { Integer.valueOf(initialCapacity), Integer.valueOf(maxCapacity) }));
/*     */     }
/*     */     
/*     */ 
/*  95 */     this.alloc = alloc;
/*  96 */     this.doNotFree = true;
/*  97 */     setByteBuffer(initialBuffer.slice().order(ByteOrder.BIG_ENDIAN));
/*  98 */     writerIndex(initialCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ByteBuffer allocateDirect(int initialCapacity)
/*     */   {
/* 105 */     return ByteBuffer.allocateDirect(initialCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void freeDirect(ByteBuffer buffer)
/*     */   {
/* 112 */     PlatformDependent.freeDirectBuffer(buffer);
/*     */   }
/*     */   
/*     */   private void setByteBuffer(ByteBuffer buffer) {
/* 116 */     ByteBuffer oldBuffer = this.buffer;
/* 117 */     if (oldBuffer != null) {
/* 118 */       if (this.doNotFree) {
/* 119 */         this.doNotFree = false;
/*     */       } else {
/* 121 */         freeDirect(oldBuffer);
/*     */       }
/*     */     }
/*     */     
/* 125 */     this.buffer = buffer;
/* 126 */     this.tmpNioBuf = null;
/* 127 */     this.capacity = buffer.remaining();
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/* 132 */     return true;
/*     */   }
/*     */   
/*     */   public int capacity()
/*     */   {
/* 137 */     return this.capacity;
/*     */   }
/*     */   
/*     */   public ByteBuf capacity(int newCapacity)
/*     */   {
/* 142 */     ensureAccessible();
/* 143 */     if ((newCapacity < 0) || (newCapacity > maxCapacity())) {
/* 144 */       throw new IllegalArgumentException("newCapacity: " + newCapacity);
/*     */     }
/*     */     
/* 147 */     int readerIndex = readerIndex();
/* 148 */     int writerIndex = writerIndex();
/*     */     
/* 150 */     int oldCapacity = this.capacity;
/* 151 */     if (newCapacity > oldCapacity) {
/* 152 */       ByteBuffer oldBuffer = this.buffer;
/* 153 */       ByteBuffer newBuffer = allocateDirect(newCapacity);
/* 154 */       oldBuffer.position(0).limit(oldBuffer.capacity());
/* 155 */       newBuffer.position(0).limit(oldBuffer.capacity());
/* 156 */       newBuffer.put(oldBuffer);
/* 157 */       newBuffer.clear();
/* 158 */       setByteBuffer(newBuffer);
/* 159 */     } else if (newCapacity < oldCapacity) {
/* 160 */       ByteBuffer oldBuffer = this.buffer;
/* 161 */       ByteBuffer newBuffer = allocateDirect(newCapacity);
/* 162 */       if (readerIndex < newCapacity) {
/* 163 */         if (writerIndex > newCapacity) {
/* 164 */           writerIndex(writerIndex = newCapacity);
/*     */         }
/* 166 */         oldBuffer.position(readerIndex).limit(writerIndex);
/* 167 */         newBuffer.position(readerIndex).limit(writerIndex);
/* 168 */         newBuffer.put(oldBuffer);
/* 169 */         newBuffer.clear();
/*     */       } else {
/* 171 */         setIndex(newCapacity, newCapacity);
/*     */       }
/* 173 */       setByteBuffer(newBuffer);
/*     */     }
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBufAllocator alloc()
/*     */   {
/* 180 */     return this.alloc;
/*     */   }
/*     */   
/*     */   public ByteOrder order()
/*     */   {
/* 185 */     return ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */   
/*     */   public boolean hasArray()
/*     */   {
/* 190 */     return false;
/*     */   }
/*     */   
/*     */   public byte[] array()
/*     */   {
/* 195 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */   
/*     */   public int arrayOffset()
/*     */   {
/* 200 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */   
/*     */   public boolean hasMemoryAddress()
/*     */   {
/* 205 */     return false;
/*     */   }
/*     */   
/*     */   public long memoryAddress()
/*     */   {
/* 210 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public byte getByte(int index)
/*     */   {
/* 215 */     ensureAccessible();
/* 216 */     return _getByte(index);
/*     */   }
/*     */   
/*     */   protected byte _getByte(int index)
/*     */   {
/* 221 */     return this.buffer.get(index);
/*     */   }
/*     */   
/*     */   public short getShort(int index)
/*     */   {
/* 226 */     ensureAccessible();
/* 227 */     return _getShort(index);
/*     */   }
/*     */   
/*     */   protected short _getShort(int index)
/*     */   {
/* 232 */     return this.buffer.getShort(index);
/*     */   }
/*     */   
/*     */   public int getUnsignedMedium(int index)
/*     */   {
/* 237 */     ensureAccessible();
/* 238 */     return _getUnsignedMedium(index);
/*     */   }
/*     */   
/*     */   protected int _getUnsignedMedium(int index)
/*     */   {
/* 243 */     return (getByte(index) & 0xFF) << 16 | (getByte(index + 1) & 0xFF) << 8 | getByte(index + 2) & 0xFF;
/*     */   }
/*     */   
/*     */   public int getInt(int index)
/*     */   {
/* 248 */     ensureAccessible();
/* 249 */     return _getInt(index);
/*     */   }
/*     */   
/*     */   protected int _getInt(int index)
/*     */   {
/* 254 */     return this.buffer.getInt(index);
/*     */   }
/*     */   
/*     */   public long getLong(int index)
/*     */   {
/* 259 */     ensureAccessible();
/* 260 */     return _getLong(index);
/*     */   }
/*     */   
/*     */   protected long _getLong(int index)
/*     */   {
/* 265 */     return this.buffer.getLong(index);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 270 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/* 271 */     if (dst.hasArray()) {
/* 272 */       getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
/* 273 */     } else if (dst.nioBufferCount() > 0) {
/* 274 */       for (ByteBuffer bb : dst.nioBuffers(dstIndex, length)) {
/* 275 */         int bbLen = bb.remaining();
/* 276 */         getBytes(index, bb);
/* 277 */         index += bbLen;
/*     */       }
/*     */     } else {
/* 280 */       dst.setBytes(dstIndex, this, index, length);
/*     */     }
/* 282 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 287 */     getBytes(index, dst, dstIndex, length, false);
/* 288 */     return this;
/*     */   }
/*     */   
/*     */   private void getBytes(int index, byte[] dst, int dstIndex, int length, boolean internal) {
/* 292 */     checkDstIndex(index, length, dstIndex, dst.length);
/*     */     
/* 294 */     if ((dstIndex < 0) || (dstIndex > dst.length - length)) {
/* 295 */       throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", new Object[] { Integer.valueOf(dstIndex), Integer.valueOf(length), Integer.valueOf(dst.length) }));
/*     */     }
/*     */     
/*     */     ByteBuffer tmpBuf;
/*     */     ByteBuffer tmpBuf;
/* 300 */     if (internal) {
/* 301 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 303 */       tmpBuf = this.buffer.duplicate();
/*     */     }
/* 305 */     tmpBuf.clear().position(index).limit(index + length);
/* 306 */     tmpBuf.get(dst, dstIndex, length);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length)
/*     */   {
/* 311 */     checkReadableBytes(length);
/* 312 */     getBytes(this.readerIndex, dst, dstIndex, length, true);
/* 313 */     this.readerIndex += length;
/* 314 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 319 */     getBytes(index, dst, false);
/* 320 */     return this;
/*     */   }
/*     */   
/*     */   private void getBytes(int index, ByteBuffer dst, boolean internal) {
/* 324 */     checkIndex(index);
/* 325 */     if (dst == null) {
/* 326 */       throw new NullPointerException("dst");
/*     */     }
/*     */     
/* 329 */     int bytesToCopy = Math.min(capacity() - index, dst.remaining());
/*     */     ByteBuffer tmpBuf;
/* 331 */     ByteBuffer tmpBuf; if (internal) {
/* 332 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 334 */       tmpBuf = this.buffer.duplicate();
/*     */     }
/* 336 */     tmpBuf.clear().position(index).limit(index + bytesToCopy);
/* 337 */     dst.put(tmpBuf);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuffer dst)
/*     */   {
/* 342 */     int length = dst.remaining();
/* 343 */     checkReadableBytes(length);
/* 344 */     getBytes(this.readerIndex, dst, true);
/* 345 */     this.readerIndex += length;
/* 346 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setByte(int index, int value)
/*     */   {
/* 351 */     ensureAccessible();
/* 352 */     _setByte(index, value);
/* 353 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setByte(int index, int value)
/*     */   {
/* 358 */     this.buffer.put(index, (byte)value);
/*     */   }
/*     */   
/*     */   public ByteBuf setShort(int index, int value)
/*     */   {
/* 363 */     ensureAccessible();
/* 364 */     _setShort(index, value);
/* 365 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setShort(int index, int value)
/*     */   {
/* 370 */     this.buffer.putShort(index, (short)value);
/*     */   }
/*     */   
/*     */   public ByteBuf setMedium(int index, int value)
/*     */   {
/* 375 */     ensureAccessible();
/* 376 */     _setMedium(index, value);
/* 377 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setMedium(int index, int value)
/*     */   {
/* 382 */     setByte(index, (byte)(value >>> 16));
/* 383 */     setByte(index + 1, (byte)(value >>> 8));
/* 384 */     setByte(index + 2, (byte)value);
/*     */   }
/*     */   
/*     */   public ByteBuf setInt(int index, int value)
/*     */   {
/* 389 */     ensureAccessible();
/* 390 */     _setInt(index, value);
/* 391 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setInt(int index, int value)
/*     */   {
/* 396 */     this.buffer.putInt(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setLong(int index, long value)
/*     */   {
/* 401 */     ensureAccessible();
/* 402 */     _setLong(index, value);
/* 403 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setLong(int index, long value)
/*     */   {
/* 408 */     this.buffer.putLong(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/* 413 */     checkSrcIndex(index, length, srcIndex, src.capacity());
/* 414 */     if (src.nioBufferCount() > 0) {
/* 415 */       for (ByteBuffer bb : src.nioBuffers(srcIndex, length)) {
/* 416 */         int bbLen = bb.remaining();
/* 417 */         setBytes(index, bb);
/* 418 */         index += bbLen;
/*     */       }
/*     */     } else {
/* 421 */       src.getBytes(srcIndex, this, index, length);
/*     */     }
/* 423 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 428 */     checkSrcIndex(index, length, srcIndex, src.length);
/* 429 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 430 */     tmpBuf.clear().position(index).limit(index + length);
/* 431 */     tmpBuf.put(src, srcIndex, length);
/* 432 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 437 */     ensureAccessible();
/* 438 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 439 */     if (src == tmpBuf) {
/* 440 */       src = src.duplicate();
/*     */     }
/*     */     
/* 443 */     tmpBuf.clear().position(index).limit(index + src.remaining());
/* 444 */     tmpBuf.put(src);
/* 445 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException
/*     */   {
/* 450 */     getBytes(index, out, length, false);
/* 451 */     return this;
/*     */   }
/*     */   
/*     */   private void getBytes(int index, OutputStream out, int length, boolean internal) throws IOException {
/* 455 */     ensureAccessible();
/* 456 */     if (length == 0) {
/* 457 */       return;
/*     */     }
/*     */     
/* 460 */     if (this.buffer.hasArray()) {
/* 461 */       out.write(this.buffer.array(), index + this.buffer.arrayOffset(), length);
/*     */     } else {
/* 463 */       byte[] tmp = new byte[length];
/*     */       ByteBuffer tmpBuf;
/* 465 */       ByteBuffer tmpBuf; if (internal) {
/* 466 */         tmpBuf = internalNioBuffer();
/*     */       } else {
/* 468 */         tmpBuf = this.buffer.duplicate();
/*     */       }
/* 470 */       tmpBuf.clear().position(index);
/* 471 */       tmpBuf.get(tmp);
/* 472 */       out.write(tmp);
/*     */     }
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(OutputStream out, int length) throws IOException
/*     */   {
/* 478 */     checkReadableBytes(length);
/* 479 */     getBytes(this.readerIndex, out, length, true);
/* 480 */     this.readerIndex += length;
/* 481 */     return this;
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 486 */     return getBytes(index, out, length, false);
/*     */   }
/*     */   
/*     */   private int getBytes(int index, GatheringByteChannel out, int length, boolean internal) throws IOException {
/* 490 */     ensureAccessible();
/* 491 */     if (length == 0) {
/* 492 */       return 0;
/*     */     }
/*     */     ByteBuffer tmpBuf;
/*     */     ByteBuffer tmpBuf;
/* 496 */     if (internal) {
/* 497 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 499 */       tmpBuf = this.buffer.duplicate();
/*     */     }
/* 501 */     tmpBuf.clear().position(index).limit(index + length);
/* 502 */     return out.write(tmpBuf);
/*     */   }
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 507 */     checkReadableBytes(length);
/* 508 */     int readBytes = getBytes(this.readerIndex, out, length, true);
/* 509 */     this.readerIndex += readBytes;
/* 510 */     return readBytes;
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException
/*     */   {
/* 515 */     ensureAccessible();
/* 516 */     if (this.buffer.hasArray()) {
/* 517 */       return in.read(this.buffer.array(), this.buffer.arrayOffset() + index, length);
/*     */     }
/* 519 */     byte[] tmp = new byte[length];
/* 520 */     int readBytes = in.read(tmp);
/* 521 */     if (readBytes <= 0) {
/* 522 */       return readBytes;
/*     */     }
/* 524 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 525 */     tmpBuf.clear().position(index);
/* 526 */     tmpBuf.put(tmp, 0, readBytes);
/* 527 */     return readBytes;
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length)
/*     */     throws IOException
/*     */   {
/* 533 */     ensureAccessible();
/* 534 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 535 */     tmpBuf.clear().position(index).limit(index + length);
/*     */     try {
/* 537 */       return in.read(this.tmpNioBuf);
/*     */     } catch (ClosedChannelException ignored) {}
/* 539 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public int nioBufferCount()
/*     */   {
/* 545 */     return 1;
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 550 */     return new ByteBuffer[] { nioBuffer(index, length) };
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 555 */     ensureAccessible();
/*     */     ByteBuffer src;
/*     */     try {
/* 558 */       src = (ByteBuffer)this.buffer.duplicate().clear().position(index).limit(index + length);
/*     */     } catch (IllegalArgumentException ignored) {
/* 560 */       throw new IndexOutOfBoundsException("Too many bytes to read - Need " + (index + length));
/*     */     }
/*     */     
/* 563 */     return alloc().directBuffer(length, maxCapacity()).writeBytes(src);
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 568 */     checkIndex(index, length);
/* 569 */     return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */   }
/*     */   
/*     */   private ByteBuffer internalNioBuffer() {
/* 573 */     ByteBuffer tmpNioBuf = this.tmpNioBuf;
/* 574 */     if (tmpNioBuf == null) {
/* 575 */       this.tmpNioBuf = (tmpNioBuf = this.buffer.duplicate());
/*     */     }
/* 577 */     return tmpNioBuf;
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length)
/*     */   {
/* 582 */     checkIndex(index, length);
/* 583 */     return ((ByteBuffer)this.buffer.duplicate().position(index).limit(index + length)).slice();
/*     */   }
/*     */   
/*     */   protected void deallocate()
/*     */   {
/* 588 */     ByteBuffer buffer = this.buffer;
/* 589 */     if (buffer == null) {
/* 590 */       return;
/*     */     }
/*     */     
/* 593 */     this.buffer = null;
/*     */     
/* 595 */     if (!this.doNotFree) {
/* 596 */       freeDirect(buffer);
/*     */     }
/*     */   }
/*     */   
/*     */   public ByteBuf unwrap()
/*     */   {
/* 602 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\UnpooledDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */