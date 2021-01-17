// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;

public interface ChannelFuture extends Future<Void>
{
    Channel channel();
    
    ChannelFuture addListener(final GenericFutureListener<? extends Future<? super Void>> p0);
    
    ChannelFuture addListeners(final GenericFutureListener<? extends Future<? super Void>>... p0);
    
    ChannelFuture removeListener(final GenericFutureListener<? extends Future<? super Void>> p0);
    
    ChannelFuture removeListeners(final GenericFutureListener<? extends Future<? super Void>>... p0);
    
    ChannelFuture sync() throws InterruptedException;
    
    ChannelFuture syncUninterruptibly();
    
    ChannelFuture await() throws InterruptedException;
    
    ChannelFuture awaitUninterruptibly();
}
