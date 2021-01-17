// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import org.apache.http.pool.PoolEntry;
import org.apache.commons.logging.LogFactory;
import org.apache.http.pool.ConnFactory;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.AbstractConnPool;

@ThreadSafe
class CPool extends AbstractConnPool<HttpRoute, ManagedHttpClientConnection, CPoolEntry>
{
    private static final AtomicLong COUNTER;
    private final Log log;
    private final long timeToLive;
    private final TimeUnit tunit;
    
    public CPool(final ConnFactory<HttpRoute, ManagedHttpClientConnection> connFactory, final int defaultMaxPerRoute, final int maxTotal, final long timeToLive, final TimeUnit tunit) {
        super(connFactory, defaultMaxPerRoute, maxTotal);
        this.log = LogFactory.getLog(CPool.class);
        this.timeToLive = timeToLive;
        this.tunit = tunit;
    }
    
    @Override
    protected CPoolEntry createEntry(final HttpRoute route, final ManagedHttpClientConnection conn) {
        final String id = Long.toString(CPool.COUNTER.getAndIncrement());
        return new CPoolEntry(this.log, id, route, conn, this.timeToLive, this.tunit);
    }
    
    static {
        COUNTER = new AtomicLong();
    }
}
