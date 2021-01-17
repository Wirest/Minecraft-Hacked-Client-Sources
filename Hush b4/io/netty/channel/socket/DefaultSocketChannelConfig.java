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
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.Channel;
import java.net.Socket;
import io.netty.channel.DefaultChannelConfig;

public class DefaultSocketChannelConfig extends DefaultChannelConfig implements SocketChannelConfig
{
    protected final Socket javaSocket;
    private volatile boolean allowHalfClosure;
    
    public DefaultSocketChannelConfig(final SocketChannel channel, final Socket javaSocket) {
        super(channel);
        if (javaSocket == null) {
            throw new NullPointerException("javaSocket");
        }
        this.javaSocket = javaSocket;
        if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
            try {
                this.setTcpNoDelay(true);
            }
            catch (Exception ex) {}
        }
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE);
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == ChannelOption.SO_RCVBUF) {
            return (T)Integer.valueOf(this.getReceiveBufferSize());
        }
        if (option == ChannelOption.SO_SNDBUF) {
            return (T)Integer.valueOf(this.getSendBufferSize());
        }
        if (option == ChannelOption.TCP_NODELAY) {
            return (T)Boolean.valueOf(this.isTcpNoDelay());
        }
        if (option == ChannelOption.SO_KEEPALIVE) {
            return (T)Boolean.valueOf(this.isKeepAlive());
        }
        if (option == ChannelOption.SO_REUSEADDR) {
            return (T)Boolean.valueOf(this.isReuseAddress());
        }
        if (option == ChannelOption.SO_LINGER) {
            return (T)Integer.valueOf(this.getSoLinger());
        }
        if (option == ChannelOption.IP_TOS) {
            return (T)Integer.valueOf(this.getTrafficClass());
        }
        if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
            return (T)Boolean.valueOf(this.isAllowHalfClosure());
        }
        return super.getOption(option);
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.validate(option, value);
        if (option == ChannelOption.SO_RCVBUF) {
            this.setReceiveBufferSize((int)value);
        }
        else if (option == ChannelOption.SO_SNDBUF) {
            this.setSendBufferSize((int)value);
        }
        else if (option == ChannelOption.TCP_NODELAY) {
            this.setTcpNoDelay((boolean)value);
        }
        else if (option == ChannelOption.SO_KEEPALIVE) {
            this.setKeepAlive((boolean)value);
        }
        else if (option == ChannelOption.SO_REUSEADDR) {
            this.setReuseAddress((boolean)value);
        }
        else if (option == ChannelOption.SO_LINGER) {
            this.setSoLinger((int)value);
        }
        else if (option == ChannelOption.IP_TOS) {
            this.setTrafficClass((int)value);
        }
        else {
            if (option != ChannelOption.ALLOW_HALF_CLOSURE) {
                return super.setOption(option, value);
            }
            this.setAllowHalfClosure((boolean)value);
        }
        return true;
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
    public int getSendBufferSize() {
        try {
            return this.javaSocket.getSendBufferSize();
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public int getSoLinger() {
        try {
            return this.javaSocket.getSoLinger();
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public int getTrafficClass() {
        try {
            return this.javaSocket.getTrafficClass();
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public boolean isKeepAlive() {
        try {
            return this.javaSocket.getKeepAlive();
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
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
    public boolean isTcpNoDelay() {
        try {
            return this.javaSocket.getTcpNoDelay();
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public SocketChannelConfig setKeepAlive(final boolean keepAlive) {
        try {
            this.javaSocket.setKeepAlive(keepAlive);
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public SocketChannelConfig setPerformancePreferences(final int connectionTime, final int latency, final int bandwidth) {
        this.javaSocket.setPerformancePreferences(connectionTime, latency, bandwidth);
        return this;
    }
    
    @Override
    public SocketChannelConfig setReceiveBufferSize(final int receiveBufferSize) {
        try {
            this.javaSocket.setReceiveBufferSize(receiveBufferSize);
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public SocketChannelConfig setReuseAddress(final boolean reuseAddress) {
        try {
            this.javaSocket.setReuseAddress(reuseAddress);
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public SocketChannelConfig setSendBufferSize(final int sendBufferSize) {
        try {
            this.javaSocket.setSendBufferSize(sendBufferSize);
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public SocketChannelConfig setSoLinger(final int soLinger) {
        try {
            if (soLinger < 0) {
                this.javaSocket.setSoLinger(false, 0);
            }
            else {
                this.javaSocket.setSoLinger(true, soLinger);
            }
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public SocketChannelConfig setTcpNoDelay(final boolean tcpNoDelay) {
        try {
            this.javaSocket.setTcpNoDelay(tcpNoDelay);
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public SocketChannelConfig setTrafficClass(final int trafficClass) {
        try {
            this.javaSocket.setTrafficClass(trafficClass);
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public boolean isAllowHalfClosure() {
        return this.allowHalfClosure;
    }
    
    @Override
    public SocketChannelConfig setAllowHalfClosure(final boolean allowHalfClosure) {
        this.allowHalfClosure = allowHalfClosure;
        return this;
    }
    
    @Override
    public SocketChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }
    
    @Override
    public SocketChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }
    
    @Override
    public SocketChannelConfig setWriteSpinCount(final int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }
    
    @Override
    public SocketChannelConfig setAllocator(final ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }
    
    @Override
    public SocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }
    
    @Override
    public SocketChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Override
    public SocketChannelConfig setAutoClose(final boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }
    
    @Override
    public SocketChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }
    
    @Override
    public SocketChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }
    
    @Override
    public SocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
}
