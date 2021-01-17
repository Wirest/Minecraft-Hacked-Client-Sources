// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.util.Args;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpRequestInterceptor;

@Immutable
public class RequestContent implements HttpRequestInterceptor
{
    private final boolean overwrite;
    
    public RequestContent() {
        this(false);
    }
    
    public RequestContent(final boolean overwrite) {
        this.overwrite = overwrite;
    }
    
    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        if (request instanceof HttpEntityEnclosingRequest) {
            if (this.overwrite) {
                request.removeHeaders("Transfer-Encoding");
                request.removeHeaders("Content-Length");
            }
            else {
                if (request.containsHeader("Transfer-Encoding")) {
                    throw new ProtocolException("Transfer-encoding header already present");
                }
                if (request.containsHeader("Content-Length")) {
                    throw new ProtocolException("Content-Length header already present");
                }
            }
            final ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
            final HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
            if (entity == null) {
                request.addHeader("Content-Length", "0");
                return;
            }
            if (entity.isChunked() || entity.getContentLength() < 0L) {
                if (ver.lessEquals(HttpVersion.HTTP_1_0)) {
                    throw new ProtocolException("Chunked transfer encoding not allowed for " + ver);
                }
                request.addHeader("Transfer-Encoding", "chunked");
            }
            else {
                request.addHeader("Content-Length", Long.toString(entity.getContentLength()));
            }
            if (entity.getContentType() != null && !request.containsHeader("Content-Type")) {
                request.addHeader(entity.getContentType());
            }
            if (entity.getContentEncoding() != null && !request.containsHeader("Content-Encoding")) {
                request.addHeader(entity.getContentEncoding());
            }
        }
    }
}
