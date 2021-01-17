// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.Beta;
import java.util.concurrent.ScheduledExecutorService;

@Beta
public interface ListeningScheduledExecutorService extends ScheduledExecutorService, ListeningExecutorService
{
    ListenableScheduledFuture<?> schedule(final Runnable p0, final long p1, final TimeUnit p2);
    
     <V> ListenableScheduledFuture<V> schedule(final Callable<V> p0, final long p1, final TimeUnit p2);
    
    ListenableScheduledFuture<?> scheduleAtFixedRate(final Runnable p0, final long p1, final long p2, final TimeUnit p3);
    
    ListenableScheduledFuture<?> scheduleWithFixedDelay(final Runnable p0, final long p1, final long p2, final TimeUnit p3);
}
