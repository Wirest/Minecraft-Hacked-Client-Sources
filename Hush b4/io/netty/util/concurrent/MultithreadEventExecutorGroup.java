// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

import java.util.Collection;
import java.util.Map;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultithreadEventExecutorGroup extends AbstractEventExecutorGroup
{
    private final EventExecutor[] children;
    private final AtomicInteger childIndex;
    private final AtomicInteger terminatedChildren;
    private final Promise<?> terminationFuture;
    private final EventExecutorChooser chooser;
    
    protected MultithreadEventExecutorGroup(final int nThreads, ThreadFactory threadFactory, final Object... args) {
        this.childIndex = new AtomicInteger();
        this.terminatedChildren = new AtomicInteger();
        this.terminationFuture = new DefaultPromise<Object>(GlobalEventExecutor.INSTANCE);
        if (nThreads <= 0) {
            throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", nThreads));
        }
        if (threadFactory == null) {
            threadFactory = this.newDefaultThreadFactory();
        }
        this.children = new SingleThreadEventExecutor[nThreads];
        if (isPowerOfTwo(this.children.length)) {
            this.chooser = new PowerOfTwoEventExecutorChooser();
        }
        else {
            this.chooser = new GenericEventExecutorChooser();
        }
        for (int i = 0; i < nThreads; ++i) {
            boolean success = false;
            try {
                this.children[i] = this.newChild(threadFactory, args);
                success = true;
            }
            catch (Exception e) {
                throw new IllegalStateException("failed to create a child event loop", e);
            }
            finally {
                if (!success) {
                    for (int j = 0; j < i; ++j) {
                        this.children[j].shutdownGracefully();
                    }
                    for (int j = 0; j < i; ++j) {
                        final EventExecutor e2 = this.children[j];
                        try {
                            while (!e2.isTerminated()) {
                                e2.awaitTermination(2147483647L, TimeUnit.SECONDS);
                            }
                        }
                        catch (InterruptedException interrupted) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }
            }
        }
        final FutureListener<Object> terminationListener = new FutureListener<Object>() {
            @Override
            public void operationComplete(final Future<Object> future) throws Exception {
                if (MultithreadEventExecutorGroup.this.terminatedChildren.incrementAndGet() == MultithreadEventExecutorGroup.this.children.length) {
                    MultithreadEventExecutorGroup.this.terminationFuture.setSuccess(null);
                }
            }
        };
        for (final EventExecutor e3 : this.children) {
            e3.terminationFuture().addListener(terminationListener);
        }
    }
    
    protected ThreadFactory newDefaultThreadFactory() {
        return new DefaultThreadFactory(this.getClass());
    }
    
    @Override
    public EventExecutor next() {
        return this.chooser.next();
    }
    
    @Override
    public Iterator<EventExecutor> iterator() {
        return this.children().iterator();
    }
    
    public final int executorCount() {
        return this.children.length;
    }
    
    protected Set<EventExecutor> children() {
        final Set<EventExecutor> children = Collections.newSetFromMap(new LinkedHashMap<EventExecutor, Boolean>());
        Collections.addAll(children, this.children);
        return children;
    }
    
    protected abstract EventExecutor newChild(final ThreadFactory p0, final Object... p1) throws Exception;
    
    @Override
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        for (final EventExecutor l : this.children) {
            l.shutdownGracefully(quietPeriod, timeout, unit);
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
        for (final EventExecutor l : this.children) {
            l.shutdown();
        }
    }
    
    @Override
    public boolean isShuttingDown() {
        for (final EventExecutor l : this.children) {
            if (!l.isShuttingDown()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isShutdown() {
        for (final EventExecutor l : this.children) {
            if (!l.isShutdown()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isTerminated() {
        for (final EventExecutor l : this.children) {
            if (!l.isTerminated()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        final long deadline = System.nanoTime() + unit.toNanos(timeout);
    Label_0084:
        for (final EventExecutor l : this.children) {
            long timeLeft;
            do {
                timeLeft = deadline - System.nanoTime();
                if (timeLeft <= 0L) {
                    break Label_0084;
                }
            } while (!l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS));
        }
        return this.isTerminated();
    }
    
    private static boolean isPowerOfTwo(final int val) {
        return (val & -val) == val;
    }
    
    private final class PowerOfTwoEventExecutorChooser implements EventExecutorChooser
    {
        @Override
        public EventExecutor next() {
            return MultithreadEventExecutorGroup.this.children[MultithreadEventExecutorGroup.this.childIndex.getAndIncrement() & MultithreadEventExecutorGroup.this.children.length - 1];
        }
    }
    
    private final class GenericEventExecutorChooser implements EventExecutorChooser
    {
        @Override
        public EventExecutor next() {
            return MultithreadEventExecutorGroup.this.children[Math.abs(MultithreadEventExecutorGroup.this.childIndex.getAndIncrement() % MultithreadEventExecutorGroup.this.children.length)];
        }
    }
    
    private interface EventExecutorChooser
    {
        EventExecutor next();
    }
}
