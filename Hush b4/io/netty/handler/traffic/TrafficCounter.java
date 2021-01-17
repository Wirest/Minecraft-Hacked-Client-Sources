// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.traffic;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import io.netty.util.internal.logging.InternalLogger;

public class TrafficCounter
{
    private static final InternalLogger logger;
    private final AtomicLong currentWrittenBytes;
    private final AtomicLong currentReadBytes;
    private final AtomicLong cumulativeWrittenBytes;
    private final AtomicLong cumulativeReadBytes;
    private long lastCumulativeTime;
    private long lastWriteThroughput;
    private long lastReadThroughput;
    private final AtomicLong lastTime;
    private long lastWrittenBytes;
    private long lastReadBytes;
    private long lastNonNullWrittenBytes;
    private long lastNonNullWrittenTime;
    private long lastNonNullReadTime;
    private long lastNonNullReadBytes;
    final AtomicLong checkInterval;
    final String name;
    private final AbstractTrafficShapingHandler trafficShapingHandler;
    private final ScheduledExecutorService executor;
    private Runnable monitor;
    private volatile ScheduledFuture<?> scheduledFuture;
    final AtomicBoolean monitorActive;
    
    public synchronized void start() {
        if (this.monitorActive.get()) {
            return;
        }
        this.lastTime.set(System.currentTimeMillis());
        if (this.checkInterval.get() > 0L) {
            this.monitorActive.set(true);
            this.monitor = new TrafficMonitoringTask(this.trafficShapingHandler, this);
            this.scheduledFuture = this.executor.schedule(this.monitor, this.checkInterval.get(), TimeUnit.MILLISECONDS);
        }
    }
    
    public synchronized void stop() {
        if (!this.monitorActive.get()) {
            return;
        }
        this.monitorActive.set(false);
        this.resetAccounting(System.currentTimeMillis());
        if (this.trafficShapingHandler != null) {
            this.trafficShapingHandler.doAccounting(this);
        }
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(true);
        }
    }
    
    synchronized void resetAccounting(final long newLastTime) {
        final long interval = newLastTime - this.lastTime.getAndSet(newLastTime);
        if (interval == 0L) {
            return;
        }
        if (TrafficCounter.logger.isDebugEnabled() && interval > 2L * this.checkInterval()) {
            TrafficCounter.logger.debug("Acct schedule not ok: " + interval + " > 2*" + this.checkInterval() + " from " + this.name);
        }
        this.lastReadBytes = this.currentReadBytes.getAndSet(0L);
        this.lastWrittenBytes = this.currentWrittenBytes.getAndSet(0L);
        this.lastReadThroughput = this.lastReadBytes / interval * 1000L;
        this.lastWriteThroughput = this.lastWrittenBytes / interval * 1000L;
        if (this.lastWrittenBytes > 0L) {
            this.lastNonNullWrittenBytes = this.lastWrittenBytes;
            this.lastNonNullWrittenTime = newLastTime;
        }
        if (this.lastReadBytes > 0L) {
            this.lastNonNullReadBytes = this.lastReadBytes;
            this.lastNonNullReadTime = newLastTime;
        }
    }
    
    public TrafficCounter(final AbstractTrafficShapingHandler trafficShapingHandler, final ScheduledExecutorService executor, final String name, final long checkInterval) {
        this.currentWrittenBytes = new AtomicLong();
        this.currentReadBytes = new AtomicLong();
        this.cumulativeWrittenBytes = new AtomicLong();
        this.cumulativeReadBytes = new AtomicLong();
        this.lastTime = new AtomicLong();
        this.checkInterval = new AtomicLong(1000L);
        this.monitorActive = new AtomicBoolean();
        this.trafficShapingHandler = trafficShapingHandler;
        this.executor = executor;
        this.name = name;
        this.lastCumulativeTime = System.currentTimeMillis();
        this.configure(checkInterval);
    }
    
    public void configure(final long newcheckInterval) {
        final long newInterval = newcheckInterval / 10L * 10L;
        if (this.checkInterval.get() != newInterval) {
            this.checkInterval.set(newInterval);
            if (newInterval <= 0L) {
                this.stop();
                this.lastTime.set(System.currentTimeMillis());
            }
            else {
                this.start();
            }
        }
    }
    
    void bytesRecvFlowControl(final long recv) {
        this.currentReadBytes.addAndGet(recv);
        this.cumulativeReadBytes.addAndGet(recv);
    }
    
    void bytesWriteFlowControl(final long write) {
        this.currentWrittenBytes.addAndGet(write);
        this.cumulativeWrittenBytes.addAndGet(write);
    }
    
    public long checkInterval() {
        return this.checkInterval.get();
    }
    
    public long lastReadThroughput() {
        return this.lastReadThroughput;
    }
    
    public long lastWriteThroughput() {
        return this.lastWriteThroughput;
    }
    
    public long lastReadBytes() {
        return this.lastReadBytes;
    }
    
    public long lastWrittenBytes() {
        return this.lastWrittenBytes;
    }
    
    public long currentReadBytes() {
        return this.currentReadBytes.get();
    }
    
    public long currentWrittenBytes() {
        return this.currentWrittenBytes.get();
    }
    
    public long lastTime() {
        return this.lastTime.get();
    }
    
    public long cumulativeWrittenBytes() {
        return this.cumulativeWrittenBytes.get();
    }
    
    public long cumulativeReadBytes() {
        return this.cumulativeReadBytes.get();
    }
    
    public long lastCumulativeTime() {
        return this.lastCumulativeTime;
    }
    
    public void resetCumulativeTime() {
        this.lastCumulativeTime = System.currentTimeMillis();
        this.cumulativeReadBytes.set(0L);
        this.cumulativeWrittenBytes.set(0L);
    }
    
    public String name() {
        return this.name;
    }
    
    public synchronized long readTimeToWait(final long size, final long limitTraffic, final long maxTime) {
        final long now = System.currentTimeMillis();
        this.bytesRecvFlowControl(size);
        if (limitTraffic == 0L) {
            return 0L;
        }
        long sum = this.currentReadBytes.get();
        final long interval = now - this.lastTime.get();
        if (interval <= 10L || sum <= 0L) {
            if (this.lastNonNullReadBytes > 0L && this.lastNonNullReadTime + 10L < now) {
                final long lastsum = sum + this.lastNonNullReadBytes;
                final long lastinterval = now - this.lastNonNullReadTime;
                final long time = (lastsum * 1000L / limitTraffic - lastinterval) / 10L * 10L;
                if (time > 10L) {
                    if (TrafficCounter.logger.isDebugEnabled()) {
                        TrafficCounter.logger.debug("Time: " + time + ":" + lastsum + ":" + lastinterval);
                    }
                    return (time > maxTime) ? maxTime : time;
                }
            }
            else {
                sum += this.lastReadBytes;
                final long lastinterval2 = 10L;
                final long time2 = (sum * 1000L / limitTraffic - lastinterval2) / 10L * 10L;
                if (time2 > 10L) {
                    if (TrafficCounter.logger.isDebugEnabled()) {
                        TrafficCounter.logger.debug("Time: " + time2 + ":" + sum + ":" + lastinterval2);
                    }
                    return (time2 > maxTime) ? maxTime : time2;
                }
            }
            return 0L;
        }
        final long time3 = (sum * 1000L / limitTraffic - interval) / 10L * 10L;
        if (time3 > 10L) {
            if (TrafficCounter.logger.isDebugEnabled()) {
                TrafficCounter.logger.debug("Time: " + time3 + ":" + sum + ":" + interval);
            }
            return (time3 > maxTime) ? maxTime : time3;
        }
        return 0L;
    }
    
    public synchronized long writeTimeToWait(final long size, final long limitTraffic, final long maxTime) {
        this.bytesWriteFlowControl(size);
        if (limitTraffic == 0L) {
            return 0L;
        }
        long sum = this.currentWrittenBytes.get();
        final long now = System.currentTimeMillis();
        final long interval = now - this.lastTime.get();
        if (interval <= 10L || sum <= 0L) {
            if (this.lastNonNullWrittenBytes > 0L && this.lastNonNullWrittenTime + 10L < now) {
                final long lastsum = sum + this.lastNonNullWrittenBytes;
                final long lastinterval = now - this.lastNonNullWrittenTime;
                final long time = (lastsum * 1000L / limitTraffic - lastinterval) / 10L * 10L;
                if (time > 10L) {
                    if (TrafficCounter.logger.isDebugEnabled()) {
                        TrafficCounter.logger.debug("Time: " + time + ":" + lastsum + ":" + lastinterval);
                    }
                    return (time > maxTime) ? maxTime : time;
                }
            }
            else {
                sum += this.lastWrittenBytes;
                final long lastinterval2 = 10L + Math.abs(interval);
                final long time2 = (sum * 1000L / limitTraffic - lastinterval2) / 10L * 10L;
                if (time2 > 10L) {
                    if (TrafficCounter.logger.isDebugEnabled()) {
                        TrafficCounter.logger.debug("Time: " + time2 + ":" + sum + ":" + lastinterval2);
                    }
                    return (time2 > maxTime) ? maxTime : time2;
                }
            }
            return 0L;
        }
        final long time3 = (sum * 1000L / limitTraffic - interval) / 10L * 10L;
        if (time3 > 10L) {
            if (TrafficCounter.logger.isDebugEnabled()) {
                TrafficCounter.logger.debug("Time: " + time3 + ":" + sum + ":" + interval);
            }
            return (time3 > maxTime) ? maxTime : time3;
        }
        return 0L;
    }
    
    @Override
    public String toString() {
        return "Monitor " + this.name + " Current Speed Read: " + (this.lastReadThroughput >> 10) + " KB/s, Write: " + (this.lastWriteThroughput >> 10) + " KB/s Current Read: " + (this.currentReadBytes.get() >> 10) + " KB Current Write: " + (this.currentWrittenBytes.get() >> 10) + " KB";
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(TrafficCounter.class);
    }
    
    private static class TrafficMonitoringTask implements Runnable
    {
        private final AbstractTrafficShapingHandler trafficShapingHandler1;
        private final TrafficCounter counter;
        
        protected TrafficMonitoringTask(final AbstractTrafficShapingHandler trafficShapingHandler, final TrafficCounter counter) {
            this.trafficShapingHandler1 = trafficShapingHandler;
            this.counter = counter;
        }
        
        @Override
        public void run() {
            if (!this.counter.monitorActive.get()) {
                return;
            }
            final long endTime = System.currentTimeMillis();
            this.counter.resetAccounting(endTime);
            if (this.trafficShapingHandler1 != null) {
                this.trafficShapingHandler1.doAccounting(this.counter);
            }
            this.counter.scheduledFuture = this.counter.executor.schedule(this, this.counter.checkInterval.get(), TimeUnit.MILLISECONDS);
        }
    }
}
