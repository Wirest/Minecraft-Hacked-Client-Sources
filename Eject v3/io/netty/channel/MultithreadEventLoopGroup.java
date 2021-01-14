package io.netty.channel;

import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.concurrent.ThreadFactory;

public abstract class MultithreadEventLoopGroup
        extends MultithreadEventExecutorGroup
        implements EventLoopGroup {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(MultithreadEventLoopGroup.class);
    private static final int DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2));

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("-Dio.netty.eventLoopThreads: {}", Integer.valueOf(DEFAULT_EVENT_LOOP_THREADS));
        }
    }

    protected MultithreadEventLoopGroup(int paramInt, ThreadFactory paramThreadFactory, Object... paramVarArgs) {
        super(paramInt == 0 ? DEFAULT_EVENT_LOOP_THREADS : paramInt, paramThreadFactory, paramVarArgs);
    }

    protected ThreadFactory newDefaultThreadFactory() {
        return new DefaultThreadFactory(getClass(), 10);
    }

    public EventLoop next() {
        return (EventLoop) super.next();
    }

    public ChannelFuture register(Channel paramChannel) {
        return next().register(paramChannel);
    }

    public ChannelFuture register(Channel paramChannel, ChannelPromise paramChannelPromise) {
        return next().register(paramChannel, paramChannelPromise);
    }
}




