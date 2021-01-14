package io.netty.util.concurrent;

public abstract interface EventExecutor
        extends EventExecutorGroup {
    public abstract EventExecutor next();

    public abstract EventExecutorGroup parent();

    public abstract boolean inEventLoop();

    public abstract boolean inEventLoop(Thread paramThread);

    public abstract <V> Promise<V> newPromise();

    public abstract <V> ProgressivePromise<V> newProgressivePromise();

    public abstract <V> Future<V> newSucceededFuture(V paramV);

    public abstract <V> Future<V> newFailedFuture(Throwable paramThrowable);
}




