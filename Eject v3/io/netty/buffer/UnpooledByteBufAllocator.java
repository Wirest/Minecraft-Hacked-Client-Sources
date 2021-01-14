package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;

public final class UnpooledByteBufAllocator
        extends AbstractByteBufAllocator {
    public static final UnpooledByteBufAllocator DEFAULT = new UnpooledByteBufAllocator(PlatformDependent.directBufferPreferred());

    public UnpooledByteBufAllocator(boolean paramBoolean) {
        super(paramBoolean);
    }

    protected ByteBuf newHeapBuffer(int paramInt1, int paramInt2) {
        return new UnpooledHeapByteBuf(this, paramInt1, paramInt2);
    }

    protected ByteBuf newDirectBuffer(int paramInt1, int paramInt2) {
        Object localObject;
        if (PlatformDependent.hasUnsafe()) {
            localObject = new UnpooledUnsafeDirectByteBuf(this, paramInt1, paramInt2);
        } else {
            localObject = new UnpooledDirectByteBuf(this, paramInt1, paramInt2);
        }
        return toLeakAwareBuffer((ByteBuf) localObject);
    }

    public boolean isDirectBufferPooled() {
        return false;
    }
}




