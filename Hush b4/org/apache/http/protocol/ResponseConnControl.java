// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpEntity;
import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.HttpVersion;
import org.apache.http.util.Args;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpResponseInterceptor;

@Immutable
public class ResponseConnControl implements HttpResponseInterceptor
{
    public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
        Args.notNull(response, "HTTP response");
        final HttpCoreContext corecontext = HttpCoreContext.adapt(context);
        final int status = response.getStatusLine().getStatusCode();
        if (status == 400 || status == 408 || status == 411 || status == 413 || status == 414 || status == 503 || status == 501) {
            response.setHeader("Connection", "Close");
            return;
        }
        final Header explicit = response.getFirstHeader("Connection");
        if (explicit != null && "Close".equalsIgnoreCase(explicit.getValue())) {
            return;
        }
        final HttpEntity entity = response.getEntity();
        if (entity != null) {
            final ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
            if (entity.getContentLength() < 0L && (!entity.isChunked() || ver.lessEquals(HttpVersion.HTTP_1_0))) {
                response.setHeader("Connection", "Close");
                return;
            }
        }
        final HttpRequest request = corecontext.getRequest();
        if (request != null) {
            final Header header = request.getFirstHeader("Connection");
            if (header != null) {
                response.setHeader("Connection", header.getValue());
            }
            else if (request.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
                response.setHeader("Connection", "Close");
            }
        }
    }
}
