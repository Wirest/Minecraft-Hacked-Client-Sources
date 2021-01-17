// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.cookie;

import org.apache.http.protocol.HttpContext;
import java.util.Collection;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.params.HttpParams;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.CookieSpecFactory;

@Immutable
public class NetscapeDraftSpecFactory implements CookieSpecFactory, CookieSpecProvider
{
    private final String[] datepatterns;
    
    public NetscapeDraftSpecFactory(final String[] datepatterns) {
        this.datepatterns = datepatterns;
    }
    
    public NetscapeDraftSpecFactory() {
        this(null);
    }
    
    public CookieSpec newInstance(final HttpParams params) {
        if (params != null) {
            String[] patterns = null;
            final Collection<?> param = (Collection<?>)params.getParameter("http.protocol.cookie-datepatterns");
            if (param != null) {
                patterns = new String[param.size()];
                patterns = param.toArray(patterns);
            }
            return new NetscapeDraftSpec(patterns);
        }
        return new NetscapeDraftSpec();
    }
    
    public CookieSpec create(final HttpContext context) {
        return new NetscapeDraftSpec(this.datepatterns);
    }
}
