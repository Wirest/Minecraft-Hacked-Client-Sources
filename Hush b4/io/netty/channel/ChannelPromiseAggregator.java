// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import java.util.Iterator;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.LinkedHashSet;
import java.util.Set;

public final class ChannelPromiseAggregator implements ChannelFutureListener
{
    private final ChannelPromise aggregatePromise;
    private Set<ChannelPromise> pendingPromises;
    
    public ChannelPromiseAggregator(final ChannelPromise aggregatePromise) {
        if (aggregatePromise == null) {
            throw new NullPointerException("aggregatePromise");
        }
        this.aggregatePromise = aggregatePromise;
    }
    
    public ChannelPromiseAggregator add(final ChannelPromise... promises) {
        if (promises == null) {
            throw new NullPointerException("promises");
        }
        if (promises.length == 0) {
            return this;
        }
        synchronized (this) {
            if (this.pendingPromises == null) {
                int size;
                if (promises.length > 1) {
                    size = promises.length;
                }
                else {
                    size = 2;
                }
                this.pendingPromises = new LinkedHashSet<ChannelPromise>(size);
            }
            for (final ChannelPromise p : promises) {
                if (p != null) {
                    this.pendingPromises.add(p);
                    p.addListener((GenericFutureListener<? extends Future<? super Void>>)this);
                }
            }
        }
        return this;
    }
    
    @Override
    public synchronized void operationComplete(final ChannelFuture future) throws Exception {
        if (this.pendingPromises == null) {
            this.aggregatePromise.setSuccess();
        }
        else {
            this.pendingPromises.remove(future);
            if (!future.isSuccess()) {
                this.aggregatePromise.setFailure(future.cause());
                for (final ChannelPromise pendingFuture : this.pendingPromises) {
                    pendingFuture.setFailure(future.cause());
                }
            }
            else if (this.pendingPromises.isEmpty()) {
                this.aggregatePromise.setSuccess();
            }
        }
    }
}
