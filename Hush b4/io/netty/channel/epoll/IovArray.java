// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.channel.ChannelOutboundBuffer;

final class IovArray implements ChannelOutboundBuffer.MessageProcessor
{
    private static final int ADDRESS_SIZE;
    private static final int IOV_SIZE;
    private static final int CAPACITY;
    private static final FastThreadLocal<IovArray> ARRAY;
    private final long memoryAddress;
    private int count;
    private long size;
    
    private IovArray() {
        this.memoryAddress = PlatformDependent.allocateMemory(IovArray.CAPACITY);
    }
    
    private boolean add(final ByteBuf buf) {
        if (this.count == Native.IOV_MAX) {
            return false;
        }
        final int len = buf.readableBytes();
        if (len == 0) {
            return true;
        }
        final long addr = buf.memoryAddress();
        final int offset = buf.readerIndex();
        final long baseOffset = this.memoryAddress(this.count++);
        final long lengthOffset = baseOffset + IovArray.ADDRESS_SIZE;
        if (IovArray.ADDRESS_SIZE == 8) {
            PlatformDependent.putLong(baseOffset, addr + offset);
            PlatformDependent.putLong(lengthOffset, len);
        }
        else {
            assert IovArray.ADDRESS_SIZE == 4;
            PlatformDependent.putInt(baseOffset, (int)addr + offset);
            PlatformDependent.putInt(lengthOffset, len);
        }
        this.size += len;
        return true;
    }
    
    long processWritten(final int index, final long written) {
        final long baseOffset = this.memoryAddress(index);
        final long lengthOffset = baseOffset + IovArray.ADDRESS_SIZE;
        if (IovArray.ADDRESS_SIZE == 8) {
            final long len = PlatformDependent.getLong(lengthOffset);
            if (len > written) {
                final long offset = PlatformDependent.getLong(baseOffset);
                PlatformDependent.putLong(baseOffset, offset + written);
                PlatformDependent.putLong(lengthOffset, len - written);
                return -1L;
            }
            return len;
        }
        else {
            assert IovArray.ADDRESS_SIZE == 4;
            final long len = PlatformDependent.getInt(lengthOffset);
            if (len > written) {
                final int offset2 = PlatformDependent.getInt(baseOffset);
                PlatformDependent.putInt(baseOffset, (int)(offset2 + written));
                PlatformDependent.putInt(lengthOffset, (int)(len - written));
                return -1L;
            }
            return len;
        }
    }
    
    int count() {
        return this.count;
    }
    
    long size() {
        return this.size;
    }
    
    long memoryAddress(final int offset) {
        return this.memoryAddress + IovArray.IOV_SIZE * offset;
    }
    
    @Override
    public boolean processMessage(final Object msg) throws Exception {
        return msg instanceof ByteBuf && this.add((ByteBuf)msg);
    }
    
    static IovArray get(final ChannelOutboundBuffer buffer) throws Exception {
        final IovArray array = IovArray.ARRAY.get();
        array.size = 0L;
        array.count = 0;
        buffer.forEachFlushedMessage(array);
        return array;
    }
    
    static {
        ADDRESS_SIZE = PlatformDependent.addressSize();
        IOV_SIZE = 2 * IovArray.ADDRESS_SIZE;
        CAPACITY = Native.IOV_MAX * IovArray.IOV_SIZE;
        ARRAY = new FastThreadLocal<IovArray>() {
            @Override
            protected IovArray initialValue() throws Exception {
                return new IovArray(null);
            }
            
            @Override
            protected void onRemoval(final IovArray value) throws Exception {
                PlatformDependent.freeMemory(value.memoryAddress);
            }
        };
    }
}
