// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.rxtx;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;

public interface RxtxChannelConfig extends ChannelConfig
{
    RxtxChannelConfig setBaudrate(final int p0);
    
    RxtxChannelConfig setStopbits(final Stopbits p0);
    
    RxtxChannelConfig setDatabits(final Databits p0);
    
    RxtxChannelConfig setParitybit(final Paritybit p0);
    
    int getBaudrate();
    
    Stopbits getStopbits();
    
    Databits getDatabits();
    
    Paritybit getParitybit();
    
    boolean isDtr();
    
    RxtxChannelConfig setDtr(final boolean p0);
    
    boolean isRts();
    
    RxtxChannelConfig setRts(final boolean p0);
    
    int getWaitTimeMillis();
    
    RxtxChannelConfig setWaitTimeMillis(final int p0);
    
    RxtxChannelConfig setReadTimeout(final int p0);
    
    int getReadTimeout();
    
    RxtxChannelConfig setConnectTimeoutMillis(final int p0);
    
    RxtxChannelConfig setMaxMessagesPerRead(final int p0);
    
    RxtxChannelConfig setWriteSpinCount(final int p0);
    
    RxtxChannelConfig setAllocator(final ByteBufAllocator p0);
    
    RxtxChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator p0);
    
    RxtxChannelConfig setAutoRead(final boolean p0);
    
    RxtxChannelConfig setAutoClose(final boolean p0);
    
    RxtxChannelConfig setWriteBufferHighWaterMark(final int p0);
    
    RxtxChannelConfig setWriteBufferLowWaterMark(final int p0);
    
    RxtxChannelConfig setMessageSizeEstimator(final MessageSizeEstimator p0);
    
    public enum Stopbits
    {
        STOPBITS_1(1), 
        STOPBITS_2(2), 
        STOPBITS_1_5(3);
        
        private final int value;
        
        private Stopbits(final int value) {
            this.value = value;
        }
        
        public int value() {
            return this.value;
        }
        
        public static Stopbits valueOf(final int value) {
            for (final Stopbits stopbit : values()) {
                if (stopbit.value == value) {
                    return stopbit;
                }
            }
            throw new IllegalArgumentException("unknown " + Stopbits.class.getSimpleName() + " value: " + value);
        }
    }
    
    public enum Databits
    {
        DATABITS_5(5), 
        DATABITS_6(6), 
        DATABITS_7(7), 
        DATABITS_8(8);
        
        private final int value;
        
        private Databits(final int value) {
            this.value = value;
        }
        
        public int value() {
            return this.value;
        }
        
        public static Databits valueOf(final int value) {
            for (final Databits databit : values()) {
                if (databit.value == value) {
                    return databit;
                }
            }
            throw new IllegalArgumentException("unknown " + Databits.class.getSimpleName() + " value: " + value);
        }
    }
    
    public enum Paritybit
    {
        NONE(0), 
        ODD(1), 
        EVEN(2), 
        MARK(3), 
        SPACE(4);
        
        private final int value;
        
        private Paritybit(final int value) {
            this.value = value;
        }
        
        public int value() {
            return this.value;
        }
        
        public static Paritybit valueOf(final int value) {
            for (final Paritybit paritybit : values()) {
                if (paritybit.value == value) {
                    return paritybit;
                }
            }
            throw new IllegalArgumentException("unknown " + Paritybit.class.getSimpleName() + " value: " + value);
        }
    }
}
