// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;

public interface EventLoopGroup extends EventExecutorGroup
{
    EventLoop next();
    
    ChannelFuture register(final Channel p0);
    
    ChannelFuture register(final Channel p0, final ChannelPromise p1);
}
