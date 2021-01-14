package io.netty.util.concurrent;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

final class ScheduledFutureTask<V>
        extends PromiseTask<V>
        implements ScheduledFuture<V> {
    private static final AtomicLong nextTaskId = new AtomicLong();
    private static final long START_TIME = System.nanoTime();
    private final long id = nextTaskId.getAndIncrement();
    private final Queue<ScheduledFutureTask<?>> delayedTaskQueue;
    private final long periodNanos;
    private long deadlineNanos;

    ScheduledFutureTask(EventExecutor paramEventExecutor, Queue<ScheduledFutureTask<?>> paramQueue, Runnable paramRunnable, V paramV, long paramLong) {
        this(paramEventExecutor, paramQueue, toCallable(paramRunnable, paramV), paramLong);
    }

    ScheduledFutureTask(EventExecutor paramEventExecutor, Queue<ScheduledFutureTask<?>> paramQueue, Callable<V> paramCallable, long paramLong1, long paramLong2) {
        super(paramEventExecutor, paramCallable);
        if (paramLong2 == 0L) {
            throw new IllegalArgumentException("period: 0 (expected: != 0)");
        }
        this.delayedTaskQueue = paramQueue;
        this.deadlineNanos = paramLong1;
        this.periodNanos = paramLong2;
    }

    ScheduledFutureTask(EventExecutor paramEventExecutor, Queue<ScheduledFutureTask<?>> paramQueue, Callable<V> paramCallable, long paramLong) {
        super(paramEventExecutor, paramCallable);
        this.delayedTaskQueue = paramQueue;
        this.deadlineNanos = paramLong;
        this.periodNanos = 0L;
    }

    static long nanoTime() {
        return System.nanoTime() - START_TIME;
    }

    static long deadlineNanos(long paramLong) {
        return nanoTime() + paramLong;
    }

    protected EventExecutor executor() {
        return super.executor();
    }

    public long deadlineNanos() {
        return this.deadlineNanos;
    }

    public long delayNanos() {
        return Math.max(0L, deadlineNanos() - nanoTime());
    }

    public long delayNanos(long paramLong) {
        return Math.max(0L, deadlineNanos() - (paramLong - START_TIME));
    }

    public long getDelay(TimeUnit paramTimeUnit) {
        return paramTimeUnit.convert(delayNanos(), TimeUnit.NANOSECONDS);
    }

    public int compareTo(Delayed paramDelayed) {
        if (this == paramDelayed) {
            return 0;
        }
        ScheduledFutureTask localScheduledFutureTask = (ScheduledFutureTask) paramDelayed;
        long l = deadlineNanos() - localScheduledFutureTask.deadlineNanos();
        if (l < 0L) {
            return -1;
        }
        if (l > 0L) {
            return 1;
        }
        if (this.id < localScheduledFutureTask.id) {
            return -1;
        }
        if (this.id == localScheduledFutureTask.id) {
            throw new Error();
        }
        return 1;
    }

    public void run() {
        assert (executor().inEventLoop());
        try {
            if (this.periodNanos == 0L) {
                if (setUncancellableInternal()) {
                    Object localObject = this.task.call();
                    setSuccessInternal(localObject);
                }
            } else if (!isCancelled()) {
                this.task.call();
                if (!executor().isShutdown()) {
                    long l = this.periodNanos;
                    if (l > 0L) {
                        this.deadlineNanos += l;
                    } else {
                        this.deadlineNanos = (nanoTime() - l);
                    }
                    if (!isCancelled()) {
                        this.delayedTaskQueue.add(this);
                    }
                }
            }
        } catch (Throwable localThrowable) {
            setFailureInternal(localThrowable);
        }
    }

    protected StringBuilder toStringBuilder() {
        StringBuilder localStringBuilder = super.toStringBuilder();
        localStringBuilder.setCharAt(localStringBuilder.length() - 1, ',');
        localStringBuilder.append(" id: ");
        localStringBuilder.append(this.id);
        localStringBuilder.append(", deadline: ");
        localStringBuilder.append(this.deadlineNanos);
        localStringBuilder.append(", period: ");
        localStringBuilder.append(this.periodNanos);
        localStringBuilder.append(')');
        return localStringBuilder;
    }
}




