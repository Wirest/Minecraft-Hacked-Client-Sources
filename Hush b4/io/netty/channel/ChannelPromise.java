// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

public interface ChannelPromise extends ChannelFuture, Promise<Void>
{
    Channel channel();
    
    ChannelPromise setSuccess(final Void p0);
    
    ChannelPromise setSuccess();
    
    boolean trySuccess();
    
    ChannelPromise setFailure(final Throwable p0);
    
    ChannelPromise addListener(final GenericFutureListener<? extends Future<? super Void>> p0);
    
    ChannelPromise addListeners(final GenericFutureListener<? extends Future<? super Void>>... p0);
    
    ChannelPromise removeListener(final GenericFutureListener<? extends Future<? super Void>> p0);
    
    ChannelPromise removeListeners(final GenericFutureListener<? extends Future<? super Void>>... p0);
    
    ChannelPromise sync() throws InterruptedException;
    
    ChannelPromise syncUninterruptibly();
    
    ChannelPromise await() throws InterruptedException;
    
    ChannelPromise awaitUninterruptibly();
}
