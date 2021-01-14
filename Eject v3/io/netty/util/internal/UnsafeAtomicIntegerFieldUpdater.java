package io.netty.util.internal;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

final class UnsafeAtomicIntegerFieldUpdater<T>
        extends AtomicIntegerFieldUpdater<T> {
    private final long offset;
    private final Unsafe unsafe;

    UnsafeAtomicIntegerFieldUpdater(Unsafe paramUnsafe, Class<?> paramClass, String paramString)
            throws NoSuchFieldException {
        Field localField = paramClass.getDeclaredField(paramString);
        if (!Modifier.isVolatile(localField.getModifiers())) {
            throw new IllegalArgumentException("Must be volatile");
        }
        this.unsafe = paramUnsafe;
        this.offset = paramUnsafe.objectFieldOffset(localField);
    }

    public boolean compareAndSet(T paramT, int paramInt1, int paramInt2) {
        return this.unsafe.compareAndSwapInt(paramT, this.offset, paramInt1, paramInt2);
    }

    public boolean weakCompareAndSet(T paramT, int paramInt1, int paramInt2) {
        return this.unsafe.compareAndSwapInt(paramT, this.offset, paramInt1, paramInt2);
    }

    public void set(T paramT, int paramInt) {
        this.unsafe.putIntVolatile(paramT, this.offset, paramInt);
    }

    public void lazySet(T paramT, int paramInt) {
        this.unsafe.putOrderedInt(paramT, this.offset, paramInt);
    }

    public int get(T paramT) {
        return this.unsafe.getIntVolatile(paramT, this.offset);
    }
}




