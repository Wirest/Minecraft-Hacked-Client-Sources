// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client;

import org.apache.http.auth.AuthScheme;
import org.apache.http.HttpHost;

public interface AuthCache
{
    void put(final HttpHost p0, final AuthScheme p1);
    
    AuthScheme get(final HttpHost p0);
    
    void remove(final HttpHost p0);
    
    void clear();
}
