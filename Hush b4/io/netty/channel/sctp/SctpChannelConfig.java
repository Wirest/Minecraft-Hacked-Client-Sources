// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.sctp;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import com.sun.nio.sctp.SctpStandardSocketOptions;
import io.netty.channel.ChannelConfig;

public interface SctpChannelConfig extends ChannelConfig
{
    boolean isSctpNoDelay();
    
    SctpChannelConfig setSctpNoDelay(final boolean p0);
    
    int getSendBufferSize();
    
    SctpChannelConfig setSendBufferSize(final int p0);
    
    int getReceiveBufferSize();
    
    SctpChannelConfig setReceiveBufferSize(final int p0);
    
    SctpStandardSocketOptions.InitMaxStreams getInitMaxStreams();
    
    SctpChannelConfig setInitMaxStreams(final SctpStandardSocketOptions.InitMaxStreams p0);
    
    SctpChannelConfig setConnectTimeoutMillis(final int p0);
    
    SctpChannelConfig setMaxMessagesPerRead(final int p0);
    
    SctpChannelConfig setWriteSpinCount(final int p0);
    
    SctpChannelConfig setAllocator(final ByteBufAllocator p0);
    
    SctpChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator p0);
    
    SctpChannelConfig setAutoRead(final boolean p0);
    
    SctpChannelConfig setAutoClose(final boolean p0);
    
    SctpChannelConfig setWriteBufferHighWaterMark(final int p0);
    
    SctpChannelConfig setWriteBufferLowWaterMark(final int p0);
    
    SctpChannelConfig setMessageSizeEstimator(final MessageSizeEstimator p0);
}
