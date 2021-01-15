/*     */ package io.netty.buffer;
/*     */ 
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
/*     */ 
/*     */ public class SwappedByteBuf
/*     */   extends ByteBuf
/*     */ {
/*     */   private final ByteBuf buf;
/*     */   private final ByteOrder order;
/*     */   
/*     */   public SwappedByteBuf(ByteBuf buf)
/*     */   {
/*  36 */     if (buf == null) {
/*  37 */       throw new NullPointerException("buf");
/*     */     }
/*  39 */     this.buf = buf;
/*  40 */     if (buf.order() == ByteOrder.BIG_ENDIAN) {
/*  41 */       this.order = ByteOrder.LITTLE_ENDIAN;
/*     */     } else {
/*  43 */       this.order = ByteOrder.BIG_ENDIAN;
/*     */     }
/*     */   }
/*     */   
/*     */   public ByteOrder order()
/*     */   {
/*  49 */     return this.order;
/*     */   }
/*     */   
/*     */   public ByteBuf order(ByteOrder endianness)
/*     */   {
/*  54 */     if (endianness == null) {
/*  55 */       throw new NullPointerException("endianness");
/*     */     }
/*  57 */     if (endianness == this.order) {
/*  58 */       return this;
/*     */     }
/*  60 */     return this.buf;
/*     */   }
/*     */   
/*     */   public ByteBuf unwrap()
/*     */   {
/*  65 */     return this.buf.unwrap();
/*     */   }
/*     */   
/*     */   public ByteBufAllocator alloc()
/*     */   {
/*  70 */     return this.buf.alloc();
/*     */   }
/*     */   
/*     */   public int capacity()
/*     */   {
/*  75 */     return this.buf.capacity();
/*     */   }
/*     */   
/*     */   public ByteBuf capacity(int newCapacity)
/*     */   {
/*  80 */     this.buf.capacity(newCapacity);
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public int maxCapacity()
/*     */   {
/*  86 */     return this.buf.maxCapacity();
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/*  91 */     return this.buf.isDirect();
/*     */   }
/*     */   
/*     */   public int readerIndex()
/*     */   {
/*  96 */     return this.buf.readerIndex();
/*     */   }
/*     */   
/*     */   public ByteBuf readerIndex(int readerIndex)
/*     */   {
/* 101 */     this.buf.readerIndex(readerIndex);
/* 102 */     return this;
/*     */   }
/*     */   
/*     */   public int writerIndex()
/*     */   {
/* 107 */     return this.buf.writerIndex();
/*     */   }
/*     */   
/*     */   public ByteBuf writerIndex(int writerIndex)
/*     */   {
/* 112 */     this.buf.writerIndex(writerIndex);
/* 113 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setIndex(int readerIndex, int writerIndex)
/*     */   {
/* 118 */     this.buf.setIndex(readerIndex, writerIndex);
/* 119 */     return this;
/*     */   }
/*     */   
/*     */   public int readableBytes()
/*     */   {
/* 124 */     return this.buf.readableBytes();
/*     */   }
/*     */   
/*     */   public int writableBytes()
/*     */   {
/* 129 */     return this.buf.writableBytes();
/*     */   }
/*     */   
/*     */   public int maxWritableBytes()
/*     */   {
/* 134 */     return this.buf.maxWritableBytes();
/*     */   }
/*     */   
/*     */   public boolean isReadable()
/*     */   {
/* 139 */     return this.buf.isReadable();
/*     */   }
/*     */   
/*     */   public boolean isReadable(int size)
/*     */   {
/* 144 */     return this.buf.isReadable(size);
/*     */   }
/*     */   
/*     */   public boolean isWritable()
/*     */   {
/* 149 */     return this.buf.isWritable();
/*     */   }
/*     */   
/*     */   public boolean isWritable(int size)
/*     */   {
/* 154 */     return this.buf.isWritable(size);
/*     */   }
/*     */   
/*     */   public ByteBuf clear()
/*     */   {
/* 159 */     this.buf.clear();
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf markReaderIndex()
/*     */   {
/* 165 */     this.buf.markReaderIndex();
/* 166 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf resetReaderIndex()
/*     */   {
/* 171 */     this.buf.resetReaderIndex();
/* 172 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf markWriterIndex()
/*     */   {
/* 177 */     this.buf.markWriterIndex();
/* 178 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf resetWriterIndex()
/*     */   {
/* 183 */     this.buf.resetWriterIndex();
/* 184 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf discardReadBytes()
/*     */   {
/* 189 */     this.buf.discardReadBytes();
/* 190 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf discardSomeReadBytes()
/*     */   {
/* 195 */     this.buf.discardSomeReadBytes();
/* 196 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf ensureWritable(int writableBytes)
/*     */   {
/* 201 */     this.buf.ensureWritable(writableBytes);
/* 202 */     return this;
/*     */   }
/*     */   
/*     */   public int ensureWritable(int minWritableBytes, boolean force)
/*     */   {
/* 207 */     return this.buf.ensureWritable(minWritableBytes, force);
/*     */   }
/*     */   
/*     */   public boolean getBoolean(int index)
/*     */   {
/* 212 */     return this.buf.getBoolean(index);
/*     */   }
/*     */   
/*     */   public byte getByte(int index)
/*     */   {
/* 217 */     return this.buf.getByte(index);
/*     */   }
/*     */   
/*     */   public short getUnsignedByte(int index)
/*     */   {
/* 222 */     return this.buf.getUnsignedByte(index);
/*     */   }
/*     */   
/*     */   public short getShort(int index)
/*     */   {
/* 227 */     return ByteBufUtil.swapShort(this.buf.getShort(index));
/*     */   }
/*     */   
/*     */   public int getUnsignedShort(int index)
/*     */   {
/* 232 */     return getShort(index) & 0xFFFF;
/*     */   }
/*     */   
/*     */   public int getMedium(int index)
/*     */   {
/* 237 */     return ByteBufUtil.swapMedium(this.buf.getMedium(index));
/*     */   }
/*     */   
/*     */   public int getUnsignedMedium(int index)
/*     */   {
/* 242 */     return getMedium(index) & 0xFFFFFF;
/*     */   }
/*     */   
/*     */   public int getInt(int index)
/*     */   {
/* 247 */     return ByteBufUtil.swapInt(this.buf.getInt(index));
/*     */   }
/*     */   
/*     */   public long getUnsignedInt(int index)
/*     */   {
/* 252 */     return getInt(index) & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long getLong(int index)
/*     */   {
/* 257 */     return ByteBufUtil.swapLong(this.buf.getLong(index));
/*     */   }
/*     */   
/*     */   public char getChar(int index)
/*     */   {
/* 262 */     return (char)getShort(index);
/*     */   }
/*     */   
/*     */   public float getFloat(int index)
/*     */   {
/* 267 */     return Float.intBitsToFloat(getInt(index));
/*     */   }
/*     */   
/*     */   public double getDouble(int index)
/*     */   {
/* 272 */     return Double.longBitsToDouble(getLong(index));
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst)
/*     */   {
/* 277 */     this.buf.getBytes(index, dst);
/* 278 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int length)
/*     */   {
/* 283 */     this.buf.getBytes(index, dst, length);
/* 284 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 289 */     this.buf.getBytes(index, dst, dstIndex, length);
/* 290 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst)
/*     */   {
/* 295 */     this.buf.getBytes(index, dst);
/* 296 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 301 */     this.buf.getBytes(index, dst, dstIndex, length);
/* 302 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 307 */     this.buf.getBytes(index, dst);
/* 308 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException
/*     */   {
/* 313 */     this.buf.getBytes(index, out, length);
/* 314 */     return this;
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 319 */     return this.buf.getBytes(index, out, length);
/*     */   }
/*     */   
/*     */   public ByteBuf setBoolean(int index, boolean value)
/*     */   {
/* 324 */     this.buf.setBoolean(index, value);
/* 325 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setByte(int index, int value)
/*     */   {
/* 330 */     this.buf.setByte(index, value);
/* 331 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setShort(int index, int value)
/*     */   {
/* 336 */     this.buf.setShort(index, ByteBufUtil.swapShort((short)value));
/* 337 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setMedium(int index, int value)
/*     */   {
/* 342 */     this.buf.setMedium(index, ByteBufUtil.swapMedium(value));
/* 343 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setInt(int index, int value)
/*     */   {
/* 348 */     this.buf.setInt(index, ByteBufUtil.swapInt(value));
/* 349 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setLong(int index, long value)
/*     */   {
/* 354 */     this.buf.setLong(index, ByteBufUtil.swapLong(value));
/* 355 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setChar(int index, int value)
/*     */   {
/* 360 */     setShort(index, value);
/* 361 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setFloat(int index, float value)
/*     */   {
/* 366 */     setInt(index, Float.floatToRawIntBits(value));
/* 367 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setDouble(int index, double value)
/*     */   {
/* 372 */     setLong(index, Double.doubleToRawLongBits(value));
/* 373 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src)
/*     */   {
/* 378 */     this.buf.setBytes(index, src);
/* 379 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int length)
/*     */   {
/* 384 */     this.buf.setBytes(index, src, length);
/* 385 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/* 390 */     this.buf.setBytes(index, src, srcIndex, length);
/* 391 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src)
/*     */   {
/* 396 */     this.buf.setBytes(index, src);
/* 397 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 402 */     this.buf.setBytes(index, src, srcIndex, length);
/* 403 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 408 */     this.buf.setBytes(index, src);
/* 409 */     return this;
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException
/*     */   {
/* 414 */     return this.buf.setBytes(index, in, length);
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException
/*     */   {
/* 419 */     return this.buf.setBytes(index, in, length);
/*     */   }
/*     */   
/*     */   public ByteBuf setZero(int index, int length)
/*     */   {
/* 424 */     this.buf.setZero(index, length);
/* 425 */     return this;
/*     */   }
/*     */   
/*     */   public boolean readBoolean()
/*     */   {
/* 430 */     return this.buf.readBoolean();
/*     */   }
/*     */   
/*     */   public byte readByte()
/*     */   {
/* 435 */     return this.buf.readByte();
/*     */   }
/*     */   
/*     */   public short readUnsignedByte()
/*     */   {
/* 440 */     return this.buf.readUnsignedByte();
/*     */   }
/*     */   
/*     */   public short readShort()
/*     */   {
/* 445 */     return ByteBufUtil.swapShort(this.buf.readShort());
/*     */   }
/*     */   
/*     */   public int readUnsignedShort()
/*     */   {
/* 450 */     return readShort() & 0xFFFF;
/*     */   }
/*     */   
/*     */   public int readMedium()
/*     */   {
/* 455 */     return ByteBufUtil.swapMedium(this.buf.readMedium());
/*     */   }
/*     */   
/*     */   public int readUnsignedMedium()
/*     */   {
/* 460 */     return readMedium() & 0xFFFFFF;
/*     */   }
/*     */   
/*     */   public int readInt()
/*     */   {
/* 465 */     return ByteBufUtil.swapInt(this.buf.readInt());
/*     */   }
/*     */   
/*     */   public long readUnsignedInt()
/*     */   {
/* 470 */     return readInt() & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long readLong()
/*     */   {
/* 475 */     return ByteBufUtil.swapLong(this.buf.readLong());
/*     */   }
/*     */   
/*     */   public char readChar()
/*     */   {
/* 480 */     return (char)readShort();
/*     */   }
/*     */   
/*     */   public float readFloat()
/*     */   {
/* 485 */     return Float.intBitsToFloat(readInt());
/*     */   }
/*     */   
/*     */   public double readDouble()
/*     */   {
/* 490 */     return Double.longBitsToDouble(readLong());
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(int length)
/*     */   {
/* 495 */     return this.buf.readBytes(length).order(order());
/*     */   }
/*     */   
/*     */   public ByteBuf readSlice(int length)
/*     */   {
/* 500 */     return this.buf.readSlice(length).order(this.order);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuf dst)
/*     */   {
/* 505 */     this.buf.readBytes(dst);
/* 506 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuf dst, int length)
/*     */   {
/* 511 */     this.buf.readBytes(dst, length);
/* 512 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 517 */     this.buf.readBytes(dst, dstIndex, length);
/* 518 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(byte[] dst)
/*     */   {
/* 523 */     this.buf.readBytes(dst);
/* 524 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length)
/*     */   {
/* 529 */     this.buf.readBytes(dst, dstIndex, length);
/* 530 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuffer dst)
/*     */   {
/* 535 */     this.buf.readBytes(dst);
/* 536 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(OutputStream out, int length) throws IOException
/*     */   {
/* 541 */     this.buf.readBytes(out, length);
/* 542 */     return this;
/*     */   }
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 547 */     return this.buf.readBytes(out, length);
/*     */   }
/*     */   
/*     */   public ByteBuf skipBytes(int length)
/*     */   {
/* 552 */     this.buf.skipBytes(length);
/* 553 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeBoolean(boolean value)
/*     */   {
/* 558 */     this.buf.writeBoolean(value);
/* 559 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeByte(int value)
/*     */   {
/* 564 */     this.buf.writeByte(value);
/* 565 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeShort(int value)
/*     */   {
/* 570 */     this.buf.writeShort(ByteBufUtil.swapShort((short)value));
/* 571 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeMedium(int value)
/*     */   {
/* 576 */     this.buf.writeMedium(ByteBufUtil.swapMedium(value));
/* 577 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeInt(int value)
/*     */   {
/* 582 */     this.buf.writeInt(ByteBufUtil.swapInt(value));
/* 583 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeLong(long value)
/*     */   {
/* 588 */     this.buf.writeLong(ByteBufUtil.swapLong(value));
/* 589 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeChar(int value)
/*     */   {
/* 594 */     writeShort(value);
/* 595 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeFloat(float value)
/*     */   {
/* 600 */     writeInt(Float.floatToRawIntBits(value));
/* 601 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeDouble(double value)
/*     */   {
/* 606 */     writeLong(Double.doubleToRawLongBits(value));
/* 607 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuf src)
/*     */   {
/* 612 */     this.buf.writeBytes(src);
/* 613 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuf src, int length)
/*     */   {
/* 618 */     this.buf.writeBytes(src, length);
/* 619 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length)
/*     */   {
/* 624 */     this.buf.writeBytes(src, srcIndex, length);
/* 625 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(byte[] src)
/*     */   {
/* 630 */     this.buf.writeBytes(src);
/* 631 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(byte[] src, int srcIndex, int length)
/*     */   {
/* 636 */     this.buf.writeBytes(src, srcIndex, length);
/* 637 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuffer src)
/*     */   {
/* 642 */     this.buf.writeBytes(src);
/* 643 */     return this;
/*     */   }
/*     */   
/*     */   public int writeBytes(InputStream in, int length) throws IOException
/*     */   {
/* 648 */     return this.buf.writeBytes(in, length);
/*     */   }
/*     */   
/*     */   public int writeBytes(ScatteringByteChannel in, int length) throws IOException
/*     */   {
/* 653 */     return this.buf.writeBytes(in, length);
/*     */   }
/*     */   
/*     */   public ByteBuf writeZero(int length)
/*     */   {
/* 658 */     this.buf.writeZero(length);
/* 659 */     return this;
/*     */   }
/*     */   
/*     */   public int indexOf(int fromIndex, int toIndex, byte value)
/*     */   {
/* 664 */     return this.buf.indexOf(fromIndex, toIndex, value);
/*     */   }
/*     */   
/*     */   public int bytesBefore(byte value)
/*     */   {
/* 669 */     return this.buf.bytesBefore(value);
/*     */   }
/*     */   
/*     */   public int bytesBefore(int length, byte value)
/*     */   {
/* 674 */     return this.buf.bytesBefore(length, value);
/*     */   }
/*     */   
/*     */   public int bytesBefore(int index, int length, byte value)
/*     */   {
/* 679 */     return this.buf.bytesBefore(index, length, value);
/*     */   }
/*     */   
/*     */   public int forEachByte(ByteBufProcessor processor)
/*     */   {
/* 684 */     return this.buf.forEachByte(processor);
/*     */   }
/*     */   
/*     */   public int forEachByte(int index, int length, ByteBufProcessor processor)
/*     */   {
/* 689 */     return this.buf.forEachByte(index, length, processor);
/*     */   }
/*     */   
/*     */   public int forEachByteDesc(ByteBufProcessor processor)
/*     */   {
/* 694 */     return this.buf.forEachByteDesc(processor);
/*     */   }
/*     */   
/*     */   public int forEachByteDesc(int index, int length, ByteBufProcessor processor)
/*     */   {
/* 699 */     return this.buf.forEachByteDesc(index, length, processor);
/*     */   }
/*     */   
/*     */   public ByteBuf copy()
/*     */   {
/* 704 */     return this.buf.copy().order(this.order);
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 709 */     return this.buf.copy(index, length).order(this.order);
/*     */   }
/*     */   
/*     */   public ByteBuf slice()
/*     */   {
/* 714 */     return this.buf.slice().order(this.order);
/*     */   }
/*     */   
/*     */   public ByteBuf slice(int index, int length)
/*     */   {
/* 719 */     return this.buf.slice(index, length).order(this.order);
/*     */   }
/*     */   
/*     */   public ByteBuf duplicate()
/*     */   {
/* 724 */     return this.buf.duplicate().order(this.order);
/*     */   }
/*     */   
/*     */   public int nioBufferCount()
/*     */   {
/* 729 */     return this.buf.nioBufferCount();
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer()
/*     */   {
/* 734 */     return this.buf.nioBuffer().order(this.order);
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length)
/*     */   {
/* 739 */     return this.buf.nioBuffer(index, length).order(this.order);
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 744 */     return nioBuffer(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers()
/*     */   {
/* 749 */     ByteBuffer[] nioBuffers = this.buf.nioBuffers();
/* 750 */     for (int i = 0; i < nioBuffers.length; i++) {
/* 751 */       nioBuffers[i] = nioBuffers[i].order(this.order);
/*     */     }
/* 753 */     return nioBuffers;
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 758 */     ByteBuffer[] nioBuffers = this.buf.nioBuffers(index, length);
/* 759 */     for (int i = 0; i < nioBuffers.length; i++) {
/* 760 */       nioBuffers[i] = nioBuffers[i].order(this.order);
/*     */     }
/* 762 */     return nioBuffers;
/*     */   }
/*     */   
/*     */   public boolean hasArray()
/*     */   {
/* 767 */     return this.buf.hasArray();
/*     */   }
/*     */   
/*     */   public byte[] array()
/*     */   {
/* 772 */     return this.buf.array();
/*     */   }
/*     */   
/*     */   public int arrayOffset()
/*     */   {
/* 777 */     return this.buf.arrayOffset();
/*     */   }
/*     */   
/*     */   public boolean hasMemoryAddress()
/*     */   {
/* 782 */     return this.buf.hasMemoryAddress();
/*     */   }
/*     */   
/*     */   public long memoryAddress()
/*     */   {
/* 787 */     return this.buf.memoryAddress();
/*     */   }
/*     */   
/*     */   public String toString(Charset charset)
/*     */   {
/* 792 */     return this.buf.toString(charset);
/*     */   }
/*     */   
/*     */   public String toString(int index, int length, Charset charset)
/*     */   {
/* 797 */     return this.buf.toString(index, length, charset);
/*     */   }
/*     */   
/*     */   public int refCnt()
/*     */   {
/* 802 */     return this.buf.refCnt();
/*     */   }
/*     */   
/*     */   public ByteBuf retain()
/*     */   {
/* 807 */     this.buf.retain();
/* 808 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf retain(int increment)
/*     */   {
/* 813 */     this.buf.retain(increment);
/* 814 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf touch()
/*     */   {
/* 819 */     this.buf.touch();
/* 820 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf touch(Object hint)
/*     */   {
/* 825 */     this.buf.touch(hint);
/* 826 */     return this;
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/* 831 */     return this.buf.release();
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 836 */     return this.buf.release(decrement);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 841 */     return this.buf.hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 846 */     if (this == obj) {
/* 847 */       return true;
/*     */     }
/* 849 */     if ((obj instanceof ByteBuf)) {
/* 850 */       return ByteBufUtil.equals(this, (ByteBuf)obj);
/*     */     }
/* 852 */     return false;
/*     */   }
/*     */   
/*     */   public int compareTo(ByteBuf buffer)
/*     */   {
/* 857 */     return ByteBufUtil.compare(this, buffer);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 862 */     return "Swapped(" + this.buf + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\SwappedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */