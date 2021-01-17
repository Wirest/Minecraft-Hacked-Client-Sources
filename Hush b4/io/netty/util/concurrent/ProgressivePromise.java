// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

public interface ProgressivePromise<V> extends Promise<V>, ProgressiveFuture<V>
{
    ProgressivePromise<V> setProgress(final long p0, final long p1);
    
    boolean tryProgress(final long p0, final long p1);
    
    ProgressivePromise<V> setSuccess(final V p0);
    
    ProgressivePromise<V> setFailure(final Throwable p0);
    
    ProgressivePromise<V> addListener(final GenericFutureListener<? extends Future<? super V>> p0);
    
    ProgressivePromise<V> addListeners(final GenericFutureListener<? extends Future<? super V>>... p0);
    
    ProgressivePromise<V> removeListener(final GenericFutureListener<? extends Future<? super V>> p0);
    
    ProgressivePromise<V> removeListeners(final GenericFutureListener<? extends Future<? super V>>... p0);
    
    ProgressivePromise<V> await() throws InterruptedException;
    
    ProgressivePromise<V> awaitUninterruptibly();
    
    ProgressivePromise<V> sync() throws InterruptedException;
    
    ProgressivePromise<V> syncUninterruptibly();
}
