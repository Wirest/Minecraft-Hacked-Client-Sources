/*     */ package io.netty.buffer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ public class DuplicatedByteBuf
/*     */   extends AbstractDerivedByteBuf
/*     */ {
/*     */   private final ByteBuf buffer;
/*     */   
/*     */   public DuplicatedByteBuf(ByteBuf buffer)
/*     */   {
/*  37 */     super(buffer.maxCapacity());
/*     */     
/*  39 */     if ((buffer instanceof DuplicatedByteBuf)) {
/*  40 */       this.buffer = ((DuplicatedByteBuf)buffer).buffer;
/*     */     } else {
/*  42 */       this.buffer = buffer;
/*     */     }
/*     */     
/*  45 */     setIndex(buffer.readerIndex(), buffer.writerIndex());
/*     */   }
/*     */   
/*     */   public ByteBuf unwrap()
/*     */   {
/*  50 */     return this.buffer;
/*     */   }
/*     */   
/*     */   public ByteBufAllocator alloc()
/*     */   {
/*  55 */     return this.buffer.alloc();
/*     */   }
/*     */   
/*     */   public ByteOrder order()
/*     */   {
/*  60 */     return this.buffer.order();
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/*  65 */     return this.buffer.isDirect();
/*     */   }
/*     */   
/*     */   public int capacity()
/*     */   {
/*  70 */     return this.buffer.capacity();
/*     */   }
/*     */   
/*     */   public ByteBuf capacity(int newCapacity)
/*     */   {
/*  75 */     this.buffer.capacity(newCapacity);
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public boolean hasArray()
/*     */   {
/*  81 */     return this.buffer.hasArray();
/*     */   }
/*     */   
/*     */   public byte[] array()
/*     */   {
/*  86 */     return this.buffer.array();
/*     */   }
/*     */   
/*     */   public int arrayOffset()
/*     */   {
/*  91 */     return this.buffer.arrayOffset();
/*     */   }
/*     */   
/*     */   public boolean hasMemoryAddress()
/*     */   {
/*  96 */     return this.buffer.hasMemoryAddress();
/*     */   }
/*     */   
/*     */   public long memoryAddress()
/*     */   {
/* 101 */     return this.buffer.memoryAddress();
/*     */   }
/*     */   
/*     */   public byte getByte(int index)
/*     */   {
/* 106 */     return _getByte(index);
/*     */   }
/*     */   
/*     */   protected byte _getByte(int index)
/*     */   {
/* 111 */     return this.buffer.getByte(index);
/*     */   }
/*     */   
/*     */   public short getShort(int index)
/*     */   {
/* 116 */     return _getShort(index);
/*     */   }
/*     */   
/*     */   protected short _getShort(int index)
/*     */   {
/* 121 */     return this.buffer.getShort(index);
/*     */   }
/*     */   
/*     */   public int getUnsignedMedium(int index)
/*     */   {
/* 126 */     return _getUnsignedMedium(index);
/*     */   }
/*     */   
/*     */   protected int _getUnsignedMedium(int index)
/*     */   {
/* 131 */     return this.buffer.getUnsignedMedium(index);
/*     */   }
/*     */   
/*     */   public int getInt(int index)
/*     */   {
/* 136 */     return _getInt(index);
/*     */   }
/*     */   
/*     */   protected int _getInt(int index)
/*     */   {
/* 141 */     return this.buffer.getInt(index);
/*     */   }
/*     */   
/*     */   public long getLong(int index)
/*     */   {
/* 146 */     return _getLong(index);
/*     */   }
/*     */   
/*     */   protected long _getLong(int index)
/*     */   {
/* 151 */     return this.buffer.getLong(index);
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 156 */     return this.buffer.copy(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuf slice(int index, int length)
/*     */   {
/* 161 */     return this.buffer.slice(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 166 */     this.buffer.getBytes(index, dst, dstIndex, length);
/* 167 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 172 */     this.buffer.getBytes(index, dst, dstIndex, length);
/* 173 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 178 */     this.buffer.getBytes(index, dst);
/* 179 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setByte(int index, int value)
/*     */   {
/* 184 */     _setByte(index, value);
/* 185 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setByte(int index, int value)
/*     */   {
/* 190 */     this.buffer.setByte(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setShort(int index, int value)
/*     */   {
/* 195 */     _setShort(index, value);
/* 196 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setShort(int index, int value)
/*     */   {
/* 201 */     this.buffer.setShort(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setMedium(int index, int value)
/*     */   {
/* 206 */     _setMedium(index, value);
/* 207 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setMedium(int index, int value)
/*     */   {
/* 212 */     this.buffer.setMedium(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setInt(int index, int value)
/*     */   {
/* 217 */     _setInt(index, value);
/* 218 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setInt(int index, int value)
/*     */   {
/* 223 */     this.buffer.setInt(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setLong(int index, long value)
/*     */   {
/* 228 */     _setLong(index, value);
/* 229 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setLong(int index, long value)
/*     */   {
/* 234 */     this.buffer.setLong(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 239 */     this.buffer.setBytes(index, src, srcIndex, length);
/* 240 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/* 245 */     this.buffer.setBytes(index, src, srcIndex, length);
/* 246 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 251 */     this.buffer.setBytes(index, src);
/* 252 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length)
/*     */     throws IOException
/*     */   {
/* 258 */     this.buffer.getBytes(index, out, length);
/* 259 */     return this;
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length)
/*     */     throws IOException
/*     */   {
/* 265 */     return this.buffer.getBytes(index, out, length);
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length)
/*     */     throws IOException
/*     */   {
/* 271 */     return this.buffer.setBytes(index, in, length);
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length)
/*     */     throws IOException
/*     */   {
/* 277 */     return this.buffer.setBytes(index, in, length);
/*     */   }
/*     */   
/*     */   public int nioBufferCount()
/*     */   {
/* 282 */     return this.buffer.nioBufferCount();
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 287 */     return this.buffer.nioBuffers(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 292 */     return nioBuffer(index, length);
/*     */   }
/*     */   
/*     */   public int forEachByte(int index, int length, ByteBufProcessor processor)
/*     */   {
/* 297 */     return this.buffer.forEachByte(index, length, processor);
/*     */   }
/*     */   
/*     */   public int forEachByteDesc(int index, int length, ByteBufProcessor processor)
/*     */   {
/* 302 */     return this.buffer.forEachByteDesc(index, length, processor);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\DuplicatedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */