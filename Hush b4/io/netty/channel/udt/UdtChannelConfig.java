// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.udt;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;

public interface UdtChannelConfig extends ChannelConfig
{
    int getProtocolReceiveBufferSize();
    
    int getProtocolSendBufferSize();
    
    int getReceiveBufferSize();
    
    int getSendBufferSize();
    
    int getSoLinger();
    
    int getSystemReceiveBufferSize();
    
    int getSystemSendBufferSize();
    
    boolean isReuseAddress();
    
    UdtChannelConfig setConnectTimeoutMillis(final int p0);
    
    UdtChannelConfig setMaxMessagesPerRead(final int p0);
    
    UdtChannelConfig setWriteSpinCount(final int p0);
    
    UdtChannelConfig setAllocator(final ByteBufAllocator p0);
    
    UdtChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator p0);
    
    UdtChannelConfig setAutoRead(final boolean p0);
    
    UdtChannelConfig setAutoClose(final boolean p0);
    
    UdtChannelConfig setWriteBufferHighWaterMark(final int p0);
    
    UdtChannelConfig setWriteBufferLowWaterMark(final int p0);
    
    UdtChannelConfig setMessageSizeEstimator(final MessageSizeEstimator p0);
    
    UdtChannelConfig setProtocolReceiveBufferSize(final int p0);
    
    UdtChannelConfig setProtocolSendBufferSize(final int p0);
    
    UdtChannelConfig setReceiveBufferSize(final int p0);
    
    UdtChannelConfig setReuseAddress(final boolean p0);
    
    UdtChannelConfig setSendBufferSize(final int p0);
    
    UdtChannelConfig setSoLinger(final int p0);
    
    UdtChannelConfig setSystemReceiveBufferSize(final int p0);
    
    UdtChannelConfig setSystemSendBufferSize(final int p0);
}
