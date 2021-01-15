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
/*     */ 
/*     */ public class SlicedByteBuf
/*     */   extends AbstractDerivedByteBuf
/*     */ {
/*     */   private final ByteBuf buffer;
/*     */   private final int adjustment;
/*     */   private final int length;
/*     */   
/*     */   public SlicedByteBuf(ByteBuf buffer, int index, int length)
/*     */   {
/*  40 */     super(length);
/*  41 */     if ((index < 0) || (index > buffer.capacity() - length)) {
/*  42 */       throw new IndexOutOfBoundsException(buffer + ".slice(" + index + ", " + length + ')');
/*     */     }
/*     */     
/*  45 */     if ((buffer instanceof SlicedByteBuf)) {
/*  46 */       this.buffer = ((SlicedByteBuf)buffer).buffer;
/*  47 */       this.adjustment = (((SlicedByteBuf)buffer).adjustment + index);
/*  48 */     } else if ((buffer instanceof DuplicatedByteBuf)) {
/*  49 */       this.buffer = buffer.unwrap();
/*  50 */       this.adjustment = index;
/*     */     } else {
/*  52 */       this.buffer = buffer;
/*  53 */       this.adjustment = index;
/*     */     }
/*  55 */     this.length = length;
/*     */     
/*  57 */     writerIndex(length);
/*     */   }
/*     */   
/*     */   public ByteBuf unwrap()
/*     */   {
/*  62 */     return this.buffer;
/*     */   }
/*     */   
/*     */   public ByteBufAllocator alloc()
/*     */   {
/*  67 */     return this.buffer.alloc();
/*     */   }
/*     */   
/*     */   public ByteOrder order()
/*     */   {
/*  72 */     return this.buffer.order();
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/*  77 */     return this.buffer.isDirect();
/*     */   }
/*     */   
/*     */   public int capacity()
/*     */   {
/*  82 */     return this.length;
/*     */   }
/*     */   
/*     */   public ByteBuf capacity(int newCapacity)
/*     */   {
/*  87 */     throw new UnsupportedOperationException("sliced buffer");
/*     */   }
/*     */   
/*     */   public boolean hasArray()
/*     */   {
/*  92 */     return this.buffer.hasArray();
/*     */   }
/*     */   
/*     */   public byte[] array()
/*     */   {
/*  97 */     return this.buffer.array();
/*     */   }
/*     */   
/*     */   public int arrayOffset()
/*     */   {
/* 102 */     return this.buffer.arrayOffset() + this.adjustment;
/*     */   }
/*     */   
/*     */   public boolean hasMemoryAddress()
/*     */   {
/* 107 */     return this.buffer.hasMemoryAddress();
/*     */   }
/*     */   
/*     */   public long memoryAddress()
/*     */   {
/* 112 */     return this.buffer.memoryAddress() + this.adjustment;
/*     */   }
/*     */   
/*     */   protected byte _getByte(int index)
/*     */   {
/* 117 */     return this.buffer.getByte(index + this.adjustment);
/*     */   }
/*     */   
/*     */   protected short _getShort(int index)
/*     */   {
/* 122 */     return this.buffer.getShort(index + this.adjustment);
/*     */   }
/*     */   
/*     */   protected int _getUnsignedMedium(int index)
/*     */   {
/* 127 */     return this.buffer.getUnsignedMedium(index + this.adjustment);
/*     */   }
/*     */   
/*     */   protected int _getInt(int index)
/*     */   {
/* 132 */     return this.buffer.getInt(index + this.adjustment);
/*     */   }
/*     */   
/*     */   protected long _getLong(int index)
/*     */   {
/* 137 */     return this.buffer.getLong(index + this.adjustment);
/*     */   }
/*     */   
/*     */   public ByteBuf duplicate()
/*     */   {
/* 142 */     ByteBuf duplicate = this.buffer.slice(this.adjustment, this.length);
/* 143 */     duplicate.setIndex(readerIndex(), writerIndex());
/* 144 */     return duplicate;
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 149 */     checkIndex(index, length);
/* 150 */     return this.buffer.copy(index + this.adjustment, length);
/*     */   }
/*     */   
/*     */   public ByteBuf slice(int index, int length)
/*     */   {
/* 155 */     checkIndex(index, length);
/* 156 */     if (length == 0) {
/* 157 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/* 159 */     return this.buffer.slice(index + this.adjustment, length);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 164 */     checkIndex(index, length);
/* 165 */     this.buffer.getBytes(index + this.adjustment, dst, dstIndex, length);
/* 166 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 171 */     checkIndex(index, length);
/* 172 */     this.buffer.getBytes(index + this.adjustment, dst, dstIndex, length);
/* 173 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 178 */     checkIndex(index, dst.remaining());
/* 179 */     this.buffer.getBytes(index + this.adjustment, dst);
/* 180 */     return this;
/*     */   }
/*     */   
/*     */   protected void _setByte(int index, int value)
/*     */   {
/* 185 */     this.buffer.setByte(index + this.adjustment, value);
/*     */   }
/*     */   
/*     */   protected void _setShort(int index, int value)
/*     */   {
/* 190 */     this.buffer.setShort(index + this.adjustment, value);
/*     */   }
/*     */   
/*     */   protected void _setMedium(int index, int value)
/*     */   {
/* 195 */     this.buffer.setMedium(index + this.adjustment, value);
/*     */   }
/*     */   
/*     */   protected void _setInt(int index, int value)
/*     */   {
/* 200 */     this.buffer.setInt(index + this.adjustment, value);
/*     */   }
/*     */   
/*     */   protected void _setLong(int index, long value)
/*     */   {
/* 205 */     this.buffer.setLong(index + this.adjustment, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 210 */     checkIndex(index, length);
/* 211 */     this.buffer.setBytes(index + this.adjustment, src, srcIndex, length);
/* 212 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/* 217 */     checkIndex(index, length);
/* 218 */     this.buffer.setBytes(index + this.adjustment, src, srcIndex, length);
/* 219 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 224 */     checkIndex(index, src.remaining());
/* 225 */     this.buffer.setBytes(index + this.adjustment, src);
/* 226 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException
/*     */   {
/* 231 */     checkIndex(index, length);
/* 232 */     this.buffer.getBytes(index + this.adjustment, out, length);
/* 233 */     return this;
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 238 */     checkIndex(index, length);
/* 239 */     return this.buffer.getBytes(index + this.adjustment, out, length);
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException
/*     */   {
/* 244 */     checkIndex(index, length);
/* 245 */     return this.buffer.setBytes(index + this.adjustment, in, length);
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException
/*     */   {
/* 250 */     checkIndex(index, length);
/* 251 */     return this.buffer.setBytes(index + this.adjustment, in, length);
/*     */   }
/*     */   
/*     */   public int nioBufferCount()
/*     */   {
/* 256 */     return this.buffer.nioBufferCount();
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length)
/*     */   {
/* 261 */     checkIndex(index, length);
/* 262 */     return this.buffer.nioBuffer(index + this.adjustment, length);
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 267 */     checkIndex(index, length);
/* 268 */     return this.buffer.nioBuffers(index + this.adjustment, length);
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 273 */     checkIndex(index, length);
/* 274 */     return nioBuffer(index, length);
/*     */   }
/*     */   
/*     */   public int forEachByte(int index, int length, ByteBufProcessor processor)
/*     */   {
/* 279 */     int ret = this.buffer.forEachByte(index + this.adjustment, length, processor);
/* 280 */     if (ret >= this.adjustment) {
/* 281 */       return ret - this.adjustment;
/*     */     }
/* 283 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public int forEachByteDesc(int index, int length, ByteBufProcessor processor)
/*     */   {
/* 289 */     int ret = this.buffer.forEachByteDesc(index + this.adjustment, length, processor);
/* 290 */     if (ret >= this.adjustment) {
/* 291 */       return ret - this.adjustment;
/*     */     }
/* 293 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\SlicedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */