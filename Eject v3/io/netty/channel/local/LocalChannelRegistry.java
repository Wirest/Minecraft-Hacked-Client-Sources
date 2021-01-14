package io.netty.channel.local;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.util.internal.StringUtil;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentMap;

final class LocalChannelRegistry {
    private static final ConcurrentMap<LocalAddress, Channel> boundChannels = ;

    static LocalAddress register(Channel paramChannel, LocalAddress paramLocalAddress, SocketAddress paramSocketAddress) {
        if (paramLocalAddress != null) {
            throw new ChannelException("already bound");
        }
        if (!(paramSocketAddress instanceof LocalAddress)) {
            throw new ChannelException("unsupported address type: " + StringUtil.simpleClassName(paramSocketAddress));
        }
        LocalAddress localLocalAddress = (LocalAddress) paramSocketAddress;
        if (LocalAddress.ANY.equals(localLocalAddress)) {
            localLocalAddress = new LocalAddress(paramChannel);
        }
        Channel localChannel = (Channel) boundChannels.putIfAbsent(localLocalAddress, paramChannel);
        if (localChannel != null) {
            throw new ChannelException("address already in use by: " + localChannel);
        }
        return localLocalAddress;
    }

    static Channel get(SocketAddress paramSocketAddress) {
        return (Channel) boundChannels.get(paramSocketAddress);
    }

    static void unregister(LocalAddress paramLocalAddress) {
        boundChannels.remove(paramLocalAddress);
    }
}




