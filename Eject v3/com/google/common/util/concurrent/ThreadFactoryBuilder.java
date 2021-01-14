package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public final class ThreadFactoryBuilder {
    private String nameFormat = null;
    private Boolean daemon = null;
    private Integer priority = null;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;
    private ThreadFactory backingThreadFactory = null;

    private static ThreadFactory build(ThreadFactoryBuilder paramThreadFactoryBuilder) {
        final String str = paramThreadFactoryBuilder.nameFormat;
        final Boolean localBoolean = paramThreadFactoryBuilder.daemon;
        final Integer localInteger = paramThreadFactoryBuilder.priority;
        final Thread.UncaughtExceptionHandler localUncaughtExceptionHandler = paramThreadFactoryBuilder.uncaughtExceptionHandler;
        ThreadFactory localThreadFactory = paramThreadFactoryBuilder.backingThreadFactory != null ? paramThreadFactoryBuilder.backingThreadFactory : Executors.defaultThreadFactory();
        final AtomicLong localAtomicLong = str != null ? new AtomicLong(0L) : null;
        new ThreadFactory() {
            public Thread newThread(Runnable paramAnonymousRunnable) {
                Thread localThread = this.val$backingThreadFactory.newThread(paramAnonymousRunnable);
                if (str != null) {
                    localThread.setName(String.format(str, new Object[]{Long.valueOf(localAtomicLong.getAndIncrement())}));
                }
                if (localBoolean != null) {
                    localThread.setDaemon(localBoolean.booleanValue());
                }
                if (localInteger != null) {
                    localThread.setPriority(localInteger.intValue());
                }
                if (localUncaughtExceptionHandler != null) {
                    localThread.setUncaughtExceptionHandler(localUncaughtExceptionHandler);
                }
                return localThread;
            }
        };
    }

    public ThreadFactoryBuilder setNameFormat(String paramString) {
        String.format(paramString, new Object[]{Integer.valueOf(0)});
        this.nameFormat = paramString;
        return this;
    }

    public ThreadFactoryBuilder setDaemon(boolean paramBoolean) {
        this.daemon = Boolean.valueOf(paramBoolean);
        return this;
    }

    public ThreadFactoryBuilder setPriority(int paramInt) {
        Preconditions.checkArgument(paramInt >= 1, "Thread priority (%s) must be >= %s", new Object[]{Integer.valueOf(paramInt), Integer.valueOf(1)});
        Preconditions.checkArgument(paramInt <= 10, "Thread priority (%s) must be <= %s", new Object[]{Integer.valueOf(paramInt), Integer.valueOf(10)});
        this.priority = Integer.valueOf(paramInt);
        return this;
    }

    public ThreadFactoryBuilder setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler paramUncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = ((Thread.UncaughtExceptionHandler) Preconditions.checkNotNull(paramUncaughtExceptionHandler));
        return this;
    }

    public ThreadFactoryBuilder setThreadFactory(ThreadFactory paramThreadFactory) {
        this.backingThreadFactory = ((ThreadFactory) Preconditions.checkNotNull(paramThreadFactory));
        return this;
    }

    public ThreadFactory build() {
        return build(this);
    }
}




