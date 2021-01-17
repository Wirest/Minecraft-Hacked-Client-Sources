// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn.tsccm;

import java.util.Iterator;
import org.apache.http.util.Asserts;
import java.util.Date;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.OperatedClientConnection;
import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.commons.logging.LogFactory;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.routing.HttpRoute;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.ClientConnectionOperator;
import java.util.concurrent.locks.Lock;
import org.apache.commons.logging.Log;

@Deprecated
public class ConnPoolByRoute extends AbstractConnPool
{
    private final Log log;
    private final Lock poolLock;
    protected final ClientConnectionOperator operator;
    protected final ConnPerRoute connPerRoute;
    protected final Set<BasicPoolEntry> leasedConnections;
    protected final Queue<BasicPoolEntry> freeConnections;
    protected final Queue<WaitingThread> waitingThreads;
    protected final Map<HttpRoute, RouteSpecificPool> routeToPool;
    private final long connTTL;
    private final TimeUnit connTTLTimeUnit;
    protected volatile boolean shutdown;
    protected volatile int maxTotalConnections;
    protected volatile int numConnections;
    
    public ConnPoolByRoute(final ClientConnectionOperator operator, final ConnPerRoute connPerRoute, final int maxTotalConnections) {
        this(operator, connPerRoute, maxTotalConnections, -1L, TimeUnit.MILLISECONDS);
    }
    
    public ConnPoolByRoute(final ClientConnectionOperator operator, final ConnPerRoute connPerRoute, final int maxTotalConnections, final long connTTL, final TimeUnit connTTLTimeUnit) {
        this.log = LogFactory.getLog(this.getClass());
        Args.notNull(operator, "Connection operator");
        Args.notNull(connPerRoute, "Connections per route");
        this.poolLock = super.poolLock;
        this.leasedConnections = (Set<BasicPoolEntry>)super.leasedConnections;
        this.operator = operator;
        this.connPerRoute = connPerRoute;
        this.maxTotalConnections = maxTotalConnections;
        this.freeConnections = this.createFreeConnQueue();
        this.waitingThreads = this.createWaitingThreadQueue();
        this.routeToPool = this.createRouteToPoolMap();
        this.connTTL = connTTL;
        this.connTTLTimeUnit = connTTLTimeUnit;
    }
    
    protected Lock getLock() {
        return this.poolLock;
    }
    
    @Deprecated
    public ConnPoolByRoute(final ClientConnectionOperator operator, final HttpParams params) {
        this(operator, ConnManagerParams.getMaxConnectionsPerRoute(params), ConnManagerParams.getMaxTotalConnections(params));
    }
    
    protected Queue<BasicPoolEntry> createFreeConnQueue() {
        return new LinkedList<BasicPoolEntry>();
    }
    
    protected Queue<WaitingThread> createWaitingThreadQueue() {
        return new LinkedList<WaitingThread>();
    }
    
    protected Map<HttpRoute, RouteSpecificPool> createRouteToPoolMap() {
        return new HashMap<HttpRoute, RouteSpecificPool>();
    }
    
    protected RouteSpecificPool newRouteSpecificPool(final HttpRoute route) {
        return new RouteSpecificPool(route, this.connPerRoute);
    }
    
    protected WaitingThread newWaitingThread(final Condition cond, final RouteSpecificPool rospl) {
        return new WaitingThread(cond, rospl);
    }
    
    private void closeConnection(final BasicPoolEntry entry) {
        final OperatedClientConnection conn = entry.getConnection();
        if (conn != null) {
            try {
                conn.close();
            }
            catch (IOException ex) {
                this.log.debug("I/O error closing connection", ex);
            }
        }
    }
    
    protected RouteSpecificPool getRoutePool(final HttpRoute route, final boolean create) {
        RouteSpecificPool rospl = null;
        this.poolLock.lock();
        try {
            rospl = this.routeToPool.get(route);
            if (rospl == null && create) {
                rospl = this.newRouteSpecificPool(route);
                this.routeToPool.put(route, rospl);
            }
        }
        finally {
            this.poolLock.unlock();
        }
        return rospl;
    }
    
    public int getConnectionsInPool(final HttpRoute route) {
        this.poolLock.lock();
        try {
            final RouteSpecificPool rospl = this.getRoutePool(route, false);
            return (rospl != null) ? rospl.getEntryCount() : 0;
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    public int getConnectionsInPool() {
        this.poolLock.lock();
        try {
            return this.numConnections;
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    @Override
    public PoolEntryRequest requestPoolEntry(final HttpRoute route, final Object state) {
        final WaitingThreadAborter aborter = new WaitingThreadAborter();
        return new PoolEntryRequest() {
            public void abortRequest() {
                ConnPoolByRoute.this.poolLock.lock();
                try {
                    aborter.abort();
                }
                finally {
                    ConnPoolByRoute.this.poolLock.unlock();
                }
            }
            
            public BasicPoolEntry getPoolEntry(final long timeout, final TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
                return ConnPoolByRoute.this.getEntryBlocking(route, state, timeout, tunit, aborter);
            }
        };
    }
    
    protected BasicPoolEntry getEntryBlocking(final HttpRoute route, final Object state, final long timeout, final TimeUnit tunit, final WaitingThreadAborter aborter) throws ConnectionPoolTimeoutException, InterruptedException {
        Date deadline = null;
        if (timeout > 0L) {
            deadline = new Date(System.currentTimeMillis() + tunit.toMillis(timeout));
        }
        BasicPoolEntry entry = null;
        this.poolLock.lock();
        try {
            RouteSpecificPool rospl = this.getRoutePool(route, true);
            WaitingThread waitingThread = null;
            while (entry == null) {
                Asserts.check(!this.shutdown, "Connection pool shut down");
                if (this.log.isDebugEnabled()) {
                    this.log.debug("[" + route + "] total kept alive: " + this.freeConnections.size() + ", total issued: " + this.leasedConnections.size() + ", total allocated: " + this.numConnections + " out of " + this.maxTotalConnections);
                }
                entry = this.getFreeEntry(rospl, state);
                if (entry != null) {
                    break;
                }
                final boolean hasCapacity = rospl.getCapacity() > 0;
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Available capacity: " + rospl.getCapacity() + " out of " + rospl.getMaxEntries() + " [" + route + "][" + state + "]");
                }
                if (hasCapacity && this.numConnections < this.maxTotalConnections) {
                    entry = this.createEntry(rospl, this.operator);
                }
                else if (hasCapacity && !this.freeConnections.isEmpty()) {
                    this.deleteLeastUsedEntry();
                    rospl = this.getRoutePool(route, true);
                    entry = this.createEntry(rospl, this.operator);
                }
                else {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Need to wait for connection [" + route + "][" + state + "]");
                    }
                    if (waitingThread == null) {
                        waitingThread = this.newWaitingThread(this.poolLock.newCondition(), rospl);
                        aborter.setWaitingThread(waitingThread);
                    }
                    boolean success = false;
                    try {
                        rospl.queueThread(waitingThread);
                        this.waitingThreads.add(waitingThread);
                        success = waitingThread.await(deadline);
                    }
                    finally {
                        rospl.removeThread(waitingThread);
                        this.waitingThreads.remove(waitingThread);
                    }
                    if (!success && deadline != null && deadline.getTime() <= System.currentTimeMillis()) {
                        throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
                    }
                    continue;
                }
            }
        }
        finally {
            this.poolLock.unlock();
        }
        return entry;
    }
    
    @Override
    public void freeEntry(final BasicPoolEntry entry, final boolean reusable, final long validDuration, final TimeUnit timeUnit) {
        final HttpRoute route = entry.getPlannedRoute();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Releasing connection [" + route + "][" + entry.getState() + "]");
        }
        this.poolLock.lock();
        try {
            if (this.shutdown) {
                this.closeConnection(entry);
                return;
            }
            this.leasedConnections.remove(entry);
            final RouteSpecificPool rospl = this.getRoutePool(route, true);
            if (reusable && rospl.getCapacity() >= 0) {
                if (this.log.isDebugEnabled()) {
                    String s;
                    if (validDuration > 0L) {
                        s = "for " + validDuration + " " + timeUnit;
                    }
                    else {
                        s = "indefinitely";
                    }
                    this.log.debug("Pooling connection [" + route + "][" + entry.getState() + "]; keep alive " + s);
                }
                rospl.freeEntry(entry);
                entry.updateExpiry(validDuration, timeUnit);
                this.freeConnections.add(entry);
            }
            else {
                this.closeConnection(entry);
                rospl.dropEntry();
                --this.numConnections;
            }
            this.notifyWaitingThread(rospl);
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    protected BasicPoolEntry getFreeEntry(final RouteSpecificPool rospl, final Object state) {
        BasicPoolEntry entry = null;
        this.poolLock.lock();
        try {
            boolean done = false;
            while (!done) {
                entry = rospl.allocEntry(state);
                if (entry != null) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Getting free connection [" + rospl.getRoute() + "][" + state + "]");
                    }
                    this.freeConnections.remove(entry);
                    if (entry.isExpired(System.currentTimeMillis())) {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("Closing expired free connection [" + rospl.getRoute() + "][" + state + "]");
                        }
                        this.closeConnection(entry);
                        rospl.dropEntry();
                        --this.numConnections;
                    }
                    else {
                        this.leasedConnections.add(entry);
                        done = true;
                    }
                }
                else {
                    done = true;
                    if (!this.log.isDebugEnabled()) {
                        continue;
                    }
                    this.log.debug("No free connections [" + rospl.getRoute() + "][" + state + "]");
                }
            }
        }
        finally {
            this.poolLock.unlock();
        }
        return entry;
    }
    
    protected BasicPoolEntry createEntry(final RouteSpecificPool rospl, final ClientConnectionOperator op) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Creating new connection [" + rospl.getRoute() + "]");
        }
        final BasicPoolEntry entry = new BasicPoolEntry(op, rospl.getRoute(), this.connTTL, this.connTTLTimeUnit);
        this.poolLock.lock();
        try {
            rospl.createdEntry(entry);
            ++this.numConnections;
            this.leasedConnections.add(entry);
        }
        finally {
            this.poolLock.unlock();
        }
        return entry;
    }
    
    protected void deleteEntry(final BasicPoolEntry entry) {
        final HttpRoute route = entry.getPlannedRoute();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Deleting connection [" + route + "][" + entry.getState() + "]");
        }
        this.poolLock.lock();
        try {
            this.closeConnection(entry);
            final RouteSpecificPool rospl = this.getRoutePool(route, true);
            rospl.deleteEntry(entry);
            --this.numConnections;
            if (rospl.isUnused()) {
                this.routeToPool.remove(route);
            }
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    protected void deleteLeastUsedEntry() {
        this.poolLock.lock();
        try {
            final BasicPoolEntry entry = this.freeConnections.remove();
            if (entry != null) {
                this.deleteEntry(entry);
            }
            else if (this.log.isDebugEnabled()) {
                this.log.debug("No free connection to delete");
            }
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    @Override
    protected void handleLostEntry(final HttpRoute route) {
        this.poolLock.lock();
        try {
            final RouteSpecificPool rospl = this.getRoutePool(route, true);
            rospl.dropEntry();
            if (rospl.isUnused()) {
                this.routeToPool.remove(route);
            }
            --this.numConnections;
            this.notifyWaitingThread(rospl);
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    protected void notifyWaitingThread(final RouteSpecificPool rospl) {
        WaitingThread waitingThread = null;
        this.poolLock.lock();
        try {
            if (rospl != null && rospl.hasThread()) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Notifying thread waiting on pool [" + rospl.getRoute() + "]");
                }
                waitingThread = rospl.nextThread();
            }
            else if (!this.waitingThreads.isEmpty()) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Notifying thread waiting on any pool");
                }
                waitingThread = this.waitingThreads.remove();
            }
            else if (this.log.isDebugEnabled()) {
                this.log.debug("Notifying no-one, there are no waiting threads");
            }
            if (waitingThread != null) {
                waitingThread.wakeup();
            }
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    @Override
    public void deleteClosedConnections() {
        this.poolLock.lock();
        try {
            final Iterator<BasicPoolEntry> iter = this.freeConnections.iterator();
            while (iter.hasNext()) {
                final BasicPoolEntry entry = iter.next();
                if (!entry.getConnection().isOpen()) {
                    iter.remove();
                    this.deleteEntry(entry);
                }
            }
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    @Override
    public void closeIdleConnections(final long idletime, final TimeUnit tunit) {
        Args.notNull(tunit, "Time unit");
        final long t = (idletime > 0L) ? idletime : 0L;
        if (this.log.isDebugEnabled()) {
            this.log.debug("Closing connections idle longer than " + t + " " + tunit);
        }
        final long deadline = System.currentTimeMillis() - tunit.toMillis(t);
        this.poolLock.lock();
        try {
            final Iterator<BasicPoolEntry> iter = this.freeConnections.iterator();
            while (iter.hasNext()) {
                final BasicPoolEntry entry = iter.next();
                if (entry.getUpdated() <= deadline) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Closing connection last used @ " + new Date(entry.getUpdated()));
                    }
                    iter.remove();
                    this.deleteEntry(entry);
                }
            }
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    @Override
    public void closeExpiredConnections() {
        this.log.debug("Closing expired connections");
        final long now = System.currentTimeMillis();
        this.poolLock.lock();
        try {
            final Iterator<BasicPoolEntry> iter = this.freeConnections.iterator();
            while (iter.hasNext()) {
                final BasicPoolEntry entry = iter.next();
                if (entry.isExpired(now)) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Closing connection expired @ " + new Date(entry.getExpiry()));
                    }
                    iter.remove();
                    this.deleteEntry(entry);
                }
            }
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    @Override
    public void shutdown() {
        this.poolLock.lock();
        try {
            if (this.shutdown) {
                return;
            }
            this.shutdown = true;
            final Iterator<BasicPoolEntry> iter1 = this.leasedConnections.iterator();
            while (iter1.hasNext()) {
                final BasicPoolEntry entry = iter1.next();
                iter1.remove();
                this.closeConnection(entry);
            }
            final Iterator<BasicPoolEntry> iter2 = this.freeConnections.iterator();
            while (iter2.hasNext()) {
                final BasicPoolEntry entry2 = iter2.next();
                iter2.remove();
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Closing connection [" + entry2.getPlannedRoute() + "][" + entry2.getState() + "]");
                }
                this.closeConnection(entry2);
            }
            final Iterator<WaitingThread> iwth = this.waitingThreads.iterator();
            while (iwth.hasNext()) {
                final WaitingThread waiter = iwth.next();
                iwth.remove();
                waiter.wakeup();
            }
            this.routeToPool.clear();
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    public void setMaxTotalConnections(final int max) {
        this.poolLock.lock();
        try {
            this.maxTotalConnections = max;
        }
        finally {
            this.poolLock.unlock();
        }
    }
    
    public int getMaxTotalConnections() {
        return this.maxTotalConnections;
    }
}
