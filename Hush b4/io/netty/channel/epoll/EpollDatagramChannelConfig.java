// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.epoll;

import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.MessageSizeEstimator;
import java.net.NetworkInterface;
import java.net.InetAddress;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.channel.Channel;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.DefaultChannelConfig;

public final class EpollDatagramChannelConfig extends DefaultChannelConfig implements DatagramChannelConfig
{
    private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR;
    private final EpollDatagramChannel datagramChannel;
    private boolean activeOnOpen;
    
    EpollDatagramChannelConfig(final EpollDatagramChannel channel) {
        super(channel);
        this.datagramChannel = channel;
        this.setRecvByteBufAllocator(EpollDatagramChannelConfig.DEFAULT_RCVBUF_ALLOCATOR);
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), ChannelOption.SO_BROADCAST, ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SO_REUSEADDR, ChannelOption.IP_MULTICAST_LOOP_DISABLED, ChannelOption.IP_MULTICAST_ADDR, ChannelOption.IP_MULTICAST_IF, ChannelOption.IP_MULTICAST_TTL, ChannelOption.IP_TOS, ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION, EpollChannelOption.SO_REUSEPORT);
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == ChannelOption.SO_BROADCAST) {
            return (T)Boolean.valueOf(this.isBroadcast());
        }
        if (option == ChannelOption.SO_RCVBUF) {
            return (T)Integer.valueOf(this.getReceiveBufferSize());
        }
        if (option == ChannelOption.SO_SNDBUF) {
            return (T)Integer.valueOf(this.getSendBufferSize());
        }
        if (option == ChannelOption.SO_REUSEADDR) {
            return (T)Boolean.valueOf(this.isReuseAddress());
        }
        if (option == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
            return (T)Boolean.valueOf(this.isLoopbackModeDisabled());
        }
        if (option == ChannelOption.IP_MULTICAST_ADDR) {
            return (T)this.getInterface();
        }
        if (option == ChannelOption.IP_MULTICAST_IF) {
            return (T)this.getNetworkInterface();
        }
        if (option == ChannelOption.IP_MULTICAST_TTL) {
            return (T)Integer.valueOf(this.getTimeToLive());
        }
        if (option == ChannelOption.IP_TOS) {
            return (T)Integer.valueOf(this.getTrafficClass());
        }
        if (option == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
            return (T)Boolean.valueOf(this.activeOnOpen);
        }
        if (option == EpollChannelOption.SO_REUSEPORT) {
            return (T)Boolean.valueOf(this.isReusePort());
        }
        return super.getOption(option);
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.validate(option, value);
        if (option == ChannelOption.SO_BROADCAST) {
            this.setBroadcast((boolean)value);
        }
        else if (option == ChannelOption.SO_RCVBUF) {
            this.setReceiveBufferSize((int)value);
        }
        else if (option == ChannelOption.SO_SNDBUF) {
            this.setSendBufferSize((int)value);
        }
        else if (option == ChannelOption.SO_REUSEADDR) {
            this.setReuseAddress((boolean)value);
        }
        else if (option == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
            this.setLoopbackModeDisabled((boolean)value);
        }
        else if (option == ChannelOption.IP_MULTICAST_ADDR) {
            this.setInterface((InetAddress)value);
        }
        else if (option == ChannelOption.IP_MULTICAST_IF) {
            this.setNetworkInterface((NetworkInterface)value);
        }
        else if (option == ChannelOption.IP_MULTICAST_TTL) {
            this.setTimeToLive((int)value);
        }
        else if (option == ChannelOption.IP_TOS) {
            this.setTrafficClass((int)value);
        }
        else if (option == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
            this.setActiveOnOpen((boolean)value);
        }
        else {
            if (option != EpollChannelOption.SO_REUSEPORT) {
                return super.setOption(option, value);
            }
            this.setReusePort((boolean)value);
        }
        return true;
    }
    
    private void setActiveOnOpen(final boolean activeOnOpen) {
        if (this.channel.isRegistered()) {
            throw new IllegalStateException("Can only changed before channel was registered");
        }
        this.activeOnOpen = activeOnOpen;
    }
    
    @Override
    public EpollDatagramChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
    
    @Override
    public EpollDatagramChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }
    
    @Override
    public EpollDatagramChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }
    
    @Override
    public EpollDatagramChannelConfig setAutoClose(final boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }
    
    @Override
    public EpollDatagramChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Override
    public EpollDatagramChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }
    
    @Override
    public EpollDatagramChannelConfig setWriteSpinCount(final int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }
    
    @Override
    public EpollDatagramChannelConfig setAllocator(final ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }
    
    @Override
    public EpollDatagramChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }
    
    @Override
    public EpollDatagramChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }
    
    @Override
    public int getSendBufferSize() {
        return Native.getSendBufferSize(this.datagramChannel.fd);
    }
    
    @Override
    public EpollDatagramChannelConfig setSendBufferSize(final int sendBufferSize) {
        Native.setSendBufferSize(this.datagramChannel.fd, sendBufferSize);
        return this;
    }
    
    @Override
    public int getReceiveBufferSize() {
        return Native.getReceiveBufferSize(this.datagramChannel.fd);
    }
    
    @Override
    public EpollDatagramChannelConfig setReceiveBufferSize(final int receiveBufferSize) {
        Native.setReceiveBufferSize(this.datagramChannel.fd, receiveBufferSize);
        return this;
    }
    
    @Override
    public int getTrafficClass() {
        return Native.getTrafficClass(this.datagramChannel.fd);
    }
    
    @Override
    public EpollDatagramChannelConfig setTrafficClass(final int trafficClass) {
        Native.setTrafficClass(this.datagramChannel.fd, trafficClass);
        return this;
    }
    
    @Override
    public boolean isReuseAddress() {
        return Native.isReuseAddress(this.datagramChannel.fd) == 1;
    }
    
    @Override
    public EpollDatagramChannelConfig setReuseAddress(final boolean reuseAddress) {
        Native.setReuseAddress(this.datagramChannel.fd, reuseAddress ? 1 : 0);
        return this;
    }
    
    @Override
    public boolean isBroadcast() {
        return Native.isBroadcast(this.datagramChannel.fd) == 1;
    }
    
    @Override
    public EpollDatagramChannelConfig setBroadcast(final boolean broadcast) {
        Native.setBroadcast(this.datagramChannel.fd, broadcast ? 1 : 0);
        return this;
    }
    
    @Override
    public boolean isLoopbackModeDisabled() {
        return false;
    }
    
    @Override
    public DatagramChannelConfig setLoopbackModeDisabled(final boolean loopbackModeDisabled) {
        throw new UnsupportedOperationException("Multicast not supported");
    }
    
    @Override
    public int getTimeToLive() {
        return -1;
    }
    
    @Override
    public EpollDatagramChannelConfig setTimeToLive(final int ttl) {
        throw new UnsupportedOperationException("Multicast not supported");
    }
    
    @Override
    public InetAddress getInterface() {
        return null;
    }
    
    @Override
    public EpollDatagramChannelConfig setInterface(final InetAddress interfaceAddress) {
        throw new UnsupportedOperationException("Multicast not supported");
    }
    
    @Override
    public NetworkInterface getNetworkInterface() {
        return null;
    }
    
    @Override
    public EpollDatagramChannelConfig setNetworkInterface(final NetworkInterface networkInterface) {
        throw new UnsupportedOperationException("Multicast not supported");
    }
    
    public boolean isReusePort() {
        return Native.isReusePort(this.datagramChannel.fd) == 1;
    }
    
    public EpollDatagramChannelConfig setReusePort(final boolean reusePort) {
        Native.setReusePort(this.datagramChannel.fd, reusePort ? 1 : 0);
        return this;
    }
    
    @Override
    protected void autoReadCleared() {
        this.datagramChannel.clearEpollIn();
    }
    
    static {
        DEFAULT_RCVBUF_ALLOCATOR = new FixedRecvByteBufAllocator(2048);
    }
}
