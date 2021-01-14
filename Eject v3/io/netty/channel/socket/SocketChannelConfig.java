package io.netty.channel.socket;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;

public abstract interface SocketChannelConfig
        extends ChannelConfig {
    public abstract boolean isTcpNoDelay();

    public abstract SocketChannelConfig setTcpNoDelay(boolean paramBoolean);

    public abstract int getSoLinger();

    public abstract SocketChannelConfig setSoLinger(int paramInt);

    public abstract int getSendBufferSize();

    public abstract SocketChannelConfig setSendBufferSize(int paramInt);

    public abstract int getReceiveBufferSize();

    public abstract SocketChannelConfig setReceiveBufferSize(int paramInt);

    public abstract boolean isKeepAlive();

    public abstract SocketChannelConfig setKeepAlive(boolean paramBoolean);

    public abstract int getTrafficClass();

    public abstract SocketChannelConfig setTrafficClass(int paramInt);

    public abstract boolean isReuseAddress();

    public abstract SocketChannelConfig setReuseAddress(boolean paramBoolean);

    public abstract SocketChannelConfig setPerformancePreferences(int paramInt1, int paramInt2, int paramInt3);

    public abstract boolean isAllowHalfClosure();

    public abstract SocketChannelConfig setAllowHalfClosure(boolean paramBoolean);

    public abstract SocketChannelConfig setConnectTimeoutMillis(int paramInt);

    public abstract SocketChannelConfig setMaxMessagesPerRead(int paramInt);

    public abstract SocketChannelConfig setWriteSpinCount(int paramInt);

    public abstract SocketChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);

    public abstract SocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);

    public abstract SocketChannelConfig setAutoRead(boolean paramBoolean);

    public abstract SocketChannelConfig setAutoClose(boolean paramBoolean);

    public abstract SocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
}




