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
public class BrowserCompatSpecFactory implements CookieSpecFactory, CookieSpecProvider
{
    private final String[] datepatterns;
    private final SecurityLevel securityLevel;
    
    public BrowserCompatSpecFactory(final String[] datepatterns, final SecurityLevel securityLevel) {
        this.datepatterns = datepatterns;
        this.securityLevel = securityLevel;
    }
    
    public BrowserCompatSpecFactory(final String[] datepatterns) {
        this(null, SecurityLevel.SECURITYLEVEL_DEFAULT);
    }
    
    public BrowserCompatSpecFactory() {
        this(null, SecurityLevel.SECURITYLEVEL_DEFAULT);
    }
    
    public CookieSpec newInstance(final HttpParams params) {
        if (params != null) {
            String[] patterns = null;
            final Collection<?> param = (Collection<?>)params.getParameter("http.protocol.cookie-datepatterns");
            if (param != null) {
                patterns = new String[param.size()];
                patterns = param.toArray(patterns);
            }
            return new BrowserCompatSpec(patterns, this.securityLevel);
        }
        return new BrowserCompatSpec(null, this.securityLevel);
    }
    
    public CookieSpec create(final HttpContext context) {
        return new BrowserCompatSpec(this.datepatterns);
    }
    
    public enum SecurityLevel
    {
        SECURITYLEVEL_DEFAULT, 
        SECURITYLEVEL_IE_MEDIUM;
    }
}
