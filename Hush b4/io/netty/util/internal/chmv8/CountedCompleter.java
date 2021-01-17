// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal.chmv8;

import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;

public abstract class CountedCompleter<T> extends ForkJoinTask<T>
{
    private static final long serialVersionUID = 5232453752276485070L;
    final CountedCompleter<?> completer;
    volatile int pending;
    private static final Unsafe U;
    private static final long PENDING;
    
    protected CountedCompleter(final CountedCompleter<?> completer, final int initialPendingCount) {
        this.completer = completer;
        this.pending = initialPendingCount;
    }
    
    protected CountedCompleter(final CountedCompleter<?> completer) {
        this.completer = completer;
    }
    
    protected CountedCompleter() {
        this.completer = null;
    }
    
    public abstract void compute();
    
    public void onCompletion(final CountedCompleter<?> caller) {
    }
    
    public boolean onExceptionalCompletion(final Throwable ex, final CountedCompleter<?> caller) {
        return true;
    }
    
    public final CountedCompleter<?> getCompleter() {
        return this.completer;
    }
    
    public final int getPendingCount() {
        return this.pending;
    }
    
    public final void setPendingCount(final int count) {
        this.pending = count;
    }
    
    public final void addToPendingCount(final int delta) {
        int c;
        while (!CountedCompleter.U.compareAndSwapInt(this, CountedCompleter.PENDING, c = this.pending, c + delta)) {}
    }
    
    public final boolean compareAndSetPendingCount(final int expected, final int count) {
        return CountedCompleter.U.compareAndSwapInt(this, CountedCompleter.PENDING, expected, count);
    }
    
    public final int decrementPendingCountUnlessZero() {
        int c;
        while ((c = this.pending) != 0 && !CountedCompleter.U.compareAndSwapInt(this, CountedCompleter.PENDING, c, c - 1)) {}
        return c;
    }
    
    public final CountedCompleter<?> getRoot() {
        CountedCompleter<?> a;
        CountedCompleter<?> p;
        for (a = this; (p = a.completer) != null; a = p) {}
        return a;
    }
    
    public final void tryComplete() {
        CountedCompleter<?> s;
        CountedCompleter<?> a = s = this;
        while (true) {
            final int c;
            if ((c = a.pending) == 0) {
                a.onCompletion(s);
                if ((a = (s = a).completer) == null) {
                    s.quietlyComplete();
                    return;
                }
                continue;
            }
            else {
                if (CountedCompleter.U.compareAndSwapInt(a, CountedCompleter.PENDING, c, c - 1)) {
                    return;
                }
                continue;
            }
        }
    }
    
    public final void propagateCompletion() {
        CountedCompleter<?> s;
        CountedCompleter<?> a = s = this;
        while (true) {
            final int c;
            if ((c = a.pending) == 0) {
                if ((a = (s = a).completer) == null) {
                    s.quietlyComplete();
                    return;
                }
                continue;
            }
            else {
                if (CountedCompleter.U.compareAndSwapInt(a, CountedCompleter.PENDING, c, c - 1)) {
                    return;
                }
                continue;
            }
        }
    }
    
    @Override
    public void complete(final T rawResult) {
        this.setRawResult(rawResult);
        this.onCompletion(this);
        this.quietlyComplete();
        final CountedCompleter<?> p;
        if ((p = this.completer) != null) {
            p.tryComplete();
        }
    }
    
    public final CountedCompleter<?> firstComplete() {
        int c;
        while ((c = this.pending) != 0) {
            if (CountedCompleter.U.compareAndSwapInt(this, CountedCompleter.PENDING, c, c - 1)) {
                return null;
            }
        }
        return this;
    }
    
    public final CountedCompleter<?> nextComplete() {
        final CountedCompleter<?> p;
        if ((p = this.completer) != null) {
            return p.firstComplete();
        }
        this.quietlyComplete();
        return null;
    }
    
    public final void quietlyCompleteRoot() {
        CountedCompleter<?> a;
        CountedCompleter<?> p;
        for (a = this; (p = a.completer) != null; a = p) {}
        a.quietlyComplete();
    }
    
    @Override
    void internalPropagateException(final Throwable ex) {
        CountedCompleter<?> s;
        CountedCompleter<?> a = s = this;
        while (a.onExceptionalCompletion(ex, s) && (a = (s = a).completer) != null && a.status >= 0 && a.recordExceptionalCompletion(ex) == Integer.MIN_VALUE) {}
    }
    
    @Override
    protected final boolean exec() {
        this.compute();
        return false;
    }
    
    @Override
    public T getRawResult() {
        return null;
    }
    
    @Override
    protected void setRawResult(final T t) {
    }
    
    private static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        }
        catch (SecurityException tryReflectionInstead) {
            try {
                return AccessController.doPrivileged((PrivilegedExceptionAction<Unsafe>)new PrivilegedExceptionAction<Unsafe>() {
                    @Override
                    public Unsafe run() throws Exception {
                        final Class<Unsafe> k = Unsafe.class;
                        for (final Field f : k.getDeclaredFields()) {
                            f.setAccessible(true);
                            final Object x = f.get(null);
                            if (k.isInstance(x)) {
                                return k.cast(x);
                            }
                        }
                        throw new NoSuchFieldError("the Unsafe");
                    }
                });
            }
            catch (PrivilegedActionException e) {
                throw new RuntimeException("Could not initialize intrinsics", e.getCause());
            }
        }
    }
    
    static {
        try {
            U = getUnsafe();
            PENDING = CountedCompleter.U.objectFieldOffset(CountedCompleter.class.getDeclaredField("pending"));
        }
        catch (Exception e) {
            throw new Error(e);
        }
    }
}
