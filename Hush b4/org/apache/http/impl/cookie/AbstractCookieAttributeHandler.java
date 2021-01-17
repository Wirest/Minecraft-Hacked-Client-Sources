// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.cookie;

import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.Cookie;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieAttributeHandler;

@Immutable
public abstract class AbstractCookieAttributeHandler implements CookieAttributeHandler
{
    public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
    }
    
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        return true;
    }
}
