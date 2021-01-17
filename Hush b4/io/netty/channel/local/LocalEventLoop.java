// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.local;

import io.netty.channel.EventLoopGroup;
import java.util.concurrent.ThreadFactory;
import io.netty.channel.SingleThreadEventLoop;

final class LocalEventLoop extends SingleThreadEventLoop
{
    LocalEventLoop(final LocalEventLoopGroup parent, final ThreadFactory threadFactory) {
        super(parent, threadFactory, true);
    }
    
    @Override
    protected void run() {
        do {
            final Runnable task = this.takeTask();
            if (task != null) {
                task.run();
                this.updateLastExecutionTime();
            }
        } while (!this.confirmShutdown());
    }
}
