// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.group;

import java.util.Iterator;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;

public interface ChannelGroupFuture extends Future<Void>, Iterable<ChannelFuture>
{
    ChannelGroup group();
    
    ChannelFuture find(final Channel p0);
    
    boolean isSuccess();
    
    ChannelGroupException cause();
    
    boolean isPartialSuccess();
    
    boolean isPartialFailure();
    
    ChannelGroupFuture addListener(final GenericFutureListener<? extends Future<? super Void>> p0);
    
    ChannelGroupFuture addListeners(final GenericFutureListener<? extends Future<? super Void>>... p0);
    
    ChannelGroupFuture removeListener(final GenericFutureListener<? extends Future<? super Void>> p0);
    
    ChannelGroupFuture removeListeners(final GenericFutureListener<? extends Future<? super Void>>... p0);
    
    ChannelGroupFuture await() throws InterruptedException;
    
    ChannelGroupFuture awaitUninterruptibly();
    
    ChannelGroupFuture syncUninterruptibly();
    
    ChannelGroupFuture sync() throws InterruptedException;
    
    Iterator<ChannelFuture> iterator();
}
