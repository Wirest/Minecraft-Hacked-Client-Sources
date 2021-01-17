// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import org.apache.http.pool.PoolEntry;
import org.apache.http.pool.AbstractConnPool;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import org.apache.http.util.Asserts;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ManagedClientConnection;
import java.util.concurrent.Future;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.pool.PoolStats;
import org.apache.http.util.Args;
import org.apache.commons.logging.LogFactory;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.conn.ClientConnectionManager;

@Deprecated
@ThreadSafe
public class PoolingClientConnectionManager implements ClientConnectionManager, ConnPoolControl<HttpRoute>
{
    private final Log log;
    private final SchemeRegistry schemeRegistry;
    private final HttpConnPool pool;
    private final ClientConnectionOperator operator;
    private final DnsResolver dnsResolver;
    
    public PoolingClientConnectionManager(final SchemeRegistry schreg) {
        this(schreg, -1L, TimeUnit.MILLISECONDS);
    }
    
    public PoolingClientConnectionManager(final SchemeRegistry schreg, final DnsResolver dnsResolver) {
        this(schreg, -1L, TimeUnit.MILLISECONDS, dnsResolver);
    }
    
    public PoolingClientConnectionManager() {
        this(SchemeRegistryFactory.createDefault());
    }
    
    public PoolingClientConnectionManager(final SchemeRegistry schemeRegistry, final long timeToLive, final TimeUnit tunit) {
        this(schemeRegistry, timeToLive, tunit, new SystemDefaultDnsResolver());
    }
    
    public PoolingClientConnectionManager(final SchemeRegistry schemeRegistry, final long timeToLive, final TimeUnit tunit, final DnsResolver dnsResolver) {
        this.log = LogFactory.getLog(this.getClass());
        Args.notNull(schemeRegistry, "Scheme registry");
        Args.notNull(dnsResolver, "DNS resolver");
        this.schemeRegistry = schemeRegistry;
        this.dnsResolver = dnsResolver;
        this.operator = this.createConnectionOperator(schemeRegistry);
        this.pool = new HttpConnPool(this.log, this.operator, 2, 20, timeToLive, tunit);
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
    
    protected ClientConnectionOperator createConnectionOperator(final SchemeRegistry schreg) {
        return new DefaultClientConnectionOperator(schreg, this.dnsResolver);
    }
    
    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }
    
    private String format(final HttpRoute route, final Object state) {
        final StringBuilder buf = new StringBuilder();
        buf.append("[route: ").append(route).append("]");
        if (state != null) {
            buf.append("[state: ").append(state).append("]");
        }
        return buf.toString();
    }
    
    private String formatStats(final HttpRoute route) {
        final StringBuilder buf = new StringBuilder();
        final PoolStats totals = this.pool.getTotalStats();
        final PoolStats stats = ((AbstractConnPool<HttpRoute, C, E>)this.pool).getStats(route);
        buf.append("[total kept alive: ").append(totals.getAvailable()).append("; ");
        buf.append("route allocated: ").append(stats.getLeased() + stats.getAvailable());
        buf.append(" of ").append(stats.getMax()).append("; ");
        buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
        buf.append(" of ").append(totals.getMax()).append("]");
        return buf.toString();
    }
    
    private String format(final HttpPoolEntry entry) {
        final StringBuilder buf = new StringBuilder();
        buf.append("[id: ").append(entry.getId()).append("]");
        buf.append("[route: ").append(((PoolEntry<Object, C>)entry).getRoute()).append("]");
        final Object state = entry.getState();
        if (state != null) {
            buf.append("[state: ").append(state).append("]");
        }
        return buf.toString();
    }
    
    public ClientConnectionRequest requestConnection(final HttpRoute route, final Object state) {
        Args.notNull(route, "HTTP route");
        if (this.log.isDebugEnabled()) {
            this.log.debug("Connection request: " + this.format(route, state) + this.formatStats(route));
        }
        final Future<HttpPoolEntry> future = ((AbstractConnPool<HttpRoute, C, HttpPoolEntry>)this.pool).lease(route, state);
        return new ClientConnectionRequest() {
            public void abortRequest() {
                future.cancel(true);
            }
            
            public ManagedClientConnection getConnection(final long timeout, final TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
                return PoolingClientConnectionManager.this.leaseConnection(future, timeout, tunit);
            }
        };
    }
    
    ManagedClientConnection leaseConnection(final Future<HttpPoolEntry> future, final long timeout, final TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
        try {
            final HttpPoolEntry entry = future.get(timeout, tunit);
            if (entry == null || future.isCancelled()) {
                throw new InterruptedException();
            }
            Asserts.check(entry.getConnection() != null, "Pool entry with no connection");
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connection leased: " + this.format(entry) + this.formatStats(((PoolEntry<HttpRoute, C>)entry).getRoute()));
            }
            return new ManagedClientConnectionImpl(this, this.operator, entry);
        }
        catch (ExecutionException ex) {
            Throwable cause = ex.getCause();
            if (cause == null) {
                cause = ex;
            }
            this.log.error("Unexpected exception leasing connection from pool", cause);
            throw new InterruptedException();
        }
        catch (TimeoutException ex2) {
            throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
        }
    }
    
    public void releaseConnection(final ManagedClientConnection conn, final long keepalive, final TimeUnit tunit) {
        Args.check(conn instanceof ManagedClientConnectionImpl, "Connection class mismatch, connection not obtained from this manager");
        final ManagedClientConnectionImpl managedConn = (ManagedClientConnectionImpl)conn;
        Asserts.check(managedConn.getManager() == this, "Connection not obtained from this manager");
        synchronized (managedConn) {
            final HttpPoolEntry entry = managedConn.detach();
            if (entry == null) {
                return;
            }
            try {
                if (managedConn.isOpen() && !managedConn.isMarkedReusable()) {
                    try {
                        managedConn.shutdown();
                    }
                    catch (IOException iox) {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("I/O exception shutting down released connection", iox);
                        }
                    }
                }
                if (managedConn.isMarkedReusable()) {
                    entry.updateExpiry(keepalive, (tunit != null) ? tunit : TimeUnit.MILLISECONDS);
                    if (this.log.isDebugEnabled()) {
                        String s;
                        if (keepalive > 0L) {
                            s = "for " + keepalive + " " + tunit;
                        }
                        else {
                            s = "indefinitely";
                        }
                        this.log.debug("Connection " + this.format(entry) + " can be kept alive " + s);
                    }
                }
            }
            finally {
                ((AbstractConnPool<T, C, HttpPoolEntry>)this.pool).release(entry, managedConn.isMarkedReusable());
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connection released: " + this.format(entry) + this.formatStats(((PoolEntry<HttpRoute, C>)entry).getRoute()));
            }
        }
    }
    
    public void shutdown() {
        this.log.debug("Connection manager is shutting down");
        try {
            this.pool.shutdown();
        }
        catch (IOException ex) {
            this.log.debug("I/O exception shutting down connection manager", ex);
        }
        this.log.debug("Connection manager shut down");
    }
    
    public void closeIdleConnections(final long idleTimeout, final TimeUnit tunit) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
        }
        this.pool.closeIdle(idleTimeout, tunit);
    }
    
    public void closeExpiredConnections() {
        this.log.debug("Closing expired connections");
        this.pool.closeExpired();
    }
    
    public int getMaxTotal() {
        return this.pool.getMaxTotal();
    }
    
    public void setMaxTotal(final int max) {
        this.pool.setMaxTotal(max);
    }
    
    public int getDefaultMaxPerRoute() {
        return this.pool.getDefaultMaxPerRoute();
    }
    
    public void setDefaultMaxPerRoute(final int max) {
        this.pool.setDefaultMaxPerRoute(max);
    }
    
    public int getMaxPerRoute(final HttpRoute route) {
        return ((AbstractConnPool<HttpRoute, C, E>)this.pool).getMaxPerRoute(route);
    }
    
    public void setMaxPerRoute(final HttpRoute route, final int max) {
        ((AbstractConnPool<HttpRoute, C, E>)this.pool).setMaxPerRoute(route, max);
    }
    
    public PoolStats getTotalStats() {
        return this.pool.getTotalStats();
    }
    
    public PoolStats getStats(final HttpRoute route) {
        return ((AbstractConnPool<HttpRoute, C, E>)this.pool).getStats(route);
    }
}
