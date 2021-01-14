package io.netty.buffer;

import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class AbstractReferenceCountedByteBuf
        extends AbstractByteBuf {
    private static final AtomicIntegerFieldUpdater<AbstractReferenceCountedByteBuf> refCntUpdater;

    static {
        AtomicIntegerFieldUpdater localAtomicIntegerFieldUpdater = PlatformDependent.newAtomicIntegerFieldUpdater(AbstractReferenceCountedByteBuf.class, "refCnt");
        if (localAtomicIntegerFieldUpdater == null) {
            localAtomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCountedByteBuf.class, "refCnt");
        }
        refCntUpdater = localAtomicIntegerFieldUpdater;
    }

    private volatile int refCnt = 1;

    protected AbstractReferenceCountedByteBuf(int paramInt) {
        super(paramInt);
    }

    public final int refCnt() {
        return this.refCnt;
    }

    protected final void setRefCnt(int paramInt) {
        this.refCnt = paramInt;
    }

    public ByteBuf retain() {
        for (; ; ) {
            int i = this.refCnt;
            if (i == 0) {
                throw new IllegalReferenceCountException(0, 1);
            }
            if (i == Integer.MAX_VALUE) {
                throw new IllegalReferenceCountException(Integer.MAX_VALUE, 1);
            }
            if (refCntUpdater.compareAndSet(this, i, i | 0x1)) {
                break;
            }
        }
        return this;
    }

    public ByteBuf retain(int paramInt) {
        if (paramInt <= 0) {
            throw new IllegalArgumentException("increment: " + paramInt + " (expected: > 0)");
        }
        for (; ; ) {
            int i = this.refCnt;
            if (i == 0) {
                throw new IllegalReferenceCountException(0, paramInt);
            }
            if (i > Integer.MAX_VALUE - paramInt) {
                throw new IllegalReferenceCountException(i, paramInt);
            }
            if (refCntUpdater.compareAndSet(this, i, i | paramInt)) {
                break;
            }
        }
        return this;
    }

    public final boolean release() {
        for (; ; ) {
            int i = this.refCnt;
            if (i == 0) {
                throw new IllegalReferenceCountException(0, -1);
            }
            if (refCntUpdater.compareAndSet(this, i, i - 1)) {
                if (i == 1) {
                    deallocate();
                    return true;
                }
                return false;
            }
        }
    }

    public final boolean release(int arg1) {
        // Byte code:
        //   0: iload_1
        //   1: ifgt +35 -> 36
        //   4: new 36	java/lang/IllegalArgumentException
        //   7: dup
        //   8: new 38	java/lang/StringBuilder
        //   11: dup
        //   12: invokespecial 41	java/lang/StringBuilder:<init>	()V
        //   15: ldc 67
        //   17: invokevirtual 47	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   20: iload_1
        //   21: invokevirtual 50	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
        //   24: ldc 52
        //   26: invokevirtual 47	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   29: invokevirtual 56	java/lang/StringBuilder:toString	()Ljava/lang/String;
        //   32: invokespecial 59	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
        //   35: athrow
        //   36: aload_0
        //   37: getfield 15	io/netty/buffer/AbstractReferenceCountedByteBuf:refCnt	I
        //   40: istore_2
        //   41: iload_2
        //   42: iload_1
        //   43: if_icmpge +14 -> 57
        //   46: new 21	io/netty/util/IllegalReferenceCountException
        //   49: dup
        //   50: iload_2
        //   51: iload_1
        //   52: idiv
        //   53: invokespecial 24	io/netty/util/IllegalReferenceCountException:<init>	(II)V
        //   56: athrow
        //   57: getstatic 27	io/netty/buffer/AbstractReferenceCountedByteBuf:refCntUpdater	Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
        //   60: aload_0
        //   61: iload_2
        //   62: iload_2
        //   63: iload_1
        //   64: isub
        //   65: invokevirtual 33	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:compareAndSet	(Ljava/lang/Object;II)Z
        //   68: ifeq +16 -> 84
        //   71: iload_2
        //   72: iload_1
        //   73: if_icmpne +9 -> 82
        //   76: aload_0
        //   77: invokevirtual 64	io/netty/buffer/AbstractReferenceCountedByteBuf:deallocate	()V
        //   80: iconst_1
        //   81: ireturn
        //   82: iconst_0
        //   83: ireturn
        //   84: goto -48 -> 36
    }

    protected abstract void deallocate();
}




