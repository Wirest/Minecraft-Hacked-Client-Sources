// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

import java.util.NoSuchElementException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.Callable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import java.util.concurrent.AbstractExecutorService;

public abstract class AbstractEventExecutor extends AbstractExecutorService implements EventExecutor
{
    @Override
    public EventExecutor next() {
        return this;
    }
    
    @Override
    public boolean inEventLoop() {
        return this.inEventLoop(Thread.currentThread());
    }
    
    @Override
    public Iterator<EventExecutor> iterator() {
        return new EventExecutorIterator();
    }
    
    @Override
    public Future<?> shutdownGracefully() {
        return this.shutdownGracefully(2L, 15L, TimeUnit.SECONDS);
    }
    
    @Deprecated
    @Override
    public abstract void shutdown();
    
    @Deprecated
    @Override
    public List<Runnable> shutdownNow() {
        this.shutdown();
        return Collections.emptyList();
    }
    
    @Override
    public <V> Promise<V> newPromise() {
        return new DefaultPromise<V>(this);
    }
    
    @Override
    public <V> ProgressivePromise<V> newProgressivePromise() {
        return new DefaultProgressivePromise<V>(this);
    }
    
    @Override
    public <V> Future<V> newSucceededFuture(final V result) {
        return new SucceededFuture<V>(this, result);
    }
    
    @Override
    public <V> Future<V> newFailedFuture(final Throwable cause) {
        return new FailedFuture<V>(this, cause);
    }
    
    @Override
    public Future<?> submit(final Runnable task) {
        return (Future<?>)(Future)super.submit(task);
    }
    
    @Override
    public <T> Future<T> submit(final Runnable task, final T result) {
        return (Future<T>)(Future)super.submit(task, result);
    }
    
    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        return (Future<T>)(Future)super.submit(task);
    }
    
    @Override
    protected final <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
        return new PromiseTask<T>(this, runnable, value);
    }
    
    @Override
    protected final <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
        return new PromiseTask<T>(this, callable);
    }
    
    @Override
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        throw new UnsupportedOperationException();
    }
    
    private final class EventExecutorIterator implements Iterator<EventExecutor>
    {
        private boolean nextCalled;
        
        @Override
        public boolean hasNext() {
            return !this.nextCalled;
        }
        
        @Override
        public EventExecutor next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.nextCalled = true;
            return AbstractEventExecutor.this;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("read-only");
        }
    }
}
