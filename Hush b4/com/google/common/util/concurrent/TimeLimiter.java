// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.Beta;

@Beta
public interface TimeLimiter
{
     <T> T newProxy(final T p0, final Class<T> p1, final long p2, final TimeUnit p3);
    
     <T> T callWithTimeout(final Callable<T> p0, final long p1, final TimeUnit p2, final boolean p3) throws Exception;
}
