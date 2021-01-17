// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn;

import java.util.concurrent.TimeUnit;

@Deprecated
public interface ClientConnectionRequest
{
    ManagedClientConnection getConnection(final long p0, final TimeUnit p1) throws InterruptedException, ConnectionPoolTimeoutException;
    
    void abortRequest();
}
