// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.traffic;

import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelPromise;
import java.util.Iterator;
import io.netty.buffer.ByteBuf;
import java.util.LinkedList;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.EventExecutor;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.List;
import java.util.Map;
import io.netty.channel.ChannelHandler;

@ChannelHandler.Sharable
public class GlobalTrafficShapingHandler extends AbstractTrafficShapingHandler
{
    private Map<Integer, List<ToSend>> messagesQueues;
    
    void createGlobalTrafficCounter(final ScheduledExecutorService executor) {
        if (executor == null) {
            throw new NullPointerException("executor");
        }
        final TrafficCounter tc = new TrafficCounter(this, executor, "GlobalTC", this.checkInterval);
        this.setTrafficCounter(tc);
        tc.start();
    }
    
    public GlobalTrafficShapingHandler(final ScheduledExecutorService executor, final long writeLimit, final long readLimit, final long checkInterval, final long maxTime) {
        super(writeLimit, readLimit, checkInterval, maxTime);
        this.messagesQueues = new HashMap<Integer, List<ToSend>>();
        this.createGlobalTrafficCounter(executor);
    }
    
    public GlobalTrafficShapingHandler(final ScheduledExecutorService executor, final long writeLimit, final long readLimit, final long checkInterval) {
        super(writeLimit, readLimit, checkInterval);
        this.messagesQueues = new HashMap<Integer, List<ToSend>>();
        this.createGlobalTrafficCounter(executor);
    }
    
    public GlobalTrafficShapingHandler(final ScheduledExecutorService executor, final long writeLimit, final long readLimit) {
        super(writeLimit, readLimit);
        this.messagesQueues = new HashMap<Integer, List<ToSend>>();
        this.createGlobalTrafficCounter(executor);
    }
    
    public GlobalTrafficShapingHandler(final ScheduledExecutorService executor, final long checkInterval) {
        super(checkInterval);
        this.messagesQueues = new HashMap<Integer, List<ToSend>>();
        this.createGlobalTrafficCounter(executor);
    }
    
    public GlobalTrafficShapingHandler(final EventExecutor executor) {
        this.messagesQueues = new HashMap<Integer, List<ToSend>>();
        this.createGlobalTrafficCounter(executor);
    }
    
    public final void release() {
        if (this.trafficCounter != null) {
            this.trafficCounter.stop();
        }
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        final Integer key = ctx.channel().hashCode();
        final List<ToSend> mq = new LinkedList<ToSend>();
        this.messagesQueues.put(key, mq);
    }
    
    @Override
    public synchronized void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        final Integer key = ctx.channel().hashCode();
        final List<ToSend> mq = this.messagesQueues.remove(key);
        if (mq != null) {
            for (final ToSend toSend : mq) {
                if (toSend.toSend instanceof ByteBuf) {
                    ((ByteBuf)toSend.toSend).release();
                }
            }
            mq.clear();
        }
    }
    
    @Override
    protected synchronized void submitWrite(final ChannelHandlerContext ctx, final Object msg, final long delay, final ChannelPromise promise) {
        final Integer key = ctx.channel().hashCode();
        List<ToSend> messagesQueue = this.messagesQueues.get(key);
        if (delay == 0L && (messagesQueue == null || messagesQueue.isEmpty())) {
            ctx.write(msg, promise);
            return;
        }
        final ToSend newToSend = new ToSend(delay, msg, promise);
        if (messagesQueue == null) {
            messagesQueue = new LinkedList<ToSend>();
            this.messagesQueues.put(key, messagesQueue);
        }
        messagesQueue.add(newToSend);
        final List<ToSend> mqfinal = messagesQueue;
        ctx.executor().schedule((Runnable)new Runnable() {
            @Override
            public void run() {
                GlobalTrafficShapingHandler.this.sendAllValid(ctx, mqfinal);
            }
        }, delay, TimeUnit.MILLISECONDS);
    }
    
    private synchronized void sendAllValid(final ChannelHandlerContext ctx, final List<ToSend> messagesQueue) {
        while (!messagesQueue.isEmpty()) {
            final ToSend newToSend = messagesQueue.remove(0);
            if (newToSend.date > System.currentTimeMillis()) {
                messagesQueue.add(0, newToSend);
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
