// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.sctp;

import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import java.util.Set;
import java.net.InetSocketAddress;
import com.sun.nio.sctp.Association;
import io.netty.channel.Channel;

public interface SctpChannel extends Channel
{
    SctpServerChannel parent();
    
    Association association();
    
    InetSocketAddress localAddress();
    
    Set<InetSocketAddress> allLocalAddresses();
    
    SctpChannelConfig config();
    
    InetSocketAddress remoteAddress();
    
    Set<InetSocketAddress> allRemoteAddresses();
    
    ChannelFuture bindAddress(final InetAddress p0);
    
    ChannelFuture bindAddress(final InetAddress p0, final ChannelPromise p1);
    
    ChannelFuture unbindAddress(final InetAddress p0);
    
    ChannelFuture unbindAddress(final InetAddress p0, final ChannelPromise p1);
}
