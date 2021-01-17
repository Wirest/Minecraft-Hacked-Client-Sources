// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public interface RecvByteBufAllocator
{
    Handle newHandle();
    
    public interface Handle
    {
        ByteBuf allocate(final ByteBufAllocator p0);
        
        int guess();
        
        void record(final int p0);
    }
}
