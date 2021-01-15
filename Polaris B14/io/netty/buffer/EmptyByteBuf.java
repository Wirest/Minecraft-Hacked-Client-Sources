/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.ReadOnlyBufferException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EmptyByteBuf
/*     */   extends ByteBuf
/*     */ {
/*  37 */   private static final ByteBuffer EMPTY_BYTE_BUFFER = ByteBuffer.allocateDirect(0);
/*     */   private static final long EMPTY_BYTE_BUFFER_ADDRESS;
/*     */   private final ByteBufAllocator alloc;
/*     */   
/*  41 */   static { long emptyByteBufferAddress = 0L;
/*     */     try {
/*  43 */       if (PlatformDependent.hasUnsafe()) {
/*  44 */         emptyByteBufferAddress = PlatformDependent.directBufferAddress(EMPTY_BYTE_BUFFER);
/*     */       }
/*     */     }
/*     */     catch (Throwable t) {}
/*     */     
/*  49 */     EMPTY_BYTE_BUFFER_ADDRESS = emptyByteBufferAddress;
/*     */   }
/*     */   
/*     */ 
/*     */   private final ByteOrder order;
/*     */   private final String str;
/*     */   private EmptyByteBuf swapped;
/*     */   public EmptyByteBuf(ByteBufAllocator alloc)
/*     */   {
/*  58 */     this(alloc, ByteOrder.BIG_ENDIAN);
/*     */   }
/*     */   
/*     */   private EmptyByteBuf(ByteBufAllocator alloc, ByteOrder order) {
/*  62 */     if (alloc == null) {
/*  63 */       throw new NullPointerException("alloc");
/*     */     }
/*     */     
/*  66 */     this.alloc = alloc;
/*  67 */     this.order = order;
/*  68 */     this.str = (StringUtil.simpleClassName(this) + (order == ByteOrder.BIG_ENDIAN ? "BE" : "LE"));
/*     */   }
/*     */   
/*     */   public int capacity()
/*     */   {
/*  73 */     return 0;
/*     */   }
/*     */   
/*     */   public ByteBuf capacity(int newCapacity)
/*     */   {
/*  78 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBufAllocator alloc()
/*     */   {
/*  83 */     return this.alloc;
/*     */   }
/*     */   
/*     */   public ByteOrder order()
/*     */   {
/*  88 */     return this.order;
/*     */   }
/*     */   
/*     */   public ByteBuf unwrap()
/*     */   {
/*  93 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/*  98 */     return true;
/*     */   }
/*     */   
/*     */   public int maxCapacity()
/*     */   {
/* 103 */     return 0;
/*     */   }
/*     */   
/*     */   public ByteBuf order(ByteOrder endianness)
/*     */   {
/* 108 */     if (endianness == null) {
/* 109 */       throw new NullPointerException("endianness");
/*     */     }
/* 111 */     if (endianness == order()) {
/* 112 */       return this;
/*     */     }
/*     */     
/* 115 */     EmptyByteBuf swapped = this.swapped;
/* 116 */     if (swapped != null) {
/* 117 */       return swapped;
/*     */     }
/*     */     
/* 120 */     this.swapped = (swapped = new EmptyByteBuf(alloc(), endianness));
/* 121 */     return swapped;
/*     */   }
/*     */   
/*     */   public int readerIndex()
/*     */   {
/* 126 */     return 0;
/*     */   }
/*     */   
/*     */   public ByteBuf readerIndex(int readerIndex)
/*     */   {
/* 131 */     return checkIndex(readerIndex);
/*     */   }
/*     */   
/*     */   public int writerIndex()
/*     */   {
/* 136 */     return 0;
/*     */   }
/*     */   
/*     */   public ByteBuf writerIndex(int writerIndex)
/*     */   {
/* 141 */     return checkIndex(writerIndex);
/*     */   }
/*     */   
/*     */   public ByteBuf setIndex(int readerIndex, int writerIndex)
/*     */   {
/* 146 */     checkIndex(readerIndex);
/* 147 */     checkIndex(writerIndex);
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   public int readableBytes()
/*     */   {
/* 153 */     return 0;
/*     */   }
/*     */   
/*     */   public int writableBytes()
/*     */   {
/* 158 */     return 0;
/*     */   }
/*     */   
/*     */   public int maxWritableBytes()
/*     */   {
/* 163 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isReadable()
/*     */   {
/* 168 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isWritable()
/*     */   {
/* 173 */     return false;
/*     */   }
/*     */   
/*     */   public ByteBuf clear()
/*     */   {
/* 178 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf markReaderIndex()
/*     */   {
/* 183 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf resetReaderIndex()
/*     */   {
/* 188 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf markWriterIndex()
/*     */   {
/* 193 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf resetWriterIndex()
/*     */   {
/* 198 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf discardReadBytes()
/*     */   {
/* 203 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf discardSomeReadBytes()
/*     */   {
/* 208 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf ensureWritable(int minWritableBytes)
/*     */   {
/* 213 */     if (minWritableBytes < 0) {
/* 214 */       throw new IllegalArgumentException("minWritableBytes: " + minWritableBytes + " (expected: >= 0)");
/*     */     }
/* 216 */     if (minWritableBytes != 0) {
/* 217 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 219 */     return this;
/*     */   }
/*     */   
/*     */   public int ensureWritable(int minWritableBytes, boolean force)
/*     */   {
/* 224 */     if (minWritableBytes < 0) {
/* 225 */       throw new IllegalArgumentException("minWritableBytes: " + minWritableBytes + " (expected: >= 0)");
/*     */     }
/*     */     
/* 228 */     if (minWritableBytes == 0) {
/* 229 */       return 0;
/*     */     }
/*     */     
/* 232 */     return 1;
/*     */   }
/*     */   
/*     */   public boolean getBoolean(int index)
/*     */   {
/* 237 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public byte getByte(int index)
/*     */   {
/* 242 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public short getUnsignedByte(int index)
/*     */   {
/* 247 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public short getShort(int index)
/*     */   {
/* 252 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public int getUnsignedShort(int index)
/*     */   {
/* 257 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public int getMedium(int index)
/*     */   {
/* 262 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public int getUnsignedMedium(int index)
/*     */   {
/* 267 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public int getInt(int index)
/*     */   {
/* 272 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public long getUnsignedInt(int index)
/*     */   {
/* 277 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public long getLong(int index)
/*     */   {
/* 282 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public char getChar(int index)
/*     */   {
/* 287 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public float getFloat(int index)
/*     */   {
/* 292 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public double getDouble(int index)
/*     */   {
/* 297 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst)
/*     */   {
/* 302 */     return checkIndex(index, dst.writableBytes());
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int length)
/*     */   {
/* 307 */     return checkIndex(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 312 */     return checkIndex(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst)
/*     */   {
/* 317 */     return checkIndex(index, dst.length);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 322 */     return checkIndex(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 327 */     return checkIndex(index, dst.remaining());
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length)
/*     */   {
/* 332 */     return checkIndex(index, length);
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length)
/*     */   {
/* 337 */     checkIndex(index, length);
/* 338 */     return 0;
/*     */   }
/*     */   
/*     */   public ByteBuf setBoolean(int index, boolean value)
/*     */   {
/* 343 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf setByte(int index, int value)
/*     */   {
/* 348 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf setShort(int index, int value)
/*     */   {
/* 353 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf setMedium(int index, int value)
/*     */   {
/* 358 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf setInt(int index, int value)
/*     */   {
/* 363 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf setLong(int index, long value)
/*     */   {
/* 368 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf setChar(int index, int value)
/*     */   {
/* 373 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf setFloat(int index, float value)
/*     */   {
/* 378 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf setDouble(int index, double value)
/*     */   {
/* 383 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src)
/*     */   {
/* 388 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int length)
/*     */   {
/* 393 */     return checkIndex(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/* 398 */     return checkIndex(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src)
/*     */   {
/* 403 */     return checkIndex(index, src.length);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 408 */     return checkIndex(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 413 */     return checkIndex(index, src.remaining());
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length)
/*     */   {
/* 418 */     checkIndex(index, length);
/* 419 */     return 0;
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length)
/*     */   {
/* 424 */     checkIndex(index, length);
/* 425 */     return 0;
/*     */   }
/*     */   
/*     */   public ByteBuf setZero(int index, int length)
/*     */   {
/* 430 */     return checkIndex(index, length);
/*     */   }
/*     */   
/*     */   public boolean readBoolean()
/*     */   {
/* 435 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public byte readByte()
/*     */   {
/* 440 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public short readUnsignedByte()
/*     */   {
/* 445 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public short readShort()
/*     */   {
/* 450 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public int readUnsignedShort()
/*     */   {
/* 455 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public int readMedium()
/*     */   {
/* 460 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public int readUnsignedMedium()
/*     */   {
/* 465 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public int readInt()
/*     */   {
/* 470 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public long readUnsignedInt()
/*     */   {
/* 475 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public long readLong()
/*     */   {
/* 480 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public char readChar()
/*     */   {
/* 485 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public float readFloat()
/*     */   {
/* 490 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public double readDouble()
/*     */   {
/* 495 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(int length)
/*     */   {
/* 500 */     return checkLength(length);
/*     */   }
/*     */   
/*     */   public ByteBuf readSlice(int length)
/*     */   {
/* 505 */     return checkLength(length);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuf dst)
/*     */   {
/* 510 */     return checkLength(dst.writableBytes());
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuf dst, int length)
/*     */   {
/* 515 */     return checkLength(length);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 520 */     return checkLength(length);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(byte[] dst)
/*     */   {
/* 525 */     return checkLength(dst.length);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length)
/*     */   {
/* 530 */     return checkLength(length);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuffer dst)
/*     */   {
/* 535 */     return checkLength(dst.remaining());
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(OutputStream out, int length)
/*     */   {
/* 540 */     return checkLength(length);
/*     */   }
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length)
/*     */   {
/* 545 */     checkLength(length);
/* 546 */     return 0;
/*     */   }
/*     */   
/*     */   public ByteBuf skipBytes(int length)
/*     */   {
/* 551 */     return checkLength(length);
/*     */   }
/*     */   
/*     */   public ByteBuf writeBoolean(boolean value)
/*     */   {
/* 556 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf writeByte(int value)
/*     */   {
/* 561 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf writeShort(int value)
/*     */   {
/* 566 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf writeMedium(int value)
/*     */   {
/* 571 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf writeInt(int value)
/*     */   {
/* 576 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf writeLong(long value)
/*     */   {
/* 581 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf writeChar(int value)
/*     */   {
/* 586 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf writeFloat(float value)
/*     */   {
/* 591 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf writeDouble(double value)
/*     */   {
/* 596 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuf src)
/*     */   {
/* 601 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuf src, int length)
/*     */   {
/* 606 */     return checkLength(length);
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length)
/*     */   {
/* 611 */     return checkLength(length);
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(byte[] src)
/*     */   {
/* 616 */     return checkLength(src.length);
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(byte[] src, int srcIndex, int length)
/*     */   {
/* 621 */     return checkLength(length);
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuffer src)
/*     */   {
/* 626 */     return checkLength(src.remaining());
/*     */   }
/*     */   
/*     */   public int writeBytes(InputStream in, int length)
/*     */   {
/* 631 */     checkLength(length);
/* 632 */     return 0;
/*     */   }
/*     */   
/*     */   public int writeBytes(ScatteringByteChannel in, int length)
/*     */   {
/* 637 */     checkLength(length);
/* 638 */     return 0;
/*     */   }
/*     */   
/*     */   public ByteBuf writeZero(int length)
/*     */   {
/* 643 */     return checkLength(length);
/*     */   }
/*     */   
/*     */   public int indexOf(int fromIndex, int toIndex, byte value)
/*     */   {
/* 648 */     checkIndex(fromIndex);
/* 649 */     checkIndex(toIndex);
/* 650 */     return -1;
/*     */   }
/*     */   
/*     */   public int bytesBefore(byte value)
/*     */   {
/* 655 */     return -1;
/*     */   }
/*     */   
/*     */   public int bytesBefore(int length, byte value)
/*     */   {
/* 660 */     checkLength(length);
/* 661 */     return -1;
/*     */   }
/*     */   
/*     */   public int bytesBefore(int index, int length, byte value)
/*     */   {
/* 666 */     checkIndex(index, length);
/* 667 */     return -1;
/*     */   }
/*     */   
/*     */   public int forEachByte(ByteBufProcessor processor)
/*     */   {
/* 672 */     return -1;
/*     */   }
/*     */   
/*     */   public int forEachByte(int index, int length, ByteBufProcessor processor)
/*     */   {
/* 677 */     checkIndex(index, length);
/* 678 */     return -1;
/*     */   }
/*     */   
/*     */   public int forEachByteDesc(ByteBufProcessor processor)
/*     */   {
/* 683 */     return -1;
/*     */   }
/*     */   
/*     */   public int forEachByteDesc(int index, int length, ByteBufProcessor processor)
/*     */   {
/* 688 */     checkIndex(index, length);
/* 689 */     return -1;
/*     */   }
/*     */   
/*     */   public ByteBuf copy()
/*     */   {
/* 694 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 699 */     return checkIndex(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuf slice()
/*     */   {
/* 704 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf slice(int index, int length)
/*     */   {
/* 709 */     return checkIndex(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuf duplicate()
/*     */   {
/* 714 */     return this;
/*     */   }
/*     */   
/*     */   public int nioBufferCount()
/*     */   {
/* 719 */     return 1;
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer()
/*     */   {
/* 724 */     return EMPTY_BYTE_BUFFER;
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length)
/*     */   {
/* 729 */     checkIndex(index, length);
/* 730 */     return nioBuffer();
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers()
/*     */   {
/* 735 */     return new ByteBuffer[] { EMPTY_BYTE_BUFFER };
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 740 */     checkIndex(index, length);
/* 741 */     return nioBuffers();
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 746 */     return EMPTY_BYTE_BUFFER;
/*     */   }
/*     */   
/*     */   public boolean hasArray()
/*     */   {
/* 751 */     return true;
/*     */   }
/*     */   
/*     */   public byte[] array()
/*     */   {
/* 756 */     return EmptyArrays.EMPTY_BYTES;
/*     */   }
/*     */   
/*     */   public int arrayOffset()
/*     */   {
/* 761 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean hasMemoryAddress()
/*     */   {
/* 766 */     return EMPTY_BYTE_BUFFER_ADDRESS != 0L;
/*     */   }
/*     */   
/*     */   public long memoryAddress()
/*     */   {
/* 771 */     if (hasMemoryAddress()) {
/* 772 */       return EMPTY_BYTE_BUFFER_ADDRESS;
/*     */     }
/* 774 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString(Charset charset)
/*     */   {
/* 780 */     return "";
/*     */   }
/*     */   
/*     */   public String toString(int index, int length, Charset charset)
/*     */   {
/* 785 */     checkIndex(index, length);
/* 786 */     return toString(charset);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 791 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 796 */     return ((obj instanceof ByteBuf)) && (!((ByteBuf)obj).isReadable());
/*     */   }
/*     */   
/*     */   public int compareTo(ByteBuf buffer)
/*     */   {
/* 801 */     return buffer.isReadable() ? -1 : 0;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 806 */     return this.str;
/*     */   }
/*     */   
/*     */   public boolean isReadable(int size)
/*     */   {
/* 811 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isWritable(int size)
/*     */   {
/* 816 */     return false;
/*     */   }
/*     */   
/*     */   public int refCnt()
/*     */   {
/* 821 */     return 1;
/*     */   }
/*     */   
/*     */   public ByteBuf retain()
/*     */   {
/* 826 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf retain(int increment)
/*     */   {
/* 831 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf touch()
/*     */   {
/* 836 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf touch(Object hint)
/*     */   {
/* 841 */     return this;
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/* 846 */     return false;
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 851 */     return false;
/*     */   }
/*     */   
/*     */   private ByteBuf checkIndex(int index) {
/* 855 */     if (index != 0) {
/* 856 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 858 */     return this;
/*     */   }
/*     */   
/*     */   private ByteBuf checkIndex(int index, int length) {
/* 862 */     if (length < 0) {
/* 863 */       throw new IllegalArgumentException("length: " + length);
/*     */     }
/* 865 */     if ((index != 0) || (length != 0)) {
/* 866 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 868 */     return this;
/*     */   }
/*     */   
/*     */   private ByteBuf checkLength(int length) {
/* 872 */     if (length < 0) {
/* 873 */       throw new IllegalArgumentException("length: " + length + " (expected: >= 0)");
/*     */     }
/* 875 */     if (length != 0) {
/* 876 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 878 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\EmptyByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */