// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import java.net.InetAddress;
import org.apache.http.HttpConnection;
import org.apache.http.ProtocolException;
import org.apache.http.HttpHost;
import org.apache.http.HttpInetConnection;
import org.apache.http.ProtocolVersion;
import org.apache.http.HttpVersion;
import org.apache.http.util.Args;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpRequestInterceptor;

@Immutable
public class RequestTargetHost implements HttpRequestInterceptor
{
    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        final HttpCoreContext corecontext = HttpCoreContext.adapt(context);
        final ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
        final String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT") && ver.lessEquals(HttpVersion.HTTP_1_0)) {
            return;
        }
        if (!request.containsHeader("Host")) {
            HttpHost targethost = corecontext.getTargetHost();
            if (targethost == null) {
                final HttpConnection conn = corecontext.getConnection();
                if (conn instanceof HttpInetConnection) {
                    final InetAddress address = ((HttpInetConnection)conn).getRemoteAddress();
                    final int port = ((HttpInetConnection)conn).getRemotePort();
                    if (address != null) {
                        targethost = new HttpHost(address.getHostName(), port);
                    }
                }
                if (targethost == null) {
                    if (ver.lessEquals(HttpVersion.HTTP_1_0)) {
                        return;
                    }
                    throw new ProtocolException("Target host missing");
                }
            }
            request.addHeader("Host", targethost.toHostString());
        }
    }
}
