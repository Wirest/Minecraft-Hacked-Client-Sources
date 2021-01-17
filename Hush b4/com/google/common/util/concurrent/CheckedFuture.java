// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.Beta;

@Beta
public interface CheckedFuture<V, X extends Exception> extends ListenableFuture<V>
{
    V checkedGet() throws X, Exception;
    
    V checkedGet(final long p0, final TimeUnit p1) throws TimeoutException, X, Exception;
}
