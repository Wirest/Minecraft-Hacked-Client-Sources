// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.socket;

import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import java.net.SocketException;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.util.NetUtil;
import io.netty.channel.Channel;
import java.net.ServerSocket;
import io.netty.channel.DefaultChannelConfig;

public class DefaultServerSocketChannelConfig extends DefaultChannelConfig implements ServerSocketChannelConfig
{
    protected final ServerSocket javaSocket;
    private volatile int backlog;
    
    public DefaultServerSocketChannelConfig(final ServerSocketChannel channel, final ServerSocket javaSocket) {
        super(channel);
        this.backlog = NetUtil.SOMAXCONN;
        if (javaSocket == null) {
            throw new NullPointerException("javaSocket");
        }
        this.javaSocket = javaSocket;
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), ChannelOption.SO_RCVBUF, ChannelOption.SO_REUSEADDR, ChannelOption.SO_BACKLOG);
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
        else {
            if (option != ChannelOption.SO_BACKLOG) {
                return super.setOption(option, value);
            }
            this.setBacklog((int)value);
        }
        return true;
    }
    
    @Override
    public boolean isReuseAddress() {
        try {
            return this.javaSocket.getReuseAddress();
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public ServerSocketChannelConfig setReuseAddress(final boolean reuseAddress) {
        try {
            this.javaSocket.setReuseAddress(reuseAddress);
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public int getReceiveBufferSize() {
        try {
            return this.javaSocket.getReceiveBufferSize();
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public ServerSocketChannelConfig setReceiveBufferSize(final int receiveBufferSize) {
        try {
            this.javaSocket.setReceiveBufferSize(receiveBufferSize);
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public ServerSocketChannelConfig setPerformancePreferences(final int connectionTime, final int latency, final int bandwidth) {
        this.javaSocket.setPerformancePreferences(connectionTime, latency, bandwidth);
        return this;
    }
    
    @Override
    public int getBacklog() {
        return this.backlog;
    }
    
    @Override
    public ServerSocketChannelConfig setBacklog(final int backlog) {
        if (backlog < 0) {
            throw new IllegalArgumentException("backlog: " + backlog);
        }
        this.backlog = backlog;
        return this;
    }
    
    @Override
    public ServerSocketChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }
    
    @Override
    public ServerSocketChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }
    
    @Override
    public ServerSocketChannelConfig setWriteSpinCount(final int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }
    
    @Override
    public ServerSocketChannelConfig setAllocator(final ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }
    
    @Override
    public ServerSocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }
    
    @Override
    public ServerSocketChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Override
    public ServerSocketChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }
    
    @Override
    public ServerSocketChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }
    
    @Override
    public ServerSocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
}
