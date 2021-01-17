// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public interface ChannelFutureListener extends GenericFutureListener<ChannelFuture>
{
    public static final ChannelFutureListener CLOSE = new ChannelFutureListener() {
        @Override
        public void operationComplete(final ChannelFuture future) {
            future.channel().close();
        }
    };
    public static final ChannelFutureListener CLOSE_ON_FAILURE = new ChannelFutureListener() {
        @Override
        public void operationComplete(final ChannelFuture future) {
            if (!future.isSuccess()) {
                future.channel().close();
            }
        }
    };
    public static final ChannelFutureListener FIRE_EXCEPTION_ON_FAILURE = new ChannelFutureListener() {
        @Override
        public void operationComplete(final ChannelFuture future) {
            if (!future.isSuccess()) {
                future.channel().pipeline().fireExceptionCaught(future.cause());
            }
        }
    };
}
