// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.util.Args;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpResponseInterceptor;

@Immutable
public class ResponseServer implements HttpResponseInterceptor
{
    private final String originServer;
    
    public ResponseServer(final String originServer) {
        this.originServer = originServer;
    }
    
    public ResponseServer() {
        this(null);
    }
    
    public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
        Args.notNull(response, "HTTP response");
        if (!response.containsHeader("Server") && this.originServer != null) {
            response.addHeader("Server", this.originServer);
        }
    }
}
