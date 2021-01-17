// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadFactory;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import io.netty.util.internal.logging.InternalLogger;

public abstract class SingleThreadEventExecutor extends AbstractEventExecutor
{
    private static final InternalLogger logger;
    private static final int ST_NOT_STARTED = 1;
    private static final int ST_STARTED = 2;
    private static final int ST_SHUTTING_DOWN = 3;
    private static final int ST_SHUTDOWN = 4;
    private static final int ST_TERMINATED = 5;
    private static final Runnable WAKEUP_TASK;
    private static final AtomicIntegerFieldUpdater<SingleThreadEventExecutor> STATE_UPDATER;
    private final EventExecutorGroup parent;
    private final Queue<Runnable> taskQueue;
    final Queue<ScheduledFutureTask<?>> delayedTaskQueue;
    private final Thread thread;
    private final Semaphore threadLock;
    private final Set<Runnable> shutdownHooks;
    private final boolean addTaskWakesUp;
    private long lastExecutionTime;
    private volatile int state;
    private volatile long gracefulShutdownQuietPeriod;
    private volatile long gracefulShutdownTimeout;
    private long gracefulShutdownStartTime;
    private final Promise<?> terminationFuture;
    private static final long SCHEDULE_PURGE_INTERVAL;
    
    protected SingleThreadEventExecutor(final EventExecutorGroup parent, final ThreadFactory threadFactory, final boolean addTaskWakesUp) {
        this.delayedTaskQueue = new PriorityQueue<ScheduledFutureTask<?>>();
        this.threadLock = new Semaphore(0);
        this.shutdownHooks = new LinkedHashSet<Runnable>();
        this.state = 1;
        this.terminationFuture = new DefaultPromise<Object>(GlobalEventExecutor.INSTANCE);
        if (threadFactory == null) {
            throw new NullPointerException("threadFactory");
        }
        this.parent = parent;
        this.addTaskWakesUp = addTaskWakesUp;
        this.thread = threadFactory.newThread(new Runnable() {
            @Override
            public void run() {
                boolean success = false;
                SingleThreadEventExecutor.this.updateLastExecutionTime();
                try {
                    SingleThreadEventExecutor.this.run();
                    success = true;
                }
                catch (Throwable t) {
                    SingleThreadEventExecutor.logger.warn("Unexpected exception from an event executor: ", t);
                    int oldState;
                    do {
                        oldState = SingleThreadEventExecutor.STATE_UPDATER.get(SingleThreadEventExecutor.this);
                    } while (oldState < 3 && !SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, oldState, 3));
                    if (success && SingleThreadEventExecutor.this.gracefulShutdownStartTime == 0L) {
                        SingleThreadEventExecutor.logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called " + "before run() implementation terminates.");
                    }
                    try {
                        while (!SingleThreadEventExecutor.this.confirmShutdown()) {}
                    }
                    finally {
                        try {
                            SingleThreadEventExecutor.this.cleanup();
                        }
                        finally {
                            SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                            SingleThreadEventExecutor.this.threadLock.release();
                            if (!SingleThreadEventExecutor.this.taskQueue.isEmpty()) {
                                SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + SingleThreadEventExecutor.this.taskQueue.size() + ')');
                            }
                            SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
                        }
                    }
                }
                finally {
                    int oldState2;
                    do {
                        oldState2 = SingleThreadEventExecutor.STATE_UPDATER.get(SingleThreadEventExecutor.this);
                    } while (oldState2 < 3 && !SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, oldState2, 3));
                    if (success && SingleThreadEventExecutor.this.gracefulShutdownStartTime == 0L) {
                        SingleThreadEventExecutor.logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called " + "before run() implementation terminates.");
                    }
                    try {
                        while (!SingleThreadEventExecutor.this.confirmShutdown()) {}
                    }
                    finally {
                        try {
                            SingleThreadEventExecutor.this.cleanup();
                        }
                        finally {
                            SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                            SingleThreadEventExecutor.this.threadLock.release();
                            if (!SingleThreadEventExecutor.this.taskQueue.isEmpty()) {
                                SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + SingleThreadEventExecutor.this.taskQueue.size() + ')');
                            }
                            SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
                        }
                    }
                }
            }
        });
        this.taskQueue = this.newTaskQueue();
    }
    
    protected Queue<Runnable> newTaskQueue() {
        return new LinkedBlockingQueue<Runnable>();
    }
    
    @Override
    public EventExecutorGroup parent() {
        return this.parent;
    }
    
    protected void interruptThread() {
        this.thread.interrupt();
    }
    
    protected Runnable pollTask() {
        assert this.inEventLoop();
        Runnable task;
        do {
            task = this.taskQueue.poll();
        } while (task == SingleThreadEventExecutor.WAKEUP_TASK);
        return task;
    }
    
    protected Runnable takeTask() {
        assert this.inEventLoop();
        if (!(this.taskQueue instanceof BlockingQueue)) {
            throw new UnsupportedOperationException();
        }
        final BlockingQueue<Runnable> taskQueue = (BlockingQueue<Runnable>)(BlockingQueue)this.taskQueue;
        while (true) {
            final ScheduledFutureTask<?> delayedTask = this.delayedTaskQueue.peek();
            if (delayedTask == null) {
                Runnable task = null;
                try {
                    task = taskQueue.take();
                    if (task == SingleThreadEventExecutor.WAKEUP_TASK) {
                        task = null;
                    }
                }
                catch (InterruptedException ex) {}
                return task;
            }
            final long delayNanos = delayedTask.delayNanos();
            Runnable task2 = null;
            if (delayNanos > 0L) {
                try {
                    task2 = taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
                }
                catch (InterruptedException e) {
                    return null;
                }
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
    
    protected Runnable peekTask() {
        assert this.inEventLoop();
        return this.taskQueue.peek();
    }
    
    protected boolean hasTasks() {
        assert this.inEventLoop();
        return !this.taskQueue.isEmpty();
    }
    
    protected boolean hasScheduledTasks() {
        assert this.inEventLoop();
        final ScheduledFutureTask<?> delayedTask = this.delayedTaskQueue.peek();
        return delayedTask != null && delayedTask.deadlineNanos() <= ScheduledFutureTask.nanoTime();
    }
    
    public final int pendingTasks() {
        return this.taskQueue.size();
    }
    
    protected void addTask(final Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        if (this.isShutdown()) {
            reject();
        }
        this.taskQueue.add(task);
    }
    
    protected boolean removeTask(final Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        return this.taskQueue.remove(task);
    }
    
    protected boolean runAllTasks() {
        this.fetchFromDelayedQueue();
        Runnable task = this.pollTask();
        if (task == null) {
            return false;
        }
        do {
            try {
                task.run();
            }
            catch (Throwable t) {
                SingleThreadEventExecutor.logger.warn("A task raised an exception.", t);
            }
            task = this.pollTask();
        } while (task != null);
        this.lastExecutionTime = ScheduledFutureTask.nanoTime();
        return true;
    }
    
    protected boolean runAllTasks(final long timeoutNanos) {
        this.fetchFromDelayedQueue();
        Runnable task = this.pollTask();
        if (task == null) {
            return false;
        }
        final long deadline = ScheduledFutureTask.nanoTime() + timeoutNanos;
        long runTasks = 0L;
        long lastExecutionTime;
        while (true) {
            try {
                task.run();
            }
            catch (Throwable t) {
                SingleThreadEventExecutor.logger.warn("A task raised an exception.", t);
            }
            ++runTasks;
            if ((runTasks & 0x3FL) == 0x0L) {
                lastExecutionTime = ScheduledFutureTask.nanoTime();
                if (lastExecutionTime >= deadline) {
                    break;
                }
            }
            task = this.pollTask();
            if (task == null) {
                lastExecutionTime = ScheduledFutureTask.nanoTime();
                break;
            }
        }
        this.lastExecutionTime = lastExecutionTime;
        return true;
    }
    
    protected long delayNanos(final long currentTimeNanos) {
        final ScheduledFutureTask<?> delayedTask = this.delayedTaskQueue.peek();
        if (delayedTask == null) {
            return SingleThreadEventExecutor.SCHEDULE_PURGE_INTERVAL;
        }
        return delayedTask.delayNanos(currentTimeNanos);
    }
    
    protected void updateLastExecutionTime() {
        this.lastExecutionTime = ScheduledFutureTask.nanoTime();
    }
    
    protected abstract void run();
    
    protected void cleanup() {
    }
    
    protected void wakeup(final boolean inEventLoop) {
        if (!inEventLoop || SingleThreadEventExecutor.STATE_UPDATER.get(this) == 3) {
            this.taskQueue.add(SingleThreadEventExecutor.WAKEUP_TASK);
        }
    }
    
    @Override
    public boolean inEventLoop(final Thread thread) {
        return thread == this.thread;
    }
    
    public void addShutdownHook(final Runnable task) {
        if (this.inEventLoop()) {
            this.shutdownHooks.add(task);
        }
        else {
            this.execute(new Runnable() {
                @Override
                public void run() {
                    SingleThreadEventExecutor.this.shutdownHooks.add(task);
                }
            });
        }
    }
    
    public void removeShutdownHook(final Runnable task) {
        if (this.inEventLoop()) {
            this.shutdownHooks.remove(task);
        }
        else {
            this.execute(new Runnable() {
                @Override
                public void run() {
                    SingleThreadEventExecutor.this.shutdownHooks.remove(task);
                }
            });
        }
    }
    
    private boolean runShutdownHooks() {
        boolean ran = false;
        while (!this.shutdownHooks.isEmpty()) {
            final List<Runnable> copy = new ArrayList<Runnable>(this.shutdownHooks);
            this.shutdownHooks.clear();
            for (final Runnable task : copy) {
                try {
                    task.run();
                }
                catch (Throwable t) {
                    SingleThreadEventExecutor.logger.warn("Shutdown hook raised an exception.", t);
                }
                finally {
                    ran = true;
                }
            }
        }
        if (ran) {
            this.lastExecutionTime = ScheduledFutureTask.nanoTime();
        }
        return ran;
    }
    
    @Override
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        if (quietPeriod < 0L) {
            throw new IllegalArgumentException("quietPeriod: " + quietPeriod + " (expected >= 0)");
        }
        if (timeout < quietPeriod) {
            throw new IllegalArgumentException("timeout: " + timeout + " (expected >= quietPeriod (" + quietPeriod + "))");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (this.isShuttingDown()) {
            return this.terminationFuture();
        }
        final boolean inEventLoop = this.inEventLoop();
        while (!this.isShuttingDown()) {
            boolean wakeup = true;
            final int oldState = SingleThreadEventExecutor.STATE_UPDATER.get(this);
            int newState = 0;
            if (inEventLoop) {
                newState = 3;
            }
            else {
                switch (oldState) {
                    case 1:
                    case 2: {
                        newState = 3;
                        break;
                    }
                    default: {
                        newState = oldState;
                        wakeup = false;
                        break;
                    }
                }
            }
            if (SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(this, oldState, newState)) {
                this.gracefulShutdownQuietPeriod = unit.toNanos(quietPeriod);
                this.gracefulShutdownTimeout = unit.toNanos(timeout);
                if (oldState == 1) {
                    this.thread.start();
                }
                if (wakeup) {
                    this.wakeup(inEventLoop);
                }
                return this.terminationFuture();
            }
        }
        return this.terminationFuture();
    }
    
    @Override
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }
    
    @Deprecated
    @Override
    public void shutdown() {
        if (this.isShutdown()) {
            return;
        }
        final boolean inEventLoop = this.inEventLoop();
        while (!this.isShuttingDown()) {
            boolean wakeup = true;
            final int oldState = SingleThreadEventExecutor.STATE_UPDATER.get(this);
            int newState = 0;
            if (inEventLoop) {
                newState = 4;
            }
            else {
                switch (oldState) {
                    case 1:
                    case 2:
                    case 3: {
                        newState = 4;
                        break;
                    }
                    default: {
                        newState = oldState;
                        wakeup = false;
                        break;
                    }
                }
            }
            if (SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(this, oldState, newState)) {
                if (oldState == 1) {
                    this.thread.start();
                }
                if (wakeup) {
                    this.wakeup(inEventLoop);
                }
            }
        }
    }
    
    @Override
    public boolean isShuttingDown() {
        return SingleThreadEventExecutor.STATE_UPDATER.get(this) >= 3;
    }
    
    @Override
    public boolean isShutdown() {
        return SingleThreadEventExecutor.STATE_UPDATER.get(this) >= 4;
    }
    
    @Override
    public boolean isTerminated() {
        return SingleThreadEventExecutor.STATE_UPDATER.get(this) == 5;
    }
    
    protected boolean confirmShutdown() {
        if (!this.isShuttingDown()) {
            return false;
        }
        if (!this.inEventLoop()) {
            throw new IllegalStateException("must be invoked from an event loop");
        }
        this.cancelDelayedTasks();
        if (this.gracefulShutdownStartTime == 0L) {
            this.gracefulShutdownStartTime = ScheduledFutureTask.nanoTime();
        }
        if (this.runAllTasks() || this.runShutdownHooks()) {
            if (this.isShutdown()) {
                return true;
            }
            this.wakeup(true);
            return false;
        }
        else {
            final long nanoTime = ScheduledFutureTask.nanoTime();
            if (this.isShutdown() || nanoTime - this.gracefulShutdownStartTime > this.gracefulShutdownTimeout) {
                return true;
            }
            if (nanoTime - this.lastExecutionTime <= this.gracefulShutdownQuietPeriod) {
                this.wakeup(true);
                try {
                    Thread.sleep(100L);
                }
                catch (InterruptedException ex) {}
                return false;
            }
            return true;
        }
    }
    
    private void cancelDelayedTasks() {
        if (this.delayedTaskQueue.isEmpty()) {
            return;
        }
        final ScheduledFutureTask[] arr$;
        final ScheduledFutureTask<?>[] delayedTasks = (ScheduledFutureTask<?>[])(arr$ = this.delayedTaskQueue.toArray(new ScheduledFutureTask[this.delayedTaskQueue.size()]));
        for (final ScheduledFutureTask<?> task : arr$) {
            task.cancel(false);
        }
        this.delayedTaskQueue.clear();
    }
    
    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (this.inEventLoop()) {
            throw new IllegalStateException("cannot await termination of the current thread");
        }
        if (this.threadLock.tryAcquire(timeout, unit)) {
            this.threadLock.release();
        }
        return this.isTerminated();
    }
    
    @Override
    public void execute(final Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        final boolean inEventLoop = this.inEventLoop();
        if (inEventLoop) {
            this.addTask(task);
        }
        else {
            this.startThread();
            this.addTask(task);
            if (this.isShutdown() && this.removeTask(task)) {
                reject();
            }
        }
        if (!this.addTaskWakesUp && this.wakesUpForTask(task)) {
            this.wakeup(inEventLoop);
        }
    }
    
    protected boolean wakesUpForTask(final Runnable task) {
        return true;
    }
    
    protected static void reject() {
        throw new RejectedExecutionException("event executor terminated");
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
                    SingleThreadEventExecutor.this.delayedTaskQueue.add(task);
                }
            });
        }
        return task;
    }
    
    private void startThread() {
        if (SingleThreadEventExecutor.STATE_UPDATER.get(this) == 1 && SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(this, 1, 2)) {
            this.delayedTaskQueue.add(new ScheduledFutureTask<Object>(this, this.delayedTaskQueue, Executors.callable(new PurgeTask(), (Object)null), ScheduledFutureTask.deadlineNanos(SingleThreadEventExecutor.SCHEDULE_PURGE_INTERVAL), -SingleThreadEventExecutor.SCHEDULE_PURGE_INTERVAL));
            this.thread.start();
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(SingleThreadEventExecutor.class);
        WAKEUP_TASK = new Runnable() {
            @Override
            public void run() {
            }
        };
        AtomicIntegerFieldUpdater<SingleThreadEventExecutor> updater = PlatformDependent.newAtomicIntegerFieldUpdater(SingleThreadEventExecutor.class, "state");
        if (updater == null) {
            updater = AtomicIntegerFieldUpdater.newUpdater(SingleThreadEventExecutor.class, "state");
        }
        STATE_UPDATER = updater;
        SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
    }
    
    private final class PurgeTask implements Runnable
    {
        @Override
        public void run() {
            final Iterator<ScheduledFutureTask<?>> i = SingleThreadEventExecutor.this.delayedTaskQueue.iterator();
            while (i.hasNext()) {
                final ScheduledFutureTask<?> task = i.next();
                if (task.isCancelled()) {
                    i.remove();
                }
            }
        }
    }
}
