// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface Timer
{
    Timeout newTimeout(final TimerTask p0, final long p1, final TimeUnit p2);
    
    Set<Timeout> stop();
}
