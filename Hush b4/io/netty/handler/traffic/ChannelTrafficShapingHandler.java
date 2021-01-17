// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.traffic;

import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelPromise;
import java.util.Iterator;
import io.netty.buffer.ByteBuf;
import java.util.concurrent.ScheduledExecutorService;
import io.netty.channel.ChannelHandlerContext;
import java.util.LinkedList;
import java.util.List;

public class ChannelTrafficShapingHandler extends AbstractTrafficShapingHandler
{
    private List<ToSend> messagesQueue;
    
    public ChannelTrafficShapingHandler(final long writeLimit, final long readLimit, final long checkInterval, final long maxTime) {
        super(writeLimit, readLimit, checkInterval, maxTime);
        this.messagesQueue = new LinkedList<ToSend>();
    }
    
    public ChannelTrafficShapingHandler(final long writeLimit, final long readLimit, final long checkInterval) {
        super(writeLimit, readLimit, checkInterval);
        this.messagesQueue = new LinkedList<ToSend>();
    }
    
    public ChannelTrafficShapingHandler(final long writeLimit, final long readLimit) {
        super(writeLimit, readLimit);
        this.messagesQueue = new LinkedList<ToSend>();
    }
    
    public ChannelTrafficShapingHandler(final long checkInterval) {
        super(checkInterval);
        this.messagesQueue = new LinkedList<ToSend>();
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        final TrafficCounter trafficCounter = new TrafficCounter(this, ctx.executor(), "ChannelTC" + ctx.channel().hashCode(), this.checkInterval);
        this.setTrafficCounter(trafficCounter);
        trafficCounter.start();
    }
    
    @Override
    public synchronized void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        if (this.trafficCounter != null) {
            this.trafficCounter.stop();
        }
        for (final ToSend toSend : this.messagesQueue) {
            if (toSend.toSend instanceof ByteBuf) {
                ((ByteBuf)toSend.toSend).release();
            }
        }
        this.messagesQueue.clear();
    }
    
    @Override
    protected synchronized void submitWrite(final ChannelHandlerContext ctx, final Object msg, final long delay, final ChannelPromise promise) {
        if (delay == 0L && this.messagesQueue.isEmpty()) {
            ctx.write(msg, promise);
            return;
        }
        final ToSend newToSend = new ToSend(delay, msg, promise);
        this.messagesQueue.add(newToSend);
        ctx.executor().schedule((Runnable)new Runnable() {
            @Override
            public void run() {
                ChannelTrafficShapingHandler.this.sendAllValid(ctx);
            }
        }, delay, TimeUnit.MILLISECONDS);
    }
    
    private synchronized void sendAllValid(final ChannelHandlerContext ctx) {
        while (!this.messagesQueue.isEmpty()) {
            final ToSend newToSend = this.messagesQueue.remove(0);
            if (newToSend.date > System.currentTimeMillis()) {
                this.messagesQueue.add(0, newToSend);
                break;
            }
            ctx.write(newToSend.toSend, newToSend.promise);
        }
        ctx.flush();
    }
    
    private static final class ToSend
    {
        final long date;
        final Object toSend;
        final ChannelPromise promise;
        
        private ToSend(final long delay, final Object toSend, final ChannelPromise promise) {
            this.date = System.currentTimeMillis() + delay;
            this.toSend = toSend;
            this.promise = promise;
        }
    }
}
