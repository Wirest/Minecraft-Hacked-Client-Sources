package io.netty.buffer;

import io.netty.util.ResourceLeak;

import java.nio.ByteOrder;

final class SimpleLeakAwareByteBuf
        extends WrappedByteBuf {
    private final ResourceLeak leak;

    SimpleLeakAwareByteBuf(ByteBuf paramByteBuf, ResourceLeak paramResourceLeak) {
        super(paramByteBuf);
        this.leak = paramResourceLeak;
    }

    public boolean release() {
        boolean bool = super.release();
        if (bool) {
            this.leak.close();
        }
        return bool;
    }

    public boolean release(int paramInt) {
        boolean bool = super.release(paramInt);
        if (bool) {
            this.leak.close();
        }
        return bool;
    }

    public ByteBuf order(ByteOrder paramByteOrder) {
        this.leak.record();
        if (order() == paramByteOrder) {
            return this;
        }
        return new SimpleLeakAwareByteBuf(super.order(paramByteOrder), this.leak);
    }

    public ByteBuf slice() {
        return new SimpleLeakAwareByteBuf(super.slice(), this.leak);
    }

    public ByteBuf slice(int paramInt1, int paramInt2) {
        return new SimpleLeakAwareByteBuf(super.slice(paramInt1, paramInt2), this.leak);
    }

    public ByteBuf duplicate() {
        return new SimpleLeakAwareByteBuf(super.duplicate(), this.leak);
    }

    public ByteBuf readSlice(int paramInt) {
        return new SimpleLeakAwareByteBuf(super.readSlice(paramInt), this.leak);
    }
}




