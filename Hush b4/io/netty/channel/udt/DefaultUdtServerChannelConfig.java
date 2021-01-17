// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.udt;

import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import java.util.Map;
import io.netty.channel.ChannelOption;
import java.io.IOException;
import com.barchart.udt.nio.ChannelUDT;

public class DefaultUdtServerChannelConfig extends DefaultUdtChannelConfig implements UdtServerChannelConfig
{
    private volatile int backlog;
    
    public DefaultUdtServerChannelConfig(final UdtChannel channel, final ChannelUDT channelUDT, final boolean apply) throws IOException {
        super(channel, channelUDT, apply);
        this.backlog = 64;
        if (apply) {
            this.apply(channelUDT);
        }
    }
    
    @Override
    protected void apply(final ChannelUDT channelUDT) throws IOException {
    }
    
    @Override
    public int getBacklog() {
        return this.backlog;
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == ChannelOption.SO_BACKLOG) {
            return (T)Integer.valueOf(this.getBacklog());
        }
        return super.getOption(option);
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), ChannelOption.SO_BACKLOG);
    }
    
    @Override
    public UdtServerChannelConfig setBacklog(final int backlog) {
        this.backlog = backlog;
        return this;
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.validate(option, value);
        if (option == ChannelOption.SO_BACKLOG) {
            this.setBacklog((int)value);
            return true;
        }
        return super.setOption(option, value);
    }
    
    @Override
    public UdtServerChannelConfig setProtocolReceiveBufferSize(final int protocolReceiveBuferSize) {
        super.setProtocolReceiveBufferSize(protocolReceiveBuferSize);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setProtocolSendBufferSize(final int protocolSendBuferSize) {
        super.setProtocolSendBufferSize(protocolSendBuferSize);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setReceiveBufferSize(final int receiveBufferSize) {
        super.setReceiveBufferSize(receiveBufferSize);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setReuseAddress(final boolean reuseAddress) {
        super.setReuseAddress(reuseAddress);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setSendBufferSize(final int sendBufferSize) {
        super.setSendBufferSize(sendBufferSize);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setSoLinger(final int soLinger) {
        super.setSoLinger(soLinger);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setSystemReceiveBufferSize(final int systemSendBuferSize) {
        super.setSystemReceiveBufferSize(systemSendBuferSize);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setSystemSendBufferSize(final int systemReceiveBufferSize) {
        super.setSystemSendBufferSize(systemReceiveBufferSize);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setWriteSpinCount(final int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setAllocator(final ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setAutoClose(final boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }
    
    @Override
    public UdtServerChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
}
