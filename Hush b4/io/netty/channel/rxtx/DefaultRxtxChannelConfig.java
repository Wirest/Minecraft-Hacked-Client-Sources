// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.rxtx;

import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.channel.Channel;
import io.netty.channel.DefaultChannelConfig;

final class DefaultRxtxChannelConfig extends DefaultChannelConfig implements RxtxChannelConfig
{
    private volatile int baudrate;
    private volatile boolean dtr;
    private volatile boolean rts;
    private volatile Stopbits stopbits;
    private volatile Databits databits;
    private volatile Paritybit paritybit;
    private volatile int waitTime;
    private volatile int readTimeout;
    
    DefaultRxtxChannelConfig(final RxtxChannel channel) {
        super(channel);
        this.baudrate = 115200;
        this.stopbits = Stopbits.STOPBITS_1;
        this.databits = Databits.DATABITS_8;
        this.paritybit = Paritybit.NONE;
        this.readTimeout = 1000;
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), RxtxChannelOption.BAUD_RATE, RxtxChannelOption.DTR, RxtxChannelOption.RTS, RxtxChannelOption.STOP_BITS, RxtxChannelOption.DATA_BITS, RxtxChannelOption.PARITY_BIT, RxtxChannelOption.WAIT_TIME);
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == RxtxChannelOption.BAUD_RATE) {
            return (T)Integer.valueOf(this.getBaudrate());
        }
        if (option == RxtxChannelOption.DTR) {
            return (T)Boolean.valueOf(this.isDtr());
        }
        if (option == RxtxChannelOption.RTS) {
            return (T)Boolean.valueOf(this.isRts());
        }
        if (option == RxtxChannelOption.STOP_BITS) {
            return (T)this.getStopbits();
        }
        if (option == RxtxChannelOption.DATA_BITS) {
            return (T)this.getDatabits();
        }
        if (option == RxtxChannelOption.PARITY_BIT) {
            return (T)this.getParitybit();
        }
        if (option == RxtxChannelOption.WAIT_TIME) {
            return (T)Integer.valueOf(this.getWaitTimeMillis());
        }
        if (option == RxtxChannelOption.READ_TIMEOUT) {
            return (T)Integer.valueOf(this.getReadTimeout());
        }
        return super.getOption(option);
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.validate(option, value);
        if (option == RxtxChannelOption.BAUD_RATE) {
            this.setBaudrate((int)value);
        }
        else if (option == RxtxChannelOption.DTR) {
            this.setDtr((boolean)value);
        }
        else if (option == RxtxChannelOption.RTS) {
            this.setRts((boolean)value);
        }
        else if (option == RxtxChannelOption.STOP_BITS) {
            this.setStopbits((Stopbits)value);
        }
        else if (option == RxtxChannelOption.DATA_BITS) {
            this.setDatabits((Databits)value);
        }
        else if (option == RxtxChannelOption.PARITY_BIT) {
            this.setParitybit((Paritybit)value);
        }
        else if (option == RxtxChannelOption.WAIT_TIME) {
            this.setWaitTimeMillis((int)value);
        }
        else {
            if (option != RxtxChannelOption.READ_TIMEOUT) {
                return super.setOption(option, value);
            }
            this.setReadTimeout((int)value);
        }
        return true;
    }
    
    @Override
    public RxtxChannelConfig setBaudrate(final int baudrate) {
        this.baudrate = baudrate;
        return this;
    }
    
    @Override
    public RxtxChannelConfig setStopbits(final Stopbits stopbits) {
        this.stopbits = stopbits;
        return this;
    }
    
    @Override
    public RxtxChannelConfig setDatabits(final Databits databits) {
        this.databits = databits;
        return this;
    }
    
    @Override
    public RxtxChannelConfig setParitybit(final Paritybit paritybit) {
        this.paritybit = paritybit;
        return this;
    }
    
    @Override
    public int getBaudrate() {
        return this.baudrate;
    }
    
    @Override
    public Stopbits getStopbits() {
        return this.stopbits;
    }
    
    @Override
    public Databits getDatabits() {
        return this.databits;
    }
    
    @Override
    public Paritybit getParitybit() {
        return this.paritybit;
    }
    
    @Override
    public boolean isDtr() {
        return this.dtr;
    }
    
    @Override
    public RxtxChannelConfig setDtr(final boolean dtr) {
        this.dtr = dtr;
        return this;
    }
    
    @Override
    public boolean isRts() {
        return this.rts;
    }
    
    @Override
    public RxtxChannelConfig setRts(final boolean rts) {
        this.rts = rts;
        return this;
    }
    
    @Override
    public int getWaitTimeMillis() {
        return this.waitTime;
    }
    
    @Override
    public RxtxChannelConfig setWaitTimeMillis(final int waitTimeMillis) {
        if (waitTimeMillis < 0) {
            throw new IllegalArgumentException("Wait time must be >= 0");
        }
        this.waitTime = waitTimeMillis;
        return this;
    }
    
    @Override
    public RxtxChannelConfig setReadTimeout(final int readTimeout) {
        if (readTimeout < 0) {
            throw new IllegalArgumentException("readTime must be >= 0");
        }
        this.readTimeout = readTimeout;
        return this;
    }
    
    @Override
    public int getReadTimeout() {
        return this.readTimeout;
    }
    
    @Override
    public RxtxChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }
    
    @Override
    public RxtxChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }
    
    @Override
    public RxtxChannelConfig setWriteSpinCount(final int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }
    
    @Override
    public RxtxChannelConfig setAllocator(final ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }
    
    @Override
    public RxtxChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }
    
    @Override
    public RxtxChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Override
    public RxtxChannelConfig setAutoClose(final boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }
    
    @Override
    public RxtxChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }
    
    @Override
    public RxtxChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }
    
    @Override
    public RxtxChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
}
