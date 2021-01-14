package io.netty.channel.socket;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.internal.PlatformDependent;

import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

public class DefaultSocketChannelConfig
        extends DefaultChannelConfig
        implements SocketChannelConfig {
    protected final Socket javaSocket;
    private volatile boolean allowHalfClosure;

    public DefaultSocketChannelConfig(SocketChannel paramSocketChannel, Socket paramSocket) {
        super(paramSocketChannel);
        if (paramSocket == null) {
            throw new NullPointerException("javaSocket");
        }
        this.javaSocket = paramSocket;
        if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
            try {
                setTcpNoDelay(true);
            } catch (Exception localException) {
            }
        }
    }

    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(super.getOptions(), new ChannelOption[]{ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE});
    }

    public <T> T getOption(ChannelOption<T> paramChannelOption) {
        if (paramChannelOption == ChannelOption.SO_RCVBUF) {
            return Integer.valueOf(getReceiveBufferSize());
        }
        if (paramChannelOption == ChannelOption.SO_SNDBUF) {
            return Integer.valueOf(getSendBufferSize());
        }
        if (paramChannelOption == ChannelOption.TCP_NODELAY) {
            return Boolean.valueOf(isTcpNoDelay());
        }
        if (paramChannelOption == ChannelOption.SO_KEEPALIVE) {
            return Boolean.valueOf(isKeepAlive());
        }
        if (paramChannelOption == ChannelOption.SO_REUSEADDR) {
            return Boolean.valueOf(isReuseAddress());
        }
        if (paramChannelOption == ChannelOption.SO_LINGER) {
            return Integer.valueOf(getSoLinger());
        }
        if (paramChannelOption == ChannelOption.IP_TOS) {
            return Integer.valueOf(getTrafficClass());
        }
        if (paramChannelOption == ChannelOption.ALLOW_HALF_CLOSURE) {
            return Boolean.valueOf(isAllowHalfClosure());
        }
        return (T) super.getOption(paramChannelOption);
    }

    public <T> boolean setOption(ChannelOption<T> paramChannelOption, T paramT) {
        validate(paramChannelOption, paramT);
        if (paramChannelOption == ChannelOption.SO_RCVBUF) {
            setReceiveBufferSize(((Integer) paramT).intValue());
        } else if (paramChannelOption == ChannelOption.SO_SNDBUF) {
            setSendBufferSize(((Integer) paramT).intValue());
        } else if (paramChannelOption == ChannelOption.TCP_NODELAY) {
            setTcpNoDelay(((Boolean) paramT).booleanValue());
        } else if (paramChannelOption == ChannelOption.SO_KEEPALIVE) {
            setKeepAlive(((Boolean) paramT).booleanValue());
        } else if (paramChannelOption == ChannelOption.SO_REUSEADDR) {
            setReuseAddress(((Boolean) paramT).booleanValue());
        } else if (paramChannelOption == ChannelOption.SO_LINGER) {
            setSoLinger(((Integer) paramT).intValue());
        } else if (paramChannelOption == ChannelOption.IP_TOS) {
            setTrafficClass(((Integer) paramT).intValue());
        } else if (paramChannelOption == ChannelOption.ALLOW_HALF_CLOSURE) {
            setAllowHalfClosure(((Boolean) paramT).booleanValue());
        } else {
            return super.setOption(paramChannelOption, paramT);
        }
        return true;
    }

    public int getReceiveBufferSize() {
        try {
            return this.javaSocket.getReceiveBufferSize();
        } catch (SocketException localSocketException) {
            throw new ChannelException(localSocketException);
        }
    }

    public int getSendBufferSize() {
        try {
            return this.javaSocket.getSendBufferSize();
        } catch (SocketException localSocketException) {
            throw new ChannelException(localSocketException);
        }
    }

    public int getSoLinger() {
        try {
            return this.javaSocket.getSoLinger();
        } catch (SocketException localSocketException) {
            throw new ChannelException(localSocketException);
        }
    }

    public int getTrafficClass() {
        try {
            return this.javaSocket.getTrafficClass();
        } catch (SocketException localSocketException) {
            throw new ChannelException(localSocketException);
        }
    }

    public boolean isKeepAlive() {
        try {
            return this.javaSocket.getKeepAlive();
        } catch (SocketException localSocketException) {
            throw new ChannelException(localSocketException);
        }
    }

    public boolean isReuseAddress() {
        try {
            return this.javaSocket.getReuseAddress();
        } catch (SocketException localSocketException) {
            throw new ChannelException(localSocketException);
        }
    }

    public boolean isTcpNoDelay() {
        try {
            return this.javaSocket.getTcpNoDelay();
        } catch (SocketException localSocketException) {
            throw new ChannelException(localSocketException);
        }
    }

    public SocketChannelConfig setKeepAlive(boolean paramBoolean) {
        try {
            this.javaSocket.setKeepAlive(paramBoolean);
        } catch (SocketException localSocketException) {
            throw new ChannelException(localSocketException);
        }
        return this;
    }

    public SocketChannelConfig setPerformancePreferences(int paramInt1, int paramInt2, int paramInt3) {
        this.javaSocket.setPerformancePreferences(paramInt1, paramInt2, paramInt3);
        return this;
    }

    public SocketChannelConfig setReceiveBufferSize(int paramInt) {
        try {
            this.javaSocket.setReceiveBufferSize(paramInt);
        } catch (SocketException localSocketException) {
            throw new ChannelException(localSocketException);
        }
        return this;
    }

    public SocketChannelConfig setReuseAddress(boolean paramBoolean) {
        try {
            this.javaSocket.setReuseAddress(paramBoolean);
        } catch (SocketException localSocketException) {
            throw new ChannelException(localSocketException);
        }
        return this;
    }

    public SocketChannelConfig setSendBufferSize(int paramInt) {
        try {
            this.javaSocket.setSendBufferSize(paramInt);
        } catch (SocketException localSocketException) {
            throw new ChannelException(localSocketException);
        }
        return this;
    }

    public SocketChannelConfig setSoLinger(int paramInt) {
        try {
            if (paramInt < 0) {
                this.javaSocket.setSoLinger(false, 0);
            } else {
                this.javaSocket.setSoLinger(true, paramInt);
            }
        } catch (SocketException localSocketException) {
            throw new ChannelException(localSocketException);
        }
        return this;
    }

    public SocketChannelConfig setTcpNoDelay(boolean paramBoolean) {
        try {
            this.javaSocket.setTcpNoDelay(paramBoolean);
        } catch (SocketException localSocketException) {
            throw new ChannelException(localSocketException);
        }
        return this;
    }

    public SocketChannelConfig setTrafficClass(int paramInt) {
        try {
            this.javaSocket.setTrafficClass(paramInt);
        } catch (SocketException localSocketException) {
            throw new ChannelException(localSocketException);
        }
        return this;
    }

    public boolean isAllowHalfClosure() {
        return this.allowHalfClosure;
    }

    public SocketChannelConfig setAllowHalfClosure(boolean paramBoolean) {
        this.allowHalfClosure = paramBoolean;
        return this;
    }

    public SocketChannelConfig setConnectTimeoutMillis(int paramInt) {
        super.setConnectTimeoutMillis(paramInt);
        return this;
    }

    public SocketChannelConfig setMaxMessagesPerRead(int paramInt) {
        super.setMaxMessagesPerRead(paramInt);
        return this;
    }

    public SocketChannelConfig setWriteSpinCount(int paramInt) {
        super.setWriteSpinCount(paramInt);
        return this;
    }

    public SocketChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator) {
        super.setAllocator(paramByteBufAllocator);
        return this;
    }

    public SocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator) {
        super.setRecvByteBufAllocator(paramRecvByteBufAllocator);
        return this;
    }

    public SocketChannelConfig setAutoRead(boolean paramBoolean) {
        super.setAutoRead(paramBoolean);
        return this;
    }

    public SocketChannelConfig setAutoClose(boolean paramBoolean) {
        super.setAutoClose(paramBoolean);
        return this;
    }

    public SocketChannelConfig setWriteBufferHighWaterMark(int paramInt) {
        super.setWriteBufferHighWaterMark(paramInt);
        return this;
    }

    public SocketChannelConfig setWriteBufferLowWaterMark(int paramInt) {
        super.setWriteBufferLowWaterMark(paramInt);
        return this;
    }

    public SocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator) {
        super.setMessageSizeEstimator(paramMessageSizeEstimator);
        return this;
    }
}




