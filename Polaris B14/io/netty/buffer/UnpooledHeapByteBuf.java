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
/*     */ public class UnpooledHeapByteBuf
/*     */   extends AbstractReferenceCountedByteBuf
/*     */ {
/*     */   private final ByteBufAllocator alloc;
/*     */   private byte[] array;
/*     */   private ByteBuffer tmpNioBuf;
/*     */   
/*     */   protected UnpooledHeapByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity)
/*     */   {
/*  45 */     this(alloc, new byte[initialCapacity], 0, 0, maxCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected UnpooledHeapByteBuf(ByteBufAllocator alloc, byte[] initialArray, int maxCapacity)
/*     */   {
/*  55 */     this(alloc, initialArray, 0, initialArray.length, maxCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */   private UnpooledHeapByteBuf(ByteBufAllocator alloc, byte[] initialArray, int readerIndex, int writerIndex, int maxCapacity)
/*     */   {
/*  61 */     super(maxCapacity);
/*     */     
/*  63 */     if (alloc == null) {
/*  64 */       throw new NullPointerException("alloc");
/*     */     }
/*  66 */     if (initialArray == null) {
/*  67 */       throw new NullPointerException("initialArray");
/*     */     }
/*  69 */     if (initialArray.length > maxCapacity) {
/*  70 */       throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", new Object[] { Integer.valueOf(initialArray.length), Integer.valueOf(maxCapacity) }));
/*     */     }
/*     */     
/*     */ 
/*  74 */     this.alloc = alloc;
/*  75 */     setArray(initialArray);
/*  76 */     setIndex(readerIndex, writerIndex);
/*     */   }
/*     */   
/*     */   private void setArray(byte[] initialArray) {
/*  80 */     this.array = initialArray;
/*  81 */     this.tmpNioBuf = null;
/*     */   }
/*     */   
/*     */   public ByteBufAllocator alloc()
/*     */   {
/*  86 */     return this.alloc;
/*     */   }
/*     */   
/*     */   public ByteOrder order()
/*     */   {
/*  91 */     return ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/*  96 */     return false;
/*     */   }
/*     */   
/*     */   public int capacity()
/*     */   {
/* 101 */     ensureAccessible();
/* 102 */     return this.array.length;
/*     */   }
/*     */   
/*     */   public ByteBuf capacity(int newCapacity)
/*     */   {
/* 107 */     ensureAccessible();
/* 108 */     if ((newCapacity < 0) || (newCapacity > maxCapacity())) {
/* 109 */       throw new IllegalArgumentException("newCapacity: " + newCapacity);
/*     */     }
/*     */     
/* 112 */     int oldCapacity = this.array.length;
/* 113 */     if (newCapacity > oldCapacity) {
/* 114 */       byte[] newArray = new byte[newCapacity];
/* 115 */       System.arraycopy(this.array, 0, newArray, 0, this.array.length);
/* 116 */       setArray(newArray);
/* 117 */     } else if (newCapacity < oldCapacity) {
/* 118 */       byte[] newArray = new byte[newCapacity];
/* 119 */       int readerIndex = readerIndex();
/* 120 */       if (readerIndex < newCapacity) {
/* 121 */         int writerIndex = writerIndex();
/* 122 */         if (writerIndex > newCapacity) {
/* 123 */           writerIndex(writerIndex = newCapacity);
/*     */         }
/* 125 */         System.arraycopy(this.array, readerIndex, newArray, readerIndex, writerIndex - readerIndex);
/*     */       } else {
/* 127 */         setIndex(newCapacity, newCapacity);
/*     */       }
/* 129 */       setArray(newArray);
/*     */     }
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   public boolean hasArray()
/*     */   {
/* 136 */     return true;
/*     */   }
/*     */   
/*     */   public byte[] array()
/*     */   {
/* 141 */     ensureAccessible();
/* 142 */     return this.array;
/*     */   }
/*     */   
/*     */   public int arrayOffset()
/*     */   {
/* 147 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean hasMemoryAddress()
/*     */   {
/* 152 */     return false;
/*     */   }
/*     */   
/*     */   public long memoryAddress()
/*     */   {
/* 157 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 162 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/* 163 */     if (dst.hasMemoryAddress()) {
/* 164 */       PlatformDependent.copyMemory(this.array, index, dst.memoryAddress() + dstIndex, length);
/* 165 */     } else if (dst.hasArray()) {
/* 166 */       getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
/*     */     } else {
/* 168 */       dst.setBytes(dstIndex, this.array, index, length);
/*     */     }
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 175 */     checkDstIndex(index, length, dstIndex, dst.length);
/* 176 */     System.arraycopy(this.array, index, dst, dstIndex, length);
/* 177 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 182 */     ensureAccessible();
/* 183 */     dst.put(this.array, index, Math.min(capacity() - index, dst.remaining()));
/* 184 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException
/*     */   {
/* 189 */     ensureAccessible();
/* 190 */     out.write(this.array, index, length);
/* 191 */     return this;
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 196 */     ensureAccessible();
/* 197 */     return getBytes(index, out, length, false);
/*     */   }
/*     */   
/*     */   private int getBytes(int index, GatheringByteChannel out, int length, boolean internal) throws IOException {
/* 201 */     ensureAccessible();
/*     */     ByteBuffer tmpBuf;
/* 203 */     ByteBuffer tmpBuf; if (internal) {
/* 204 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 206 */       tmpBuf = ByteBuffer.wrap(this.array);
/*     */     }
/* 208 */     return out.write((ByteBuffer)tmpBuf.clear().position(index).limit(index + length));
/*     */   }
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 213 */     checkReadableBytes(length);
/* 214 */     int readBytes = getBytes(this.readerIndex, out, length, true);
/* 215 */     this.readerIndex += readBytes;
/* 216 */     return readBytes;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/* 221 */     checkSrcIndex(index, length, srcIndex, src.capacity());
/* 222 */     if (src.hasMemoryAddress()) {
/* 223 */       PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, this.array, index, length);
/* 224 */     } else if (src.hasArray()) {
/* 225 */       setBytes(index, src.array(), src.arrayOffset() + srcIndex, length);
/*     */     } else {
/* 227 */       src.getBytes(srcIndex, this.array, index, length);
/*     */     }
/* 229 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 234 */     checkSrcIndex(index, length, srcIndex, src.length);
/* 235 */     System.arraycopy(src, srcIndex, this.array, index, length);
/* 236 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 241 */     ensureAccessible();
/* 242 */     src.get(this.array, index, src.remaining());
/* 243 */     return this;
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException
/*     */   {
/* 248 */     ensureAccessible();
/* 249 */     return in.read(this.array, index, length);
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException
/*     */   {
/* 254 */     ensureAccessible();
/*     */     try {
/* 256 */       return in.read((ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length));
/*     */     } catch (ClosedChannelException ignored) {}
/* 258 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public int nioBufferCount()
/*     */   {
/* 264 */     return 1;
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length)
/*     */   {
/* 269 */     ensureAccessible();
/* 270 */     return ByteBuffer.wrap(this.array, index, length).slice();
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 275 */     return new ByteBuffer[] { nioBuffer(index, length) };
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 280 */     checkIndex(index, length);
/* 281 */     return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */   }
/*     */   
/*     */   public byte getByte(int index)
/*     */   {
/* 286 */     ensureAccessible();
/* 287 */     return _getByte(index);
/*     */   }
/*     */   
/*     */   protected byte _getByte(int index)
/*     */   {
/* 292 */     return this.array[index];
/*     */   }
/*     */   
/*     */   public short getShort(int index)
/*     */   {
/* 297 */     ensureAccessible();
/* 298 */     return _getShort(index);
/*     */   }
/*     */   
/*     */   protected short _getShort(int index)
/*     */   {
/* 303 */     return (short)(this.array[index] << 8 | this.array[(index + 1)] & 0xFF);
/*     */   }
/*     */   
/*     */   public int getUnsignedMedium(int index)
/*     */   {
/* 308 */     ensureAccessible();
/* 309 */     return _getUnsignedMedium(index);
/*     */   }
/*     */   
/*     */   protected int _getUnsignedMedium(int index)
/*     */   {
/* 314 */     return (this.array[index] & 0xFF) << 16 | (this.array[(index + 1)] & 0xFF) << 8 | this.array[(index + 2)] & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getInt(int index)
/*     */   {
/* 321 */     ensureAccessible();
/* 322 */     return _getInt(index);
/*     */   }
/*     */   
/*     */   protected int _getInt(int index)
/*     */   {
/* 327 */     return (this.array[index] & 0xFF) << 24 | (this.array[(index + 1)] & 0xFF) << 16 | (this.array[(index + 2)] & 0xFF) << 8 | this.array[(index + 3)] & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLong(int index)
/*     */   {
/* 335 */     ensureAccessible();
/* 336 */     return _getLong(index);
/*     */   }
/*     */   
/*     */   protected long _getLong(int index)
/*     */   {
/* 341 */     return (this.array[index] & 0xFF) << 56 | (this.array[(index + 1)] & 0xFF) << 48 | (this.array[(index + 2)] & 0xFF) << 40 | (this.array[(index + 3)] & 0xFF) << 32 | (this.array[(index + 4)] & 0xFF) << 24 | (this.array[(index + 5)] & 0xFF) << 16 | (this.array[(index + 6)] & 0xFF) << 8 | this.array[(index + 7)] & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteBuf setByte(int index, int value)
/*     */   {
/* 353 */     ensureAccessible();
/* 354 */     _setByte(index, value);
/* 355 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setByte(int index, int value)
/*     */   {
/* 360 */     this.array[index] = ((byte)value);
/*     */   }
/*     */   
/*     */   public ByteBuf setShort(int index, int value)
/*     */   {
/* 365 */     ensureAccessible();
/* 366 */     _setShort(index, value);
/* 367 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setShort(int index, int value)
/*     */   {
/* 372 */     this.array[index] = ((byte)(value >>> 8));
/* 373 */     this.array[(index + 1)] = ((byte)value);
/*     */   }
/*     */   
/*     */   public ByteBuf setMedium(int index, int value)
/*     */   {
/* 378 */     ensureAccessible();
/* 379 */     _setMedium(index, value);
/* 380 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setMedium(int index, int value)
/*     */   {
/* 385 */     this.array[index] = ((byte)(value >>> 16));
/* 386 */     this.array[(index + 1)] = ((byte)(value >>> 8));
/* 387 */     this.array[(index + 2)] = ((byte)value);
/*     */   }
/*     */   
/*     */   public ByteBuf setInt(int index, int value)
/*     */   {
/* 392 */     ensureAccessible();
/* 393 */     _setInt(index, value);
/* 394 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setInt(int index, int value)
/*     */   {
/* 399 */     this.array[index] = ((byte)(value >>> 24));
/* 400 */     this.array[(index + 1)] = ((byte)(value >>> 16));
/* 401 */     this.array[(index + 2)] = ((byte)(value >>> 8));
/* 402 */     this.array[(index + 3)] = ((byte)value);
/*     */   }
/*     */   
/*     */   public ByteBuf setLong(int index, long value)
/*     */   {
/* 407 */     ensureAccessible();
/* 408 */     _setLong(index, value);
/* 409 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setLong(int index, long value)
/*     */   {
/* 414 */     this.array[index] = ((byte)(int)(value >>> 56));
/* 415 */     this.array[(index + 1)] = ((byte)(int)(value >>> 48));
/* 416 */     this.array[(index + 2)] = ((byte)(int)(value >>> 40));
/* 417 */     this.array[(index + 3)] = ((byte)(int)(value >>> 32));
/* 418 */     this.array[(index + 4)] = ((byte)(int)(value >>> 24));
/* 419 */     this.array[(index + 5)] = ((byte)(int)(value >>> 16));
/* 420 */     this.array[(index + 6)] = ((byte)(int)(value >>> 8));
/* 421 */     this.array[(index + 7)] = ((byte)(int)value);
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 426 */     checkIndex(index, length);
/* 427 */     byte[] copiedArray = new byte[length];
/* 428 */     System.arraycopy(this.array, index, copiedArray, 0, length);
/* 429 */     return new UnpooledHeapByteBuf(alloc(), copiedArray, maxCapacity());
/*     */   }
/*     */   
/*     */   private ByteBuffer internalNioBuffer() {
/* 433 */     ByteBuffer tmpNioBuf = this.tmpNioBuf;
/* 434 */     if (tmpNioBuf == null) {
/* 435 */       this.tmpNioBuf = (tmpNioBuf = ByteBuffer.wrap(this.array));
/*     */     }
/* 437 */     return tmpNioBuf;
/*     */   }
/*     */   
/*     */   protected void deallocate()
/*     */   {
/* 442 */     this.array = null;
/*     */   }
/*     */   
/*     */   public ByteBuf unwrap()
/*     */   {
/* 447 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\UnpooledHeapByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */