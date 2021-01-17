// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.group;

import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.BlockingOperationException;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.Collections;
import java.util.LinkedHashMap;
import io.netty.util.concurrent.Future;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import io.netty.util.concurrent.EventExecutor;
import java.util.Collection;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelFuture;
import io.netty.channel.Channel;
import java.util.Map;
import io.netty.util.concurrent.DefaultPromise;

final class DefaultChannelGroupFuture extends DefaultPromise<Void> implements ChannelGroupFuture
{
    private final ChannelGroup group;
    private final Map<Channel, ChannelFuture> futures;
    private int successCount;
    private int failureCount;
    private final ChannelFutureListener childListener;
    
    DefaultChannelGroupFuture(final ChannelGroup group, final Collection<ChannelFuture> futures, final EventExecutor executor) {
        super(executor);
        this.childListener = new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                final boolean success = future.isSuccess();
                final boolean callSetDone;
                synchronized (DefaultChannelGroupFuture.this) {
                    if (success) {
                        DefaultChannelGroupFuture.this.successCount++;
                    }
                    else {
                        DefaultChannelGroupFuture.this.failureCount++;
                    }
                    callSetDone = (DefaultChannelGroupFuture.this.successCount + DefaultChannelGroupFuture.this.failureCount == DefaultChannelGroupFuture.this.futures.size());
                    assert DefaultChannelGroupFuture.this.successCount + DefaultChannelGroupFuture.this.failureCount <= DefaultChannelGroupFuture.this.futures.size();
                }
                if (callSetDone) {
                    if (DefaultChannelGroupFuture.this.failureCount > 0) {
                        final List<Map.Entry<Channel, Throwable>> failed = new ArrayList<Map.Entry<Channel, Throwable>>(DefaultChannelGroupFuture.this.failureCount);
                        for (final ChannelFuture f : DefaultChannelGroupFuture.this.futures.values()) {
                            if (!f.isSuccess()) {
                                failed.add(new DefaultEntry<Channel, Throwable>(f.channel(), f.cause()));
                            }
                        }
                        DefaultChannelGroupFuture.this.setFailure0(new ChannelGroupException(failed));
                    }
                    else {
                        DefaultChannelGroupFuture.this.setSuccess0();
                    }
                }
            }
        };
        if (group == null) {
            throw new NullPointerException("group");
        }
        if (futures == null) {
            throw new NullPointerException("futures");
        }
        this.group = group;
        final Map<Channel, ChannelFuture> futureMap = new LinkedHashMap<Channel, ChannelFuture>();
        for (final ChannelFuture f : futures) {
            futureMap.put(f.channel(), f);
        }
        this.futures = Collections.unmodifiableMap((Map<? extends Channel, ? extends ChannelFuture>)futureMap);
        for (final ChannelFuture f : this.futures.values()) {
            f.addListener((GenericFutureListener<? extends Future<? super Void>>)this.childListener);
        }
        if (this.futures.isEmpty()) {
            this.setSuccess0();
        }
    }
    
    DefaultChannelGroupFuture(final ChannelGroup group, final Map<Channel, ChannelFuture> futures, final EventExecutor executor) {
        super(executor);
        this.childListener = new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                final boolean success = future.isSuccess();
                final boolean callSetDone;
                synchronized (DefaultChannelGroupFuture.this) {
                    if (success) {
                        DefaultChannelGroupFuture.this.successCount++;
                    }
                    else {
                        DefaultChannelGroupFuture.this.failureCount++;
                    }
                    callSetDone = (DefaultChannelGroupFuture.this.successCount + DefaultChannelGroupFuture.this.failureCount == DefaultChannelGroupFuture.this.futures.size());
                    assert DefaultChannelGroupFuture.this.successCount + DefaultChannelGroupFuture.this.failureCount <= DefaultChannelGroupFuture.this.futures.size();
                }
                if (callSetDone) {
                    if (DefaultChannelGroupFuture.this.failureCount > 0) {
                        final List<Map.Entry<Channel, Throwable>> failed = new ArrayList<Map.Entry<Channel, Throwable>>(DefaultChannelGroupFuture.this.failureCount);
                        for (final ChannelFuture f : DefaultChannelGroupFuture.this.futures.values()) {
                            if (!f.isSuccess()) {
                                failed.add(new DefaultEntry<Channel, Throwable>(f.channel(), f.cause()));
                            }
                        }
                        DefaultChannelGroupFuture.this.setFailure0(new ChannelGroupException(failed));
                    }
                    else {
                        DefaultChannelGroupFuture.this.setSuccess0();
                    }
                }
            }
        };
        this.group = group;
        this.futures = Collections.unmodifiableMap((Map<? extends Channel, ? extends ChannelFuture>)futures);
        for (final ChannelFuture f : this.futures.values()) {
            f.addListener((GenericFutureListener<? extends Future<? super Void>>)this.childListener);
        }
        if (this.futures.isEmpty()) {
            this.setSuccess0();
        }
    }
    
    @Override
    public ChannelGroup group() {
        return this.group;
    }
    
    @Override
    public ChannelFuture find(final Channel channel) {
        return this.futures.get(channel);
    }
    
    @Override
    public Iterator<ChannelFuture> iterator() {
        return this.futures.values().iterator();
    }
    
    @Override
    public synchronized boolean isPartialSuccess() {
        return this.successCount != 0 && this.successCount != this.futures.size();
    }
    
    @Override
    public synchronized boolean isPartialFailure() {
        return this.failureCount != 0 && this.failureCount != this.futures.size();
    }
    
    @Override
    public DefaultChannelGroupFuture addListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        super.addListener(listener);
        return this;
    }
    
    @Override
    public DefaultChannelGroupFuture addListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.addListeners(listeners);
        return this;
    }
    
    @Override
    public DefaultChannelGroupFuture removeListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        super.removeListener(listener);
        return this;
    }
    
    @Override
    public DefaultChannelGroupFuture removeListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.removeListeners(listeners);
        return this;
    }
    
    @Override
    public DefaultChannelGroupFuture await() throws InterruptedException {
        super.await();
        return this;
    }
    
    @Override
    public DefaultChannelGroupFuture awaitUninterruptibly() {
        super.awaitUninterruptibly();
        return this;
    }
    
    @Override
    public DefaultChannelGroupFuture syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }
    
    @Override
    public DefaultChannelGroupFuture sync() throws InterruptedException {
        super.sync();
        return this;
    }
    
    @Override
    public ChannelGroupException cause() {
        return (ChannelGroupException)super.cause();
    }
    
    private void setSuccess0() {
        super.setSuccess(null);
    }
    
    private void setFailure0(final ChannelGroupException cause) {
        super.setFailure(cause);
    }
    
    @Override
    public DefaultChannelGroupFuture setSuccess(final Void result) {
        throw new IllegalStateException();
    }
    
    @Override
    public boolean trySuccess(final Void result) {
        throw new IllegalStateException();
    }
    
    @Override
    public DefaultChannelGroupFuture setFailure(final Throwable cause) {
        throw new IllegalStateException();
    }
    
    @Override
    public boolean tryFailure(final Throwable cause) {
        throw new IllegalStateException();
    }
    
    @Override
    protected void checkDeadLock() {
        final EventExecutor e = this.executor();
        if (e != null && e != ImmediateEventExecutor.INSTANCE && e.inEventLoop()) {
            throw new BlockingOperationException();
        }
    }
    
    private static final class DefaultEntry<K, V> implements Map.Entry<K, V>
    {
        private final K key;
        private final V value;
        
        DefaultEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        @Override
        public V setValue(final V value) {
            throw new UnsupportedOperationException("read-only");
        }
    }
}
