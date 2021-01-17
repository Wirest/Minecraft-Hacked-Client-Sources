// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.cookie;

import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.Cookie;
import java.util.Locale;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.util.Args;
import org.apache.http.cookie.SetCookie;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieAttributeHandler;

@Immutable
public class RFC2965DomainAttributeHandler implements CookieAttributeHandler
{
    public void parse(final SetCookie cookie, final String domain) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (domain == null) {
            throw new MalformedCookieException("Missing value for domain attribute");
        }
        if (domain.trim().length() == 0) {
            throw new MalformedCookieException("Blank value for domain attribute");
        }
        String s = domain;
        s = s.toLowerCase(Locale.ENGLISH);
        if (!domain.startsWith(".")) {
            s = '.' + s;
        }
        cookie.setDomain(s);
    }
    
    public boolean domainMatch(final String host, final String domain) {
        final boolean match = host.equals(domain) || (domain.startsWith(".") && host.endsWith(domain));
        return match;
    }
    
    public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        final String host = origin.getHost().toLowerCase(Locale.ENGLISH);
        if (cookie.getDomain() == null) {
            throw new CookieRestrictionViolationException("Invalid cookie state: domain not specified");
        }
        final String cookieDomain = cookie.getDomain().toLowerCase(Locale.ENGLISH);
        if (cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("domain")) {
            if (!cookieDomain.startsWith(".")) {
                throw new CookieRestrictionViolationException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2109: domain must start with a dot");
            }
            final int dotIndex = cookieDomain.indexOf(46, 1);
            if ((dotIndex < 0 || dotIndex == cookieDomain.length() - 1) && !cookieDomain.equals(".local")) {
                throw new CookieRestrictionViolationException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2965: the value contains no embedded dots " + "and the value is not .local");
            }
            if (!this.domainMatch(host, cookieDomain)) {
                throw new CookieRestrictionViolationException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2965: effective host name does not " + "domain-match domain attribute.");
            }
            final String effectiveHostWithoutDomain = host.substring(0, host.length() - cookieDomain.length());
            if (effectiveHostWithoutDomain.indexOf(46) != -1) {
                throw new CookieRestrictionViolationException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2965: " + "effective host minus domain may not contain any dots");
            }
        }
        else if (!cookie.getDomain().equals(host)) {
            throw new CookieRestrictionViolationException("Illegal domain attribute: \"" + cookie.getDomain() + "\"." + "Domain of origin: \"" + host + "\"");
        }
    }
    
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        final String host = origin.getHost().toLowerCase(Locale.ENGLISH);
        final String cookieDomain = cookie.getDomain();
        if (!this.domainMatch(host, cookieDomain)) {
            return false;
        }
        final String effectiveHostWithoutDomain = host.substring(0, host.length() - cookieDomain.length());
        return effectiveHostWithoutDomain.indexOf(46) == -1;
    }
}
