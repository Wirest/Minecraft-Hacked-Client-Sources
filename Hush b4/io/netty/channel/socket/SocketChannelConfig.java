// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.socket;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;

public interface SocketChannelConfig extends ChannelConfig
{
    boolean isTcpNoDelay();
    
    SocketChannelConfig setTcpNoDelay(final boolean p0);
    
    int getSoLinger();
    
    SocketChannelConfig setSoLinger(final int p0);
    
    int getSendBufferSize();
    
    SocketChannelConfig setSendBufferSize(final int p0);
    
    int getReceiveBufferSize();
    
    SocketChannelConfig setReceiveBufferSize(final int p0);
    
    boolean isKeepAlive();
    
    SocketChannelConfig setKeepAlive(final boolean p0);
    
    int getTrafficClass();
    
    SocketChannelConfig setTrafficClass(final int p0);
    
    boolean isReuseAddress();
    
    SocketChannelConfig setReuseAddress(final boolean p0);
    
    SocketChannelConfig setPerformancePreferences(final int p0, final int p1, final int p2);
    
    boolean isAllowHalfClosure();
    
    SocketChannelConfig setAllowHalfClosure(final boolean p0);
    
    SocketChannelConfig setConnectTimeoutMillis(final int p0);
    
    SocketChannelConfig setMaxMessagesPerRead(final int p0);
    
    SocketChannelConfig setWriteSpinCount(final int p0);
    
    SocketChannelConfig setAllocator(final ByteBufAllocator p0);
    
    SocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator p0);
    
    SocketChannelConfig setAutoRead(final boolean p0);
    
    SocketChannelConfig setAutoClose(final boolean p0);
    
    SocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator p0);
}
