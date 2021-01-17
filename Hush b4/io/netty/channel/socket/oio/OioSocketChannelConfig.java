// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.socket.oio;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.SocketChannelConfig;

public interface OioSocketChannelConfig extends SocketChannelConfig
{
    OioSocketChannelConfig setSoTimeout(final int p0);
    
    int getSoTimeout();
    
    OioSocketChannelConfig setTcpNoDelay(final boolean p0);
    
    OioSocketChannelConfig setSoLinger(final int p0);
    
    OioSocketChannelConfig setSendBufferSize(final int p0);
    
    OioSocketChannelConfig setReceiveBufferSize(final int p0);
    
    OioSocketChannelConfig setKeepAlive(final boolean p0);
    
    OioSocketChannelConfig setTrafficClass(final int p0);
    
    OioSocketChannelConfig setReuseAddress(final boolean p0);
    
    OioSocketChannelConfig setPerformancePreferences(final int p0, final int p1, final int p2);
    
    OioSocketChannelConfig setAllowHalfClosure(final boolean p0);
    
    OioSocketChannelConfig setConnectTimeoutMillis(final int p0);
    
    OioSocketChannelConfig setMaxMessagesPerRead(final int p0);
    
    OioSocketChannelConfig setWriteSpinCount(final int p0);
    
    OioSocketChannelConfig setAllocator(final ByteBufAllocator p0);
    
    OioSocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator p0);
    
    OioSocketChannelConfig setAutoRead(final boolean p0);
    
    OioSocketChannelConfig setAutoClose(final boolean p0);
    
    OioSocketChannelConfig setWriteBufferHighWaterMark(final int p0);
    
    OioSocketChannelConfig setWriteBufferLowWaterMark(final int p0);
    
    OioSocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator p0);
}
