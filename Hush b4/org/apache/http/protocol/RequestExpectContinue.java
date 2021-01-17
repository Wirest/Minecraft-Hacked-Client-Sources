// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.HttpVersion;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.util.Args;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpRequestInterceptor;

@Immutable
public class RequestExpectContinue implements HttpRequestInterceptor
{
    private final boolean activeByDefault;
    
    @Deprecated
    public RequestExpectContinue() {
        this(false);
    }
    
    public RequestExpectContinue(final boolean activeByDefault) {
        this.activeByDefault = activeByDefault;
    }
    
    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        if (!request.containsHeader("Expect") && request instanceof HttpEntityEnclosingRequest) {
            final ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
            final HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
            if (entity != null && entity.getContentLength() != 0L && !ver.lessEquals(HttpVersion.HTTP_1_0)) {
                final boolean active = request.getParams().getBooleanParameter("http.protocol.expect-continue", this.activeByDefault);
                if (active) {
                    request.addHeader("Expect", "100-continue");
                }
            }
        }
    }
}
