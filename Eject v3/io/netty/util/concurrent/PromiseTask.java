package io.netty.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

class PromiseTask<V>
        extends DefaultPromise<V>
        implements RunnableFuture<V> {
    protected final Callable<V> task;

    PromiseTask(EventExecutor paramEventExecutor, Runnable paramRunnable, V paramV) {
        this(paramEventExecutor, toCallable(paramRunnable, paramV));
    }

    PromiseTask(EventExecutor paramEventExecutor, Callable<V> paramCallable) {
        super(paramEventExecutor);
        this.task = paramCallable;
    }

    static <T> Callable<T> toCallable(Runnable paramRunnable, T paramT) {
        return new RunnableAdapter(paramRunnable, paramT);
    }

    public final int hashCode() {
        return System.identityHashCode(this);
    }

    public final boolean equals(Object paramObject) {
        return this == paramObject;
    }

    public void run() {
        try {
            if (setUncancellableInternal()) {
                Object localObject = this.task.call();
                setSuccessInternal(localObject);
            }
        } catch (Throwable localThrowable) {
            setFailureInternal(localThrowable);
        }
    }

    public final Promise<V> setFailure(Throwable paramThrowable) {
        throw new IllegalStateException();
    }

    protected final Promise<V> setFailureInternal(Throwable paramThrowable) {
        super.setFailure(paramThrowable);
        return this;
    }

    public final boolean tryFailure(Throwable paramThrowable) {
        return false;
    }

    protected final boolean tryFailureInternal(Throwable paramThrowable) {
        return super.tryFailure(paramThrowable);
    }

    public final Promise<V> setSuccess(V paramV) {
        throw new IllegalStateException();
    }

    protected final Promise<V> setSuccessInternal(V paramV) {
        super.setSuccess(paramV);
        return this;
    }

    public final boolean trySuccess(V paramV) {
        return false;
    }

    protected final boolean trySuccessInternal(V paramV) {
        return super.trySuccess(paramV);
    }

    public final boolean setUncancellable() {
        throw new IllegalStateException();
    }

    protected final boolean setUncancellableInternal() {
        return super.setUncancellable();
    }

    protected StringBuilder toStringBuilder() {
        StringBuilder localStringBuilder = super.toStringBuilder();
        localStringBuilder.setCharAt(localStringBuilder.length() - 1, ',');
        localStringBuilder.append(" task: ");
        localStringBuilder.append(this.task);
        localStringBuilder.append(')');
        return localStringBuilder;
    }

    private static final class RunnableAdapter<T>
            implements Callable<T> {
        final Runnable task;
        final T result;

        RunnableAdapter(Runnable paramRunnable, T paramT) {
            this.task = paramRunnable;
            this.result = paramT;
        }

        public T call() {
            this.task.run();
            return (T) this.result;
        }

        public String toString() {
            return "Callable(task: " + this.task + ", result: " + this.result + ')';
        }
    }
}




