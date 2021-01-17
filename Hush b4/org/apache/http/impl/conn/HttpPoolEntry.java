// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.commons.logging.Log;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.PoolEntry;

@Deprecated
class HttpPoolEntry extends PoolEntry<HttpRoute, OperatedClientConnection>
{
    private final Log log;
    private final RouteTracker tracker;
    
    public HttpPoolEntry(final Log log, final String id, final HttpRoute route, final OperatedClientConnection conn, final long timeToLive, final TimeUnit tunit) {
        super(id, route, conn, timeToLive, tunit);
        this.log = log;
        this.tracker = new RouteTracker(route);
    }
    
    @Override
    public boolean isExpired(final long now) {
        final boolean expired = super.isExpired(now);
        if (expired && this.log.isDebugEnabled()) {
            this.log.debug("Connection " + this + " expired @ " + new Date(this.getExpiry()));
        }
        return expired;
    }
    
    RouteTracker getTracker() {
        return this.tracker;
    }
    
    HttpRoute getPlannedRoute() {
        return ((PoolEntry<HttpRoute, C>)this).getRoute();
    }
    
    HttpRoute getEffectiveRoute() {
        return this.tracker.toRoute();
    }
    
    @Override
    public boolean isClosed() {
        final OperatedClientConnection conn = ((PoolEntry<T, OperatedClientConnection>)this).getConnection();
        return !conn.isOpen();
    }
    
    @Override
    public void close() {
        final OperatedClientConnection conn = ((PoolEntry<T, OperatedClientConnection>)this).getConnection();
        try {
            conn.close();
        }
        catch (IOException ex) {
            this.log.debug("I/O error closing connection", ex);
        }
    }
}
