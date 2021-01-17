// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpRequestInterceptor;

@Immutable
public class RequestAcceptEncoding implements HttpRequestInterceptor
{
    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        if (!request.containsHeader("Accept-Encoding")) {
            request.addHeader("Accept-Encoding", "gzip,deflate");
        }
    }
}
