// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.local;

import io.netty.channel.Channel;
import java.net.SocketAddress;

public final class LocalAddress extends SocketAddress implements Comparable<LocalAddress>
{
    private static final long serialVersionUID = 4644331421130916435L;
    public static final LocalAddress ANY;
    private final String id;
    private final String strVal;
    
    LocalAddress(final Channel channel) {
        final StringBuilder buf = new StringBuilder(16);
        buf.append("local:E");
        buf.append(Long.toHexString(((long)channel.hashCode() & 0xFFFFFFFFL) | 0x100000000L));
        buf.setCharAt(7, ':');
        this.id = buf.substring(6);
        this.strVal = buf.toString();
    }
    
    public LocalAddress(String id) {
        if (id == null) {
            throw new NullPointerException("id");
        }
        id = id.trim().toLowerCase();
        if (id.isEmpty()) {
            throw new IllegalArgumentException("empty id");
        }
        this.id = id;
        this.strVal = "local:" + id;
    }
    
    public String id() {
        return this.id;
    }
    
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof LocalAddress && this.id.equals(((LocalAddress)o).id);
    }
    
    @Override
    public int compareTo(final LocalAddress o) {
        return this.id.compareTo(o.id);
    }
    
    @Override
    public String toString() {
        return this.strVal;
    }
    
    static {
        ANY = new LocalAddress("ANY");
    }
}
