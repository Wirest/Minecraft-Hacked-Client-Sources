// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn.tsccm;

import org.apache.http.conn.OperatedClientConnection;
import java.util.concurrent.TimeUnit;
import org.apache.http.util.Args;
import java.lang.ref.ReferenceQueue;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.impl.conn.AbstractPoolEntry;

@Deprecated
public class BasicPoolEntry extends AbstractPoolEntry
{
    private final long created;
    private long updated;
    private final long validUntil;
    private long expiry;
    
    public BasicPoolEntry(final ClientConnectionOperator op, final HttpRoute route, final ReferenceQueue<Object> queue) {
        super(op, route);
        Args.notNull(route, "HTTP route");
        this.created = System.currentTimeMillis();
        this.validUntil = Long.MAX_VALUE;
        this.expiry = this.validUntil;
    }
    
    public BasicPoolEntry(final ClientConnectionOperator op, final HttpRoute route) {
        this(op, route, -1L, TimeUnit.MILLISECONDS);
    }
    
    public BasicPoolEntry(final ClientConnectionOperator op, final HttpRoute route, final long connTTL, final TimeUnit timeunit) {
        super(op, route);
        Args.notNull(route, "HTTP route");
        this.created = System.currentTimeMillis();
        if (connTTL > 0L) {
            this.validUntil = this.created + timeunit.toMillis(connTTL);
        }
        else {
            this.validUntil = Long.MAX_VALUE;
        }
        this.expiry = this.validUntil;
    }
    
    protected final OperatedClientConnection getConnection() {
        return super.connection;
    }
    
    protected final HttpRoute getPlannedRoute() {
        return super.route;
    }
    
    protected final BasicPoolEntryRef getWeakRef() {
        return null;
    }
    
    @Override
    protected void shutdownEntry() {
        super.shutdownEntry();
    }
    
    public long getCreated() {
        return this.created;
    }
    
    public long getUpdated() {
        return this.updated;
    }
    
    public long getExpiry() {
        return this.expiry;
    }
    
    public long getValidUntil() {
        return this.validUntil;
    }
    
    public void updateExpiry(final long time, final TimeUnit timeunit) {
        this.updated = System.currentTimeMillis();
        long newExpiry;
        if (time > 0L) {
            newExpiry = this.updated + timeunit.toMillis(time);
        }
        else {
            newExpiry = Long.MAX_VALUE;
        }
        this.expiry = Math.min(this.validUntil, newExpiry);
    }
    
    public boolean isExpired(final long now) {
        return now >= this.expiry;
    }
}
