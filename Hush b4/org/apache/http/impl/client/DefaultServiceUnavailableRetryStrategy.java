// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;
import org.apache.http.util.Args;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.ServiceUnavailableRetryStrategy;

@Immutable
public class DefaultServiceUnavailableRetryStrategy implements ServiceUnavailableRetryStrategy
{
    private final int maxRetries;
    private final long retryInterval;
    
    public DefaultServiceUnavailableRetryStrategy(final int maxRetries, final int retryInterval) {
        Args.positive(maxRetries, "Max retries");
        Args.positive(retryInterval, "Retry interval");
        this.maxRetries = maxRetries;
        this.retryInterval = retryInterval;
    }
    
    public DefaultServiceUnavailableRetryStrategy() {
        this(1, 1000);
    }
    
    public boolean retryRequest(final HttpResponse response, final int executionCount, final HttpContext context) {
        return executionCount <= this.maxRetries && response.getStatusLine().getStatusCode() == 503;
    }
    
    public long getRetryInterval() {
        return this.retryInterval;
    }
}
