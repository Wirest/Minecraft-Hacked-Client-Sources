// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpPut extends HttpEntityEnclosingRequestBase
{
    public static final String METHOD_NAME = "PUT";
    
    public HttpPut() {
    }
    
    public HttpPut(final URI uri) {
        this.setURI(uri);
    }
    
    public HttpPut(final String uri) {
        this.setURI(URI.create(uri));
    }
    
    @Override
    public String getMethod() {
        return "PUT";
    }
}
