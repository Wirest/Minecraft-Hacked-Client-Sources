// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.socket.oio;

import io.netty.channel.ChannelConfig;
import io.netty.channel.socket.ServerSocketChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import java.io.IOException;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import java.util.Map;
import java.net.ServerSocket;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.DefaultServerSocketChannelConfig;

public class DefaultOioServerSocketChannelConfig extends DefaultServerSocketChannelConfig implements OioServerSocketChannelConfig
{
    @Deprecated
    public DefaultOioServerSocketChannelConfig(final ServerSocketChannel channel, final ServerSocket javaSocket) {
        super(channel, javaSocket);
    }
    
    DefaultOioServerSocketChannelConfig(final OioServerSocketChannel channel, final ServerSocket javaSocket) {
        super(channel, javaSocket);
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), ChannelOption.SO_TIMEOUT);
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == ChannelOption.SO_TIMEOUT) {
            return (T)Integer.valueOf(this.getSoTimeout());
        }
        return super.getOption(option);
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.validate(option, value);
        if (option == ChannelOption.SO_TIMEOUT) {
            this.setSoTimeout((int)value);
            return true;
        }
        return super.setOption(option, value);
    }
    
    @Override
    public OioServerSocketChannelConfig setSoTimeout(final int timeout) {
        try {
            this.javaSocket.setSoTimeout(timeout);
        }
        catch (IOException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public int getSoTimeout() {
        try {
            return this.javaSocket.getSoTimeout();
        }
        catch (IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public OioServerSocketChannelConfig setBacklog(final int backlog) {
        super.setBacklog(backlog);
        return this;
    }
    
    @Override
    public OioServerSocketChannelConfig setReuseAddress(final boolean reuseAddress) {
        super.setReuseAddress(reuseAddress);
        return this;
    }
    
    @Override
    public OioServerSocketChannelConfig setReceiveBufferSize(final int receiveBufferSize) {
        super.setReceiveBufferSize(receiveBufferSize);
        return this;
    }
    
    @Override
    public OioServerSocketChannelConfig setPerformancePreferences(final int connectionTime, final int latency, final int bandwidth) {
        super.setPerformancePreferences(connectionTime, latency, bandwidth);
        return this;
    }
    
    @Override
    public OioServerSocketChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }
    
    @Override
    public OioServerSocketChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }
    
    @Override
    public OioServerSocketChannelConfig setWriteSpinCount(final int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }
    
    @Override
    public OioServerSocketChannelConfig setAllocator(final ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }
    
    @Override
    public OioServerSocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }
    
    @Override
    public OioServerSocketChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Override
    protected void autoReadCleared() {
        if (this.channel instanceof OioServerSocketChannel) {
            ((OioServerSocketChannel)this.channel).setReadPending(false);
        }
    }
    
    @Override
    public OioServerSocketChannelConfig setAutoClose(final boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }
    
    @Override
    public OioServerSocketChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }
    
    @Override
    public OioServerSocketChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }
    
    @Override
    public OioServerSocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
}
