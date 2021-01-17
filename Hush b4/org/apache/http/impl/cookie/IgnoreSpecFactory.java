// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.cookie;

import org.apache.http.protocol.HttpContext;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.params.HttpParams;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.CookieSpecFactory;

@Immutable
public class IgnoreSpecFactory implements CookieSpecFactory, CookieSpecProvider
{
    public CookieSpec newInstance(final HttpParams params) {
        return new IgnoreSpec();
    }
    
    public CookieSpec create(final HttpContext context) {
        return new IgnoreSpec();
    }
}
