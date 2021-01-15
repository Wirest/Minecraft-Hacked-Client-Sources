/*      */ package io.netty.handler.codec;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.buffer.ByteBufProcessor;
/*      */ import io.netty.buffer.SwappedByteBuf;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.util.Signal;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.channels.GatheringByteChannel;
/*      */ import java.nio.channels.ScatteringByteChannel;
/*      */ import java.nio.charset.Charset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ final class ReplayingDecoderBuffer
/*      */   extends ByteBuf
/*      */ {
/*   39 */   private static final Signal REPLAY = ReplayingDecoder.REPLAY;
/*      */   
/*      */   private ByteBuf buffer;
/*      */   
/*      */   private boolean terminated;
/*      */   private SwappedByteBuf swapped;
/*   45 */   static final ReplayingDecoderBuffer EMPTY_BUFFER = new ReplayingDecoderBuffer(Unpooled.EMPTY_BUFFER);
/*      */   
/*      */   static {
/*   48 */     EMPTY_BUFFER.terminate();
/*      */   }
/*      */   
/*      */ 
/*      */   ReplayingDecoderBuffer(ByteBuf buffer)
/*      */   {
/*   54 */     setCumulation(buffer);
/*      */   }
/*      */   
/*      */   void setCumulation(ByteBuf buffer) {
/*   58 */     this.buffer = buffer;
/*      */   }
/*      */   
/*      */   void terminate() {
/*   62 */     this.terminated = true;
/*      */   }
/*      */   
/*      */   public int capacity()
/*      */   {
/*   67 */     if (this.terminated) {
/*   68 */       return this.buffer.capacity();
/*      */     }
/*   70 */     return Integer.MAX_VALUE;
/*      */   }
/*      */   
/*      */ 
/*      */   public ByteBuf capacity(int newCapacity)
/*      */   {
/*   76 */     reject();
/*   77 */     return this;
/*      */   }
/*      */   
/*      */   public int maxCapacity()
/*      */   {
/*   82 */     return capacity();
/*      */   }
/*      */   
/*      */   public ByteBufAllocator alloc()
/*      */   {
/*   87 */     return this.buffer.alloc();
/*      */   }
/*      */   
/*      */   public boolean isDirect()
/*      */   {
/*   92 */     return this.buffer.isDirect();
/*      */   }
/*      */   
/*      */   public boolean hasArray()
/*      */   {
/*   97 */     return false;
/*      */   }
/*      */   
/*      */   public byte[] array()
/*      */   {
/*  102 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */   public int arrayOffset()
/*      */   {
/*  107 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */   public boolean hasMemoryAddress()
/*      */   {
/*  112 */     return false;
/*      */   }
/*      */   
/*      */   public long memoryAddress()
/*      */   {
/*  117 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */   public ByteBuf clear()
/*      */   {
/*  122 */     reject();
/*  123 */     return this;
/*      */   }
/*      */   
/*      */   public boolean equals(Object obj)
/*      */   {
/*  128 */     return this == obj;
/*      */   }
/*      */   
/*      */   public int compareTo(ByteBuf buffer)
/*      */   {
/*  133 */     reject();
/*  134 */     return 0;
/*      */   }
/*      */   
/*      */   public ByteBuf copy()
/*      */   {
/*  139 */     reject();
/*  140 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf copy(int index, int length)
/*      */   {
/*  145 */     checkIndex(index, length);
/*  146 */     return this.buffer.copy(index, length);
/*      */   }
/*      */   
/*      */   public ByteBuf discardReadBytes()
/*      */   {
/*  151 */     reject();
/*  152 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf ensureWritable(int writableBytes)
/*      */   {
/*  157 */     reject();
/*  158 */     return this;
/*      */   }
/*      */   
/*      */   public int ensureWritable(int minWritableBytes, boolean force)
/*      */   {
/*  163 */     reject();
/*  164 */     return 0;
/*      */   }
/*      */   
/*      */   public ByteBuf duplicate()
/*      */   {
/*  169 */     reject();
/*  170 */     return this;
/*      */   }
/*      */   
/*      */   public boolean getBoolean(int index)
/*      */   {
/*  175 */     checkIndex(index, 1);
/*  176 */     return this.buffer.getBoolean(index);
/*      */   }
/*      */   
/*      */   public byte getByte(int index)
/*      */   {
/*  181 */     checkIndex(index, 1);
/*  182 */     return this.buffer.getByte(index);
/*      */   }
/*      */   
/*      */   public short getUnsignedByte(int index)
/*      */   {
/*  187 */     checkIndex(index, 1);
/*  188 */     return this.buffer.getUnsignedByte(index);
/*      */   }
/*      */   
/*      */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*      */   {
/*  193 */     checkIndex(index, length);
/*  194 */     this.buffer.getBytes(index, dst, dstIndex, length);
/*  195 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf getBytes(int index, byte[] dst)
/*      */   {
/*  200 */     checkIndex(index, dst.length);
/*  201 */     this.buffer.getBytes(index, dst);
/*  202 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*      */   {
/*  207 */     reject();
/*  208 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*      */   {
/*  213 */     checkIndex(index, length);
/*  214 */     this.buffer.getBytes(index, dst, dstIndex, length);
/*  215 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst, int length)
/*      */   {
/*  220 */     reject();
/*  221 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst)
/*      */   {
/*  226 */     reject();
/*  227 */     return this;
/*      */   }
/*      */   
/*      */   public int getBytes(int index, GatheringByteChannel out, int length)
/*      */   {
/*  232 */     reject();
/*  233 */     return 0;
/*      */   }
/*      */   
/*      */   public ByteBuf getBytes(int index, OutputStream out, int length)
/*      */   {
/*  238 */     reject();
/*  239 */     return this;
/*      */   }
/*      */   
/*      */   public int getInt(int index)
/*      */   {
/*  244 */     checkIndex(index, 4);
/*  245 */     return this.buffer.getInt(index);
/*      */   }
/*      */   
/*      */   public long getUnsignedInt(int index)
/*      */   {
/*  250 */     checkIndex(index, 4);
/*  251 */     return this.buffer.getUnsignedInt(index);
/*      */   }
/*      */   
/*      */   public long getLong(int index)
/*      */   {
/*  256 */     checkIndex(index, 8);
/*  257 */     return this.buffer.getLong(index);
/*      */   }
/*      */   
/*      */   public int getMedium(int index)
/*      */   {
/*  262 */     checkIndex(index, 3);
/*  263 */     return this.buffer.getMedium(index);
/*      */   }
/*      */   
/*      */   public int getUnsignedMedium(int index)
/*      */   {
/*  268 */     checkIndex(index, 3);
/*  269 */     return this.buffer.getUnsignedMedium(index);
/*      */   }
/*      */   
/*      */   public short getShort(int index)
/*      */   {
/*  274 */     checkIndex(index, 2);
/*  275 */     return this.buffer.getShort(index);
/*      */   }
/*      */   
/*      */   public int getUnsignedShort(int index)
/*      */   {
/*  280 */     checkIndex(index, 2);
/*  281 */     return this.buffer.getUnsignedShort(index);
/*      */   }
/*      */   
/*      */   public char getChar(int index)
/*      */   {
/*  286 */     checkIndex(index, 2);
/*  287 */     return this.buffer.getChar(index);
/*      */   }
/*      */   
/*      */   public float getFloat(int index)
/*      */   {
/*  292 */     checkIndex(index, 4);
/*  293 */     return this.buffer.getFloat(index);
/*      */   }
/*      */   
/*      */   public double getDouble(int index)
/*      */   {
/*  298 */     checkIndex(index, 8);
/*  299 */     return this.buffer.getDouble(index);
/*      */   }
/*      */   
/*      */   public int hashCode()
/*      */   {
/*  304 */     reject();
/*  305 */     return 0;
/*      */   }
/*      */   
/*      */   public int indexOf(int fromIndex, int toIndex, byte value)
/*      */   {
/*  310 */     if (fromIndex == toIndex) {
/*  311 */       return -1;
/*      */     }
/*      */     
/*  314 */     if (Math.max(fromIndex, toIndex) > this.buffer.writerIndex()) {
/*  315 */       throw REPLAY;
/*      */     }
/*      */     
/*  318 */     return this.buffer.indexOf(fromIndex, toIndex, value);
/*      */   }
/*      */   
/*      */   public int bytesBefore(byte value)
/*      */   {
/*  323 */     int bytes = this.buffer.bytesBefore(value);
/*  324 */     if (bytes < 0) {
/*  325 */       throw REPLAY;
/*      */     }
/*  327 */     return bytes;
/*      */   }
/*      */   
/*      */   public int bytesBefore(int length, byte value)
/*      */   {
/*  332 */     return bytesBefore(this.buffer.readerIndex(), length, value);
/*      */   }
/*      */   
/*      */   public int bytesBefore(int index, int length, byte value)
/*      */   {
/*  337 */     int writerIndex = this.buffer.writerIndex();
/*  338 */     if (index >= writerIndex) {
/*  339 */       throw REPLAY;
/*      */     }
/*      */     
/*  342 */     if (index <= writerIndex - length) {
/*  343 */       return this.buffer.bytesBefore(index, length, value);
/*      */     }
/*      */     
/*  346 */     int res = this.buffer.bytesBefore(index, writerIndex - index, value);
/*  347 */     if (res < 0) {
/*  348 */       throw REPLAY;
/*      */     }
/*  350 */     return res;
/*      */   }
/*      */   
/*      */ 
/*      */   public int forEachByte(ByteBufProcessor processor)
/*      */   {
/*  356 */     int ret = this.buffer.forEachByte(processor);
/*  357 */     if (ret < 0) {
/*  358 */       throw REPLAY;
/*      */     }
/*  360 */     return ret;
/*      */   }
/*      */   
/*      */ 
/*      */   public int forEachByte(int index, int length, ByteBufProcessor processor)
/*      */   {
/*  366 */     int writerIndex = this.buffer.writerIndex();
/*  367 */     if (index >= writerIndex) {
/*  368 */       throw REPLAY;
/*      */     }
/*      */     
/*  371 */     if (index <= writerIndex - length) {
/*  372 */       return this.buffer.forEachByte(index, length, processor);
/*      */     }
/*      */     
/*  375 */     int ret = this.buffer.forEachByte(index, writerIndex - index, processor);
/*  376 */     if (ret < 0) {
/*  377 */       throw REPLAY;
/*      */     }
/*  379 */     return ret;
/*      */   }
/*      */   
/*      */ 
/*      */   public int forEachByteDesc(ByteBufProcessor processor)
/*      */   {
/*  385 */     if (this.terminated) {
/*  386 */       return this.buffer.forEachByteDesc(processor);
/*      */     }
/*  388 */     reject();
/*  389 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public int forEachByteDesc(int index, int length, ByteBufProcessor processor)
/*      */   {
/*  395 */     if (index + length > this.buffer.writerIndex()) {
/*  396 */       throw REPLAY;
/*      */     }
/*      */     
/*  399 */     return this.buffer.forEachByteDesc(index, length, processor);
/*      */   }
/*      */   
/*      */   public ByteBuf markReaderIndex()
/*      */   {
/*  404 */     this.buffer.markReaderIndex();
/*  405 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf markWriterIndex()
/*      */   {
/*  410 */     reject();
/*  411 */     return this;
/*      */   }
/*      */   
/*      */   public ByteOrder order()
/*      */   {
/*  416 */     return this.buffer.order();
/*      */   }
/*      */   
/*      */   public ByteBuf order(ByteOrder endianness)
/*      */   {
/*  421 */     if (endianness == null) {
/*  422 */       throw new NullPointerException("endianness");
/*      */     }
/*  424 */     if (endianness == order()) {
/*  425 */       return this;
/*      */     }
/*      */     
/*  428 */     SwappedByteBuf swapped = this.swapped;
/*  429 */     if (swapped == null) {
/*  430 */       this.swapped = (swapped = new SwappedByteBuf(this));
/*      */     }
/*  432 */     return swapped;
/*      */   }
/*      */   
/*      */   public boolean isReadable()
/*      */   {
/*  437 */     return this.terminated ? this.buffer.isReadable() : true;
/*      */   }
/*      */   
/*      */   public boolean isReadable(int size)
/*      */   {
/*  442 */     return this.terminated ? this.buffer.isReadable(size) : true;
/*      */   }
/*      */   
/*      */   public int readableBytes()
/*      */   {
/*  447 */     if (this.terminated) {
/*  448 */       return this.buffer.readableBytes();
/*      */     }
/*  450 */     return Integer.MAX_VALUE - this.buffer.readerIndex();
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean readBoolean()
/*      */   {
/*  456 */     checkReadableBytes(1);
/*  457 */     return this.buffer.readBoolean();
/*      */   }
/*      */   
/*      */   public byte readByte()
/*      */   {
/*  462 */     checkReadableBytes(1);
/*  463 */     return this.buffer.readByte();
/*      */   }
/*      */   
/*      */   public short readUnsignedByte()
/*      */   {
/*  468 */     checkReadableBytes(1);
/*  469 */     return this.buffer.readUnsignedByte();
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length)
/*      */   {
/*  474 */     checkReadableBytes(length);
/*  475 */     this.buffer.readBytes(dst, dstIndex, length);
/*  476 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(byte[] dst)
/*      */   {
/*  481 */     checkReadableBytes(dst.length);
/*  482 */     this.buffer.readBytes(dst);
/*  483 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(ByteBuffer dst)
/*      */   {
/*  488 */     reject();
/*  489 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length)
/*      */   {
/*  494 */     checkReadableBytes(length);
/*  495 */     this.buffer.readBytes(dst, dstIndex, length);
/*  496 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst, int length)
/*      */   {
/*  501 */     reject();
/*  502 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst)
/*      */   {
/*  507 */     checkReadableBytes(dst.writableBytes());
/*  508 */     this.buffer.readBytes(dst);
/*  509 */     return this;
/*      */   }
/*      */   
/*      */   public int readBytes(GatheringByteChannel out, int length)
/*      */   {
/*  514 */     reject();
/*  515 */     return 0;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(int length)
/*      */   {
/*  520 */     checkReadableBytes(length);
/*  521 */     return this.buffer.readBytes(length);
/*      */   }
/*      */   
/*      */   public ByteBuf readSlice(int length)
/*      */   {
/*  526 */     checkReadableBytes(length);
/*  527 */     return this.buffer.readSlice(length);
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(OutputStream out, int length)
/*      */   {
/*  532 */     reject();
/*  533 */     return this;
/*      */   }
/*      */   
/*      */   public int readerIndex()
/*      */   {
/*  538 */     return this.buffer.readerIndex();
/*      */   }
/*      */   
/*      */   public ByteBuf readerIndex(int readerIndex)
/*      */   {
/*  543 */     this.buffer.readerIndex(readerIndex);
/*  544 */     return this;
/*      */   }
/*      */   
/*      */   public int readInt()
/*      */   {
/*  549 */     checkReadableBytes(4);
/*  550 */     return this.buffer.readInt();
/*      */   }
/*      */   
/*      */   public long readUnsignedInt()
/*      */   {
/*  555 */     checkReadableBytes(4);
/*  556 */     return this.buffer.readUnsignedInt();
/*      */   }
/*      */   
/*      */   public long readLong()
/*      */   {
/*  561 */     checkReadableBytes(8);
/*  562 */     return this.buffer.readLong();
/*      */   }
/*      */   
/*      */   public int readMedium()
/*      */   {
/*  567 */     checkReadableBytes(3);
/*  568 */     return this.buffer.readMedium();
/*      */   }
/*      */   
/*      */   public int readUnsignedMedium()
/*      */   {
/*  573 */     checkReadableBytes(3);
/*  574 */     return this.buffer.readUnsignedMedium();
/*      */   }
/*      */   
/*      */   public short readShort()
/*      */   {
/*  579 */     checkReadableBytes(2);
/*  580 */     return this.buffer.readShort();
/*      */   }
/*      */   
/*      */   public int readUnsignedShort()
/*      */   {
/*  585 */     checkReadableBytes(2);
/*  586 */     return this.buffer.readUnsignedShort();
/*      */   }
/*      */   
/*      */   public char readChar()
/*      */   {
/*  591 */     checkReadableBytes(2);
/*  592 */     return this.buffer.readChar();
/*      */   }
/*      */   
/*      */   public float readFloat()
/*      */   {
/*  597 */     checkReadableBytes(4);
/*  598 */     return this.buffer.readFloat();
/*      */   }
/*      */   
/*      */   public double readDouble()
/*      */   {
/*  603 */     checkReadableBytes(8);
/*  604 */     return this.buffer.readDouble();
/*      */   }
/*      */   
/*      */   public ByteBuf resetReaderIndex()
/*      */   {
/*  609 */     this.buffer.resetReaderIndex();
/*  610 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf resetWriterIndex()
/*      */   {
/*  615 */     reject();
/*  616 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setBoolean(int index, boolean value)
/*      */   {
/*  621 */     reject();
/*  622 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setByte(int index, int value)
/*      */   {
/*  627 */     reject();
/*  628 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*      */   {
/*  633 */     reject();
/*  634 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setBytes(int index, byte[] src)
/*      */   {
/*  639 */     reject();
/*  640 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuffer src)
/*      */   {
/*  645 */     reject();
/*  646 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*      */   {
/*  651 */     reject();
/*  652 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src, int length)
/*      */   {
/*  657 */     reject();
/*  658 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src)
/*      */   {
/*  663 */     reject();
/*  664 */     return this;
/*      */   }
/*      */   
/*      */   public int setBytes(int index, InputStream in, int length)
/*      */   {
/*  669 */     reject();
/*  670 */     return 0;
/*      */   }
/*      */   
/*      */   public ByteBuf setZero(int index, int length)
/*      */   {
/*  675 */     reject();
/*  676 */     return this;
/*      */   }
/*      */   
/*      */   public int setBytes(int index, ScatteringByteChannel in, int length)
/*      */   {
/*  681 */     reject();
/*  682 */     return 0;
/*      */   }
/*      */   
/*      */   public ByteBuf setIndex(int readerIndex, int writerIndex)
/*      */   {
/*  687 */     reject();
/*  688 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setInt(int index, int value)
/*      */   {
/*  693 */     reject();
/*  694 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setLong(int index, long value)
/*      */   {
/*  699 */     reject();
/*  700 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setMedium(int index, int value)
/*      */   {
/*  705 */     reject();
/*  706 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setShort(int index, int value)
/*      */   {
/*  711 */     reject();
/*  712 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setChar(int index, int value)
/*      */   {
/*  717 */     reject();
/*  718 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setFloat(int index, float value)
/*      */   {
/*  723 */     reject();
/*  724 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setDouble(int index, double value)
/*      */   {
/*  729 */     reject();
/*  730 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf skipBytes(int length)
/*      */   {
/*  735 */     checkReadableBytes(length);
/*  736 */     this.buffer.skipBytes(length);
/*  737 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf slice()
/*      */   {
/*  742 */     reject();
/*  743 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf slice(int index, int length)
/*      */   {
/*  748 */     checkIndex(index, length);
/*  749 */     return this.buffer.slice(index, length);
/*      */   }
/*      */   
/*      */   public int nioBufferCount()
/*      */   {
/*  754 */     return this.buffer.nioBufferCount();
/*      */   }
/*      */   
/*      */   public ByteBuffer nioBuffer()
/*      */   {
/*  759 */     reject();
/*  760 */     return null;
/*      */   }
/*      */   
/*      */   public ByteBuffer nioBuffer(int index, int length)
/*      */   {
/*  765 */     checkIndex(index, length);
/*  766 */     return this.buffer.nioBuffer(index, length);
/*      */   }
/*      */   
/*      */   public ByteBuffer[] nioBuffers()
/*      */   {
/*  771 */     reject();
/*  772 */     return null;
/*      */   }
/*      */   
/*      */   public ByteBuffer[] nioBuffers(int index, int length)
/*      */   {
/*  777 */     checkIndex(index, length);
/*  778 */     return this.buffer.nioBuffers(index, length);
/*      */   }
/*      */   
/*      */   public ByteBuffer internalNioBuffer(int index, int length)
/*      */   {
/*  783 */     checkIndex(index, length);
/*  784 */     return this.buffer.internalNioBuffer(index, length);
/*      */   }
/*      */   
/*      */   public String toString(int index, int length, Charset charset)
/*      */   {
/*  789 */     checkIndex(index, length);
/*  790 */     return this.buffer.toString(index, length, charset);
/*      */   }
/*      */   
/*      */   public String toString(Charset charsetName)
/*      */   {
/*  795 */     reject();
/*  796 */     return null;
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/*  801 */     return StringUtil.simpleClassName(this) + '(' + "ridx=" + readerIndex() + ", " + "widx=" + writerIndex() + ')';
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isWritable()
/*      */   {
/*  812 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isWritable(int size)
/*      */   {
/*  817 */     return false;
/*      */   }
/*      */   
/*      */   public int writableBytes()
/*      */   {
/*  822 */     return 0;
/*      */   }
/*      */   
/*      */   public int maxWritableBytes()
/*      */   {
/*  827 */     return 0;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBoolean(boolean value)
/*      */   {
/*  832 */     reject();
/*  833 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeByte(int value)
/*      */   {
/*  838 */     reject();
/*  839 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(byte[] src, int srcIndex, int length)
/*      */   {
/*  844 */     reject();
/*  845 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(byte[] src)
/*      */   {
/*  850 */     reject();
/*  851 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuffer src)
/*      */   {
/*  856 */     reject();
/*  857 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length)
/*      */   {
/*  862 */     reject();
/*  863 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src, int length)
/*      */   {
/*  868 */     reject();
/*  869 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src)
/*      */   {
/*  874 */     reject();
/*  875 */     return this;
/*      */   }
/*      */   
/*      */   public int writeBytes(InputStream in, int length)
/*      */   {
/*  880 */     reject();
/*  881 */     return 0;
/*      */   }
/*      */   
/*      */   public int writeBytes(ScatteringByteChannel in, int length)
/*      */   {
/*  886 */     reject();
/*  887 */     return 0;
/*      */   }
/*      */   
/*      */   public ByteBuf writeInt(int value)
/*      */   {
/*  892 */     reject();
/*  893 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeLong(long value)
/*      */   {
/*  898 */     reject();
/*  899 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeMedium(int value)
/*      */   {
/*  904 */     reject();
/*  905 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeZero(int length)
/*      */   {
/*  910 */     reject();
/*  911 */     return this;
/*      */   }
/*      */   
/*      */   public int writerIndex()
/*      */   {
/*  916 */     return this.buffer.writerIndex();
/*      */   }
/*      */   
/*      */   public ByteBuf writerIndex(int writerIndex)
/*      */   {
/*  921 */     reject();
/*  922 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeShort(int value)
/*      */   {
/*  927 */     reject();
/*  928 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeChar(int value)
/*      */   {
/*  933 */     reject();
/*  934 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeFloat(float value)
/*      */   {
/*  939 */     reject();
/*  940 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeDouble(double value)
/*      */   {
/*  945 */     reject();
/*  946 */     return this;
/*      */   }
/*      */   
/*      */   private void checkIndex(int index, int length) {
/*  950 */     if (index + length > this.buffer.writerIndex()) {
/*  951 */       throw REPLAY;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkReadableBytes(int readableBytes) {
/*  956 */     if (this.buffer.readableBytes() < readableBytes) {
/*  957 */       throw REPLAY;
/*      */     }
/*      */   }
/*      */   
/*      */   public ByteBuf discardSomeReadBytes()
/*      */   {
/*  963 */     reject();
/*  964 */     return this;
/*      */   }
/*      */   
/*      */   public int refCnt()
/*      */   {
/*  969 */     return this.buffer.refCnt();
/*      */   }
/*      */   
/*      */   public ByteBuf retain()
/*      */   {
/*  974 */     reject();
/*  975 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf retain(int increment)
/*      */   {
/*  980 */     reject();
/*  981 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf touch()
/*      */   {
/*  986 */     this.buffer.touch();
/*  987 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf touch(Object hint)
/*      */   {
/*  992 */     this.buffer.touch(hint);
/*  993 */     return this;
/*      */   }
/*      */   
/*      */   public boolean release()
/*      */   {
/*  998 */     reject();
/*  999 */     return false;
/*      */   }
/*      */   
/*      */   public boolean release(int decrement)
/*      */   {
/* 1004 */     reject();
/* 1005 */     return false;
/*      */   }
/*      */   
/*      */   public ByteBuf unwrap()
/*      */   {
/* 1010 */     reject();
/* 1011 */     return this;
/*      */   }
/*      */   
/*      */   private static void reject() {
/* 1015 */     throw new UnsupportedOperationException("not a replayable operation");
/*      */   }
/*      */   
/*      */   ReplayingDecoderBuffer() {}
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\ReplayingDecoderBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */