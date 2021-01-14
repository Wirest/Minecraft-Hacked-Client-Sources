package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

public abstract interface EventLoop
        extends EventExecutor, EventLoopGroup {
    public abstract EventLoopGroup parent();
}




