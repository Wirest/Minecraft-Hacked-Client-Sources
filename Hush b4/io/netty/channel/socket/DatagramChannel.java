// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.socket;

import java.net.NetworkInterface;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import io.netty.channel.Channel;

public interface DatagramChannel extends Channel
{
    DatagramChannelConfig config();
    
    InetSocketAddress localAddress();
    
    InetSocketAddress remoteAddress();
    
    boolean isConnected();
    
    ChannelFuture joinGroup(final InetAddress p0);
    
    ChannelFuture joinGroup(final InetAddress p0, final ChannelPromise p1);
    
    ChannelFuture joinGroup(final InetSocketAddress p0, final NetworkInterface p1);
    
    ChannelFuture joinGroup(final InetSocketAddress p0, final NetworkInterface p1, final ChannelPromise p2);
    
    ChannelFuture joinGroup(final InetAddress p0, final NetworkInterface p1, final InetAddress p2);
    
    ChannelFuture joinGroup(final InetAddress p0, final NetworkInterface p1, final InetAddress p2, final ChannelPromise p3);
    
    ChannelFuture leaveGroup(final InetAddress p0);
    
    ChannelFuture leaveGroup(final InetAddress p0, final ChannelPromise p1);
    
    ChannelFuture leaveGroup(final InetSocketAddress p0, final NetworkInterface p1);
    
    ChannelFuture leaveGroup(final InetSocketAddress p0, final NetworkInterface p1, final ChannelPromise p2);
    
    ChannelFuture leaveGroup(final InetAddress p0, final NetworkInterface p1, final InetAddress p2);
    
    ChannelFuture leaveGroup(final InetAddress p0, final NetworkInterface p1, final InetAddress p2, final ChannelPromise p3);
    
    ChannelFuture block(final InetAddress p0, final NetworkInterface p1, final InetAddress p2);
    
    ChannelFuture block(final InetAddress p0, final NetworkInterface p1, final InetAddress p2, final ChannelPromise p3);
    
    ChannelFuture block(final InetAddress p0, final InetAddress p1);
    
    ChannelFuture block(final InetAddress p0, final InetAddress p1, final ChannelPromise p2);
}
