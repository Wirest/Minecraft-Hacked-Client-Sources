// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import java.nio.charset.Charset;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import io.netty.util.ReferenceCounted;

public abstract class ByteBuf implements ReferenceCounted, Comparable<ByteBuf>
{
    public abstract int capacity();
    
    public abstract ByteBuf capacity(final int p0);
    
    public abstract int maxCapacity();
    
    public abstract ByteBufAllocator alloc();
    
    public abstract ByteOrder order();
    
    public abstract ByteBuf order(final ByteOrder p0);
    
    public abstract ByteBuf unwrap();
    
    public abstract boolean isDirect();
    
    public abstract int readerIndex();
    
    public abstract ByteBuf readerIndex(final int p0);
    
    public abstract int writerIndex();
    
    public abstract ByteBuf writerIndex(final int p0);
    
    public abstract ByteBuf setIndex(final int p0, final int p1);
    
    public abstract int readableBytes();
    
    public abstract int writableBytes();
    
    public abstract int maxWritableBytes();
    
    public abstract boolean isReadable();
    
    public abstract boolean isReadable(final int p0);
    
    public abstract boolean isWritable();
    
    public abstract boolean isWritable(final int p0);
    
    public abstract ByteBuf clear();
    
    public abstract ByteBuf markReaderIndex();
    
    public abstract ByteBuf resetReaderIndex();
    
    public abstract ByteBuf markWriterIndex();
    
    public abstract ByteBuf resetWriterIndex();
    
    public abstract ByteBuf discardReadBytes();
    
    public abstract ByteBuf discardSomeReadBytes();
    
    public abstract ByteBuf ensureWritable(final int p0);
    
    public abstract int ensureWritable(final int p0, final boolean p1);
    
    public abstract boolean getBoolean(final int p0);
    
    public abstract byte getByte(final int p0);
    
    public abstract short getUnsignedByte(final int p0);
    
    public abstract short getShort(final int p0);
    
    public abstract int getUnsignedShort(final int p0);
    
    public abstract int getMedium(final int p0);
    
    public abstract int getUnsignedMedium(final int p0);
    
    public abstract int getInt(final int p0);
    
    public abstract long getUnsignedInt(final int p0);
    
    public abstract long getLong(final int p0);
    
    public abstract char getChar(final int p0);
    
    public abstract float getFloat(final int p0);
    
    public abstract double getDouble(final int p0);
    
    public abstract ByteBuf getBytes(final int p0, final ByteBuf p1);
    
    public abstract ByteBuf getBytes(final int p0, final ByteBuf p1, final int p2);
    
    public abstract ByteBuf getBytes(final int p0, final ByteBuf p1, final int p2, final int p3);
    
    public abstract ByteBuf getBytes(final int p0, final byte[] p1);
    
    public abstract ByteBuf getBytes(final int p0, final byte[] p1, final int p2, final int p3);
    
    public abstract ByteBuf getBytes(final int p0, final ByteBuffer p1);
    
    public abstract ByteBuf getBytes(final int p0, final OutputStream p1, final int p2) throws IOException;
    
    public abstract int getBytes(final int p0, final GatheringByteChannel p1, final int p2) throws IOException;
    
    public abstract ByteBuf setBoolean(final int p0, final boolean p1);
    
    public abstract ByteBuf setByte(final int p0, final int p1);
    
    public abstract ByteBuf setShort(final int p0, final int p1);
    
    public abstract ByteBuf setMedium(final int p0, final int p1);
    
    public abstract ByteBuf setInt(final int p0, final int p1);
    
    public abstract ByteBuf setLong(final int p0, final long p1);
    
    public abstract ByteBuf setChar(final int p0, final int p1);
    
    public abstract ByteBuf setFloat(final int p0, final float p1);
    
    public abstract ByteBuf setDouble(final int p0, final double p1);
    
    public abstract ByteBuf setBytes(final int p0, final ByteBuf p1);
    
    public abstract ByteBuf setBytes(final int p0, final ByteBuf p1, final int p2);
    
    public abstract ByteBuf setBytes(final int p0, final ByteBuf p1, final int p2, final int p3);
    
    public abstract ByteBuf setBytes(final int p0, final byte[] p1);
    
    public abstract ByteBuf setBytes(final int p0, final byte[] p1, final int p2, final int p3);
    
    public abstract ByteBuf setBytes(final int p0, final ByteBuffer p1);
    
    public abstract int setBytes(final int p0, final InputStream p1, final int p2) throws IOException;
    
    public abstract int setBytes(final int p0, final ScatteringByteChannel p1, final int p2) throws IOException;
    
    public abstract ByteBuf setZero(final int p0, final int p1);
    
    public abstract boolean readBoolean();
    
    public abstract byte readByte();
    
    public abstract short readUnsignedByte();
    
    public abstract short readShort();
    
    public abstract int readUnsignedShort();
    
    public abstract int readMedium();
    
    public abstract int readUnsignedMedium();
    
    public abstract int readInt();
    
    public abstract long readUnsignedInt();
    
    public abstract long readLong();
    
    public abstract char readChar();
    
    public abstract float readFloat();
    
    public abstract double readDouble();
    
    public abstract ByteBuf readBytes(final int p0);
    
    public abstract ByteBuf readSlice(final int p0);
    
    public abstract ByteBuf readBytes(final ByteBuf p0);
    
    public abstract ByteBuf readBytes(final ByteBuf p0, final int p1);
    
    public abstract ByteBuf readBytes(final ByteBuf p0, final int p1, final int p2);
    
    public abstract ByteBuf readBytes(final byte[] p0);
    
    public abstract ByteBuf readBytes(final byte[] p0, final int p1, final int p2);
    
    public abstract ByteBuf readBytes(final ByteBuffer p0);
    
    public abstract ByteBuf readBytes(final OutputStream p0, final int p1) throws IOException;
    
    public abstract int readBytes(final GatheringByteChannel p0, final int p1) throws IOException;
    
    public abstract ByteBuf skipBytes(final int p0);
    
    public abstract ByteBuf writeBoolean(final boolean p0);
    
    public abstract ByteBuf writeByte(final int p0);
    
    public abstract ByteBuf writeShort(final int p0);
    
    public abstract ByteBuf writeMedium(final int p0);
    
    public abstract ByteBuf writeInt(final int p0);
    
    public abstract ByteBuf writeLong(final long p0);
    
    public abstract ByteBuf writeChar(final int p0);
    
    public abstract ByteBuf writeFloat(final float p0);
    
    public abstract ByteBuf writeDouble(final double p0);
    
    public abstract ByteBuf writeBytes(final ByteBuf p0);
    
    public abstract ByteBuf writeBytes(final ByteBuf p0, final int p1);
    
    public abstract ByteBuf writeBytes(final ByteBuf p0, final int p1, final int p2);
    
    public abstract ByteBuf writeBytes(final byte[] p0);
    
    public abstract ByteBuf writeBytes(final byte[] p0, final int p1, final int p2);
    
    public abstract ByteBuf writeBytes(final ByteBuffer p0);
    
    public abstract int writeBytes(final InputStream p0, final int p1) throws IOException;
    
    public abstract int writeBytes(final ScatteringByteChannel p0, final int p1) throws IOException;
    
    public abstract ByteBuf writeZero(final int p0);
    
    public abstract int indexOf(final int p0, final int p1, final byte p2);
    
    public abstract int bytesBefore(final byte p0);
    
    public abstract int bytesBefore(final int p0, final byte p1);
    
    public abstract int bytesBefore(final int p0, final int p1, final byte p2);
    
    public abstract int forEachByte(final ByteBufProcessor p0);
    
    public abstract int forEachByte(final int p0, final int p1, final ByteBufProcessor p2);
    
    public abstract int forEachByteDesc(final ByteBufProcessor p0);
    
    public abstract int forEachByteDesc(final int p0, final int p1, final ByteBufProcessor p2);
    
    public abstract ByteBuf copy();
    
    public abstract ByteBuf copy(final int p0, final int p1);
    
    public abstract ByteBuf slice();
    
    public abstract ByteBuf slice(final int p0, final int p1);
    
    public abstract ByteBuf duplicate();
    
    public abstract int nioBufferCount();
    
    public abstract ByteBuffer nioBuffer();
    
    public abstract ByteBuffer nioBuffer(final int p0, final int p1);
    
    public abstract ByteBuffer internalNioBuffer(final int p0, final int p1);
    
    public abstract ByteBuffer[] nioBuffers();
    
    public abstract ByteBuffer[] nioBuffers(final int p0, final int p1);
    
    public abstract boolean hasArray();
    
    public abstract byte[] array();
    
    public abstract int arrayOffset();
    
    public abstract boolean hasMemoryAddress();
    
    public abstract long memoryAddress();
    
    public abstract String toString(final Charset p0);
    
    public abstract String toString(final int p0, final int p1, final Charset p2);
    
    @Override
    public abstract int hashCode();
    
    @Override
    public abstract boolean equals(final Object p0);
    
    @Override
    public abstract int compareTo(final ByteBuf p0);
    
    @Override
    public abstract String toString();
    
    @Override
    public abstract ByteBuf retain(final int p0);
    
    @Override
    public abstract ByteBuf retain();
}
