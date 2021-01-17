// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.concurrent.EventExecutor;

final class FailedChannelFuture extends CompleteChannelFuture
{
    private final Throwable cause;
    
    FailedChannelFuture(final Channel channel, final EventExecutor executor, final Throwable cause) {
        super(channel, executor);
        if (cause == null) {
            throw new NullPointerException("cause");
        }
        this.cause = cause;
    }
    
    @Override
    public Throwable cause() {
        return this.cause;
    }
    
    @Override
    public boolean isSuccess() {
        return false;
    }
    
    @Override
    public ChannelFuture sync() {
        PlatformDependent.throwException(this.cause);
        return this;
    }
    
    @Override
    public ChannelFuture syncUninterruptibly() {
        PlatformDependent.throwException(this.cause);
        return this;
    }
}
