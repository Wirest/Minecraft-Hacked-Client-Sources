// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import java.util.List;
import org.apache.http.HttpRequestInterceptor;

@Deprecated
public interface HttpRequestInterceptorList
{
    void addRequestInterceptor(final HttpRequestInterceptor p0);
    
    void addRequestInterceptor(final HttpRequestInterceptor p0, final int p1);
    
    int getRequestInterceptorCount();
    
    HttpRequestInterceptor getRequestInterceptor(final int p0);
    
    void clearRequestInterceptors();
    
    void removeRequestInterceptorByClass(final Class<? extends HttpRequestInterceptor> p0);
    
    void setInterceptors(final List<?> p0);
}
