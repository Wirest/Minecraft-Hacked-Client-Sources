// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.traffic;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPromise;
import io.netty.util.Attribute;
import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelDuplexHandler;

public abstract class AbstractTrafficShapingHandler extends ChannelDuplexHandler
{
    private static final InternalLogger logger;
    public static final long DEFAULT_CHECK_INTERVAL = 1000L;
    public static final long DEFAULT_MAX_TIME = 15000L;
    static final long MINIMAL_WAIT = 10L;
    protected TrafficCounter trafficCounter;
    private long writeLimit;
    private long readLimit;
    protected long maxTime;
    protected long checkInterval;
    private static final AttributeKey<Boolean> READ_SUSPENDED;
    private static final AttributeKey<Runnable> REOPEN_TASK;
    
    void setTrafficCounter(final TrafficCounter newTrafficCounter) {
        this.trafficCounter = newTrafficCounter;
    }
    
    protected AbstractTrafficShapingHandler(final long writeLimit, final long readLimit, final long checkInterval, final long maxTime) {
        this.maxTime = 15000L;
        this.checkInterval = 1000L;
        this.writeLimit = writeLimit;
        this.readLimit = readLimit;
        this.checkInterval = checkInterval;
        this.maxTime = maxTime;
    }
    
    protected AbstractTrafficShapingHandler(final long writeLimit, final long readLimit, final long checkInterval) {
        this(writeLimit, readLimit, checkInterval, 15000L);
    }
    
    protected AbstractTrafficShapingHandler(final long writeLimit, final long readLimit) {
        this(writeLimit, readLimit, 1000L, 15000L);
    }
    
    protected AbstractTrafficShapingHandler() {
        this(0L, 0L, 1000L, 15000L);
    }
    
    protected AbstractTrafficShapingHandler(final long checkInterval) {
        this(0L, 0L, checkInterval, 15000L);
    }
    
    public void configure(final long newWriteLimit, final long newReadLimit, final long newCheckInterval) {
        this.configure(newWriteLimit, newReadLimit);
        this.configure(newCheckInterval);
    }
    
    public void configure(final long newWriteLimit, final long newReadLimit) {
        this.writeLimit = newWriteLimit;
        this.readLimit = newReadLimit;
        if (this.trafficCounter != null) {
            this.trafficCounter.resetAccounting(System.currentTimeMillis() + 1L);
        }
    }
    
    public void configure(final long newCheckInterval) {
        this.checkInterval = newCheckInterval;
        if (this.trafficCounter != null) {
            this.trafficCounter.configure(this.checkInterval);
        }
    }
    
    public long getWriteLimit() {
        return this.writeLimit;
    }
    
    public void setWriteLimit(final long writeLimit) {
        this.writeLimit = writeLimit;
        if (this.trafficCounter != null) {
            this.trafficCounter.resetAccounting(System.currentTimeMillis() + 1L);
        }
    }
    
    public long getReadLimit() {
        return this.readLimit;
    }
    
    public void setReadLimit(final long readLimit) {
        this.readLimit = readLimit;
        if (this.trafficCounter != null) {
            this.trafficCounter.resetAccounting(System.currentTimeMillis() + 1L);
        }
    }
    
    public long getCheckInterval() {
        return this.checkInterval;
    }
    
    public void setCheckInterval(final long checkInterval) {
        this.checkInterval = checkInterval;
        if (this.trafficCounter != null) {
            this.trafficCounter.configure(checkInterval);
        }
    }
    
    public void setMaxTimeWait(final long maxTime) {
        this.maxTime = maxTime;
    }
    
    public long getMaxTimeWait() {
        return this.maxTime;
    }
    
    protected void doAccounting(final TrafficCounter counter) {
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        final long size = this.calculateSize(msg);
        if (size > 0L && this.trafficCounter != null) {
            final long wait = this.trafficCounter.readTimeToWait(size, this.readLimit, this.maxTime);
            if (wait >= 10L) {
                if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                    AbstractTrafficShapingHandler.logger.debug("Channel:" + ctx.channel().hashCode() + " Read Suspend: " + wait + ":" + ctx.channel().config().isAutoRead() + ":" + isHandlerActive(ctx));
                }
                if (ctx.channel().config().isAutoRead() && isHandlerActive(ctx)) {
                    ctx.channel().config().setAutoRead(false);
                    ctx.attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(true);
                    final Attribute<Runnable> attr = ctx.attr(AbstractTrafficShapingHandler.REOPEN_TASK);
                    Runnable reopenTask = attr.get();
                    if (reopenTask == null) {
                        reopenTask = new ReopenReadTimerTask(ctx);
                        attr.set(reopenTask);
                    }
                    ctx.executor().schedule(reopenTask, wait, TimeUnit.MILLISECONDS);
                    if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                        AbstractTrafficShapingHandler.logger.debug("Channel:" + ctx.channel().hashCode() + " Suspend final status => " + ctx.channel().config().isAutoRead() + ":" + isHandlerActive(ctx) + " will reopened at: " + wait);
                    }
                }
            }
        }
        ctx.fireChannelRead(msg);
    }
    
    protected static boolean isHandlerActive(final ChannelHandlerContext ctx) {
        final Boolean suspended = ctx.attr(AbstractTrafficShapingHandler.READ_SUSPENDED).get();
        return suspended == null || Boolean.FALSE.equals(suspended);
    }
    
    @Override
    public void read(final ChannelHandlerContext ctx) {
        if (isHandlerActive(ctx)) {
            ctx.read();
        }
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        final long size = this.calculateSize(msg);
        if (size > 0L && this.trafficCounter != null) {
            final long wait = this.trafficCounter.writeTimeToWait(size, this.writeLimit, this.maxTime);
            if (wait >= 10L) {
                if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                    AbstractTrafficShapingHandler.logger.debug("Channel:" + ctx.channel().hashCode() + " Write suspend: " + wait + ":" + ctx.channel().config().isAutoRead() + ":" + isHandlerActive(ctx));
                }
                this.submitWrite(ctx, msg, wait, promise);
                return;
            }
        }
        this.submitWrite(ctx, msg, 0L, promise);
    }
    
    protected abstract void submitWrite(final ChannelHandlerContext p0, final Object p1, final long p2, final ChannelPromise p3);
    
    public TrafficCounter trafficCounter() {
        return this.trafficCounter;
    }
    
    @Override
    public String toString() {
        return "TrafficShaping with Write Limit: " + this.writeLimit + " Read Limit: " + this.readLimit + " and Counter: " + ((this.trafficCounter != null) ? this.trafficCounter.toString() : "none");
    }
    
    protected long calculateSize(final Object msg) {
        if (msg instanceof ByteBuf) {
            return ((ByteBuf)msg).readableBytes();
        }
        if (msg instanceof ByteBufHolder) {
            return ((ByteBufHolder)msg).content().readableBytes();
        }
        return -1L;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AbstractTrafficShapingHandler.class);
        READ_SUSPENDED = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".READ_SUSPENDED");
        REOPEN_TASK = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".REOPEN_TASK");
    }
    
    private static final class ReopenReadTimerTask implements Runnable
    {
        final ChannelHandlerContext ctx;
        
        ReopenReadTimerTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }
        
        @Override
        public void run() {
            if (!this.ctx.channel().config().isAutoRead() && AbstractTrafficShapingHandler.isHandlerActive(this.ctx)) {
                if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                    AbstractTrafficShapingHandler.logger.debug("Channel:" + this.ctx.channel().hashCode() + " Not Unsuspend: " + this.ctx.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
                }
                this.ctx.attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(false);
            }
            else {
                if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                    if (this.ctx.channel().config().isAutoRead() && !AbstractTrafficShapingHandler.isHandlerActive(this.ctx)) {
                        AbstractTrafficShapingHandler.logger.debug("Channel:" + this.ctx.channel().hashCode() + " Unsuspend: " + this.ctx.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
                    }
                    else {
                        AbstractTrafficShapingHandler.logger.debug("Channel:" + this.ctx.channel().hashCode() + " Normal Unsuspend: " + this.ctx.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
                    }
                }
                this.ctx.attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(false);
                this.ctx.channel().config().setAutoRead(true);
                this.ctx.channel().read();
            }
            if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                AbstractTrafficShapingHandler.logger.debug("Channel:" + this.ctx.channel().hashCode() + " Unsupsend final status => " + this.ctx.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
            }
        }
    }
}
