// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

import java.util.concurrent.TimeUnit;

public interface Future<V> extends java.util.concurrent.Future<V>
{
    boolean isSuccess();
    
    boolean isCancellable();
    
    Throwable cause();
    
    Future<V> addListener(final GenericFutureListener<? extends Future<? super V>> p0);
    
    Future<V> addListeners(final GenericFutureListener<? extends Future<? super V>>... p0);
    
    Future<V> removeListener(final GenericFutureListener<? extends Future<? super V>> p0);
    
    Future<V> removeListeners(final GenericFutureListener<? extends Future<? super V>>... p0);
    
    Future<V> sync() throws InterruptedException;
    
    Future<V> syncUninterruptibly();
    
    Future<V> await() throws InterruptedException;
    
    Future<V> awaitUninterruptibly();
    
    boolean await(final long p0, final TimeUnit p1) throws InterruptedException;
    
    boolean await(final long p0) throws InterruptedException;
    
    boolean awaitUninterruptibly(final long p0, final TimeUnit p1);
    
    boolean awaitUninterruptibly(final long p0);
    
    V getNow();
    
    boolean cancel(final boolean p0);
}
