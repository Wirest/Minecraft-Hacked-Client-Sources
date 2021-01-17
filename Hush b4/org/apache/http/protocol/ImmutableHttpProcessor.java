// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import org.apache.http.HttpResponse;
import org.apache.http.HttpException;
import java.io.IOException;
import org.apache.http.HttpRequest;
import java.util.List;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public final class ImmutableHttpProcessor implements HttpProcessor
{
    private final HttpRequestInterceptor[] requestInterceptors;
    private final HttpResponseInterceptor[] responseInterceptors;
    
    public ImmutableHttpProcessor(final HttpRequestInterceptor[] requestInterceptors, final HttpResponseInterceptor[] responseInterceptors) {
        if (requestInterceptors != null) {
            final int l = requestInterceptors.length;
            System.arraycopy(requestInterceptors, 0, this.requestInterceptors = new HttpRequestInterceptor[l], 0, l);
        }
        else {
            this.requestInterceptors = new HttpRequestInterceptor[0];
        }
        if (responseInterceptors != null) {
            final int l = responseInterceptors.length;
            System.arraycopy(responseInterceptors, 0, this.responseInterceptors = new HttpResponseInterceptor[l], 0, l);
        }
        else {
            this.responseInterceptors = new HttpResponseInterceptor[0];
        }
    }
    
    public ImmutableHttpProcessor(final List<HttpRequestInterceptor> requestInterceptors, final List<HttpResponseInterceptor> responseInterceptors) {
        if (requestInterceptors != null) {
            final int l = requestInterceptors.size();
            this.requestInterceptors = requestInterceptors.toArray(new HttpRequestInterceptor[l]);
        }
        else {
            this.requestInterceptors = new HttpRequestInterceptor[0];
        }
        if (responseInterceptors != null) {
            final int l = responseInterceptors.size();
            this.responseInterceptors = responseInterceptors.toArray(new HttpResponseInterceptor[l]);
        }
        else {
            this.responseInterceptors = new HttpResponseInterceptor[0];
        }
    }
    
    @Deprecated
    public ImmutableHttpProcessor(final HttpRequestInterceptorList requestInterceptors, final HttpResponseInterceptorList responseInterceptors) {
        if (requestInterceptors != null) {
            final int count = requestInterceptors.getRequestInterceptorCount();
            this.requestInterceptors = new HttpRequestInterceptor[count];
            for (int i = 0; i < count; ++i) {
                this.requestInterceptors[i] = requestInterceptors.getRequestInterceptor(i);
            }
        }
        else {
            this.requestInterceptors = new HttpRequestInterceptor[0];
        }
        if (responseInterceptors != null) {
            final int count = responseInterceptors.getResponseInterceptorCount();
            this.responseInterceptors = new HttpResponseInterceptor[count];
            for (int i = 0; i < count; ++i) {
                this.responseInterceptors[i] = responseInterceptors.getResponseInterceptor(i);
            }
        }
        else {
            this.responseInterceptors = new HttpResponseInterceptor[0];
        }
    }
    
    public ImmutableHttpProcessor(final HttpRequestInterceptor... requestInterceptors) {
        this(requestInterceptors, null);
    }
    
    public ImmutableHttpProcessor(final HttpResponseInterceptor... responseInterceptors) {
        this(null, responseInterceptors);
    }
    
    public void process(final HttpRequest request, final HttpContext context) throws IOException, HttpException {
        for (final HttpRequestInterceptor requestInterceptor : this.requestInterceptors) {
            requestInterceptor.process(request, context);
        }
    }
    
    public void process(final HttpResponse response, final HttpContext context) throws IOException, HttpException {
        for (final HttpResponseInterceptor responseInterceptor : this.responseInterceptors) {
            responseInterceptor.process(response, context);
        }
    }
}
