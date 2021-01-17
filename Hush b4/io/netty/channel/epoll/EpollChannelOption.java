// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.epoll;

import io.netty.channel.ChannelOption;

public final class EpollChannelOption<T> extends ChannelOption<T>
{
    public static final ChannelOption<Boolean> TCP_CORK;
    public static final ChannelOption<Integer> TCP_KEEPIDLE;
    public static final ChannelOption<Integer> TCP_KEEPINTVL;
    public static final ChannelOption<Integer> TCP_KEEPCNT;
    public static final ChannelOption<Boolean> SO_REUSEPORT;
    
    private EpollChannelOption(final String name) {
        super(name);
    }
    
    static {
        TCP_CORK = ChannelOption.valueOf("TCP_CORK");
        TCP_KEEPIDLE = ChannelOption.valueOf("TCP_KEEPIDLE");
        TCP_KEEPINTVL = ChannelOption.valueOf("TCP_KEEPINTVL");
        TCP_KEEPCNT = ChannelOption.valueOf("TCP_KEEPCNT");
        SO_REUSEPORT = ChannelOption.valueOf("SO_REUSEPORT");
    }
}
