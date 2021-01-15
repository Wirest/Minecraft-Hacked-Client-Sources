/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.channels.GatheringByteChannel;
/*     */ import java.nio.channels.ScatteringByteChannel;
/*     */ import java.nio.charset.Charset;
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
/*     */ public class WrappedByteBuf
/*     */   extends ByteBuf
/*     */ {
/*     */   protected final ByteBuf buf;
/*     */   
/*     */   protected WrappedByteBuf(ByteBuf buf)
/*     */   {
/*  35 */     if (buf == null) {
/*  36 */       throw new NullPointerException("buf");
/*     */     }
/*  38 */     this.buf = buf;
/*     */   }
/*     */   
/*     */   public boolean hasMemoryAddress()
/*     */   {
/*  43 */     return this.buf.hasMemoryAddress();
/*     */   }
/*     */   
/*     */   public long memoryAddress()
/*     */   {
/*  48 */     return this.buf.memoryAddress();
/*     */   }
/*     */   
/*     */   public int capacity()
/*     */   {
/*  53 */     return this.buf.capacity();
/*     */   }
/*     */   
/*     */   public ByteBuf capacity(int newCapacity)
/*     */   {
/*  58 */     this.buf.capacity(newCapacity);
/*  59 */     return this;
/*     */   }
/*     */   
/*     */   public int maxCapacity()
/*     */   {
/*  64 */     return this.buf.maxCapacity();
/*     */   }
/*     */   
/*     */   public ByteBufAllocator alloc()
/*     */   {
/*  69 */     return this.buf.alloc();
/*     */   }
/*     */   
/*     */   public ByteOrder order()
/*     */   {
/*  74 */     return this.buf.order();
/*     */   }
/*     */   
/*     */   public ByteBuf order(ByteOrder endianness)
/*     */   {
/*  79 */     return this.buf.order(endianness);
/*     */   }
/*     */   
/*     */   public ByteBuf unwrap()
/*     */   {
/*  84 */     return this.buf;
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/*  89 */     return this.buf.isDirect();
/*     */   }
/*     */   
/*     */   public int readerIndex()
/*     */   {
/*  94 */     return this.buf.readerIndex();
/*     */   }
/*     */   
/*     */   public ByteBuf readerIndex(int readerIndex)
/*     */   {
/*  99 */     this.buf.readerIndex(readerIndex);
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public int writerIndex()
/*     */   {
/* 105 */     return this.buf.writerIndex();
/*     */   }
/*     */   
/*     */   public ByteBuf writerIndex(int writerIndex)
/*     */   {
/* 110 */     this.buf.writerIndex(writerIndex);
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setIndex(int readerIndex, int writerIndex)
/*     */   {
/* 116 */     this.buf.setIndex(readerIndex, writerIndex);
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   public int readableBytes()
/*     */   {
/* 122 */     return this.buf.readableBytes();
/*     */   }
/*     */   
/*     */   public int writableBytes()
/*     */   {
/* 127 */     return this.buf.writableBytes();
/*     */   }
/*     */   
/*     */   public int maxWritableBytes()
/*     */   {
/* 132 */     return this.buf.maxWritableBytes();
/*     */   }
/*     */   
/*     */   public boolean isReadable()
/*     */   {
/* 137 */     return this.buf.isReadable();
/*     */   }
/*     */   
/*     */   public boolean isWritable()
/*     */   {
/* 142 */     return this.buf.isWritable();
/*     */   }
/*     */   
/*     */   public ByteBuf clear()
/*     */   {
/* 147 */     this.buf.clear();
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf markReaderIndex()
/*     */   {
/* 153 */     this.buf.markReaderIndex();
/* 154 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf resetReaderIndex()
/*     */   {
/* 159 */     this.buf.resetReaderIndex();
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf markWriterIndex()
/*     */   {
/* 165 */     this.buf.markWriterIndex();
/* 166 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf resetWriterIndex()
/*     */   {
/* 171 */     this.buf.resetWriterIndex();
/* 172 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf discardReadBytes()
/*     */   {
/* 177 */     this.buf.discardReadBytes();
/* 178 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf discardSomeReadBytes()
/*     */   {
/* 183 */     this.buf.discardSomeReadBytes();
/* 184 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf ensureWritable(int minWritableBytes)
/*     */   {
/* 189 */     this.buf.ensureWritable(minWritableBytes);
/* 190 */     return this;
/*     */   }
/*     */   
/*     */   public int ensureWritable(int minWritableBytes, boolean force)
/*     */   {
/* 195 */     return this.buf.ensureWritable(minWritableBytes, force);
/*     */   }
/*     */   
/*     */   public boolean getBoolean(int index)
/*     */   {
/* 200 */     return this.buf.getBoolean(index);
/*     */   }
/*     */   
/*     */   public byte getByte(int index)
/*     */   {
/* 205 */     return this.buf.getByte(index);
/*     */   }
/*     */   
/*     */   public short getUnsignedByte(int index)
/*     */   {
/* 210 */     return this.buf.getUnsignedByte(index);
/*     */   }
/*     */   
/*     */   public short getShort(int index)
/*     */   {
/* 215 */     return this.buf.getShort(index);
/*     */   }
/*     */   
/*     */   public int getUnsignedShort(int index)
/*     */   {
/* 220 */     return this.buf.getUnsignedShort(index);
/*     */   }
/*     */   
/*     */   public int getMedium(int index)
/*     */   {
/* 225 */     return this.buf.getMedium(index);
/*     */   }
/*     */   
/*     */   public int getUnsignedMedium(int index)
/*     */   {
/* 230 */     return this.buf.getUnsignedMedium(index);
/*     */   }
/*     */   
/*     */   public int getInt(int index)
/*     */   {
/* 235 */     return this.buf.getInt(index);
/*     */   }
/*     */   
/*     */   public long getUnsignedInt(int index)
/*     */   {
/* 240 */     return this.buf.getUnsignedInt(index);
/*     */   }
/*     */   
/*     */   public long getLong(int index)
/*     */   {
/* 245 */     return this.buf.getLong(index);
/*     */   }
/*     */   
/*     */   public char getChar(int index)
/*     */   {
/* 250 */     return this.buf.getChar(index);
/*     */   }
/*     */   
/*     */   public float getFloat(int index)
/*     */   {
/* 255 */     return this.buf.getFloat(index);
/*     */   }
/*     */   
/*     */   public double getDouble(int index)
/*     */   {
/* 260 */     return this.buf.getDouble(index);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst)
/*     */   {
/* 265 */     this.buf.getBytes(index, dst);
/* 266 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int length)
/*     */   {
/* 271 */     this.buf.getBytes(index, dst, length);
/* 272 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 277 */     this.buf.getBytes(index, dst, dstIndex, length);
/* 278 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst)
/*     */   {
/* 283 */     this.buf.getBytes(index, dst);
/* 284 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 289 */     this.buf.getBytes(index, dst, dstIndex, length);
/* 290 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 295 */     this.buf.getBytes(index, dst);
/* 296 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException
/*     */   {
/* 301 */     this.buf.getBytes(index, out, length);
/* 302 */     return this;
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 307 */     return this.buf.getBytes(index, out, length);
/*     */   }
/*     */   
/*     */   public ByteBuf setBoolean(int index, boolean value)
/*     */   {
/* 312 */     this.buf.setBoolean(index, value);
/* 313 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setByte(int index, int value)
/*     */   {
/* 318 */     this.buf.setByte(index, value);
/* 319 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setShort(int index, int value)
/*     */   {
/* 324 */     this.buf.setShort(index, value);
/* 325 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setMedium(int index, int value)
/*     */   {
/* 330 */     this.buf.setMedium(index, value);
/* 331 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setInt(int index, int value)
/*     */   {
/* 336 */     this.buf.setInt(index, value);
/* 337 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setLong(int index, long value)
/*     */   {
/* 342 */     this.buf.setLong(index, value);
/* 343 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setChar(int index, int value)
/*     */   {
/* 348 */     this.buf.setChar(index, value);
/* 349 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setFloat(int index, float value)
/*     */   {
/* 354 */     this.buf.setFloat(index, value);
/* 355 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setDouble(int index, double value)
/*     */   {
/* 360 */     this.buf.setDouble(index, value);
/* 361 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src)
/*     */   {
/* 366 */     this.buf.setBytes(index, src);
/* 367 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int length)
/*     */   {
/* 372 */     this.buf.setBytes(index, src, length);
/* 373 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/* 378 */     this.buf.setBytes(index, src, srcIndex, length);
/* 379 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src)
/*     */   {
/* 384 */     this.buf.setBytes(index, src);
/* 385 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 390 */     this.buf.setBytes(index, src, srcIndex, length);
/* 391 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 396 */     this.buf.setBytes(index, src);
/* 397 */     return this;
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException
/*     */   {
/* 402 */     return this.buf.setBytes(index, in, length);
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException
/*     */   {
/* 407 */     return this.buf.setBytes(index, in, length);
/*     */   }
/*     */   
/*     */   public ByteBuf setZero(int index, int length)
/*     */   {
/* 412 */     this.buf.setZero(index, length);
/* 413 */     return this;
/*     */   }
/*     */   
/*     */   public boolean readBoolean()
/*     */   {
/* 418 */     return this.buf.readBoolean();
/*     */   }
/*     */   
/*     */   public byte readByte()
/*     */   {
/* 423 */     return this.buf.readByte();
/*     */   }
/*     */   
/*     */   public short readUnsignedByte()
/*     */   {
/* 428 */     return this.buf.readUnsignedByte();
/*     */   }
/*     */   
/*     */   public short readShort()
/*     */   {
/* 433 */     return this.buf.readShort();
/*     */   }
/*     */   
/*     */   public int readUnsignedShort()
/*     */   {
/* 438 */     return this.buf.readUnsignedShort();
/*     */   }
/*     */   
/*     */   public int readMedium()
/*     */   {
/* 443 */     return this.buf.readMedium();
/*     */   }
/*     */   
/*     */   public int readUnsignedMedium()
/*     */   {
/* 448 */     return this.buf.readUnsignedMedium();
/*     */   }
/*     */   
/*     */   public int readInt()
/*     */   {
/* 453 */     return this.buf.readInt();
/*     */   }
/*     */   
/*     */   public long readUnsignedInt()
/*     */   {
/* 458 */     return this.buf.readUnsignedInt();
/*     */   }
/*     */   
/*     */   public long readLong()
/*     */   {
/* 463 */     return this.buf.readLong();
/*     */   }
/*     */   
/*     */   public char readChar()
/*     */   {
/* 468 */     return this.buf.readChar();
/*     */   }
/*     */   
/*     */   public float readFloat()
/*     */   {
/* 473 */     return this.buf.readFloat();
/*     */   }
/*     */   
/*     */   public double readDouble()
/*     */   {
/* 478 */     return this.buf.readDouble();
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(int length)
/*     */   {
/* 483 */     return this.buf.readBytes(length);
/*     */   }
/*     */   
/*     */   public ByteBuf readSlice(int length)
/*     */   {
/* 488 */     return this.buf.readSlice(length);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuf dst)
/*     */   {
/* 493 */     this.buf.readBytes(dst);
/* 494 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuf dst, int length)
/*     */   {
/* 499 */     this.buf.readBytes(dst, length);
/* 500 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 505 */     this.buf.readBytes(dst, dstIndex, length);
/* 506 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(byte[] dst)
/*     */   {
/* 511 */     this.buf.readBytes(dst);
/* 512 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length)
/*     */   {
/* 517 */     this.buf.readBytes(dst, dstIndex, length);
/* 518 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuffer dst)
/*     */   {
/* 523 */     this.buf.readBytes(dst);
/* 524 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(OutputStream out, int length) throws IOException
/*     */   {
/* 529 */     this.buf.readBytes(out, length);
/* 530 */     return this;
/*     */   }
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 535 */     return this.buf.readBytes(out, length);
/*     */   }
/*     */   
/*     */   public ByteBuf skipBytes(int length)
/*     */   {
/* 540 */     this.buf.skipBytes(length);
/* 541 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeBoolean(boolean value)
/*     */   {
/* 546 */     this.buf.writeBoolean(value);
/* 547 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeByte(int value)
/*     */   {
/* 552 */     this.buf.writeByte(value);
/* 553 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeShort(int value)
/*     */   {
/* 558 */     this.buf.writeShort(value);
/* 559 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeMedium(int value)
/*     */   {
/* 564 */     this.buf.writeMedium(value);
/* 565 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeInt(int value)
/*     */   {
/* 570 */     this.buf.writeInt(value);
/* 571 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeLong(long value)
/*     */   {
/* 576 */     this.buf.writeLong(value);
/* 577 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeChar(int value)
/*     */   {
/* 582 */     this.buf.writeChar(value);
/* 583 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeFloat(float value)
/*     */   {
/* 588 */     this.buf.writeFloat(value);
/* 589 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeDouble(double value)
/*     */   {
/* 594 */     this.buf.writeDouble(value);
/* 595 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuf src)
/*     */   {
/* 600 */     this.buf.writeBytes(src);
/* 601 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuf src, int length)
/*     */   {
/* 606 */     this.buf.writeBytes(src, length);
/* 607 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length)
/*     */   {
/* 612 */     this.buf.writeBytes(src, srcIndex, length);
/* 613 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(byte[] src)
/*     */   {
/* 618 */     this.buf.writeBytes(src);
/* 619 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(byte[] src, int srcIndex, int length)
/*     */   {
/* 624 */     this.buf.writeBytes(src, srcIndex, length);
/* 625 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuffer src)
/*     */   {
/* 630 */     this.buf.writeBytes(src);
/* 631 */     return this;
/*     */   }
/*     */   
/*     */   public int writeBytes(InputStream in, int length) throws IOException
/*     */   {
/* 636 */     return this.buf.writeBytes(in, length);
/*     */   }
/*     */   
/*     */   public int writeBytes(ScatteringByteChannel in, int length) throws IOException
/*     */   {
/* 641 */     return this.buf.writeBytes(in, length);
/*     */   }
/*     */   
/*     */   public ByteBuf writeZero(int length)
/*     */   {
/* 646 */     this.buf.writeZero(length);
/* 647 */     return this;
/*     */   }
/*     */   
/*     */   public int indexOf(int fromIndex, int toIndex, byte value)
/*     */   {
/* 652 */     return this.buf.indexOf(fromIndex, toIndex, value);
/*     */   }
/*     */   
/*     */   public int bytesBefore(byte value)
/*     */   {
/* 657 */     return this.buf.bytesBefore(value);
/*     */   }
/*     */   
/*     */   public int bytesBefore(int length, byte value)
/*     */   {
/* 662 */     return this.buf.bytesBefore(length, value);
/*     */   }
/*     */   
/*     */   public int bytesBefore(int index, int length, byte value)
/*     */   {
/* 667 */     return this.buf.bytesBefore(index, length, value);
/*     */   }
/*     */   
/*     */   public int forEachByte(ByteBufProcessor processor)
/*     */   {
/* 672 */     return this.buf.forEachByte(processor);
/*     */   }
/*     */   
/*     */   public int forEachByte(int index, int length, ByteBufProcessor processor)
/*     */   {
/* 677 */     return this.buf.forEachByte(index, length, processor);
/*     */   }
/*     */   
/*     */   public int forEachByteDesc(ByteBufProcessor processor)
/*     */   {
/* 682 */     return this.buf.forEachByteDesc(processor);
/*     */   }
/*     */   
/*     */   public int forEachByteDesc(int index, int length, ByteBufProcessor processor)
/*     */   {
/* 687 */     return this.buf.forEachByteDesc(index, length, processor);
/*     */   }
/*     */   
/*     */   public ByteBuf copy()
/*     */   {
/* 692 */     return this.buf.copy();
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 697 */     return this.buf.copy(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuf slice()
/*     */   {
/* 702 */     return this.buf.slice();
/*     */   }
/*     */   
/*     */   public ByteBuf slice(int index, int length)
/*     */   {
/* 707 */     return this.buf.slice(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuf duplicate()
/*     */   {
/* 712 */     return this.buf.duplicate();
/*     */   }
/*     */   
/*     */   public int nioBufferCount()
/*     */   {
/* 717 */     return this.buf.nioBufferCount();
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer()
/*     */   {
/* 722 */     return this.buf.nioBuffer();
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length)
/*     */   {
/* 727 */     return this.buf.nioBuffer(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers()
/*     */   {
/* 732 */     return this.buf.nioBuffers();
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 737 */     return this.buf.nioBuffers(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 742 */     return this.buf.internalNioBuffer(index, length);
/*     */   }
/*     */   
/*     */   public boolean hasArray()
/*     */   {
/* 747 */     return this.buf.hasArray();
/*     */   }
/*     */   
/*     */   public byte[] array()
/*     */   {
/* 752 */     return this.buf.array();
/*     */   }
/*     */   
/*     */   public int arrayOffset()
/*     */   {
/* 757 */     return this.buf.arrayOffset();
/*     */   }
/*     */   
/*     */   public String toString(Charset charset)
/*     */   {
/* 762 */     return this.buf.toString(charset);
/*     */   }
/*     */   
/*     */   public String toString(int index, int length, Charset charset)
/*     */   {
/* 767 */     return this.buf.toString(index, length, charset);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 772 */     return this.buf.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 778 */     return this.buf.equals(obj);
/*     */   }
/*     */   
/*     */   public int compareTo(ByteBuf buffer)
/*     */   {
/* 783 */     return this.buf.compareTo(buffer);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 788 */     return StringUtil.simpleClassName(this) + '(' + this.buf.toString() + ')';
/*     */   }
/*     */   
/*     */   public ByteBuf retain(int increment)
/*     */   {
/* 793 */     this.buf.retain(increment);
/* 794 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf retain()
/*     */   {
/* 799 */     this.buf.retain();
/* 800 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf touch()
/*     */   {
/* 805 */     this.buf.touch();
/* 806 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf touch(Object hint)
/*     */   {
/* 811 */     this.buf.touch(hint);
/* 812 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isReadable(int size)
/*     */   {
/* 817 */     return this.buf.isReadable(size);
/*     */   }
/*     */   
/*     */   public boolean isWritable(int size)
/*     */   {
/* 822 */     return this.buf.isWritable(size);
/*     */   }
/*     */   
/*     */   public int refCnt()
/*     */   {
/* 827 */     return this.buf.refCnt();
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/* 832 */     return this.buf.release();
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 837 */     return this.buf.release(decrement);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\WrappedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */