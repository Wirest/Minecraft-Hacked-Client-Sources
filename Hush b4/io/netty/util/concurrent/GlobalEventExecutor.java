// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

import java.util.Iterator;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ThreadFactory;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import io.netty.util.internal.logging.InternalLogger;

public final class GlobalEventExecutor extends AbstractEventExecutor
{
    private static final InternalLogger logger;
    private static final long SCHEDULE_PURGE_INTERVAL;
    public static final GlobalEventExecutor INSTANCE;
    final BlockingQueue<Runnable> taskQueue;
    final Queue<ScheduledFutureTask<?>> delayedTaskQueue;
    final ScheduledFutureTask<Void> purgeTask;
    private final ThreadFactory threadFactory;
    private final TaskRunner taskRunner;
    private final AtomicBoolean started;
    volatile Thread thread;
    private final Future<?> terminationFuture;
    
    private GlobalEventExecutor() {
        this.taskQueue = new LinkedBlockingQueue<Runnable>();
        this.delayedTaskQueue = new PriorityQueue<ScheduledFutureTask<?>>();
        this.purgeTask = new ScheduledFutureTask<Void>(this, this.delayedTaskQueue, Executors.callable(new PurgeTask(), (Void)null), ScheduledFutureTask.deadlineNanos(GlobalEventExecutor.SCHEDULE_PURGE_INTERVAL), -GlobalEventExecutor.SCHEDULE_PURGE_INTERVAL);
        this.threadFactory = new DefaultThreadFactory(this.getClass());
        this.taskRunner = new TaskRunner();
        this.started = new AtomicBoolean();
        this.terminationFuture = new FailedFuture<Object>(this, new UnsupportedOperationException());
        this.delayedTaskQueue.add(this.purgeTask);
    }
    
    @Override
    public EventExecutorGroup parent() {
        return null;
    }
    
    Runnable takeTask() {
        final BlockingQueue<Runnable> taskQueue = this.taskQueue;
        while (true) {
            final ScheduledFutureTask<?> delayedTask = this.delayedTaskQueue.peek();
            if (delayedTask == null) {
                Runnable task = null;
                try {
                    task = taskQueue.take();
                }
                catch (InterruptedException ex) {}
                return task;
            }
            final long delayNanos = delayedTask.delayNanos();
            Runnable task2 = null;
            Label_0085: {
                if (delayNanos > 0L) {
                    try {
                        task2 = taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
                        break Label_0085;
                    }
                    catch (InterruptedException e) {
                        return null;
                    }
                }
                task2 = taskQueue.poll();
            }
            if (task2 == null) {
                this.fetchFromDelayedQueue();
                task2 = taskQueue.poll();
            }
            if (task2 != null) {
                return task2;
            }
        }
    }
    
    private void fetchFromDelayedQueue() {
        long nanoTime = 0L;
        while (true) {
            final ScheduledFutureTask<?> delayedTask = this.delayedTaskQueue.peek();
            if (delayedTask == null) {
                break;
            }
            if (nanoTime == 0L) {
                nanoTime = ScheduledFutureTask.nanoTime();
            }
            if (delayedTask.deadlineNanos() > nanoTime) {
                break;
            }
            this.delayedTaskQueue.remove();
            this.taskQueue.add(delayedTask);
        }
    }
    
    public int pendingTasks() {
        return this.taskQueue.size();
    }
    
    private void addTask(final Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        this.taskQueue.add(task);
    }
    
    @Override
    public boolean inEventLoop(final Thread thread) {
        return thread == this.thread;
    }
    
    @Override
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        return this.terminationFuture();
    }
    
    @Override
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }
    
    @Deprecated
    @Override
    public void shutdown() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isShuttingDown() {
        return false;
    }
    
    @Override
    public boolean isShutdown() {
        return false;
    }
    
    @Override
    public boolean isTerminated() {
        return false;
    }
    
    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) {
        return false;
    }
    
    public boolean awaitInactivity(final long timeout, final TimeUnit unit) throws InterruptedException {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        final Thread thread = this.thread;
        if (thread == null) {
            throw new IllegalStateException("thread was not started");
        }
        thread.join(unit.toMillis(timeout));
        return !thread.isAlive();
    }
    
    @Override
    public void execute(final Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        this.addTask(task);
        if (!this.inEventLoop()) {
            this.startThread();
        }
    }
    
    @Override
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (delay < 0L) {
            throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", delay));
        }
        return this.schedule((ScheduledFutureTask<?>)new ScheduledFutureTask<Object>(this, this.delayedTaskQueue, command, null, ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
    }
    
    @Override
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
        if (callable == null) {
            throw new NullPointerException("callable");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (delay < 0L) {
            throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", delay));
        }
        return this.schedule(new ScheduledFutureTask<V>(this, this.delayedTaskQueue, callable, ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
    }
    
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (initialDelay < 0L) {
            throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", initialDelay));
        }
        if (period <= 0L) {
            throw new IllegalArgumentException(String.format("period: %d (expected: > 0)", period));
        }
        return this.schedule((ScheduledFutureTask<?>)new ScheduledFutureTask<Object>(this, this.delayedTaskQueue, Executors.callable(command, (V)null), ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), unit.toNanos(period)));
    }
    
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (initialDelay < 0L) {
            throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", initialDelay));
        }
        if (delay <= 0L) {
            throw new IllegalArgumentException(String.format("delay: %d (expected: > 0)", delay));
        }
        return this.schedule((ScheduledFutureTask<?>)new ScheduledFutureTask<Object>(this, this.delayedTaskQueue, Executors.callable(command, (V)null), ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), -unit.toNanos(delay)));
    }
    
    private <V> ScheduledFuture<V> schedule(final ScheduledFutureTask<V> task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        if (this.inEventLoop()) {
            this.delayedTaskQueue.add(task);
        }
        else {
            this.execute(new Runnable() {
                @Override
                public void run() {
                    GlobalEventExecutor.this.delayedTaskQueue.add(task);
                }
            });
        }
        return task;
    }
    
    private void startThread() {
        if (this.started.compareAndSet(false, true)) {
            final Thread t = this.threadFactory.newThread(this.taskRunner);
            t.start();
            this.thread = t;
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(GlobalEventExecutor.class);
        SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
        INSTANCE = new GlobalEventExecutor();
    }
    
    final class TaskRunner implements Runnable
    {
        @Override
        public void run() {
            while (true) {
                final Runnable task = GlobalEventExecutor.this.takeTask();
                if (task != null) {
                    try {
                        task.run();
                    }
                    catch (Throwable t) {
                        GlobalEventExecutor.logger.warn("Unexpected exception from the global event executor: ", t);
                    }
                    if (task != GlobalEventExecutor.this.purgeTask) {
                        continue;
                    }
                }
                if (GlobalEventExecutor.this.taskQueue.isEmpty() && GlobalEventExecutor.this.delayedTaskQueue.size() == 1) {
                    final boolean stopped = GlobalEventExecutor.this.started.compareAndSet(true, false);
                    assert stopped;
                    if (GlobalEventExecutor.this.taskQueue.isEmpty() && GlobalEventExecutor.this.delayedTaskQueue.size() == 1) {
                        break;
                    }
                    if (!GlobalEventExecutor.this.started.compareAndSet(false, true)) {
                        break;
                    }
                    continue;
                }
            }
        }
    }
    
    private final class PurgeTask implements Runnable
    {
        @Override
        public void run() {
            final Iterator<ScheduledFutureTask<?>> i = GlobalEventExecutor.this.delayedTaskQueue.iterator();
            while (i.hasNext()) {
                final ScheduledFutureTask<?> task = i.next();
                if (task.isCancelled()) {
                    i.remove();
                }
            }
        }
    }
}
