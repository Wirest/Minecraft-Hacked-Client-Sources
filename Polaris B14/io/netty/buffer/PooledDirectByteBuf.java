/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.Recycler.Handle;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
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
/*     */ final class PooledDirectByteBuf
/*     */   extends PooledByteBuf<ByteBuffer>
/*     */ {
/*  31 */   private static final Recycler<PooledDirectByteBuf> RECYCLER = new Recycler()
/*     */   {
/*     */     protected PooledDirectByteBuf newObject(Recycler.Handle<PooledDirectByteBuf> handle) {
/*  34 */       return new PooledDirectByteBuf(handle, 0, null);
/*     */     }
/*     */   };
/*     */   
/*     */   static PooledDirectByteBuf newInstance(int maxCapacity) {
/*  39 */     PooledDirectByteBuf buf = (PooledDirectByteBuf)RECYCLER.get();
/*  40 */     buf.setRefCnt(1);
/*  41 */     buf.maxCapacity(maxCapacity);
/*  42 */     return buf;
/*     */   }
/*     */   
/*     */   private PooledDirectByteBuf(Recycler.Handle<PooledDirectByteBuf> recyclerHandle, int maxCapacity) {
/*  46 */     super(recyclerHandle, maxCapacity);
/*     */   }
/*     */   
/*     */   protected ByteBuffer newInternalNioBuffer(ByteBuffer memory)
/*     */   {
/*  51 */     return memory.duplicate();
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/*  56 */     return true;
/*     */   }
/*     */   
/*     */   protected byte _getByte(int index)
/*     */   {
/*  61 */     return ((ByteBuffer)this.memory).get(idx(index));
/*     */   }
/*     */   
/*     */   protected short _getShort(int index)
/*     */   {
/*  66 */     return ((ByteBuffer)this.memory).getShort(idx(index));
/*     */   }
/*     */   
/*     */   protected int _getUnsignedMedium(int index)
/*     */   {
/*  71 */     index = idx(index);
/*  72 */     return (((ByteBuffer)this.memory).get(index) & 0xFF) << 16 | (((ByteBuffer)this.memory).get(index + 1) & 0xFF) << 8 | ((ByteBuffer)this.memory).get(index + 2) & 0xFF;
/*     */   }
/*     */   
/*     */   protected int _getInt(int index)
/*     */   {
/*  77 */     return ((ByteBuffer)this.memory).getInt(idx(index));
/*     */   }
/*     */   
/*     */   protected long _getLong(int index)
/*     */   {
/*  82 */     return ((ByteBuffer)this.memory).getLong(idx(index));
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/*  87 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/*  88 */     if (dst.hasArray()) {
/*  89 */       getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
/*  90 */     } else if (dst.nioBufferCount() > 0) {
/*  91 */       for (ByteBuffer bb : dst.nioBuffers(dstIndex, length)) {
/*  92 */         int bbLen = bb.remaining();
/*  93 */         getBytes(index, bb);
/*  94 */         index += bbLen;
/*     */       }
/*     */     } else {
/*  97 */       dst.setBytes(dstIndex, this, index, length);
/*     */     }
/*  99 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 104 */     getBytes(index, dst, dstIndex, length, false);
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   private void getBytes(int index, byte[] dst, int dstIndex, int length, boolean internal) {
/* 109 */     checkDstIndex(index, length, dstIndex, dst.length);
/*     */     ByteBuffer tmpBuf;
/* 111 */     ByteBuffer tmpBuf; if (internal) {
/* 112 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 114 */       tmpBuf = ((ByteBuffer)this.memory).duplicate();
/*     */     }
/* 116 */     index = idx(index);
/* 117 */     tmpBuf.clear().position(index).limit(index + length);
/* 118 */     tmpBuf.get(dst, dstIndex, length);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length)
/*     */   {
/* 123 */     checkReadableBytes(length);
/* 124 */     getBytes(this.readerIndex, dst, dstIndex, length, true);
/* 125 */     this.readerIndex += length;
/* 126 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 131 */     getBytes(index, dst, false);
/* 132 */     return this;
/*     */   }
/*     */   
/*     */   private void getBytes(int index, ByteBuffer dst, boolean internal) {
/* 136 */     checkIndex(index);
/* 137 */     int bytesToCopy = Math.min(capacity() - index, dst.remaining());
/*     */     ByteBuffer tmpBuf;
/* 139 */     ByteBuffer tmpBuf; if (internal) {
/* 140 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 142 */       tmpBuf = ((ByteBuffer)this.memory).duplicate();
/*     */     }
/* 144 */     index = idx(index);
/* 145 */     tmpBuf.clear().position(index).limit(index + bytesToCopy);
/* 146 */     dst.put(tmpBuf);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuffer dst)
/*     */   {
/* 151 */     int length = dst.remaining();
/* 152 */     checkReadableBytes(length);
/* 153 */     getBytes(this.readerIndex, dst, true);
/* 154 */     this.readerIndex += length;
/* 155 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException
/*     */   {
/* 160 */     getBytes(index, out, length, false);
/* 161 */     return this;
/*     */   }
/*     */   
/*     */   private void getBytes(int index, OutputStream out, int length, boolean internal) throws IOException {
/* 165 */     checkIndex(index, length);
/* 166 */     if (length == 0) {
/* 167 */       return;
/*     */     }
/*     */     
/* 170 */     byte[] tmp = new byte[length];
/*     */     ByteBuffer tmpBuf;
/* 172 */     ByteBuffer tmpBuf; if (internal) {
/* 173 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 175 */       tmpBuf = ((ByteBuffer)this.memory).duplicate();
/*     */     }
/* 177 */     tmpBuf.clear().position(idx(index));
/* 178 */     tmpBuf.get(tmp);
/* 179 */     out.write(tmp);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(OutputStream out, int length) throws IOException
/*     */   {
/* 184 */     checkReadableBytes(length);
/* 185 */     getBytes(this.readerIndex, out, length, true);
/* 186 */     this.readerIndex += length;
/* 187 */     return this;
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 192 */     return getBytes(index, out, length, false);
/*     */   }
/*     */   
/*     */   private int getBytes(int index, GatheringByteChannel out, int length, boolean internal) throws IOException {
/* 196 */     checkIndex(index, length);
/* 197 */     if (length == 0) {
/* 198 */       return 0;
/*     */     }
/*     */     ByteBuffer tmpBuf;
/*     */     ByteBuffer tmpBuf;
/* 202 */     if (internal) {
/* 203 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 205 */       tmpBuf = ((ByteBuffer)this.memory).duplicate();
/*     */     }
/* 207 */     index = idx(index);
/* 208 */     tmpBuf.clear().position(index).limit(index + length);
/* 209 */     return out.write(tmpBuf);
/*     */   }
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 214 */     checkReadableBytes(length);
/* 215 */     int readBytes = getBytes(this.readerIndex, out, length, true);
/* 216 */     this.readerIndex += readBytes;
/* 217 */     return readBytes;
/*     */   }
/*     */   
/*     */   protected void _setByte(int index, int value)
/*     */   {
/* 222 */     ((ByteBuffer)this.memory).put(idx(index), (byte)value);
/*     */   }
/*     */   
/*     */   protected void _setShort(int index, int value)
/*     */   {
/* 227 */     ((ByteBuffer)this.memory).putShort(idx(index), (short)value);
/*     */   }
/*     */   
/*     */   protected void _setMedium(int index, int value)
/*     */   {
/* 232 */     index = idx(index);
/* 233 */     ((ByteBuffer)this.memory).put(index, (byte)(value >>> 16));
/* 234 */     ((ByteBuffer)this.memory).put(index + 1, (byte)(value >>> 8));
/* 235 */     ((ByteBuffer)this.memory).put(index + 2, (byte)value);
/*     */   }
/*     */   
/*     */   protected void _setInt(int index, int value)
/*     */   {
/* 240 */     ((ByteBuffer)this.memory).putInt(idx(index), value);
/*     */   }
/*     */   
/*     */   protected void _setLong(int index, long value)
/*     */   {
/* 245 */     ((ByteBuffer)this.memory).putLong(idx(index), value);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/* 250 */     checkSrcIndex(index, length, srcIndex, src.capacity());
/* 251 */     if (src.hasArray()) {
/* 252 */       setBytes(index, src.array(), src.arrayOffset() + srcIndex, length);
/* 253 */     } else if (src.nioBufferCount() > 0) {
/* 254 */       for (ByteBuffer bb : src.nioBuffers(srcIndex, length)) {
/* 255 */         int bbLen = bb.remaining();
/* 256 */         setBytes(index, bb);
/* 257 */         index += bbLen;
/*     */       }
/*     */     } else {
/* 260 */       src.getBytes(srcIndex, this, index, length);
/*     */     }
/* 262 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 267 */     checkSrcIndex(index, length, srcIndex, src.length);
/* 268 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 269 */     index = idx(index);
/* 270 */     tmpBuf.clear().position(index).limit(index + length);
/* 271 */     tmpBuf.put(src, srcIndex, length);
/* 272 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 277 */     checkIndex(index, src.remaining());
/* 278 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 279 */     if (src == tmpBuf) {
/* 280 */       src = src.duplicate();
/*     */     }
/*     */     
/* 283 */     index = idx(index);
/* 284 */     tmpBuf.clear().position(index).limit(index + src.remaining());
/* 285 */     tmpBuf.put(src);
/* 286 */     return this;
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException
/*     */   {
/* 291 */     checkIndex(index, length);
/* 292 */     byte[] tmp = new byte[length];
/* 293 */     int readBytes = in.read(tmp);
/* 294 */     if (readBytes <= 0) {
/* 295 */       return readBytes;
/*     */     }
/* 297 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 298 */     tmpBuf.clear().position(idx(index));
/* 299 */     tmpBuf.put(tmp, 0, readBytes);
/* 300 */     return readBytes;
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException
/*     */   {
/* 305 */     checkIndex(index, length);
/* 306 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 307 */     index = idx(index);
/* 308 */     tmpBuf.clear().position(index).limit(index + length);
/*     */     try {
/* 310 */       return in.read(tmpBuf);
/*     */     } catch (ClosedChannelException ignored) {}
/* 312 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 318 */     checkIndex(index, length);
/* 319 */     ByteBuf copy = alloc().directBuffer(length, maxCapacity());
/* 320 */     copy.writeBytes(this, index, length);
/* 321 */     return copy;
/*     */   }
/*     */   
/*     */   public int nioBufferCount()
/*     */   {
/* 326 */     return 1;
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length)
/*     */   {
/* 331 */     checkIndex(index, length);
/* 332 */     index = idx(index);
/* 333 */     return ((ByteBuffer)((ByteBuffer)this.memory).duplicate().position(index).limit(index + length)).slice();
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 338 */     return new ByteBuffer[] { nioBuffer(index, length) };
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 343 */     checkIndex(index, length);
/* 344 */     index = idx(index);
/* 345 */     return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */   }
/*     */   
/*     */   public boolean hasArray()
/*     */   {
/* 350 */     return false;
/*     */   }
/*     */   
/*     */   public byte[] array()
/*     */   {
/* 355 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */   
/*     */   public int arrayOffset()
/*     */   {
/* 360 */     throw new UnsupportedOperationException("direct buffer");
/*     */   }
/*     */   
/*     */   public boolean hasMemoryAddress()
/*     */   {
/* 365 */     return false;
/*     */   }
/*     */   
/*     */   public long memoryAddress()
/*     */   {
/* 370 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\PooledDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */