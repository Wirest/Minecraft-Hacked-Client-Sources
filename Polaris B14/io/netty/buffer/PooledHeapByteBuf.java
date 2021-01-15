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
/*     */ final class PooledHeapByteBuf
/*     */   extends PooledByteBuf<byte[]>
/*     */ {
/*  30 */   private static final Recycler<PooledHeapByteBuf> RECYCLER = new Recycler()
/*     */   {
/*     */     protected PooledHeapByteBuf newObject(Recycler.Handle<PooledHeapByteBuf> handle) {
/*  33 */       return new PooledHeapByteBuf(handle, 0, null);
/*     */     }
/*     */   };
/*     */   
/*     */   static PooledHeapByteBuf newInstance(int maxCapacity) {
/*  38 */     PooledHeapByteBuf buf = (PooledHeapByteBuf)RECYCLER.get();
/*  39 */     buf.setRefCnt(1);
/*  40 */     buf.maxCapacity(maxCapacity);
/*  41 */     return buf;
/*     */   }
/*     */   
/*     */   private PooledHeapByteBuf(Recycler.Handle<PooledHeapByteBuf> recyclerHandle, int maxCapacity) {
/*  45 */     super(recyclerHandle, maxCapacity);
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/*  50 */     return false;
/*     */   }
/*     */   
/*     */   protected byte _getByte(int index)
/*     */   {
/*  55 */     return ((byte[])this.memory)[idx(index)];
/*     */   }
/*     */   
/*     */   protected short _getShort(int index)
/*     */   {
/*  60 */     index = idx(index);
/*  61 */     return (short)(((byte[])this.memory)[index] << 8 | ((byte[])this.memory)[(index + 1)] & 0xFF);
/*     */   }
/*     */   
/*     */   protected int _getUnsignedMedium(int index)
/*     */   {
/*  66 */     index = idx(index);
/*  67 */     return (((byte[])this.memory)[index] & 0xFF) << 16 | (((byte[])this.memory)[(index + 1)] & 0xFF) << 8 | ((byte[])this.memory)[(index + 2)] & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int _getInt(int index)
/*     */   {
/*  74 */     index = idx(index);
/*  75 */     return (((byte[])this.memory)[index] & 0xFF) << 24 | (((byte[])this.memory)[(index + 1)] & 0xFF) << 16 | (((byte[])this.memory)[(index + 2)] & 0xFF) << 8 | ((byte[])this.memory)[(index + 3)] & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected long _getLong(int index)
/*     */   {
/*  83 */     index = idx(index);
/*  84 */     return (((byte[])this.memory)[index] & 0xFF) << 56 | (((byte[])this.memory)[(index + 1)] & 0xFF) << 48 | (((byte[])this.memory)[(index + 2)] & 0xFF) << 40 | (((byte[])this.memory)[(index + 3)] & 0xFF) << 32 | (((byte[])this.memory)[(index + 4)] & 0xFF) << 24 | (((byte[])this.memory)[(index + 5)] & 0xFF) << 16 | (((byte[])this.memory)[(index + 6)] & 0xFF) << 8 | ((byte[])this.memory)[(index + 7)] & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/*  96 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/*  97 */     if (dst.hasMemoryAddress()) {
/*  98 */       PlatformDependent.copyMemory((byte[])this.memory, idx(index), dst.memoryAddress() + dstIndex, length);
/*  99 */     } else if (dst.hasArray()) {
/* 100 */       getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
/*     */     } else {
/* 102 */       dst.setBytes(dstIndex, (byte[])this.memory, idx(index), length);
/*     */     }
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 109 */     checkDstIndex(index, length, dstIndex, dst.length);
/* 110 */     System.arraycopy(this.memory, idx(index), dst, dstIndex, length);
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 116 */     checkIndex(index);
/* 117 */     dst.put((byte[])this.memory, idx(index), Math.min(capacity() - index, dst.remaining()));
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException
/*     */   {
/* 123 */     checkIndex(index, length);
/* 124 */     out.write((byte[])this.memory, idx(index), length);
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 130 */     return getBytes(index, out, length, false);
/*     */   }
/*     */   
/*     */   private int getBytes(int index, GatheringByteChannel out, int length, boolean internal) throws IOException {
/* 134 */     checkIndex(index, length);
/* 135 */     index = idx(index);
/*     */     ByteBuffer tmpBuf;
/* 137 */     ByteBuffer tmpBuf; if (internal) {
/* 138 */       tmpBuf = internalNioBuffer();
/*     */     } else {
/* 140 */       tmpBuf = ByteBuffer.wrap((byte[])this.memory);
/*     */     }
/* 142 */     return out.write((ByteBuffer)tmpBuf.clear().position(index).limit(index + length));
/*     */   }
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 147 */     checkReadableBytes(length);
/* 148 */     int readBytes = getBytes(this.readerIndex, out, length, true);
/* 149 */     this.readerIndex += readBytes;
/* 150 */     return readBytes;
/*     */   }
/*     */   
/*     */   protected void _setByte(int index, int value)
/*     */   {
/* 155 */     ((byte[])this.memory)[idx(index)] = ((byte)value);
/*     */   }
/*     */   
/*     */   protected void _setShort(int index, int value)
/*     */   {
/* 160 */     index = idx(index);
/* 161 */     ((byte[])this.memory)[index] = ((byte)(value >>> 8));
/* 162 */     ((byte[])this.memory)[(index + 1)] = ((byte)value);
/*     */   }
/*     */   
/*     */   protected void _setMedium(int index, int value)
/*     */   {
/* 167 */     index = idx(index);
/* 168 */     ((byte[])this.memory)[index] = ((byte)(value >>> 16));
/* 169 */     ((byte[])this.memory)[(index + 1)] = ((byte)(value >>> 8));
/* 170 */     ((byte[])this.memory)[(index + 2)] = ((byte)value);
/*     */   }
/*     */   
/*     */   protected void _setInt(int index, int value)
/*     */   {
/* 175 */     index = idx(index);
/* 176 */     ((byte[])this.memory)[index] = ((byte)(value >>> 24));
/* 177 */     ((byte[])this.memory)[(index + 1)] = ((byte)(value >>> 16));
/* 178 */     ((byte[])this.memory)[(index + 2)] = ((byte)(value >>> 8));
/* 179 */     ((byte[])this.memory)[(index + 3)] = ((byte)value);
/*     */   }
/*     */   
/*     */   protected void _setLong(int index, long value)
/*     */   {
/* 184 */     index = idx(index);
/* 185 */     ((byte[])this.memory)[index] = ((byte)(int)(value >>> 56));
/* 186 */     ((byte[])this.memory)[(index + 1)] = ((byte)(int)(value >>> 48));
/* 187 */     ((byte[])this.memory)[(index + 2)] = ((byte)(int)(value >>> 40));
/* 188 */     ((byte[])this.memory)[(index + 3)] = ((byte)(int)(value >>> 32));
/* 189 */     ((byte[])this.memory)[(index + 4)] = ((byte)(int)(value >>> 24));
/* 190 */     ((byte[])this.memory)[(index + 5)] = ((byte)(int)(value >>> 16));
/* 191 */     ((byte[])this.memory)[(index + 6)] = ((byte)(int)(value >>> 8));
/* 192 */     ((byte[])this.memory)[(index + 7)] = ((byte)(int)value);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/* 197 */     checkSrcIndex(index, length, srcIndex, src.capacity());
/* 198 */     if (src.hasMemoryAddress()) {
/* 199 */       PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, (byte[])this.memory, idx(index), length);
/* 200 */     } else if (src.hasArray()) {
/* 201 */       setBytes(index, src.array(), src.arrayOffset() + srcIndex, length);
/*     */     } else {
/* 203 */       src.getBytes(srcIndex, (byte[])this.memory, idx(index), length);
/*     */     }
/* 205 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 210 */     checkSrcIndex(index, length, srcIndex, src.length);
/* 211 */     System.arraycopy(src, srcIndex, this.memory, idx(index), length);
/* 212 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 217 */     int length = src.remaining();
/* 218 */     checkIndex(index, length);
/* 219 */     src.get((byte[])this.memory, idx(index), length);
/* 220 */     return this;
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException
/*     */   {
/* 225 */     checkIndex(index, length);
/* 226 */     return in.read((byte[])this.memory, idx(index), length);
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException
/*     */   {
/* 231 */     checkIndex(index, length);
/* 232 */     index = idx(index);
/*     */     try {
/* 234 */       return in.read((ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length));
/*     */     } catch (ClosedChannelException ignored) {}
/* 236 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 242 */     checkIndex(index, length);
/* 243 */     ByteBuf copy = alloc().heapBuffer(length, maxCapacity());
/* 244 */     copy.writeBytes((byte[])this.memory, idx(index), length);
/* 245 */     return copy;
/*     */   }
/*     */   
/*     */   public int nioBufferCount()
/*     */   {
/* 250 */     return 1;
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 255 */     return new ByteBuffer[] { nioBuffer(index, length) };
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length)
/*     */   {
/* 260 */     checkIndex(index, length);
/* 261 */     index = idx(index);
/* 262 */     ByteBuffer buf = ByteBuffer.wrap((byte[])this.memory, index, length);
/* 263 */     return buf.slice();
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 268 */     checkIndex(index, length);
/* 269 */     index = idx(index);
/* 270 */     return (ByteBuffer)internalNioBuffer().clear().position(index).limit(index + length);
/*     */   }
/*     */   
/*     */   public boolean hasArray()
/*     */   {
/* 275 */     return true;
/*     */   }
/*     */   
/*     */   public byte[] array()
/*     */   {
/* 280 */     ensureAccessible();
/* 281 */     return (byte[])this.memory;
/*     */   }
/*     */   
/*     */   public int arrayOffset()
/*     */   {
/* 286 */     return this.offset;
/*     */   }
/*     */   
/*     */   public boolean hasMemoryAddress()
/*     */   {
/* 291 */     return false;
/*     */   }
/*     */   
/*     */   public long memoryAddress()
/*     */   {
/* 296 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected ByteBuffer newInternalNioBuffer(byte[] memory)
/*     */   {
/* 301 */     return ByteBuffer.wrap(memory);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\PooledHeapByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */