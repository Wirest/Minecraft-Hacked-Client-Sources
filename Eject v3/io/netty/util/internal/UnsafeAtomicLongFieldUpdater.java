package io.netty.util.internal;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

final class UnsafeAtomicLongFieldUpdater<T>
        extends AtomicLongFieldUpdater<T> {
    private final long offset;
    private final Unsafe unsafe;

    UnsafeAtomicLongFieldUpdater(Unsafe paramUnsafe, Class<?> paramClass, String paramString)
            throws NoSuchFieldException {
        Field localField = paramClass.getDeclaredField(paramString);
        if (!Modifier.isVolatile(localField.getModifiers())) {
            throw new IllegalArgumentException("Must be volatile");
        }
        this.unsafe = paramUnsafe;
        this.offset = paramUnsafe.objectFieldOffset(localField);
    }

    public boolean compareAndSet(T paramT, long paramLong1, long paramLong2) {
        return this.unsafe.compareAndSwapLong(paramT, this.offset, paramLong1, paramLong2);
    }

    public boolean weakCompareAndSet(T paramT, long paramLong1, long paramLong2) {
        return this.unsafe.compareAndSwapLong(paramT, this.offset, paramLong1, paramLong2);
    }

    public void set(T paramT, long paramLong) {
        this.unsafe.putLongVolatile(paramT, this.offset, paramLong);
    }

    public void lazySet(T paramT, long paramLong) {
        this.unsafe.putOrderedLong(paramT, this.offset, paramLong);
    }

    public long get(T paramT) {
        return this.unsafe.getLongVolatile(paramT, this.offset);
    }
}




