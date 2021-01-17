// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;

public class ThreadPerChannelEventLoop extends SingleThreadEventLoop
{
    private final ThreadPerChannelEventLoopGroup parent;
    private Channel ch;
    
    public ThreadPerChannelEventLoop(final ThreadPerChannelEventLoopGroup parent) {
        super(parent, parent.threadFactory, true);
        this.parent = parent;
    }
    
    @Override
    public ChannelFuture register(final Channel channel, final ChannelPromise promise) {
        return super.register(channel, promise).addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    ThreadPerChannelEventLoop.this.ch = future.channel();
                }
                else {
                    ThreadPerChannelEventLoop.this.deregister();
                }
            }
        });
    }
    
    @Override
    protected void run() {
        while (true) {
            final Runnable task = this.takeTask();
            if (task != null) {
                task.run();
                this.updateLastExecutionTime();
            }
            final Channel ch = this.ch;
            if (this.isShuttingDown()) {
                if (ch != null) {
                    ch.unsafe().close(ch.unsafe().voidPromise());
                }
                if (this.confirmShutdown()) {
                    break;
                }
                continue;
            }
            else {
                if (ch == null || ch.isRegistered()) {
                    continue;
                }
                this.runAllTasks();
                this.deregister();
            }
        }
    }
    
    protected void deregister() {
        this.ch = null;
        this.parent.activeChildren.remove(this);
        this.parent.idleChildren.add(this);
    }
}
