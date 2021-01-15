/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
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
/*     */ final class UnsafeDirectSwappedByteBuf
/*     */   extends SwappedByteBuf
/*     */ {
/*  27 */   private static final boolean NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
/*     */   private final boolean nativeByteOrder;
/*     */   private final AbstractByteBuf wrapped;
/*     */   
/*     */   UnsafeDirectSwappedByteBuf(AbstractByteBuf buf) {
/*  32 */     super(buf);
/*  33 */     this.wrapped = buf;
/*  34 */     this.nativeByteOrder = (NATIVE_ORDER == (order() == ByteOrder.BIG_ENDIAN));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private long addr(int index)
/*     */   {
/*  42 */     return this.wrapped.memoryAddress() + index;
/*     */   }
/*     */   
/*     */   public long getLong(int index)
/*     */   {
/*  47 */     this.wrapped.checkIndex(index, 8);
/*  48 */     long v = PlatformDependent.getLong(addr(index));
/*  49 */     return this.nativeByteOrder ? v : Long.reverseBytes(v);
/*     */   }
/*     */   
/*     */   public float getFloat(int index)
/*     */   {
/*  54 */     return Float.intBitsToFloat(getInt(index));
/*     */   }
/*     */   
/*     */   public double getDouble(int index)
/*     */   {
/*  59 */     return Double.longBitsToDouble(getLong(index));
/*     */   }
/*     */   
/*     */   public char getChar(int index)
/*     */   {
/*  64 */     return (char)getShort(index);
/*     */   }
/*     */   
/*     */   public long getUnsignedInt(int index)
/*     */   {
/*  69 */     return getInt(index) & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public int getInt(int index)
/*     */   {
/*  74 */     this.wrapped.checkIndex(index, 4);
/*  75 */     int v = PlatformDependent.getInt(addr(index));
/*  76 */     return this.nativeByteOrder ? v : Integer.reverseBytes(v);
/*     */   }
/*     */   
/*     */   public int getUnsignedShort(int index)
/*     */   {
/*  81 */     return getShort(index) & 0xFFFF;
/*     */   }
/*     */   
/*     */   public short getShort(int index)
/*     */   {
/*  86 */     this.wrapped.checkIndex(index, 2);
/*  87 */     short v = PlatformDependent.getShort(addr(index));
/*  88 */     return this.nativeByteOrder ? v : Short.reverseBytes(v);
/*     */   }
/*     */   
/*     */   public ByteBuf setShort(int index, int value)
/*     */   {
/*  93 */     this.wrapped.checkIndex(index, 2);
/*  94 */     _setShort(index, value);
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setInt(int index, int value)
/*     */   {
/* 100 */     this.wrapped.checkIndex(index, 4);
/* 101 */     _setInt(index, value);
/* 102 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setLong(int index, long value)
/*     */   {
/* 107 */     this.wrapped.checkIndex(index, 8);
/* 108 */     _setLong(index, value);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setChar(int index, int value)
/*     */   {
/* 114 */     setShort(index, value);
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setFloat(int index, float value)
/*     */   {
/* 120 */     setInt(index, Float.floatToRawIntBits(value));
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf setDouble(int index, double value)
/*     */   {
/* 126 */     setLong(index, Double.doubleToRawLongBits(value));
/* 127 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeShort(int value)
/*     */   {
/* 132 */     this.wrapped.ensureAccessible();
/* 133 */     this.wrapped.ensureWritable(2);
/* 134 */     _setShort(this.wrapped.writerIndex, value);
/* 135 */     this.wrapped.writerIndex += 2;
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeInt(int value)
/*     */   {
/* 141 */     this.wrapped.ensureAccessible();
/* 142 */     this.wrapped.ensureWritable(4);
/* 143 */     _setInt(this.wrapped.writerIndex, value);
/* 144 */     this.wrapped.writerIndex += 4;
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeLong(long value)
/*     */   {
/* 150 */     this.wrapped.ensureAccessible();
/* 151 */     this.wrapped.ensureWritable(8);
/* 152 */     _setLong(this.wrapped.writerIndex, value);
/* 153 */     this.wrapped.writerIndex += 8;
/* 154 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeChar(int value)
/*     */   {
/* 159 */     writeShort(value);
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeFloat(float value)
/*     */   {
/* 165 */     writeInt(Float.floatToRawIntBits(value));
/* 166 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf writeDouble(double value)
/*     */   {
/* 171 */     writeLong(Double.doubleToRawLongBits(value));
/* 172 */     return this;
/*     */   }
/*     */   
/*     */   private void _setShort(int index, int value) {
/* 176 */     PlatformDependent.putShort(addr(index), this.nativeByteOrder ? (short)value : Short.reverseBytes((short)value));
/*     */   }
/*     */   
/*     */   private void _setInt(int index, int value) {
/* 180 */     PlatformDependent.putInt(addr(index), this.nativeByteOrder ? value : Integer.reverseBytes(value));
/*     */   }
/*     */   
/*     */   private void _setLong(int index, long value) {
/* 184 */     PlatformDependent.putLong(addr(index), this.nativeByteOrder ? value : Long.reverseBytes(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\UnsafeDirectSwappedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */