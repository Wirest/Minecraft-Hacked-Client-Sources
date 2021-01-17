// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;

public final class Callables
{
    private Callables() {
    }
    
    public static <T> Callable<T> returning(@Nullable final T value) {
        return new Callable<T>() {
            @Override
            public T call() {
                return value;
            }
        };
    }
    
    static <T> Callable<T> threadRenaming(final Callable<T> callable, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(nameSupplier);
        Preconditions.checkNotNull(callable);
        return new Callable<T>() {
            @Override
            public T call() throws Exception {
                final Thread currentThread = Thread.currentThread();
                final String oldName = currentThread.getName();
                final boolean restoreName = trySetName(nameSupplier.get(), currentThread);
                try {
                    return callable.call();
                }
                finally {
                    if (restoreName) {
                        trySetName(oldName, currentThread);
                    }
                }
            }
        };
    }
    
    static Runnable threadRenaming(final Runnable task, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(nameSupplier);
        Preconditions.checkNotNull(task);
        return new Runnable() {
            @Override
            public void run() {
                final Thread currentThread = Thread.currentThread();
                final String oldName = currentThread.getName();
                final boolean restoreName = trySetName(nameSupplier.get(), currentThread);
                try {
                    task.run();
                }
                finally {
                    if (restoreName) {
                        trySetName(oldName, currentThread);
                    }
                }
            }
        };
    }
    
    private static boolean trySetName(final String threadName, final Thread currentThread) {
        try {
            currentThread.setName(threadName);
            return true;
        }
        catch (SecurityException e) {
            return false;
        }
    }
}
