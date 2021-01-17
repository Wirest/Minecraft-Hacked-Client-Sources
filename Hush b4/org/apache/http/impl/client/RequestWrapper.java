// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.message.BasicRequestLine;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.RequestLine;
import java.net.URISyntaxException;
import org.apache.http.ProtocolException;
import org.apache.http.util.Args;
import org.apache.http.ProtocolVersion;
import java.net.URI;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.AbstractHttpMessage;

@Deprecated
@NotThreadSafe
public class RequestWrapper extends AbstractHttpMessage implements HttpUriRequest
{
    private final HttpRequest original;
    private URI uri;
    private String method;
    private ProtocolVersion version;
    private int execCount;
    
    public RequestWrapper(final HttpRequest request) throws ProtocolException {
        Args.notNull(request, "HTTP request");
        this.original = request;
        this.setParams(request.getParams());
        this.setHeaders(request.getAllHeaders());
        if (request instanceof HttpUriRequest) {
            this.uri = ((HttpUriRequest)request).getURI();
            this.method = ((HttpUriRequest)request).getMethod();
            this.version = null;
        }
        else {
            final RequestLine requestLine = request.getRequestLine();
            try {
                this.uri = new URI(requestLine.getUri());
            }
            catch (URISyntaxException ex) {
                throw new ProtocolException("Invalid request URI: " + requestLine.getUri(), ex);
            }
            this.method = requestLine.getMethod();
            this.version = request.getProtocolVersion();
        }
        this.execCount = 0;
    }
    
    public void resetHeaders() {
        this.headergroup.clear();
        this.setHeaders(this.original.getAllHeaders());
    }
    
    public String getMethod() {
        return this.method;
    }
    
    public void setMethod(final String method) {
        Args.notNull(method, "Method name");
        this.method = method;
    }
    
    public ProtocolVersion getProtocolVersion() {
        if (this.version == null) {
            this.version = HttpProtocolParams.getVersion(this.getParams());
        }
        return this.version;
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
    
    public RequestLine getRequestLine() {
        final String method = this.getMethod();
        final ProtocolVersion ver = this.getProtocolVersion();
        String uritext = null;
        if (this.uri != null) {
            uritext = this.uri.toASCIIString();
        }
        if (uritext == null || uritext.length() == 0) {
            uritext = "/";
        }
        return new BasicRequestLine(method, uritext, ver);
    }
    
    public void abort() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    public boolean isAborted() {
        return false;
    }
    
    public HttpRequest getOriginal() {
        return this.original;
    }
    
    public boolean isRepeatable() {
        return true;
    }
    
    public int getExecCount() {
        return this.execCount;
    }
    
    public void incrementExecCount() {
        ++this.execCount;
    }
}
