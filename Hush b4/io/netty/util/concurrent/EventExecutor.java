// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

public interface EventExecutor extends EventExecutorGroup
{
    EventExecutor next();
    
    EventExecutorGroup parent();
    
    boolean inEventLoop();
    
    boolean inEventLoop(final Thread p0);
    
     <V> Promise<V> newPromise();
    
     <V> ProgressivePromise<V> newProgressivePromise();
    
     <V> Future<V> newSucceededFuture(final V p0);
    
     <V> Future<V> newFailedFuture(final Throwable p0);
}
