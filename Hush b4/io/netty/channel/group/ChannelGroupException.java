// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.group;

import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import io.netty.channel.Channel;
import java.util.Map;
import io.netty.channel.ChannelException;

public class ChannelGroupException extends ChannelException implements Iterable<Map.Entry<Channel, Throwable>>
{
    private static final long serialVersionUID = -4093064295562629453L;
    private final Collection<Map.Entry<Channel, Throwable>> failed;
    
    public ChannelGroupException(final Collection<Map.Entry<Channel, Throwable>> causes) {
        if (causes == null) {
            throw new NullPointerException("causes");
        }
        if (causes.isEmpty()) {
            throw new IllegalArgumentException("causes must be non empty");
        }
        this.failed = Collections.unmodifiableCollection((Collection<? extends Map.Entry<Channel, Throwable>>)causes);
    }
    
    @Override
    public Iterator<Map.Entry<Channel, Throwable>> iterator() {
        return this.failed.iterator();
    }
}
