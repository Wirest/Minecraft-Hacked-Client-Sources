// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.params;

import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;
import org.apache.http.params.HttpParams;
import org.apache.http.annotation.Immutable;

@Deprecated
@Immutable
public final class ConnManagerParams implements ConnManagerPNames
{
    public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 20;
    private static final ConnPerRoute DEFAULT_CONN_PER_ROUTE;
    
    @Deprecated
    public static long getTimeout(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getLongParameter("http.conn-manager.timeout", 0L);
    }
    
    @Deprecated
    public static void setTimeout(final HttpParams params, final long timeout) {
        Args.notNull(params, "HTTP parameters");
        params.setLongParameter("http.conn-manager.timeout", timeout);
    }
    
    public static void setMaxConnectionsPerRoute(final HttpParams params, final ConnPerRoute connPerRoute) {
        Args.notNull(params, "HTTP parameters");
        params.setParameter("http.conn-manager.max-per-route", connPerRoute);
    }
    
    public static ConnPerRoute getMaxConnectionsPerRoute(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        ConnPerRoute connPerRoute = (ConnPerRoute)params.getParameter("http.conn-manager.max-per-route");
        if (connPerRoute == null) {
            connPerRoute = ConnManagerParams.DEFAULT_CONN_PER_ROUTE;
        }
        return connPerRoute;
    }
    
    public static void setMaxTotalConnections(final HttpParams params, final int maxTotalConnections) {
        Args.notNull(params, "HTTP parameters");
        params.setIntParameter("http.conn-manager.max-total", maxTotalConnections);
    }
    
    public static int getMaxTotalConnections(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getIntParameter("http.conn-manager.max-total", 20);
    }
    
    static {
        DEFAULT_CONN_PER_ROUTE = new ConnPerRoute() {
            public int getMaxForRoute(final HttpRoute route) {
                return 2;
            }
        };
    }
}
