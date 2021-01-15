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
/*     */ public class UnpooledUnsafeDirectByteBuf
/*     */   extends AbstractReferenceCountedByteBuf
/*     */ {
/*  36 */   private static final boolean NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
/*     */   
/*     */   private final ByteBufAllocator alloc;
/*     */   
/*     */   private long memoryAddress;
/*     */   
/*     */   private ByteBuffer buffer;
/*     */   
/*     */   private ByteBuffer tmpNioBuf;
/*     */   
/*     */   private int capacity;
/*     */   
/*     */   private boolean doNotFree;
/*     */   
/*     */ 
/*     */   protected UnpooledUnsafeDirectByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity)
/*     */   {
/*  53 */     super(maxCapacity);
/*  54 */     if (alloc == null) {
/*  55 */       throw new NullPointerException("alloc");
/*     */     }
/*  57 */     if (initialCapacity < 0) {
/*  58 */       throw new IllegalArgumentException("initialCapacity: " + initialCapacity);
/*     */     }
/*  60 */     if (maxCapacity < 0) {
/*  61 */       throw new IllegalArgumentException("maxCapacity: " + maxCapacity);
/*     */     }
/*  63 */     if (initialCapacity > maxCapacity) {
/*  64 */       throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[] { Integer.valueOf(initialCapacity), Integer.valueOf(maxCapacity) }));
/*     */     }
/*     */     
/*     */ 
/*  68 */     this.alloc = alloc;
/*  69 */     setByteBuffer(allocateDirect(initialCapacity));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected UnpooledUnsafeDirectByteBuf(ByteBufAllocator alloc, ByteBuffer initialBuffer, int maxCapacity)
/*     */   {
/*  78 */     super(maxCapacity);
/*  79 */     if (alloc == null) {
/*  80 */       throw new NullPointerException("alloc");
/*     */     }
/*  82 */     if (initialBuffer == null) {
/*  83 */       throw new NullPointerException("initialBuffer");
/*     */     }
/*  85 */     if (!initialBuffer.isDirect()) {
/*  86 */       throw new IllegalArgumentException("initialBuffer is not a direct buffer.");
/*     */     }
/*  88 */     if (initialBuffer.isReadOnly()) {
/*  89 */       throw new IllegalArgumentException("initialBuffer is a read-only buffer.");
/*     */     }
/*     */     
/*  92 */     int initialCapacity = initialBuffer.remaining();
/*  93 */     if (initialCapacity > maxCapacity) {
/*  94 */       throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[] { Integer.valueOf(initialCapacity), Integer.valueOf(maxCapacity) }));
/*     */     }
/*     */     
/*     */ 
/*  98 */     this.alloc = alloc;
/*  99 */     this.doNotFree = true;
/* 100 */     setByteBuffer(initialBuffer.slice().order(ByteOrder.BIG_ENDIAN));
/* 101 */     writerIndex(initialCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ByteBuffer allocateDirect(int initialCapacity)
/*     */   {
/* 108 */     return ByteBuffer.allocateDirect(initialCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void freeDirect(ByteBuffer buffer)
/*     */   {
/* 115 */     PlatformDependent.freeDirectBuffer(buffer);
/*     */   }
/*     */   
/*     */   private void setByteBuffer(ByteBuffer buffer) {
/* 119 */     ByteBuffer oldBuffer = this.buffer;
/* 120 */     if (oldBuffer != null) {
/* 121 */       if (this.doNotFree) {
/* 122 */         this.doNotFree = false;
/*     */       } else {
/* 124 */         freeDirect(oldBuffer);
/*     */       }
/*     */     }
/*     */     
/* 128 */     this.buffer = buffer;
/* 129 */     this.memoryAddress = PlatformDependent.directBufferAddress(buffer);
/* 130 */     this.tmpNioBuf = null;
/* 131 */     this.capacity = buffer.remaining();
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/* 136 */     return true;
/*     */   }
/*     */   
/*     */   public int capacity()
/*     */   {
/* 141 */     return this.capacity;
/*     */   }
/*     */   
/*     */   public ByteBuf capacity(int newCapacity)
/*     */   {
/* 146 */     ensureAccessible();
/* 147 */     if ((newCapacity < 0) || (newCapacity > maxCapacity())) {
/* 148 */       throw new IllegalArgumentException("newCapacity: " + newCapacity);
/*     */     }
/*     */     
/* 151 */     int readerIndex = readerIndex();
/* 152 */     int writerIndex = writerIndex();
/*     */     
/* 154 */     int oldCapacity = this.capacity;
/* 155 */     if (newCapacity > oldCapacity) {
/* 156 */       ByteBuffer oldBuffer = this.buffer;
/* 157 */       ByteBuffer newBuffer = allocateDirect(newCapacity);
/* 158 */       oldBuffer.position(0).limit(oldBuffer.capacity());
/* 159 */       newBuffer.position(0).limit(oldBuffer.capacity());
/* 160 */       newBuffer.put(oldBuffer);
/* 161 */       newBuffer.clear();
/* 162 */       setByteBuffer(newBuffer);
/* 163 */     } else if (newCapacity < oldCapacity) {
/* 164 */       ByteBuffer oldBuffer = this.buffer;
/* 165 */       ByteBuffer newBuffer = allocateDirect(newCapacity);
/* 166 */       if (readerIndex < newCapacity) {
/* 167 */         if (writerIndex > newCapacity) {
/* 168 */           writerIndex(writerIndex = newCapacity);
/*     */         }
/* 170 */         oldBuffer.position(readerIndex).limit(writerIndex);
/* 171 */         newBuffer.position(readerIndex).limit(writerIndex);
/* 172 */         newBuffer.put(oldBuffer);
/* 173 */         newBuffer.clear();
/*     */       } else {
/* 175 */         setIndex(newCapacity, newCapacity);
/*     */       }
/* 177 */       setByteBuffer(newBuffer);
/*     */     }
/* 179 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBufAllocator alloc()
/*     */   {
/* 184 */     return this.alloc;
/*     */   }
/*     */   
/*     */   public ByteOrder order()
/*     */   {
/* 189 */     return ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */   
/*     */   public boolean hasArray()
/*     */   {
/* 194 */     return false;
/*     */   }
/*     */   
/*     */   public byte[] array()
/*     */   {
/* 199 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */   
/*     */   public int arrayOffset()
/*     */   {
/* 204 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */   
/*     */   public boolean hasMemoryAddress()
/*     */   {
/* 209 */     return true;
/*     */   }
/*     */   
/*     */   public long memoryAddress()
/*     */   {
/* 214 */     ensureAccessible();
/* 215 */     return this.memoryAddress;
/*     */   }
/*     */   
/*     */   protected byte _getByte(int index)
/*     */   {
/* 220 */     return PlatformDependent.getByte(addr(index));
/*     */   }
/*     */   
/*     */   protected short _getShort(int index)
/*     */   {
/* 225 */     short v = PlatformDependent.getShort(addr(index));
/* 226 */     return NATIVE_ORDER ? v : Short.reverseBytes(v);
/*     */   }
/*     */   
/*     */   protected int _getUnsignedMedium(int index)
/*     */   {
/* 231 */     long addr = addr(index);
/* 232 */     return (PlatformDependent.getByte(addr) & 0xFF) << 16 | (PlatformDependent.getByte(addr + 1L) & 0xFF) << 8 | PlatformDependent.getByte(addr + 2L) & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int _getInt(int index)
/*     */   {
/* 239 */     int v = PlatformDependent.getInt(addr(index));
/* 240 */     return NATIVE_ORDER ? v : Integer.reverseBytes(v);
/*     */   }
/*     */   
/*     */   protected long _getLong(int index)
/*     */   {
/* 245 */     long v = PlatformDependent.getLong(addr(index));
/* 246 */     return NATIVE_ORDER ? v : Long.reverseBytes(v);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 251 */     checkIndex(index, length);
/* 252 */     if (dst == null) {
/* 253 */       throw new NullPointerException("dst");
/*     */     }
/* 255 */     if ((dstIndex < 0) || (dstIndex > dst.capacity() - length)) {
/* 256 */       throw new IndexOutOfBoundsException("dstIndex: " + dstIndex);
/*     */     }
/*     */     
/* 259 */     if (dst.hasMemoryAddress()) {
/* 260 */       PlatformDependent.copyMemory(addr(index), dst.memoryAddress() + dstIndex, length);
/* 261 */     } else if (dst.hasArray()) {
/* 262 */       PlatformDependent.copyMemory(addr(index), dst.array(), dst.arrayOffset() + dstIndex, length);
/*     */     } else {
/* 264 */       dst.setBytes(dstIndex, this, index, length);
/*     */     }
/* 266 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 271 */     checkIndex(index, length);
/* 272 */     if (dst == null) {
/* 273 */       throw new NullPointerException("dst");
/*     */     }
/* 275 */     if ((dstIndex < 0) || (dstIndex > dst.length - length)) {
/* 276 */       throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", new Object[] { Integer.valueOf(dstIndex), Integer.valueOf(length), Integer.valueOf(dst.length) }));
/*     */     }
/*     */     
/*     */ 
/* 280 */     if (length != 0) {
/* 281 */       PlatformDependent.copyMemory(addr(index), dst, dstIndex, length);
/*     */     }
/* 283 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 288 */     getBytes(index, dst, false);
/* 289 */     return this;
/*     */   }
/*     */   
/*     */   private void getBytes(int index, ByteBuffer dst, boolean internal) {
/* 293 */     checkIndex(index);
/* 294 */     if (dst == null) {
/* 295 */       throw new NullPointerException("dst");
/*     */     }
/*     */     
/* 298 */     int bytesToCopy = Math.min(capacity() - index, dst.remaining());
/*     */     ByteBuffer tmpBuf;
/* 300 */     ByteBuffer tmpBuf; if (internal) {
/* 301 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 303 */       tmpBuf = this.buffer.duplicate();
/*     */     }
/* 305 */     tmpBuf.clear().position(index).limit(index + bytesToCopy);
/* 306 */     dst.put(tmpBuf);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuffer dst)
/*     */   {
/* 311 */     int length = dst.remaining();
/* 312 */     checkReadableBytes(length);
/* 313 */     getBytes(this.readerIndex, dst, true);
/* 314 */     this.readerIndex += length;
/* 315 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setByte(int index, int value)
/*     */   {
/* 320 */     PlatformDependent.putByte(addr(index), (byte)value);
/*     */   }
/*     */   
/*     */   protected void _setShort(int index, int value)
/*     */   {
/* 325 */     PlatformDependent.putShort(addr(index), NATIVE_ORDER ? (short)value : Short.reverseBytes((short)value));
/*     */   }
/*     */   
/*     */   protected void _setMedium(int index, int value)
/*     */   {
/* 330 */     long addr = addr(index);
/* 331 */     PlatformDependent.putByte(addr, (byte)(value >>> 16));
/* 332 */     PlatformDependent.putByte(addr + 1L, (byte)(value >>> 8));
/* 333 */     PlatformDependent.putByte(addr + 2L, (byte)value);
/*     */   }
/*     */   
/*     */   protected void _setInt(int index, int value)
/*     */   {
/* 338 */     PlatformDependent.putInt(addr(index), NATIVE_ORDER ? value : Integer.reverseBytes(value));
/*     */   }
/*     */   
/*     */   protected void _setLong(int index, long value)
/*     */   {
/* 343 */     PlatformDependent.putLong(addr(index), NATIVE_ORDER ? value : Long.reverseBytes(value));
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/* 348 */     checkIndex(index, length);
/* 349 */     if (src == null) {
/* 350 */       throw new NullPointerException("src");
/*     */     }
/* 352 */     if ((srcIndex < 0) || (srcIndex > src.capacity() - length)) {
/* 353 */       throw new IndexOutOfBoundsException("srcIndex: " + srcIndex);
/*     */     }
/*     */     
/* 356 */     if (length != 0) {
/* 357 */       if (src.hasMemoryAddress()) {
/* 358 */         PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, addr(index), length);
/* 359 */       } else if (src.hasArray()) {
/* 360 */         PlatformDependent.copyMemory(src.array(), src.arrayOffset() + srcIndex, addr(index), length);
/*     */       } else {
/* 362 */         src.getBytes(srcIndex, this, index, length);
/*     */       }
/*     */     }
/* 365 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 370 */     checkIndex(index, length);
/* 371 */     if (length != 0) {
/* 372 */       PlatformDependent.copyMemory(src, srcIndex, addr(index), length);
/*     */     }
/* 374 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 379 */     ensureAccessible();
/* 380 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 381 */     if (src == tmpBuf) {
/* 382 */       src = src.duplicate();
/*     */     }
/*     */     
/* 385 */     tmpBuf.clear().position(index).limit(index + src.remaining());
/* 386 */     tmpBuf.put(src);
/* 387 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException
/*     */   {
/* 392 */     ensureAccessible();
/* 393 */     if (length != 0) {
/* 394 */       byte[] tmp = new byte[length];
/* 395 */       PlatformDependent.copyMemory(addr(index), tmp, 0, length);
/* 396 */       out.write(tmp);
/*     */     }
/* 398 */     return this;
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 403 */     return getBytes(index, out, length, false);
/*     */   }
/*     */   
/*     */   private int getBytes(int index, GatheringByteChannel out, int length, boolean internal) throws IOException {
/* 407 */     ensureAccessible();
/* 408 */     if (length == 0) {
/* 409 */       return 0;
/*     */     }
/*     */     ByteBuffer tmpBuf;
/*     */     ByteBuffer tmpBuf;
/* 413 */     if (internal) {
/* 414 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 416 */       tmpBuf = this.buffer.duplicate();
/*     */     }
/* 418 */     tmpBuf.clear().position(index).limit(index + length);
/* 419 */     return out.write(tmpBuf);
/*     */   }
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 424 */     checkReadableBytes(length);
/* 425 */     int readBytes = getBytes(this.readerIndex, out, length, true);
/* 426 */     this.readerIndex += readBytes;
/* 427 */     return readBytes;
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException
/*     */   {
/* 432 */     checkIndex(index, length);
/* 433 */     byte[] tmp = new byte[length];
/* 434 */     int readBytes = in.read(tmp);
/* 435 */     if (readBytes > 0) {
/* 436 */       PlatformDependent.copyMemory(tmp, 0, addr(index), readBytes);
/*     */     }
/* 438 */     return readBytes;
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException
/*     */   {
/* 443 */     ensureAccessible();
/* 444 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 445 */     tmpBuf.clear().position(index).limit(index + length);
/*     */     try {
/* 447 */       return in.read(tmpBuf);
/*     */     } catch (ClosedChannelException ignored) {}
/* 449 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public int nioBufferCount()
/*     */   {
/* 455 */     return 1;
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 460 */     return new ByteBuffer[] { nioBuffer(index, length) };
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 465 */     checkIndex(index, length);
/* 466 */     ByteBuf copy = alloc().directBuffer(length, maxCapacity());
/* 467 */     if (length != 0) {
/* 468 */       if (copy.hasMemoryAddress()) {
/* 469 */         PlatformDependent.copyMemory(addr(index), copy.memoryAddress(), length);
/* 470 */         copy.setIndex(0, length);
/*     */       } else {
/* 472 */         copy.writeBytes(this, index, length);
/*     */       }
/*     */     }
/* 475 */     return copy;
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 480 */     checkIndex(index, length);
/* 481 */     return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */   }
/*     */   
/*     */   private ByteBuffer internalNioBuffer() {
/* 485 */     ByteBuffer tmpNioBuf = this.tmpNioBuf;
/* 486 */     if (tmpNioBuf == null) {
/* 487 */       this.tmpNioBuf = (tmpNioBuf = this.buffer.duplicate());
/*     */     }
/* 489 */     return tmpNioBuf;
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length)
/*     */   {
/* 494 */     checkIndex(index, length);
/* 495 */     return ((ByteBuffer)this.buffer.duplicate().position(index).limit(index + length)).slice();
/*     */   }
/*     */   
/*     */   protected void deallocate()
/*     */   {
/* 500 */     ByteBuffer buffer = this.buffer;
/* 501 */     if (buffer == null) {
/* 502 */       return;
/*     */     }
/*     */     
/* 505 */     this.buffer = null;
/*     */     
/* 507 */     if (!this.doNotFree) {
/* 508 */       freeDirect(buffer);
/*     */     }
/*     */   }
/*     */   
/*     */   public ByteBuf unwrap()
/*     */   {
/* 514 */     return null;
/*     */   }
/*     */   
/*     */   long addr(int index) {
/* 518 */     return this.memoryAddress + index;
/*     */   }
/*     */   
/*     */   protected SwappedByteBuf newSwappedByteBuf()
/*     */   {
/* 523 */     return new UnsafeDirectSwappedByteBuf(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\UnpooledUnsafeDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */