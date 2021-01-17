// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.util.Args;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpRequestInterceptor;

@Immutable
public class RequestClientConnControl implements HttpRequestInterceptor
{
    private final Log log;
    private static final String PROXY_CONN_DIRECTIVE = "Proxy-Connection";
    
    public RequestClientConnControl() {
        this.log = LogFactory.getLog(this.getClass());
    }
    
    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        final String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT")) {
            request.setHeader("Proxy-Connection", "Keep-Alive");
            return;
        }
        final HttpClientContext clientContext = HttpClientContext.adapt(context);
        final RouteInfo route = clientContext.getHttpRoute();
        if (route == null) {
            this.log.debug("Connection route not set in the context");
            return;
        }
        if ((route.getHopCount() == 1 || route.isTunnelled()) && !request.containsHeader("Connection")) {
            request.addHeader("Connection", "Keep-Alive");
        }
        if (route.getHopCount() == 2 && !route.isTunnelled() && !request.containsHeader("Proxy-Connection")) {
            request.addHeader("Proxy-Connection", "Keep-Alive");
        }
    }
}
