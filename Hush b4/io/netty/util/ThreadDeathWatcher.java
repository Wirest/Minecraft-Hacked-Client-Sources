// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util;

import io.netty.util.internal.MpscLinkedQueueNode;
import java.util.ArrayList;
import java.util.List;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Queue;
import java.util.concurrent.ThreadFactory;
import io.netty.util.internal.logging.InternalLogger;

public final class ThreadDeathWatcher
{
    private static final InternalLogger logger;
    private static final ThreadFactory threadFactory;
    private static final Queue<Entry> pendingEntries;
    private static final Watcher watcher;
    private static final AtomicBoolean started;
    private static volatile Thread watcherThread;
    
    public static void watch(final Thread thread, final Runnable task) {
        if (thread == null) {
            throw new NullPointerException("thread");
        }
        if (task == null) {
            throw new NullPointerException("task");
        }
        if (!thread.isAlive()) {
            throw new IllegalArgumentException("thread must be alive.");
        }
        schedule(thread, task, true);
    }
    
    public static void unwatch(final Thread thread, final Runnable task) {
        if (thread == null) {
            throw new NullPointerException("thread");
        }
        if (task == null) {
            throw new NullPointerException("task");
        }
        schedule(thread, task, false);
    }
    
    private static void schedule(final Thread thread, final Runnable task, final boolean isWatch) {
        ThreadDeathWatcher.pendingEntries.add(new Entry(thread, task, isWatch));
        if (ThreadDeathWatcher.started.compareAndSet(false, true)) {
            final Thread watcherThread = ThreadDeathWatcher.threadFactory.newThread(ThreadDeathWatcher.watcher);
            watcherThread.start();
            ThreadDeathWatcher.watcherThread = watcherThread;
        }
    }
    
    public static boolean awaitInactivity(final long timeout, final TimeUnit unit) throws InterruptedException {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        final Thread watcherThread = ThreadDeathWatcher.watcherThread;
        if (watcherThread != null) {
            watcherThread.join(unit.toMillis(timeout));
            return !watcherThread.isAlive();
        }
        return true;
    }
    
    private ThreadDeathWatcher() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ThreadDeathWatcher.class);
        threadFactory = new DefaultThreadFactory(ThreadDeathWatcher.class, true, 1);
        pendingEntries = PlatformDependent.newMpscQueue();
        watcher = new Watcher();
        started = new AtomicBoolean();
    }
    
    private static final class Watcher implements Runnable
    {
        private final List<Entry> watchees;
        
        private Watcher() {
            this.watchees = new ArrayList<Entry>();
        }
        
        @Override
        public void run() {
            while (true) {
                this.fetchWatchees();
                this.notifyWatchees();
                this.fetchWatchees();
                this.notifyWatchees();
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException ex) {}
                if (this.watchees.isEmpty() && ThreadDeathWatcher.pendingEntries.isEmpty()) {
                    final boolean stopped = ThreadDeathWatcher.started.compareAndSet(true, false);
                    assert stopped;
                    if (ThreadDeathWatcher.pendingEntries.isEmpty()) {
                        break;
                    }
                    if (!ThreadDeathWatcher.started.compareAndSet(false, true)) {
                        break;
                    }
                    continue;
                }
            }
        }
        
        private void fetchWatchees() {
            while (true) {
                final Entry e = ThreadDeathWatcher.pendingEntries.poll();
                if (e == null) {
                    break;
                }
                if (e.isWatch) {
                    this.watchees.add(e);
                }
                else {
                    this.watchees.remove(e);
                }
            }
        }
        
        private void notifyWatchees() {
            final List<Entry> watchees = this.watchees;
            int i = 0;
            while (i < watchees.size()) {
                final Entry e = watchees.get(i);
                if (!e.thread.isAlive()) {
                    watchees.remove(i);
                    try {
                        e.task.run();
                    }
                    catch (Throwable t) {
                        ThreadDeathWatcher.logger.warn("Thread death watcher task raised an exception:", t);
                    }
                }
                else {
                    ++i;
                }
            }
        }
    }
    
    private static final class Entry extends MpscLinkedQueueNode<Entry>
    {
        final Thread thread;
        final Runnable task;
        final boolean isWatch;
        
        Entry(final Thread thread, final Runnable task, final boolean isWatch) {
            this.thread = thread;
            this.task = task;
            this.isWatch = isWatch;
        }
        
        @Override
        public Entry value() {
            return this;
        }
        
        @Override
        public int hashCode() {
            return this.thread.hashCode() ^ this.task.hashCode();
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Entry)) {
                return false;
            }
            final Entry that = (Entry)obj;
            return this.thread == that.thread && this.task == that.task;
        }
    }
}
