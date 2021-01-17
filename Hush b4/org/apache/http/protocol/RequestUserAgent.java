// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpRequestInterceptor;

@Immutable
public class RequestUserAgent implements HttpRequestInterceptor
{
    private final String userAgent;
    
    public RequestUserAgent(final String userAgent) {
        this.userAgent = userAgent;
    }
    
    public RequestUserAgent() {
        this(null);
    }
    
    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        if (!request.containsHeader("User-Agent")) {
            String s = null;
            final HttpParams params = request.getParams();
            if (params != null) {
                s = (String)params.getParameter("http.useragent");
            }
            if (s == null) {
                s = this.userAgent;
            }
            if (s != null) {
                request.addHeader("User-Agent", s);
            }
        }
    }
}
