// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.concurrent;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;

public class TimedSemaphore
{
    public static final int NO_LIMIT = 0;
    private static final int THREAD_POOL_SIZE = 1;
    private final ScheduledExecutorService executorService;
    private final long period;
    private final TimeUnit unit;
    private final boolean ownExecutor;
    private ScheduledFuture<?> task;
    private long totalAcquireCount;
    private long periodCount;
    private int limit;
    private int acquireCount;
    private int lastCallsPerPeriod;
    private boolean shutdown;
    
    public TimedSemaphore(final long timePeriod, final TimeUnit timeUnit, final int limit) {
        this(null, timePeriod, timeUnit, limit);
    }
    
    public TimedSemaphore(final ScheduledExecutorService service, final long timePeriod, final TimeUnit timeUnit, final int limit) {
        if (timePeriod <= 0L) {
            throw new IllegalArgumentException("Time period must be greater 0!");
        }
        this.period = timePeriod;
        this.unit = timeUnit;
        if (service != null) {
            this.executorService = service;
            this.ownExecutor = false;
        }
        else {
            final ScheduledThreadPoolExecutor s = new ScheduledThreadPoolExecutor(1);
            s.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
            s.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
            this.executorService = s;
            this.ownExecutor = true;
        }
        this.setLimit(limit);
    }
    
    public final synchronized int getLimit() {
        return this.limit;
    }
    
    public final synchronized void setLimit(final int limit) {
        this.limit = limit;
    }
    
    public synchronized void shutdown() {
        if (!this.shutdown) {
            if (this.ownExecutor) {
                this.getExecutorService().shutdownNow();
            }
            if (this.task != null) {
                this.task.cancel(false);
            }
            this.shutdown = true;
        }
    }
    
    public synchronized boolean isShutdown() {
        return this.shutdown;
    }
    
    public synchronized void acquire() throws InterruptedException {
        if (this.isShutdown()) {
            throw new IllegalStateException("TimedSemaphore is shut down!");
        }
        if (this.task == null) {
            this.task = this.startTimer();
        }
        boolean canPass = false;
        do {
            canPass = (this.getLimit() <= 0 || this.acquireCount < this.getLimit());
            if (!canPass) {
                this.wait();
            }
            else {
                ++this.acquireCount;
            }
        } while (!canPass);
    }
    
    public synchronized int getLastAcquiresPerPeriod() {
        return this.lastCallsPerPeriod;
    }
    
    public synchronized int getAcquireCount() {
        return this.acquireCount;
    }
    
    public synchronized int getAvailablePermits() {
        return this.getLimit() - this.getAcquireCount();
    }
    
    public synchronized double getAverageCallsPerPeriod() {
        return (this.periodCount == 0L) ? 0.0 : (this.totalAcquireCount / (double)this.periodCount);
    }
    
    public long getPeriod() {
        return this.period;
    }
    
    public TimeUnit getUnit() {
        return this.unit;
    }
    
    protected ScheduledExecutorService getExecutorService() {
        return this.executorService;
    }
    
    protected ScheduledFuture<?> startTimer() {
        return this.getExecutorService().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                TimedSemaphore.this.endOfPeriod();
            }
        }, this.getPeriod(), this.getPeriod(), this.getUnit());
    }
    
    synchronized void endOfPeriod() {
        this.lastCallsPerPeriod = this.acquireCount;
        this.totalAcquireCount += this.acquireCount;
        ++this.periodCount;
        this.acquireCount = 0;
        this.notifyAll();
    }
}
