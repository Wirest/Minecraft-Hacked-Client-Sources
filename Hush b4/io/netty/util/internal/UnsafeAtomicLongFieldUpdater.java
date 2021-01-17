// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import sun.misc.Unsafe;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

final class UnsafeAtomicLongFieldUpdater<T> extends AtomicLongFieldUpdater<T>
{
    private final long offset;
    private final Unsafe unsafe;
    
    UnsafeAtomicLongFieldUpdater(final Unsafe unsafe, final Class<?> tClass, final String fieldName) throws NoSuchFieldException {
        final Field field = tClass.getDeclaredField(fieldName);
        if (!Modifier.isVolatile(field.getModifiers())) {
            throw new IllegalArgumentException("Must be volatile");
        }
        this.unsafe = unsafe;
        this.offset = unsafe.objectFieldOffset(field);
    }
    
    @Override
    public boolean compareAndSet(final T obj, final long expect, final long update) {
        return this.unsafe.compareAndSwapLong(obj, this.offset, expect, update);
    }
    
    @Override
    public boolean weakCompareAndSet(final T obj, final long expect, final long update) {
        return this.unsafe.compareAndSwapLong(obj, this.offset, expect, update);
    }
    
    @Override
    public void set(final T obj, final long newValue) {
        this.unsafe.putLongVolatile(obj, this.offset, newValue);
    }
    
    @Override
    public void lazySet(final T obj, final long newValue) {
        this.unsafe.putOrderedLong(obj, this.offset, newValue);
    }
    
    @Override
    public long get(final T obj) {
        return this.unsafe.getLongVolatile(obj, this.offset);
    }
}
