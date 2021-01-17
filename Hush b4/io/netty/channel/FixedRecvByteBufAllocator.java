// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class FixedRecvByteBufAllocator implements RecvByteBufAllocator
{
    private final Handle handle;
    
    public FixedRecvByteBufAllocator(final int bufferSize) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("bufferSize must greater than 0: " + bufferSize);
        }
        this.handle = new HandleImpl(bufferSize);
    }
    
    @Override
    public Handle newHandle() {
        return this.handle;
    }
    
    private static final class HandleImpl implements Handle
    {
        private final int bufferSize;
        
        HandleImpl(final int bufferSize) {
            this.bufferSize = bufferSize;
        }
        
        @Override
        public ByteBuf allocate(final ByteBufAllocator alloc) {
            return alloc.ioBuffer(this.bufferSize);
        }
        
        @Override
        public int guess() {
            return this.bufferSize;
        }
        
        @Override
        public void record(final int actualReadBytes) {
        }
    }
}
