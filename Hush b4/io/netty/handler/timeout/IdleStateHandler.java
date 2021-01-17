// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.timeout;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;
import io.netty.channel.ChannelDuplexHandler;

public class IdleStateHandler extends ChannelDuplexHandler
{
    private static final long MIN_TIMEOUT_NANOS;
    private final long readerIdleTimeNanos;
    private final long writerIdleTimeNanos;
    private final long allIdleTimeNanos;
    volatile ScheduledFuture<?> readerIdleTimeout;
    volatile long lastReadTime;
    private boolean firstReaderIdleEvent;
    volatile ScheduledFuture<?> writerIdleTimeout;
    volatile long lastWriteTime;
    private boolean firstWriterIdleEvent;
    volatile ScheduledFuture<?> allIdleTimeout;
    private boolean firstAllIdleEvent;
    private volatile int state;
    
    public IdleStateHandler(final int readerIdleTimeSeconds, final int writerIdleTimeSeconds, final int allIdleTimeSeconds) {
        this(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds, TimeUnit.SECONDS);
    }
    
    public IdleStateHandler(final long readerIdleTime, final long writerIdleTime, final long allIdleTime, final TimeUnit unit) {
        this.firstReaderIdleEvent = true;
        this.firstWriterIdleEvent = true;
        this.firstAllIdleEvent = true;
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (readerIdleTime <= 0L) {
            this.readerIdleTimeNanos = 0L;
        }
        else {
            this.readerIdleTimeNanos = Math.max(unit.toNanos(readerIdleTime), IdleStateHandler.MIN_TIMEOUT_NANOS);
        }
        if (writerIdleTime <= 0L) {
            this.writerIdleTimeNanos = 0L;
        }
        else {
            this.writerIdleTimeNanos = Math.max(unit.toNanos(writerIdleTime), IdleStateHandler.MIN_TIMEOUT_NANOS);
        }
        if (allIdleTime <= 0L) {
            this.allIdleTimeNanos = 0L;
        }
        else {
            this.allIdleTimeNanos = Math.max(unit.toNanos(allIdleTime), IdleStateHandler.MIN_TIMEOUT_NANOS);
        }
    }
    
    public long getReaderIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(this.readerIdleTimeNanos);
    }
    
    public long getWriterIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(this.writerIdleTimeNanos);
    }
    
    public long getAllIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(this.allIdleTimeNanos);
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
        final boolean b = true;
        this.firstAllIdleEvent = b;
        this.firstReaderIdleEvent = b;
        ctx.fireChannelRead(msg);
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        promise.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                IdleStateHandler.this.lastWriteTime = System.nanoTime();
                IdleStateHandler.this.firstWriterIdleEvent = (IdleStateHandler.this.firstAllIdleEvent = true);
            }
        });
        ctx.write(msg, promise);
    }
    
    private void initialize(final ChannelHandlerContext ctx) {
        switch (this.state) {
            case 1:
            case 2: {}
            default: {
                this.state = 1;
                final EventExecutor loop = ctx.executor();
                final long nanoTime = System.nanoTime();
                this.lastWriteTime = nanoTime;
                this.lastReadTime = nanoTime;
                if (this.readerIdleTimeNanos > 0L) {
                    this.readerIdleTimeout = loop.schedule((Runnable)new ReaderIdleTimeoutTask(ctx), this.readerIdleTimeNanos, TimeUnit.NANOSECONDS);
                }
                if (this.writerIdleTimeNanos > 0L) {
                    this.writerIdleTimeout = loop.schedule((Runnable)new WriterIdleTimeoutTask(ctx), this.writerIdleTimeNanos, TimeUnit.NANOSECONDS);
                }
                if (this.allIdleTimeNanos > 0L) {
                    this.allIdleTimeout = loop.schedule((Runnable)new AllIdleTimeoutTask(ctx), this.allIdleTimeNanos, TimeUnit.NANOSECONDS);
                }
            }
        }
    }
    
    private void destroy() {
        this.state = 2;
        if (this.readerIdleTimeout != null) {
            this.readerIdleTimeout.cancel(false);
            this.readerIdleTimeout = null;
        }
        if (this.writerIdleTimeout != null) {
            this.writerIdleTimeout.cancel(false);
            this.writerIdleTimeout = null;
        }
        if (this.allIdleTimeout != null) {
            this.allIdleTimeout.cancel(false);
            this.allIdleTimeout = null;
        }
    }
    
    protected void channelIdle(final ChannelHandlerContext ctx, final IdleStateEvent evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }
    
    static {
        MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
    }
    
    private final class ReaderIdleTimeoutTask implements Runnable
    {
        private final ChannelHandlerContext ctx;
        
        ReaderIdleTimeoutTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }
        
        @Override
        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            final long currentTime = System.nanoTime();
            final long lastReadTime = IdleStateHandler.this.lastReadTime;
            final long nextDelay = IdleStateHandler.this.readerIdleTimeNanos - (currentTime - lastReadTime);
            if (nextDelay <= 0L) {
                IdleStateHandler.this.readerIdleTimeout = this.ctx.executor().schedule((Runnable)this, IdleStateHandler.this.readerIdleTimeNanos, TimeUnit.NANOSECONDS);
                try {
                    IdleStateEvent event;
                    if (IdleStateHandler.this.firstReaderIdleEvent) {
                        IdleStateHandler.this.firstReaderIdleEvent = false;
                        event = IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT;
                    }
                    else {
                        event = IdleStateEvent.READER_IDLE_STATE_EVENT;
                    }
                    IdleStateHandler.this.channelIdle(this.ctx, event);
                }
                catch (Throwable t) {
                    this.ctx.fireExceptionCaught(t);
                }
            }
            else {
                IdleStateHandler.this.readerIdleTimeout = this.ctx.executor().schedule((Runnable)this, nextDelay, TimeUnit.NANOSECONDS);
            }
        }
    }
    
    private final class WriterIdleTimeoutTask implements Runnable
    {
        private final ChannelHandlerContext ctx;
        
        WriterIdleTimeoutTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }
        
        @Override
        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            final long currentTime = System.nanoTime();
            final long lastWriteTime = IdleStateHandler.this.lastWriteTime;
            final long nextDelay = IdleStateHandler.this.writerIdleTimeNanos - (currentTime - lastWriteTime);
            if (nextDelay <= 0L) {
                IdleStateHandler.this.writerIdleTimeout = this.ctx.executor().schedule((Runnable)this, IdleStateHandler.this.writerIdleTimeNanos, TimeUnit.NANOSECONDS);
                try {
                    IdleStateEvent event;
                    if (IdleStateHandler.this.firstWriterIdleEvent) {
                        IdleStateHandler.this.firstWriterIdleEvent = false;
                        event = IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT;
                    }
                    else {
                        event = IdleStateEvent.WRITER_IDLE_STATE_EVENT;
                    }
                    IdleStateHandler.this.channelIdle(this.ctx, event);
                }
                catch (Throwable t) {
                    this.ctx.fireExceptionCaught(t);
                }
            }
            else {
                IdleStateHandler.this.writerIdleTimeout = this.ctx.executor().schedule((Runnable)this, nextDelay, TimeUnit.NANOSECONDS);
            }
        }
    }
    
    private final class AllIdleTimeoutTask implements Runnable
    {
        private final ChannelHandlerContext ctx;
        
        AllIdleTimeoutTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }
        
        @Override
        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            final long currentTime = System.nanoTime();
            final long lastIoTime = Math.max(IdleStateHandler.this.lastReadTime, IdleStateHandler.this.lastWriteTime);
            final long nextDelay = IdleStateHandler.this.allIdleTimeNanos - (currentTime - lastIoTime);
            if (nextDelay <= 0L) {
                IdleStateHandler.this.allIdleTimeout = this.ctx.executor().schedule((Runnable)this, IdleStateHandler.this.allIdleTimeNanos, TimeUnit.NANOSECONDS);
                try {
                    IdleStateEvent event;
                    if (IdleStateHandler.this.firstAllIdleEvent) {
                        IdleStateHandler.this.firstAllIdleEvent = false;
                        event = IdleStateEvent.FIRST_ALL_IDLE_STATE_EVENT;
                    }
                    else {
                        event = IdleStateEvent.ALL_IDLE_STATE_EVENT;
                    }
                    IdleStateHandler.this.channelIdle(this.ctx, event);
                }
                catch (Throwable t) {
                    this.ctx.fireExceptionCaught(t);
                }
            }
            else {
                IdleStateHandler.this.allIdleTimeout = this.ctx.executor().schedule((Runnable)this, nextDelay, TimeUnit.NANOSECONDS);
            }
        }
    }
}
