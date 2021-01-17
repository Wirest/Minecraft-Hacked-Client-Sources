// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.CompleteFuture;

abstract class CompleteChannelFuture extends CompleteFuture<Void> implements ChannelFuture
{
    private final Channel channel;
    
    protected CompleteChannelFuture(final Channel channel, final EventExecutor executor) {
        super(executor);
        if (channel == null) {
            throw new NullPointerException("channel");
        }
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
    public ChannelFuture addListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        super.addListener(listener);
        return this;
    }
    
    @Override
    public ChannelFuture addListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.addListeners(listeners);
        return this;
    }
    
    @Override
    public ChannelFuture removeListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        super.removeListener(listener);
        return this;
    }
    
    @Override
    public ChannelFuture removeListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.removeListeners(listeners);
        return this;
    }
    
    @Override
    public ChannelFuture syncUninterruptibly() {
        return this;
    }
    
    @Override
    public ChannelFuture sync() throws InterruptedException {
        return this;
    }
    
    @Override
    public ChannelFuture await() throws InterruptedException {
        return this;
    }
    
    @Override
    public ChannelFuture awaitUninterruptibly() {
        return this;
    }
    
    @Override
    public Channel channel() {
        return this.channel;
    }
    
    @Override
    public Void getNow() {
        return null;
    }
}
