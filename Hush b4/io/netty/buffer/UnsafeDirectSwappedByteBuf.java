// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;
import java.nio.ByteOrder;

final class UnsafeDirectSwappedByteBuf extends SwappedByteBuf
{
    private static final boolean NATIVE_ORDER;
    private final boolean nativeByteOrder;
    private final AbstractByteBuf wrapped;
    
    UnsafeDirectSwappedByteBuf(final AbstractByteBuf buf) {
        super(buf);
        this.wrapped = buf;
        this.nativeByteOrder = (UnsafeDirectSwappedByteBuf.NATIVE_ORDER == (this.order() == ByteOrder.BIG_ENDIAN));
    }
    
    private long addr(final int index) {
        return this.wrapped.memoryAddress() + index;
    }
    
    @Override
    public long getLong(final int index) {
        this.wrapped.checkIndex(index, 8);
        final long v = PlatformDependent.getLong(this.addr(index));
        return this.nativeByteOrder ? v : Long.reverseBytes(v);
    }
    
    @Override
    public float getFloat(final int index) {
        return Float.intBitsToFloat(this.getInt(index));
    }
    
    @Override
    public double getDouble(final int index) {
        return Double.longBitsToDouble(this.getLong(index));
    }
    
    @Override
    public char getChar(final int index) {
        return (char)this.getShort(index);
    }
    
    @Override
    public long getUnsignedInt(final int index) {
        return (long)this.getInt(index) & 0xFFFFFFFFL;
    }
    
    @Override
    public int getInt(final int index) {
        this.wrapped.checkIndex(index, 4);
        final int v = PlatformDependent.getInt(this.addr(index));
        return this.nativeByteOrder ? v : Integer.reverseBytes(v);
    }
    
    @Override
    public int getUnsignedShort(final int index) {
        return this.getShort(index) & 0xFFFF;
    }
    
    @Override
    public short getShort(final int index) {
        this.wrapped.checkIndex(index, 2);
        final short v = PlatformDependent.getShort(this.addr(index));
        return this.nativeByteOrder ? v : Short.reverseBytes(v);
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        this.wrapped.checkIndex(index, 2);
        this._setShort(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        this.wrapped.checkIndex(index, 4);
        this._setInt(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        this.wrapped.checkIndex(index, 8);
        this._setLong(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setChar(final int index, final int value) {
        this.setShort(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setFloat(final int index, final float value) {
        this.setInt(index, Float.floatToRawIntBits(value));
        return this;
    }
    
    @Override
    public ByteBuf setDouble(final int index, final double value) {
        this.setLong(index, Double.doubleToRawLongBits(value));
        return this;
    }
    
    @Override
    public ByteBuf writeShort(final int value) {
        this.wrapped.ensureAccessible();
        this.wrapped.ensureWritable(2);
        this._setShort(this.wrapped.writerIndex, value);
        final AbstractByteBuf wrapped = this.wrapped;
        wrapped.writerIndex += 2;
        return this;
    }
    
    @Override
    public ByteBuf writeInt(final int value) {
        this.wrapped.ensureAccessible();
        this.wrapped.ensureWritable(4);
        this._setInt(this.wrapped.writerIndex, value);
        final AbstractByteBuf wrapped = this.wrapped;
        wrapped.writerIndex += 4;
        return this;
    }
    
    @Override
    public ByteBuf writeLong(final long value) {
        this.wrapped.ensureAccessible();
        this.wrapped.ensureWritable(8);
        this._setLong(this.wrapped.writerIndex, value);
        final AbstractByteBuf wrapped = this.wrapped;
        wrapped.writerIndex += 8;
        return this;
    }
    
    @Override
    public ByteBuf writeChar(final int value) {
        this.writeShort(value);
        return this;
    }
    
    @Override
    public ByteBuf writeFloat(final float value) {
        this.writeInt(Float.floatToRawIntBits(value));
        return this;
    }
    
    @Override
    public ByteBuf writeDouble(final double value) {
        this.writeLong(Double.doubleToRawLongBits(value));
        return this;
    }
    
    private void _setShort(final int index, final int value) {
        PlatformDependent.putShort(this.addr(index), this.nativeByteOrder ? ((short)value) : Short.reverseBytes((short)value));
    }
    
    private void _setInt(final int index, final int value) {
        PlatformDependent.putInt(this.addr(index), this.nativeByteOrder ? value : Integer.reverseBytes(value));
    }
    
    private void _setLong(final int index, final long value) {
        PlatformDependent.putLong(this.addr(index), this.nativeByteOrder ? value : Long.reverseBytes(value));
    }
    
    static {
        NATIVE_ORDER = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
    }
}
