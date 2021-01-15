/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.Recycler.Handle;
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
/*     */ final class PooledUnsafeDirectByteBuf
/*     */   extends PooledByteBuf<ByteBuffer>
/*     */ {
/*  33 */   private static final boolean NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
/*     */   
/*  35 */   private static final Recycler<PooledUnsafeDirectByteBuf> RECYCLER = new Recycler()
/*     */   {
/*     */ 
/*  38 */     protected PooledUnsafeDirectByteBuf newObject(Recycler.Handle<PooledUnsafeDirectByteBuf> handle) { return new PooledUnsafeDirectByteBuf(handle, 0, null); }
/*     */   };
/*     */   private long memoryAddress;
/*     */   
/*     */   static PooledUnsafeDirectByteBuf newInstance(int maxCapacity) {
/*  43 */     PooledUnsafeDirectByteBuf buf = (PooledUnsafeDirectByteBuf)RECYCLER.get();
/*  44 */     buf.setRefCnt(1);
/*  45 */     buf.maxCapacity(maxCapacity);
/*  46 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */   private PooledUnsafeDirectByteBuf(Recycler.Handle<PooledUnsafeDirectByteBuf> recyclerHandle, int maxCapacity)
/*     */   {
/*  52 */     super(recyclerHandle, maxCapacity);
/*     */   }
/*     */   
/*     */   void init(PoolChunk<ByteBuffer> chunk, long handle, int offset, int length, int maxLength)
/*     */   {
/*  57 */     super.init(chunk, handle, offset, length, maxLength);
/*  58 */     initMemoryAddress();
/*     */   }
/*     */   
/*     */   void initUnpooled(PoolChunk<ByteBuffer> chunk, int length)
/*     */   {
/*  63 */     super.initUnpooled(chunk, length);
/*  64 */     initMemoryAddress();
/*     */   }
/*     */   
/*     */   private void initMemoryAddress() {
/*  68 */     this.memoryAddress = (PlatformDependent.directBufferAddress((ByteBuffer)this.memory) + this.offset);
/*     */   }
/*     */   
/*     */   protected ByteBuffer newInternalNioBuffer(ByteBuffer memory)
/*     */   {
/*  73 */     return memory.duplicate();
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/*  78 */     return true;
/*     */   }
/*     */   
/*     */   protected byte _getByte(int index)
/*     */   {
/*  83 */     return PlatformDependent.getByte(addr(index));
/*     */   }
/*     */   
/*     */   protected short _getShort(int index)
/*     */   {
/*  88 */     short v = PlatformDependent.getShort(addr(index));
/*  89 */     return NATIVE_ORDER ? v : Short.reverseBytes(v);
/*     */   }
/*     */   
/*     */   protected int _getUnsignedMedium(int index)
/*     */   {
/*  94 */     long addr = addr(index);
/*  95 */     return (PlatformDependent.getByte(addr) & 0xFF) << 16 | (PlatformDependent.getByte(addr + 1L) & 0xFF) << 8 | PlatformDependent.getByte(addr + 2L) & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int _getInt(int index)
/*     */   {
/* 102 */     int v = PlatformDependent.getInt(addr(index));
/* 103 */     return NATIVE_ORDER ? v : Integer.reverseBytes(v);
/*     */   }
/*     */   
/*     */   protected long _getLong(int index)
/*     */   {
/* 108 */     long v = PlatformDependent.getLong(addr(index));
/* 109 */     return NATIVE_ORDER ? v : Long.reverseBytes(v);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 114 */     checkIndex(index, length);
/* 115 */     if (dst == null) {
/* 116 */       throw new NullPointerException("dst");
/*     */     }
/* 118 */     if ((dstIndex < 0) || (dstIndex > dst.capacity() - length)) {
/* 119 */       throw new IndexOutOfBoundsException("dstIndex: " + dstIndex);
/*     */     }
/*     */     
/* 122 */     if (length != 0) {
/* 123 */       if (dst.hasMemoryAddress()) {
/* 124 */         PlatformDependent.copyMemory(addr(index), dst.memoryAddress() + dstIndex, length);
/* 125 */       } else if (dst.hasArray()) {
/* 126 */         PlatformDependent.copyMemory(addr(index), dst.array(), dst.arrayOffset() + dstIndex, length);
/*     */       } else {
/* 128 */         dst.setBytes(dstIndex, this, index, length);
/*     */       }
/*     */     }
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 136 */     checkIndex(index, length);
/* 137 */     if (dst == null) {
/* 138 */       throw new NullPointerException("dst");
/*     */     }
/* 140 */     if ((dstIndex < 0) || (dstIndex > dst.length - length)) {
/* 141 */       throw new IndexOutOfBoundsException("dstIndex: " + dstIndex);
/*     */     }
/* 143 */     if (length != 0) {
/* 144 */       PlatformDependent.copyMemory(addr(index), dst, dstIndex, length);
/*     */     }
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 151 */     getBytes(index, dst, false);
/* 152 */     return this;
/*     */   }
/*     */   
/*     */   private void getBytes(int index, ByteBuffer dst, boolean internal) {
/* 156 */     checkIndex(index);
/* 157 */     int bytesToCopy = Math.min(capacity() - index, dst.remaining());
/*     */     ByteBuffer tmpBuf;
/* 159 */     ByteBuffer tmpBuf; if (internal) {
/* 160 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 162 */       tmpBuf = ((ByteBuffer)this.memory).duplicate();
/*     */     }
/* 164 */     index = idx(index);
/* 165 */     tmpBuf.clear().position(index).limit(index + bytesToCopy);
/* 166 */     dst.put(tmpBuf);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuffer dst)
/*     */   {
/* 171 */     int length = dst.remaining();
/* 172 */     checkReadableBytes(length);
/* 173 */     getBytes(this.readerIndex, dst, true);
/* 174 */     this.readerIndex += length;
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException
/*     */   {
/* 180 */     checkIndex(index, length);
/* 181 */     if (length != 0) {
/* 182 */       byte[] tmp = new byte[length];
/* 183 */       PlatformDependent.copyMemory(addr(index), tmp, 0, length);
/* 184 */       out.write(tmp);
/*     */     }
/* 186 */     return this;
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 191 */     return getBytes(index, out, length, false);
/*     */   }
/*     */   
/*     */   private int getBytes(int index, GatheringByteChannel out, int length, boolean internal) throws IOException {
/* 195 */     checkIndex(index, length);
/* 196 */     if (length == 0) {
/* 197 */       return 0;
/*     */     }
/*     */     ByteBuffer tmpBuf;
/*     */     ByteBuffer tmpBuf;
/* 201 */     if (internal) {
/* 202 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 204 */       tmpBuf = ((ByteBuffer)this.memory).duplicate();
/*     */     }
/* 206 */     index = idx(index);
/* 207 */     tmpBuf.clear().position(index).limit(index + length);
/* 208 */     return out.write(tmpBuf);
/*     */   }
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length)
/*     */     throws IOException
/*     */   {
/* 214 */     checkReadableBytes(length);
/* 215 */     int readBytes = getBytes(this.readerIndex, out, length, true);
/* 216 */     this.readerIndex += readBytes;
/* 217 */     return readBytes;
/*     */   }
/*     */   
/*     */   protected void _setByte(int index, int value)
/*     */   {
/* 222 */     PlatformDependent.putByte(addr(index), (byte)value);
/*     */   }
/*     */   
/*     */   protected void _setShort(int index, int value)
/*     */   {
/* 227 */     PlatformDependent.putShort(addr(index), NATIVE_ORDER ? (short)value : Short.reverseBytes((short)value));
/*     */   }
/*     */   
/*     */   protected void _setMedium(int index, int value)
/*     */   {
/* 232 */     long addr = addr(index);
/* 233 */     PlatformDependent.putByte(addr, (byte)(value >>> 16));
/* 234 */     PlatformDependent.putByte(addr + 1L, (byte)(value >>> 8));
/* 235 */     PlatformDependent.putByte(addr + 2L, (byte)value);
/*     */   }
/*     */   
/*     */   protected void _setInt(int index, int value)
/*     */   {
/* 240 */     PlatformDependent.putInt(addr(index), NATIVE_ORDER ? value : Integer.reverseBytes(value));
/*     */   }
/*     */   
/*     */   protected void _setLong(int index, long value)
/*     */   {
/* 245 */     PlatformDependent.putLong(addr(index), NATIVE_ORDER ? value : Long.reverseBytes(value));
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/* 250 */     checkIndex(index, length);
/* 251 */     if (src == null) {
/* 252 */       throw new NullPointerException("src");
/*     */     }
/* 254 */     if ((srcIndex < 0) || (srcIndex > src.capacity() - length)) {
/* 255 */       throw new IndexOutOfBoundsException("srcIndex: " + srcIndex);
/*     */     }
/*     */     
/* 258 */     if (length != 0) {
/* 259 */       if (src.hasMemoryAddress()) {
/* 260 */         PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, addr(index), length);
/* 261 */       } else if (src.hasArray()) {
/* 262 */         PlatformDependent.copyMemory(src.array(), src.arrayOffset() + srcIndex, addr(index), length);
/*     */       } else {
/* 264 */         src.getBytes(srcIndex, this, index, length);
/*     */       }
/*     */     }
/* 267 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 272 */     checkIndex(index, length);
/* 273 */     if (length != 0) {
/* 274 */       PlatformDependent.copyMemory(src, srcIndex, addr(index), length);
/*     */     }
/* 276 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 281 */     checkIndex(index, src.remaining());
/* 282 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 283 */     if (src == tmpBuf) {
/* 284 */       src = src.duplicate();
/*     */     }
/*     */     
/* 287 */     index = idx(index);
/* 288 */     tmpBuf.clear().position(index).limit(index + src.remaining());
/* 289 */     tmpBuf.put(src);
/* 290 */     return this;
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException
/*     */   {
/* 295 */     checkIndex(index, length);
/* 296 */     byte[] tmp = new byte[length];
/* 297 */     int readBytes = in.read(tmp);
/* 298 */     if (readBytes > 0) {
/* 299 */       PlatformDependent.copyMemory(tmp, 0, addr(index), readBytes);
/*     */     }
/* 301 */     return readBytes;
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException
/*     */   {
/* 306 */     checkIndex(index, length);
/* 307 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 308 */     index = idx(index);
/* 309 */     tmpBuf.clear().position(index).limit(index + length);
/*     */     try {
/* 311 */       return in.read(tmpBuf);
/*     */     } catch (ClosedChannelException ignored) {}
/* 313 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 319 */     checkIndex(index, length);
/* 320 */     ByteBuf copy = alloc().directBuffer(length, maxCapacity());
/* 321 */     if (length != 0) {
/* 322 */       if (copy.hasMemoryAddress()) {
/* 323 */         PlatformDependent.copyMemory(addr(index), copy.memoryAddress(), length);
/* 324 */         copy.setIndex(0, length);
/*     */       } else {
/* 326 */         copy.writeBytes(this, index, length);
/*     */       }
/*     */     }
/* 329 */     return copy;
/*     */   }
/*     */   
/*     */   public int nioBufferCount()
/*     */   {
/* 334 */     return 1;
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 339 */     return new ByteBuffer[] { nioBuffer(index, length) };
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length)
/*     */   {
/* 344 */     checkIndex(index, length);
/* 345 */     index = idx(index);
/* 346 */     return ((ByteBuffer)((ByteBuffer)this.memory).duplicate().position(index).limit(index + length)).slice();
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 351 */     checkIndex(index, length);
/* 352 */     index = idx(index);
/* 353 */     return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */   }
/*     */   
/*     */   public boolean hasArray()
/*     */   {
/* 358 */     return false;
/*     */   }
/*     */   
/*     */   public byte[] array()
/*     */   {
/* 363 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */   
/*     */   public int arrayOffset()
/*     */   {
/* 368 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */   
/*     */   public boolean hasMemoryAddress()
/*     */   {
/* 373 */     return true;
/*     */   }
/*     */   
/*     */   public long memoryAddress()
/*     */   {
/* 378 */     ensureAccessible();
/* 379 */     return this.memoryAddress;
/*     */   }
/*     */   
/*     */   private long addr(int index) {
/* 383 */     return this.memoryAddress + index;
/*     */   }
/*     */   
/*     */   protected SwappedByteBuf newSwappedByteBuf()
/*     */   {
/* 388 */     return new UnsafeDirectSwappedByteBuf(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\PooledUnsafeDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */