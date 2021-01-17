// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import java.nio.charset.Charset;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import io.netty.util.ResourceLeak;

final class AdvancedLeakAwareByteBuf extends WrappedByteBuf
{
    private final ResourceLeak leak;
    
    AdvancedLeakAwareByteBuf(final ByteBuf buf, final ResourceLeak leak) {
        super(buf);
        this.leak = leak;
    }
    
    @Override
    public boolean release() {
        final boolean deallocated = super.release();
        if (deallocated) {
            this.leak.close();
        }
        else {
            this.leak.record();
        }
        return deallocated;
    }
    
    @Override
    public boolean release(final int decrement) {
        final boolean deallocated = super.release(decrement);
        if (deallocated) {
            this.leak.close();
        }
        else {
            this.leak.record();
        }
        return deallocated;
    }
    
    @Override
    public ByteBuf order(final ByteOrder endianness) {
        this.leak.record();
        if (this.order() == endianness) {
            return this;
        }
        return new AdvancedLeakAwareByteBuf(super.order(endianness), this.leak);
    }
    
    @Override
    public ByteBuf slice() {
        this.leak.record();
        return new AdvancedLeakAwareByteBuf(super.slice(), this.leak);
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        this.leak.record();
        return new AdvancedLeakAwareByteBuf(super.slice(index, length), this.leak);
    }
    
    @Override
    public ByteBuf duplicate() {
        this.leak.record();
        return new AdvancedLeakAwareByteBuf(super.duplicate(), this.leak);
    }
    
    @Override
    public ByteBuf readSlice(final int length) {
        this.leak.record();
        return new AdvancedLeakAwareByteBuf(super.readSlice(length), this.leak);
    }
    
    @Override
    public ByteBuf discardReadBytes() {
        this.leak.record();
        return super.discardReadBytes();
    }
    
    @Override
    public ByteBuf discardSomeReadBytes() {
        this.leak.record();
        return super.discardSomeReadBytes();
    }
    
    @Override
    public ByteBuf ensureWritable(final int minWritableBytes) {
        this.leak.record();
        return super.ensureWritable(minWritableBytes);
    }
    
    @Override
    public int ensureWritable(final int minWritableBytes, final boolean force) {
        this.leak.record();
        return super.ensureWritable(minWritableBytes, force);
    }
    
    @Override
    public boolean getBoolean(final int index) {
        this.leak.record();
        return super.getBoolean(index);
    }
    
    @Override
    public byte getByte(final int index) {
        this.leak.record();
        return super.getByte(index);
    }
    
    @Override
    public short getUnsignedByte(final int index) {
        this.leak.record();
        return super.getUnsignedByte(index);
    }
    
    @Override
    public short getShort(final int index) {
        this.leak.record();
        return super.getShort(index);
    }
    
    @Override
    public int getUnsignedShort(final int index) {
        this.leak.record();
        return super.getUnsignedShort(index);
    }
    
    @Override
    public int getMedium(final int index) {
        this.leak.record();
        return super.getMedium(index);
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        this.leak.record();
        return super.getUnsignedMedium(index);
    }
    
    @Override
    public int getInt(final int index) {
        this.leak.record();
        return super.getInt(index);
    }
    
    @Override
    public long getUnsignedInt(final int index) {
        this.leak.record();
        return super.getUnsignedInt(index);
    }
    
    @Override
    public long getLong(final int index) {
        this.leak.record();
        return super.getLong(index);
    }
    
    @Override
    public char getChar(final int index) {
        this.leak.record();
        return super.getChar(index);
    }
    
    @Override
    public float getFloat(final int index) {
        this.leak.record();
        return super.getFloat(index);
    }
    
    @Override
    public double getDouble(final int index) {
        this.leak.record();
        return super.getDouble(index);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst) {
        this.leak.record();
        return super.getBytes(index, dst);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int length) {
        this.leak.record();
        return super.getBytes(index, dst, length);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.leak.record();
        return super.getBytes(index, dst, dstIndex, length);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst) {
        this.leak.record();
        return super.getBytes(index, dst);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.leak.record();
        return super.getBytes(index, dst, dstIndex, length);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.leak.record();
        return super.getBytes(index, dst);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.leak.record();
        return super.getBytes(index, out, length);
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        this.leak.record();
        return super.getBytes(index, out, length);
    }
    
    @Override
    public ByteBuf setBoolean(final int index, final boolean value) {
        this.leak.record();
        return super.setBoolean(index, value);
    }
    
    @Override
    public ByteBuf setByte(final int index, final int value) {
        this.leak.record();
        return super.setByte(index, value);
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        this.leak.record();
        return super.setShort(index, value);
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        this.leak.record();
        return super.setMedium(index, value);
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        this.leak.record();
        return super.setInt(index, value);
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        this.leak.record();
        return super.setLong(index, value);
    }
    
    @Override
    public ByteBuf setChar(final int index, final int value) {
        this.leak.record();
        return super.setChar(index, value);
    }
    
    @Override
    public ByteBuf setFloat(final int index, final float value) {
        this.leak.record();
        return super.setFloat(index, value);
    }
    
    @Override
    public ByteBuf setDouble(final int index, final double value) {
        this.leak.record();
        return super.setDouble(index, value);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src) {
        this.leak.record();
        return super.setBytes(index, src);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int length) {
        this.leak.record();
        return super.setBytes(index, src, length);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        this.leak.record();
        return super.setBytes(index, src, srcIndex, length);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src) {
        this.leak.record();
        return super.setBytes(index, src);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        this.leak.record();
        return super.setBytes(index, src, srcIndex, length);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuffer src) {
        this.leak.record();
        return super.setBytes(index, src);
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        this.leak.record();
        return super.setBytes(index, in, length);
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) throws IOException {
        this.leak.record();
        return super.setBytes(index, in, length);
    }
    
    @Override
    public ByteBuf setZero(final int index, final int length) {
        this.leak.record();
        return super.setZero(index, length);
    }
    
    @Override
    public boolean readBoolean() {
        this.leak.record();
        return super.readBoolean();
    }
    
    @Override
    public byte readByte() {
        this.leak.record();
        return super.readByte();
    }
    
    @Override
    public short readUnsignedByte() {
        this.leak.record();
        return super.readUnsignedByte();
    }
    
    @Override
    public short readShort() {
        this.leak.record();
        return super.readShort();
    }
    
    @Override
    public int readUnsignedShort() {
        this.leak.record();
        return super.readUnsignedShort();
    }
    
    @Override
    public int readMedium() {
        this.leak.record();
        return super.readMedium();
    }
    
    @Override
    public int readUnsignedMedium() {
        this.leak.record();
        return super.readUnsignedMedium();
    }
    
    @Override
    public int readInt() {
        this.leak.record();
        return super.readInt();
    }
    
    @Override
    public long readUnsignedInt() {
        this.leak.record();
        return super.readUnsignedInt();
    }
    
    @Override
    public long readLong() {
        this.leak.record();
        return super.readLong();
    }
    
    @Override
    public char readChar() {
        this.leak.record();
        return super.readChar();
    }
    
    @Override
    public float readFloat() {
        this.leak.record();
        return super.readFloat();
    }
    
    @Override
    public double readDouble() {
        this.leak.record();
        return super.readDouble();
    }
    
    @Override
    public ByteBuf readBytes(final int length) {
        this.leak.record();
        return super.readBytes(length);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst) {
        this.leak.record();
        return super.readBytes(dst);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst, final int length) {
        this.leak.record();
        return super.readBytes(dst, length);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst, final int dstIndex, final int length) {
        this.leak.record();
        return super.readBytes(dst, dstIndex, length);
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst) {
        this.leak.record();
        return super.readBytes(dst);
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst, final int dstIndex, final int length) {
        this.leak.record();
        return super.readBytes(dst, dstIndex, length);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuffer dst) {
        this.leak.record();
        return super.readBytes(dst);
    }
    
    @Override
    public ByteBuf readBytes(final OutputStream out, final int length) throws IOException {
        this.leak.record();
        return super.readBytes(out, length);
    }
    
    @Override
    public int readBytes(final GatheringByteChannel out, final int length) throws IOException {
        this.leak.record();
        return super.readBytes(out, length);
    }
    
    @Override
    public ByteBuf skipBytes(final int length) {
        this.leak.record();
        return super.skipBytes(length);
    }
    
    @Override
    public ByteBuf writeBoolean(final boolean value) {
        this.leak.record();
        return super.writeBoolean(value);
    }
    
    @Override
    public ByteBuf writeByte(final int value) {
        this.leak.record();
        return super.writeByte(value);
    }
    
    @Override
    public ByteBuf writeShort(final int value) {
        this.leak.record();
        return super.writeShort(value);
    }
    
    @Override
    public ByteBuf writeMedium(final int value) {
        this.leak.record();
        return super.writeMedium(value);
    }
    
    @Override
    public ByteBuf writeInt(final int value) {
        this.leak.record();
        return super.writeInt(value);
    }
    
    @Override
    public ByteBuf writeLong(final long value) {
        this.leak.record();
        return super.writeLong(value);
    }
    
    @Override
    public ByteBuf writeChar(final int value) {
        this.leak.record();
        return super.writeChar(value);
    }
    
    @Override
    public ByteBuf writeFloat(final float value) {
        this.leak.record();
        return super.writeFloat(value);
    }
    
    @Override
    public ByteBuf writeDouble(final double value) {
        this.leak.record();
        return super.writeDouble(value);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src) {
        this.leak.record();
        return super.writeBytes(src);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src, final int length) {
        this.leak.record();
        return super.writeBytes(src, length);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src, final int srcIndex, final int length) {
        this.leak.record();
        return super.writeBytes(src, srcIndex, length);
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] src) {
        this.leak.record();
        return super.writeBytes(src);
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] src, final int srcIndex, final int length) {
        this.leak.record();
        return super.writeBytes(src, srcIndex, length);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuffer src) {
        this.leak.record();
        return super.writeBytes(src);
    }
    
    @Override
    public int writeBytes(final InputStream in, final int length) throws IOException {
        this.leak.record();
        return super.writeBytes(in, length);
    }
    
    @Override
    public int writeBytes(final ScatteringByteChannel in, final int length) throws IOException {
        this.leak.record();
        return super.writeBytes(in, length);
    }
    
    @Override
    public ByteBuf writeZero(final int length) {
        this.leak.record();
        return super.writeZero(length);
    }
    
    @Override
    public int indexOf(final int fromIndex, final int toIndex, final byte value) {
        this.leak.record();
        return super.indexOf(fromIndex, toIndex, value);
    }
    
    @Override
    public int bytesBefore(final byte value) {
        this.leak.record();
        return super.bytesBefore(value);
    }
    
    @Override
    public int bytesBefore(final int length, final byte value) {
        this.leak.record();
        return super.bytesBefore(length, value);
    }
    
    @Override
    public int bytesBefore(final int index, final int length, final byte value) {
        this.leak.record();
        return super.bytesBefore(index, length, value);
    }
    
    @Override
    public int forEachByte(final ByteBufProcessor processor) {
        this.leak.record();
        return super.forEachByte(processor);
    }
    
    @Override
    public int forEachByte(final int index, final int length, final ByteBufProcessor processor) {
        this.leak.record();
        return super.forEachByte(index, length, processor);
    }
    
    @Override
    public int forEachByteDesc(final ByteBufProcessor processor) {
        this.leak.record();
        return super.forEachByteDesc(processor);
    }
    
    @Override
    public int forEachByteDesc(final int index, final int length, final ByteBufProcessor processor) {
        this.leak.record();
        return super.forEachByteDesc(index, length, processor);
    }
    
    @Override
    public ByteBuf copy() {
        this.leak.record();
        return super.copy();
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        this.leak.record();
        return super.copy(index, length);
    }
    
    @Override
    public int nioBufferCount() {
        this.leak.record();
        return super.nioBufferCount();
    }
    
    @Override
    public ByteBuffer nioBuffer() {
        this.leak.record();
        return super.nioBuffer();
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        this.leak.record();
        return super.nioBuffer(index, length);
    }
    
    @Override
    public ByteBuffer[] nioBuffers() {
        this.leak.record();
        return super.nioBuffers();
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        this.leak.record();
        return super.nioBuffers(index, length);
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        this.leak.record();
        return super.internalNioBuffer(index, length);
    }
    
    @Override
    public String toString(final Charset charset) {
        this.leak.record();
        return super.toString(charset);
    }
    
    @Override
    public String toString(final int index, final int length, final Charset charset) {
        this.leak.record();
        return super.toString(index, length, charset);
    }
    
    @Override
    public ByteBuf retain() {
        this.leak.record();
        return super.retain();
    }
    
    @Override
    public ByteBuf retain(final int increment) {
        this.leak.record();
        return super.retain(increment);
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        this.leak.record();
        return super.capacity(newCapacity);
    }
}
