// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.sctp;

import java.net.SocketAddress;
import com.sun.nio.sctp.SctpStandardSocketOptions;
import io.netty.channel.ChannelOption;

public class SctpChannelOption<T> extends ChannelOption<T>
{
    public static final SctpChannelOption<Boolean> SCTP_DISABLE_FRAGMENTS;
    public static final SctpChannelOption<Boolean> SCTP_EXPLICIT_COMPLETE;
    public static final SctpChannelOption<Integer> SCTP_FRAGMENT_INTERLEAVE;
    public static final SctpChannelOption<SctpStandardSocketOptions.InitMaxStreams> SCTP_INIT_MAXSTREAMS;
    public static final SctpChannelOption<Boolean> SCTP_NODELAY;
    public static final SctpChannelOption<SocketAddress> SCTP_PRIMARY_ADDR;
    public static final SctpChannelOption<SocketAddress> SCTP_SET_PEER_PRIMARY_ADDR;
    
    @Deprecated
    protected SctpChannelOption(final String name) {
        super(name);
    }
    
    static {
        SCTP_DISABLE_FRAGMENTS = new SctpChannelOption<Boolean>("SCTP_DISABLE_FRAGMENTS");
        SCTP_EXPLICIT_COMPLETE = new SctpChannelOption<Boolean>("SCTP_EXPLICIT_COMPLETE");
        SCTP_FRAGMENT_INTERLEAVE = new SctpChannelOption<Integer>("SCTP_FRAGMENT_INTERLEAVE");
        SCTP_INIT_MAXSTREAMS = new SctpChannelOption<SctpStandardSocketOptions.InitMaxStreams>("SCTP_INIT_MAXSTREAMS");
        SCTP_NODELAY = new SctpChannelOption<Boolean>("SCTP_NODELAY");
        SCTP_PRIMARY_ADDR = new SctpChannelOption<SocketAddress>("SCTP_PRIMARY_ADDR");
        SCTP_SET_PEER_PRIMARY_ADDR = new SctpChannelOption<SocketAddress>("SCTP_SET_PEER_PRIMARY_ADDR");
    }
}
