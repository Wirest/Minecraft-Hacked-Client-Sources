// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import java.util.Date;
import java.io.IOException;
import org.apache.http.HttpClientConnection;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.PoolEntry;

@ThreadSafe
class CPoolEntry extends PoolEntry<HttpRoute, ManagedHttpClientConnection>
{
    private final Log log;
    private volatile boolean routeComplete;
    
    public CPoolEntry(final Log log, final String id, final HttpRoute route, final ManagedHttpClientConnection conn, final long timeToLive, final TimeUnit tunit) {
        super(id, route, conn, timeToLive, tunit);
        this.log = log;
    }
    
    public void markRouteComplete() {
        this.routeComplete = true;
    }
    
    public boolean isRouteComplete() {
        return this.routeComplete;
    }
    
    public void closeConnection() throws IOException {
        final HttpClientConnection conn = ((PoolEntry<T, HttpClientConnection>)this).getConnection();
        conn.close();
    }
    
    public void shutdownConnection() throws IOException {
        final HttpClientConnection conn = ((PoolEntry<T, HttpClientConnection>)this).getConnection();
        conn.shutdown();
    }
    
    @Override
    public boolean isExpired(final long now) {
        final boolean expired = super.isExpired(now);
        if (expired && this.log.isDebugEnabled()) {
            this.log.debug("Connection " + this + " expired @ " + new Date(this.getExpiry()));
        }
        return expired;
    }
    
    @Override
    public boolean isClosed() {
        final HttpClientConnection conn = ((PoolEntry<T, HttpClientConnection>)this).getConnection();
        return !conn.isOpen();
    }
    
    @Override
    public void close() {
        try {
            this.closeConnection();
        }
        catch (IOException ex) {
            this.log.debug("I/O error closing connection", ex);
        }
    }
}
