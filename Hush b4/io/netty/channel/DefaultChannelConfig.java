// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import java.util.Iterator;
import java.util.IdentityHashMap;
import java.util.Map;
import io.netty.channel.nio.AbstractNioByteChannel;
import io.netty.buffer.ByteBufAllocator;

public class DefaultChannelConfig implements ChannelConfig
{
    private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR;
    private static final MessageSizeEstimator DEFAULT_MSG_SIZE_ESTIMATOR;
    private static final int DEFAULT_CONNECT_TIMEOUT = 30000;
    protected final Channel channel;
    private volatile ByteBufAllocator allocator;
    private volatile RecvByteBufAllocator rcvBufAllocator;
    private volatile MessageSizeEstimator msgSizeEstimator;
    private volatile int connectTimeoutMillis;
    private volatile int maxMessagesPerRead;
    private volatile int writeSpinCount;
    private volatile boolean autoRead;
    private volatile boolean autoClose;
    private volatile int writeBufferHighWaterMark;
    private volatile int writeBufferLowWaterMark;
    
    public DefaultChannelConfig(final Channel channel) {
        this.allocator = ByteBufAllocator.DEFAULT;
        this.rcvBufAllocator = DefaultChannelConfig.DEFAULT_RCVBUF_ALLOCATOR;
        this.msgSizeEstimator = DefaultChannelConfig.DEFAULT_MSG_SIZE_ESTIMATOR;
        this.connectTimeoutMillis = 30000;
        this.writeSpinCount = 16;
        this.autoRead = true;
        this.autoClose = true;
        this.writeBufferHighWaterMark = 65536;
        this.writeBufferLowWaterMark = 32768;
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        this.channel = channel;
        if (channel instanceof ServerChannel || channel instanceof AbstractNioByteChannel) {
            this.maxMessagesPerRead = 16;
        }
        else {
            this.maxMessagesPerRead = 1;
        }
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(null, ChannelOption.CONNECT_TIMEOUT_MILLIS, ChannelOption.MAX_MESSAGES_PER_READ, ChannelOption.WRITE_SPIN_COUNT, ChannelOption.ALLOCATOR, ChannelOption.AUTO_READ, ChannelOption.AUTO_CLOSE, ChannelOption.RCVBUF_ALLOCATOR, ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, ChannelOption.MESSAGE_SIZE_ESTIMATOR);
    }
    
    protected Map<ChannelOption<?>, Object> getOptions(Map<ChannelOption<?>, Object> result, final ChannelOption<?>... options) {
        if (result == null) {
            result = new IdentityHashMap<ChannelOption<?>, Object>();
        }
        for (final ChannelOption<?> o : options) {
            result.put(o, this.getOption(o));
        }
        return result;
    }
    
    @Override
    public boolean setOptions(final Map<ChannelOption<?>, ?> options) {
        if (options == null) {
            throw new NullPointerException("options");
        }
        boolean setAllOptions = true;
        for (final Map.Entry<ChannelOption<?>, ?> e : options.entrySet()) {
            if (!this.setOption(e.getKey(), e.getValue())) {
                setAllOptions = false;
            }
        }
        return setAllOptions;
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == null) {
            throw new NullPointerException("option");
        }
        if (option == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
            return (T)Integer.valueOf(this.getConnectTimeoutMillis());
        }
        if (option == ChannelOption.MAX_MESSAGES_PER_READ) {
            return (T)Integer.valueOf(this.getMaxMessagesPerRead());
        }
        if (option == ChannelOption.WRITE_SPIN_COUNT) {
            return (T)Integer.valueOf(this.getWriteSpinCount());
        }
        if (option == ChannelOption.ALLOCATOR) {
            return (T)this.getAllocator();
        }
        if (option == ChannelOption.RCVBUF_ALLOCATOR) {
            return (T)this.getRecvByteBufAllocator();
        }
        if (option == ChannelOption.AUTO_READ) {
            return (T)Boolean.valueOf(this.isAutoRead());
        }
        if (option == ChannelOption.AUTO_CLOSE) {
            return (T)Boolean.valueOf(this.isAutoClose());
        }
        if (option == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
            return (T)Integer.valueOf(this.getWriteBufferHighWaterMark());
        }
        if (option == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
            return (T)Integer.valueOf(this.getWriteBufferLowWaterMark());
        }
        if (option == ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
            return (T)this.getMessageSizeEstimator();
        }
        return null;
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.validate(option, value);
        if (option == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
            this.setConnectTimeoutMillis((int)value);
        }
        else if (option == ChannelOption.MAX_MESSAGES_PER_READ) {
            this.setMaxMessagesPerRead((int)value);
        }
        else if (option == ChannelOption.WRITE_SPIN_COUNT) {
            this.setWriteSpinCount((int)value);
        }
        else if (option == ChannelOption.ALLOCATOR) {
            this.setAllocator((ByteBufAllocator)value);
        }
        else if (option == ChannelOption.RCVBUF_ALLOCATOR) {
            this.setRecvByteBufAllocator((RecvByteBufAllocator)value);
        }
        else if (option == ChannelOption.AUTO_READ) {
            this.setAutoRead((boolean)value);
        }
        else if (option == ChannelOption.AUTO_CLOSE) {
            this.setAutoClose((boolean)value);
        }
        else if (option == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
            this.setWriteBufferHighWaterMark((int)value);
        }
        else if (option == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
            this.setWriteBufferLowWaterMark((int)value);
        }
        else {
            if (option != ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
                return false;
            }
            this.setMessageSizeEstimator((MessageSizeEstimator)value);
        }
        return true;
    }
    
    protected <T> void validate(final ChannelOption<T> option, final T value) {
        if (option == null) {
            throw new NullPointerException("option");
        }
        option.validate(value);
    }
    
    @Override
    public int getConnectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }
    
    @Override
    public ChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        if (connectTimeoutMillis < 0) {
            throw new IllegalArgumentException(String.format("connectTimeoutMillis: %d (expected: >= 0)", connectTimeoutMillis));
        }
        this.connectTimeoutMillis = connectTimeoutMillis;
        return this;
    }
    
    @Override
    public int getMaxMessagesPerRead() {
        return this.maxMessagesPerRead;
    }
    
    @Override
    public ChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        if (maxMessagesPerRead <= 0) {
            throw new IllegalArgumentException("maxMessagesPerRead: " + maxMessagesPerRead + " (expected: > 0)");
        }
        this.maxMessagesPerRead = maxMessagesPerRead;
        return this;
    }
    
    @Override
    public int getWriteSpinCount() {
        return this.writeSpinCount;
    }
    
    @Override
    public ChannelConfig setWriteSpinCount(final int writeSpinCount) {
        if (writeSpinCount <= 0) {
            throw new IllegalArgumentException("writeSpinCount must be a positive integer.");
        }
        this.writeSpinCount = writeSpinCount;
        return this;
    }
    
    @Override
    public ByteBufAllocator getAllocator() {
        return this.allocator;
    }
    
    @Override
    public ChannelConfig setAllocator(final ByteBufAllocator allocator) {
        if (allocator == null) {
            throw new NullPointerException("allocator");
        }
        this.allocator = allocator;
        return this;
    }
    
    @Override
    public RecvByteBufAllocator getRecvByteBufAllocator() {
        return this.rcvBufAllocator;
    }
    
    @Override
    public ChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        if (allocator == null) {
            throw new NullPointerException("allocator");
        }
        this.rcvBufAllocator = allocator;
        return this;
    }
    
    @Override
    public boolean isAutoRead() {
        return this.autoRead;
    }
    
    @Override
    public ChannelConfig setAutoRead(final boolean autoRead) {
        final boolean oldAutoRead = this.autoRead;
        this.autoRead = autoRead;
        if (autoRead && !oldAutoRead) {
            this.channel.read();
        }
        else if (!autoRead && oldAutoRead) {
            this.autoReadCleared();
        }
        return this;
    }
    
    protected void autoReadCleared() {
    }
    
    @Override
    public boolean isAutoClose() {
        return this.autoClose;
    }
    
    @Override
    public ChannelConfig setAutoClose(final boolean autoClose) {
        this.autoClose = autoClose;
        return this;
    }
    
    @Override
    public int getWriteBufferHighWaterMark() {
        return this.writeBufferHighWaterMark;
    }
    
    @Override
    public ChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        if (writeBufferHighWaterMark < this.getWriteBufferLowWaterMark()) {
            throw new IllegalArgumentException("writeBufferHighWaterMark cannot be less than writeBufferLowWaterMark (" + this.getWriteBufferLowWaterMark() + "): " + writeBufferHighWaterMark);
        }
        if (writeBufferHighWaterMark < 0) {
            throw new IllegalArgumentException("writeBufferHighWaterMark must be >= 0");
        }
        this.writeBufferHighWaterMark = writeBufferHighWaterMark;
        return this;
    }
    
    @Override
    public int getWriteBufferLowWaterMark() {
        return this.writeBufferLowWaterMark;
    }
    
    @Override
    public ChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        if (writeBufferLowWaterMark > this.getWriteBufferHighWaterMark()) {
            throw new IllegalArgumentException("writeBufferLowWaterMark cannot be greater than writeBufferHighWaterMark (" + this.getWriteBufferHighWaterMark() + "): " + writeBufferLowWaterMark);
        }
        if (writeBufferLowWaterMark < 0) {
            throw new IllegalArgumentException("writeBufferLowWaterMark must be >= 0");
        }
        this.writeBufferLowWaterMark = writeBufferLowWaterMark;
        return this;
    }
    
    @Override
    public MessageSizeEstimator getMessageSizeEstimator() {
        return this.msgSizeEstimator;
    }
    
    @Override
    public ChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        if (estimator == null) {
            throw new NullPointerException("estimator");
        }
        this.msgSizeEstimator = estimator;
        return this;
    }
    
    static {
        DEFAULT_RCVBUF_ALLOCATOR = AdaptiveRecvByteBufAllocator.DEFAULT;
        DEFAULT_MSG_SIZE_ESTIMATOR = DefaultMessageSizeEstimator.DEFAULT;
    }
}
