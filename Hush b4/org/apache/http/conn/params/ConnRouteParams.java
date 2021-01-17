// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.params;

import java.net.InetAddress;
import org.apache.http.util.Args;
import org.apache.http.params.HttpParams;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;

@Deprecated
@Immutable
public class ConnRouteParams implements ConnRoutePNames
{
    public static final HttpHost NO_HOST;
    public static final HttpRoute NO_ROUTE;
    
    private ConnRouteParams() {
    }
    
    public static HttpHost getDefaultProxy(final HttpParams params) {
        Args.notNull(params, "Parameters");
        HttpHost proxy = (HttpHost)params.getParameter("http.route.default-proxy");
        if (proxy != null && ConnRouteParams.NO_HOST.equals(proxy)) {
            proxy = null;
        }
        return proxy;
    }
    
    public static void setDefaultProxy(final HttpParams params, final HttpHost proxy) {
        Args.notNull(params, "Parameters");
        params.setParameter("http.route.default-proxy", proxy);
    }
    
    public static HttpRoute getForcedRoute(final HttpParams params) {
        Args.notNull(params, "Parameters");
        HttpRoute route = (HttpRoute)params.getParameter("http.route.forced-route");
        if (route != null && ConnRouteParams.NO_ROUTE.equals(route)) {
            route = null;
        }
        return route;
    }
    
    public static void setForcedRoute(final HttpParams params, final HttpRoute route) {
        Args.notNull(params, "Parameters");
        params.setParameter("http.route.forced-route", route);
    }
    
    public static InetAddress getLocalAddress(final HttpParams params) {
        Args.notNull(params, "Parameters");
        final InetAddress local = (InetAddress)params.getParameter("http.route.local-address");
        return local;
    }
    
    public static void setLocalAddress(final HttpParams params, final InetAddress local) {
        Args.notNull(params, "Parameters");
        params.setParameter("http.route.local-address", local);
    }
    
    static {
        NO_HOST = new HttpHost("127.0.0.255", 0, "no-host");
        NO_ROUTE = new HttpRoute(ConnRouteParams.NO_HOST);
    }
}
