// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.epoll;

import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.util.NetUtil;
import io.netty.channel.Channel;
import io.netty.channel.socket.ServerSocketChannelConfig;
import io.netty.channel.DefaultChannelConfig;

public final class EpollServerSocketChannelConfig extends DefaultChannelConfig implements ServerSocketChannelConfig
{
    private final EpollServerSocketChannel channel;
    private volatile int backlog;
    
    EpollServerSocketChannelConfig(final EpollServerSocketChannel channel) {
        super(channel);
        this.backlog = NetUtil.SOMAXCONN;
        this.channel = channel;
        this.setReuseAddress(true);
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), ChannelOption.SO_RCVBUF, ChannelOption.SO_REUSEADDR, ChannelOption.SO_BACKLOG, EpollChannelOption.SO_REUSEPORT);
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == ChannelOption.SO_RCVBUF) {
            return (T)Integer.valueOf(this.getReceiveBufferSize());
        }
        if (option == ChannelOption.SO_REUSEADDR) {
            return (T)Boolean.valueOf(this.isReuseAddress());
        }
        if (option == ChannelOption.SO_BACKLOG) {
            return (T)Integer.valueOf(this.getBacklog());
        }
        if (option == EpollChannelOption.SO_REUSEPORT) {
            return (T)Boolean.valueOf(this.isReusePort());
        }
        return super.getOption(option);
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.validate(option, value);
        if (option == ChannelOption.SO_RCVBUF) {
            this.setReceiveBufferSize((int)value);
        }
        else if (option == ChannelOption.SO_REUSEADDR) {
            this.setReuseAddress((boolean)value);
        }
        else if (option == ChannelOption.SO_BACKLOG) {
            this.setBacklog((int)value);
        }
        else {
            if (option != EpollChannelOption.SO_REUSEPORT) {
                return super.setOption(option, value);
            }
            this.setReusePort((boolean)value);
        }
        return true;
    }
    
    @Override
    public boolean isReuseAddress() {
        return Native.isReuseAddress(this.channel.fd) == 1;
    }
    
    @Override
    public EpollServerSocketChannelConfig setReuseAddress(final boolean reuseAddress) {
        Native.setReuseAddress(this.channel.fd, reuseAddress ? 1 : 0);
        return this;
    }
    
    @Override
    public int getReceiveBufferSize() {
        return Native.getReceiveBufferSize(this.channel.fd);
    }
    
    @Override
    public EpollServerSocketChannelConfig setReceiveBufferSize(final int receiveBufferSize) {
        Native.setReceiveBufferSize(this.channel.fd, receiveBufferSize);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setPerformancePreferences(final int connectionTime, final int latency, final int bandwidth) {
        return this;
    }
    
    @Override
    public int getBacklog() {
        return this.backlog;
    }
    
    @Override
    public EpollServerSocketChannelConfig setBacklog(final int backlog) {
        if (backlog < 0) {
            throw new IllegalArgumentException("backlog: " + backlog);
        }
        this.backlog = backlog;
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setWriteSpinCount(final int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setAllocator(final ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
    
    public boolean isReusePort() {
        return Native.isReusePort(this.channel.fd) == 1;
    }
    
    public EpollServerSocketChannelConfig setReusePort(final boolean reusePort) {
        Native.setReusePort(this.channel.fd, reusePort ? 1 : 0);
        return this;
    }
    
    @Override
    protected void autoReadCleared() {
        this.channel.clearEpollIn();
    }
}
