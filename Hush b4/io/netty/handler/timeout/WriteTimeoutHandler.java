// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.timeout;

import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import java.util.concurrent.ScheduledFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public class WriteTimeoutHandler extends ChannelOutboundHandlerAdapter
{
    private static final long MIN_TIMEOUT_NANOS;
    private final long timeoutNanos;
    private boolean closed;
    
    public WriteTimeoutHandler(final int timeoutSeconds) {
        this(timeoutSeconds, TimeUnit.SECONDS);
    }
    
    public WriteTimeoutHandler(final long timeout, final TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (timeout <= 0L) {
            this.timeoutNanos = 0L;
        }
        else {
            this.timeoutNanos = Math.max(unit.toNanos(timeout), WriteTimeoutHandler.MIN_TIMEOUT_NANOS);
        }
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        this.scheduleTimeout(ctx, promise);
        ctx.write(msg, promise);
    }
    
    private void scheduleTimeout(final ChannelHandlerContext ctx, final ChannelPromise future) {
        if (this.timeoutNanos > 0L) {
            final ScheduledFuture<?> sf = ctx.executor().schedule((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (!future.isDone()) {
                        try {
                            WriteTimeoutHandler.this.writeTimedOut(ctx);
                        }
                        catch (Throwable t) {
                            ctx.fireExceptionCaught(t);
                        }
                    }
                }
            }, this.timeoutNanos, TimeUnit.NANOSECONDS);
            future.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                @Override
                public void operationComplete(final ChannelFuture future) throws Exception {
                    sf.cancel(false);
                }
            });
        }
    }
    
    protected void writeTimedOut(final ChannelHandlerContext ctx) throws Exception {
        if (!this.closed) {
            ctx.fireExceptionCaught(WriteTimeoutException.INSTANCE);
            ctx.close();
            this.closed = true;
        }
    }
    
    static {
        MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
    }
}
