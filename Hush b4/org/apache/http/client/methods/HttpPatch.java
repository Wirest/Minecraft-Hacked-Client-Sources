// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpPatch extends HttpEntityEnclosingRequestBase
{
    public static final String METHOD_NAME = "PATCH";
    
    public HttpPatch() {
    }
    
    public HttpPatch(final URI uri) {
        this.setURI(uri);
    }
    
    public HttpPatch(final String uri) {
        this.setURI(URI.create(uri));
    }
    
    @Override
    public String getMethod() {
        return "PATCH";
    }
}
