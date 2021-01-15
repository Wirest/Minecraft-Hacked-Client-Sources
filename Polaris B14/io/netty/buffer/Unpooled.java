/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public final class Unpooled
/*     */ {
/*     */   private static final ByteBufAllocator ALLOC;
/*     */   public static final ByteOrder BIG_ENDIAN;
/*     */   public static final ByteOrder LITTLE_ENDIAN;
/*     */   public static final ByteBuf EMPTY_BUFFER;
/*     */   
/*     */   static
/*     */   {
/*  79 */     ALLOC = UnpooledByteBufAllocator.DEFAULT;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  84 */     BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  89 */     LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  94 */     EMPTY_BUFFER = ALLOC.buffer(0, 0);
/*     */     
/*     */ 
/*  97 */     assert ((EMPTY_BUFFER instanceof EmptyByteBuf)) : "EMPTY_BUFFER must be an EmptyByteBuf.";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf buffer()
/*     */   {
/* 105 */     return ALLOC.heapBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf directBuffer()
/*     */   {
/* 113 */     return ALLOC.directBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf buffer(int initialCapacity)
/*     */   {
/* 122 */     return ALLOC.heapBuffer(initialCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf directBuffer(int initialCapacity)
/*     */   {
/* 131 */     return ALLOC.directBuffer(initialCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf buffer(int initialCapacity, int maxCapacity)
/*     */   {
/* 141 */     return ALLOC.heapBuffer(initialCapacity, maxCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf directBuffer(int initialCapacity, int maxCapacity)
/*     */   {
/* 151 */     return ALLOC.directBuffer(initialCapacity, maxCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(byte[] array)
/*     */   {
/* 160 */     if (array.length == 0) {
/* 161 */       return EMPTY_BUFFER;
/*     */     }
/* 163 */     return new UnpooledHeapByteBuf(ALLOC, array, array.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(byte[] array, int offset, int length)
/*     */   {
/* 172 */     if (length == 0) {
/* 173 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 176 */     if ((offset == 0) && (length == array.length)) {
/* 177 */       return wrappedBuffer(array);
/*     */     }
/*     */     
/* 180 */     return wrappedBuffer(array).slice(offset, length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(ByteBuffer buffer)
/*     */   {
/* 189 */     if (!buffer.hasRemaining()) {
/* 190 */       return EMPTY_BUFFER;
/*     */     }
/* 192 */     if (buffer.hasArray()) {
/* 193 */       return wrappedBuffer(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining()).order(buffer.order());
/*     */     }
/*     */     
/*     */ 
/* 197 */     if (PlatformDependent.hasUnsafe()) {
/* 198 */       if (buffer.isReadOnly()) {
/* 199 */         if (buffer.isDirect()) {
/* 200 */           return new ReadOnlyUnsafeDirectByteBuf(ALLOC, buffer);
/*     */         }
/* 202 */         return new ReadOnlyByteBufferBuf(ALLOC, buffer);
/*     */       }
/*     */       
/* 205 */       return new UnpooledUnsafeDirectByteBuf(ALLOC, buffer, buffer.remaining());
/*     */     }
/*     */     
/* 208 */     if (buffer.isReadOnly()) {
/* 209 */       return new ReadOnlyByteBufferBuf(ALLOC, buffer);
/*     */     }
/* 211 */     return new UnpooledDirectByteBuf(ALLOC, buffer, buffer.remaining());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(ByteBuf buffer)
/*     */   {
/* 222 */     if (buffer.isReadable()) {
/* 223 */       return buffer.slice();
/*     */     }
/* 225 */     return EMPTY_BUFFER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(byte[]... arrays)
/*     */   {
/* 235 */     return wrappedBuffer(16, arrays);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(ByteBuf... buffers)
/*     */   {
/* 244 */     return wrappedBuffer(16, buffers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(ByteBuffer... buffers)
/*     */   {
/* 253 */     return wrappedBuffer(16, buffers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(int maxNumComponents, byte[]... arrays)
/*     */   {
/* 262 */     switch (arrays.length) {
/*     */     case 0: 
/*     */       break;
/*     */     case 1: 
/* 266 */       if (arrays[0].length != 0) {
/* 267 */         return wrappedBuffer(arrays[0]);
/*     */       }
/*     */       
/*     */       break;
/*     */     default: 
/* 272 */       List<ByteBuf> components = new ArrayList(arrays.length);
/* 273 */       for (byte[] a : arrays) {
/* 274 */         if (a == null) {
/*     */           break;
/*     */         }
/* 277 */         if (a.length > 0) {
/* 278 */           components.add(wrappedBuffer(a));
/*     */         }
/*     */       }
/*     */       
/* 282 */       if (!components.isEmpty()) {
/* 283 */         return new CompositeByteBuf(ALLOC, false, maxNumComponents, components);
/*     */       }
/*     */       break;
/*     */     }
/* 287 */     return EMPTY_BUFFER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(int maxNumComponents, ByteBuf... buffers)
/*     */   {
/* 296 */     switch (buffers.length) {
/*     */     case 0: 
/*     */       break;
/*     */     case 1: 
/* 300 */       if (buffers[0].isReadable()) {
/* 301 */         return wrappedBuffer(buffers[0].order(BIG_ENDIAN));
/*     */       }
/*     */       break;
/*     */     default: 
/* 305 */       for (ByteBuf b : buffers) {
/* 306 */         if (b.isReadable()) {
/* 307 */           return new CompositeByteBuf(ALLOC, false, maxNumComponents, buffers);
/*     */         }
/*     */       }
/*     */     }
/* 311 */     return EMPTY_BUFFER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(int maxNumComponents, ByteBuffer... buffers)
/*     */   {
/* 320 */     switch (buffers.length) {
/*     */     case 0: 
/*     */       break;
/*     */     case 1: 
/* 324 */       if (buffers[0].hasRemaining()) {
/* 325 */         return wrappedBuffer(buffers[0].order(BIG_ENDIAN));
/*     */       }
/*     */       
/*     */       break;
/*     */     default: 
/* 330 */       List<ByteBuf> components = new ArrayList(buffers.length);
/* 331 */       for (ByteBuffer b : buffers) {
/* 332 */         if (b == null) {
/*     */           break;
/*     */         }
/* 335 */         if (b.remaining() > 0) {
/* 336 */           components.add(wrappedBuffer(b.order(BIG_ENDIAN)));
/*     */         }
/*     */       }
/*     */       
/* 340 */       if (!components.isEmpty()) {
/* 341 */         return new CompositeByteBuf(ALLOC, false, maxNumComponents, components);
/*     */       }
/*     */       break;
/*     */     }
/* 345 */     return EMPTY_BUFFER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static CompositeByteBuf compositeBuffer()
/*     */   {
/* 352 */     return compositeBuffer(16);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static CompositeByteBuf compositeBuffer(int maxNumComponents)
/*     */   {
/* 359 */     return new CompositeByteBuf(ALLOC, false, maxNumComponents);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(byte[] array)
/*     */   {
/* 368 */     if (array.length == 0) {
/* 369 */       return EMPTY_BUFFER;
/*     */     }
/* 371 */     return wrappedBuffer((byte[])array.clone());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(byte[] array, int offset, int length)
/*     */   {
/* 381 */     if (length == 0) {
/* 382 */       return EMPTY_BUFFER;
/*     */     }
/* 384 */     byte[] copy = new byte[length];
/* 385 */     System.arraycopy(array, offset, copy, 0, length);
/* 386 */     return wrappedBuffer(copy);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(ByteBuffer buffer)
/*     */   {
/* 396 */     int length = buffer.remaining();
/* 397 */     if (length == 0) {
/* 398 */       return EMPTY_BUFFER;
/*     */     }
/* 400 */     byte[] copy = new byte[length];
/* 401 */     int position = buffer.position();
/*     */     try {
/* 403 */       buffer.get(copy);
/*     */     } finally {
/* 405 */       buffer.position(position);
/*     */     }
/* 407 */     return wrappedBuffer(copy).order(buffer.order());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(ByteBuf buffer)
/*     */   {
/* 417 */     int readable = buffer.readableBytes();
/* 418 */     if (readable > 0) {
/* 419 */       ByteBuf copy = buffer(readable);
/* 420 */       copy.writeBytes(buffer, buffer.readerIndex(), readable);
/* 421 */       return copy;
/*     */     }
/* 423 */     return EMPTY_BUFFER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(byte[]... arrays)
/*     */   {
/* 434 */     switch (arrays.length) {
/*     */     case 0: 
/* 436 */       return EMPTY_BUFFER;
/*     */     case 1: 
/* 438 */       if (arrays[0].length == 0) {
/* 439 */         return EMPTY_BUFFER;
/*     */       }
/* 441 */       return copiedBuffer(arrays[0]);
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 446 */     int length = 0;
/* 447 */     for (byte[] a : arrays) {
/* 448 */       if (Integer.MAX_VALUE - length < a.length) {
/* 449 */         throw new IllegalArgumentException("The total length of the specified arrays is too big.");
/*     */       }
/*     */       
/* 452 */       length += a.length;
/*     */     }
/*     */     
/* 455 */     if (length == 0) {
/* 456 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 459 */     byte[] mergedArray = new byte[length];
/* 460 */     int i = 0; for (int j = 0; i < arrays.length; i++) {
/* 461 */       byte[] a = arrays[i];
/* 462 */       System.arraycopy(a, 0, mergedArray, j, a.length);
/* 463 */       j += a.length;
/*     */     }
/*     */     
/* 466 */     return wrappedBuffer(mergedArray);
/*     */   }
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
/*     */   public static ByteBuf copiedBuffer(ByteBuf... buffers)
/*     */   {
/* 480 */     switch (buffers.length) {
/*     */     case 0: 
/* 482 */       return EMPTY_BUFFER;
/*     */     case 1: 
/* 484 */       return copiedBuffer(buffers[0]);
/*     */     }
/*     */     
/*     */     
/* 488 */     ByteOrder order = null;
/* 489 */     int length = 0;
/* 490 */     for (ByteBuf b : buffers) {
/* 491 */       int bLen = b.readableBytes();
/* 492 */       if (bLen > 0)
/*     */       {
/*     */ 
/* 495 */         if (Integer.MAX_VALUE - length < bLen) {
/* 496 */           throw new IllegalArgumentException("The total length of the specified buffers is too big.");
/*     */         }
/*     */         
/* 499 */         length += bLen;
/* 500 */         if (order != null) {
/* 501 */           if (!order.equals(b.order())) {
/* 502 */             throw new IllegalArgumentException("inconsistent byte order");
/*     */           }
/*     */         } else {
/* 505 */           order = b.order();
/*     */         }
/*     */       }
/*     */     }
/* 509 */     if (length == 0) {
/* 510 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 513 */     byte[] mergedArray = new byte[length];
/* 514 */     int i = 0; for (int j = 0; i < buffers.length; i++) {
/* 515 */       ByteBuf b = buffers[i];
/* 516 */       int bLen = b.readableBytes();
/* 517 */       b.getBytes(b.readerIndex(), mergedArray, j, bLen);
/* 518 */       j += bLen;
/*     */     }
/*     */     
/* 521 */     return wrappedBuffer(mergedArray).order(order);
/*     */   }
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
/*     */   public static ByteBuf copiedBuffer(ByteBuffer... buffers)
/*     */   {
/* 535 */     switch (buffers.length) {
/*     */     case 0: 
/* 537 */       return EMPTY_BUFFER;
/*     */     case 1: 
/* 539 */       return copiedBuffer(buffers[0]);
/*     */     }
/*     */     
/*     */     
/* 543 */     ByteOrder order = null;
/* 544 */     int length = 0;
/* 545 */     for (ByteBuffer b : buffers) {
/* 546 */       int bLen = b.remaining();
/* 547 */       if (bLen > 0)
/*     */       {
/*     */ 
/* 550 */         if (Integer.MAX_VALUE - length < bLen) {
/* 551 */           throw new IllegalArgumentException("The total length of the specified buffers is too big.");
/*     */         }
/*     */         
/* 554 */         length += bLen;
/* 555 */         if (order != null) {
/* 556 */           if (!order.equals(b.order())) {
/* 557 */             throw new IllegalArgumentException("inconsistent byte order");
/*     */           }
/*     */         } else {
/* 560 */           order = b.order();
/*     */         }
/*     */       }
/*     */     }
/* 564 */     if (length == 0) {
/* 565 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 568 */     byte[] mergedArray = new byte[length];
/* 569 */     int i = 0; for (int j = 0; i < buffers.length; i++) {
/* 570 */       ByteBuffer b = buffers[i];
/* 571 */       int bLen = b.remaining();
/* 572 */       int oldPos = b.position();
/* 573 */       b.get(mergedArray, j, bLen);
/* 574 */       b.position(oldPos);
/* 575 */       j += bLen;
/*     */     }
/*     */     
/* 578 */     return wrappedBuffer(mergedArray).order(order);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(CharSequence string, Charset charset)
/*     */   {
/* 588 */     if (string == null) {
/* 589 */       throw new NullPointerException("string");
/*     */     }
/*     */     
/* 592 */     if ((string instanceof CharBuffer)) {
/* 593 */       return copiedBuffer((CharBuffer)string, charset);
/*     */     }
/*     */     
/* 596 */     return copiedBuffer(CharBuffer.wrap(string), charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(CharSequence string, int offset, int length, Charset charset)
/*     */   {
/* 607 */     if (string == null) {
/* 608 */       throw new NullPointerException("string");
/*     */     }
/* 610 */     if (length == 0) {
/* 611 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 614 */     if ((string instanceof CharBuffer)) {
/* 615 */       CharBuffer buf = (CharBuffer)string;
/* 616 */       if (buf.hasArray()) {
/* 617 */         return copiedBuffer(buf.array(), buf.arrayOffset() + buf.position() + offset, length, charset);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 623 */       buf = buf.slice();
/* 624 */       buf.limit(length);
/* 625 */       buf.position(offset);
/* 626 */       return copiedBuffer(buf, charset);
/*     */     }
/*     */     
/* 629 */     return copiedBuffer(CharBuffer.wrap(string, offset, offset + length), charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(char[] array, Charset charset)
/*     */   {
/* 639 */     if (array == null) {
/* 640 */       throw new NullPointerException("array");
/*     */     }
/* 642 */     return copiedBuffer(array, 0, array.length, charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(char[] array, int offset, int length, Charset charset)
/*     */   {
/* 652 */     if (array == null) {
/* 653 */       throw new NullPointerException("array");
/*     */     }
/* 655 */     if (length == 0) {
/* 656 */       return EMPTY_BUFFER;
/*     */     }
/* 658 */     return copiedBuffer(CharBuffer.wrap(array, offset, length), charset);
/*     */   }
/*     */   
/*     */   private static ByteBuf copiedBuffer(CharBuffer buffer, Charset charset) {
/* 662 */     return ByteBufUtil.encodeString0(ALLOC, true, buffer, charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf unmodifiableBuffer(ByteBuf buffer)
/*     */   {
/* 672 */     ByteOrder endianness = buffer.order();
/* 673 */     if (endianness == BIG_ENDIAN) {
/* 674 */       return new ReadOnlyByteBuf(buffer);
/*     */     }
/*     */     
/* 677 */     return new ReadOnlyByteBuf(buffer.order(BIG_ENDIAN)).order(LITTLE_ENDIAN);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyInt(int value)
/*     */   {
/* 684 */     ByteBuf buf = buffer(4);
/* 685 */     buf.writeInt(value);
/* 686 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyInt(int... values)
/*     */   {
/* 693 */     if ((values == null) || (values.length == 0)) {
/* 694 */       return EMPTY_BUFFER;
/*     */     }
/* 696 */     ByteBuf buffer = buffer(values.length * 4);
/* 697 */     for (int v : values) {
/* 698 */       buffer.writeInt(v);
/*     */     }
/* 700 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyShort(int value)
/*     */   {
/* 707 */     ByteBuf buf = buffer(2);
/* 708 */     buf.writeShort(value);
/* 709 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyShort(short... values)
/*     */   {
/* 716 */     if ((values == null) || (values.length == 0)) {
/* 717 */       return EMPTY_BUFFER;
/*     */     }
/* 719 */     ByteBuf buffer = buffer(values.length * 2);
/* 720 */     for (int v : values) {
/* 721 */       buffer.writeShort(v);
/*     */     }
/* 723 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyShort(int... values)
/*     */   {
/* 730 */     if ((values == null) || (values.length == 0)) {
/* 731 */       return EMPTY_BUFFER;
/*     */     }
/* 733 */     ByteBuf buffer = buffer(values.length * 2);
/* 734 */     for (int v : values) {
/* 735 */       buffer.writeShort(v);
/*     */     }
/* 737 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyMedium(int value)
/*     */   {
/* 744 */     ByteBuf buf = buffer(3);
/* 745 */     buf.writeMedium(value);
/* 746 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyMedium(int... values)
/*     */   {
/* 753 */     if ((values == null) || (values.length == 0)) {
/* 754 */       return EMPTY_BUFFER;
/*     */     }
/* 756 */     ByteBuf buffer = buffer(values.length * 3);
/* 757 */     for (int v : values) {
/* 758 */       buffer.writeMedium(v);
/*     */     }
/* 760 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyLong(long value)
/*     */   {
/* 767 */     ByteBuf buf = buffer(8);
/* 768 */     buf.writeLong(value);
/* 769 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyLong(long... values)
/*     */   {
/* 776 */     if ((values == null) || (values.length == 0)) {
/* 777 */       return EMPTY_BUFFER;
/*     */     }
/* 779 */     ByteBuf buffer = buffer(values.length * 8);
/* 780 */     for (long v : values) {
/* 781 */       buffer.writeLong(v);
/*     */     }
/* 783 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyBoolean(boolean value)
/*     */   {
/* 790 */     ByteBuf buf = buffer(1);
/* 791 */     buf.writeBoolean(value);
/* 792 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyBoolean(boolean... values)
/*     */   {
/* 799 */     if ((values == null) || (values.length == 0)) {
/* 800 */       return EMPTY_BUFFER;
/*     */     }
/* 802 */     ByteBuf buffer = buffer(values.length);
/* 803 */     for (boolean v : values) {
/* 804 */       buffer.writeBoolean(v);
/*     */     }
/* 806 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyFloat(float value)
/*     */   {
/* 813 */     ByteBuf buf = buffer(4);
/* 814 */     buf.writeFloat(value);
/* 815 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyFloat(float... values)
/*     */   {
/* 822 */     if ((values == null) || (values.length == 0)) {
/* 823 */       return EMPTY_BUFFER;
/*     */     }
/* 825 */     ByteBuf buffer = buffer(values.length * 4);
/* 826 */     for (float v : values) {
/* 827 */       buffer.writeFloat(v);
/*     */     }
/* 829 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyDouble(double value)
/*     */   {
/* 836 */     ByteBuf buf = buffer(8);
/* 837 */     buf.writeDouble(value);
/* 838 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyDouble(double... values)
/*     */   {
/* 845 */     if ((values == null) || (values.length == 0)) {
/* 846 */       return EMPTY_BUFFER;
/*     */     }
/* 848 */     ByteBuf buffer = buffer(values.length * 8);
/* 849 */     for (double v : values) {
/* 850 */       buffer.writeDouble(v);
/*     */     }
/* 852 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf unreleasableBuffer(ByteBuf buf)
/*     */   {
/* 859 */     return new UnreleasableByteBuf(buf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf unmodifiableBuffer(ByteBuf... buffers)
/*     */   {
/* 867 */     return new FixedCompositeByteBuf(ALLOC, buffers);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\Unpooled.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */