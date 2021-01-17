// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.cookie;

import org.apache.http.cookie.MalformedCookieException;
import java.util.Collections;
import org.apache.http.cookie.Cookie;
import java.util.List;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.Header;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class IgnoreSpec extends CookieSpecBase
{
    public int getVersion() {
        return 0;
    }
    
    public List<Cookie> parse(final Header header, final CookieOrigin origin) throws MalformedCookieException {
        return Collections.emptyList();
    }
    
    public List<Header> formatCookies(final List<Cookie> cookies) {
        return Collections.emptyList();
    }
    
    public Header getVersionHeader() {
        return null;
    }
}
