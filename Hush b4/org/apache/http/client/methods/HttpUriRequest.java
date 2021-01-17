// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.HttpRequest;

public interface HttpUriRequest extends HttpRequest
{
    String getMethod();
    
    URI getURI();
    
    void abort() throws UnsupportedOperationException;
    
    boolean isAborted();
}
