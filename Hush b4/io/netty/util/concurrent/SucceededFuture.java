// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

public final class SucceededFuture<V> extends CompleteFuture<V>
{
    private final V result;
    
    public SucceededFuture(final EventExecutor executor, final V result) {
        super(executor);
        this.result = result;
    }
    
    @Override
    public Throwable cause() {
        return null;
    }
    
    @Override
    public boolean isSuccess() {
        return true;
    }
    
    @Override
    public V getNow() {
        return this.result;
    }
}
