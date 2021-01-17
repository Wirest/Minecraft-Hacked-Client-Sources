// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import java.util.List;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpRequestInterceptor;

public class HttpProcessorBuilder
{
    private ChainBuilder<HttpRequestInterceptor> requestChainBuilder;
    private ChainBuilder<HttpResponseInterceptor> responseChainBuilder;
    
    public static HttpProcessorBuilder create() {
        return new HttpProcessorBuilder();
    }
    
    HttpProcessorBuilder() {
    }
    
    private ChainBuilder<HttpRequestInterceptor> getRequestChainBuilder() {
        if (this.requestChainBuilder == null) {
            this.requestChainBuilder = new ChainBuilder<HttpRequestInterceptor>();
        }
        return this.requestChainBuilder;
    }
    
    private ChainBuilder<HttpResponseInterceptor> getResponseChainBuilder() {
        if (this.responseChainBuilder == null) {
            this.responseChainBuilder = new ChainBuilder<HttpResponseInterceptor>();
        }
        return this.responseChainBuilder;
    }
    
    public HttpProcessorBuilder addFirst(final HttpRequestInterceptor e) {
        if (e == null) {
            return this;
        }
        this.getRequestChainBuilder().addFirst(e);
        return this;
    }
    
    public HttpProcessorBuilder addLast(final HttpRequestInterceptor e) {
        if (e == null) {
            return this;
        }
        this.getRequestChainBuilder().addLast(e);
        return this;
    }
    
    public HttpProcessorBuilder add(final HttpRequestInterceptor e) {
        return this.addLast(e);
    }
    
    public HttpProcessorBuilder addAllFirst(final HttpRequestInterceptor... e) {
        if (e == null) {
            return this;
        }
        this.getRequestChainBuilder().addAllFirst(e);
        return this;
    }
    
    public HttpProcessorBuilder addAllLast(final HttpRequestInterceptor... e) {
        if (e == null) {
            return this;
        }
        this.getRequestChainBuilder().addAllLast(e);
        return this;
    }
    
    public HttpProcessorBuilder addAll(final HttpRequestInterceptor... e) {
        return this.addAllLast(e);
    }
    
    public HttpProcessorBuilder addFirst(final HttpResponseInterceptor e) {
        if (e == null) {
            return this;
        }
        this.getResponseChainBuilder().addFirst(e);
        return this;
    }
    
    public HttpProcessorBuilder addLast(final HttpResponseInterceptor e) {
        if (e == null) {
            return this;
        }
        this.getResponseChainBuilder().addLast(e);
        return this;
    }
    
    public HttpProcessorBuilder add(final HttpResponseInterceptor e) {
        return this.addLast(e);
    }
    
    public HttpProcessorBuilder addAllFirst(final HttpResponseInterceptor... e) {
        if (e == null) {
            return this;
        }
        this.getResponseChainBuilder().addAllFirst(e);
        return this;
    }
    
    public HttpProcessorBuilder addAllLast(final HttpResponseInterceptor... e) {
        if (e == null) {
            return this;
        }
        this.getResponseChainBuilder().addAllLast(e);
        return this;
    }
    
    public HttpProcessorBuilder addAll(final HttpResponseInterceptor... e) {
        return this.addAllLast(e);
    }
    
    public HttpProcessor build() {
        return new ImmutableHttpProcessor((this.requestChainBuilder != null) ? this.requestChainBuilder.build() : null, (this.responseChainBuilder != null) ? this.responseChainBuilder.build() : null);
    }
}
