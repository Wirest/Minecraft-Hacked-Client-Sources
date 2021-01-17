// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.embedded;

import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.ArrayDeque;
import java.util.Queue;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.AbstractEventExecutor;

final class EmbeddedEventLoop extends AbstractEventExecutor implements EventLoop
{
    private final Queue<Runnable> tasks;
    
    EmbeddedEventLoop() {
        this.tasks = new ArrayDeque<Runnable>(2);
    }
    
    @Override
    public void execute(final Runnable command) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        this.tasks.add(command);
    }
    
    void runTasks() {
        while (true) {
            final Runnable task = this.tasks.poll();
            if (task == null) {
                break;
            }
            task.run();
        }
    }
    
    @Override
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Future<?> terminationFuture() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public void shutdown() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isShuttingDown() {
        return false;
    }
    
    @Override
    public boolean isShutdown() {
        return false;
    }
    
    @Override
    public boolean isTerminated() {
        return false;
    }
    
    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        Thread.sleep(unit.toMillis(timeout));
        return false;
    }
    
    @Override
    public ChannelFuture register(final Channel channel) {
        return this.register(channel, new DefaultChannelPromise(channel, this));
    }
    
    @Override
    public ChannelFuture register(final Channel channel, final ChannelPromise promise) {
        channel.unsafe().register(this, promise);
        return promise;
    }
    
    @Override
    public boolean inEventLoop() {
        return true;
    }
    
    @Override
    public boolean inEventLoop(final Thread thread) {
        return true;
    }
    
    @Override
    public EventLoop next() {
        return this;
    }
    
    @Override
    public EventLoopGroup parent() {
        return this;
    }
}
