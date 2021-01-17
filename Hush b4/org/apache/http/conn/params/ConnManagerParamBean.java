// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.params;

import org.apache.http.params.HttpParams;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.HttpAbstractParamBean;

@Deprecated
@NotThreadSafe
public class ConnManagerParamBean extends HttpAbstractParamBean
{
    public ConnManagerParamBean(final HttpParams params) {
        super(params);
    }
    
    public void setTimeout(final long timeout) {
        this.params.setLongParameter("http.conn-manager.timeout", timeout);
    }
    
    public void setMaxTotalConnections(final int maxConnections) {
        this.params.setIntParameter("http.conn-manager.max-total", maxConnections);
    }
    
    public void setConnectionsPerRoute(final ConnPerRouteBean connPerRoute) {
        this.params.setParameter("http.conn-manager.max-per-route", connPerRoute);
    }
}
