// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.ReferenceCounted;
import io.netty.util.IllegalReferenceCountException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class AbstractReferenceCountedByteBuf extends AbstractByteBuf
{
    private static final AtomicIntegerFieldUpdater<AbstractReferenceCountedByteBuf> refCntUpdater;
    private volatile int refCnt;
    
    protected AbstractReferenceCountedByteBuf(final int maxCapacity) {
        super(maxCapacity);
        this.refCnt = 1;
    }
    
    @Override
    public final int refCnt() {
        return this.refCnt;
    }
    
    protected final void setRefCnt(final int refCnt) {
        this.refCnt = refCnt;
    }
    
    @Override
    public ByteBuf retain() {
        while (true) {
            final int refCnt = this.refCnt;
            if (refCnt == 0) {
                throw new IllegalReferenceCountException(0, 1);
            }
            if (refCnt == Integer.MAX_VALUE) {
                throw new IllegalReferenceCountException(Integer.MAX_VALUE, 1);
            }
            if (AbstractReferenceCountedByteBuf.refCntUpdater.compareAndSet(this, refCnt, refCnt + 1)) {
                return this;
            }
        }
    }
    
    @Override
    public ByteBuf retain(final int increment) {
        if (increment <= 0) {
            throw new IllegalArgumentException("increment: " + increment + " (expected: > 0)");
        }
        while (true) {
            final int refCnt = this.refCnt;
            if (refCnt == 0) {
                throw new IllegalReferenceCountException(0, increment);
            }
            if (refCnt > Integer.MAX_VALUE - increment) {
                throw new IllegalReferenceCountException(refCnt, increment);
            }
            if (AbstractReferenceCountedByteBuf.refCntUpdater.compareAndSet(this, refCnt, refCnt + increment)) {
                return this;
            }
        }
    }
    
    @Override
    public final boolean release() {
        while (true) {
            final int refCnt = this.refCnt;
            if (refCnt == 0) {
                throw new IllegalReferenceCountException(0, -1);
            }
            if (!AbstractReferenceCountedByteBuf.refCntUpdater.compareAndSet(this, refCnt, refCnt - 1)) {
                continue;
            }
            if (refCnt == 1) {
                this.deallocate();
                return true;
            }
            return false;
        }
    }
    
    @Override
    public final boolean release(final int decrement) {
        if (decrement <= 0) {
            throw new IllegalArgumentException("decrement: " + decrement + " (expected: > 0)");
        }
        while (true) {
            final int refCnt = this.refCnt;
            if (refCnt < decrement) {
                throw new IllegalReferenceCountException(refCnt, -decrement);
            }
            if (!AbstractReferenceCountedByteBuf.refCntUpdater.compareAndSet(this, refCnt, refCnt - decrement)) {
                continue;
            }
            if (refCnt == decrement) {
                this.deallocate();
                return true;
            }
            return false;
        }
    }
    
    protected abstract void deallocate();
    
    static {
        AtomicIntegerFieldUpdater<AbstractReferenceCountedByteBuf> updater = PlatformDependent.newAtomicIntegerFieldUpdater(AbstractReferenceCountedByteBuf.class, "refCnt");
        if (updater == null) {
            updater = AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCountedByteBuf.class, "refCnt");
        }
        refCntUpdater = updater;
    }
}
