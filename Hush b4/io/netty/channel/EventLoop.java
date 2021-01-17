// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

public interface EventLoop extends EventExecutor, EventLoopGroup
{
    EventLoopGroup parent();
}
