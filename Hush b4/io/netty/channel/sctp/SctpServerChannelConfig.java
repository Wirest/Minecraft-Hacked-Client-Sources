// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.sctp;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import com.sun.nio.sctp.SctpStandardSocketOptions;
import io.netty.channel.ChannelConfig;

public interface SctpServerChannelConfig extends ChannelConfig
{
    int getBacklog();
    
    SctpServerChannelConfig setBacklog(final int p0);
    
    int getSendBufferSize();
    
    SctpServerChannelConfig setSendBufferSize(final int p0);
    
    int getReceiveBufferSize();
    
    SctpServerChannelConfig setReceiveBufferSize(final int p0);
    
    SctpStandardSocketOptions.InitMaxStreams getInitMaxStreams();
    
    SctpServerChannelConfig setInitMaxStreams(final SctpStandardSocketOptions.InitMaxStreams p0);
    
    SctpServerChannelConfig setMaxMessagesPerRead(final int p0);
    
    SctpServerChannelConfig setWriteSpinCount(final int p0);
    
    SctpServerChannelConfig setConnectTimeoutMillis(final int p0);
    
    SctpServerChannelConfig setAllocator(final ByteBufAllocator p0);
    
    SctpServerChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator p0);
    
    SctpServerChannelConfig setAutoRead(final boolean p0);
    
    SctpServerChannelConfig setAutoClose(final boolean p0);
    
    SctpServerChannelConfig setWriteBufferHighWaterMark(final int p0);
    
    SctpServerChannelConfig setWriteBufferLowWaterMark(final int p0);
    
    SctpServerChannelConfig setMessageSizeEstimator(final MessageSizeEstimator p0);
}
