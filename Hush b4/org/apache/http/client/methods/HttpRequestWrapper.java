// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.methods;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.params.HttpParams;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.RequestLine;
import java.net.URI;
import org.apache.http.ProtocolVersion;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.message.AbstractHttpMessage;

@NotThreadSafe
public class HttpRequestWrapper extends AbstractHttpMessage implements HttpUriRequest
{
    private final HttpRequest original;
    private final String method;
    private ProtocolVersion version;
    private URI uri;
    
    private HttpRequestWrapper(final HttpRequest request) {
        this.original = request;
        this.version = this.original.getRequestLine().getProtocolVersion();
        this.method = this.original.getRequestLine().getMethod();
        if (request instanceof HttpUriRequest) {
            this.uri = ((HttpUriRequest)request).getURI();
        }
        else {
            this.uri = null;
        }
        this.setHeaders(request.getAllHeaders());
    }
    
    public ProtocolVersion getProtocolVersion() {
        return (this.version != null) ? this.version : this.original.getProtocolVersion();
    }
    
    public void setProtocolVersion(final ProtocolVersion version) {
        this.version = version;
    }
    
    public URI getURI() {
        return this.uri;
    }
    
    public void setURI(final URI uri) {
        this.uri = uri;
    }
    
    public String getMethod() {
        return this.method;
    }
    
    public void abort() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    public boolean isAborted() {
        return false;
    }
    
    public RequestLine getRequestLine() {
        String requestUri = null;
        if (this.uri != null) {
            requestUri = this.uri.toASCIIString();
        }
        else {
            requestUri = this.original.getRequestLine().getUri();
        }
        if (requestUri == null || requestUri.length() == 0) {
            requestUri = "/";
        }
        return new BasicRequestLine(this.method, requestUri, this.getProtocolVersion());
    }
    
    public HttpRequest getOriginal() {
        return this.original;
    }
    
    @Override
    public String toString() {
        return this.getRequestLine() + " " + this.headergroup;
    }
    
    public static HttpRequestWrapper wrap(final HttpRequest request) {
        if (request == null) {
            return null;
        }
        if (request instanceof HttpEntityEnclosingRequest) {
            return new HttpEntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)request);
        }
        return new HttpRequestWrapper(request);
    }
    
    @Deprecated
    @Override
    public HttpParams getParams() {
        if (this.params == null) {
            this.params = this.original.getParams().copy();
        }
        return this.params;
    }
    
    static class HttpEntityEnclosingRequestWrapper extends HttpRequestWrapper implements HttpEntityEnclosingRequest
    {
        private HttpEntity entity;
        
        public HttpEntityEnclosingRequestWrapper(final HttpEntityEnclosingRequest request) {
            super(request, null);
            this.entity = request.getEntity();
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
}
