// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.cookie;

public interface CookieAttributeHandler
{
    void parse(final SetCookie p0, final String p1) throws MalformedCookieException;
    
    void validate(final Cookie p0, final CookieOrigin p1) throws MalformedCookieException;
    
    boolean match(final Cookie p0, final CookieOrigin p1);
}
