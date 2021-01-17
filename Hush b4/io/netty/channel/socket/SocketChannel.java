// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.socket;

import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.net.InetSocketAddress;
import io.netty.channel.Channel;

public interface SocketChannel extends Channel
{
    ServerSocketChannel parent();
    
    SocketChannelConfig config();
    
    InetSocketAddress localAddress();
    
    InetSocketAddress remoteAddress();
    
    boolean isInputShutdown();
    
    boolean isOutputShutdown();
    
    ChannelFuture shutdownOutput();
    
    ChannelFuture shutdownOutput(final ChannelPromise p0);
}
