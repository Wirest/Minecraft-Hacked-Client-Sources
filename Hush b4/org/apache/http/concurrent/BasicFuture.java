// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.concurrent;

import java.util.concurrent.TimeoutException;
import org.apache.http.util.Args;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class BasicFuture<T> implements Future<T>, Cancellable
{
    private final FutureCallback<T> callback;
    private volatile boolean completed;
    private volatile boolean cancelled;
    private volatile T result;
    private volatile Exception ex;
    
    public BasicFuture(final FutureCallback<T> callback) {
        this.callback = callback;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public boolean isDone() {
        return this.completed;
    }
    
    private T getResult() throws ExecutionException {
        if (this.ex != null) {
            throw new ExecutionException(this.ex);
        }
        return this.result;
    }
    
    public synchronized T get() throws InterruptedException, ExecutionException {
        while (!this.completed) {
            this.wait();
        }
        return this.getResult();
    }
    
    public synchronized T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        Args.notNull(unit, "Time unit");
        final long msecs = unit.toMillis(timeout);
        final long startTime = (msecs <= 0L) ? 0L : System.currentTimeMillis();
        long waitTime = msecs;
        if (this.completed) {
            return this.getResult();
        }
        if (waitTime <= 0L) {
            throw new TimeoutException();
        }
        do {
            this.wait(waitTime);
            if (this.completed) {
                return this.getResult();
            }
            waitTime = msecs - (System.currentTimeMillis() - startTime);
        } while (waitTime > 0L);
        throw new TimeoutException();
    }
    
    public boolean completed(final T result) {
        synchronized (this) {
            if (this.completed) {
                return false;
            }
            this.completed = true;
            this.result = result;
            this.notifyAll();
        }
        if (this.callback != null) {
            this.callback.completed(result);
        }
        return true;
    }
    
    public boolean failed(final Exception exception) {
        synchronized (this) {
            if (this.completed) {
                return false;
            }
            this.completed = true;
            this.ex = exception;
            this.notifyAll();
        }
        if (this.callback != null) {
            this.callback.failed(exception);
        }
        return true;
    }
    
    public boolean cancel(final boolean mayInterruptIfRunning) {
        synchronized (this) {
            if (this.completed) {
                return false;
            }
            this.completed = true;
            this.cancelled = true;
            this.notifyAll();
        }
        if (this.callback != null) {
            this.callback.cancelled();
        }
        return true;
    }
    
    public boolean cancel() {
        return this.cancel(true);
    }
}
