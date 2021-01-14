package io.netty.channel.local;

import io.netty.channel.Channel;

import java.net.SocketAddress;

public final class LocalAddress
        extends SocketAddress
        implements Comparable<LocalAddress> {
    public static final LocalAddress ANY = new LocalAddress("ANY");
    private static final long serialVersionUID = 4644331421130916435L;
    private final String id;
    private final String strVal;

    LocalAddress(Channel paramChannel) {
        StringBuilder localStringBuilder = new StringBuilder(16);
        localStringBuilder.append("local:E");
        localStringBuilder.append(Long.toHexString(paramChannel.hashCode() & 0xFFFFFFFF | 0x100000000));
        localStringBuilder.setCharAt(7, ':');
        this.id = localStringBuilder.substring(6);
        this.strVal = localStringBuilder.toString();
    }

    public LocalAddress(String paramString) {
        if (paramString == null) {
            throw new NullPointerException("id");
        }
        paramString = paramString.trim().toLowerCase();
        if (paramString.isEmpty()) {
            throw new IllegalArgumentException("empty id");
        }
        this.id = paramString;
        this.strVal = ("local:" + paramString);
    }

    public String id() {
        return this.id;
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public boolean equals(Object paramObject) {
        if (!(paramObject instanceof LocalAddress)) {
            return false;
        }
        return this.id.equals(((LocalAddress) paramObject).id);
    }

    public int compareTo(LocalAddress paramLocalAddress) {
        return this.id.compareTo(paramLocalAddress.id);
    }

    public String toString() {
        return this.strVal;
    }
}




