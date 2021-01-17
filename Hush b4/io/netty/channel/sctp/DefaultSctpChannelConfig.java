// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.sctp;

import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import java.io.IOException;
import io.netty.channel.ChannelException;
import com.sun.nio.sctp.SctpStandardSocketOptions;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.Channel;
import com.sun.nio.sctp.SctpChannel;
import io.netty.channel.DefaultChannelConfig;

public class DefaultSctpChannelConfig extends DefaultChannelConfig implements SctpChannelConfig
{
    private final SctpChannel javaChannel;
    
    public DefaultSctpChannelConfig(final io.netty.channel.sctp.SctpChannel channel, final SctpChannel javaChannel) {
        super(channel);
        if (javaChannel == null) {
            throw new NullPointerException("javaChannel");
        }
        this.javaChannel = javaChannel;
        if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
            try {
                this.setSctpNoDelay(true);
            }
            catch (Exception ex) {}
        }
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), SctpChannelOption.SO_RCVBUF, SctpChannelOption.SO_SNDBUF, SctpChannelOption.SCTP_NODELAY, SctpChannelOption.SCTP_INIT_MAXSTREAMS);
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == SctpChannelOption.SO_RCVBUF) {
            return (T)Integer.valueOf(this.getReceiveBufferSize());
        }
        if (option == SctpChannelOption.SO_SNDBUF) {
            return (T)Integer.valueOf(this.getSendBufferSize());
        }
        if (option == SctpChannelOption.SCTP_NODELAY) {
            return (T)Boolean.valueOf(this.isSctpNoDelay());
        }
        return super.getOption(option);
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.validate(option, value);
        if (option == SctpChannelOption.SO_RCVBUF) {
            this.setReceiveBufferSize((int)value);
        }
        else if (option == SctpChannelOption.SO_SNDBUF) {
            this.setSendBufferSize((int)value);
        }
        else if (option == SctpChannelOption.SCTP_NODELAY) {
            this.setSctpNoDelay((boolean)value);
        }
        else {
            if (option != SctpChannelOption.SCTP_INIT_MAXSTREAMS) {
                return super.setOption(option, value);
            }
            this.setInitMaxStreams((SctpStandardSocketOptions.InitMaxStreams)value);
        }
        return true;
    }
    
    @Override
    public boolean isSctpNoDelay() {
        try {
            return this.javaChannel.getOption(SctpStandardSocketOptions.SCTP_NODELAY);
        }
        catch (IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public SctpChannelConfig setSctpNoDelay(final boolean sctpNoDelay) {
        try {
            this.javaChannel.setOption(SctpStandardSocketOptions.SCTP_NODELAY, sctpNoDelay);
        }
        catch (IOException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public int getSendBufferSize() {
        try {
            return this.javaChannel.getOption(SctpStandardSocketOptions.SO_SNDBUF);
        }
        catch (IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public SctpChannelConfig setSendBufferSize(final int sendBufferSize) {
        try {
            this.javaChannel.setOption(SctpStandardSocketOptions.SO_SNDBUF, sendBufferSize);
        }
        catch (IOException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public int getReceiveBufferSize() {
        try {
            return this.javaChannel.getOption(SctpStandardSocketOptions.SO_RCVBUF);
        }
        catch (IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public SctpChannelConfig setReceiveBufferSize(final int receiveBufferSize) {
        try {
            this.javaChannel.setOption(SctpStandardSocketOptions.SO_RCVBUF, receiveBufferSize);
        }
        catch (IOException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public SctpStandardSocketOptions.InitMaxStreams getInitMaxStreams() {
        try {
            return this.javaChannel.getOption(SctpStandardSocketOptions.SCTP_INIT_MAXSTREAMS);
        }
        catch (IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public SctpChannelConfig setInitMaxStreams(final SctpStandardSocketOptions.InitMaxStreams initMaxStreams) {
        try {
            this.javaChannel.setOption(SctpStandardSocketOptions.SCTP_INIT_MAXSTREAMS, initMaxStreams);
        }
        catch (IOException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public SctpChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }
    
    @Override
    public SctpChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }
    
    @Override
    public SctpChannelConfig setWriteSpinCount(final int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }
    
    @Override
    public SctpChannelConfig setAllocator(final ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }
    
    @Override
    public SctpChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }
    
    @Override
    public SctpChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Override
    public SctpChannelConfig setAutoClose(final boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }
    
    @Override
    public SctpChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }
    
    @Override
    public SctpChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }
    
    @Override
    public SctpChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
}
