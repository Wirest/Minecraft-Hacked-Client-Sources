// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.cookie;

import java.util.Locale;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.util.Args;
import org.apache.http.cookie.SetCookie;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieAttributeHandler;

@Immutable
public class RFC2109DomainHandler implements CookieAttributeHandler
{
    public void parse(final SetCookie cookie, final String value) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (value == null) {
            throw new MalformedCookieException("Missing value for domain attribute");
        }
        if (value.trim().length() == 0) {
            throw new MalformedCookieException("Blank value for domain attribute");
        }
        cookie.setDomain(value);
    }
    
    public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        String host = origin.getHost();
        final String domain = cookie.getDomain();
        if (domain == null) {
            throw new CookieRestrictionViolationException("Cookie domain may not be null");
        }
        if (!domain.equals(host)) {
            int dotIndex = domain.indexOf(46);
            if (dotIndex == -1) {
                throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" does not match the host \"" + host + "\"");
            }
            if (!domain.startsWith(".")) {
                throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates RFC 2109: domain must start with a dot");
            }
            dotIndex = domain.indexOf(46, 1);
            if (dotIndex < 0 || dotIndex == domain.length() - 1) {
                throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates RFC 2109: domain must contain an embedded dot");
            }
            host = host.toLowerCase(Locale.ENGLISH);
            if (!host.endsWith(domain)) {
                throw new CookieRestrictionViolationException("Illegal domain attribute \"" + domain + "\". Domain of origin: \"" + host + "\"");
            }
            final String hostWithoutDomain = host.substring(0, host.length() - domain.length());
            if (hostWithoutDomain.indexOf(46) != -1) {
                throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates RFC 2109: host minus domain may not contain any dots");
            }
        }
    }
    
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        final String host = origin.getHost();
        final String domain = cookie.getDomain();
        return domain != null && (host.equals(domain) || (domain.startsWith(".") && host.endsWith(domain)));
    }
}
