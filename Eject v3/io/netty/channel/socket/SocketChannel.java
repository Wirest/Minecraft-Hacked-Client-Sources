package io.netty.channel.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;

import java.net.InetSocketAddress;

public abstract interface SocketChannel
        extends Channel {
    public abstract ServerSocketChannel parent();

    public abstract SocketChannelConfig config();

    public abstract InetSocketAddress localAddress();

    public abstract InetSocketAddress remoteAddress();

    public abstract boolean isInputShutdown();

    public abstract boolean isOutputShutdown();

    public abstract ChannelFuture shutdownOutput();

    public abstract ChannelFuture shutdownOutput(ChannelPromise paramChannelPromise);
}




