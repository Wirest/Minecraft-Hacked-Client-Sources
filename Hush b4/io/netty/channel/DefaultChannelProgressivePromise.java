// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.ProgressiveFuture;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.ProgressivePromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.DefaultProgressivePromise;

public class DefaultChannelProgressivePromise extends DefaultProgressivePromise<Void> implements ChannelProgressivePromise, ChannelFlushPromiseNotifier.FlushCheckpoint
{
    private final Channel channel;
    private long checkpoint;
    
    public DefaultChannelProgressivePromise(final Channel channel) {
        this.channel = channel;
    }
    
    public DefaultChannelProgressivePromise(final Channel channel, final EventExecutor executor) {
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
    public ChannelProgressivePromise setSuccess() {
        return this.setSuccess(null);
    }
    
    @Override
    public ChannelProgressivePromise setSuccess(final Void result) {
        super.setSuccess(result);
        return this;
    }
    
    @Override
    public boolean trySuccess() {
        return this.trySuccess(null);
    }
    
    @Override
    public ChannelProgressivePromise setFailure(final Throwable cause) {
        super.setFailure(cause);
        return this;
    }
    
    @Override
    public ChannelProgressivePromise setProgress(final long progress, final long total) {
        super.setProgress(progress, total);
        return this;
    }
    
    @Override
    public ChannelProgressivePromise addListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        super.addListener(listener);
        return this;
    }
    
    @Override
    public ChannelProgressivePromise addListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.addListeners(listeners);
        return this;
    }
    
    @Override
    public ChannelProgressivePromise removeListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        super.removeListener(listener);
        return this;
    }
    
    @Override
    public ChannelProgressivePromise removeListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.removeListeners(listeners);
        return this;
    }
    
    @Override
    public ChannelProgressivePromise sync() throws InterruptedException {
        super.sync();
        return this;
    }
    
    @Override
    public ChannelProgressivePromise syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }
    
    @Override
    public ChannelProgressivePromise await() throws InterruptedException {
        super.await();
        return this;
    }
    
    @Override
    public ChannelProgressivePromise awaitUninterruptibly() {
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
    public ChannelProgressivePromise promise() {
        return this;
    }
    
    @Override
    protected void checkDeadLock() {
        if (this.channel().isRegistered()) {
            super.checkDeadLock();
        }
    }
}
