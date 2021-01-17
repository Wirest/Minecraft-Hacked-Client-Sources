// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import java.util.concurrent.ThreadFactory;
import io.netty.util.concurrent.SingleThreadEventExecutor;

public abstract class SingleThreadEventLoop extends SingleThreadEventExecutor implements EventLoop
{
    protected SingleThreadEventLoop(final EventLoopGroup parent, final ThreadFactory threadFactory, final boolean addTaskWakesUp) {
        super(parent, threadFactory, addTaskWakesUp);
    }
    
    @Override
    public EventLoopGroup parent() {
        return (EventLoopGroup)super.parent();
    }
    
    @Override
    public EventLoop next() {
        return (EventLoop)super.next();
    }
    
    @Override
    public ChannelFuture register(final Channel channel) {
        return this.register(channel, new DefaultChannelPromise(channel, this));
    }
    
    @Override
    public ChannelFuture register(final Channel channel, final ChannelPromise promise) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        if (promise == null) {
            throw new NullPointerException("promise");
        }
        channel.unsafe().register(this, promise);
        return promise;
    }
    
    @Override
    protected boolean wakesUpForTask(final Runnable task) {
        return !(task instanceof NonWakeupRunnable);
    }
    
    interface NonWakeupRunnable extends Runnable
    {
    }
}
