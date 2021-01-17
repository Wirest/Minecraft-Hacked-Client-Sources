// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.nio;

import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;
import java.util.Collection;
import java.util.ArrayList;
import java.nio.channels.CancelledKeyException;
import java.util.Set;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.nio.channels.SelectionKey;
import io.netty.channel.EventLoopException;
import java.nio.channels.SelectableChannel;
import java.util.Queue;
import java.lang.reflect.Field;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import io.netty.channel.ChannelException;
import io.netty.channel.EventLoopGroup;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.channels.spi.SelectorProvider;
import java.nio.channels.Selector;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.SingleThreadEventLoop;

public final class NioEventLoop extends SingleThreadEventLoop
{
    private static final InternalLogger logger;
    private static final int CLEANUP_INTERVAL = 256;
    private static final boolean DISABLE_KEYSET_OPTIMIZATION;
    private static final int MIN_PREMATURE_SELECTOR_RETURNS = 3;
    private static final int SELECTOR_AUTO_REBUILD_THRESHOLD;
    Selector selector;
    private SelectedSelectionKeySet selectedKeys;
    private final SelectorProvider provider;
    private final AtomicBoolean wakenUp;
    private volatile int ioRatio;
    private int cancelledKeys;
    private boolean needsToSelectAgain;
    
    NioEventLoop(final NioEventLoopGroup parent, final ThreadFactory threadFactory, final SelectorProvider selectorProvider) {
        super(parent, threadFactory, false);
        this.wakenUp = new AtomicBoolean();
        this.ioRatio = 50;
        if (selectorProvider == null) {
            throw new NullPointerException("selectorProvider");
        }
        this.provider = selectorProvider;
        this.selector = this.openSelector();
    }
    
    private Selector openSelector() {
        Selector selector;
        try {
            selector = this.provider.openSelector();
        }
        catch (IOException e) {
            throw new ChannelException("failed to open a new selector", e);
        }
        if (NioEventLoop.DISABLE_KEYSET_OPTIMIZATION) {
            return selector;
        }
        try {
            final SelectedSelectionKeySet selectedKeySet = new SelectedSelectionKeySet();
            final Class<?> selectorImplClass = Class.forName("sun.nio.ch.SelectorImpl", false, PlatformDependent.getSystemClassLoader());
            if (!selectorImplClass.isAssignableFrom(selector.getClass())) {
                return selector;
            }
            final Field selectedKeysField = selectorImplClass.getDeclaredField("selectedKeys");
            final Field publicSelectedKeysField = selectorImplClass.getDeclaredField("publicSelectedKeys");
            selectedKeysField.setAccessible(true);
            publicSelectedKeysField.setAccessible(true);
            selectedKeysField.set(selector, selectedKeySet);
            publicSelectedKeysField.set(selector, selectedKeySet);
            this.selectedKeys = selectedKeySet;
            NioEventLoop.logger.trace("Instrumented an optimized java.util.Set into: {}", selector);
        }
        catch (Throwable t) {
            this.selectedKeys = null;
            NioEventLoop.logger.trace("Failed to instrument an optimized java.util.Set into: {}", selector, t);
        }
        return selector;
    }
    
    @Override
    protected Queue<Runnable> newTaskQueue() {
        return PlatformDependent.newMpscQueue();
    }
    
    public void register(final SelectableChannel ch, final int interestOps, final NioTask<?> task) {
        if (ch == null) {
            throw new NullPointerException("ch");
        }
        if (interestOps == 0) {
            throw new IllegalArgumentException("interestOps must be non-zero.");
        }
        if ((interestOps & ~ch.validOps()) != 0x0) {
            throw new IllegalArgumentException("invalid interestOps: " + interestOps + "(validOps: " + ch.validOps() + ')');
        }
        if (task == null) {
            throw new NullPointerException("task");
        }
        if (this.isShutdown()) {
            throw new IllegalStateException("event loop shut down");
        }
        try {
            ch.register(this.selector, interestOps, task);
        }
        catch (Exception e) {
            throw new EventLoopException("failed to register a channel", e);
        }
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
    
    public void rebuildSelector() {
        if (!this.inEventLoop()) {
            this.execute(new Runnable() {
                @Override
                public void run() {
                    NioEventLoop.this.rebuildSelector();
                }
            });
            return;
        }
        final Selector oldSelector = this.selector;
        if (oldSelector == null) {
            return;
        }
        Selector newSelector;
        try {
            newSelector = this.openSelector();
        }
        catch (Exception e) {
            NioEventLoop.logger.warn("Failed to create a new Selector.", e);
            return;
        }
        int nChannels = 0;
        while (true) {
            try {
                for (final SelectionKey key : oldSelector.keys()) {
                    final Object a = key.attachment();
                    try {
                        if (!key.isValid() || key.channel().keyFor(newSelector) != null) {
                            continue;
                        }
                        final int interestOps = key.interestOps();
                        key.cancel();
                        final SelectionKey newKey = key.channel().register(newSelector, interestOps, a);
                        if (a instanceof AbstractNioChannel) {
                            ((AbstractNioChannel)a).selectionKey = newKey;
                        }
                        ++nChannels;
                    }
                    catch (Exception e2) {
                        NioEventLoop.logger.warn("Failed to re-register a Channel to the new Selector.", e2);
                        if (a instanceof AbstractNioChannel) {
                            final AbstractNioChannel ch = (AbstractNioChannel)a;
                            ch.unsafe().close(ch.unsafe().voidPromise());
                        }
                        else {
                            final NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
                            invokeChannelUnregistered(task, key, e2);
                        }
                    }
                }
            }
            catch (ConcurrentModificationException e3) {
                continue;
            }
            break;
        }
        this.selector = newSelector;
        try {
            oldSelector.close();
        }
        catch (Throwable t) {
            if (NioEventLoop.logger.isWarnEnabled()) {
                NioEventLoop.logger.warn("Failed to close the old Selector.", t);
            }
        }
        NioEventLoop.logger.info("Migrated " + nChannels + " channel(s) to the new Selector.");
    }
    
    @Override
    protected void run() {
        while (true) {
            final boolean oldWakenUp = this.wakenUp.getAndSet(false);
            try {
                if (this.hasTasks()) {
                    this.selectNow();
                }
                else {
                    this.select(oldWakenUp);
                    if (this.wakenUp.get()) {
                        this.selector.wakeup();
                    }
                }
                this.cancelledKeys = 0;
                this.needsToSelectAgain = false;
                final int ioRatio = this.ioRatio;
                if (ioRatio == 100) {
                    this.processSelectedKeys();
                    this.runAllTasks();
                }
                else {
                    final long ioStartTime = System.nanoTime();
                    this.processSelectedKeys();
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
                NioEventLoop.logger.warn("Unexpected exception in the selector loop.", t);
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException ex) {}
            }
        }
    }
    
    private void processSelectedKeys() {
        if (this.selectedKeys != null) {
            this.processSelectedKeysOptimized(this.selectedKeys.flip());
        }
        else {
            this.processSelectedKeysPlain(this.selector.selectedKeys());
        }
    }
    
    @Override
    protected void cleanup() {
        try {
            this.selector.close();
        }
        catch (IOException e) {
            NioEventLoop.logger.warn("Failed to close a selector.", e);
        }
    }
    
    void cancel(final SelectionKey key) {
        key.cancel();
        ++this.cancelledKeys;
        if (this.cancelledKeys >= 256) {
            this.cancelledKeys = 0;
            this.needsToSelectAgain = true;
        }
    }
    
    @Override
    protected Runnable pollTask() {
        final Runnable task = super.pollTask();
        if (this.needsToSelectAgain) {
            this.selectAgain();
        }
        return task;
    }
    
    private void processSelectedKeysPlain(Set<SelectionKey> selectedKeys) {
        if (selectedKeys.isEmpty()) {
            return;
        }
        Iterator<SelectionKey> i = selectedKeys.iterator();
        while (true) {
            final SelectionKey k = i.next();
            final Object a = k.attachment();
            i.remove();
            if (a instanceof AbstractNioChannel) {
                processSelectedKey(k, (AbstractNioChannel)a);
            }
            else {
                final NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
                processSelectedKey(k, task);
            }
            if (!i.hasNext()) {
                break;
            }
            if (!this.needsToSelectAgain) {
                continue;
            }
            this.selectAgain();
            selectedKeys = this.selector.selectedKeys();
            if (selectedKeys.isEmpty()) {
                break;
            }
            i = selectedKeys.iterator();
        }
    }
    
    private void processSelectedKeysOptimized(SelectionKey[] selectedKeys) {
        int i = 0;
        while (true) {
            final SelectionKey k = selectedKeys[i];
            if (k == null) {
                break;
            }
            selectedKeys[i] = null;
            final Object a = k.attachment();
            if (a instanceof AbstractNioChannel) {
                processSelectedKey(k, (AbstractNioChannel)a);
            }
            else {
                final NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
                processSelectedKey(k, task);
            }
            if (this.needsToSelectAgain) {
                while (selectedKeys[i] != null) {
                    selectedKeys[i] = null;
                    ++i;
                }
                this.selectAgain();
                selectedKeys = this.selectedKeys.flip();
                i = -1;
            }
            ++i;
        }
    }
    
    private static void processSelectedKey(final SelectionKey k, final AbstractNioChannel ch) {
        final AbstractNioChannel.NioUnsafe unsafe = ch.unsafe();
        if (!k.isValid()) {
            unsafe.close(unsafe.voidPromise());
            return;
        }
        try {
            final int readyOps = k.readyOps();
            if ((readyOps & 0x11) != 0x0 || readyOps == 0) {
                unsafe.read();
                if (!ch.isOpen()) {
                    return;
                }
            }
            if ((readyOps & 0x4) != 0x0) {
                ch.unsafe().forceFlush();
            }
            if ((readyOps & 0x8) != 0x0) {
                int ops = k.interestOps();
                ops &= 0xFFFFFFF7;
                k.interestOps(ops);
                unsafe.finishConnect();
            }
        }
        catch (CancelledKeyException ignored) {
            unsafe.close(unsafe.voidPromise());
        }
    }
    
    private static void processSelectedKey(final SelectionKey k, final NioTask<SelectableChannel> task) {
        int state = 0;
        try {
            task.channelReady(k.channel(), k);
            state = 1;
        }
        catch (Exception e) {
            k.cancel();
            invokeChannelUnregistered(task, k, e);
            state = 2;
        }
        finally {
            switch (state) {
                case 0: {
                    k.cancel();
                    invokeChannelUnregistered(task, k, null);
                    break;
                }
                case 1: {
                    if (!k.isValid()) {
                        invokeChannelUnregistered(task, k, null);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private void closeAll() {
        this.selectAgain();
        final Set<SelectionKey> keys = this.selector.keys();
        final Collection<AbstractNioChannel> channels = new ArrayList<AbstractNioChannel>(keys.size());
        for (final SelectionKey k : keys) {
            final Object a = k.attachment();
            if (a instanceof AbstractNioChannel) {
                channels.add((AbstractNioChannel)a);
            }
            else {
                k.cancel();
                final NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
                invokeChannelUnregistered(task, k, null);
            }
        }
        for (final AbstractNioChannel ch : channels) {
            ch.unsafe().close(ch.unsafe().voidPromise());
        }
    }
    
    private static void invokeChannelUnregistered(final NioTask<SelectableChannel> task, final SelectionKey k, final Throwable cause) {
        try {
            task.channelUnregistered(k.channel(), cause);
        }
        catch (Exception e) {
            NioEventLoop.logger.warn("Unexpected exception while running NioTask.channelUnregistered()", e);
        }
    }
    
    @Override
    protected void wakeup(final boolean inEventLoop) {
        if (!inEventLoop && this.wakenUp.compareAndSet(false, true)) {
            this.selector.wakeup();
        }
    }
    
    void selectNow() throws IOException {
        try {
            this.selector.selectNow();
        }
        finally {
            if (this.wakenUp.get()) {
                this.selector.wakeup();
            }
        }
    }
    
    private void select(final boolean oldWakenUp) throws IOException {
        Selector selector = this.selector;
        try {
            int selectCnt = 0;
            long currentTimeNanos = System.nanoTime();
            final long selectDeadLineNanos = currentTimeNanos + this.delayNanos(currentTimeNanos);
            while (true) {
                final long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
                if (timeoutMillis <= 0L) {
                    if (selectCnt == 0) {
                        selector.selectNow();
                        selectCnt = 1;
                        break;
                    }
                    break;
                }
                else {
                    final int selectedKeys = selector.select(timeoutMillis);
                    ++selectCnt;
                    if (selectedKeys != 0 || oldWakenUp || this.wakenUp.get() || this.hasTasks()) {
                        break;
                    }
                    if (this.hasScheduledTasks()) {
                        break;
                    }
                    if (Thread.interrupted()) {
                        if (NioEventLoop.logger.isDebugEnabled()) {
                            NioEventLoop.logger.debug("Selector.select() returned prematurely because Thread.currentThread().interrupt() was called. Use NioEventLoop.shutdownGracefully() to shutdown the NioEventLoop.");
                        }
                        selectCnt = 1;
                        break;
                    }
                    final long time = System.nanoTime();
                    if (time - TimeUnit.MILLISECONDS.toNanos(timeoutMillis) >= currentTimeNanos) {
                        selectCnt = 1;
                    }
                    else if (NioEventLoop.SELECTOR_AUTO_REBUILD_THRESHOLD > 0 && selectCnt >= NioEventLoop.SELECTOR_AUTO_REBUILD_THRESHOLD) {
                        NioEventLoop.logger.warn("Selector.select() returned prematurely {} times in a row; rebuilding selector.", (Object)selectCnt);
                        this.rebuildSelector();
                        selector = this.selector;
                        selector.selectNow();
                        selectCnt = 1;
                        break;
                    }
                    currentTimeNanos = time;
                }
            }
            if (selectCnt > 3 && NioEventLoop.logger.isDebugEnabled()) {
                NioEventLoop.logger.debug("Selector.select() returned prematurely {} times in a row.", (Object)(selectCnt - 1));
            }
        }
        catch (CancelledKeyException e) {
            if (NioEventLoop.logger.isDebugEnabled()) {
                NioEventLoop.logger.debug(CancelledKeyException.class.getSimpleName() + " raised by a Selector - JDK bug?", e);
            }
        }
    }
    
    private void selectAgain() {
        this.needsToSelectAgain = false;
        try {
            this.selector.selectNow();
        }
        catch (Throwable t) {
            NioEventLoop.logger.warn("Failed to update SelectionKeys.", t);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(NioEventLoop.class);
        DISABLE_KEYSET_OPTIMIZATION = SystemPropertyUtil.getBoolean("io.netty.noKeySetOptimization", false);
        final String key = "sun.nio.ch.bugLevel";
        try {
            final String buglevel = SystemPropertyUtil.get(key);
            if (buglevel == null) {
                System.setProperty(key, "");
            }
        }
        catch (SecurityException e) {
            if (NioEventLoop.logger.isDebugEnabled()) {
                NioEventLoop.logger.debug("Unable to get/set System Property: {}", key, e);
            }
        }
        int selectorAutoRebuildThreshold = SystemPropertyUtil.getInt("io.netty.selectorAutoRebuildThreshold", 512);
        if (selectorAutoRebuildThreshold < 3) {
            selectorAutoRebuildThreshold = 0;
        }
        SELECTOR_AUTO_REBUILD_THRESHOLD = selectorAutoRebuildThreshold;
        if (NioEventLoop.logger.isDebugEnabled()) {
            NioEventLoop.logger.debug("-Dio.netty.noKeySetOptimization: {}", (Object)NioEventLoop.DISABLE_KEYSET_OPTIMIZATION);
            NioEventLoop.logger.debug("-Dio.netty.selectorAutoRebuildThreshold: {}", (Object)NioEventLoop.SELECTOR_AUTO_REBUILD_THRESHOLD);
        }
    }
}
