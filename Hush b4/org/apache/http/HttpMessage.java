// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http;

import org.apache.http.params.HttpParams;

public interface HttpMessage
{
    ProtocolVersion getProtocolVersion();
    
    boolean containsHeader(final String p0);
    
    Header[] getHeaders(final String p0);
    
    Header getFirstHeader(final String p0);
    
    Header getLastHeader(final String p0);
    
    Header[] getAllHeaders();
    
    void addHeader(final Header p0);
    
    void addHeader(final String p0, final String p1);
    
    void setHeader(final Header p0);
    
    void setHeader(final String p0, final String p1);
    
    void setHeaders(final Header[] p0);
    
    void removeHeader(final Header p0);
    
    void removeHeaders(final String p0);
    
    HeaderIterator headerIterator();
    
    HeaderIterator headerIterator(final String p0);
    
    @Deprecated
    HttpParams getParams();
    
    @Deprecated
    void setParams(final HttpParams p0);
}
