// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Promise;
import io.netty.util.Recycler;

public final class PendingWrite
{
    private static final Recycler<PendingWrite> RECYCLER;
    private final Recycler.Handle handle;
    private Object msg;
    private Promise<Void> promise;
    
    public static PendingWrite newInstance(final Object msg, final Promise<Void> promise) {
        final PendingWrite pending = PendingWrite.RECYCLER.get();
        pending.msg = msg;
        pending.promise = promise;
        return pending;
    }
    
    private PendingWrite(final Recycler.Handle handle) {
        this.handle = handle;
    }
    
    public boolean recycle() {
        this.msg = null;
        this.promise = null;
        return PendingWrite.RECYCLER.recycle(this, this.handle);
    }
    
    public boolean failAndRecycle(final Throwable cause) {
        ReferenceCountUtil.release(this.msg);
        if (this.promise != null) {
            this.promise.setFailure(cause);
        }
        return this.recycle();
    }
    
    public boolean successAndRecycle() {
        if (this.promise != null) {
            this.promise.setSuccess(null);
        }
        return this.recycle();
    }
    
    public Object msg() {
        return this.msg;
    }
    
    public Promise<Void> promise() {
        return this.promise;
    }
    
    public Promise<Void> recycleAndGet() {
        final Promise<Void> promise = this.promise;
        this.recycle();
        return promise;
    }
    
    static {
        RECYCLER = new Recycler<PendingWrite>() {
            @Override
            protected PendingWrite newObject(final Handle handle) {
                return new PendingWrite(handle, null);
            }
        };
    }
}
