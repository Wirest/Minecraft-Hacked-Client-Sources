package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public abstract class AbstractFuture<V>
        implements ListenableFuture<V> {
    private final Sync<V> sync = new Sync();
    private final ExecutionList executionList = new ExecutionList();

    static final CancellationException cancellationExceptionWithCause(@Nullable String paramString, @Nullable Throwable paramThrowable) {
        CancellationException localCancellationException = new CancellationException(paramString);
        localCancellationException.initCause(paramThrowable);
        return localCancellationException;
    }

    public V get(long paramLong, TimeUnit paramTimeUnit)
            throws InterruptedException, TimeoutException, ExecutionException {
        return (V) this.sync.get(paramTimeUnit.toNanos(paramLong));
    }

    public V get()
            throws InterruptedException, ExecutionException {
        return (V) this.sync.get();
    }

    public boolean isDone() {
        return this.sync.isDone();
    }

    public boolean isCancelled() {
        return this.sync.isCancelled();
    }

    public boolean cancel(boolean paramBoolean) {
        if (!this.sync.cancel(paramBoolean)) {
            return false;
        }
        this.executionList.execute();
        if (paramBoolean) {
            interruptTask();
        }
        return true;
    }

    protected void interruptTask() {
    }

    protected final boolean wasInterrupted() {
        return this.sync.wasInterrupted();
    }

    public void addListener(Runnable paramRunnable, Executor paramExecutor) {
        this.executionList.add(paramRunnable, paramExecutor);
    }

    protected boolean set(@Nullable V paramV) {
        boolean bool = this.sync.set(paramV);
        if (bool) {
            this.executionList.execute();
        }
        return bool;
    }

    protected boolean setException(Throwable paramThrowable) {
        boolean bool = this.sync.setException((Throwable) Preconditions.checkNotNull(paramThrowable));
        if (bool) {
            this.executionList.execute();
        }
        return bool;
    }

    static final class Sync<V>
            extends AbstractQueuedSynchronizer {
        static final int RUNNING = 0;
        static final int COMPLETING = 1;
        static final int COMPLETED = 2;
        static final int CANCELLED = 4;
        static final int INTERRUPTED = 8;
        private static final long serialVersionUID = 0L;
        private V value;
        private Throwable exception;

        protected int tryAcquireShared(int paramInt) {
            if (isDone()) {
                return 1;
            }
            return -1;
        }

        protected boolean tryReleaseShared(int paramInt) {
            setState(paramInt);
            return true;
        }

        V get(long paramLong)
                throws TimeoutException, CancellationException, ExecutionException, InterruptedException {
            if (!tryAcquireSharedNanos(-1, paramLong)) {
                throw new TimeoutException("Timeout waiting for task.");
            }
            return (V) getValue();
        }

        V get()
                throws CancellationException, ExecutionException, InterruptedException {
            acquireSharedInterruptibly(-1);
            return (V) getValue();
        }

        private V getValue()
                throws CancellationException, ExecutionException {
            int i = getState();
            switch (i) {
                case 2:
                    if (this.exception != null) {
                        throw new ExecutionException(this.exception);
                    }
                    return (V) this.value;
                case 4:
                case 8:
                    throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.exception);
            }
            throw new IllegalStateException("Error, synchronizer in invalid state: " + i);
        }

        boolean isDone() {
            return getState() >> 14 != 0;
        }

        boolean isCancelled() {
            return getState() >> 12 != 0;
        }

        boolean wasInterrupted() {
            return getState() == 8;
        }

        boolean set(@Nullable V paramV) {
            return complete(paramV, null, 2);
        }

        boolean setException(Throwable paramThrowable) {
            return complete(null, paramThrowable, 2);
        }

        boolean cancel(boolean paramBoolean) {
            return complete(null, null, paramBoolean ? 8 : 4);
        }

        private boolean complete(@Nullable V paramV, @Nullable Throwable paramThrowable, int paramInt) {
            boolean bool = compareAndSetState(0, 1);
            if (bool) {
                this.value = paramV;
                this.exception = (paramInt >> 12 != 0 ? new CancellationException("Future.cancel() was called.") : paramThrowable);
                releaseShared(paramInt);
            } else if (getState() == 1) {
                acquireShared(-1);
            }
            return bool;
        }
    }
}




