// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.locks.Condition;
import com.google.common.base.Throwables;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.GuardedBy;
import java.util.concurrent.locks.ReentrantLock;
import com.google.common.annotations.Beta;

@Beta
public final class Monitor
{
    private final boolean fair;
    private final ReentrantLock lock;
    @GuardedBy("lock")
    private Guard activeGuards;
    
    public Monitor() {
        this(false);
    }
    
    public Monitor(final boolean fair) {
        this.activeGuards = null;
        this.fair = fair;
        this.lock = new ReentrantLock(fair);
    }
    
    public void enter() {
        this.lock.lock();
    }
    
    public void enterInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }
    
    public boolean enter(final long time, final TimeUnit unit) {
        long timeoutNanos = unit.toNanos(time);
        final ReentrantLock lock = this.lock;
        if (!this.fair && lock.tryLock()) {
            return true;
        }
        final long deadline = System.nanoTime() + timeoutNanos;
        boolean interrupted = Thread.interrupted();
        try {
            return lock.tryLock(timeoutNanos, TimeUnit.NANOSECONDS);
        }
        catch (InterruptedException interrupt) {
            interrupted = true;
            timeoutNanos = deadline - System.nanoTime();
            return lock.tryLock(timeoutNanos, TimeUnit.NANOSECONDS);
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public boolean enterInterruptibly(final long time, final TimeUnit unit) throws InterruptedException {
        return this.lock.tryLock(time, unit);
    }
    
    public boolean tryEnter() {
        return this.lock.tryLock();
    }
    
    public void enterWhen(final Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        final boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        lock.lockInterruptibly();
        boolean satisfied = false;
        try {
            if (!guard.isSatisfied()) {
                this.await(guard, signalBeforeWaiting);
            }
            satisfied = true;
        }
        finally {
            if (!satisfied) {
                this.leave();
            }
        }
    }
    
    public void enterWhenUninterruptibly(final Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        final boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        lock.lock();
        boolean satisfied = false;
        try {
            if (!guard.isSatisfied()) {
                this.awaitUninterruptibly(guard, signalBeforeWaiting);
            }
            satisfied = true;
        }
        finally {
            if (!satisfied) {
                this.leave();
            }
        }
    }
    
    public boolean enterWhen(final Guard guard, final long time, final TimeUnit unit) throws InterruptedException {
        long timeoutNanos = unit.toNanos(time);
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        final boolean reentrant = lock.isHeldByCurrentThread();
        if (this.fair || !lock.tryLock()) {
            final long deadline = System.nanoTime() + timeoutNanos;
            if (!lock.tryLock(time, unit)) {
                return false;
            }
            timeoutNanos = deadline - System.nanoTime();
        }
        boolean satisfied = false;
        boolean threw = true;
        try {
            satisfied = (guard.isSatisfied() || this.awaitNanos(guard, timeoutNanos, reentrant));
            threw = false;
            return satisfied;
        }
        finally {
            if (!satisfied) {
                try {
                    if (threw && !reentrant) {
                        this.signalNextWaiter();
                    }
                }
                finally {
                    lock.unlock();
                }
            }
        }
    }
    
    public boolean enterWhenUninterruptibly(final Guard guard, final long time, final TimeUnit unit) {
        long timeoutNanos = unit.toNanos(time);
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        final long deadline = System.nanoTime() + timeoutNanos;
        boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        boolean interrupted = Thread.interrupted();
        try {
            if (this.fair || !lock.tryLock()) {
                boolean locked = false;
                do {
                    try {
                        locked = lock.tryLock(timeoutNanos, TimeUnit.NANOSECONDS);
                        if (!locked) {
                            return false;
                        }
                    }
                    catch (InterruptedException interrupt) {
                        interrupted = true;
                    }
                    timeoutNanos = deadline - System.nanoTime();
                } while (!locked);
            }
            boolean satisfied = false;
            try {
                return satisfied = (guard.isSatisfied() || this.awaitNanos(guard, timeoutNanos, signalBeforeWaiting));
            }
            catch (InterruptedException interrupt) {
                interrupted = true;
                signalBeforeWaiting = false;
                timeoutNanos = deadline - System.nanoTime();
            }
            finally {
                if (!satisfied) {
                    lock.unlock();
                }
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public boolean enterIf(final Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        lock.lock();
        boolean satisfied = false;
        try {
            return satisfied = guard.isSatisfied();
        }
        finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }
    
    public boolean enterIfInterruptibly(final Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        boolean satisfied = false;
        try {
            return satisfied = guard.isSatisfied();
        }
        finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }
    
    public boolean enterIf(final Guard guard, final long time, final TimeUnit unit) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        if (!this.enter(time, unit)) {
            return false;
        }
        boolean satisfied = false;
        try {
            return satisfied = guard.isSatisfied();
        }
        finally {
            if (!satisfied) {
                this.lock.unlock();
            }
        }
    }
    
    public boolean enterIfInterruptibly(final Guard guard, final long time, final TimeUnit unit) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        if (!lock.tryLock(time, unit)) {
            return false;
        }
        boolean satisfied = false;
        try {
            return satisfied = guard.isSatisfied();
        }
        finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }
    
    public boolean tryEnterIf(final Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        if (!lock.tryLock()) {
            return false;
        }
        boolean satisfied = false;
        try {
            return satisfied = guard.isSatisfied();
        }
        finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }
    
    public void waitFor(final Guard guard) throws InterruptedException {
        if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (!guard.isSatisfied()) {
            this.await(guard, true);
        }
    }
    
    public void waitForUninterruptibly(final Guard guard) {
        if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (!guard.isSatisfied()) {
            this.awaitUninterruptibly(guard, true);
        }
    }
    
    public boolean waitFor(final Guard guard, final long time, final TimeUnit unit) throws InterruptedException {
        final long timeoutNanos = unit.toNanos(time);
        if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        return guard.isSatisfied() || this.awaitNanos(guard, timeoutNanos, true);
    }
    
    public boolean waitForUninterruptibly(final Guard guard, final long time, final TimeUnit unit) {
        long timeoutNanos = unit.toNanos(time);
        if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (guard.isSatisfied()) {
            return true;
        }
        boolean signalBeforeWaiting = true;
        final long deadline = System.nanoTime() + timeoutNanos;
        boolean interrupted = Thread.interrupted();
        try {
            return this.awaitNanos(guard, timeoutNanos, signalBeforeWaiting);
        }
        catch (InterruptedException interrupt) {
            interrupted = true;
            if (guard.isSatisfied()) {
                return true;
            }
            signalBeforeWaiting = false;
            timeoutNanos = deadline - System.nanoTime();
            return this.awaitNanos(guard, timeoutNanos, signalBeforeWaiting);
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public void leave() {
        final ReentrantLock lock = this.lock;
        try {
            if (lock.getHoldCount() == 1) {
                this.signalNextWaiter();
            }
        }
        finally {
            lock.unlock();
        }
    }
    
    public boolean isFair() {
        return this.fair;
    }
    
    public boolean isOccupied() {
        return this.lock.isLocked();
    }
    
    public boolean isOccupiedByCurrentThread() {
        return this.lock.isHeldByCurrentThread();
    }
    
    public int getOccupiedDepth() {
        return this.lock.getHoldCount();
    }
    
    public int getQueueLength() {
        return this.lock.getQueueLength();
    }
    
    public boolean hasQueuedThreads() {
        return this.lock.hasQueuedThreads();
    }
    
    public boolean hasQueuedThread(final Thread thread) {
        return this.lock.hasQueuedThread(thread);
    }
    
    public boolean hasWaiters(final Guard guard) {
        return this.getWaitQueueLength(guard) > 0;
    }
    
    public int getWaitQueueLength(final Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        this.lock.lock();
        try {
            return guard.waiterCount;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @GuardedBy("lock")
    private void signalNextWaiter() {
        for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
            if (this.isSatisfied(guard)) {
                guard.condition.signal();
                break;
            }
        }
    }
    
    @GuardedBy("lock")
    private boolean isSatisfied(final Guard guard) {
        try {
            return guard.isSatisfied();
        }
        catch (Throwable throwable) {
            this.signalAllWaiters();
            throw Throwables.propagate(throwable);
        }
    }
    
    @GuardedBy("lock")
    private void signalAllWaiters() {
        for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
            guard.condition.signalAll();
        }
    }
    
    @GuardedBy("lock")
    private void beginWaitingFor(final Guard guard) {
        final int waiters = guard.waiterCount++;
        if (waiters == 0) {
            guard.next = this.activeGuards;
            this.activeGuards = guard;
        }
    }
    
    @GuardedBy("lock")
    private void endWaitingFor(final Guard guard) {
        final int waiterCount = guard.waiterCount - 1;
        guard.waiterCount = waiterCount;
        final int waiters = waiterCount;
        if (waiters == 0) {
            Guard p = this.activeGuards;
            Guard pred = null;
            while (p != guard) {
                pred = p;
                p = p.next;
            }
            if (pred == null) {
                this.activeGuards = p.next;
            }
            else {
                pred.next = p.next;
            }
            p.next = null;
        }
    }
    
    @GuardedBy("lock")
    private void await(final Guard guard, final boolean signalBeforeWaiting) throws InterruptedException {
        if (signalBeforeWaiting) {
            this.signalNextWaiter();
        }
        this.beginWaitingFor(guard);
        try {
            do {
                guard.condition.await();
            } while (!guard.isSatisfied());
        }
        finally {
            this.endWaitingFor(guard);
        }
    }
    
    @GuardedBy("lock")
    private void awaitUninterruptibly(final Guard guard, final boolean signalBeforeWaiting) {
        if (signalBeforeWaiting) {
            this.signalNextWaiter();
        }
        this.beginWaitingFor(guard);
        try {
            do {
                guard.condition.awaitUninterruptibly();
            } while (!guard.isSatisfied());
        }
        finally {
            this.endWaitingFor(guard);
        }
    }
    
    @GuardedBy("lock")
    private boolean awaitNanos(final Guard guard, long nanos, final boolean signalBeforeWaiting) throws InterruptedException {
        if (signalBeforeWaiting) {
            this.signalNextWaiter();
        }
        this.beginWaitingFor(guard);
        try {
            while (nanos >= 0L) {
                nanos = guard.condition.awaitNanos(nanos);
                if (guard.isSatisfied()) {
                    return true;
                }
            }
            return false;
        }
        finally {
            this.endWaitingFor(guard);
        }
    }
    
    @Beta
    public abstract static class Guard
    {
        final Monitor monitor;
        final Condition condition;
        @GuardedBy("monitor.lock")
        int waiterCount;
        @GuardedBy("monitor.lock")
        Guard next;
        
        protected Guard(final Monitor monitor) {
            this.waiterCount = 0;
            this.monitor = Preconditions.checkNotNull(monitor, (Object)"monitor");
            this.condition = monitor.lock.newCondition();
        }
        
        public abstract boolean isSatisfied();
    }
}
