// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.sctp;

import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import java.util.Set;
import java.net.InetSocketAddress;
import io.netty.channel.ServerChannel;

public interface SctpServerChannel extends ServerChannel
{
    SctpServerChannelConfig config();
    
    InetSocketAddress localAddress();
    
    Set<InetSocketAddress> allLocalAddresses();
    
    ChannelFuture bindAddress(final InetAddress p0);
    
    ChannelFuture bindAddress(final InetAddress p0, final ChannelPromise p1);
    
    ChannelFuture unbindAddress(final InetAddress p0);
    
    ChannelFuture unbindAddress(final InetAddress p0, final ChannelPromise p1);
}
