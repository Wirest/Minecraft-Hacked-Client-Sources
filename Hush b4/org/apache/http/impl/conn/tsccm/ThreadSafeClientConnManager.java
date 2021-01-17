// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import org.apache.http.util.Asserts;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.impl.conn.AbstractPoolEntry;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.DefaultClientConnectionOperator;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.params.HttpParams;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.Args;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionManager;

@Deprecated
@ThreadSafe
public class ThreadSafeClientConnManager implements ClientConnectionManager
{
    private final Log log;
    protected final SchemeRegistry schemeRegistry;
    protected final AbstractConnPool connectionPool;
    protected final ConnPoolByRoute pool;
    protected final ClientConnectionOperator connOperator;
    protected final ConnPerRouteBean connPerRoute;
    
    public ThreadSafeClientConnManager(final SchemeRegistry schreg) {
        this(schreg, -1L, TimeUnit.MILLISECONDS);
    }
    
    public ThreadSafeClientConnManager() {
        this(SchemeRegistryFactory.createDefault());
    }
    
    public ThreadSafeClientConnManager(final SchemeRegistry schreg, final long connTTL, final TimeUnit connTTLTimeUnit) {
        this(schreg, connTTL, connTTLTimeUnit, new ConnPerRouteBean());
    }
    
    public ThreadSafeClientConnManager(final SchemeRegistry schreg, final long connTTL, final TimeUnit connTTLTimeUnit, final ConnPerRouteBean connPerRoute) {
        Args.notNull(schreg, "Scheme registry");
        this.log = LogFactory.getLog(this.getClass());
        this.schemeRegistry = schreg;
        this.connPerRoute = connPerRoute;
        this.connOperator = this.createConnectionOperator(schreg);
        this.pool = this.createConnectionPool(connTTL, connTTLTimeUnit);
        this.connectionPool = this.pool;
    }
    
    @Deprecated
    public ThreadSafeClientConnManager(final HttpParams params, final SchemeRegistry schreg) {
        Args.notNull(schreg, "Scheme registry");
        this.log = LogFactory.getLog(this.getClass());
        this.schemeRegistry = schreg;
        this.connPerRoute = new ConnPerRouteBean();
        this.connOperator = this.createConnectionOperator(schreg);
        this.pool = (ConnPoolByRoute)this.createConnectionPool(params);
        this.connectionPool = this.pool;
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            this.shutdown();
        }
        finally {
            super.finalize();
        }
    }
    
    @Deprecated
    protected AbstractConnPool createConnectionPool(final HttpParams params) {
        return new ConnPoolByRoute(this.connOperator, params);
    }
    
    protected ConnPoolByRoute createConnectionPool(final long connTTL, final TimeUnit connTTLTimeUnit) {
        return new ConnPoolByRoute(this.connOperator, this.connPerRoute, 20, connTTL, connTTLTimeUnit);
    }
    
    protected ClientConnectionOperator createConnectionOperator(final SchemeRegistry schreg) {
        return new DefaultClientConnectionOperator(schreg);
    }
    
    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }
    
    public ClientConnectionRequest requestConnection(final HttpRoute route, final Object state) {
        final PoolEntryRequest poolRequest = this.pool.requestPoolEntry(route, state);
        return new ClientConnectionRequest() {
            public void abortRequest() {
                poolRequest.abortRequest();
            }
            
            public ManagedClientConnection getConnection(final long timeout, final TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
                Args.notNull(route, "Route");
                if (ThreadSafeClientConnManager.this.log.isDebugEnabled()) {
                    ThreadSafeClientConnManager.this.log.debug("Get connection: " + route + ", timeout = " + timeout);
                }
                final BasicPoolEntry entry = poolRequest.getPoolEntry(timeout, tunit);
                return new BasicPooledConnAdapter(ThreadSafeClientConnManager.this, entry);
            }
        };
    }
    
    public void releaseConnection(final ManagedClientConnection conn, final long validDuration, final TimeUnit timeUnit) {
        Args.check(conn instanceof BasicPooledConnAdapter, "Connection class mismatch, connection not obtained from this manager");
        final BasicPooledConnAdapter hca = (BasicPooledConnAdapter)conn;
        if (hca.getPoolEntry() != null) {
            Asserts.check(hca.getManager() == this, "Connection not obtained from this manager");
        }
        synchronized (hca) {
            final BasicPoolEntry entry = (BasicPoolEntry)hca.getPoolEntry();
            if (entry == null) {
                return;
            }
            try {
                if (hca.isOpen() && !hca.isMarkedReusable()) {
                    hca.shutdown();
                }
            }
            catch (IOException iox) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Exception shutting down released connection.", iox);
                }
            }
            finally {
                final boolean reusable = hca.isMarkedReusable();
                if (this.log.isDebugEnabled()) {
                    if (reusable) {
                        this.log.debug("Released connection is reusable.");
                    }
                    else {
                        this.log.debug("Released connection is not reusable.");
                    }
                }
                hca.detach();
                this.pool.freeEntry(entry, reusable, validDuration, timeUnit);
            }
        }
    }
    
    public void shutdown() {
        this.log.debug("Shutting down");
        this.pool.shutdown();
    }
    
    public int getConnectionsInPool(final HttpRoute route) {
        return this.pool.getConnectionsInPool(route);
    }
    
    public int getConnectionsInPool() {
        return this.pool.getConnectionsInPool();
    }
    
    public void closeIdleConnections(final long idleTimeout, final TimeUnit tunit) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
        }
        this.pool.closeIdleConnections(idleTimeout, tunit);
    }
    
    public void closeExpiredConnections() {
        this.log.debug("Closing expired connections");
        this.pool.closeExpiredConnections();
    }
    
    public int getMaxTotal() {
        return this.pool.getMaxTotalConnections();
    }
    
    public void setMaxTotal(final int max) {
        this.pool.setMaxTotalConnections(max);
    }
    
    public int getDefaultMaxPerRoute() {
        return this.connPerRoute.getDefaultMaxPerRoute();
    }
    
    public void setDefaultMaxPerRoute(final int max) {
        this.connPerRoute.setDefaultMaxPerRoute(max);
    }
    
    public int getMaxForRoute(final HttpRoute route) {
        return this.connPerRoute.getMaxForRoute(route);
    }
    
    public void setMaxForRoute(final HttpRoute route, final int max) {
        this.connPerRoute.setMaxForRoute(route, max);
    }
}
