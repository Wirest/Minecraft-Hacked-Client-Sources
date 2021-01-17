// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.group;

import io.netty.util.internal.StringUtil;
import java.util.Map;
import java.util.LinkedHashMap;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ServerChannel;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.internal.ConcurrentSet;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import io.netty.channel.Channel;
import java.util.AbstractSet;

public class DefaultChannelGroup extends AbstractSet<Channel> implements ChannelGroup
{
    private static final AtomicInteger nextId;
    private final String name;
    private final EventExecutor executor;
    private final ConcurrentSet<Channel> serverChannels;
    private final ConcurrentSet<Channel> nonServerChannels;
    private final ChannelFutureListener remover;
    
    public DefaultChannelGroup(final EventExecutor executor) {
        this("group-0x" + Integer.toHexString(DefaultChannelGroup.nextId.incrementAndGet()), executor);
    }
    
    public DefaultChannelGroup(final String name, final EventExecutor executor) {
        this.serverChannels = new ConcurrentSet<Channel>();
        this.nonServerChannels = new ConcurrentSet<Channel>();
        this.remover = new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                DefaultChannelGroup.this.remove(future.channel());
            }
        };
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.name = name;
        this.executor = executor;
    }
    
    @Override
    public String name() {
        return this.name;
    }
    
    @Override
    public boolean isEmpty() {
        return this.nonServerChannels.isEmpty() && this.serverChannels.isEmpty();
    }
    
    @Override
    public int size() {
        return this.nonServerChannels.size() + this.serverChannels.size();
    }
    
    @Override
    public boolean contains(final Object o) {
        if (!(o instanceof Channel)) {
            return false;
        }
        final Channel c = (Channel)o;
        if (o instanceof ServerChannel) {
            return this.serverChannels.contains(c);
        }
        return this.nonServerChannels.contains(c);
    }
    
    @Override
    public boolean add(final Channel channel) {
        final ConcurrentSet<Channel> set = (channel instanceof ServerChannel) ? this.serverChannels : this.nonServerChannels;
        final boolean added = set.add(channel);
        if (added) {
            channel.closeFuture().addListener((GenericFutureListener<? extends Future<? super Void>>)this.remover);
        }
        return added;
    }
    
    @Override
    public boolean remove(final Object o) {
        if (!(o instanceof Channel)) {
            return false;
        }
        final Channel c = (Channel)o;
        boolean removed;
        if (c instanceof ServerChannel) {
            removed = this.serverChannels.remove(c);
        }
        else {
            removed = this.nonServerChannels.remove(c);
        }
        if (!removed) {
            return false;
        }
        c.closeFuture().removeListener((GenericFutureListener<? extends Future<? super Void>>)this.remover);
        return true;
    }
    
    @Override
    public void clear() {
        this.nonServerChannels.clear();
        this.serverChannels.clear();
    }
    
    @Override
    public Iterator<Channel> iterator() {
        return new CombinedIterator<Channel>(this.serverChannels.iterator(), this.nonServerChannels.iterator());
    }
    
    @Override
    public Object[] toArray() {
        final Collection<Channel> channels = new ArrayList<Channel>(this.size());
        channels.addAll(this.serverChannels);
        channels.addAll(this.nonServerChannels);
        return channels.toArray();
    }
    
    @Override
    public <T> T[] toArray(final T[] a) {
        final Collection<Channel> channels = new ArrayList<Channel>(this.size());
        channels.addAll(this.serverChannels);
        channels.addAll(this.nonServerChannels);
        return channels.toArray(a);
    }
    
    @Override
    public ChannelGroupFuture close() {
        return this.close(ChannelMatchers.all());
    }
    
    @Override
    public ChannelGroupFuture disconnect() {
        return this.disconnect(ChannelMatchers.all());
    }
    
    @Override
    public ChannelGroupFuture deregister() {
        return this.deregister(ChannelMatchers.all());
    }
    
    @Override
    public ChannelGroupFuture write(final Object message) {
        return this.write(message, ChannelMatchers.all());
    }
    
    private static Object safeDuplicate(final Object message) {
        if (message instanceof ByteBuf) {
            return ((ByteBuf)message).duplicate().retain();
        }
        if (message instanceof ByteBufHolder) {
            return ((ByteBufHolder)message).duplicate().retain();
        }
        return ReferenceCountUtil.retain(message);
    }
    
    @Override
    public ChannelGroupFuture write(final Object message, final ChannelMatcher matcher) {
        if (message == null) {
            throw new NullPointerException("message");
        }
        if (matcher == null) {
            throw new NullPointerException("matcher");
        }
        final Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(this.size());
        for (final Channel c : this.nonServerChannels) {
            if (matcher.matches(c)) {
                futures.put(c, c.write(safeDuplicate(message)));
            }
        }
        ReferenceCountUtil.release(message);
        return new DefaultChannelGroupFuture(this, futures, this.executor);
    }
    
    @Override
    public ChannelGroup flush() {
        return this.flush(ChannelMatchers.all());
    }
    
    @Override
    public ChannelGroupFuture flushAndWrite(final Object message) {
        return this.writeAndFlush(message);
    }
    
    @Override
    public ChannelGroupFuture writeAndFlush(final Object message) {
        return this.writeAndFlush(message, ChannelMatchers.all());
    }
    
    @Override
    public ChannelGroupFuture disconnect(final ChannelMatcher matcher) {
        if (matcher == null) {
            throw new NullPointerException("matcher");
        }
        final Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(this.size());
        for (final Channel c : this.serverChannels) {
            if (matcher.matches(c)) {
                futures.put(c, c.disconnect());
            }
        }
        for (final Channel c : this.nonServerChannels) {
            if (matcher.matches(c)) {
                futures.put(c, c.disconnect());
            }
        }
        return new DefaultChannelGroupFuture(this, futures, this.executor);
    }
    
    @Override
    public ChannelGroupFuture close(final ChannelMatcher matcher) {
        if (matcher == null) {
            throw new NullPointerException("matcher");
        }
        final Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(this.size());
        for (final Channel c : this.serverChannels) {
            if (matcher.matches(c)) {
                futures.put(c, c.close());
            }
        }
        for (final Channel c : this.nonServerChannels) {
            if (matcher.matches(c)) {
                futures.put(c, c.close());
            }
        }
        return new DefaultChannelGroupFuture(this, futures, this.executor);
    }
    
    @Override
    public ChannelGroupFuture deregister(final ChannelMatcher matcher) {
        if (matcher == null) {
            throw new NullPointerException("matcher");
        }
        final Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(this.size());
        for (final Channel c : this.serverChannels) {
            if (matcher.matches(c)) {
                futures.put(c, c.deregister());
            }
        }
        for (final Channel c : this.nonServerChannels) {
            if (matcher.matches(c)) {
                futures.put(c, c.deregister());
            }
        }
        return new DefaultChannelGroupFuture(this, futures, this.executor);
    }
    
    @Override
    public ChannelGroup flush(final ChannelMatcher matcher) {
        for (final Channel c : this.nonServerChannels) {
            if (matcher.matches(c)) {
                c.flush();
            }
        }
        return this;
    }
    
    @Override
    public ChannelGroupFuture flushAndWrite(final Object message, final ChannelMatcher matcher) {
        return this.writeAndFlush(message, matcher);
    }
    
    @Override
    public ChannelGroupFuture writeAndFlush(final Object message, final ChannelMatcher matcher) {
        if (message == null) {
            throw new NullPointerException("message");
        }
        final Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(this.size());
        for (final Channel c : this.nonServerChannels) {
            if (matcher.matches(c)) {
                futures.put(c, c.writeAndFlush(safeDuplicate(message)));
            }
        }
        ReferenceCountUtil.release(message);
        return new DefaultChannelGroupFuture(this, futures, this.executor);
    }
    
    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o;
    }
    
    @Override
    public int compareTo(final ChannelGroup o) {
        final int v = this.name().compareTo(o.name());
        if (v != 0) {
            return v;
        }
        return System.identityHashCode(this) - System.identityHashCode(o);
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + "(name: " + this.name() + ", size: " + this.size() + ')';
    }
    
    static {
        nextId = new AtomicInteger();
    }
}
