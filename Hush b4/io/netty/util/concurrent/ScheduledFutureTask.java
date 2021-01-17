// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

final class ScheduledFutureTask<V> extends PromiseTask<V> implements ScheduledFuture<V>
{
    private static final AtomicLong nextTaskId;
    private static final long START_TIME;
    private final long id;
    private final Queue<ScheduledFutureTask<?>> delayedTaskQueue;
    private long deadlineNanos;
    private final long periodNanos;
    
    static long nanoTime() {
        return System.nanoTime() - ScheduledFutureTask.START_TIME;
    }
    
    static long deadlineNanos(final long delay) {
        return nanoTime() + delay;
    }
    
    ScheduledFutureTask(final EventExecutor executor, final Queue<ScheduledFutureTask<?>> delayedTaskQueue, final Runnable runnable, final V result, final long nanoTime) {
        this(executor, delayedTaskQueue, PromiseTask.toCallable(runnable, result), nanoTime);
    }
    
    ScheduledFutureTask(final EventExecutor executor, final Queue<ScheduledFutureTask<?>> delayedTaskQueue, final Callable<V> callable, final long nanoTime, final long period) {
        super(executor, callable);
        this.id = ScheduledFutureTask.nextTaskId.getAndIncrement();
        if (period == 0L) {
            throw new IllegalArgumentException("period: 0 (expected: != 0)");
        }
        this.delayedTaskQueue = delayedTaskQueue;
        this.deadlineNanos = nanoTime;
        this.periodNanos = period;
    }
    
    ScheduledFutureTask(final EventExecutor executor, final Queue<ScheduledFutureTask<?>> delayedTaskQueue, final Callable<V> callable, final long nanoTime) {
        super(executor, callable);
        this.id = ScheduledFutureTask.nextTaskId.getAndIncrement();
        this.delayedTaskQueue = delayedTaskQueue;
        this.deadlineNanos = nanoTime;
        this.periodNanos = 0L;
    }
    
    @Override
    protected EventExecutor executor() {
        return super.executor();
    }
    
    public long deadlineNanos() {
        return this.deadlineNanos;
    }
    
    public long delayNanos() {
        return Math.max(0L, this.deadlineNanos() - nanoTime());
    }
    
    public long delayNanos(final long currentTimeNanos) {
        return Math.max(0L, this.deadlineNanos() - (currentTimeNanos - ScheduledFutureTask.START_TIME));
    }
    
    @Override
    public long getDelay(final TimeUnit unit) {
        return unit.convert(this.delayNanos(), TimeUnit.NANOSECONDS);
    }
    
    @Override
    public int compareTo(final Delayed o) {
        if (this == o) {
            return 0;
        }
        final ScheduledFutureTask<?> that = (ScheduledFutureTask<?>)o;
        final long d = this.deadlineNanos() - that.deadlineNanos();
        if (d < 0L) {
            return -1;
        }
        if (d > 0L) {
            return 1;
        }
        if (this.id < that.id) {
            return -1;
        }
        if (this.id == that.id) {
            throw new Error();
        }
        return 1;
    }
    
    @Override
    public void run() {
        assert this.executor().inEventLoop();
        try {
            if (this.periodNanos == 0L) {
                if (this.setUncancellableInternal()) {
                    final V result = this.task.call();
                    this.setSuccessInternal(result);
                }
            }
            else if (!this.isCancelled()) {
                this.task.call();
                if (!this.executor().isShutdown()) {
                    final long p = this.periodNanos;
                    if (p > 0L) {
                        this.deadlineNanos += p;
                    }
                    else {
                        this.deadlineNanos = nanoTime() - p;
                    }
                    if (!this.isCancelled()) {
                        this.delayedTaskQueue.add(this);
                    }
                }
            }
        }
        catch (Throwable cause) {
            this.setFailureInternal(cause);
        }
    }
    
    @Override
    protected StringBuilder toStringBuilder() {
        final StringBuilder buf = super.toStringBuilder();
        buf.setCharAt(buf.length() - 1, ',');
        buf.append(" id: ");
        buf.append(this.id);
        buf.append(", deadline: ");
        buf.append(this.deadlineNanos);
        buf.append(", period: ");
        buf.append(this.periodNanos);
        buf.append(')');
        return buf;
    }
    
    static {
        nextTaskId = new AtomicLong();
        START_TIME = System.nanoTime();
    }
}
