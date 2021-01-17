// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.methods;

import java.net.URISyntaxException;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.message.BasicNameValuePair;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HeaderIterator;
import org.apache.http.message.BasicHeader;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.util.Args;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.NameValuePair;
import java.util.LinkedList;
import org.apache.http.HttpEntity;
import org.apache.http.message.HeaderGroup;
import java.net.URI;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class RequestBuilder
{
    private String method;
    private ProtocolVersion version;
    private URI uri;
    private HeaderGroup headergroup;
    private HttpEntity entity;
    private LinkedList<NameValuePair> parameters;
    private RequestConfig config;
    
    RequestBuilder(final String method) {
        this.method = method;
    }
    
    RequestBuilder() {
        this(null);
    }
    
    public static RequestBuilder create(final String method) {
        Args.notBlank(method, "HTTP method");
        return new RequestBuilder(method);
    }
    
    public static RequestBuilder get() {
        return new RequestBuilder("GET");
    }
    
    public static RequestBuilder head() {
        return new RequestBuilder("HEAD");
    }
    
    public static RequestBuilder post() {
        return new RequestBuilder("POST");
    }
    
    public static RequestBuilder put() {
        return new RequestBuilder("PUT");
    }
    
    public static RequestBuilder delete() {
        return new RequestBuilder("DELETE");
    }
    
    public static RequestBuilder trace() {
        return new RequestBuilder("TRACE");
    }
    
    public static RequestBuilder options() {
        return new RequestBuilder("OPTIONS");
    }
    
    public static RequestBuilder copy(final HttpRequest request) {
        Args.notNull(request, "HTTP request");
        return new RequestBuilder().doCopy(request);
    }
    
    private RequestBuilder doCopy(final HttpRequest request) {
        if (request == null) {
            return this;
        }
        this.method = request.getRequestLine().getMethod();
        this.version = request.getRequestLine().getProtocolVersion();
        if (request instanceof HttpUriRequest) {
            this.uri = ((HttpUriRequest)request).getURI();
        }
        else {
            this.uri = URI.create(request.getRequestLine().getMethod());
        }
        if (this.headergroup == null) {
            this.headergroup = new HeaderGroup();
        }
        this.headergroup.clear();
        this.headergroup.setHeaders(request.getAllHeaders());
        if (request instanceof HttpEntityEnclosingRequest) {
            this.entity = ((HttpEntityEnclosingRequest)request).getEntity();
        }
        else {
            this.entity = null;
        }
        if (request instanceof Configurable) {
            this.config = ((Configurable)request).getConfig();
        }
        else {
            this.config = null;
        }
        this.parameters = null;
        return this;
    }
    
    public String getMethod() {
        return this.method;
    }
    
    public ProtocolVersion getVersion() {
        return this.version;
    }
    
    public RequestBuilder setVersion(final ProtocolVersion version) {
        this.version = version;
        return this;
    }
    
    public URI getUri() {
        return this.uri;
    }
    
    public RequestBuilder setUri(final URI uri) {
        this.uri = uri;
        return this;
    }
    
    public RequestBuilder setUri(final String uri) {
        this.uri = ((uri != null) ? URI.create(uri) : null);
        return this;
    }
    
    public Header getFirstHeader(final String name) {
        return (this.headergroup != null) ? this.headergroup.getFirstHeader(name) : null;
    }
    
    public Header getLastHeader(final String name) {
        return (this.headergroup != null) ? this.headergroup.getLastHeader(name) : null;
    }
    
    public Header[] getHeaders(final String name) {
        return (Header[])((this.headergroup != null) ? this.headergroup.getHeaders(name) : null);
    }
    
    public RequestBuilder addHeader(final Header header) {
        if (this.headergroup == null) {
            this.headergroup = new HeaderGroup();
        }
        this.headergroup.addHeader(header);
        return this;
    }
    
    public RequestBuilder addHeader(final String name, final String value) {
        if (this.headergroup == null) {
            this.headergroup = new HeaderGroup();
        }
        this.headergroup.addHeader(new BasicHeader(name, value));
        return this;
    }
    
    public RequestBuilder removeHeader(final Header header) {
        if (this.headergroup == null) {
            this.headergroup = new HeaderGroup();
        }
        this.headergroup.removeHeader(header);
        return this;
    }
    
    public RequestBuilder removeHeaders(final String name) {
        if (name == null || this.headergroup == null) {
            return this;
        }
        final HeaderIterator i = this.headergroup.iterator();
        while (i.hasNext()) {
            final Header header = i.nextHeader();
            if (name.equalsIgnoreCase(header.getName())) {
                i.remove();
            }
        }
        return this;
    }
    
    public RequestBuilder setHeader(final Header header) {
        if (this.headergroup == null) {
            this.headergroup = new HeaderGroup();
        }
        this.headergroup.updateHeader(header);
        return this;
    }
    
    public RequestBuilder setHeader(final String name, final String value) {
        if (this.headergroup == null) {
            this.headergroup = new HeaderGroup();
        }
        this.headergroup.updateHeader(new BasicHeader(name, value));
        return this;
    }
    
    public HttpEntity getEntity() {
        return this.entity;
    }
    
    public RequestBuilder setEntity(final HttpEntity entity) {
        this.entity = entity;
        return this;
    }
    
    public List<NameValuePair> getParameters() {
        return (this.parameters != null) ? new ArrayList<NameValuePair>(this.parameters) : new ArrayList<NameValuePair>();
    }
    
    public RequestBuilder addParameter(final NameValuePair nvp) {
        Args.notNull(nvp, "Name value pair");
        if (this.parameters == null) {
            this.parameters = new LinkedList<NameValuePair>();
        }
        this.parameters.add(nvp);
        return this;
    }
    
    public RequestBuilder addParameter(final String name, final String value) {
        return this.addParameter(new BasicNameValuePair(name, value));
    }
    
    public RequestBuilder addParameters(final NameValuePair... nvps) {
        for (final NameValuePair nvp : nvps) {
            this.addParameter(nvp);
        }
        return this;
    }
    
    public RequestConfig getConfig() {
        return this.config;
    }
    
    public RequestBuilder setConfig(final RequestConfig config) {
        this.config = config;
        return this;
    }
    
    public HttpUriRequest build() {
        URI uri = (this.uri != null) ? this.uri : URI.create("/");
        HttpEntity entity = this.entity;
        if (this.parameters != null && !this.parameters.isEmpty()) {
            if (entity == null && ("POST".equalsIgnoreCase(this.method) || "PUT".equalsIgnoreCase(this.method))) {
                entity = new UrlEncodedFormEntity(this.parameters, HTTP.DEF_CONTENT_CHARSET);
            }
            else {
                try {
                    uri = new URIBuilder(uri).addParameters(this.parameters).build();
                }
                catch (URISyntaxException ex) {}
            }
        }
        HttpRequestBase result;
        if (entity == null) {
            result = new InternalRequest(this.method);
        }
        else {
            final InternalEntityEclosingRequest request = new InternalEntityEclosingRequest(this.method);
            request.setEntity(entity);
            result = request;
        }
        result.setProtocolVersion(this.version);
        result.setURI(uri);
        if (this.headergroup != null) {
            result.setHeaders(this.headergroup.getAllHeaders());
        }
        result.setConfig(this.config);
        return result;
    }
    
    static class InternalRequest extends HttpRequestBase
    {
        private final String method;
        
        InternalRequest(final String method) {
            this.method = method;
        }
        
        @Override
        public String getMethod() {
            return this.method;
        }
    }
    
    static class InternalEntityEclosingRequest extends HttpEntityEnclosingRequestBase
    {
        private final String method;
        
        InternalEntityEclosingRequest(final String method) {
            this.method = method;
        }
        
        @Override
        public String getMethod() {
            return this.method;
        }
    }
}
