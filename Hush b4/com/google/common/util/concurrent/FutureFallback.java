// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;

@Beta
public interface FutureFallback<V>
{
    ListenableFuture<V> create(final Throwable p0) throws Exception;
}
