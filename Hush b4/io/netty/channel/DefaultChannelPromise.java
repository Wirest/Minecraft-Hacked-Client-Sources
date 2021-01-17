// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.DefaultPromise;

public class DefaultChannelPromise extends DefaultPromise<Void> implements ChannelPromise, ChannelFlushPromiseNotifier.FlushCheckpoint
{
    private final Channel channel;
    private long checkpoint;
    
    public DefaultChannelPromise(final Channel channel) {
        this.channel = channel;
    }
    
    public DefaultChannelPromise(final Channel channel, final EventExecutor executor) {
        super(executor);
        this.channel = channel;
    }
    
    @Override
    protected EventExecutor executor() {
        final EventExecutor e = super.executor();
        if (e == null) {
            return this.channel().eventLoop();
        }
        return e;
    }
    
    @Override
    public Channel channel() {
        return this.channel;
    }
    
    @Override
    public ChannelPromise setSuccess() {
        return this.setSuccess(null);
    }
    
    @Override
    public ChannelPromise setSuccess(final Void result) {
        super.setSuccess(result);
        return this;
    }
    
    @Override
    public boolean trySuccess() {
        return this.trySuccess(null);
    }
    
    @Override
    public ChannelPromise setFailure(final Throwable cause) {
        super.setFailure(cause);
        return this;
    }
    
    @Override
    public ChannelPromise addListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        super.addListener(listener);
        return this;
    }
    
    @Override
    public ChannelPromise addListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.addListeners(listeners);
        return this;
    }
    
    @Override
    public ChannelPromise removeListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        super.removeListener(listener);
        return this;
    }
    
    @Override
    public ChannelPromise removeListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.removeListeners(listeners);
        return this;
    }
    
    @Override
    public ChannelPromise sync() throws InterruptedException {
        super.sync();
        return this;
    }
    
    @Override
    public ChannelPromise syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }
    
    @Override
    public ChannelPromise await() throws InterruptedException {
        super.await();
        return this;
    }
    
    @Override
    public ChannelPromise awaitUninterruptibly() {
        super.awaitUninterruptibly();
        return this;
    }
    
    @Override
    public long flushCheckpoint() {
        return this.checkpoint;
    }
    
    @Override
    public void flushCheckpoint(final long checkpoint) {
        this.checkpoint = checkpoint;
    }
    
    @Override
    public ChannelPromise promise() {
        return this;
    }
    
    @Override
    protected void checkDeadLock() {
        if (this.channel().isRegistered()) {
            super.checkDeadLock();
        }
    }
}
