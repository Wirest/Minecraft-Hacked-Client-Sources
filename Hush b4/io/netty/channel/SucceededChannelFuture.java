// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

final class SucceededChannelFuture extends CompleteChannelFuture
{
    SucceededChannelFuture(final Channel channel, final EventExecutor executor) {
        super(channel, executor);
    }
    
    @Override
    public Throwable cause() {
        return null;
    }
    
    @Override
    public boolean isSuccess() {
        return true;
    }
}
