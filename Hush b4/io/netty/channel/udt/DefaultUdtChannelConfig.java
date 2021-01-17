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
import com.barchart.udt.SocketUDT;
import com.barchart.udt.OptionUDT;
import java.io.IOException;
import io.netty.channel.Channel;
import com.barchart.udt.nio.ChannelUDT;
import io.netty.channel.DefaultChannelConfig;

public class DefaultUdtChannelConfig extends DefaultChannelConfig implements UdtChannelConfig
{
    private static final int K = 1024;
    private static final int M = 1048576;
    private volatile int protocolReceiveBuferSize;
    private volatile int protocolSendBuferSize;
    private volatile int systemReceiveBufferSize;
    private volatile int systemSendBuferSize;
    private volatile int allocatorReceiveBufferSize;
    private volatile int allocatorSendBufferSize;
    private volatile int soLinger;
    private volatile boolean reuseAddress;
    
    public DefaultUdtChannelConfig(final UdtChannel channel, final ChannelUDT channelUDT, final boolean apply) throws IOException {
        super(channel);
        this.protocolReceiveBuferSize = 10485760;
        this.protocolSendBuferSize = 10485760;
        this.systemReceiveBufferSize = 1048576;
        this.systemSendBuferSize = 1048576;
        this.allocatorReceiveBufferSize = 131072;
        this.allocatorSendBufferSize = 131072;
        this.reuseAddress = true;
        if (apply) {
            this.apply(channelUDT);
        }
    }
    
    protected void apply(final ChannelUDT channelUDT) throws IOException {
        final SocketUDT socketUDT = channelUDT.socketUDT();
        socketUDT.setReuseAddress(this.isReuseAddress());
        socketUDT.setSendBufferSize(this.getSendBufferSize());
        if (this.getSoLinger() <= 0) {
            socketUDT.setSoLinger(false, 0);
        }
        else {
            socketUDT.setSoLinger(true, this.getSoLinger());
        }
        socketUDT.setOption(OptionUDT.Protocol_Receive_Buffer_Size, (Object)this.getProtocolReceiveBufferSize());
        socketUDT.setOption(OptionUDT.Protocol_Send_Buffer_Size, (Object)this.getProtocolSendBufferSize());
        socketUDT.setOption(OptionUDT.System_Receive_Buffer_Size, (Object)this.getSystemReceiveBufferSize());
        socketUDT.setOption(OptionUDT.System_Send_Buffer_Size, (Object)this.getSystemSendBufferSize());
    }
    
    @Override
    public int getProtocolReceiveBufferSize() {
        return this.protocolReceiveBuferSize;
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE) {
            return (T)Integer.valueOf(this.getProtocolReceiveBufferSize());
        }
        if (option == UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE) {
            return (T)Integer.valueOf(this.getProtocolSendBufferSize());
        }
        if (option == UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE) {
            return (T)Integer.valueOf(this.getSystemReceiveBufferSize());
        }
        if (option == UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE) {
            return (T)Integer.valueOf(this.getSystemSendBufferSize());
        }
        if (option == UdtChannelOption.SO_RCVBUF) {
            return (T)Integer.valueOf(this.getReceiveBufferSize());
        }
        if (option == UdtChannelOption.SO_SNDBUF) {
            return (T)Integer.valueOf(this.getSendBufferSize());
        }
        if (option == UdtChannelOption.SO_REUSEADDR) {
            return (T)Boolean.valueOf(this.isReuseAddress());
        }
        if (option == UdtChannelOption.SO_LINGER) {
            return (T)Integer.valueOf(this.getSoLinger());
        }
        return super.getOption(option);
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE, UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE, UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE, UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE, UdtChannelOption.SO_RCVBUF, UdtChannelOption.SO_SNDBUF, UdtChannelOption.SO_REUSEADDR, UdtChannelOption.SO_LINGER);
    }
    
    @Override
    public int getReceiveBufferSize() {
        return this.allocatorReceiveBufferSize;
    }
    
    @Override
    public int getSendBufferSize() {
        return this.allocatorSendBufferSize;
    }
    
    @Override
    public int getSoLinger() {
        return this.soLinger;
    }
    
    @Override
    public boolean isReuseAddress() {
        return this.reuseAddress;
    }
    
    @Override
    public UdtChannelConfig setProtocolReceiveBufferSize(final int protocolReceiveBuferSize) {
        this.protocolReceiveBuferSize = protocolReceiveBuferSize;
        return this;
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.validate(option, value);
        if (option == UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE) {
            this.setProtocolReceiveBufferSize((int)value);
        }
        else if (option == UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE) {
            this.setProtocolSendBufferSize((int)value);
        }
        else if (option == UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE) {
            this.setSystemReceiveBufferSize((int)value);
        }
        else if (option == UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE) {
            this.setSystemSendBufferSize((int)value);
        }
        else if (option == UdtChannelOption.SO_RCVBUF) {
            this.setReceiveBufferSize((int)value);
        }
        else if (option == UdtChannelOption.SO_SNDBUF) {
            this.setSendBufferSize((int)value);
        }
        else if (option == UdtChannelOption.SO_REUSEADDR) {
            this.setReuseAddress((boolean)value);
        }
        else {
            if (option != UdtChannelOption.SO_LINGER) {
                return super.setOption(option, value);
            }
            this.setSoLinger((int)value);
        }
        return true;
    }
    
    @Override
    public UdtChannelConfig setReceiveBufferSize(final int receiveBufferSize) {
        this.allocatorReceiveBufferSize = receiveBufferSize;
        return this;
    }
    
    @Override
    public UdtChannelConfig setReuseAddress(final boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
        return this;
    }
    
    @Override
    public UdtChannelConfig setSendBufferSize(final int sendBufferSize) {
        this.allocatorSendBufferSize = sendBufferSize;
        return this;
    }
    
    @Override
    public UdtChannelConfig setSoLinger(final int soLinger) {
        this.soLinger = soLinger;
        return this;
    }
    
    @Override
    public int getSystemReceiveBufferSize() {
        return this.systemReceiveBufferSize;
    }
    
    @Override
    public UdtChannelConfig setSystemSendBufferSize(final int systemReceiveBufferSize) {
        this.systemReceiveBufferSize = systemReceiveBufferSize;
        return this;
    }
    
    @Override
    public int getProtocolSendBufferSize() {
        return this.protocolSendBuferSize;
    }
    
    @Override
    public UdtChannelConfig setProtocolSendBufferSize(final int protocolSendBuferSize) {
        this.protocolSendBuferSize = protocolSendBuferSize;
        return this;
    }
    
    @Override
    public UdtChannelConfig setSystemReceiveBufferSize(final int systemSendBuferSize) {
        this.systemSendBuferSize = systemSendBuferSize;
        return this;
    }
    
    @Override
    public int getSystemSendBufferSize() {
        return this.systemSendBuferSize;
    }
    
    @Override
    public UdtChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }
    
    @Override
    public UdtChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }
    
    @Override
    public UdtChannelConfig setWriteSpinCount(final int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }
    
    @Override
    public UdtChannelConfig setAllocator(final ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }
    
    @Override
    public UdtChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }
    
    @Override
    public UdtChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Override
    public UdtChannelConfig setAutoClose(final boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }
    
    @Override
    public UdtChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }
    
    @Override
    public UdtChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }
    
    @Override
    public UdtChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
}
