// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.concurrent;

public abstract class LazyInitializer<T> implements ConcurrentInitializer<T>
{
    private volatile T object;
    
    @Override
    public T get() throws ConcurrentException {
        T result = this.object;
        if (result == null) {
            synchronized (this) {
                result = this.object;
                if (result == null) {
                    result = (this.object = this.initialize());
                }
            }
        }
        return result;
    }
    
    protected abstract T initialize() throws ConcurrentException;
}
