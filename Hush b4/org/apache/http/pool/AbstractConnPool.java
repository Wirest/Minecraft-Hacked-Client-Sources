// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.pool;

import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import org.apache.http.util.Asserts;
import java.util.concurrent.Future;
import org.apache.http.concurrent.FutureCallback;
import java.io.IOException;
import java.util.Iterator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.util.Args;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public abstract class AbstractConnPool<T, C, E extends PoolEntry<T, C>> implements ConnPool<T, E>, ConnPoolControl<T>
{
    private final Lock lock;
    private final ConnFactory<T, C> connFactory;
    private final Map<T, RouteSpecificPool<T, C, E>> routeToPool;
    private final Set<E> leased;
    private final LinkedList<E> available;
    private final LinkedList<PoolEntryFuture<E>> pending;
    private final Map<T, Integer> maxPerRoute;
    private volatile boolean isShutDown;
    private volatile int defaultMaxPerRoute;
    private volatile int maxTotal;
    
    public AbstractConnPool(final ConnFactory<T, C> connFactory, final int defaultMaxPerRoute, final int maxTotal) {
        this.connFactory = Args.notNull(connFactory, "Connection factory");
        this.defaultMaxPerRoute = Args.notNegative(defaultMaxPerRoute, "Max per route value");
        this.maxTotal = Args.notNegative(maxTotal, "Max total value");
        this.lock = new ReentrantLock();
        this.routeToPool = new HashMap<T, RouteSpecificPool<T, C, E>>();
        this.leased = new HashSet<E>();
        this.available = new LinkedList<E>();
        this.pending = new LinkedList<PoolEntryFuture<E>>();
        this.maxPerRoute = new HashMap<T, Integer>();
    }
    
    protected abstract E createEntry(final T p0, final C p1);
    
    protected void onLease(final E entry) {
    }
    
    protected void onRelease(final E entry) {
    }
    
    public boolean isShutdown() {
        return this.isShutDown;
    }
    
    public void shutdown() throws IOException {
        if (this.isShutDown) {
            return;
        }
        this.isShutDown = true;
        this.lock.lock();
        try {
            for (final E entry : this.available) {
                entry.close();
            }
            for (final E entry : this.leased) {
                entry.close();
            }
            for (final RouteSpecificPool<T, C, E> pool : this.routeToPool.values()) {
                pool.shutdown();
            }
            this.routeToPool.clear();
            this.leased.clear();
            this.available.clear();
        }
        finally {
            this.lock.unlock();
        }
    }
    
    private RouteSpecificPool<T, C, E> getPool(final T route) {
        RouteSpecificPool<T, C, E> pool = this.routeToPool.get(route);
        if (pool == null) {
            pool = new RouteSpecificPool<T, C, E>(route) {
                @Override
                protected E createEntry(final C conn) {
                    return AbstractConnPool.this.createEntry(route, conn);
                }
            };
            this.routeToPool.put(route, pool);
        }
        return pool;
    }
    
    public Future<E> lease(final T route, final Object state, final FutureCallback<E> callback) {
        Args.notNull(route, "Route");
        Asserts.check(!this.isShutDown, "Connection pool shut down");
        return new PoolEntryFuture<E>(this.lock, callback) {
            public E getPoolEntry(final long timeout, final TimeUnit tunit) throws InterruptedException, TimeoutException, IOException {
                final E entry = AbstractConnPool.this.getPoolEntryBlocking(route, state, timeout, tunit, this);
                AbstractConnPool.this.onLease(entry);
                return entry;
            }
        };
    }
    
    public Future<E> lease(final T route, final Object state) {
        return this.lease(route, state, null);
    }
    
    private E getPoolEntryBlocking(final T route, final Object state, final long timeout, final TimeUnit tunit, final PoolEntryFuture<E> future) throws IOException, InterruptedException, TimeoutException {
        Date deadline = null;
        if (timeout > 0L) {
            deadline = new Date(System.currentTimeMillis() + tunit.toMillis(timeout));
        }
        this.lock.lock();
        try {
            final RouteSpecificPool<T, C, E> pool = this.getPool(route);
            E entry = null;
            while (entry == null) {
                Asserts.check(!this.isShutDown, "Connection pool shut down");
                while (true) {
                    entry = pool.getFree(state);
                    if (entry == null) {
                        break;
                    }
                    if (!entry.isClosed() && !entry.isExpired(System.currentTimeMillis())) {
                        break;
                    }
                    entry.close();
                    this.available.remove(entry);
                    pool.free(entry, false);
                }
                if (entry != null) {
                    this.available.remove(entry);
                    this.leased.add(entry);
                    return entry;
                }
                final int maxPerRoute = this.getMax(route);
                final int excess = Math.max(0, pool.getAllocatedCount() + 1 - maxPerRoute);
                if (excess > 0) {
                    for (int i = 0; i < excess; ++i) {
                        final E lastUsed = pool.getLastUsed();
                        if (lastUsed == null) {
                            break;
                        }
                        lastUsed.close();
                        this.available.remove(lastUsed);
                        pool.remove(lastUsed);
                    }
                }
                if (pool.getAllocatedCount() < maxPerRoute) {
                    final int totalUsed = this.leased.size();
                    final int freeCapacity = Math.max(this.maxTotal - totalUsed, 0);
                    if (freeCapacity > 0) {
                        final int totalAvailable = this.available.size();
                        if (totalAvailable > freeCapacity - 1 && !this.available.isEmpty()) {
                            final E lastUsed2 = this.available.removeLast();
                            lastUsed2.close();
                            final RouteSpecificPool<T, C, E> otherpool = this.getPool(((PoolEntry<T, C>)lastUsed2).getRoute());
                            otherpool.remove(lastUsed2);
                        }
                        final C conn = this.connFactory.create(route);
                        entry = pool.add(conn);
                        this.leased.add(entry);
                        return entry;
                    }
                }
                boolean success = false;
                try {
                    pool.queue(future);
                    this.pending.add(future);
                    success = future.await(deadline);
                }
                finally {
                    pool.unqueue(future);
                    this.pending.remove(future);
                }
                if (!success && deadline != null && deadline.getTime() <= System.currentTimeMillis()) {
                    break;
                }
            }
            throw new TimeoutException("Timeout waiting for connection");
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public void release(final E entry, final boolean reusable) {
        this.lock.lock();
        try {
            if (this.leased.remove(entry)) {
                final RouteSpecificPool<T, C, E> pool = this.getPool(((PoolEntry<T, C>)entry).getRoute());
                pool.free(entry, reusable);
                if (reusable && !this.isShutDown) {
                    this.available.addFirst(entry);
                    this.onRelease(entry);
                }
                else {
                    entry.close();
                }
                PoolEntryFuture<E> future = pool.nextPending();
                if (future != null) {
                    this.pending.remove(future);
                }
                else {
                    future = this.pending.poll();
                }
                if (future != null) {
                    future.wakeup();
                }
            }
        }
        finally {
            this.lock.unlock();
        }
    }
    
    private int getMax(final T route) {
        final Integer v = this.maxPerRoute.get(route);
        if (v != null) {
            return v;
        }
        return this.defaultMaxPerRoute;
    }
    
    public void setMaxTotal(final int max) {
        Args.notNegative(max, "Max value");
        this.lock.lock();
        try {
            this.maxTotal = max;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public int getMaxTotal() {
        this.lock.lock();
        try {
            return this.maxTotal;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public void setDefaultMaxPerRoute(final int max) {
        Args.notNegative(max, "Max per route value");
        this.lock.lock();
        try {
            this.defaultMaxPerRoute = max;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public int getDefaultMaxPerRoute() {
        this.lock.lock();
        try {
            return this.defaultMaxPerRoute;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public void setMaxPerRoute(final T route, final int max) {
        Args.notNull(route, "Route");
        Args.notNegative(max, "Max per route value");
        this.lock.lock();
        try {
            this.maxPerRoute.put(route, max);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public int getMaxPerRoute(final T route) {
        Args.notNull(route, "Route");
        this.lock.lock();
        try {
            return this.getMax(route);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public PoolStats getTotalStats() {
        this.lock.lock();
        try {
            return new PoolStats(this.leased.size(), this.pending.size(), this.available.size(), this.maxTotal);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public PoolStats getStats(final T route) {
        Args.notNull(route, "Route");
        this.lock.lock();
        try {
            final RouteSpecificPool<T, C, E> pool = this.getPool(route);
            return new PoolStats(pool.getLeasedCount(), pool.getPendingCount(), pool.getAvailableCount(), this.getMax(route));
        }
        finally {
            this.lock.unlock();
        }
    }
    
    protected void enumAvailable(final PoolEntryCallback<T, C> callback) {
        this.lock.lock();
        try {
            final Iterator<E> it = this.available.iterator();
            while (it.hasNext()) {
                final E entry = it.next();
                callback.process(entry);
                if (entry.isClosed()) {
                    final RouteSpecificPool<T, C, E> pool = this.getPool(((PoolEntry<T, C>)entry).getRoute());
                    pool.remove(entry);
                    it.remove();
                }
            }
            this.purgePoolMap();
        }
        finally {
            this.lock.unlock();
        }
    }
    
    protected void enumLeased(final PoolEntryCallback<T, C> callback) {
        this.lock.lock();
        try {
            for (final E entry : this.leased) {
                callback.process(entry);
            }
        }
        finally {
            this.lock.unlock();
        }
    }
    
    private void purgePoolMap() {
        final Iterator<Map.Entry<T, RouteSpecificPool<T, C, E>>> it = this.routeToPool.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<T, RouteSpecificPool<T, C, E>> entry = it.next();
            final RouteSpecificPool<T, C, E> pool = entry.getValue();
            if (pool.getPendingCount() + pool.getAllocatedCount() == 0) {
                it.remove();
            }
        }
    }
    
    public void closeIdle(final long idletime, final TimeUnit tunit) {
        Args.notNull(tunit, "Time unit");
        long time = tunit.toMillis(idletime);
        if (time < 0L) {
            time = 0L;
        }
        final long deadline = System.currentTimeMillis() - time;
        this.enumAvailable(new PoolEntryCallback<T, C>() {
            public void process(final PoolEntry<T, C> entry) {
                if (entry.getUpdated() <= deadline) {
                    entry.close();
                }
            }
        });
    }
    
    public void closeExpired() {
        final long now = System.currentTimeMillis();
        this.enumAvailable(new PoolEntryCallback<T, C>() {
            public void process(final PoolEntry<T, C> entry) {
                if (entry.isExpired(now)) {
                    entry.close();
                }
            }
        });
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("[leased: ");
        buffer.append(this.leased);
        buffer.append("][available: ");
        buffer.append(this.available);
        buffer.append("][pending: ");
        buffer.append(this.pending);
        buffer.append("]");
        return buffer.toString();
    }
}
