// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util;

import io.netty.util.internal.StringUtil;
import io.netty.util.internal.MpscLinkedQueueNode;
import java.util.HashSet;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Collections;
import java.util.Set;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import io.netty.util.internal.logging.InternalLogger;

public class HashedWheelTimer implements Timer
{
    static final InternalLogger logger;
    private static final ResourceLeakDetector<HashedWheelTimer> leakDetector;
    private static final AtomicIntegerFieldUpdater<HashedWheelTimer> WORKER_STATE_UPDATER;
    private final ResourceLeak leak;
    private final Worker worker;
    private final Thread workerThread;
    public static final int WORKER_STATE_INIT = 0;
    public static final int WORKER_STATE_STARTED = 1;
    public static final int WORKER_STATE_SHUTDOWN = 2;
    private volatile int workerState;
    private final long tickDuration;
    private final HashedWheelBucket[] wheel;
    private final int mask;
    private final CountDownLatch startTimeInitialized;
    private final Queue<HashedWheelTimeout> timeouts;
    private final Queue<Runnable> cancelledTimeouts;
    private volatile long startTime;
    
    public HashedWheelTimer() {
        this(Executors.defaultThreadFactory());
    }
    
    public HashedWheelTimer(final long tickDuration, final TimeUnit unit) {
        this(Executors.defaultThreadFactory(), tickDuration, unit);
    }
    
    public HashedWheelTimer(final long tickDuration, final TimeUnit unit, final int ticksPerWheel) {
        this(Executors.defaultThreadFactory(), tickDuration, unit, ticksPerWheel);
    }
    
    public HashedWheelTimer(final ThreadFactory threadFactory) {
        this(threadFactory, 100L, TimeUnit.MILLISECONDS);
    }
    
    public HashedWheelTimer(final ThreadFactory threadFactory, final long tickDuration, final TimeUnit unit) {
        this(threadFactory, tickDuration, unit, 512);
    }
    
    public HashedWheelTimer(final ThreadFactory threadFactory, final long tickDuration, final TimeUnit unit, final int ticksPerWheel) {
        this.worker = new Worker();
        this.workerState = 0;
        this.startTimeInitialized = new CountDownLatch(1);
        this.timeouts = PlatformDependent.newMpscQueue();
        this.cancelledTimeouts = PlatformDependent.newMpscQueue();
        if (threadFactory == null) {
            throw new NullPointerException("threadFactory");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (tickDuration <= 0L) {
            throw new IllegalArgumentException("tickDuration must be greater than 0: " + tickDuration);
        }
        if (ticksPerWheel <= 0) {
            throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + ticksPerWheel);
        }
        this.wheel = createWheel(ticksPerWheel);
        this.mask = this.wheel.length - 1;
        this.tickDuration = unit.toNanos(tickDuration);
        if (this.tickDuration >= Long.MAX_VALUE / this.wheel.length) {
            throw new IllegalArgumentException(String.format("tickDuration: %d (expected: 0 < tickDuration in nanos < %d", tickDuration, Long.MAX_VALUE / this.wheel.length));
        }
        this.workerThread = threadFactory.newThread(this.worker);
        this.leak = HashedWheelTimer.leakDetector.open(this);
    }
    
    private static HashedWheelBucket[] createWheel(int ticksPerWheel) {
        if (ticksPerWheel <= 0) {
            throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + ticksPerWheel);
        }
        if (ticksPerWheel > 1073741824) {
            throw new IllegalArgumentException("ticksPerWheel may not be greater than 2^30: " + ticksPerWheel);
        }
        ticksPerWheel = normalizeTicksPerWheel(ticksPerWheel);
        final HashedWheelBucket[] wheel = new HashedWheelBucket[ticksPerWheel];
        for (int i = 0; i < wheel.length; ++i) {
            wheel[i] = new HashedWheelBucket();
        }
        return wheel;
    }
    
    private static int normalizeTicksPerWheel(final int ticksPerWheel) {
        int normalizedTicksPerWheel;
        for (normalizedTicksPerWheel = 1; normalizedTicksPerWheel < ticksPerWheel; normalizedTicksPerWheel <<= 1) {}
        return normalizedTicksPerWheel;
    }
    
    public void start() {
        switch (HashedWheelTimer.WORKER_STATE_UPDATER.get(this)) {
            case 0: {
                if (HashedWheelTimer.WORKER_STATE_UPDATER.compareAndSet(this, 0, 1)) {
                    this.workerThread.start();
                    break;
                }
                break;
            }
            case 1: {
                break;
            }
            case 2: {
                throw new IllegalStateException("cannot be started once stopped");
            }
            default: {
                throw new Error("Invalid WorkerState");
            }
        }
        while (this.startTime == 0L) {
            try {
                this.startTimeInitialized.await();
            }
            catch (InterruptedException ignore) {}
        }
    }
    
    @Override
    public Set<Timeout> stop() {
        if (Thread.currentThread() == this.workerThread) {
            throw new IllegalStateException(HashedWheelTimer.class.getSimpleName() + ".stop() cannot be called from " + TimerTask.class.getSimpleName());
        }
        if (!HashedWheelTimer.WORKER_STATE_UPDATER.compareAndSet(this, 1, 2)) {
            HashedWheelTimer.WORKER_STATE_UPDATER.set(this, 2);
            if (this.leak != null) {
                this.leak.close();
            }
            return Collections.emptySet();
        }
        boolean interrupted = false;
        while (this.workerThread.isAlive()) {
            this.workerThread.interrupt();
            try {
                this.workerThread.join(100L);
            }
            catch (InterruptedException ignored) {
                interrupted = true;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        if (this.leak != null) {
            this.leak.close();
        }
        return this.worker.unprocessedTimeouts();
    }
    
    @Override
    public Timeout newTimeout(final TimerTask task, final long delay, final TimeUnit unit) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        this.start();
        final long deadline = System.nanoTime() + unit.toNanos(delay) - this.startTime;
        final HashedWheelTimeout timeout = new HashedWheelTimeout(this, task, deadline);
        this.timeouts.add(timeout);
        return timeout;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(HashedWheelTimer.class);
        leakDetector = new ResourceLeakDetector<HashedWheelTimer>(HashedWheelTimer.class, 1, Runtime.getRuntime().availableProcessors() * 4);
        AtomicIntegerFieldUpdater<HashedWheelTimer> workerStateUpdater = PlatformDependent.newAtomicIntegerFieldUpdater(HashedWheelTimer.class, "workerState");
        if (workerStateUpdater == null) {
            workerStateUpdater = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimer.class, "workerState");
        }
        WORKER_STATE_UPDATER = workerStateUpdater;
    }
    
    private final class Worker implements Runnable
    {
        private final Set<Timeout> unprocessedTimeouts;
        private long tick;
        
        private Worker() {
            this.unprocessedTimeouts = new HashSet<Timeout>();
        }
        
        @Override
        public void run() {
            HashedWheelTimer.this.startTime = System.nanoTime();
            if (HashedWheelTimer.this.startTime == 0L) {
                HashedWheelTimer.this.startTime = 1L;
            }
            HashedWheelTimer.this.startTimeInitialized.countDown();
            do {
                final long deadline = this.waitForNextTick();
                if (deadline > 0L) {
                    final int idx = (int)(this.tick & (long)HashedWheelTimer.this.mask);
                    this.processCancelledTasks();
                    final HashedWheelBucket bucket = HashedWheelTimer.this.wheel[idx];
                    this.transferTimeoutsToBuckets();
                    bucket.expireTimeouts(deadline);
                    ++this.tick;
                }
            } while (HashedWheelTimer.WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 1);
            final HashedWheelBucket[] arr$ = HashedWheelTimer.this.wheel;
            for (int len$ = arr$.length, i$ = 0; i$ < len$; ++i$) {
                final HashedWheelBucket bucket = arr$[i$];
                bucket.clearTimeouts(this.unprocessedTimeouts);
            }
            while (true) {
                final HashedWheelTimeout timeout = HashedWheelTimer.this.timeouts.poll();
                if (timeout == null) {
                    break;
                }
                if (timeout.isCancelled()) {
                    continue;
                }
                this.unprocessedTimeouts.add(timeout);
            }
            this.processCancelledTasks();
        }
        
        private void transferTimeoutsToBuckets() {
            for (int i = 0; i < 100000; ++i) {
                final HashedWheelTimeout timeout = HashedWheelTimer.this.timeouts.poll();
                if (timeout == null) {
                    break;
                }
                if (timeout.state() != 1) {
                    final long calculated = timeout.deadline / HashedWheelTimer.this.tickDuration;
                    timeout.remainingRounds = (calculated - this.tick) / HashedWheelTimer.this.wheel.length;
                    final long ticks = Math.max(calculated, this.tick);
                    final int stopIndex = (int)(ticks & (long)HashedWheelTimer.this.mask);
                    final HashedWheelBucket bucket = HashedWheelTimer.this.wheel[stopIndex];
                    bucket.addTimeout(timeout);
                }
            }
        }
        
        private void processCancelledTasks() {
            while (true) {
                final Runnable task = HashedWheelTimer.this.cancelledTimeouts.poll();
                if (task == null) {
                    break;
                }
                try {
                    task.run();
                }
                catch (Throwable t) {
                    if (!HashedWheelTimer.logger.isWarnEnabled()) {
                        continue;
                    }
                    HashedWheelTimer.logger.warn("An exception was thrown while process a cancellation task", t);
                }
            }
        }
        
        private long waitForNextTick() {
            final long deadline = HashedWheelTimer.this.tickDuration * (this.tick + 1L);
            long currentTime;
            while (true) {
                currentTime = System.nanoTime() - HashedWheelTimer.this.startTime;
                long sleepTimeMs = (deadline - currentTime + 999999L) / 1000000L;
                if (sleepTimeMs <= 0L) {
                    break;
                }
                if (PlatformDependent.isWindows()) {
                    sleepTimeMs = sleepTimeMs / 10L * 10L;
                }
                try {
                    Thread.sleep(sleepTimeMs);
                }
                catch (InterruptedException ignored) {
                    if (HashedWheelTimer.WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 2) {
                        return Long.MIN_VALUE;
                    }
                    continue;
                }
            }
            if (currentTime == Long.MIN_VALUE) {
                return -9223372036854775807L;
            }
            return currentTime;
        }
        
        public Set<Timeout> unprocessedTimeouts() {
            return Collections.unmodifiableSet((Set<? extends Timeout>)this.unprocessedTimeouts);
        }
    }
    
    private static final class HashedWheelTimeout extends MpscLinkedQueueNode<Timeout> implements Timeout
    {
        private static final int ST_INIT = 0;
        private static final int ST_CANCELLED = 1;
        private static final int ST_EXPIRED = 2;
        private static final AtomicIntegerFieldUpdater<HashedWheelTimeout> STATE_UPDATER;
        private final HashedWheelTimer timer;
        private final TimerTask task;
        private final long deadline;
        private volatile int state;
        long remainingRounds;
        HashedWheelTimeout next;
        HashedWheelTimeout prev;
        HashedWheelBucket bucket;
        
        HashedWheelTimeout(final HashedWheelTimer timer, final TimerTask task, final long deadline) {
            this.state = 0;
            this.timer = timer;
            this.task = task;
            this.deadline = deadline;
        }
        
        @Override
        public Timer timer() {
            return this.timer;
        }
        
        @Override
        public TimerTask task() {
            return this.task;
        }
        
        @Override
        public boolean cancel() {
            if (!this.compareAndSetState(0, 1)) {
                return false;
            }
            this.timer.cancelledTimeouts.add(new Runnable() {
                @Override
                public void run() {
                    final HashedWheelBucket bucket = HashedWheelTimeout.this.bucket;
                    if (bucket != null) {
                        bucket.remove(HashedWheelTimeout.this);
                    }
                }
            });
            return true;
        }
        
        public boolean compareAndSetState(final int expected, final int state) {
            return HashedWheelTimeout.STATE_UPDATER.compareAndSet(this, expected, state);
        }
        
        public int state() {
            return this.state;
        }
        
        @Override
        public boolean isCancelled() {
            return this.state() == 1;
        }
        
        @Override
        public boolean isExpired() {
            return this.state() == 2;
        }
        
        @Override
        public HashedWheelTimeout value() {
            return this;
        }
        
        public void expire() {
            if (!this.compareAndSetState(0, 2)) {
                return;
            }
            try {
                this.task.run(this);
            }
            catch (Throwable t) {
                if (HashedWheelTimer.logger.isWarnEnabled()) {
                    HashedWheelTimer.logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);
                }
            }
        }
        
        @Override
        public String toString() {
            final long currentTime = System.nanoTime();
            final long remaining = this.deadline - currentTime + this.timer.startTime;
            final StringBuilder buf = new StringBuilder(192);
            buf.append(StringUtil.simpleClassName(this));
            buf.append('(');
            buf.append("deadline: ");
            if (remaining > 0L) {
                buf.append(remaining);
                buf.append(" ns later");
            }
            else if (remaining < 0L) {
                buf.append(-remaining);
                buf.append(" ns ago");
            }
            else {
                buf.append("now");
            }
            if (this.isCancelled()) {
                buf.append(", cancelled");
            }
            buf.append(", task: ");
            buf.append(this.task());
            return buf.append(')').toString();
        }
        
        static {
            AtomicIntegerFieldUpdater<HashedWheelTimeout> updater = PlatformDependent.newAtomicIntegerFieldUpdater(HashedWheelTimeout.class, "state");
            if (updater == null) {
                updater = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimeout.class, "state");
            }
            STATE_UPDATER = updater;
        }
    }
    
    private static final class HashedWheelBucket
    {
        private HashedWheelTimeout head;
        private HashedWheelTimeout tail;
        
        public void addTimeout(final HashedWheelTimeout timeout) {
            assert timeout.bucket == null;
            timeout.bucket = this;
            if (this.head == null) {
                this.tail = timeout;
                this.head = timeout;
            }
            else {
                this.tail.next = timeout;
                timeout.prev = this.tail;
                this.tail = timeout;
            }
        }
        
        public void expireTimeouts(final long deadline) {
            HashedWheelTimeout next;
            for (HashedWheelTimeout timeout = this.head; timeout != null; timeout = next) {
                boolean remove = false;
                if (timeout.remainingRounds <= 0L) {
                    if (timeout.deadline > deadline) {
                        throw new IllegalStateException(String.format("timeout.deadline (%d) > deadline (%d)", timeout.deadline, deadline));
                    }
                    timeout.expire();
                    remove = true;
                }
                else if (timeout.isCancelled()) {
                    remove = true;
                }
                else {
                    final HashedWheelTimeout hashedWheelTimeout = timeout;
                    --hashedWheelTimeout.remainingRounds;
                }
                next = timeout.next;
                if (remove) {
                    this.remove(timeout);
                }
            }
        }
        
        public void remove(final HashedWheelTimeout timeout) {
            final HashedWheelTimeout next = timeout.next;
            if (timeout.prev != null) {
                timeout.prev.next = next;
            }
            if (timeout.next != null) {
                timeout.next.prev = timeout.prev;
            }
            if (timeout == this.head) {
                if (timeout == this.tail) {
                    this.tail = null;
                    this.head = null;
                }
                else {
                    this.head = next;
                }
            }
            else if (timeout == this.tail) {
                this.tail = timeout.prev;
            }
            timeout.prev = null;
            timeout.next = null;
            timeout.bucket = null;
        }
        
        public void clearTimeouts(final Set<Timeout> set) {
            while (true) {
                final HashedWheelTimeout timeout = this.pollTimeout();
                if (timeout == null) {
                    break;
                }
                if (timeout.isExpired()) {
                    continue;
                }
                if (timeout.isCancelled()) {
                    continue;
                }
                set.add(timeout);
            }
        }
        
        private HashedWheelTimeout pollTimeout() {
            final HashedWheelTimeout head = this.head;
            if (head == null) {
                return null;
            }
            final HashedWheelTimeout next = head.next;
            if (next == null) {
                final HashedWheelTimeout hashedWheelTimeout = null;
                this.head = hashedWheelTimeout;
                this.tail = hashedWheelTimeout;
            }
            else {
                this.head = next;
                next.prev = null;
            }
            head.next = null;
            head.prev = null;
            head.bucket = null;
            return head;
        }
    }
}
