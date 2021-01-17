// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn.tsccm;

import org.apache.http.conn.ConnectionPoolTimeoutException;
import java.util.concurrent.TimeUnit;

@Deprecated
public interface PoolEntryRequest
{
    BasicPoolEntry getPoolEntry(final long p0, final TimeUnit p1) throws InterruptedException, ConnectionPoolTimeoutException;
    
    void abortRequest();
}
