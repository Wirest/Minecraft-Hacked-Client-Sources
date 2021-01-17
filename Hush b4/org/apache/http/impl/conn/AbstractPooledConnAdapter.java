// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.HttpHost;
import java.io.IOException;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.ClientConnectionManager;

@Deprecated
public abstract class AbstractPooledConnAdapter extends AbstractClientConnAdapter
{
    protected volatile AbstractPoolEntry poolEntry;
    
    protected AbstractPooledConnAdapter(final ClientConnectionManager manager, final AbstractPoolEntry entry) {
        super(manager, entry.connection);
        this.poolEntry = entry;
    }
    
    public String getId() {
        return null;
    }
    
    @Deprecated
    protected AbstractPoolEntry getPoolEntry() {
        return this.poolEntry;
    }
    
    protected void assertValid(final AbstractPoolEntry entry) {
        if (this.isReleased() || entry == null) {
            throw new ConnectionShutdownException();
        }
    }
    
    @Deprecated
    protected final void assertAttached() {
        if (this.poolEntry == null) {
            throw new ConnectionShutdownException();
        }
    }
    
    @Override
    protected synchronized void detach() {
        this.poolEntry = null;
        super.detach();
    }
    
    public HttpRoute getRoute() {
        final AbstractPoolEntry entry = this.getPoolEntry();
        this.assertValid(entry);
        return (entry.tracker == null) ? null : entry.tracker.toRoute();
    }
    
    public void open(final HttpRoute route, final HttpContext context, final HttpParams params) throws IOException {
        final AbstractPoolEntry entry = this.getPoolEntry();
        this.assertValid(entry);
        entry.open(route, context, params);
    }
    
    public void tunnelTarget(final boolean secure, final HttpParams params) throws IOException {
        final AbstractPoolEntry entry = this.getPoolEntry();
        this.assertValid(entry);
        entry.tunnelTarget(secure, params);
    }
    
    public void tunnelProxy(final HttpHost next, final boolean secure, final HttpParams params) throws IOException {
        final AbstractPoolEntry entry = this.getPoolEntry();
        this.assertValid(entry);
        entry.tunnelProxy(next, secure, params);
    }
    
    public void layerProtocol(final HttpContext context, final HttpParams params) throws IOException {
        final AbstractPoolEntry entry = this.getPoolEntry();
        this.assertValid(entry);
        entry.layerProtocol(context, params);
    }
    
    public void close() throws IOException {
        final AbstractPoolEntry entry = this.getPoolEntry();
        if (entry != null) {
            entry.shutdownEntry();
        }
        final OperatedClientConnection conn = this.getWrappedConnection();
        if (conn != null) {
            conn.close();
        }
    }
    
    public void shutdown() throws IOException {
        final AbstractPoolEntry entry = this.getPoolEntry();
        if (entry != null) {
            entry.shutdownEntry();
        }
        final OperatedClientConnection conn = this.getWrappedConnection();
        if (conn != null) {
            conn.shutdown();
        }
    }
    
    public Object getState() {
        final AbstractPoolEntry entry = this.getPoolEntry();
        this.assertValid(entry);
        return entry.getState();
    }
    
    public void setState(final Object state) {
        final AbstractPoolEntry entry = this.getPoolEntry();
        this.assertValid(entry);
        entry.setState(state);
    }
}
