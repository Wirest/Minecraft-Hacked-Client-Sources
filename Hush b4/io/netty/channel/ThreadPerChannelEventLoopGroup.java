// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.GenericFutureListener;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import io.netty.util.internal.ReadOnlyIterator;
import java.util.Iterator;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Map;
import java.util.Collections;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.Executors;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Promise;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import io.netty.util.concurrent.AbstractEventExecutorGroup;

public class ThreadPerChannelEventLoopGroup extends AbstractEventExecutorGroup implements EventLoopGroup
{
    private final Object[] childArgs;
    private final int maxChannels;
    final ThreadFactory threadFactory;
    final Set<ThreadPerChannelEventLoop> activeChildren;
    final Queue<ThreadPerChannelEventLoop> idleChildren;
    private final ChannelException tooManyChannels;
    private volatile boolean shuttingDown;
    private final Promise<?> terminationFuture;
    private final FutureListener<Object> childTerminationListener;
    
    protected ThreadPerChannelEventLoopGroup() {
        this(0);
    }
    
    protected ThreadPerChannelEventLoopGroup(final int maxChannels) {
        this(maxChannels, Executors.defaultThreadFactory(), new Object[0]);
    }
    
    protected ThreadPerChannelEventLoopGroup(final int maxChannels, final ThreadFactory threadFactory, final Object... args) {
        this.activeChildren = Collections.newSetFromMap((Map<ThreadPerChannelEventLoop, Boolean>)PlatformDependent.newConcurrentHashMap());
        this.idleChildren = new ConcurrentLinkedQueue<ThreadPerChannelEventLoop>();
        this.terminationFuture = new DefaultPromise<Object>(GlobalEventExecutor.INSTANCE);
        this.childTerminationListener = new FutureListener<Object>() {
            @Override
            public void operationComplete(final Future<Object> future) throws Exception {
                if (ThreadPerChannelEventLoopGroup.this.isTerminated()) {
                    ThreadPerChannelEventLoopGroup.this.terminationFuture.trySuccess(null);
                }
            }
        };
        if (maxChannels < 0) {
            throw new IllegalArgumentException(String.format("maxChannels: %d (expected: >= 0)", maxChannels));
        }
        if (threadFactory == null) {
            throw new NullPointerException("threadFactory");
        }
        if (args == null) {
            this.childArgs = EmptyArrays.EMPTY_OBJECTS;
        }
        else {
            this.childArgs = args.clone();
        }
        this.maxChannels = maxChannels;
        this.threadFactory = threadFactory;
        (this.tooManyChannels = new ChannelException("too many channels (max: " + maxChannels + ')')).setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    }
    
    protected ThreadPerChannelEventLoop newChild(final Object... args) throws Exception {
        return new ThreadPerChannelEventLoop(this);
    }
    
    @Override
    public Iterator<EventExecutor> iterator() {
        return new ReadOnlyIterator<EventExecutor>(this.activeChildren.iterator());
    }
    
    @Override
    public EventLoop next() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        this.shuttingDown = true;
        for (final EventLoop l : this.activeChildren) {
            l.shutdownGracefully(quietPeriod, timeout, unit);
        }
        for (final EventLoop l : this.idleChildren) {
            l.shutdownGracefully(quietPeriod, timeout, unit);
        }
        if (this.isTerminated()) {
            this.terminationFuture.trySuccess(null);
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
        this.shuttingDown = true;
        for (final EventLoop l : this.activeChildren) {
            l.shutdown();
        }
        for (final EventLoop l : this.idleChildren) {
            l.shutdown();
        }
        if (this.isTerminated()) {
            this.terminationFuture.trySuccess(null);
        }
    }
    
    @Override
    public boolean isShuttingDown() {
        for (final EventLoop l : this.activeChildren) {
            if (!l.isShuttingDown()) {
                return false;
            }
        }
        for (final EventLoop l : this.idleChildren) {
            if (!l.isShuttingDown()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isShutdown() {
        for (final EventLoop l : this.activeChildren) {
            if (!l.isShutdown()) {
                return false;
            }
        }
        for (final EventLoop l : this.idleChildren) {
            if (!l.isShutdown()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isTerminated() {
        for (final EventLoop l : this.activeChildren) {
            if (!l.isTerminated()) {
                return false;
            }
        }
        for (final EventLoop l : this.idleChildren) {
            if (!l.isTerminated()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        final long deadline = System.nanoTime() + unit.toNanos(timeout);
        for (final EventLoop l : this.activeChildren) {
            long timeLeft;
            do {
                timeLeft = deadline - System.nanoTime();
                if (timeLeft <= 0L) {
                    return this.isTerminated();
                }
            } while (!l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS));
        }
        for (final EventLoop l : this.idleChildren) {
            long timeLeft;
            do {
                timeLeft = deadline - System.nanoTime();
                if (timeLeft <= 0L) {
                    return this.isTerminated();
                }
            } while (!l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS));
        }
        return this.isTerminated();
    }
    
    @Override
    public ChannelFuture register(final Channel channel) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        try {
            final EventLoop l = this.nextChild();
            return l.register(channel, new DefaultChannelPromise(channel, l));
        }
        catch (Throwable t) {
            return new FailedChannelFuture(channel, GlobalEventExecutor.INSTANCE, t);
        }
    }
    
    @Override
    public ChannelFuture register(final Channel channel, final ChannelPromise promise) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        try {
            return this.nextChild().register(channel, promise);
        }
        catch (Throwable t) {
            promise.setFailure(t);
            return promise;
        }
    }
    
    private EventLoop nextChild() throws Exception {
        if (this.shuttingDown) {
            throw new RejectedExecutionException("shutting down");
        }
        ThreadPerChannelEventLoop loop = this.idleChildren.poll();
        if (loop == null) {
            if (this.maxChannels > 0 && this.activeChildren.size() >= this.maxChannels) {
                throw this.tooManyChannels;
            }
            loop = this.newChild(this.childArgs);
            loop.terminationFuture().addListener(this.childTerminationListener);
        }
        this.activeChildren.add(loop);
        return loop;
    }
}
