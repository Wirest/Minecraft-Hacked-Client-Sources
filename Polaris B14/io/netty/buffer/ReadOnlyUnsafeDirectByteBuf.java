/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ final class ReadOnlyUnsafeDirectByteBuf
/*     */   extends ReadOnlyByteBufferBuf
/*     */ {
/*  30 */   private static final boolean NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
/*     */   private final long memoryAddress;
/*     */   
/*     */   ReadOnlyUnsafeDirectByteBuf(ByteBufAllocator allocator, ByteBuffer buffer) {
/*  34 */     super(allocator, buffer);
/*  35 */     this.memoryAddress = PlatformDependent.directBufferAddress(buffer);
/*     */   }
/*     */   
/*     */   protected byte _getByte(int index)
/*     */   {
/*  40 */     return PlatformDependent.getByte(addr(index));
/*     */   }
/*     */   
/*     */   protected short _getShort(int index)
/*     */   {
/*  45 */     short v = PlatformDependent.getShort(addr(index));
/*  46 */     return NATIVE_ORDER ? v : Short.reverseBytes(v);
/*     */   }
/*     */   
/*     */   protected int _getUnsignedMedium(int index)
/*     */   {
/*  51 */     long addr = addr(index);
/*  52 */     return (PlatformDependent.getByte(addr) & 0xFF) << 16 | (PlatformDependent.getByte(addr + 1L) & 0xFF) << 8 | PlatformDependent.getByte(addr + 2L) & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int _getInt(int index)
/*     */   {
/*  59 */     int v = PlatformDependent.getInt(addr(index));
/*  60 */     return NATIVE_ORDER ? v : Integer.reverseBytes(v);
/*     */   }
/*     */   
/*     */   protected long _getLong(int index)
/*     */   {
/*  65 */     long v = PlatformDependent.getLong(addr(index));
/*  66 */     return NATIVE_ORDER ? v : Long.reverseBytes(v);
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*     */   {
/*  71 */     checkIndex(index, length);
/*  72 */     if (dst == null) {
/*  73 */       throw new NullPointerException("dst");
/*     */     }
/*  75 */     if ((dstIndex < 0) || (dstIndex > dst.capacity() - length)) {
/*  76 */       throw new IndexOutOfBoundsException("dstIndex: " + dstIndex);
/*     */     }
/*     */     
/*  79 */     if (dst.hasMemoryAddress()) {
/*  80 */       PlatformDependent.copyMemory(addr(index), dst.memoryAddress() + dstIndex, length);
/*  81 */     } else if (dst.hasArray()) {
/*  82 */       PlatformDependent.copyMemory(addr(index), dst.array(), dst.arrayOffset() + dstIndex, length);
/*     */     } else {
/*  84 */       dst.setBytes(dstIndex, this, index, length);
/*     */     }
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*     */   {
/*  91 */     checkIndex(index, length);
/*  92 */     if (dst == null) {
/*  93 */       throw new NullPointerException("dst");
/*     */     }
/*  95 */     if ((dstIndex < 0) || (dstIndex > dst.length - length)) {
/*  96 */       throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", new Object[] { Integer.valueOf(dstIndex), Integer.valueOf(length), Integer.valueOf(dst.length) }));
/*     */     }
/*     */     
/*     */ 
/* 100 */     if (length != 0) {
/* 101 */       PlatformDependent.copyMemory(addr(index), dst, dstIndex, length);
/*     */     }
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf getBytes(int index, ByteBuffer dst)
/*     */   {
/* 108 */     checkIndex(index);
/* 109 */     if (dst == null) {
/* 110 */       throw new NullPointerException("dst");
/*     */     }
/*     */     
/* 113 */     int bytesToCopy = Math.min(capacity() - index, dst.remaining());
/* 114 */     ByteBuffer tmpBuf = internalNioBuffer();
/* 115 */     tmpBuf.clear().position(index).limit(index + bytesToCopy);
/* 116 */     dst.put(tmpBuf);
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf copy(int index, int length)
/*     */   {
/* 122 */     checkIndex(index, length);
/* 123 */     ByteBuf copy = alloc().directBuffer(length, maxCapacity());
/* 124 */     if (length != 0) {
/* 125 */       if (copy.hasMemoryAddress()) {
/* 126 */         PlatformDependent.copyMemory(addr(index), copy.memoryAddress(), length);
/* 127 */         copy.setIndex(0, length);
/*     */       } else {
/* 129 */         copy.writeBytes(this, index, length);
/*     */       }
/*     */     }
/* 132 */     return copy;
/*     */   }
/*     */   
/*     */   private long addr(int index) {
/* 136 */     return this.memoryAddress + index;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\ReadOnlyUnsafeDirectByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */