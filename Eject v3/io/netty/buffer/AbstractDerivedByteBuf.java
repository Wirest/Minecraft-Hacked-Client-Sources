package io.netty.buffer;

import java.nio.ByteBuffer;

public abstract class AbstractDerivedByteBuf
        extends AbstractByteBuf {
    protected AbstractDerivedByteBuf(int paramInt) {
        super(paramInt);
    }

    public final int refCnt() {
        return unwrap().refCnt();
    }

    public final ByteBuf retain() {
        unwrap().retain();
        return this;
    }

    public final ByteBuf retain(int paramInt) {
        unwrap().retain(paramInt);
        return this;
    }

    public final boolean release() {
        return unwrap().release();
    }

    public final boolean release(int paramInt) {
        return unwrap().release(paramInt);
    }

    public ByteBuffer internalNioBuffer(int paramInt1, int paramInt2) {
        return nioBuffer(paramInt1, paramInt2);
    }

    public ByteBuffer nioBuffer(int paramInt1, int paramInt2) {
        return unwrap().nioBuffer(paramInt1, paramInt2);
    }
}




