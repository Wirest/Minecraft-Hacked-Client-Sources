// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

abstract class WrappingScheduledExecutorService extends WrappingExecutorService implements ScheduledExecutorService
{
    final ScheduledExecutorService delegate;
    
    protected WrappingScheduledExecutorService(final ScheduledExecutorService delegate) {
        super(delegate);
        this.delegate = delegate;
    }
    
    @Override
    public final ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        return this.delegate.schedule(this.wrapTask(command), delay, unit);
    }
    
    @Override
    public final <V> ScheduledFuture<V> schedule(final Callable<V> task, final long delay, final TimeUnit unit) {
        return this.delegate.schedule((Callable<V>)this.wrapTask((Callable<V>)task), delay, unit);
    }
    
    @Override
    public final ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        return this.delegate.scheduleAtFixedRate(this.wrapTask(command), initialDelay, period, unit);
    }
    
    @Override
    public final ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        return this.delegate.scheduleWithFixedDelay(this.wrapTask(command), initialDelay, delay, unit);
    }
}
