// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client;

import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;

public interface ServiceUnavailableRetryStrategy
{
    boolean retryRequest(final HttpResponse p0, final int p1, final HttpContext p2);
    
    long getRetryInterval();
}
