// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import org.apache.http.conn.routing.RouteTracker;
import java.io.IOException;
import org.apache.http.conn.ManagedClientConnection;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Asserts;
import org.apache.http.util.Args;
import org.apache.commons.logging.LogFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionManager;

@Deprecated
@ThreadSafe
public class SingleClientConnManager implements ClientConnectionManager
{
    private final Log log;
    public static final String MISUSE_MESSAGE = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
    protected final SchemeRegistry schemeRegistry;
    protected final ClientConnectionOperator connOperator;
    protected final boolean alwaysShutDown;
    @GuardedBy("this")
    protected volatile PoolEntry uniquePoolEntry;
    @GuardedBy("this")
    protected volatile ConnAdapter managedConn;
    @GuardedBy("this")
    protected volatile long lastReleaseTime;
    @GuardedBy("this")
    protected volatile long connectionExpiresTime;
    protected volatile boolean isShutDown;
    
    @Deprecated
    public SingleClientConnManager(final HttpParams params, final SchemeRegistry schreg) {
        this(schreg);
    }
    
    public SingleClientConnManager(final SchemeRegistry schreg) {
        this.log = LogFactory.getLog(this.getClass());
        Args.notNull(schreg, "Scheme registry");
        this.schemeRegistry = schreg;
        this.connOperator = this.createConnectionOperator(schreg);
        this.uniquePoolEntry = new PoolEntry();
        this.managedConn = null;
        this.lastReleaseTime = -1L;
        this.alwaysShutDown = false;
        this.isShutDown = false;
    }
    
    public SingleClientConnManager() {
        this(SchemeRegistryFactory.createDefault());
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
    
    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }
    
    protected ClientConnectionOperator createConnectionOperator(final SchemeRegistry schreg) {
        return new DefaultClientConnectionOperator(schreg);
    }
    
    protected final void assertStillUp() throws IllegalStateException {
        Asserts.check(!this.isShutDown, "Manager is shut down");
    }
    
    public final ClientConnectionRequest requestConnection(final HttpRoute route, final Object state) {
        return new ClientConnectionRequest() {
            public void abortRequest() {
            }
            
            public ManagedClientConnection getConnection(final long timeout, final TimeUnit tunit) {
                return SingleClientConnManager.this.getConnection(route, state);
            }
        };
    }
    
    public ManagedClientConnection getConnection(final HttpRoute route, final Object state) {
        Args.notNull(route, "Route");
        this.assertStillUp();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Get connection for route " + route);
        }
        synchronized (this) {
            Asserts.check(this.managedConn == null, "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
            boolean recreate = false;
            boolean shutdown = false;
            this.closeExpiredConnections();
            if (this.uniquePoolEntry.connection.isOpen()) {
                final RouteTracker tracker = this.uniquePoolEntry.tracker;
                shutdown = (tracker == null || !tracker.toRoute().equals(route));
            }
            else {
                recreate = true;
            }
            if (shutdown) {
                recreate = true;
                try {
                    this.uniquePoolEntry.shutdown();
                }
                catch (IOException iox) {
                    this.log.debug("Problem shutting down connection.", iox);
                }
            }
            if (recreate) {
                this.uniquePoolEntry = new PoolEntry();
            }
            return this.managedConn = new ConnAdapter(this.uniquePoolEntry, route);
        }
    }
    
    public void releaseConnection(final ManagedClientConnection conn, final long validDuration, final TimeUnit timeUnit) {
        Args.check(conn instanceof ConnAdapter, "Connection class mismatch, connection not obtained from this manager");
        this.assertStillUp();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Releasing connection " + conn);
        }
        final ConnAdapter sca = (ConnAdapter)conn;
        synchronized (sca) {
            if (sca.poolEntry == null) {
                return;
            }
            final ClientConnectionManager manager = sca.getManager();
            Asserts.check(manager == this, "Connection not obtained from this manager");
            try {
                if (sca.isOpen() && (this.alwaysShutDown || !sca.isMarkedReusable())) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Released connection open but not reusable.");
                    }
                    sca.shutdown();
                }
            }
            catch (IOException iox) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Exception shutting down released connection.", iox);
                }
                sca.detach();
                synchronized (this) {
                    this.managedConn = null;
                    this.lastReleaseTime = System.currentTimeMillis();
                    if (validDuration > 0L) {
                        this.connectionExpiresTime = timeUnit.toMillis(validDuration) + this.lastReleaseTime;
                    }
                    else {
                        this.connectionExpiresTime = Long.MAX_VALUE;
                    }
                }
            }
            finally {
                sca.detach();
                synchronized (this) {
                    this.managedConn = null;
                    this.lastReleaseTime = System.currentTimeMillis();
                    if (validDuration > 0L) {
                        this.connectionExpiresTime = timeUnit.toMillis(validDuration) + this.lastReleaseTime;
                    }
                    else {
                        this.connectionExpiresTime = Long.MAX_VALUE;
                    }
                }
            }
        }
    }
    
    public void closeExpiredConnections() {
        final long time = this.connectionExpiresTime;
        if (System.currentTimeMillis() >= time) {
            this.closeIdleConnections(0L, TimeUnit.MILLISECONDS);
        }
    }
    
    public void closeIdleConnections(final long idletime, final TimeUnit tunit) {
        this.assertStillUp();
        Args.notNull(tunit, "Time unit");
        synchronized (this) {
            if (this.managedConn == null && this.uniquePoolEntry.connection.isOpen()) {
                final long cutoff = System.currentTimeMillis() - tunit.toMillis(idletime);
                if (this.lastReleaseTime <= cutoff) {
                    try {
                        this.uniquePoolEntry.close();
                    }
                    catch (IOException iox) {
                        this.log.debug("Problem closing idle connection.", iox);
                    }
                }
            }
        }
    }
    
    public void shutdown() {
        this.isShutDown = true;
        synchronized (this) {
            try {
                if (this.uniquePoolEntry != null) {
                    this.uniquePoolEntry.shutdown();
                }
            }
            catch (IOException iox) {
                this.log.debug("Problem while shutting down manager.", iox);
            }
            finally {
                this.uniquePoolEntry = null;
                this.managedConn = null;
            }
        }
    }
    
    protected void revokeConnection() {
        final ConnAdapter conn = this.managedConn;
        if (conn == null) {
            return;
        }
        conn.detach();
        synchronized (this) {
            try {
                this.uniquePoolEntry.shutdown();
            }
            catch (IOException iox) {
                this.log.debug("Problem while shutting down connection.", iox);
            }
        }
    }
    
    protected class PoolEntry extends AbstractPoolEntry
    {
        protected PoolEntry() {
            super(SingleClientConnManager.this.connOperator, null);
        }
        
        protected void close() throws IOException {
            this.shutdownEntry();
            if (this.connection.isOpen()) {
                this.connection.close();
            }
        }
        
        protected void shutdown() throws IOException {
            this.shutdownEntry();
            if (this.connection.isOpen()) {
                this.connection.shutdown();
            }
        }
    }
    
    protected class ConnAdapter extends AbstractPooledConnAdapter
    {
        protected ConnAdapter(final PoolEntry entry, final HttpRoute route) {
            super(SingleClientConnManager.this, entry);
            this.markReusable();
            entry.route = route;
        }
    }
}
