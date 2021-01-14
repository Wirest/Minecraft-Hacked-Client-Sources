package io.netty.channel;

import io.netty.util.concurrent.SingleThreadEventExecutor;

import java.util.concurrent.ThreadFactory;

public abstract class SingleThreadEventLoop
        extends SingleThreadEventExecutor
        implements EventLoop {
    protected SingleThreadEventLoop(EventLoopGroup paramEventLoopGroup, ThreadFactory paramThreadFactory, boolean paramBoolean) {
        super(paramEventLoopGroup, paramThreadFactory, paramBoolean);
    }

    public EventLoopGroup parent() {
        return (EventLoopGroup) super.parent();
    }

    public EventLoop next() {
        return (EventLoop) super.next();
    }

    public ChannelFuture register(Channel paramChannel) {
        return register(paramChannel, new DefaultChannelPromise(paramChannel, this));
    }

    public ChannelFuture register(Channel paramChannel, ChannelPromise paramChannelPromise) {
        if (paramChannel == null) {
            throw new NullPointerException("channel");
        }
        if (paramChannelPromise == null) {
            throw new NullPointerException("promise");
        }
        paramChannel.unsafe().register(this, paramChannelPromise);
        return paramChannelPromise;
    }

    protected boolean wakesUpForTask(Runnable paramRunnable) {
        return !(paramRunnable instanceof NonWakeupRunnable);
    }

    static abstract interface NonWakeupRunnable
            extends Runnable {
    }
}




