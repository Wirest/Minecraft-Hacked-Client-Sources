// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.epoll;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import io.netty.util.internal.PlatformDependent;
import java.util.Queue;
import io.netty.util.collection.IntObjectHashMap;
import java.util.concurrent.ThreadFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.util.collection.IntObjectMap;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.SingleThreadEventLoop;

final class EpollEventLoop extends SingleThreadEventLoop
{
    private static final InternalLogger logger;
    private static final AtomicIntegerFieldUpdater<EpollEventLoop> WAKEN_UP_UPDATER;
    private final int epollFd;
    private final int eventFd;
    private final IntObjectMap<AbstractEpollChannel> ids;
    private final long[] events;
    private int id;
    private boolean overflown;
    private volatile int wakenUp;
    private volatile int ioRatio;
    
    EpollEventLoop(final EventLoopGroup parent, final ThreadFactory threadFactory, final int maxEvents) {
        super(parent, threadFactory, false);
        this.ids = new IntObjectHashMap<AbstractEpollChannel>();
        this.ioRatio = 50;
        this.events = new long[maxEvents];
        boolean success = false;
        int epollFd = -1;
        int eventFd = -1;
        try {
            epollFd = (this.epollFd = Native.epollCreate());
            eventFd = (this.eventFd = Native.eventFd());
            Native.epollCtlAdd(epollFd, eventFd, 1, 0);
            success = true;
        }
        finally {
            if (!success) {
                if (epollFd != -1) {
                    try {
                        Native.close(epollFd);
                    }
                    catch (Exception ex) {}
                }
                if (eventFd != -1) {
                    try {
                        Native.close(eventFd);
                    }
                    catch (Exception ex2) {}
                }
            }
        }
    }
    
    private int nextId() {
        int id = this.id;
        if (id == Integer.MAX_VALUE) {
            this.overflown = true;
            id = 0;
        }
        if (this.overflown) {
            while (this.ids.containsKey(++id)) {}
            this.id = id;
        }
        else {
            this.id = ++id;
        }
        return id;
    }
    
    @Override
    protected void wakeup(final boolean inEventLoop) {
        if (!inEventLoop && EpollEventLoop.WAKEN_UP_UPDATER.compareAndSet(this, 0, 1)) {
            Native.eventFdWrite(this.eventFd, 1L);
        }
    }
    
    void add(final AbstractEpollChannel ch) {
        assert this.inEventLoop();
        final int id = this.nextId();
        Native.epollCtlAdd(this.epollFd, ch.fd, ch.flags, id);
        ch.id = id;
        this.ids.put(id, ch);
    }
    
    void modify(final AbstractEpollChannel ch) {
        assert this.inEventLoop();
        Native.epollCtlMod(this.epollFd, ch.fd, ch.flags, ch.id);
    }
    
    void remove(final AbstractEpollChannel ch) {
        assert this.inEventLoop();
        if (this.ids.remove(ch.id) != null && ch.isOpen()) {
            Native.epollCtlDel(this.epollFd, ch.fd);
        }
    }
    
    @Override
    protected Queue<Runnable> newTaskQueue() {
        return PlatformDependent.newMpscQueue();
    }
    
    public int getIoRatio() {
        return this.ioRatio;
    }
    
    public void setIoRatio(final int ioRatio) {
        if (ioRatio <= 0 || ioRatio > 100) {
            throw new IllegalArgumentException("ioRatio: " + ioRatio + " (expected: 0 < ioRatio <= 100)");
        }
        this.ioRatio = ioRatio;
    }
    
    private int epollWait(final boolean oldWakenUp) {
        int selectCnt = 0;
        long currentTimeNanos = System.nanoTime();
        final long selectDeadLineNanos = currentTimeNanos + this.delayNanos(currentTimeNanos);
        while (true) {
            final long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
            if (timeoutMillis <= 0L) {
                if (selectCnt == 0) {
                    final int ready = Native.epollWait(this.epollFd, this.events, 0);
                    if (ready > 0) {
                        return ready;
                    }
                }
                return 0;
            }
            final int selectedKeys = Native.epollWait(this.epollFd, this.events, (int)timeoutMillis);
            ++selectCnt;
            if (selectedKeys != 0 || oldWakenUp || this.wakenUp == 1 || this.hasTasks() || this.hasScheduledTasks()) {
                return selectedKeys;
            }
            currentTimeNanos = System.nanoTime();
        }
    }
    
    @Override
    protected void run() {
        while (true) {
            final boolean oldWakenUp = EpollEventLoop.WAKEN_UP_UPDATER.getAndSet(this, 0) == 1;
            try {
                int ready;
                if (this.hasTasks()) {
                    ready = Native.epollWait(this.epollFd, this.events, 0);
                }
                else {
                    ready = this.epollWait(oldWakenUp);
                    if (this.wakenUp == 1) {
                        Native.eventFdWrite(this.eventFd, 1L);
                    }
                }
                final int ioRatio = this.ioRatio;
                if (ioRatio == 100) {
                    if (ready > 0) {
                        this.processReady(this.events, ready);
                    }
                    this.runAllTasks();
                }
                else {
                    final long ioStartTime = System.nanoTime();
                    if (ready > 0) {
                        this.processReady(this.events, ready);
                    }
                    final long ioTime = System.nanoTime() - ioStartTime;
                    this.runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
                }
                if (!this.isShuttingDown()) {
                    continue;
                }
                this.closeAll();
                if (this.confirmShutdown()) {
                    break;
                }
                continue;
            }
            catch (Throwable t) {
                EpollEventLoop.logger.warn("Unexpected exception in the selector loop.", t);
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException ex) {}
            }
        }
    }
    
    private void closeAll() {
        Native.epollWait(this.epollFd, this.events, 0);
        final Collection<AbstractEpollChannel> channels = new ArrayList<AbstractEpollChannel>(this.ids.size());
        for (final IntObjectMap.Entry<AbstractEpollChannel> entry : this.ids.entries()) {
            channels.add(entry.value());
        }
        for (final AbstractEpollChannel ch : channels) {
            ch.unsafe().close(ch.unsafe().voidPromise());
        }
    }
    
    private void processReady(final long[] events, final int ready) {
        for (final long ev : events) {
            final int id = (int)(ev >> 32);
            if (id == 0) {
                Native.eventFdRead(this.eventFd);
            }
            else {
                final boolean read = (ev & 0x1L) != 0x0L;
                final boolean write = (ev & 0x2L) != 0x0L;
                final boolean close = (ev & 0x8L) != 0x0L;
                final AbstractEpollChannel ch = this.ids.get(id);
                if (ch != null) {
                    final AbstractEpollChannel.AbstractEpollUnsafe unsafe = (AbstractEpollChannel.AbstractEpollUnsafe)ch.unsafe();
                    if (write && ch.isOpen()) {
                        unsafe.epollOutReady();
                    }
                    if (read && ch.isOpen()) {
                        unsafe.epollInReady();
                    }
                    if (close && ch.isOpen()) {
                        unsafe.epollRdHupReady();
                    }
                }
            }
        }
    }
    
    @Override
    protected void cleanup() {
        try {
            Native.close(this.epollFd);
        }
        catch (IOException e) {
            EpollEventLoop.logger.warn("Failed to close the epoll fd.", e);
        }
        try {
            Native.close(this.eventFd);
        }
        catch (IOException e) {
            EpollEventLoop.logger.warn("Failed to close the event fd.", e);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(EpollEventLoop.class);
        AtomicIntegerFieldUpdater<EpollEventLoop> updater = PlatformDependent.newAtomicIntegerFieldUpdater(EpollEventLoop.class, "wakenUp");
        if (updater == null) {
            updater = AtomicIntegerFieldUpdater.newUpdater(EpollEventLoop.class, "wakenUp");
        }
        WAKEN_UP_UPDATER = updater;
    }
}
