// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpTrace extends HttpRequestBase
{
    public static final String METHOD_NAME = "TRACE";
    
    public HttpTrace() {
    }
    
    public HttpTrace(final URI uri) {
        this.setURI(uri);
    }
    
    public HttpTrace(final String uri) {
        this.setURI(URI.create(uri));
    }
    
    @Override
    public String getMethod() {
        return "TRACE";
    }
}
