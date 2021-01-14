package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;

public abstract interface EventLoopGroup
        extends EventExecutorGroup {
    public abstract EventLoop next();

    public abstract ChannelFuture register(Channel paramChannel);

    public abstract ChannelFuture register(Channel paramChannel, ChannelPromise paramChannelPromise);
}




