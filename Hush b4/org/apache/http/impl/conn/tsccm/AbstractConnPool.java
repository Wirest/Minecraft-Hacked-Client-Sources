// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import org.apache.http.conn.OperatedClientConnection;
import java.util.Iterator;
import org.apache.http.util.Args;
import java.lang.ref.Reference;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.routing.HttpRoute;
import java.util.concurrent.locks.ReentrantLock;
import java.util.HashSet;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.conn.IdleConnectionHandler;
import java.lang.ref.ReferenceQueue;
import org.apache.http.annotation.GuardedBy;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import org.apache.commons.logging.Log;

@Deprecated
public abstract class AbstractConnPool
{
    private final Log log;
    protected final Lock poolLock;
    @GuardedBy("poolLock")
    protected Set<BasicPoolEntry> leasedConnections;
    @GuardedBy("poolLock")
    protected int numConnections;
    protected volatile boolean isShutDown;
    protected Set<BasicPoolEntryRef> issuedConnections;
    protected ReferenceQueue<Object> refQueue;
    protected IdleConnectionHandler idleConnHandler;
    
    protected AbstractConnPool() {
        this.log = LogFactory.getLog(this.getClass());
        this.leasedConnections = new HashSet<BasicPoolEntry>();
        this.idleConnHandler = new IdleConnectionHandler();
        this.poolLock = new ReentrantLock();
    }
    
    public void enableConnectionGC() throws IllegalStateException {
    }
    
    public final BasicPoolEntry getEntry(final HttpRoute route, final Object state, final long timeout, final TimeUnit tunit) throws ConnectionPoolTimeoutException, InterruptedException {
        return this.requestPoolEntry(route, state).getPoolEntry(timeout, tunit);
    }
    
    public abstract PoolEntryRequest requestPoolEntry(final HttpRoute p0, final Object p1);
    
    public abstract void freeEntry(final BasicPoolEntry p0, final boolean p1, final long p2, final TimeUnit p3);
    
    public void handleReference(final Reference<?> ref) {
    }
    
    protected abstract void handleLostEntry(final HttpRoute p0);
    
    public void closeIdleConnections(final long idletime, final TimeUnit tunit) {
        Args.notNull(tunit, "Time unit");
        this.poolLock.lock();
        try {
            this.idleConnHandler.closeIdleConnections(tunit.toMillis(idletime));
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    public void closeExpiredConnections() {
        this.poolLock.lock();
        try {
            this.idleConnHandler.closeExpiredConnections();
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    public abstract void deleteClosedConnections();
    
    public void shutdown() {
        this.poolLock.lock();
        try {
            if (this.isShutDown) {
                return;
            }
            final Iterator<BasicPoolEntry> iter = this.leasedConnections.iterator();
            while (iter.hasNext()) {
                final BasicPoolEntry entry = iter.next();
                iter.remove();
                this.closeConnection(entry.getConnection());
            }
            this.idleConnHandler.removeAll();
            this.isShutDown = true;
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    protected void closeConnection(final OperatedClientConnection conn) {
        if (conn != null) {
            try {
                conn.close();
            }
            catch (IOException ex) {
                this.log.debug("I/O error closing connection", ex);
            }
        }
    }
}
