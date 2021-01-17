// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.CancellationException;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

public abstract class AbstractFuture<V> implements ListenableFuture<V>
{
    private final Sync<V> sync;
    private final ExecutionList executionList;
    
    protected AbstractFuture() {
        this.sync = new Sync<V>();
        this.executionList = new ExecutionList();
    }
    
    @Override
    public V get(final long timeout, final TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
        return this.sync.get(unit.toNanos(timeout));
    }
    
    @Override
    public V get() throws InterruptedException, ExecutionException {
        return this.sync.get();
    }
    
    @Override
    public boolean isDone() {
        return this.sync.isDone();
    }
    
    @Override
    public boolean isCancelled() {
        return this.sync.isCancelled();
    }
    
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        if (!this.sync.cancel(mayInterruptIfRunning)) {
            return false;
        }
        this.executionList.execute();
        if (mayInterruptIfRunning) {
            this.interruptTask();
        }
        return true;
    }
    
    protected void interruptTask() {
    }
    
    protected final boolean wasInterrupted() {
        return this.sync.wasInterrupted();
    }
    
    @Override
    public void addListener(final Runnable listener, final Executor exec) {
        this.executionList.add(listener, exec);
    }
    
    protected boolean set(@Nullable final V value) {
        final boolean result = this.sync.set(value);
        if (result) {
            this.executionList.execute();
        }
        return result;
    }
    
    protected boolean setException(final Throwable throwable) {
        final boolean result = this.sync.setException(Preconditions.checkNotNull(throwable));
        if (result) {
            this.executionList.execute();
        }
        return result;
    }
    
    static final CancellationException cancellationExceptionWithCause(@Nullable final String message, @Nullable final Throwable cause) {
        final CancellationException exception = new CancellationException(message);
        exception.initCause(cause);
        return exception;
    }
    
    static final class Sync<V> extends AbstractQueuedSynchronizer
    {
        private static final long serialVersionUID = 0L;
        static final int RUNNING = 0;
        static final int COMPLETING = 1;
        static final int COMPLETED = 2;
        static final int CANCELLED = 4;
        static final int INTERRUPTED = 8;
        private V value;
        private Throwable exception;
        
        @Override
        protected int tryAcquireShared(final int ignored) {
            if (this.isDone()) {
                return 1;
            }
            return -1;
        }
        
        @Override
        protected boolean tryReleaseShared(final int finalState) {
            this.setState(finalState);
            return true;
        }
        
        V get(final long nanos) throws TimeoutException, CancellationException, ExecutionException, InterruptedException {
            if (!this.tryAcquireSharedNanos(-1, nanos)) {
                throw new TimeoutException("Timeout waiting for task.");
            }
            return this.getValue();
        }
        
        V get() throws CancellationException, ExecutionException, InterruptedException {
            this.acquireSharedInterruptibly(-1);
            return this.getValue();
        }
        
        private V getValue() throws CancellationException, ExecutionException {
            final int state = this.getState();
            switch (state) {
                case 2: {
                    if (this.exception != null) {
                        throw new ExecutionException(this.exception);
                    }
                    return this.value;
                }
                case 4:
                case 8: {
                    throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.exception);
                }
                default: {
                    throw new IllegalStateException("Error, synchronizer in invalid state: " + state);
                }
            }
        }
        
        boolean isDone() {
            return (this.getState() & 0xE) != 0x0;
        }
        
        boolean isCancelled() {
            return (this.getState() & 0xC) != 0x0;
        }
        
        boolean wasInterrupted() {
            return this.getState() == 8;
        }
        
        boolean set(@Nullable final V v) {
            return this.complete(v, null, 2);
        }
        
        boolean setException(final Throwable t) {
            return this.complete(null, t, 2);
        }
        
        boolean cancel(final boolean interrupt) {
            return this.complete(null, null, interrupt ? 8 : 4);
        }
        
        private boolean complete(@Nullable final V v, @Nullable final Throwable t, final int finalState) {
            final boolean doCompletion = this.compareAndSetState(0, 1);
            if (doCompletion) {
                this.value = v;
                this.exception = (((finalState & 0xC) != 0x0) ? new CancellationException("Future.cancel() was called.") : t);
                this.releaseShared(finalState);
            }
            else if (this.getState() == 1) {
                this.acquireShared(-1);
            }
            return doCompletion;
        }
    }
}
