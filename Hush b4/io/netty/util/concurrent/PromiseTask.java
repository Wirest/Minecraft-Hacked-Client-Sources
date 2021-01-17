// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

class PromiseTask<V> extends DefaultPromise<V> implements RunnableFuture<V>
{
    protected final Callable<V> task;
    
    static <T> Callable<T> toCallable(final Runnable runnable, final T result) {
        return new RunnableAdapter<T>(runnable, result);
    }
    
    PromiseTask(final EventExecutor executor, final Runnable runnable, final V result) {
        this(executor, toCallable(runnable, result));
    }
    
    PromiseTask(final EventExecutor executor, final Callable<V> callable) {
        super(executor);
        this.task = callable;
    }
    
    @Override
    public final int hashCode() {
        return System.identityHashCode(this);
    }
    
    @Override
    public final boolean equals(final Object obj) {
        return this == obj;
    }
    
    @Override
    public void run() {
        try {
            if (this.setUncancellableInternal()) {
                final V result = this.task.call();
                this.setSuccessInternal(result);
            }
        }
        catch (Throwable e) {
            this.setFailureInternal(e);
        }
    }
    
    @Override
    public final Promise<V> setFailure(final Throwable cause) {
        throw new IllegalStateException();
    }
    
    protected final Promise<V> setFailureInternal(final Throwable cause) {
        super.setFailure(cause);
        return this;
    }
    
    @Override
    public final boolean tryFailure(final Throwable cause) {
        return false;
    }
    
    protected final boolean tryFailureInternal(final Throwable cause) {
        return super.tryFailure(cause);
    }
    
    @Override
    public final Promise<V> setSuccess(final V result) {
        throw new IllegalStateException();
    }
    
    protected final Promise<V> setSuccessInternal(final V result) {
        super.setSuccess(result);
        return this;
    }
    
    @Override
    public final boolean trySuccess(final V result) {
        return false;
    }
    
    protected final boolean trySuccessInternal(final V result) {
        return super.trySuccess(result);
    }
    
    @Override
    public final boolean setUncancellable() {
        throw new IllegalStateException();
    }
    
    protected final boolean setUncancellableInternal() {
        return super.setUncancellable();
    }
    
    @Override
    protected StringBuilder toStringBuilder() {
        final StringBuilder buf = super.toStringBuilder();
        buf.setCharAt(buf.length() - 1, ',');
        buf.append(" task: ");
        buf.append(this.task);
        buf.append(')');
        return buf;
    }
    
    private static final class RunnableAdapter<T> implements Callable<T>
    {
        final Runnable task;
        final T result;
        
        RunnableAdapter(final Runnable task, final T result) {
            this.task = task;
            this.result = result;
        }
        
        @Override
        public T call() {
            this.task.run();
            return this.result;
        }
        
        @Override
        public String toString() {
            return "Callable(task: " + this.task + ", result: " + this.result + ')';
        }
    }
}
