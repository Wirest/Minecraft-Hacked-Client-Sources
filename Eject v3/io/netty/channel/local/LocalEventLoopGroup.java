package io.netty.channel.local;

import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;

import java.util.concurrent.ThreadFactory;

public class LocalEventLoopGroup
        extends MultithreadEventLoopGroup {
    public LocalEventLoopGroup() {
        this(0);
    }

    public LocalEventLoopGroup(int paramInt) {
        this(paramInt, null);
    }

    public LocalEventLoopGroup(int paramInt, ThreadFactory paramThreadFactory) {
        super(paramInt, paramThreadFactory, new Object[0]);
    }

    protected EventExecutor newChild(ThreadFactory paramThreadFactory, Object... paramVarArgs)
            throws Exception {
        return new LocalEventLoop(this, paramThreadFactory);
    }
}




