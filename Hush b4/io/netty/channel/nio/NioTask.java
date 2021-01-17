// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.SelectableChannel;

public interface NioTask<C extends SelectableChannel>
{
    void channelReady(final C p0, final SelectionKey p1) throws Exception;
    
    void channelUnregistered(final C p0, final Throwable p1) throws Exception;
}
