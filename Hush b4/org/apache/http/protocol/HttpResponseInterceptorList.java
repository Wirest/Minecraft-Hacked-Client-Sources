// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import java.util.List;
import org.apache.http.HttpResponseInterceptor;

@Deprecated
public interface HttpResponseInterceptorList
{
    void addResponseInterceptor(final HttpResponseInterceptor p0);
    
    void addResponseInterceptor(final HttpResponseInterceptor p0, final int p1);
    
    int getResponseInterceptorCount();
    
    HttpResponseInterceptor getResponseInterceptor(final int p0);
    
    void clearResponseInterceptors();
    
    void removeResponseInterceptorByClass(final Class<? extends HttpResponseInterceptor> p0);
    
    void setInterceptors(final List<?> p0);
}
