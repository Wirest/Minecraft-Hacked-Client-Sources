// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import sun.misc.Unsafe;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

final class UnsafeAtomicIntegerFieldUpdater<T> extends AtomicIntegerFieldUpdater<T>
{
    private final long offset;
    private final Unsafe unsafe;
    
    UnsafeAtomicIntegerFieldUpdater(final Unsafe unsafe, final Class<?> tClass, final String fieldName) throws NoSuchFieldException {
        final Field field = tClass.getDeclaredField(fieldName);
        if (!Modifier.isVolatile(field.getModifiers())) {
            throw new IllegalArgumentException("Must be volatile");
        }
        this.unsafe = unsafe;
        this.offset = unsafe.objectFieldOffset(field);
    }
    
    @Override
    public boolean compareAndSet(final T obj, final int expect, final int update) {
        return this.unsafe.compareAndSwapInt(obj, this.offset, expect, update);
    }
    
    @Override
    public boolean weakCompareAndSet(final T obj, final int expect, final int update) {
        return this.unsafe.compareAndSwapInt(obj, this.offset, expect, update);
    }
    
    @Override
    public void set(final T obj, final int newValue) {
        this.unsafe.putIntVolatile(obj, this.offset, newValue);
    }
    
    @Override
    public void lazySet(final T obj, final int newValue) {
        this.unsafe.putOrderedInt(obj, this.offset, newValue);
    }
    
    @Override
    public int get(final T obj) {
        return this.unsafe.getIntVolatile(obj, this.offset);
    }
}
