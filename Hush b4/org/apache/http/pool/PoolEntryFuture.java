// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.pool;

import java.util.Date;
import java.io.IOException;
import org.apache.http.util.Args;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import org.apache.http.concurrent.FutureCallback;
import java.util.concurrent.locks.Lock;
import org.apache.http.annotation.ThreadSafe;
import java.util.concurrent.Future;

@ThreadSafe
abstract class PoolEntryFuture<T> implements Future<T>
{
    private final Lock lock;
    private final FutureCallback<T> callback;
    private final Condition condition;
    private volatile boolean cancelled;
    private volatile boolean completed;
    private T result;
    
    PoolEntryFuture(final Lock lock, final FutureCallback<T> callback) {
        this.lock = lock;
        this.condition = lock.newCondition();
        this.callback = callback;
    }
    
    public boolean cancel(final boolean mayInterruptIfRunning) {
        this.lock.lock();
        try {
            if (this.completed) {
                return false;
            }
            this.completed = true;
            this.cancelled = true;
            if (this.callback != null) {
                this.callback.cancelled();
            }
            this.condition.signalAll();
            return true;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public boolean isDone() {
        return this.completed;
    }
    
    public T get() throws InterruptedException, ExecutionException {
        try {
            return this.get(0L, TimeUnit.MILLISECONDS);
        }
        catch (TimeoutException ex) {
            throw new ExecutionException(ex);
        }
    }
    
    public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        Args.notNull(unit, "Time unit");
        this.lock.lock();
        try {
            if (this.completed) {
                return this.result;
            }
            this.result = this.getPoolEntry(timeout, unit);
            this.completed = true;
            if (this.callback != null) {
                this.callback.completed(this.result);
            }
            return this.result;
        }
        catch (IOException ex) {
            this.completed = true;
            this.result = null;
            if (this.callback != null) {
                this.callback.failed(ex);
            }
            throw new ExecutionException(ex);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    protected abstract T getPoolEntry(final long p0, final TimeUnit p1) throws IOException, InterruptedException, TimeoutException;
    
    public boolean await(final Date deadline) throws InterruptedException {
        this.lock.lock();
        try {
            if (this.cancelled) {
                throw new InterruptedException("Operation interrupted");
            }
            boolean success;
            if (deadline != null) {
                success = this.condition.awaitUntil(deadline);
            }
            else {
                this.condition.await();
                success = true;
            }
            if (this.cancelled) {
                throw new InterruptedException("Operation interrupted");
            }
            return success;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public void wakeup() {
        this.lock.lock();
        try {
            this.condition.signalAll();
        }
        finally {
            this.lock.unlock();
        }
    }
}
