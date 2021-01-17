// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.timeout;

import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ReadTimeoutHandler extends ChannelInboundHandlerAdapter
{
    private static final long MIN_TIMEOUT_NANOS;
    private final long timeoutNanos;
    private volatile ScheduledFuture<?> timeout;
    private volatile long lastReadTime;
    private volatile int state;
    private boolean closed;
    
    public ReadTimeoutHandler(final int timeoutSeconds) {
        this(timeoutSeconds, TimeUnit.SECONDS);
    }
    
    public ReadTimeoutHandler(final long timeout, final TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (timeout <= 0L) {
            this.timeoutNanos = 0L;
        }
        else {
            this.timeoutNanos = Math.max(unit.toNanos(timeout), ReadTimeoutHandler.MIN_TIMEOUT_NANOS);
        }
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isActive() && ctx.channel().isRegistered()) {
            this.initialize(ctx);
        }
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        this.destroy();
    }
    
    @Override
    public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isActive()) {
            this.initialize(ctx);
        }
        super.channelRegistered(ctx);
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        this.initialize(ctx);
        super.channelActive(ctx);
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.destroy();
        super.channelInactive(ctx);
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        this.lastReadTime = System.nanoTime();
        ctx.fireChannelRead(msg);
    }
    
    private void initialize(final ChannelHandlerContext ctx) {
        switch (this.state) {
            case 1:
            case 2: {}
            default: {
                this.state = 1;
                this.lastReadTime = System.nanoTime();
                if (this.timeoutNanos > 0L) {
                    this.timeout = ctx.executor().schedule((Runnable)new ReadTimeoutTask(ctx), this.timeoutNanos, TimeUnit.NANOSECONDS);
                }
            }
        }
    }
    
    private void destroy() {
        this.state = 2;
        if (this.timeout != null) {
            this.timeout.cancel(false);
            this.timeout = null;
        }
    }
    
    protected void readTimedOut(final ChannelHandlerContext ctx) throws Exception {
        if (!this.closed) {
            ctx.fireExceptionCaught(ReadTimeoutException.INSTANCE);
            ctx.close();
            this.closed = true;
        }
    }
    
    static {
        MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
    }
    
    private final class ReadTimeoutTask implements Runnable
    {
        private final ChannelHandlerContext ctx;
        
        ReadTimeoutTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }
        
        @Override
        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            final long currentTime = System.nanoTime();
            final long nextDelay = ReadTimeoutHandler.this.timeoutNanos - (currentTime - ReadTimeoutHandler.this.lastReadTime);
            if (nextDelay <= 0L) {
                ReadTimeoutHandler.this.timeout = this.ctx.executor().schedule((Runnable)this, ReadTimeoutHandler.this.timeoutNanos, TimeUnit.NANOSECONDS);
                try {
                    ReadTimeoutHandler.this.readTimedOut(this.ctx);
                }
                catch (Throwable t) {
                    this.ctx.fireExceptionCaught(t);
                }
            }
            else {
                ReadTimeoutHandler.this.timeout = this.ctx.executor().schedule((Runnable)this, nextDelay, TimeUnit.NANOSECONDS);
            }
        }
    }
}
