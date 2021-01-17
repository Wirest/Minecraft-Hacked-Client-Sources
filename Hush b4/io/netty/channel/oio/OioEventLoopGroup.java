// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.oio;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import io.netty.channel.ThreadPerChannelEventLoopGroup;

public class OioEventLoopGroup extends ThreadPerChannelEventLoopGroup
{
    public OioEventLoopGroup() {
        this(0);
    }
    
    public OioEventLoopGroup(final int maxChannels) {
        this(maxChannels, Executors.defaultThreadFactory());
    }
    
    public OioEventLoopGroup(final int maxChannels, final ThreadFactory threadFactory) {
        super(maxChannels, threadFactory, new Object[0]);
    }
}
