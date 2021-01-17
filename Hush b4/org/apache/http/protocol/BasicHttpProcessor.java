// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import java.util.Collection;
import org.apache.http.HttpResponse;
import org.apache.http.HttpException;
import java.io.IOException;
import org.apache.http.HttpRequest;
import org.apache.http.util.Args;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpRequestInterceptor;
import java.util.List;
import org.apache.http.annotation.NotThreadSafe;

@Deprecated
@NotThreadSafe
public final class BasicHttpProcessor implements HttpProcessor, HttpRequestInterceptorList, HttpResponseInterceptorList, Cloneable
{
    protected final List<HttpRequestInterceptor> requestInterceptors;
    protected final List<HttpResponseInterceptor> responseInterceptors;
    
    public BasicHttpProcessor() {
        this.requestInterceptors = new ArrayList<HttpRequestInterceptor>();
        this.responseInterceptors = new ArrayList<HttpResponseInterceptor>();
    }
    
    public void addRequestInterceptor(final HttpRequestInterceptor itcp) {
        if (itcp == null) {
            return;
        }
        this.requestInterceptors.add(itcp);
    }
    
    public void addRequestInterceptor(final HttpRequestInterceptor itcp, final int index) {
        if (itcp == null) {
            return;
        }
        this.requestInterceptors.add(index, itcp);
    }
    
    public void addResponseInterceptor(final HttpResponseInterceptor itcp, final int index) {
        if (itcp == null) {
            return;
        }
        this.responseInterceptors.add(index, itcp);
    }
    
    public void removeRequestInterceptorByClass(final Class<? extends HttpRequestInterceptor> clazz) {
        final Iterator<HttpRequestInterceptor> it = this.requestInterceptors.iterator();
        while (it.hasNext()) {
            final Object request = it.next();
            if (request.getClass().equals(clazz)) {
                it.remove();
            }
        }
    }
    
    public void removeResponseInterceptorByClass(final Class<? extends HttpResponseInterceptor> clazz) {
        final Iterator<HttpResponseInterceptor> it = this.responseInterceptors.iterator();
        while (it.hasNext()) {
            final Object request = it.next();
            if (request.getClass().equals(clazz)) {
                it.remove();
            }
        }
    }
    
    public final void addInterceptor(final HttpRequestInterceptor interceptor) {
        this.addRequestInterceptor(interceptor);
    }
    
    public final void addInterceptor(final HttpRequestInterceptor interceptor, final int index) {
        this.addRequestInterceptor(interceptor, index);
    }
    
    public int getRequestInterceptorCount() {
        return this.requestInterceptors.size();
    }
    
    public HttpRequestInterceptor getRequestInterceptor(final int index) {
        if (index < 0 || index >= this.requestInterceptors.size()) {
            return null;
        }
        return this.requestInterceptors.get(index);
    }
    
    public void clearRequestInterceptors() {
        this.requestInterceptors.clear();
    }
    
    public void addResponseInterceptor(final HttpResponseInterceptor itcp) {
        if (itcp == null) {
            return;
        }
        this.responseInterceptors.add(itcp);
    }
    
    public final void addInterceptor(final HttpResponseInterceptor interceptor) {
        this.addResponseInterceptor(interceptor);
    }
    
    public final void addInterceptor(final HttpResponseInterceptor interceptor, final int index) {
        this.addResponseInterceptor(interceptor, index);
    }
    
    public int getResponseInterceptorCount() {
        return this.responseInterceptors.size();
    }
    
    public HttpResponseInterceptor getResponseInterceptor(final int index) {
        if (index < 0 || index >= this.responseInterceptors.size()) {
            return null;
        }
        return this.responseInterceptors.get(index);
    }
    
    public void clearResponseInterceptors() {
        this.responseInterceptors.clear();
    }
    
    public void setInterceptors(final List<?> list) {
        Args.notNull(list, "Inteceptor list");
        this.requestInterceptors.clear();
        this.responseInterceptors.clear();
        for (final Object obj : list) {
            if (obj instanceof HttpRequestInterceptor) {
                this.addInterceptor((HttpRequestInterceptor)obj);
            }
            if (obj instanceof HttpResponseInterceptor) {
                this.addInterceptor((HttpResponseInterceptor)obj);
            }
        }
    }
    
    public void clearInterceptors() {
        this.clearRequestInterceptors();
        this.clearResponseInterceptors();
    }
    
    public void process(final HttpRequest request, final HttpContext context) throws IOException, HttpException {
        for (final HttpRequestInterceptor interceptor : this.requestInterceptors) {
            interceptor.process(request, context);
        }
    }
    
    public void process(final HttpResponse response, final HttpContext context) throws IOException, HttpException {
        for (final HttpResponseInterceptor interceptor : this.responseInterceptors) {
            interceptor.process(response, context);
        }
    }
    
    protected void copyInterceptors(final BasicHttpProcessor target) {
        target.requestInterceptors.clear();
        target.requestInterceptors.addAll(this.requestInterceptors);
        target.responseInterceptors.clear();
        target.responseInterceptors.addAll(this.responseInterceptors);
    }
    
    public BasicHttpProcessor copy() {
        final BasicHttpProcessor clone = new BasicHttpProcessor();
        this.copyInterceptors(clone);
        return clone;
    }
    
    public Object clone() throws CloneNotSupportedException {
        final BasicHttpProcessor clone = (BasicHttpProcessor)super.clone();
        this.copyInterceptors(clone);
        return clone;
    }
}
