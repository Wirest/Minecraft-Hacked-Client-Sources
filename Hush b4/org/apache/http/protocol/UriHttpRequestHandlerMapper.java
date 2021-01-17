// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import org.apache.http.HttpRequest;
import org.apache.http.util.Args;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public class UriHttpRequestHandlerMapper implements HttpRequestHandlerMapper
{
    private final UriPatternMatcher<HttpRequestHandler> matcher;
    
    protected UriHttpRequestHandlerMapper(final UriPatternMatcher<HttpRequestHandler> matcher) {
        this.matcher = Args.notNull(matcher, "Pattern matcher");
    }
    
    public UriHttpRequestHandlerMapper() {
        this(new UriPatternMatcher<HttpRequestHandler>());
    }
    
    public void register(final String pattern, final HttpRequestHandler handler) {
        Args.notNull(pattern, "Pattern");
        Args.notNull(handler, "Handler");
        this.matcher.register(pattern, handler);
    }
    
    public void unregister(final String pattern) {
        this.matcher.unregister(pattern);
    }
    
    protected String getRequestPath(final HttpRequest request) {
        String uriPath = request.getRequestLine().getUri();
        int index = uriPath.indexOf("?");
        if (index != -1) {
            uriPath = uriPath.substring(0, index);
        }
        else {
            index = uriPath.indexOf("#");
            if (index != -1) {
                uriPath = uriPath.substring(0, index);
            }
        }
        return uriPath;
    }
    
    public HttpRequestHandler lookup(final HttpRequest request) {
        Args.notNull(request, "HTTP request");
        return this.matcher.lookup(this.getRequestPath(request));
    }
}
