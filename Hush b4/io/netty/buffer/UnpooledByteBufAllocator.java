// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;

public final class UnpooledByteBufAllocator extends AbstractByteBufAllocator
{
    public static final UnpooledByteBufAllocator DEFAULT;
    
    public UnpooledByteBufAllocator(final boolean preferDirect) {
        super(preferDirect);
    }
    
    @Override
    protected ByteBuf newHeapBuffer(final int initialCapacity, final int maxCapacity) {
        return new UnpooledHeapByteBuf(this, initialCapacity, maxCapacity);
    }
    
    @Override
    protected ByteBuf newDirectBuffer(final int initialCapacity, final int maxCapacity) {
        ByteBuf buf;
        if (PlatformDependent.hasUnsafe()) {
            buf = new UnpooledUnsafeDirectByteBuf(this, initialCapacity, maxCapacity);
        }
        else {
            buf = new UnpooledDirectByteBuf(this, initialCapacity, maxCapacity);
        }
        return AbstractByteBufAllocator.toLeakAwareBuffer(buf);
    }
    
    @Override
    public boolean isDirectBufferPooled() {
        return false;
    }
    
    static {
        DEFAULT = new UnpooledByteBufAllocator(PlatformDependent.directBufferPreferred());
    }
}
