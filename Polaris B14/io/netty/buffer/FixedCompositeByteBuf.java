/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.RecyclableArrayList;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.ReadOnlyBufferException;
/*     */ import java.nio.channels.GatheringByteChannel;
/*     */ import java.nio.channels.ScatteringByteChannel;
/*     */ import java.util.Collections;
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
/*     */ final class FixedCompositeByteBuf
/*     */   extends AbstractReferenceCountedByteBuf
/*     */ {
/*  36 */   private static final ByteBuf[] EMPTY = { Unpooled.EMPTY_BUFFER };
/*     */   private final int nioBufferCount;
/*     */   private final int capacity;
/*     */   private final ByteBufAllocator allocator;
/*     */   private final ByteOrder order;
/*     */   private final Object[] buffers;
/*     */   private final boolean direct;
/*     */   
/*     */   FixedCompositeByteBuf(ByteBufAllocator allocator, ByteBuf... buffers) {
/*  45 */     super(Integer.MAX_VALUE);
/*  46 */     if (buffers.length == 0) {
/*  47 */       this.buffers = EMPTY;
/*  48 */       this.order = ByteOrder.BIG_ENDIAN;
/*  49 */       this.nioBufferCount = 1;
/*  50 */       this.capacity = 0;
/*  51 */       this.direct = buffers[0].isDirect();
/*     */     } else {
/*  53 */       ByteBuf b = buffers[0];
/*  54 */       this.buffers = new Object[buffers.length];
/*  55 */       this.buffers[0] = b;
/*  56 */       boolean direct = true;
/*  57 */       int nioBufferCount = b.nioBufferCount();
/*  58 */       int capacity = b.readableBytes();
/*  59 */       this.order = b.order();
/*  60 */       for (int i = 1; i < buffers.length; i++) {
/*  61 */         b = buffers[i];
/*  62 */         if (buffers[i].order() != this.order) {
/*  63 */           throw new IllegalArgumentException("All ByteBufs need to have same ByteOrder");
/*     */         }
/*  65 */         nioBufferCount += b.nioBufferCount();
/*  66 */         capacity += b.readableBytes();
/*  67 */         if (!b.isDirect()) {
/*  68 */           direct = false;
/*     */         }
/*  70 */         this.buffers[i] = b;
/*     */       }
/*  72 */       this.nioBufferCount = nioBufferCount;
/*  73 */       this.capacity = capacity;
/*  74 */       this.direct = direct;
/*     */     }
/*  76 */     setIndex(0, capacity());
/*  77 */     this.allocator = allocator;
/*     */   }
/*     */   
/*     */   public boolean isWritable()
/*     */   {
/*  82 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isWritable(int size)
/*     */   {
/*  87 */     return false;
/*     */   }
/*     */   
/*     */   public ByteBuf discardReadBytes()
/*     */   {
/*  92 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*     */   {
/*  97 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*     */   {
/* 102 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setBytes(int index, ByteBuffer src)
/*     */   {
/* 107 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setByte(int index, int value)
/*     */   {
/* 112 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected void _setByte(int index, int value)
/*     */   {
/* 117 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setShort(int index, int value)
/*     */   {
/* 122 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected void _setShort(int index, int value)
/*     */   {
/* 127 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setMedium(int index, int value)
/*     */   {
/* 132 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected void _setMedium(int index, int value)
/*     */   {
/* 137 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setInt(int index, int value)
/*     */   {
/* 142 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected void _setInt(int index, int value)
/*     */   {
/* 147 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBuf setLong(int index, long value)
/*     */   {
/* 152 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   protected void _setLong(int index, long value)
/*     */   {
/* 157 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public int setBytes(int index, InputStream in, int length)
/*     */   {
/* 162 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public int setBytes(int index, ScatteringByteChannel in, int length)
/*     */   {
/* 167 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public int capacity()
/*     */   {
/* 172 */     return this.capacity;
/*     */   }
/*     */   
/*     */   public int maxCapacity()
/*     */   {
/* 177 */     return this.capacity;
/*     */   }
/*     */   
/*     */   public ByteBuf capacity(int newCapacity)
/*     */   {
/* 182 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */   
/*     */   public ByteBufAllocator alloc()
/*     */   {
/* 187 */     return this.allocator;
/*     */   }
/*     */   
/*     */   public ByteOrder order()
/*     */   {
/* 192 */     return this.order;
/*     */   }
/*     */   
/*     */   public ByteBuf unwrap()
/*     */   {
/* 197 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/* 202 */     return this.direct;
/*     */   }
/*     */   
/*     */   private Component findComponent(int index) {
/* 206 */     int readable = 0;
/* 207 */     for (int i = 0; i < this.buffers.length; i++) {
/* 208 */       Component comp = null;
/*     */       
/* 210 */       Object obj = this.buffers[i];
/*     */       boolean isBuffer;
/* 212 */       ByteBuf b; boolean isBuffer; if ((obj instanceof ByteBuf)) {
/* 213 */         ByteBuf b = (ByteBuf)obj;
/* 214 */         isBuffer = true;
/*     */       } else {
/* 216 */         comp = (Component)obj;
/* 217 */         b = comp.buf;
/* 218 */         isBuffer = false;
/*     */       }
/* 220 */       readable += b.readableBytes();
/* 221 */       if (index < readable) {
/* 222 */         if (isBuffer)
/*     */         {
/*     */ 
/* 225 */           comp = new Component(i, readable - b.readableBytes(), b);
/* 226 */           this.buffers[i] = comp;
/*     */         }
/* 228 */         return comp;
/*     */       }
/*     */     }
/* 231 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private ByteBuf buffer(int i)
/*     */   {
/* 238 */     Object obj = this.buffers[i];
/* 239 */     if ((obj instanceof ByteBuf)) {
/* 240 */       return (ByteBuf)obj;
/*     */     }
/* 242 */     return ((Component)obj).buf;
/*     */   }
/*     */   
/*     */   public byte getByte(int index)
/*     */   {
/* 247 */     return _getByte(index);
/*     */   }
/*     */   
/*     */   protected byte _getByte(int index)
/*     */   {
/* 252 */     Component c = findComponent(index);
/* 253 */     return c.buf.getByte(index - c.offset);
/*     */   }
/*     */   
/*     */   protected short _getShort(int index)
/*     */   {
/* 258 */     Component c = findComponent(index);
/* 259 */     if (index + 2 <= c.endOffset)
/* 260 */       return c.buf.getShort(index - c.offset);
/* 261 */     if (order() == ByteOrder.BIG_ENDIAN) {
/* 262 */       return (short)((_getByte(index) & 0xFF) << 8 | _getByte(index + 1) & 0xFF);
/*     */     }
/* 264 */     return (short)(_getByte(index) & 0xFF | (_getByte(index + 1) & 0xFF) << 8);
/*     */   }
/*     */   
/*     */ 
/*     */   protected int _getUnsignedMedium(int index)
/*     */   {
/* 270 */     Component c = findComponent(index);
/* 271 */     if (index + 3 <= c.endOffset)
/* 272 */       return c.buf.getUnsignedMedium(index - c.offset);
/* 273 */     if (order() == ByteOrder.BIG_ENDIAN) {
/* 274 */       return (_getShort(index) & 0xFFFF) << 8 | _getByte(index + 2) & 0xFF;
/*     */     }
/* 276 */     return _getShort(index) & 0xFFFF | (_getByte(index + 2) & 0xFF) << 16;
/*     */   }
/*     */   
/*     */ 
/*     */   protected int _getInt(int index)
/*     */   {
/* 282 */     Component c = findComponent(index);
/* 283 */     if (index + 4 <= c.endOffset)
/* 284 */       return c.buf.getInt(index - c.offset);
/* 285 */     if (order() == ByteOrder.BIG_ENDIAN) {
/* 286 */       return (_getShort(index) & 0xFFFF) << 16 | _getShort(index + 2) & 0xFFFF;
/*     */     }
/* 288 */     return _getShort(index) & 0xFFFF | (_getShort(index + 2) & 0xFFFF) << 16;
/*     */   }
/*     */   
/*     */ 
/*     */   protected long _getLong(int index)
/*     */   {
/* 294 */     Component c = findComponent(index);
/* 295 */     if (index + 8 <= c.endOffset)
/* 296 */       return c.buf.getLong(index - c.offset);
/* 297 */     if (order() == ByteOrder.BIG_ENDIAN) {
/* 298 */       return (_getInt(index) & 0xFFFFFFFF) << 32 | _getInt(index + 4) & 0xFFFFFFFF;
/*     */     }
/* 300 */     return _getInt(index) & 0xFFFFFFFF | (_getInt(index + 4) & 0xFFFFFFFF) << 32;
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/* 306 */     checkDstIndex(index, length, dstIndex, dst.length);
/* 307 */     if (length == 0) {
/* 308 */       return this;
/*     */     }
/*     */     
/* 311 */     Component c = findComponent(index);
/* 312 */     int i = c.index;
/* 313 */     int adjustment = c.offset;
/* 314 */     ByteBuf s = c.buf;
/*     */     for (;;) {
/* 316 */       int localLength = Math.min(length, s.readableBytes() - (index - adjustment));
/* 317 */       s.getBytes(index - adjustment, dst, dstIndex, localLength);
/* 318 */       index += localLength;
/* 319 */       dstIndex += localLength;
/* 320 */       length -= localLength;
/* 321 */       adjustment += s.readableBytes();
/* 322 */       if (length <= 0) {
/*     */         break;
/*     */       }
/* 325 */       s = buffer(++i);
/*     */     }
/* 327 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 332 */     int limit = dst.limit();
/* 333 */     int length = dst.remaining();
/*     */     
/* 335 */     checkIndex(index, length);
/* 336 */     if (length == 0) {
/* 337 */       return this;
/*     */     }
/*     */     try
/*     */     {
/* 341 */       Component c = findComponent(index);
/* 342 */       int i = c.index;
/* 343 */       int adjustment = c.offset;
/* 344 */       ByteBuf s = c.buf;
/*     */       for (;;) {
/* 346 */         int localLength = Math.min(length, s.capacity() - (index - adjustment));
/* 347 */         dst.limit(dst.position() + localLength);
/* 348 */         s.getBytes(index - adjustment, dst);
/* 349 */         index += localLength;
/* 350 */         length -= localLength;
/* 351 */         adjustment += s.readableBytes();
/* 352 */         if (length <= 0) {
/*     */           break;
/*     */         }
/* 355 */         s = buffer(++i);
/*     */       }
/*     */     } finally {
/* 358 */       dst.limit(limit);
/*     */     }
/* 360 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/* 365 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/* 366 */     if (length == 0) {
/* 367 */       return this;
/*     */     }
/*     */     
/* 370 */     Component c = findComponent(index);
/* 371 */     int i = c.index;
/* 372 */     int adjustment = c.offset;
/* 373 */     ByteBuf s = c.buf;
/*     */     for (;;) {
/* 375 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/* 376 */       s.getBytes(index - adjustment, dst, dstIndex, localLength);
/* 377 */       index += localLength;
/* 378 */       dstIndex += localLength;
/* 379 */       length -= localLength;
/* 380 */       adjustment += s.readableBytes();
/* 381 */       if (length <= 0) {
/*     */         break;
/*     */       }
/* 384 */       s = buffer(++i);
/*     */     }
/* 386 */     return this;
/*     */   }
/*     */   
/*     */   public int getBytes(int index, GatheringByteChannel out, int length)
/*     */     throws IOException
/*     */   {
/* 392 */     int count = nioBufferCount();
/* 393 */     if (count == 1) {
/* 394 */       return out.write(internalNioBuffer(index, length));
/*     */     }
/* 396 */     long writtenBytes = out.write(nioBuffers(index, length));
/* 397 */     if (writtenBytes > 2147483647L) {
/* 398 */       return Integer.MAX_VALUE;
/*     */     }
/* 400 */     return (int)writtenBytes;
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuf getBytes(int index, OutputStream out, int length)
/*     */     throws IOException
/*     */   {
/* 407 */     checkIndex(index, length);
/* 408 */     if (length == 0) {
/* 409 */       return this;
/*     */     }
/*     */     
/* 412 */     Component c = findComponent(index);
/* 413 */     int i = c.index;
/* 414 */     int adjustment = c.offset;
/* 415 */     ByteBuf s = c.buf;
/*     */     for (;;) {
/* 417 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/* 418 */       s.getBytes(index - adjustment, out, localLength);
/* 419 */       index += localLength;
/* 420 */       length -= localLength;
/* 421 */       adjustment += s.readableBytes();
/* 422 */       if (length <= 0) {
/*     */         break;
/*     */       }
/* 425 */       s = buffer(++i);
/*     */     }
/* 427 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 432 */     checkIndex(index, length);
/* 433 */     boolean release = true;
/* 434 */     ByteBuf buf = alloc().buffer(length);
/*     */     try {
/* 436 */       buf.writeBytes(this, index, length);
/* 437 */       release = false;
/* 438 */       return buf;
/*     */     } finally {
/* 440 */       if (release) {
/* 441 */         buf.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int nioBufferCount()
/*     */   {
/* 448 */     return this.nioBufferCount;
/*     */   }
/*     */   
/*     */   public ByteBuffer nioBuffer(int index, int length)
/*     */   {
/* 453 */     checkIndex(index, length);
/* 454 */     if (this.buffers.length == 1) {
/* 455 */       ByteBuf buf = buffer(0);
/* 456 */       if (buf.nioBufferCount() == 1) {
/* 457 */         return buf.nioBuffer(index, length);
/*     */       }
/*     */     }
/* 460 */     ByteBuffer merged = ByteBuffer.allocate(length).order(order());
/* 461 */     ByteBuffer[] buffers = nioBuffers(index, length);
/*     */     
/*     */ 
/* 464 */     for (int i = 0; i < buffers.length; i++) {
/* 465 */       merged.put(buffers[i]);
/*     */     }
/*     */     
/* 468 */     merged.flip();
/* 469 */     return merged;
/*     */   }
/*     */   
/*     */   public ByteBuffer internalNioBuffer(int index, int length)
/*     */   {
/* 474 */     if (this.buffers.length == 1) {
/* 475 */       return buffer(0).internalNioBuffer(index, length);
/*     */     }
/* 477 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public ByteBuffer[] nioBuffers(int index, int length)
/*     */   {
/* 482 */     checkIndex(index, length);
/* 483 */     if (length == 0) {
/* 484 */       return EmptyArrays.EMPTY_BYTE_BUFFERS;
/*     */     }
/*     */     
/* 487 */     RecyclableArrayList array = RecyclableArrayList.newInstance(this.buffers.length);
/*     */     try {
/* 489 */       Component c = findComponent(index);
/* 490 */       int i = c.index;
/* 491 */       int adjustment = c.offset;
/* 492 */       ByteBuf s = c.buf;
/*     */       int localLength;
/* 494 */       for (;;) { localLength = Math.min(length, s.capacity() - (index - adjustment));
/* 495 */         switch (s.nioBufferCount()) {
/*     */         case 0: 
/* 497 */           throw new UnsupportedOperationException();
/*     */         case 1: 
/* 499 */           array.add(s.nioBuffer(index - adjustment, localLength));
/* 500 */           break;
/*     */         default: 
/* 502 */           Collections.addAll(array, s.nioBuffers(index - adjustment, localLength));
/*     */         }
/*     */         
/* 505 */         index += localLength;
/* 506 */         length -= localLength;
/* 507 */         adjustment += s.readableBytes();
/* 508 */         if (length <= 0) {
/*     */           break;
/*     */         }
/* 511 */         s = buffer(++i);
/*     */       }
/*     */       
/* 514 */       return (ByteBuffer[])array.toArray(new ByteBuffer[array.size()]);
/*     */     } finally {
/* 516 */       array.recycle();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasArray()
/*     */   {
/* 522 */     return false;
/*     */   }
/*     */   
/*     */   public byte[] array()
/*     */   {
/* 527 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public int arrayOffset()
/*     */   {
/* 532 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean hasMemoryAddress()
/*     */   {
/* 537 */     return false;
/*     */   }
/*     */   
/*     */   public long memoryAddress()
/*     */   {
/* 542 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected void deallocate()
/*     */   {
/* 547 */     for (int i = 0; i < this.buffers.length; i++) {
/* 548 */       buffer(i).release();
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 554 */     String result = super.toString();
/* 555 */     result = result.substring(0, result.length() - 1);
/* 556 */     return result + ", components=" + this.buffers.length + ')';
/*     */   }
/*     */   
/*     */   private static final class Component {
/*     */     private final int index;
/*     */     private final int offset;
/*     */     private final ByteBuf buf;
/*     */     private final int endOffset;
/*     */     
/*     */     Component(int index, int offset, ByteBuf buf) {
/* 566 */       this.index = index;
/* 567 */       this.offset = offset;
/* 568 */       this.endOffset = (offset + buf.readableBytes());
/* 569 */       this.buf = buf;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\FixedCompositeByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */