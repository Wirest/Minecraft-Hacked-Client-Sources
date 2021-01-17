// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.udt;

import io.netty.channel.ChannelOption;

public final class UdtChannelOption<T> extends ChannelOption<T>
{
    public static final UdtChannelOption<Integer> PROTOCOL_RECEIVE_BUFFER_SIZE;
    public static final UdtChannelOption<Integer> PROTOCOL_SEND_BUFFER_SIZE;
    public static final UdtChannelOption<Integer> SYSTEM_RECEIVE_BUFFER_SIZE;
    public static final UdtChannelOption<Integer> SYSTEM_SEND_BUFFER_SIZE;
    
    private UdtChannelOption(final String name) {
        super(name);
    }
    
    static {
        PROTOCOL_RECEIVE_BUFFER_SIZE = new UdtChannelOption<Integer>("PROTOCOL_RECEIVE_BUFFER_SIZE");
        PROTOCOL_SEND_BUFFER_SIZE = new UdtChannelOption<Integer>("PROTOCOL_SEND_BUFFER_SIZE");
        SYSTEM_RECEIVE_BUFFER_SIZE = new UdtChannelOption<Integer>("SYSTEM_RECEIVE_BUFFER_SIZE");
        SYSTEM_SEND_BUFFER_SIZE = new UdtChannelOption<Integer>("SYSTEM_SEND_BUFFER_SIZE");
    }
}
