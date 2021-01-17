// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.methods;

import org.apache.http.message.BasicRequestLine;
import org.apache.http.RequestLine;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.client.config.RequestConfig;
import java.net.URI;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public abstract class HttpRequestBase extends AbstractExecutionAwareRequest implements HttpUriRequest, Configurable
{
    private ProtocolVersion version;
    private URI uri;
    private RequestConfig config;
    
    public abstract String getMethod();
    
    public void setProtocolVersion(final ProtocolVersion version) {
        this.version = version;
    }
    
    public ProtocolVersion getProtocolVersion() {
        return (this.version != null) ? this.version : HttpProtocolParams.getVersion(this.getParams());
    }
    
    public URI getURI() {
        return this.uri;
    }
    
    public RequestLine getRequestLine() {
        final String method = this.getMethod();
        final ProtocolVersion ver = this.getProtocolVersion();
        final URI uri = this.getURI();
        String uritext = null;
        if (uri != null) {
            uritext = uri.toASCIIString();
        }
        if (uritext == null || uritext.length() == 0) {
            uritext = "/";
        }
        return new BasicRequestLine(method, uritext, ver);
    }
    
    public RequestConfig getConfig() {
        return this.config;
    }
    
    public void setConfig(final RequestConfig config) {
        this.config = config;
    }
    
    public void setURI(final URI uri) {
        this.uri = uri;
    }
    
    public void started() {
    }
    
    public void releaseConnection() {
        this.reset();
    }
    
    @Override
    public String toString() {
        return this.getMethod() + " " + this.getURI() + " " + this.getProtocolVersion();
    }
}
