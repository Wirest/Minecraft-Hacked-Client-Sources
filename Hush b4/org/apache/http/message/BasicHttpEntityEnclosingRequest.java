// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.message;

import org.apache.http.Header;
import org.apache.http.RequestLine;
import org.apache.http.ProtocolVersion;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HttpEntityEnclosingRequest;

@NotThreadSafe
public class BasicHttpEntityEnclosingRequest extends BasicHttpRequest implements HttpEntityEnclosingRequest
{
    private HttpEntity entity;
    
    public BasicHttpEntityEnclosingRequest(final String method, final String uri) {
        super(method, uri);
    }
    
    public BasicHttpEntityEnclosingRequest(final String method, final String uri, final ProtocolVersion ver) {
        super(method, uri, ver);
    }
    
    public BasicHttpEntityEnclosingRequest(final RequestLine requestline) {
        super(requestline);
    }
    
    public HttpEntity getEntity() {
        return this.entity;
    }
    
    public void setEntity(final HttpEntity entity) {
        this.entity = entity;
    }
    
    public boolean expectContinue() {
        final Header expect = this.getFirstHeader("Expect");
        return expect != null && "100-continue".equalsIgnoreCase(expect.getValue());
    }
}
