/*     */ package io.netty.buffer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ 
/*     */ public class ReadOnlyByteBuf
/*     */   extends AbstractDerivedByteBuf
/*     */ {
/*     */   private final ByteBuf buffer;
/*     */   
/*     */   public ReadOnlyByteBuf(ByteBuf buffer)
/*     */   {
/*  37 */     super(buffer.maxCapacity());
/*     */     
/*  39 */     if (((buffer instanceof ReadOnlyByteBuf)) || ((buffer instanceof DuplicatedByteBuf))) {
/*  40 */       this.buffer = buffer.unwrap();
/*     */     } else {
/*  42 */       this.buffer = buffer;
/*     */     }
/*  44 */     setIndex(buffer.readerIndex(), buffer.writerIndex());
/*     */   }
/*     */   
/*     */   public boolean isWritable()
/*     */   {
/*  49 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isWritable(int numBytes)
/*     */   {
/*  54 */     return false;
/*     */   }
/*     */   
/*     */   public ByteBuf unwrap()
/*     */   {
/*  59 */     return this.buffer;
/*     */   }
/*     */   
/*     */   public ByteBufAllocator alloc()
/*     */   {
/*  64 */     return this.buffer.alloc();
/*     */   }
/*     */   
/*     */   public ByteOrder order()
/*     */   {
/*  69 */     return this.buffer.order();
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/*  74 */     return this.buffer.isDirect();
/*     */   }
/*     */   
/*     */   public boolean hasArray()
/*     */   {
/*  79 */     return false;
/*     */   }
/*     */   
/*     */   public byte[] array()
/*     */   {
/*  84 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public int arrayOffset()
/*     */   {
/*  89 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public boolean hasMemoryAddress()
/*     */   {
/*  94 */     return false;
/*     */   }
/*     */   
/*     */   public long memoryAddress()
/*     */   {
/*  99 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf discardReadBytes()
/*     */   {
/* 104 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/* 109 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 114 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 119 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setByte(int index, int value)
/*     */   {
/* 124 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected void _setByte(int index, int value)
/*     */   {
/* 129 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setShort(int index, int value)
/*     */   {
/* 134 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected void _setShort(int index, int value)
/*     */   {
/* 139 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setMedium(int index, int value)
/*     */   {
/* 144 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected void _setMedium(int index, int value)
/*     */   {
/* 149 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setInt(int index, int value)
/*     */   {
/* 154 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected void _setInt(int index, int value)
/*     */   {
/* 159 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setLong(int index, long value)
/*     */   {
/* 164 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected void _setLong(int index, long value)
/*     */   {
/* 169 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length)
/*     */   {
/* 174 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length)
/*     */   {
/* 179 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length)
/*     */     throws IOException
/*     */   {
/* 185 */     return this.buffer.getBytes(index, out, length);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length)
/*     */     throws IOException
/*     */   {
/* 191 */     this.buffer.getBytes(index, out, length);
/* 192 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 197 */     this.buffer.getBytes(index, dst, dstIndex, length);
/* 198 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 203 */     this.buffer.getBytes(index, dst, dstIndex, length);
/* 204 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 209 */     this.buffer.getBytes(index, dst);
/* 210 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf duplicate()
/*     */   {
/* 215 */     return new ReadOnlyByteBuf(this);
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 220 */     return this.buffer.copy(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuf slice(int index, int length)
/*     */   {
/* 225 */     return Unpooled.unmodifiableBuffer(this.buffer.slice(index, length));
/*     */   }
/*     */   
/*     */   public byte getByte(int index)
/*     */   {
/* 230 */     return _getByte(index);
/*     */   }
/*     */   
/*     */   protected byte _getByte(int index)
/*     */   {
/* 235 */     return this.buffer.getByte(index);
/*     */   }
/*     */   
/*     */   public short getShort(int index)
/*     */   {
/* 240 */     return _getShort(index);
/*     */   }
/*     */   
/*     */   protected short _getShort(int index)
/*     */   {
/* 245 */     return this.buffer.getShort(index);
/*     */   }
/*     */   
/*     */   public int getUnsignedMedium(int index)
/*     */   {
/* 250 */     return _getUnsignedMedium(index);
/*     */   }
/*     */   
/*     */   protected int _getUnsignedMedium(int index)
/*     */   {
/* 255 */     return this.buffer.getUnsignedMedium(index);
/*     */   }
/*     */   
/*     */   public int getInt(int index)
/*     */   {
/* 260 */     return _getInt(index);
/*     */   }
/*     */   
/*     */   protected int _getInt(int index)
/*     */   {
/* 265 */     return this.buffer.getInt(index);
/*     */   }
/*     */   
/*     */   public long getLong(int index)
/*     */   {
/* 270 */     return _getLong(index);
/*     */   }
/*     */   
/*     */   protected long _getLong(int index)
/*     */   {
/* 275 */     return this.buffer.getLong(index);
/*     */   }
/*     */   
/*     */   public int nioBufferCount()
/*     */   {
/* 280 */     return this.buffer.nioBufferCount();
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length)
/*     */   {
/* 285 */     return this.buffer.nioBuffer(index, length).asReadOnlyBuffer();
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 290 */     return this.buffer.nioBuffers(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 295 */     return nioBuffer(index, length);
/*     */   }
/*     */   
/*     */   public int forEachByte(int index, int length, ByteBufProcessor processor)
/*     */   {
/* 300 */     return this.buffer.forEachByte(index, length, processor);
/*     */   }
/*     */   
/*     */   public int forEachByteDesc(int index, int length, ByteBufProcessor processor)
/*     */   {
/* 305 */     return this.buffer.forEachByteDesc(index, length, processor);
/*     */   }
/*     */   
/*     */   public int capacity()
/*     */   {
/* 310 */     return this.buffer.capacity();
/*     */   }
/*     */   
/*     */   public ByteBuf capacity(int newCapacity)
/*     */   {
/* 315 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\ReadOnlyByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */