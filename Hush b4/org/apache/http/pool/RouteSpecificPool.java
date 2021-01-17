// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.pool;

import org.apache.http.util.Asserts;
import org.apache.http.util.Args;
import java.util.Iterator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
abstract class RouteSpecificPool<T, C, E extends PoolEntry<T, C>>
{
    private final T route;
    private final Set<E> leased;
    private final LinkedList<E> available;
    private final LinkedList<PoolEntryFuture<E>> pending;
    
    RouteSpecificPool(final T route) {
        this.route = route;
        this.leased = new HashSet<E>();
        this.available = new LinkedList<E>();
        this.pending = new LinkedList<PoolEntryFuture<E>>();
    }
    
    protected abstract E createEntry(final C p0);
    
    public final T getRoute() {
        return this.route;
    }
    
    public int getLeasedCount() {
        return this.leased.size();
    }
    
    public int getPendingCount() {
        return this.pending.size();
    }
    
    public int getAvailableCount() {
        return this.available.size();
    }
    
    public int getAllocatedCount() {
        return this.available.size() + this.leased.size();
    }
    
    public E getFree(final Object state) {
        if (!this.available.isEmpty()) {
            if (state != null) {
                final Iterator<E> it = this.available.iterator();
                while (it.hasNext()) {
                    final E entry = it.next();
                    if (state.equals(entry.getState())) {
                        it.remove();
                        this.leased.add(entry);
                        return entry;
                    }
                }
            }
            final Iterator<E> it = this.available.iterator();
            while (it.hasNext()) {
                final E entry = it.next();
                if (entry.getState() == null) {
                    it.remove();
                    this.leased.add(entry);
                    return entry;
                }
            }
        }
        return null;
    }
    
    public E getLastUsed() {
        if (!this.available.isEmpty()) {
            return this.available.getLast();
        }
        return null;
    }
    
    public boolean remove(final E entry) {
        Args.notNull(entry, "Pool entry");
        return this.available.remove(entry) || this.leased.remove(entry);
    }
    
    public void free(final E entry, final boolean reusable) {
        Args.notNull(entry, "Pool entry");
        final boolean found = this.leased.remove(entry);
        Asserts.check(found, "Entry %s has not been leased from this pool", entry);
        if (reusable) {
            this.available.addFirst(entry);
        }
    }
    
    public E add(final C conn) {
        final E entry = this.createEntry(conn);
        this.leased.add(entry);
        return entry;
    }
    
    public void queue(final PoolEntryFuture<E> future) {
        if (future == null) {
            return;
        }
        this.pending.add(future);
    }
    
    public PoolEntryFuture<E> nextPending() {
        return this.pending.poll();
    }
    
    public void unqueue(final PoolEntryFuture<E> future) {
        if (future == null) {
            return;
        }
        this.pending.remove(future);
    }
    
    public void shutdown() {
        for (final PoolEntryFuture<E> future : this.pending) {
            future.cancel(true);
        }
        this.pending.clear();
        for (final E entry : this.available) {
            entry.close();
        }
        this.available.clear();
        for (final E entry : this.leased) {
            entry.close();
        }
        this.leased.clear();
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("[route: ");
        buffer.append(this.route);
        buffer.append("][leased: ");
        buffer.append(this.leased.size());
        buffer.append("][available: ");
        buffer.append(this.available.size());
        buffer.append("][pending: ");
        buffer.append(this.pending.size());
        buffer.append("]");
        return buffer.toString();
    }
}
