// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpGet extends HttpRequestBase
{
    public static final String METHOD_NAME = "GET";
    
    public HttpGet() {
    }
    
    public HttpGet(final URI uri) {
        this.setURI(uri);
    }
    
    public HttpGet(final String uri) {
        this.setURI(URI.create(uri));
    }
    
    @Override
    public String getMethod() {
        return "GET";
    }
}
