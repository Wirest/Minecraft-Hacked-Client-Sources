package io.netty.handler.timeout;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ReadTimeoutHandler
        extends ChannelInboundHandlerAdapter {
    private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
    private final long timeoutNanos;
    private volatile ScheduledFuture<?> timeout;
    private volatile long lastReadTime;
    private volatile int state;
    private boolean closed;

    public ReadTimeoutHandler(int paramInt) {
        this(paramInt, TimeUnit.SECONDS);
    }

    public ReadTimeoutHandler(long paramLong, TimeUnit paramTimeUnit) {
        if (paramTimeUnit == null) {
            throw new NullPointerException("unit");
        }
        if (paramLong <= 0L) {
            this.timeoutNanos = 0L;
        } else {
            this.timeoutNanos = Math.max(paramTimeUnit.toNanos(paramLong), MIN_TIMEOUT_NANOS);
        }
    }

    public void handlerAdded(ChannelHandlerContext paramChannelHandlerContext)
            throws Exception {
        if ((paramChannelHandlerContext.channel().isActive()) && (paramChannelHandlerContext.channel().isRegistered())) {
            initialize(paramChannelHandlerContext);
        }
    }

    public void handlerRemoved(ChannelHandlerContext paramChannelHandlerContext)
            throws Exception {
        destroy();
    }

    public void channelRegistered(ChannelHandlerContext paramChannelHandlerContext)
            throws Exception {
        if (paramChannelHandlerContext.channel().isActive()) {
            initialize(paramChannelHandlerContext);
        }
        super.channelRegistered(paramChannelHandlerContext);
    }

    public void channelActive(ChannelHandlerContext paramChannelHandlerContext)
            throws Exception {
        initialize(paramChannelHandlerContext);
        super.channelActive(paramChannelHandlerContext);
    }

    public void channelInactive(ChannelHandlerContext paramChannelHandlerContext)
            throws Exception {
        destroy();
        super.channelInactive(paramChannelHandlerContext);
    }

    public void channelRead(ChannelHandlerContext paramChannelHandlerContext, Object paramObject)
            throws Exception {
        this.lastReadTime = System.nanoTime();
        paramChannelHandlerContext.fireChannelRead(paramObject);
    }

    private void initialize(ChannelHandlerContext paramChannelHandlerContext) {
        switch (this.state) {
            case 1:
            case 2:
                return;
        }
        this.state = 1;
        this.lastReadTime = System.nanoTime();
        if (this.timeoutNanos > 0L) {
            this.timeout = paramChannelHandlerContext.executor().schedule(new ReadTimeoutTask(paramChannelHandlerContext), this.timeoutNanos, TimeUnit.NANOSECONDS);
        }
    }

    private void destroy() {
        this.state = 2;
        if (this.timeout != null) {
            this.timeout.cancel(false);
            this.timeout = null;
        }
    }

    protected void readTimedOut(ChannelHandlerContext paramChannelHandlerContext)
            throws Exception {
        if (!this.closed) {
            paramChannelHandlerContext.fireExceptionCaught(ReadTimeoutException.INSTANCE);
            paramChannelHandlerContext.close();
            this.closed = true;
        }
    }

    private final class ReadTimeoutTask
            implements Runnable {
        private final ChannelHandlerContext ctx;

        ReadTimeoutTask(ChannelHandlerContext paramChannelHandlerContext) {
            this.ctx = paramChannelHandlerContext;
        }

        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            long l1 = System.nanoTime();
            long l2 = ReadTimeoutHandler.this.timeoutNanos - (l1 - ReadTimeoutHandler.this.lastReadTime);
            if (l2 <= 0L) {
                ReadTimeoutHandler.this.timeout = this.ctx.executor().schedule(this, ReadTimeoutHandler.this.timeoutNanos, TimeUnit.NANOSECONDS);
                try {
                    ReadTimeoutHandler.this.readTimedOut(this.ctx);
                } catch (Throwable localThrowable) {
                    this.ctx.fireExceptionCaught(localThrowable);
                }
            } else {
                ReadTimeoutHandler.this.timeout = this.ctx.executor().schedule(this, l2, TimeUnit.NANOSECONDS);
            }
        }
    }
}




