package io.netty.channel.local;

import io.netty.channel.SingleThreadEventLoop;

import java.util.concurrent.ThreadFactory;

final class LocalEventLoop
        extends SingleThreadEventLoop {
    LocalEventLoop(LocalEventLoopGroup paramLocalEventLoopGroup, ThreadFactory paramThreadFactory) {
        super(paramLocalEventLoopGroup, paramThreadFactory, true);
    }

    protected void run() {
        for (; ; ) {
            Runnable localRunnable = takeTask();
            if (localRunnable != null) {
                localRunnable.run();
                updateLastExecutionTime();
            }
            if (confirmShutdown()) {
                break;
            }
        }
    }
}




