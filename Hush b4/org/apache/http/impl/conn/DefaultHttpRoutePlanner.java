// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import org.apache.http.conn.scheme.Scheme;
import java.net.InetAddress;
import org.apache.http.HttpException;
import org.apache.http.util.Asserts;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.http.HttpHost;
import org.apache.http.util.Args;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.routing.HttpRoutePlanner;

@Deprecated
@ThreadSafe
public class DefaultHttpRoutePlanner implements HttpRoutePlanner
{
    protected final SchemeRegistry schemeRegistry;
    
    public DefaultHttpRoutePlanner(final SchemeRegistry schreg) {
        Args.notNull(schreg, "Scheme registry");
        this.schemeRegistry = schreg;
    }
    
    public HttpRoute determineRoute(final HttpHost target, final HttpRequest request, final HttpContext context) throws HttpException {
        Args.notNull(request, "HTTP request");
        HttpRoute route = ConnRouteParams.getForcedRoute(request.getParams());
        if (route != null) {
            return route;
        }
        Asserts.notNull(target, "Target host");
        final InetAddress local = ConnRouteParams.getLocalAddress(request.getParams());
        final HttpHost proxy = ConnRouteParams.getDefaultProxy(request.getParams());
        Scheme schm;
        try {
            schm = this.schemeRegistry.getScheme(target.getSchemeName());
        }
        catch (IllegalStateException ex) {
            throw new HttpException(ex.getMessage());
        }
        final boolean secure = schm.isLayered();
        if (proxy == null) {
            route = new HttpRoute(target, local, secure);
        }
        else {
            route = new HttpRoute(target, local, proxy, secure);
        }
        return route;
    }
}
