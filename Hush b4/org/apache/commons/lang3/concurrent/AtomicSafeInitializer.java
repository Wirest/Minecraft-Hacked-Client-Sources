// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AtomicSafeInitializer<T> implements ConcurrentInitializer<T>
{
    private final AtomicReference<AtomicSafeInitializer<T>> factory;
    private final AtomicReference<T> reference;
    
    public AtomicSafeInitializer() {
        this.factory = new AtomicReference<AtomicSafeInitializer<T>>();
        this.reference = new AtomicReference<T>();
    }
    
    @Override
    public final T get() throws ConcurrentException {
        T result;
        while ((result = this.reference.get()) == null) {
            if (this.factory.compareAndSet(null, this)) {
                this.reference.set(this.initialize());
            }
        }
        return result;
    }
    
    protected abstract T initialize() throws ConcurrentException;
}
