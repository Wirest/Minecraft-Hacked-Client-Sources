// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.HttpResponse;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import org.apache.http.client.ConnectionBackoffStrategy;

public class DefaultBackoffStrategy implements ConnectionBackoffStrategy
{
    public boolean shouldBackoff(final Throwable t) {
        return t instanceof SocketTimeoutException || t instanceof ConnectException;
    }
    
    public boolean shouldBackoff(final HttpResponse resp) {
        return resp.getStatusLine().getStatusCode() == 503;
    }
}
