// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ProgressiveFuture;

public interface ChannelProgressiveFuture extends ChannelFuture, ProgressiveFuture<Void>
{
    ChannelProgressiveFuture addListener(final GenericFutureListener<? extends Future<? super Void>> p0);
    
    ChannelProgressiveFuture addListeners(final GenericFutureListener<? extends Future<? super Void>>... p0);
    
    ChannelProgressiveFuture removeListener(final GenericFutureListener<? extends Future<? super Void>> p0);
    
    ChannelProgressiveFuture removeListeners(final GenericFutureListener<? extends Future<? super Void>>... p0);
    
    ChannelProgressiveFuture sync() throws InterruptedException;
    
    ChannelProgressiveFuture syncUninterruptibly();
    
    ChannelProgressiveFuture await() throws InterruptedException;
    
    ChannelProgressiveFuture awaitUninterruptibly();
}
