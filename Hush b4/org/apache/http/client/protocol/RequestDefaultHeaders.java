// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import java.util.Iterator;
import org.apache.http.util.Args;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.http.Header;
import java.util.Collection;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpRequestInterceptor;

@Immutable
public class RequestDefaultHeaders implements HttpRequestInterceptor
{
    private final Collection<? extends Header> defaultHeaders;
    
    public RequestDefaultHeaders(final Collection<? extends Header> defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
    }
    
    public RequestDefaultHeaders() {
        this(null);
    }
    
    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        final String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT")) {
            return;
        }
        Collection<? extends Header> defHeaders = (Collection<? extends Header>)request.getParams().getParameter("http.default-headers");
        if (defHeaders == null) {
            defHeaders = this.defaultHeaders;
        }
        if (defHeaders != null) {
            for (final Header defHeader : defHeaders) {
                request.addHeader(defHeader);
            }
        }
    }
}
