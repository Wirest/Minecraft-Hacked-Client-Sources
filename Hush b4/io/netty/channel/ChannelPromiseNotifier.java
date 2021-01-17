// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.Future;

public final class ChannelPromiseNotifier implements ChannelFutureListener
{
    private final ChannelPromise[] promises;
    
    public ChannelPromiseNotifier(final ChannelPromise... promises) {
        if (promises == null) {
            throw new NullPointerException("promises");
        }
        for (final ChannelPromise promise : promises) {
            if (promise == null) {
                throw new IllegalArgumentException("promises contains null ChannelPromise");
            }
        }
        this.promises = promises.clone();
    }
    
    @Override
    public void operationComplete(final ChannelFuture cf) throws Exception {
        if (cf.isSuccess()) {
            for (final ChannelPromise p : this.promises) {
                p.setSuccess();
            }
            return;
        }
        final Throwable cause = cf.cause();
        for (final ChannelPromise p2 : this.promises) {
            p2.setFailure(cause);
        }
    }
}
