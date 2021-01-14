package io.netty.channel.nio;

import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;

import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.ThreadFactory;

public class NioEventLoopGroup
        extends MultithreadEventLoopGroup {
    public NioEventLoopGroup() {
        this(0);
    }

    public NioEventLoopGroup(int paramInt) {
        this(paramInt, null);
    }

    public NioEventLoopGroup(int paramInt, ThreadFactory paramThreadFactory) {
        this(paramInt, paramThreadFactory, SelectorProvider.provider());
    }

    public NioEventLoopGroup(int paramInt, ThreadFactory paramThreadFactory, SelectorProvider paramSelectorProvider) {
        super(paramInt, paramThreadFactory, new Object[]{paramSelectorProvider});
    }

    public void setIoRatio(int paramInt) {
        Iterator localIterator = children().iterator();
        while (localIterator.hasNext()) {
            EventExecutor localEventExecutor = (EventExecutor) localIterator.next();
            ((NioEventLoop) localEventExecutor).setIoRatio(paramInt);
        }
    }

    public void rebuildSelectors() {
        Iterator localIterator = children().iterator();
        while (localIterator.hasNext()) {
            EventExecutor localEventExecutor = (EventExecutor) localIterator.next();
            ((NioEventLoop) localEventExecutor).rebuildSelector();
        }
    }

    protected EventExecutor newChild(ThreadFactory paramThreadFactory, Object... paramVarArgs)
            throws Exception {
        return new NioEventLoop(this, paramThreadFactory, (SelectorProvider) paramVarArgs[0]);
    }
}




