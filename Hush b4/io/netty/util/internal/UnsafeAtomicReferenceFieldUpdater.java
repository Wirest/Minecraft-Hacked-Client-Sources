// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import sun.misc.Unsafe;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

final class UnsafeAtomicReferenceFieldUpdater<U, M> extends AtomicReferenceFieldUpdater<U, M>
{
    private final long offset;
    private final Unsafe unsafe;
    
    UnsafeAtomicReferenceFieldUpdater(final Unsafe unsafe, final Class<U> tClass, final String fieldName) throws NoSuchFieldException {
        final Field field = tClass.getDeclaredField(fieldName);
        if (!Modifier.isVolatile(field.getModifiers())) {
            throw new IllegalArgumentException("Must be volatile");
        }
        this.unsafe = unsafe;
        this.offset = unsafe.objectFieldOffset(field);
    }
    
    @Override
    public boolean compareAndSet(final U obj, final M expect, final M update) {
        return this.unsafe.compareAndSwapObject(obj, this.offset, expect, update);
    }
    
    @Override
    public boolean weakCompareAndSet(final U obj, final M expect, final M update) {
        return this.unsafe.compareAndSwapObject(obj, this.offset, expect, update);
    }
    
    @Override
    public void set(final U obj, final M newValue) {
        this.unsafe.putObjectVolatile(obj, this.offset, newValue);
    }
    
    @Override
    public void lazySet(final U obj, final M newValue) {
        this.unsafe.putOrderedObject(obj, this.offset, newValue);
    }
    
    @Override
    public M get(final U obj) {
        return (M)this.unsafe.getObjectVolatile(obj, this.offset);
    }
}
