/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.ReadOnlyBufferException;
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
/*     */ class ReadOnlyByteBufferBuf
/*     */   extends AbstractReferenceCountedByteBuf
/*     */ {
/*     */   protected final ByteBuffer buffer;
/*     */   private final ByteBufAllocator allocator;
/*     */   private ByteBuffer tmpNioBuf;
/*     */   
/*     */   ReadOnlyByteBufferBuf(ByteBufAllocator allocator, ByteBuffer buffer)
/*     */   {
/*  40 */     super(buffer.remaining());
/*  41 */     if (!buffer.isReadOnly()) {
/*  42 */       throw new IllegalArgumentException("must be a readonly buffer: " + StringUtil.simpleClassName(buffer));
/*     */     }
/*     */     
/*  45 */     this.allocator = allocator;
/*  46 */     this.buffer = buffer.slice().order(ByteOrder.BIG_ENDIAN);
/*  47 */     writerIndex(this.buffer.limit());
/*     */   }
/*     */   
/*     */ 
/*     */   protected void deallocate() {}
/*     */   
/*     */   public byte getByte(int index)
/*     */   {
/*  55 */     ensureAccessible();
/*  56 */     return _getByte(index);
/*     */   }
/*     */   
/*     */   protected byte _getByte(int index)
/*     */   {
/*  61 */     return this.buffer.get(index);
/*     */   }
/*     */   
/*     */   public short getShort(int index)
/*     */   {
/*  66 */     ensureAccessible();
/*  67 */     return _getShort(index);
/*     */   }
/*     */   
/*     */   protected short _getShort(int index)
/*     */   {
/*  72 */     return this.buffer.getShort(index);
/*     */   }
/*     */   
/*     */   public int getUnsignedMedium(int index)
/*     */   {
/*  77 */     ensureAccessible();
/*  78 */     return _getUnsignedMedium(index);
/*     */   }
/*     */   
/*     */   protected int _getUnsignedMedium(int index)
/*     */   {
/*  83 */     return (getByte(index) & 0xFF) << 16 | (getByte(index + 1) & 0xFF) << 8 | getByte(index + 2) & 0xFF;
/*     */   }
/*     */   
/*     */   public int getInt(int index)
/*     */   {
/*  88 */     ensureAccessible();
/*  89 */     return _getInt(index);
/*     */   }
/*     */   
/*     */   protected int _getInt(int index)
/*     */   {
/*  94 */     return this.buffer.getInt(index);
/*     */   }
/*     */   
/*     */   public long getLong(int index)
/*     */   {
/*  99 */     ensureAccessible();
/* 100 */     return _getLong(index);
/*     */   }
/*     */   
/*     */   protected long _getLong(int index)
/*     */   {
/* 105 */     return this.buffer.getLong(index);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 110 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/* 111 */     if (dst.hasArray()) {
/* 112 */       getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
/* 113 */     } else if (dst.nioBufferCount() > 0) {
/* 114 */       for (ByteBuffer bb : dst.nioBuffers(dstIndex, length)) {
/* 115 */         int bbLen = bb.remaining();
/* 116 */         getBytes(index, bb);
/* 117 */         index += bbLen;
/*     */       }
/*     */     } else {
/* 120 */       dst.setBytes(dstIndex, this, index, length);
/*     */     }
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 127 */     checkDstIndex(index, length, dstIndex, dst.length);
/*     */     
/* 129 */     if ((dstIndex < 0) || (dstIndex > dst.length - length)) {
/* 130 */       throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", new Object[] { Integer.valueOf(dstIndex), Integer.valueOf(length), Integer.valueOf(dst.length) }));
/*     */     }
/*     */     
/*     */ 
/* 134 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 135 */     tmpBuf.clear().position(index).limit(index + length);
/* 136 */     tmpBuf.get(dst, dstIndex, length);
/* 137 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 142 */     checkIndex(index);
/* 143 */     if (dst == null) {
/* 144 */       throw new NullPointerException("dst");
/*     */     }
/*     */     
/* 147 */     int bytesToCopy = Math.min(capacity() - index, dst.remaining());
/* 148 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 149 */     tmpBuf.clear().position(index).limit(index + bytesToCopy);
/* 150 */     dst.put(tmpBuf);
/* 151 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setByte(int index, int value)
/*     */   {
/* 156 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected void _setShort(int index, int value)
/*     */   {
/* 161 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected void _setMedium(int index, int value)
/*     */   {
/* 166 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected void _setInt(int index, int value)
/*     */   {
/* 171 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected void _setLong(int index, long value)
/*     */   {
/* 176 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public int capacity()
/*     */   {
/* 181 */     return maxCapacity();
/*     */   }
/*     */   
/*     */   public ByteBuf capacity(int newCapacity)
/*     */   {
/* 186 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBufAllocator alloc()
/*     */   {
/* 191 */     return this.allocator;
/*     */   }
/*     */   
/*     */   public ByteOrder order()
/*     */   {
/* 196 */     return ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */   
/*     */   public ByteBuf unwrap()
/*     */   {
/* 201 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/* 206 */     return this.buffer.isDirect();
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException
/*     */   {
/* 211 */     ensureAccessible();
/* 212 */     if (length == 0) {
/* 213 */       return this;
/*     */     }
/*     */     
/* 216 */     if (this.buffer.hasArray()) {
/* 217 */       out.write(this.buffer.array(), index + this.buffer.arrayOffset(), length);
/*     */     } else {
/* 219 */       byte[] tmp = new byte[length];
/* 220 */       ByteBuffer tmpBuf = internalNioBuffer();
/* 221 */       tmpBuf.clear().position(index);
/* 222 */       tmpBuf.get(tmp);
/* 223 */       out.write(tmp);
/*     */     }
/* 225 */     return this;
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 230 */     ensureAccessible();
/* 231 */     if (length == 0) {
/* 232 */       return 0;
/*     */     }
/*     */     
/* 235 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 236 */     tmpBuf.clear().position(index).limit(index + length);
/* 237 */     return out.write(tmpBuf);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/* 242 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 247 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 252 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException
/*     */   {
/* 257 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException
/*     */   {
/* 262 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected final ByteBuffer internalNioBuffer() {
/* 266 */     ByteBuffer tmpNioBuf = this.tmpNioBuf;
/* 267 */     if (tmpNioBuf == null) {
/* 268 */       this.tmpNioBuf = (tmpNioBuf = this.buffer.duplicate());
/*     */     }
/* 270 */     return tmpNioBuf;
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 275 */     ensureAccessible();
/*     */     ByteBuffer src;
/*     */     try {
/* 278 */       src = (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */     } catch (IllegalArgumentException ignored) {
/* 280 */       throw new IndexOutOfBoundsException("Too many bytes to read - Need " + (index + length));
/*     */     }
/*     */     
/* 283 */     ByteBuffer dst = ByteBuffer.allocateDirect(length);
/* 284 */     dst.put(src);
/* 285 */     dst.order(order());
/* 286 */     dst.clear();
/* 287 */     return new UnpooledDirectByteBuf(alloc(), dst, maxCapacity());
/*     */   }
/*     */   
/*     */   public int nioBufferCount()
/*     */   {
/* 292 */     return 1;
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 297 */     return new ByteBuffer[] { nioBuffer(index, length) };
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length)
/*     */   {
/* 302 */     return (ByteBuffer)this.buffer.duplicate().position(index).limit(index + length);
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 307 */     ensureAccessible();
/* 308 */     return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */   }
/*     */   
/*     */   public boolean hasArray()
/*     */   {
/* 313 */     return this.buffer.hasArray();
/*     */   }
/*     */   
/*     */   public byte[] array()
/*     */   {
/* 318 */     return this.buffer.array();
/*     */   }
/*     */   
/*     */   public int arrayOffset()
/*     */   {
/* 323 */     return this.buffer.arrayOffset();
/*     */   }
/*     */   
/*     */   public boolean hasMemoryAddress()
/*     */   {
/* 328 */     return false;
/*     */   }
/*     */   
/*     */   public long memoryAddress()
/*     */   {
/* 333 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\ReadOnlyByteBufferBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */