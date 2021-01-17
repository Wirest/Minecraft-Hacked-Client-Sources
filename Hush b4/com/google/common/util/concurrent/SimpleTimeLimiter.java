// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.lang.reflect.Proxy;
import com.google.common.collect.Sets;
import com.google.common.collect.ObjectArrays;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.lang.reflect.Method;
import java.util.Set;
import java.lang.reflect.InvocationHandler;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import com.google.common.base.Preconditions;
import java.util.concurrent.ExecutorService;
import com.google.common.annotations.Beta;

@Beta
public final class SimpleTimeLimiter implements TimeLimiter
{
    private final ExecutorService executor;
    
    public SimpleTimeLimiter(final ExecutorService executor) {
        this.executor = Preconditions.checkNotNull(executor);
    }
    
    public SimpleTimeLimiter() {
        this(Executors.newCachedThreadPool());
    }
    
    @Override
    public <T> T newProxy(final T target, final Class<T> interfaceType, final long timeoutDuration, final TimeUnit timeoutUnit) {
        Preconditions.checkNotNull(target);
        Preconditions.checkNotNull(interfaceType);
        Preconditions.checkNotNull(timeoutUnit);
        Preconditions.checkArgument(timeoutDuration > 0L, "bad timeout: %s", timeoutDuration);
        Preconditions.checkArgument(interfaceType.isInterface(), (Object)"interfaceType must be an interface type");
        final Set<Method> interruptibleMethods = findInterruptibleMethods(interfaceType);
        final InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(final Object obj, final Method method, final Object[] args) throws Throwable {
                final Callable<Object> callable = new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        try {
                            return method.invoke(target, args);
                        }
                        catch (InvocationTargetException e) {
                            throwCause(e, false);
                            throw new AssertionError((Object)"can't get here");
                        }
                    }
                };
                return SimpleTimeLimiter.this.callWithTimeout(callable, timeoutDuration, timeoutUnit, interruptibleMethods.contains(method));
            }
        };
        return newProxy(interfaceType, handler);
    }
    
    @Override
    public <T> T callWithTimeout(final Callable<T> callable, final long timeoutDuration, final TimeUnit timeoutUnit, final boolean amInterruptible) throws Exception {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(timeoutUnit);
        Preconditions.checkArgument(timeoutDuration > 0L, "timeout must be positive: %s", timeoutDuration);
        final Future<T> future = this.executor.submit(callable);
        try {
            if (amInterruptible) {
                try {
                    return future.get(timeoutDuration, timeoutUnit);
                }
                catch (InterruptedException e) {
                    future.cancel(true);
                    throw e;
                }
            }
            return Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
        }
        catch (ExecutionException e2) {
            throw throwCause(e2, true);
        }
        catch (TimeoutException e3) {
            future.cancel(true);
            throw new UncheckedTimeoutException(e3);
        }
    }
    
    private static Exception throwCause(final Exception e, final boolean combineStackTraces) throws Exception {
        final Throwable cause = e.getCause();
        if (cause == null) {
            throw e;
        }
        if (combineStackTraces) {
            final StackTraceElement[] combined = ObjectArrays.concat(cause.getStackTrace(), e.getStackTrace(), StackTraceElement.class);
            cause.setStackTrace(combined);
        }
        if (cause instanceof Exception) {
            throw (Exception)cause;
        }
        if (cause instanceof Error) {
            throw (Error)cause;
        }
        throw e;
    }
    
    private static Set<Method> findInterruptibleMethods(final Class<?> interfaceType) {
        final Set<Method> set = (Set<Method>)Sets.newHashSet();
        for (final Method m : interfaceType.getMethods()) {
            if (declaresInterruptedEx(m)) {
                set.add(m);
            }
        }
        return set;
    }
    
    private static boolean declaresInterruptedEx(final Method method) {
        for (final Class<?> exType : method.getExceptionTypes()) {
            if (exType == InterruptedException.class) {
                return true;
            }
        }
        return false;
    }
    
    private static <T> T newProxy(final Class<T> interfaceType, final InvocationHandler handler) {
        final Object object = Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType }, handler);
        return interfaceType.cast(object);
    }
}
