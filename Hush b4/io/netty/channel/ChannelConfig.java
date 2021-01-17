// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import java.util.Map;

public interface ChannelConfig
{
    Map<ChannelOption<?>, Object> getOptions();
    
    boolean setOptions(final Map<ChannelOption<?>, ?> p0);
    
     <T> T getOption(final ChannelOption<T> p0);
    
     <T> boolean setOption(final ChannelOption<T> p0, final T p1);
    
    int getConnectTimeoutMillis();
    
    ChannelConfig setConnectTimeoutMillis(final int p0);
    
    int getMaxMessagesPerRead();
    
    ChannelConfig setMaxMessagesPerRead(final int p0);
    
    int getWriteSpinCount();
    
    ChannelConfig setWriteSpinCount(final int p0);
    
    ByteBufAllocator getAllocator();
    
    ChannelConfig setAllocator(final ByteBufAllocator p0);
    
    RecvByteBufAllocator getRecvByteBufAllocator();
    
    ChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator p0);
    
    boolean isAutoRead();
    
    ChannelConfig setAutoRead(final boolean p0);
    
    @Deprecated
    boolean isAutoClose();
    
    @Deprecated
    ChannelConfig setAutoClose(final boolean p0);
    
    int getWriteBufferHighWaterMark();
    
    ChannelConfig setWriteBufferHighWaterMark(final int p0);
    
    int getWriteBufferLowWaterMark();
    
    ChannelConfig setWriteBufferLowWaterMark(final int p0);
    
    MessageSizeEstimator getMessageSizeEstimator();
    
    ChannelConfig setMessageSizeEstimator(final MessageSizeEstimator p0);
}
