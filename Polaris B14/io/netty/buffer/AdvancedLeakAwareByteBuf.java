/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.ResourceLeak;
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
/*     */ final class AdvancedLeakAwareByteBuf
/*     */   extends WrappedByteBuf
/*     */ {
/*     */   private final ResourceLeak leak;
/*     */   
/*     */   AdvancedLeakAwareByteBuf(ByteBuf buf, ResourceLeak leak)
/*     */   {
/*  35 */     super(buf);
/*  36 */     this.leak = leak;
/*     */   }
/*     */   
/*     */   public ByteBuf order(ByteOrder endianness)
/*     */   {
/*  41 */     this.leak.record();
/*  42 */     if (order() == endianness) {
/*  43 */       return this;
/*     */     }
/*  45 */     return new AdvancedLeakAwareByteBuf(super.order(endianness), this.leak);
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuf slice()
/*     */   {
/*  51 */     this.leak.record();
/*  52 */     return new AdvancedLeakAwareByteBuf(super.slice(), this.leak);
/*     */   }
/*     */   
/*     */   public ByteBuf slice(int index, int length)
/*     */   {
/*  57 */     this.leak.record();
/*  58 */     return new AdvancedLeakAwareByteBuf(super.slice(index, length), this.leak);
/*     */   }
/*     */   
/*     */   public ByteBuf duplicate()
/*     */   {
/*  63 */     this.leak.record();
/*  64 */     return new AdvancedLeakAwareByteBuf(super.duplicate(), this.leak);
/*     */   }
/*     */   
/*     */   public ByteBuf readSlice(int length)
/*     */   {
/*  69 */     this.leak.record();
/*  70 */     return new AdvancedLeakAwareByteBuf(super.readSlice(length), this.leak);
/*     */   }
/*     */   
/*     */   public ByteBuf discardReadBytes()
/*     */   {
/*  75 */     this.leak.record();
/*  76 */     return super.discardReadBytes();
/*     */   }
/*     */   
/*     */   public ByteBuf discardSomeReadBytes()
/*     */   {
/*  81 */     this.leak.record();
/*  82 */     return super.discardSomeReadBytes();
/*     */   }
/*     */   
/*     */   public ByteBuf ensureWritable(int minWritableBytes)
/*     */   {
/*  87 */     this.leak.record();
/*  88 */     return super.ensureWritable(minWritableBytes);
/*     */   }
/*     */   
/*     */   public int ensureWritable(int minWritableBytes, boolean force)
/*     */   {
/*  93 */     this.leak.record();
/*  94 */     return super.ensureWritable(minWritableBytes, force);
/*     */   }
/*     */   
/*     */   public boolean getBoolean(int index)
/*     */   {
/*  99 */     this.leak.record();
/* 100 */     return super.getBoolean(index);
/*     */   }
/*     */   
/*     */   public byte getByte(int index)
/*     */   {
/* 105 */     this.leak.record();
/* 106 */     return super.getByte(index);
/*     */   }
/*     */   
/*     */   public short getUnsignedByte(int index)
/*     */   {
/* 111 */     this.leak.record();
/* 112 */     return super.getUnsignedByte(index);
/*     */   }
/*     */   
/*     */   public short getShort(int index)
/*     */   {
/* 117 */     this.leak.record();
/* 118 */     return super.getShort(index);
/*     */   }
/*     */   
/*     */   public int getUnsignedShort(int index)
/*     */   {
/* 123 */     this.leak.record();
/* 124 */     return super.getUnsignedShort(index);
/*     */   }
/*     */   
/*     */   public int getMedium(int index)
/*     */   {
/* 129 */     this.leak.record();
/* 130 */     return super.getMedium(index);
/*     */   }
/*     */   
/*     */   public int getUnsignedMedium(int index)
/*     */   {
/* 135 */     this.leak.record();
/* 136 */     return super.getUnsignedMedium(index);
/*     */   }
/*     */   
/*     */   public int getInt(int index)
/*     */   {
/* 141 */     this.leak.record();
/* 142 */     return super.getInt(index);
/*     */   }
/*     */   
/*     */   public long getUnsignedInt(int index)
/*     */   {
/* 147 */     this.leak.record();
/* 148 */     return super.getUnsignedInt(index);
/*     */   }
/*     */   
/*     */   public long getLong(int index)
/*     */   {
/* 153 */     this.leak.record();
/* 154 */     return super.getLong(index);
/*     */   }
/*     */   
/*     */   public char getChar(int index)
/*     */   {
/* 159 */     this.leak.record();
/* 160 */     return super.getChar(index);
/*     */   }
/*     */   
/*     */   public float getFloat(int index)
/*     */   {
/* 165 */     this.leak.record();
/* 166 */     return super.getFloat(index);
/*     */   }
/*     */   
/*     */   public double getDouble(int index)
/*     */   {
/* 171 */     this.leak.record();
/* 172 */     return super.getDouble(index);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst)
/*     */   {
/* 177 */     this.leak.record();
/* 178 */     return super.getBytes(index, dst);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int length)
/*     */   {
/* 183 */     this.leak.record();
/* 184 */     return super.getBytes(index, dst, length);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 189 */     this.leak.record();
/* 190 */     return super.getBytes(index, dst, dstIndex, length);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst)
/*     */   {
/* 195 */     this.leak.record();
/* 196 */     return super.getBytes(index, dst);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 201 */     this.leak.record();
/* 202 */     return super.getBytes(index, dst, dstIndex, length);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 207 */     this.leak.record();
/* 208 */     return super.getBytes(index, dst);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException
/*     */   {
/* 213 */     this.leak.record();
/* 214 */     return super.getBytes(index, out, length);
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 219 */     this.leak.record();
/* 220 */     return super.getBytes(index, out, length);
/*     */   }
/*     */   
/*     */   public ByteBuf setBoolean(int index, boolean value)
/*     */   {
/* 225 */     this.leak.record();
/* 226 */     return super.setBoolean(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setByte(int index, int value)
/*     */   {
/* 231 */     this.leak.record();
/* 232 */     return super.setByte(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setShort(int index, int value)
/*     */   {
/* 237 */     this.leak.record();
/* 238 */     return super.setShort(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setMedium(int index, int value)
/*     */   {
/* 243 */     this.leak.record();
/* 244 */     return super.setMedium(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setInt(int index, int value)
/*     */   {
/* 249 */     this.leak.record();
/* 250 */     return super.setInt(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setLong(int index, long value)
/*     */   {
/* 255 */     this.leak.record();
/* 256 */     return super.setLong(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setChar(int index, int value)
/*     */   {
/* 261 */     this.leak.record();
/* 262 */     return super.setChar(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setFloat(int index, float value)
/*     */   {
/* 267 */     this.leak.record();
/* 268 */     return super.setFloat(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setDouble(int index, double value)
/*     */   {
/* 273 */     this.leak.record();
/* 274 */     return super.setDouble(index, value);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src)
/*     */   {
/* 279 */     this.leak.record();
/* 280 */     return super.setBytes(index, src);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int length)
/*     */   {
/* 285 */     this.leak.record();
/* 286 */     return super.setBytes(index, src, length);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/* 291 */     this.leak.record();
/* 292 */     return super.setBytes(index, src, srcIndex, length);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src)
/*     */   {
/* 297 */     this.leak.record();
/* 298 */     return super.setBytes(index, src);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 303 */     this.leak.record();
/* 304 */     return super.setBytes(index, src, srcIndex, length);
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 309 */     this.leak.record();
/* 310 */     return super.setBytes(index, src);
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length) throws IOException
/*     */   {
/* 315 */     this.leak.record();
/* 316 */     return super.setBytes(index, in, length);
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException
/*     */   {
/* 321 */     this.leak.record();
/* 322 */     return super.setBytes(index, in, length);
/*     */   }
/*     */   
/*     */   public ByteBuf setZero(int index, int length)
/*     */   {
/* 327 */     this.leak.record();
/* 328 */     return super.setZero(index, length);
/*     */   }
/*     */   
/*     */   public boolean readBoolean()
/*     */   {
/* 333 */     this.leak.record();
/* 334 */     return super.readBoolean();
/*     */   }
/*     */   
/*     */   public byte readByte()
/*     */   {
/* 339 */     this.leak.record();
/* 340 */     return super.readByte();
/*     */   }
/*     */   
/*     */   public short readUnsignedByte()
/*     */   {
/* 345 */     this.leak.record();
/* 346 */     return super.readUnsignedByte();
/*     */   }
/*     */   
/*     */   public short readShort()
/*     */   {
/* 351 */     this.leak.record();
/* 352 */     return super.readShort();
/*     */   }
/*     */   
/*     */   public int readUnsignedShort()
/*     */   {
/* 357 */     this.leak.record();
/* 358 */     return super.readUnsignedShort();
/*     */   }
/*     */   
/*     */   public int readMedium()
/*     */   {
/* 363 */     this.leak.record();
/* 364 */     return super.readMedium();
/*     */   }
/*     */   
/*     */   public int readUnsignedMedium()
/*     */   {
/* 369 */     this.leak.record();
/* 370 */     return super.readUnsignedMedium();
/*     */   }
/*     */   
/*     */   public int readInt()
/*     */   {
/* 375 */     this.leak.record();
/* 376 */     return super.readInt();
/*     */   }
/*     */   
/*     */   public long readUnsignedInt()
/*     */   {
/* 381 */     this.leak.record();
/* 382 */     return super.readUnsignedInt();
/*     */   }
/*     */   
/*     */   public long readLong()
/*     */   {
/* 387 */     this.leak.record();
/* 388 */     return super.readLong();
/*     */   }
/*     */   
/*     */   public char readChar()
/*     */   {
/* 393 */     this.leak.record();
/* 394 */     return super.readChar();
/*     */   }
/*     */   
/*     */   public float readFloat()
/*     */   {
/* 399 */     this.leak.record();
/* 400 */     return super.readFloat();
/*     */   }
/*     */   
/*     */   public double readDouble()
/*     */   {
/* 405 */     this.leak.record();
/* 406 */     return super.readDouble();
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(int length)
/*     */   {
/* 411 */     this.leak.record();
/* 412 */     return super.readBytes(length);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuf dst)
/*     */   {
/* 417 */     this.leak.record();
/* 418 */     return super.readBytes(dst);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuf dst, int length)
/*     */   {
/* 423 */     this.leak.record();
/* 424 */     return super.readBytes(dst, length);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 429 */     this.leak.record();
/* 430 */     return super.readBytes(dst, dstIndex, length);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(byte[] dst)
/*     */   {
/* 435 */     this.leak.record();
/* 436 */     return super.readBytes(dst);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length)
/*     */   {
/* 441 */     this.leak.record();
/* 442 */     return super.readBytes(dst, dstIndex, length);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(ByteBuffer dst)
/*     */   {
/* 447 */     this.leak.record();
/* 448 */     return super.readBytes(dst);
/*     */   }
/*     */   
/*     */   public ByteBuf readBytes(OutputStream out, int length) throws IOException
/*     */   {
/* 453 */     this.leak.record();
/* 454 */     return super.readBytes(out, length);
/*     */   }
/*     */   
/*     */   public int readBytes(GatheringByteChannel out, int length) throws IOException
/*     */   {
/* 459 */     this.leak.record();
/* 460 */     return super.readBytes(out, length);
/*     */   }
/*     */   
/*     */   public ByteBuf skipBytes(int length)
/*     */   {
/* 465 */     this.leak.record();
/* 466 */     return super.skipBytes(length);
/*     */   }
/*     */   
/*     */   public ByteBuf writeBoolean(boolean value)
/*     */   {
/* 471 */     this.leak.record();
/* 472 */     return super.writeBoolean(value);
/*     */   }
/*     */   
/*     */   public ByteBuf writeByte(int value)
/*     */   {
/* 477 */     this.leak.record();
/* 478 */     return super.writeByte(value);
/*     */   }
/*     */   
/*     */   public ByteBuf writeShort(int value)
/*     */   {
/* 483 */     this.leak.record();
/* 484 */     return super.writeShort(value);
/*     */   }
/*     */   
/*     */   public ByteBuf writeMedium(int value)
/*     */   {
/* 489 */     this.leak.record();
/* 490 */     return super.writeMedium(value);
/*     */   }
/*     */   
/*     */   public ByteBuf writeInt(int value)
/*     */   {
/* 495 */     this.leak.record();
/* 496 */     return super.writeInt(value);
/*     */   }
/*     */   
/*     */   public ByteBuf writeLong(long value)
/*     */   {
/* 501 */     this.leak.record();
/* 502 */     return super.writeLong(value);
/*     */   }
/*     */   
/*     */   public ByteBuf writeChar(int value)
/*     */   {
/* 507 */     this.leak.record();
/* 508 */     return super.writeChar(value);
/*     */   }
/*     */   
/*     */   public ByteBuf writeFloat(float value)
/*     */   {
/* 513 */     this.leak.record();
/* 514 */     return super.writeFloat(value);
/*     */   }
/*     */   
/*     */   public ByteBuf writeDouble(double value)
/*     */   {
/* 519 */     this.leak.record();
/* 520 */     return super.writeDouble(value);
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuf src)
/*     */   {
/* 525 */     this.leak.record();
/* 526 */     return super.writeBytes(src);
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuf src, int length)
/*     */   {
/* 531 */     this.leak.record();
/* 532 */     return super.writeBytes(src, length);
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length)
/*     */   {
/* 537 */     this.leak.record();
/* 538 */     return super.writeBytes(src, srcIndex, length);
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(byte[] src)
/*     */   {
/* 543 */     this.leak.record();
/* 544 */     return super.writeBytes(src);
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(byte[] src, int srcIndex, int length)
/*     */   {
/* 549 */     this.leak.record();
/* 550 */     return super.writeBytes(src, srcIndex, length);
/*     */   }
/*     */   
/*     */   public ByteBuf writeBytes(ByteBuffer src)
/*     */   {
/* 555 */     this.leak.record();
/* 556 */     return super.writeBytes(src);
/*     */   }
/*     */   
/*     */   public int writeBytes(InputStream in, int length) throws IOException
/*     */   {
/* 561 */     this.leak.record();
/* 562 */     return super.writeBytes(in, length);
/*     */   }
/*     */   
/*     */   public int writeBytes(ScatteringByteChannel in, int length) throws IOException
/*     */   {
/* 567 */     this.leak.record();
/* 568 */     return super.writeBytes(in, length);
/*     */   }
/*     */   
/*     */   public ByteBuf writeZero(int length)
/*     */   {
/* 573 */     this.leak.record();
/* 574 */     return super.writeZero(length);
/*     */   }
/*     */   
/*     */   public int indexOf(int fromIndex, int toIndex, byte value)
/*     */   {
/* 579 */     this.leak.record();
/* 580 */     return super.indexOf(fromIndex, toIndex, value);
/*     */   }
/*     */   
/*     */   public int bytesBefore(byte value)
/*     */   {
/* 585 */     this.leak.record();
/* 586 */     return super.bytesBefore(value);
/*     */   }
/*     */   
/*     */   public int bytesBefore(int length, byte value)
/*     */   {
/* 591 */     this.leak.record();
/* 592 */     return super.bytesBefore(length, value);
/*     */   }
/*     */   
/*     */   public int bytesBefore(int index, int length, byte value)
/*     */   {
/* 597 */     this.leak.record();
/* 598 */     return super.bytesBefore(index, length, value);
/*     */   }
/*     */   
/*     */   public int forEachByte(ByteBufProcessor processor)
/*     */   {
/* 603 */     this.leak.record();
/* 604 */     return super.forEachByte(processor);
/*     */   }
/*     */   
/*     */   public int forEachByte(int index, int length, ByteBufProcessor processor)
/*     */   {
/* 609 */     this.leak.record();
/* 610 */     return super.forEachByte(index, length, processor);
/*     */   }
/*     */   
/*     */   public int forEachByteDesc(ByteBufProcessor processor)
/*     */   {
/* 615 */     this.leak.record();
/* 616 */     return super.forEachByteDesc(processor);
/*     */   }
/*     */   
/*     */   public int forEachByteDesc(int index, int length, ByteBufProcessor processor)
/*     */   {
/* 621 */     this.leak.record();
/* 622 */     return super.forEachByteDesc(index, length, processor);
/*     */   }
/*     */   
/*     */   public ByteBuf copy()
/*     */   {
/* 627 */     this.leak.record();
/* 628 */     return super.copy();
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 633 */     this.leak.record();
/* 634 */     return super.copy(index, length);
/*     */   }
/*     */   
/*     */   public int nioBufferCount()
/*     */   {
/* 639 */     this.leak.record();
/* 640 */     return super.nioBufferCount();
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer()
/*     */   {
/* 645 */     this.leak.record();
/* 646 */     return super.nioBuffer();
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length)
/*     */   {
/* 651 */     this.leak.record();
/* 652 */     return super.nioBuffer(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers()
/*     */   {
/* 657 */     this.leak.record();
/* 658 */     return super.nioBuffers();
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 663 */     this.leak.record();
/* 664 */     return super.nioBuffers(index, length);
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 669 */     this.leak.record();
/* 670 */     return super.internalNioBuffer(index, length);
/*     */   }
/*     */   
/*     */   public String toString(Charset charset)
/*     */   {
/* 675 */     this.leak.record();
/* 676 */     return super.toString(charset);
/*     */   }
/*     */   
/*     */   public String toString(int index, int length, Charset charset)
/*     */   {
/* 681 */     this.leak.record();
/* 682 */     return super.toString(index, length, charset);
/*     */   }
/*     */   
/*     */   public ByteBuf capacity(int newCapacity)
/*     */   {
/* 687 */     this.leak.record();
/* 688 */     return super.capacity(newCapacity);
/*     */   }
/*     */   
/*     */   public ByteBuf retain()
/*     */   {
/* 693 */     this.leak.record();
/* 694 */     return super.retain();
/*     */   }
/*     */   
/*     */   public ByteBuf retain(int increment)
/*     */   {
/* 699 */     this.leak.record();
/* 700 */     return super.retain(increment);
/*     */   }
/*     */   
/*     */   public ByteBuf touch()
/*     */   {
/* 705 */     this.leak.record();
/* 706 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf touch(Object hint)
/*     */   {
/* 711 */     this.leak.record(hint);
/* 712 */     return this;
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/* 717 */     boolean deallocated = super.release();
/* 718 */     if (deallocated) {
/* 719 */       this.leak.close();
/*     */     } else {
/* 721 */       this.leak.record();
/*     */     }
/* 723 */     return deallocated;
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 728 */     boolean deallocated = super.release(decrement);
/* 729 */     if (deallocated) {
/* 730 */       this.leak.close();
/*     */     } else {
/* 732 */       this.leak.record();
/*     */     }
/* 734 */     return deallocated;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\AdvancedLeakAwareByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */