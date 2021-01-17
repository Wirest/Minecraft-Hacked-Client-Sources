// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.socket;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import java.net.NetworkInterface;
import java.net.InetAddress;
import io.netty.channel.ChannelConfig;

public interface DatagramChannelConfig extends ChannelConfig
{
    int getSendBufferSize();
    
    DatagramChannelConfig setSendBufferSize(final int p0);
    
    int getReceiveBufferSize();
    
    DatagramChannelConfig setReceiveBufferSize(final int p0);
    
    int getTrafficClass();
    
    DatagramChannelConfig setTrafficClass(final int p0);
    
    boolean isReuseAddress();
    
    DatagramChannelConfig setReuseAddress(final boolean p0);
    
    boolean isBroadcast();
    
    DatagramChannelConfig setBroadcast(final boolean p0);
    
    boolean isLoopbackModeDisabled();
    
    DatagramChannelConfig setLoopbackModeDisabled(final boolean p0);
    
    int getTimeToLive();
    
    DatagramChannelConfig setTimeToLive(final int p0);
    
    InetAddress getInterface();
    
    DatagramChannelConfig setInterface(final InetAddress p0);
    
    NetworkInterface getNetworkInterface();
    
    DatagramChannelConfig setNetworkInterface(final NetworkInterface p0);
    
    DatagramChannelConfig setMaxMessagesPerRead(final int p0);
    
    DatagramChannelConfig setWriteSpinCount(final int p0);
    
    DatagramChannelConfig setConnectTimeoutMillis(final int p0);
    
    DatagramChannelConfig setAllocator(final ByteBufAllocator p0);
    
    DatagramChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator p0);
    
    DatagramChannelConfig setAutoRead(final boolean p0);
    
    DatagramChannelConfig setAutoClose(final boolean p0);
    
    DatagramChannelConfig setMessageSizeEstimator(final MessageSizeEstimator p0);
}
