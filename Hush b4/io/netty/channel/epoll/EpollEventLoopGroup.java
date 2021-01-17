// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.epoll;

import io.netty.channel.EventLoopGroup;
import java.util.Iterator;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.ThreadFactory;
import io.netty.channel.MultithreadEventLoopGroup;

public final class EpollEventLoopGroup extends MultithreadEventLoopGroup
{
    public EpollEventLoopGroup() {
        this(0);
    }
    
    public EpollEventLoopGroup(final int nThreads) {
        this(nThreads, null);
    }
    
    public EpollEventLoopGroup(final int nThreads, final ThreadFactory threadFactory) {
        this(nThreads, threadFactory, 128);
    }
    
    public EpollEventLoopGroup(final int nThreads, final ThreadFactory threadFactory, final int maxEventsAtOnce) {
        super(nThreads, threadFactory, new Object[] { maxEventsAtOnce });
    }
    
    public void setIoRatio(final int ioRatio) {
        for (final EventExecutor e : this.children()) {
            ((EpollEventLoop)e).setIoRatio(ioRatio);
        }
    }
    
    @Override
    protected EventExecutor newChild(final ThreadFactory threadFactory, final Object... args) throws Exception {
        return new EpollEventLoop(this, threadFactory, (int)args[0]);
    }
}
