// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.rxtx;

import io.netty.channel.ChannelOption;

public final class RxtxChannelOption<T> extends ChannelOption<T>
{
    public static final RxtxChannelOption<Integer> BAUD_RATE;
    public static final RxtxChannelOption<Boolean> DTR;
    public static final RxtxChannelOption<Boolean> RTS;
    public static final RxtxChannelOption<RxtxChannelConfig.Stopbits> STOP_BITS;
    public static final RxtxChannelOption<RxtxChannelConfig.Databits> DATA_BITS;
    public static final RxtxChannelOption<RxtxChannelConfig.Paritybit> PARITY_BIT;
    public static final RxtxChannelOption<Integer> WAIT_TIME;
    public static final RxtxChannelOption<Integer> READ_TIMEOUT;
    
    private RxtxChannelOption(final String name) {
        super(name);
    }
    
    static {
        BAUD_RATE = new RxtxChannelOption<Integer>("BAUD_RATE");
        DTR = new RxtxChannelOption<Boolean>("DTR");
        RTS = new RxtxChannelOption<Boolean>("RTS");
        STOP_BITS = new RxtxChannelOption<RxtxChannelConfig.Stopbits>("STOP_BITS");
        DATA_BITS = new RxtxChannelOption<RxtxChannelConfig.Databits>("DATA_BITS");
        PARITY_BIT = new RxtxChannelOption<RxtxChannelConfig.Paritybit>("PARITY_BIT");
        WAIT_TIME = new RxtxChannelOption<Integer>("WAIT_TIME");
        READ_TIMEOUT = new RxtxChannelOption<Integer>("READ_TIMEOUT");
    }
}
