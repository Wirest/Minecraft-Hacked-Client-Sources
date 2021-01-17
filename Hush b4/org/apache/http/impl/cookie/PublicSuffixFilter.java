// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.cookie;

import org.apache.http.client.utils.Punycode;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.Cookie;
import java.util.HashSet;
import java.util.Collection;
import java.util.Set;
import org.apache.http.cookie.CookieAttributeHandler;

public class PublicSuffixFilter implements CookieAttributeHandler
{
    private final CookieAttributeHandler wrapped;
    private Set<String> exceptions;
    private Set<String> suffixes;
    
    public PublicSuffixFilter(final CookieAttributeHandler wrapped) {
        this.wrapped = wrapped;
    }
    
    public void setPublicSuffixes(final Collection<String> suffixes) {
        this.suffixes = new HashSet<String>(suffixes);
    }
    
    public void setExceptions(final Collection<String> exceptions) {
        this.exceptions = new HashSet<String>(exceptions);
    }
    
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        return !this.isForPublicSuffix(cookie) && this.wrapped.match(cookie, origin);
    }
    
    public void parse(final SetCookie cookie, final String value) throws MalformedCookieException {
        this.wrapped.parse(cookie, value);
    }
    
    public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
        this.wrapped.validate(cookie, origin);
    }
    
    private boolean isForPublicSuffix(final Cookie cookie) {
        String domain = cookie.getDomain();
        if (domain.startsWith(".")) {
            domain = domain.substring(1);
        }
        domain = Punycode.toUnicode(domain);
        if (this.exceptions != null && this.exceptions.contains(domain)) {
            return false;
        }
        if (this.suffixes == null) {
            return false;
        }
        while (!this.suffixes.contains(domain)) {
            if (domain.startsWith("*.")) {
                domain = domain.substring(2);
            }
            final int nextdot = domain.indexOf(46);
            if (nextdot != -1) {
                domain = "*" + domain.substring(nextdot);
                if (domain.length() > 0) {
                    continue;
                }
            }
            return false;
        }
        return true;
    }
}
