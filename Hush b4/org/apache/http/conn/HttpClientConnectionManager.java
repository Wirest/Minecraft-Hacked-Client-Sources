// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn;

import java.io.IOException;
import org.apache.http.protocol.HttpContext;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;

public interface HttpClientConnectionManager
{
    ConnectionRequest requestConnection(final HttpRoute p0, final Object p1);
    
    void releaseConnection(final HttpClientConnection p0, final Object p1, final long p2, final TimeUnit p3);
    
    void connect(final HttpClientConnection p0, final HttpRoute p1, final int p2, final HttpContext p3) throws IOException;
    
    void upgrade(final HttpClientConnection p0, final HttpRoute p1, final HttpContext p2) throws IOException;
    
    void routeComplete(final HttpClientConnection p0, final HttpRoute p1, final HttpContext p2) throws IOException;
    
    void closeIdleConnections(final long p0, final TimeUnit p1);
    
    void closeExpiredConnections();
    
    void shutdown();
}
