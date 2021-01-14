package io.netty.util.internal;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

final class UnsafeAtomicReferenceFieldUpdater<U, M>
        extends AtomicReferenceFieldUpdater<U, M> {
    private final long offset;
    private final Unsafe unsafe;

    UnsafeAtomicReferenceFieldUpdater(Unsafe paramUnsafe, Class<U> paramClass, String paramString)
            throws NoSuchFieldException {
        Field localField = paramClass.getDeclaredField(paramString);
        if (!Modifier.isVolatile(localField.getModifiers())) {
            throw new IllegalArgumentException("Must be volatile");
        }
        this.unsafe = paramUnsafe;
        this.offset = paramUnsafe.objectFieldOffset(localField);
    }

    public boolean compareAndSet(U paramU, M paramM1, M paramM2) {
        return this.unsafe.compareAndSwapObject(paramU, this.offset, paramM1, paramM2);
    }

    public boolean weakCompareAndSet(U paramU, M paramM1, M paramM2) {
        return this.unsafe.compareAndSwapObject(paramU, this.offset, paramM1, paramM2);
    }

    public void set(U paramU, M paramM) {
        this.unsafe.putObjectVolatile(paramU, this.offset, paramM);
    }

    public void lazySet(U paramU, M paramM) {
        this.unsafe.putOrderedObject(paramU, this.offset, paramM);
    }

    public M get(U paramU) {
        return (M) this.unsafe.getObjectVolatile(paramU, this.offset);
    }
}




