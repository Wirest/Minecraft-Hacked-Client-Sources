/*      */ package io.netty.buffer;
/*      */ 
/*      */ import io.netty.util.IllegalReferenceCountException;
/*      */ import io.netty.util.ResourceLeakDetector;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import java.io.IOException;
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
/*      */ 
/*      */ public abstract class AbstractByteBuf
/*      */   extends ByteBuf
/*      */ {
/*   38 */   static final ResourceLeakDetector<ByteBuf> leakDetector = new ResourceLeakDetector(ByteBuf.class);
/*      */   
/*      */   int readerIndex;
/*      */   
/*      */   int writerIndex;
/*      */   private int markedReaderIndex;
/*      */   private int markedWriterIndex;
/*      */   private int maxCapacity;
/*      */   private SwappedByteBuf swappedBuf;
/*      */   
/*      */   protected AbstractByteBuf(int maxCapacity)
/*      */   {
/*   50 */     if (maxCapacity < 0) {
/*   51 */       throw new IllegalArgumentException("maxCapacity: " + maxCapacity + " (expected: >= 0)");
/*      */     }
/*   53 */     this.maxCapacity = maxCapacity;
/*      */   }
/*      */   
/*      */   public int maxCapacity()
/*      */   {
/*   58 */     return this.maxCapacity;
/*      */   }
/*      */   
/*      */   protected final void maxCapacity(int maxCapacity) {
/*   62 */     this.maxCapacity = maxCapacity;
/*      */   }
/*      */   
/*      */   public int readerIndex()
/*      */   {
/*   67 */     return this.readerIndex;
/*      */   }
/*      */   
/*      */   public ByteBuf readerIndex(int readerIndex)
/*      */   {
/*   72 */     if ((readerIndex < 0) || (readerIndex > this.writerIndex)) {
/*   73 */       throw new IndexOutOfBoundsException(String.format("readerIndex: %d (expected: 0 <= readerIndex <= writerIndex(%d))", new Object[] { Integer.valueOf(readerIndex), Integer.valueOf(this.writerIndex) }));
/*      */     }
/*      */     
/*   76 */     this.readerIndex = readerIndex;
/*   77 */     return this;
/*      */   }
/*      */   
/*      */   public int writerIndex()
/*      */   {
/*   82 */     return this.writerIndex;
/*      */   }
/*      */   
/*      */   public ByteBuf writerIndex(int writerIndex)
/*      */   {
/*   87 */     if ((writerIndex < this.readerIndex) || (writerIndex > capacity())) {
/*   88 */       throw new IndexOutOfBoundsException(String.format("writerIndex: %d (expected: readerIndex(%d) <= writerIndex <= capacity(%d))", new Object[] { Integer.valueOf(writerIndex), Integer.valueOf(this.readerIndex), Integer.valueOf(capacity()) }));
/*      */     }
/*      */     
/*      */ 
/*   92 */     this.writerIndex = writerIndex;
/*   93 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setIndex(int readerIndex, int writerIndex)
/*      */   {
/*   98 */     if ((readerIndex < 0) || (readerIndex > writerIndex) || (writerIndex > capacity())) {
/*   99 */       throw new IndexOutOfBoundsException(String.format("readerIndex: %d, writerIndex: %d (expected: 0 <= readerIndex <= writerIndex <= capacity(%d))", new Object[] { Integer.valueOf(readerIndex), Integer.valueOf(writerIndex), Integer.valueOf(capacity()) }));
/*      */     }
/*      */     
/*      */ 
/*  103 */     this.readerIndex = readerIndex;
/*  104 */     this.writerIndex = writerIndex;
/*  105 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf clear()
/*      */   {
/*  110 */     this.readerIndex = (this.writerIndex = 0);
/*  111 */     return this;
/*      */   }
/*      */   
/*      */   public boolean isReadable()
/*      */   {
/*  116 */     return this.writerIndex > this.readerIndex;
/*      */   }
/*      */   
/*      */   public boolean isReadable(int numBytes)
/*      */   {
/*  121 */     return this.writerIndex - this.readerIndex >= numBytes;
/*      */   }
/*      */   
/*      */   public boolean isWritable()
/*      */   {
/*  126 */     return capacity() > this.writerIndex;
/*      */   }
/*      */   
/*      */   public boolean isWritable(int numBytes)
/*      */   {
/*  131 */     return capacity() - this.writerIndex >= numBytes;
/*      */   }
/*      */   
/*      */   public int readableBytes()
/*      */   {
/*  136 */     return this.writerIndex - this.readerIndex;
/*      */   }
/*      */   
/*      */   public int writableBytes()
/*      */   {
/*  141 */     return capacity() - this.writerIndex;
/*      */   }
/*      */   
/*      */   public int maxWritableBytes()
/*      */   {
/*  146 */     return maxCapacity() - this.writerIndex;
/*      */   }
/*      */   
/*      */   public ByteBuf markReaderIndex()
/*      */   {
/*  151 */     this.markedReaderIndex = this.readerIndex;
/*  152 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf resetReaderIndex()
/*      */   {
/*  157 */     readerIndex(this.markedReaderIndex);
/*  158 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf markWriterIndex()
/*      */   {
/*  163 */     this.markedWriterIndex = this.writerIndex;
/*  164 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf resetWriterIndex()
/*      */   {
/*  169 */     this.writerIndex = this.markedWriterIndex;
/*  170 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf discardReadBytes()
/*      */   {
/*  175 */     ensureAccessible();
/*  176 */     if (this.readerIndex == 0) {
/*  177 */       return this;
/*      */     }
/*      */     
/*  180 */     if (this.readerIndex != this.writerIndex) {
/*  181 */       setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
/*  182 */       this.writerIndex -= this.readerIndex;
/*  183 */       adjustMarkers(this.readerIndex);
/*  184 */       this.readerIndex = 0;
/*      */     } else {
/*  186 */       adjustMarkers(this.readerIndex);
/*  187 */       this.writerIndex = (this.readerIndex = 0);
/*      */     }
/*  189 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf discardSomeReadBytes()
/*      */   {
/*  194 */     ensureAccessible();
/*  195 */     if (this.readerIndex == 0) {
/*  196 */       return this;
/*      */     }
/*      */     
/*  199 */     if (this.readerIndex == this.writerIndex) {
/*  200 */       adjustMarkers(this.readerIndex);
/*  201 */       this.writerIndex = (this.readerIndex = 0);
/*  202 */       return this;
/*      */     }
/*      */     
/*  205 */     if (this.readerIndex >= capacity() >>> 1) {
/*  206 */       setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
/*  207 */       this.writerIndex -= this.readerIndex;
/*  208 */       adjustMarkers(this.readerIndex);
/*  209 */       this.readerIndex = 0;
/*      */     }
/*  211 */     return this;
/*      */   }
/*      */   
/*      */   protected final void adjustMarkers(int decrement) {
/*  215 */     int markedReaderIndex = this.markedReaderIndex;
/*  216 */     if (markedReaderIndex <= decrement) {
/*  217 */       this.markedReaderIndex = 0;
/*  218 */       int markedWriterIndex = this.markedWriterIndex;
/*  219 */       if (markedWriterIndex <= decrement) {
/*  220 */         this.markedWriterIndex = 0;
/*      */       } else {
/*  222 */         this.markedWriterIndex = (markedWriterIndex - decrement);
/*      */       }
/*      */     } else {
/*  225 */       this.markedReaderIndex = (markedReaderIndex - decrement);
/*  226 */       this.markedWriterIndex -= decrement;
/*      */     }
/*      */   }
/*      */   
/*      */   public ByteBuf ensureWritable(int minWritableBytes)
/*      */   {
/*  232 */     if (minWritableBytes < 0) {
/*  233 */       throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", new Object[] { Integer.valueOf(minWritableBytes) }));
/*      */     }
/*      */     
/*      */ 
/*  237 */     if (minWritableBytes <= writableBytes()) {
/*  238 */       return this;
/*      */     }
/*      */     
/*  241 */     if (minWritableBytes > this.maxCapacity - this.writerIndex) {
/*  242 */       throw new IndexOutOfBoundsException(String.format("writerIndex(%d) + minWritableBytes(%d) exceeds maxCapacity(%d): %s", new Object[] { Integer.valueOf(this.writerIndex), Integer.valueOf(minWritableBytes), Integer.valueOf(this.maxCapacity), this }));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  248 */     int newCapacity = alloc().calculateNewCapacity(this.writerIndex + minWritableBytes, this.maxCapacity);
/*      */     
/*      */ 
/*  251 */     capacity(newCapacity);
/*  252 */     return this;
/*      */   }
/*      */   
/*      */   public int ensureWritable(int minWritableBytes, boolean force)
/*      */   {
/*  257 */     if (minWritableBytes < 0) {
/*  258 */       throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", new Object[] { Integer.valueOf(minWritableBytes) }));
/*      */     }
/*      */     
/*      */ 
/*  262 */     if (minWritableBytes <= writableBytes()) {
/*  263 */       return 0;
/*      */     }
/*      */     
/*  266 */     if ((minWritableBytes > this.maxCapacity - this.writerIndex) && 
/*  267 */       (force)) {
/*  268 */       if (capacity() == maxCapacity()) {
/*  269 */         return 1;
/*      */       }
/*      */       
/*  272 */       capacity(maxCapacity());
/*  273 */       return 3;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  278 */     int newCapacity = alloc().calculateNewCapacity(this.writerIndex + minWritableBytes, this.maxCapacity);
/*      */     
/*      */ 
/*  281 */     capacity(newCapacity);
/*  282 */     return 2;
/*      */   }
/*      */   
/*      */   public ByteBuf order(ByteOrder endianness)
/*      */   {
/*  287 */     if (endianness == null) {
/*  288 */       throw new NullPointerException("endianness");
/*      */     }
/*  290 */     if (endianness == order()) {
/*  291 */       return this;
/*      */     }
/*      */     
/*  294 */     SwappedByteBuf swappedBuf = this.swappedBuf;
/*  295 */     if (swappedBuf == null) {
/*  296 */       this.swappedBuf = (swappedBuf = newSwappedByteBuf());
/*      */     }
/*  298 */     return swappedBuf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected SwappedByteBuf newSwappedByteBuf()
/*      */   {
/*  305 */     return new SwappedByteBuf(this);
/*      */   }
/*      */   
/*      */   public byte getByte(int index)
/*      */   {
/*  310 */     checkIndex(index);
/*  311 */     return _getByte(index);
/*      */   }
/*      */   
/*      */   protected abstract byte _getByte(int paramInt);
/*      */   
/*      */   public boolean getBoolean(int index)
/*      */   {
/*  318 */     return getByte(index) != 0;
/*      */   }
/*      */   
/*      */   public short getUnsignedByte(int index)
/*      */   {
/*  323 */     return (short)(getByte(index) & 0xFF);
/*      */   }
/*      */   
/*      */   public short getShort(int index)
/*      */   {
/*  328 */     checkIndex(index, 2);
/*  329 */     return _getShort(index);
/*      */   }
/*      */   
/*      */   protected abstract short _getShort(int paramInt);
/*      */   
/*      */   public int getUnsignedShort(int index)
/*      */   {
/*  336 */     return getShort(index) & 0xFFFF;
/*      */   }
/*      */   
/*      */   public int getUnsignedMedium(int index)
/*      */   {
/*  341 */     checkIndex(index, 3);
/*  342 */     return _getUnsignedMedium(index);
/*      */   }
/*      */   
/*      */   protected abstract int _getUnsignedMedium(int paramInt);
/*      */   
/*      */   public int getMedium(int index)
/*      */   {
/*  349 */     int value = getUnsignedMedium(index);
/*  350 */     if ((value & 0x800000) != 0) {
/*  351 */       value |= 0xFF000000;
/*      */     }
/*  353 */     return value;
/*      */   }
/*      */   
/*      */   public int getInt(int index)
/*      */   {
/*  358 */     checkIndex(index, 4);
/*  359 */     return _getInt(index);
/*      */   }
/*      */   
/*      */   protected abstract int _getInt(int paramInt);
/*      */   
/*      */   public long getUnsignedInt(int index)
/*      */   {
/*  366 */     return getInt(index) & 0xFFFFFFFF;
/*      */   }
/*      */   
/*      */   public long getLong(int index)
/*      */   {
/*  371 */     checkIndex(index, 8);
/*  372 */     return _getLong(index);
/*      */   }
/*      */   
/*      */   protected abstract long _getLong(int paramInt);
/*      */   
/*      */   public char getChar(int index)
/*      */   {
/*  379 */     return (char)getShort(index);
/*      */   }
/*      */   
/*      */   public float getFloat(int index)
/*      */   {
/*  384 */     return Float.intBitsToFloat(getInt(index));
/*      */   }
/*      */   
/*      */   public double getDouble(int index)
/*      */   {
/*  389 */     return Double.longBitsToDouble(getLong(index));
/*      */   }
/*      */   
/*      */   public ByteBuf getBytes(int index, byte[] dst)
/*      */   {
/*  394 */     getBytes(index, dst, 0, dst.length);
/*  395 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst)
/*      */   {
/*  400 */     getBytes(index, dst, dst.writableBytes());
/*  401 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst, int length)
/*      */   {
/*  406 */     getBytes(index, dst, dst.writerIndex(), length);
/*  407 */     dst.writerIndex(dst.writerIndex() + length);
/*  408 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setByte(int index, int value)
/*      */   {
/*  413 */     checkIndex(index);
/*  414 */     _setByte(index, value);
/*  415 */     return this;
/*      */   }
/*      */   
/*      */   protected abstract void _setByte(int paramInt1, int paramInt2);
/*      */   
/*      */   public ByteBuf setBoolean(int index, boolean value)
/*      */   {
/*  422 */     setByte(index, value ? 1 : 0);
/*  423 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setShort(int index, int value)
/*      */   {
/*  428 */     checkIndex(index, 2);
/*  429 */     _setShort(index, value);
/*  430 */     return this;
/*      */   }
/*      */   
/*      */   protected abstract void _setShort(int paramInt1, int paramInt2);
/*      */   
/*      */   public ByteBuf setChar(int index, int value)
/*      */   {
/*  437 */     setShort(index, value);
/*  438 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setMedium(int index, int value)
/*      */   {
/*  443 */     checkIndex(index, 3);
/*  444 */     _setMedium(index, value);
/*  445 */     return this;
/*      */   }
/*      */   
/*      */   protected abstract void _setMedium(int paramInt1, int paramInt2);
/*      */   
/*      */   public ByteBuf setInt(int index, int value)
/*      */   {
/*  452 */     checkIndex(index, 4);
/*  453 */     _setInt(index, value);
/*  454 */     return this;
/*      */   }
/*      */   
/*      */   protected abstract void _setInt(int paramInt1, int paramInt2);
/*      */   
/*      */   public ByteBuf setFloat(int index, float value)
/*      */   {
/*  461 */     setInt(index, Float.floatToRawIntBits(value));
/*  462 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setLong(int index, long value)
/*      */   {
/*  467 */     checkIndex(index, 8);
/*  468 */     _setLong(index, value);
/*  469 */     return this;
/*      */   }
/*      */   
/*      */   protected abstract void _setLong(int paramInt, long paramLong);
/*      */   
/*      */   public ByteBuf setDouble(int index, double value)
/*      */   {
/*  476 */     setLong(index, Double.doubleToRawLongBits(value));
/*  477 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setBytes(int index, byte[] src)
/*      */   {
/*  482 */     setBytes(index, src, 0, src.length);
/*  483 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src)
/*      */   {
/*  488 */     setBytes(index, src, src.readableBytes());
/*  489 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src, int length)
/*      */   {
/*  494 */     checkIndex(index, length);
/*  495 */     if (src == null) {
/*  496 */       throw new NullPointerException("src");
/*      */     }
/*  498 */     if (length > src.readableBytes()) {
/*  499 */       throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", new Object[] { Integer.valueOf(length), Integer.valueOf(src.readableBytes()), src }));
/*      */     }
/*      */     
/*      */ 
/*  503 */     setBytes(index, src, src.readerIndex(), length);
/*  504 */     src.readerIndex(src.readerIndex() + length);
/*  505 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setZero(int index, int length)
/*      */   {
/*  510 */     if (length == 0) {
/*  511 */       return this;
/*      */     }
/*      */     
/*  514 */     checkIndex(index, length);
/*      */     
/*  516 */     int nLong = length >>> 3;
/*  517 */     int nBytes = length & 0x7;
/*  518 */     for (int i = nLong; i > 0; i--) {
/*  519 */       setLong(index, 0L);
/*  520 */       index += 8;
/*      */     }
/*  522 */     if (nBytes == 4) {
/*  523 */       setInt(index, 0);
/*  524 */     } else if (nBytes < 4) {
/*  525 */       for (int i = nBytes; i > 0; i--) {
/*  526 */         setByte(index, 0);
/*  527 */         index++;
/*      */       }
/*      */     } else {
/*  530 */       setInt(index, 0);
/*  531 */       index += 4;
/*  532 */       for (int i = nBytes - 4; i > 0; i--) {
/*  533 */         setByte(index, 0);
/*  534 */         index++;
/*      */       }
/*      */     }
/*  537 */     return this;
/*      */   }
/*      */   
/*      */   public byte readByte()
/*      */   {
/*  542 */     checkReadableBytes(1);
/*  543 */     int i = this.readerIndex;
/*  544 */     byte b = getByte(i);
/*  545 */     this.readerIndex = (i + 1);
/*  546 */     return b;
/*      */   }
/*      */   
/*      */   public boolean readBoolean()
/*      */   {
/*  551 */     return readByte() != 0;
/*      */   }
/*      */   
/*      */   public short readUnsignedByte()
/*      */   {
/*  556 */     return (short)(readByte() & 0xFF);
/*      */   }
/*      */   
/*      */   public short readShort()
/*      */   {
/*  561 */     checkReadableBytes(2);
/*  562 */     short v = _getShort(this.readerIndex);
/*  563 */     this.readerIndex += 2;
/*  564 */     return v;
/*      */   }
/*      */   
/*      */   public int readUnsignedShort()
/*      */   {
/*  569 */     return readShort() & 0xFFFF;
/*      */   }
/*      */   
/*      */   public int readMedium()
/*      */   {
/*  574 */     int value = readUnsignedMedium();
/*  575 */     if ((value & 0x800000) != 0) {
/*  576 */       value |= 0xFF000000;
/*      */     }
/*  578 */     return value;
/*      */   }
/*      */   
/*      */   public int readUnsignedMedium()
/*      */   {
/*  583 */     checkReadableBytes(3);
/*  584 */     int v = _getUnsignedMedium(this.readerIndex);
/*  585 */     this.readerIndex += 3;
/*  586 */     return v;
/*      */   }
/*      */   
/*      */   public int readInt()
/*      */   {
/*  591 */     checkReadableBytes(4);
/*  592 */     int v = _getInt(this.readerIndex);
/*  593 */     this.readerIndex += 4;
/*  594 */     return v;
/*      */   }
/*      */   
/*      */   public long readUnsignedInt()
/*      */   {
/*  599 */     return readInt() & 0xFFFFFFFF;
/*      */   }
/*      */   
/*      */   public long readLong()
/*      */   {
/*  604 */     checkReadableBytes(8);
/*  605 */     long v = _getLong(this.readerIndex);
/*  606 */     this.readerIndex += 8;
/*  607 */     return v;
/*      */   }
/*      */   
/*      */   public char readChar()
/*      */   {
/*  612 */     return (char)readShort();
/*      */   }
/*      */   
/*      */   public float readFloat()
/*      */   {
/*  617 */     return Float.intBitsToFloat(readInt());
/*      */   }
/*      */   
/*      */   public double readDouble()
/*      */   {
/*  622 */     return Double.longBitsToDouble(readLong());
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(int length)
/*      */   {
/*  627 */     checkReadableBytes(length);
/*  628 */     if (length == 0) {
/*  629 */       return Unpooled.EMPTY_BUFFER;
/*      */     }
/*      */     
/*      */ 
/*  633 */     ByteBuf buf = Unpooled.buffer(length, this.maxCapacity);
/*  634 */     buf.writeBytes(this, this.readerIndex, length);
/*  635 */     this.readerIndex += length;
/*  636 */     return buf;
/*      */   }
/*      */   
/*      */   public ByteBuf readSlice(int length)
/*      */   {
/*  641 */     ByteBuf slice = slice(this.readerIndex, length);
/*  642 */     this.readerIndex += length;
/*  643 */     return slice;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length)
/*      */   {
/*  648 */     checkReadableBytes(length);
/*  649 */     getBytes(this.readerIndex, dst, dstIndex, length);
/*  650 */     this.readerIndex += length;
/*  651 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(byte[] dst)
/*      */   {
/*  656 */     readBytes(dst, 0, dst.length);
/*  657 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst)
/*      */   {
/*  662 */     readBytes(dst, dst.writableBytes());
/*  663 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst, int length)
/*      */   {
/*  668 */     if (length > dst.writableBytes()) {
/*  669 */       throw new IndexOutOfBoundsException(String.format("length(%d) exceeds dst.writableBytes(%d) where dst is: %s", new Object[] { Integer.valueOf(length), Integer.valueOf(dst.writableBytes()), dst }));
/*      */     }
/*      */     
/*  672 */     readBytes(dst, dst.writerIndex(), length);
/*  673 */     dst.writerIndex(dst.writerIndex() + length);
/*  674 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length)
/*      */   {
/*  679 */     checkReadableBytes(length);
/*  680 */     getBytes(this.readerIndex, dst, dstIndex, length);
/*  681 */     this.readerIndex += length;
/*  682 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(ByteBuffer dst)
/*      */   {
/*  687 */     int length = dst.remaining();
/*  688 */     checkReadableBytes(length);
/*  689 */     getBytes(this.readerIndex, dst);
/*  690 */     this.readerIndex += length;
/*  691 */     return this;
/*      */   }
/*      */   
/*      */   public int readBytes(GatheringByteChannel out, int length)
/*      */     throws IOException
/*      */   {
/*  697 */     checkReadableBytes(length);
/*  698 */     int readBytes = getBytes(this.readerIndex, out, length);
/*  699 */     this.readerIndex += readBytes;
/*  700 */     return readBytes;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(OutputStream out, int length) throws IOException
/*      */   {
/*  705 */     checkReadableBytes(length);
/*  706 */     getBytes(this.readerIndex, out, length);
/*  707 */     this.readerIndex += length;
/*  708 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf skipBytes(int length)
/*      */   {
/*  713 */     checkReadableBytes(length);
/*  714 */     this.readerIndex += length;
/*  715 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBoolean(boolean value)
/*      */   {
/*  720 */     writeByte(value ? 1 : 0);
/*  721 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeByte(int value)
/*      */   {
/*  726 */     ensureAccessible();
/*  727 */     ensureWritable(1);
/*  728 */     _setByte(this.writerIndex++, value);
/*  729 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeShort(int value)
/*      */   {
/*  734 */     ensureAccessible();
/*  735 */     ensureWritable(2);
/*  736 */     _setShort(this.writerIndex, value);
/*  737 */     this.writerIndex += 2;
/*  738 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeMedium(int value)
/*      */   {
/*  743 */     ensureAccessible();
/*  744 */     ensureWritable(3);
/*  745 */     _setMedium(this.writerIndex, value);
/*  746 */     this.writerIndex += 3;
/*  747 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeInt(int value)
/*      */   {
/*  752 */     ensureAccessible();
/*  753 */     ensureWritable(4);
/*  754 */     _setInt(this.writerIndex, value);
/*  755 */     this.writerIndex += 4;
/*  756 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeLong(long value)
/*      */   {
/*  761 */     ensureAccessible();
/*  762 */     ensureWritable(8);
/*  763 */     _setLong(this.writerIndex, value);
/*  764 */     this.writerIndex += 8;
/*  765 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeChar(int value)
/*      */   {
/*  770 */     writeShort(value);
/*  771 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeFloat(float value)
/*      */   {
/*  776 */     writeInt(Float.floatToRawIntBits(value));
/*  777 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeDouble(double value)
/*      */   {
/*  782 */     writeLong(Double.doubleToRawLongBits(value));
/*  783 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(byte[] src, int srcIndex, int length)
/*      */   {
/*  788 */     ensureAccessible();
/*  789 */     ensureWritable(length);
/*  790 */     setBytes(this.writerIndex, src, srcIndex, length);
/*  791 */     this.writerIndex += length;
/*  792 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(byte[] src)
/*      */   {
/*  797 */     writeBytes(src, 0, src.length);
/*  798 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src)
/*      */   {
/*  803 */     writeBytes(src, src.readableBytes());
/*  804 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src, int length)
/*      */   {
/*  809 */     if (length > src.readableBytes()) {
/*  810 */       throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", new Object[] { Integer.valueOf(length), Integer.valueOf(src.readableBytes()), src }));
/*      */     }
/*      */     
/*  813 */     writeBytes(src, src.readerIndex(), length);
/*  814 */     src.readerIndex(src.readerIndex() + length);
/*  815 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length)
/*      */   {
/*  820 */     ensureAccessible();
/*  821 */     ensureWritable(length);
/*  822 */     setBytes(this.writerIndex, src, srcIndex, length);
/*  823 */     this.writerIndex += length;
/*  824 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuffer src)
/*      */   {
/*  829 */     ensureAccessible();
/*  830 */     int length = src.remaining();
/*  831 */     ensureWritable(length);
/*  832 */     setBytes(this.writerIndex, src);
/*  833 */     this.writerIndex += length;
/*  834 */     return this;
/*      */   }
/*      */   
/*      */   public int writeBytes(InputStream in, int length)
/*      */     throws IOException
/*      */   {
/*  840 */     ensureAccessible();
/*  841 */     ensureWritable(length);
/*  842 */     int writtenBytes = setBytes(this.writerIndex, in, length);
/*  843 */     if (writtenBytes > 0) {
/*  844 */       this.writerIndex += writtenBytes;
/*      */     }
/*  846 */     return writtenBytes;
/*      */   }
/*      */   
/*      */   public int writeBytes(ScatteringByteChannel in, int length) throws IOException
/*      */   {
/*  851 */     ensureAccessible();
/*  852 */     ensureWritable(length);
/*  853 */     int writtenBytes = setBytes(this.writerIndex, in, length);
/*  854 */     if (writtenBytes > 0) {
/*  855 */       this.writerIndex += writtenBytes;
/*      */     }
/*  857 */     return writtenBytes;
/*      */   }
/*      */   
/*      */   public ByteBuf writeZero(int length)
/*      */   {
/*  862 */     if (length == 0) {
/*  863 */       return this;
/*      */     }
/*      */     
/*  866 */     ensureWritable(length);
/*  867 */     checkIndex(this.writerIndex, length);
/*      */     
/*  869 */     int nLong = length >>> 3;
/*  870 */     int nBytes = length & 0x7;
/*  871 */     for (int i = nLong; i > 0; i--) {
/*  872 */       writeLong(0L);
/*      */     }
/*  874 */     if (nBytes == 4) {
/*  875 */       writeInt(0);
/*  876 */     } else if (nBytes < 4) {
/*  877 */       for (int i = nBytes; i > 0; i--) {
/*  878 */         writeByte(0);
/*      */       }
/*      */     } else {
/*  881 */       writeInt(0);
/*  882 */       for (int i = nBytes - 4; i > 0; i--) {
/*  883 */         writeByte(0);
/*      */       }
/*      */     }
/*  886 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf copy()
/*      */   {
/*  891 */     return copy(this.readerIndex, readableBytes());
/*      */   }
/*      */   
/*      */   public ByteBuf duplicate()
/*      */   {
/*  896 */     return new DuplicatedByteBuf(this);
/*      */   }
/*      */   
/*      */   public ByteBuf slice()
/*      */   {
/*  901 */     return slice(this.readerIndex, readableBytes());
/*      */   }
/*      */   
/*      */   public ByteBuf slice(int index, int length)
/*      */   {
/*  906 */     return new SlicedByteBuf(this, index, length);
/*      */   }
/*      */   
/*      */   public ByteBuffer nioBuffer()
/*      */   {
/*  911 */     return nioBuffer(this.readerIndex, readableBytes());
/*      */   }
/*      */   
/*      */   public ByteBuffer[] nioBuffers()
/*      */   {
/*  916 */     return nioBuffers(this.readerIndex, readableBytes());
/*      */   }
/*      */   
/*      */   public String toString(Charset charset)
/*      */   {
/*  921 */     return toString(this.readerIndex, readableBytes(), charset);
/*      */   }
/*      */   
/*      */   public String toString(int index, int length, Charset charset)
/*      */   {
/*  926 */     if (length == 0) {
/*  927 */       return "";
/*      */     }
/*      */     ByteBuffer nioBuffer;
/*      */     ByteBuffer nioBuffer;
/*  931 */     if (nioBufferCount() == 1) {
/*  932 */       nioBuffer = nioBuffer(index, length);
/*      */     } else {
/*  934 */       nioBuffer = ByteBuffer.allocate(length);
/*  935 */       getBytes(index, nioBuffer);
/*  936 */       nioBuffer.flip();
/*      */     }
/*      */     
/*  939 */     return ByteBufUtil.decodeString(nioBuffer, charset);
/*      */   }
/*      */   
/*      */   public int indexOf(int fromIndex, int toIndex, byte value)
/*      */   {
/*  944 */     return ByteBufUtil.indexOf(this, fromIndex, toIndex, value);
/*      */   }
/*      */   
/*      */   public int bytesBefore(byte value)
/*      */   {
/*  949 */     return bytesBefore(readerIndex(), readableBytes(), value);
/*      */   }
/*      */   
/*      */   public int bytesBefore(int length, byte value)
/*      */   {
/*  954 */     checkReadableBytes(length);
/*  955 */     return bytesBefore(readerIndex(), length, value);
/*      */   }
/*      */   
/*      */   public int bytesBefore(int index, int length, byte value)
/*      */   {
/*  960 */     int endIndex = indexOf(index, index + length, value);
/*  961 */     if (endIndex < 0) {
/*  962 */       return -1;
/*      */     }
/*  964 */     return endIndex - index;
/*      */   }
/*      */   
/*      */   public int forEachByte(ByteBufProcessor processor)
/*      */   {
/*  969 */     int index = this.readerIndex;
/*  970 */     int length = this.writerIndex - index;
/*  971 */     ensureAccessible();
/*  972 */     return forEachByteAsc0(index, length, processor);
/*      */   }
/*      */   
/*      */   public int forEachByte(int index, int length, ByteBufProcessor processor)
/*      */   {
/*  977 */     checkIndex(index, length);
/*  978 */     return forEachByteAsc0(index, length, processor);
/*      */   }
/*      */   
/*      */   private int forEachByteAsc0(int index, int length, ByteBufProcessor processor) {
/*  982 */     if (processor == null) {
/*  983 */       throw new NullPointerException("processor");
/*      */     }
/*      */     
/*  986 */     if (length == 0) {
/*  987 */       return -1;
/*      */     }
/*      */     
/*  990 */     int endIndex = index + length;
/*  991 */     int i = index;
/*      */     try {
/*      */       do {
/*  994 */         if (processor.process(_getByte(i))) {
/*  995 */           i++;
/*      */         } else {
/*  997 */           return i;
/*      */         }
/*  999 */       } while (i < endIndex);
/*      */     } catch (Exception e) {
/* 1001 */       PlatformDependent.throwException(e);
/*      */     }
/*      */     
/* 1004 */     return -1;
/*      */   }
/*      */   
/*      */   public int forEachByteDesc(ByteBufProcessor processor)
/*      */   {
/* 1009 */     int index = this.readerIndex;
/* 1010 */     int length = this.writerIndex - index;
/* 1011 */     ensureAccessible();
/* 1012 */     return forEachByteDesc0(index, length, processor);
/*      */   }
/*      */   
/*      */   public int forEachByteDesc(int index, int length, ByteBufProcessor processor)
/*      */   {
/* 1017 */     checkIndex(index, length);
/*      */     
/* 1019 */     return forEachByteDesc0(index, length, processor);
/*      */   }
/*      */   
/*      */   private int forEachByteDesc0(int index, int length, ByteBufProcessor processor)
/*      */   {
/* 1024 */     if (processor == null) {
/* 1025 */       throw new NullPointerException("processor");
/*      */     }
/*      */     
/* 1028 */     if (length == 0) {
/* 1029 */       return -1;
/*      */     }
/*      */     
/* 1032 */     int i = index + length - 1;
/*      */     try {
/*      */       do {
/* 1035 */         if (processor.process(_getByte(i))) {
/* 1036 */           i--;
/*      */         } else {
/* 1038 */           return i;
/*      */         }
/* 1040 */       } while (i >= index);
/*      */     } catch (Exception e) {
/* 1042 */       PlatformDependent.throwException(e);
/*      */     }
/*      */     
/* 1045 */     return -1;
/*      */   }
/*      */   
/*      */   public int hashCode()
/*      */   {
/* 1050 */     return ByteBufUtil.hashCode(this);
/*      */   }
/*      */   
/*      */   public boolean equals(Object o)
/*      */   {
/* 1055 */     if (this == o) {
/* 1056 */       return true;
/*      */     }
/* 1058 */     if ((o instanceof ByteBuf)) {
/* 1059 */       return ByteBufUtil.equals(this, (ByteBuf)o);
/*      */     }
/* 1061 */     return false;
/*      */   }
/*      */   
/*      */   public int compareTo(ByteBuf that)
/*      */   {
/* 1066 */     return ByteBufUtil.compare(this, that);
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 1071 */     if (refCnt() == 0) {
/* 1072 */       return StringUtil.simpleClassName(this) + "(freed)";
/*      */     }
/*      */     
/* 1075 */     StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append("(ridx: ").append(this.readerIndex).append(", widx: ").append(this.writerIndex).append(", cap: ").append(capacity());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1080 */     if (this.maxCapacity != Integer.MAX_VALUE) {
/* 1081 */       buf.append('/').append(this.maxCapacity);
/*      */     }
/*      */     
/* 1084 */     ByteBuf unwrapped = unwrap();
/* 1085 */     if (unwrapped != null) {
/* 1086 */       buf.append(", unwrapped: ").append(unwrapped);
/*      */     }
/* 1088 */     buf.append(')');
/* 1089 */     return buf.toString();
/*      */   }
/*      */   
/*      */   protected final void checkIndex(int index) {
/* 1093 */     ensureAccessible();
/* 1094 */     if ((index < 0) || (index >= capacity())) {
/* 1095 */       throw new IndexOutOfBoundsException(String.format("index: %d (expected: range(0, %d))", new Object[] { Integer.valueOf(index), Integer.valueOf(capacity()) }));
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void checkIndex(int index, int fieldLength)
/*      */   {
/* 1101 */     ensureAccessible();
/* 1102 */     if (fieldLength < 0) {
/* 1103 */       throw new IllegalArgumentException("length: " + fieldLength + " (expected: >= 0)");
/*      */     }
/* 1105 */     if ((index < 0) || (index > capacity() - fieldLength)) {
/* 1106 */       throw new IndexOutOfBoundsException(String.format("index: %d, length: %d (expected: range(0, %d))", new Object[] { Integer.valueOf(index), Integer.valueOf(fieldLength), Integer.valueOf(capacity()) }));
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void checkSrcIndex(int index, int length, int srcIndex, int srcCapacity)
/*      */   {
/* 1112 */     checkIndex(index, length);
/* 1113 */     if ((srcIndex < 0) || (srcIndex > srcCapacity - length)) {
/* 1114 */       throw new IndexOutOfBoundsException(String.format("srcIndex: %d, length: %d (expected: range(0, %d))", new Object[] { Integer.valueOf(srcIndex), Integer.valueOf(length), Integer.valueOf(srcCapacity) }));
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void checkDstIndex(int index, int length, int dstIndex, int dstCapacity)
/*      */   {
/* 1120 */     checkIndex(index, length);
/* 1121 */     if ((dstIndex < 0) || (dstIndex > dstCapacity - length)) {
/* 1122 */       throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", new Object[] { Integer.valueOf(dstIndex), Integer.valueOf(length), Integer.valueOf(dstCapacity) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void checkReadableBytes(int minimumReadableBytes)
/*      */   {
/* 1133 */     ensureAccessible();
/* 1134 */     if (minimumReadableBytes < 0) {
/* 1135 */       throw new IllegalArgumentException("minimumReadableBytes: " + minimumReadableBytes + " (expected: >= 0)");
/*      */     }
/* 1137 */     if (this.readerIndex > this.writerIndex - minimumReadableBytes) {
/* 1138 */       throw new IndexOutOfBoundsException(String.format("readerIndex(%d) + length(%d) exceeds writerIndex(%d): %s", new Object[] { Integer.valueOf(this.readerIndex), Integer.valueOf(minimumReadableBytes), Integer.valueOf(this.writerIndex), this }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void ensureAccessible()
/*      */   {
/* 1149 */     if (refCnt() == 0) {
/* 1150 */       throw new IllegalReferenceCountException(0);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\AbstractByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */