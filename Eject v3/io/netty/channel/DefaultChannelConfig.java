package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.nio.AbstractNioByteChannel;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultChannelConfig
        implements ChannelConfig {
    private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR = AdaptiveRecvByteBufAllocator.DEFAULT;
    private static final MessageSizeEstimator DEFAULT_MSG_SIZE_ESTIMATOR = DefaultMessageSizeEstimator.DEFAULT;
    private static final int DEFAULT_CONNECT_TIMEOUT = 30000;
    protected final Channel channel;
    private volatile ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
    private volatile RecvByteBufAllocator rcvBufAllocator = DEFAULT_RCVBUF_ALLOCATOR;
    private volatile MessageSizeEstimator msgSizeEstimator = DEFAULT_MSG_SIZE_ESTIMATOR;
    private volatile int connectTimeoutMillis = 30000;
    private volatile int maxMessagesPerRead;
    private volatile int writeSpinCount = 16;
    private volatile boolean autoRead = true;
    private volatile boolean autoClose = true;
    private volatile int writeBufferHighWaterMark = 65536;
    private volatile int writeBufferLowWaterMark = 32768;

    public DefaultChannelConfig(Channel paramChannel) {
        if (paramChannel == null) {
            throw new NullPointerException("channel");
        }
        this.channel = paramChannel;
        if (((paramChannel instanceof ServerChannel)) || ((paramChannel instanceof AbstractNioByteChannel))) {
            this.maxMessagesPerRead = 16;
        } else {
            this.maxMessagesPerRead = 1;
        }
    }

    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(null, new ChannelOption[]{ChannelOption.CONNECT_TIMEOUT_MILLIS, ChannelOption.MAX_MESSAGES_PER_READ, ChannelOption.WRITE_SPIN_COUNT, ChannelOption.ALLOCATOR, ChannelOption.AUTO_READ, ChannelOption.AUTO_CLOSE, ChannelOption.RCVBUF_ALLOCATOR, ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, ChannelOption.MESSAGE_SIZE_ESTIMATOR});
    }

    protected Map<ChannelOption<?>, Object> getOptions(Map<ChannelOption<?>, Object> paramMap, ChannelOption<?>... paramVarArgs) {
        if (paramMap == null) {
            paramMap = new IdentityHashMap();
        }
        for (ChannelOption<?> localChannelOption : paramVarArgs) {
            paramMap.put(localChannelOption, getOption(localChannelOption));
        }
        return paramMap;
    }

    public boolean setOptions(Map<ChannelOption<?>, ?> paramMap) {
        if (paramMap == null) {
            throw new NullPointerException("options");
        }
        boolean bool = true;
        Iterator localIterator = paramMap.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            if (!setOption((ChannelOption) localEntry.getKey(), localEntry.getValue())) {
                bool = false;
            }
        }
        return bool;
    }

    public <T> T getOption(ChannelOption<T> paramChannelOption) {
        if (paramChannelOption == null) {
            throw new NullPointerException("option");
        }
        if (paramChannelOption == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
            return Integer.valueOf(getConnectTimeoutMillis());
        }
        if (paramChannelOption == ChannelOption.MAX_MESSAGES_PER_READ) {
            return Integer.valueOf(getMaxMessagesPerRead());
        }
        if (paramChannelOption == ChannelOption.WRITE_SPIN_COUNT) {
            return Integer.valueOf(getWriteSpinCount());
        }
        if (paramChannelOption == ChannelOption.ALLOCATOR) {
            return getAllocator();
        }
        if (paramChannelOption == ChannelOption.RCVBUF_ALLOCATOR) {
            return getRecvByteBufAllocator();
        }
        if (paramChannelOption == ChannelOption.AUTO_READ) {
            return Boolean.valueOf(isAutoRead());
        }
        if (paramChannelOption == ChannelOption.AUTO_CLOSE) {
            return Boolean.valueOf(isAutoClose());
        }
        if (paramChannelOption == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
            return Integer.valueOf(getWriteBufferHighWaterMark());
        }
        if (paramChannelOption == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
            return Integer.valueOf(getWriteBufferLowWaterMark());
        }
        if (paramChannelOption == ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
            return getMessageSizeEstimator();
        }
        return null;
    }

    public <T> boolean setOption(ChannelOption<T> paramChannelOption, T paramT) {
        validate(paramChannelOption, paramT);
        if (paramChannelOption == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
            setConnectTimeoutMillis(((Integer) paramT).intValue());
        } else if (paramChannelOption == ChannelOption.MAX_MESSAGES_PER_READ) {
            setMaxMessagesPerRead(((Integer) paramT).intValue());
        } else if (paramChannelOption == ChannelOption.WRITE_SPIN_COUNT) {
            setWriteSpinCount(((Integer) paramT).intValue());
        } else if (paramChannelOption == ChannelOption.ALLOCATOR) {
            setAllocator((ByteBufAllocator) paramT);
        } else if (paramChannelOption == ChannelOption.RCVBUF_ALLOCATOR) {
            setRecvByteBufAllocator((RecvByteBufAllocator) paramT);
        } else if (paramChannelOption == ChannelOption.AUTO_READ) {
            setAutoRead(((Boolean) paramT).booleanValue());
        } else if (paramChannelOption == ChannelOption.AUTO_CLOSE) {
            setAutoClose(((Boolean) paramT).booleanValue());
        } else if (paramChannelOption == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
            setWriteBufferHighWaterMark(((Integer) paramT).intValue());
        } else if (paramChannelOption == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
            setWriteBufferLowWaterMark(((Integer) paramT).intValue());
        } else if (paramChannelOption == ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
            setMessageSizeEstimator((MessageSizeEstimator) paramT);
        } else {
            return false;
        }
        return true;
    }

    protected <T> void validate(ChannelOption<T> paramChannelOption, T paramT) {
        if (paramChannelOption == null) {
            throw new NullPointerException("option");
        }
        paramChannelOption.validate(paramT);
    }

    public int getConnectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }

    public ChannelConfig setConnectTimeoutMillis(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException(String.format("connectTimeoutMillis: %d (expected: >= 0)", new Object[]{Integer.valueOf(paramInt)}));
        }
        this.connectTimeoutMillis = paramInt;
        return this;
    }

    public int getMaxMessagesPerRead() {
        return this.maxMessagesPerRead;
    }

    public ChannelConfig setMaxMessagesPerRead(int paramInt) {
        if (paramInt <= 0) {
            throw new IllegalArgumentException("maxMessagesPerRead: " + paramInt + " (expected: > 0)");
        }
        this.maxMessagesPerRead = paramInt;
        return this;
    }

    public int getWriteSpinCount() {
        return this.writeSpinCount;
    }

    public ChannelConfig setWriteSpinCount(int paramInt) {
        if (paramInt <= 0) {
            throw new IllegalArgumentException("writeSpinCount must be a positive integer.");
        }
        this.writeSpinCount = paramInt;
        return this;
    }

    public ByteBufAllocator getAllocator() {
        return this.allocator;
    }

    public ChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator) {
        if (paramByteBufAllocator == null) {
            throw new NullPointerException("allocator");
        }
        this.allocator = paramByteBufAllocator;
        return this;
    }

    public RecvByteBufAllocator getRecvByteBufAllocator() {
        return this.rcvBufAllocator;
    }

    public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator) {
        if (paramRecvByteBufAllocator == null) {
            throw new NullPointerException("allocator");
        }
        this.rcvBufAllocator = paramRecvByteBufAllocator;
        return this;
    }

    public boolean isAutoRead() {
        return this.autoRead;
    }

    public ChannelConfig setAutoRead(boolean paramBoolean) {
        boolean bool = this.autoRead;
        this.autoRead = paramBoolean;
        if ((paramBoolean) && (!bool)) {
            this.channel.read();
        } else if ((!paramBoolean) && (bool)) {
            autoReadCleared();
        }
        return this;
    }

    protected void autoReadCleared() {
    }

    public boolean isAutoClose() {
        return this.autoClose;
    }

    public ChannelConfig setAutoClose(boolean paramBoolean) {
        this.autoClose = paramBoolean;
        return this;
    }

    public int getWriteBufferHighWaterMark() {
        return this.writeBufferHighWaterMark;
    }

    public ChannelConfig setWriteBufferHighWaterMark(int paramInt) {
        if (paramInt < getWriteBufferLowWaterMark()) {
            throw new IllegalArgumentException("writeBufferHighWaterMark cannot be less than writeBufferLowWaterMark (" + getWriteBufferLowWaterMark() + "): " + paramInt);
        }
        if (paramInt < 0) {
            throw new IllegalArgumentException("writeBufferHighWaterMark must be >= 0");
        }
        this.writeBufferHighWaterMark = paramInt;
        return this;
    }

    public int getWriteBufferLowWaterMark() {
        return this.writeBufferLowWaterMark;
    }

    public ChannelConfig setWriteBufferLowWaterMark(int paramInt) {
        if (paramInt > getWriteBufferHighWaterMark()) {
            throw new IllegalArgumentException("writeBufferLowWaterMark cannot be greater than writeBufferHighWaterMark (" + getWriteBufferHighWaterMark() + "): " + paramInt);
        }
        if (paramInt < 0) {
            throw new IllegalArgumentException("writeBufferLowWaterMark must be >= 0");
        }
        this.writeBufferLowWaterMark = paramInt;
        return this;
    }

    public MessageSizeEstimator getMessageSizeEstimator() {
        return this.msgSizeEstimator;
    }

    public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator) {
        if (paramMessageSizeEstimator == null) {
            throw new NullPointerException("estimator");
        }
        this.msgSizeEstimator = paramMessageSizeEstimator;
        return this;
    }
}




