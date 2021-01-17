// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.cookie;

import java.util.Iterator;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.HeaderElement;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.ParserCursor;
import org.apache.http.FormattedHeader;
import org.apache.http.util.Args;
import org.apache.http.cookie.Cookie;
import java.util.List;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.Header;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.CookieSpec;

@NotThreadSafe
public class BestMatchSpec implements CookieSpec
{
    private final String[] datepatterns;
    private final boolean oneHeader;
    private RFC2965Spec strict;
    private RFC2109Spec obsoleteStrict;
    private BrowserCompatSpec compat;
    
    public BestMatchSpec(final String[] datepatterns, final boolean oneHeader) {
        this.datepatterns = (String[])((datepatterns == null) ? null : ((String[])datepatterns.clone()));
        this.oneHeader = oneHeader;
    }
    
    public BestMatchSpec() {
        this(null, false);
    }
    
    private RFC2965Spec getStrict() {
        if (this.strict == null) {
            this.strict = new RFC2965Spec(this.datepatterns, this.oneHeader);
        }
        return this.strict;
    }
    
    private RFC2109Spec getObsoleteStrict() {
        if (this.obsoleteStrict == null) {
            this.obsoleteStrict = new RFC2109Spec(this.datepatterns, this.oneHeader);
        }
        return this.obsoleteStrict;
    }
    
    private BrowserCompatSpec getCompat() {
        if (this.compat == null) {
            this.compat = new BrowserCompatSpec(this.datepatterns);
        }
        return this.compat;
    }
    
    public List<Cookie> parse(final Header header, final CookieOrigin origin) throws MalformedCookieException {
        Args.notNull(header, "Header");
        Args.notNull(origin, "Cookie origin");
        HeaderElement[] helems = header.getElements();
        boolean versioned = false;
        boolean netscape = false;
        for (final HeaderElement helem : helems) {
            if (helem.getParameterByName("version") != null) {
                versioned = true;
            }
            if (helem.getParameterByName("expires") != null) {
                netscape = true;
            }
        }
        if (netscape || !versioned) {
            final NetscapeDraftHeaderParser parser = NetscapeDraftHeaderParser.DEFAULT;
            CharArrayBuffer buffer;
            ParserCursor cursor;
            if (header instanceof FormattedHeader) {
                buffer = ((FormattedHeader)header).getBuffer();
                cursor = new ParserCursor(((FormattedHeader)header).getValuePos(), buffer.length());
            }
            else {
                final String s = header.getValue();
                if (s == null) {
                    throw new MalformedCookieException("Header value is null");
                }
                buffer = new CharArrayBuffer(s.length());
                buffer.append(s);
                cursor = new ParserCursor(0, buffer.length());
            }
            helems = new HeaderElement[] { parser.parseHeader(buffer, cursor) };
            return this.getCompat().parse(helems, origin);
        }
        if ("Set-Cookie2".equals(header.getName())) {
            return this.getStrict().parse(helems, origin);
        }
        return this.getObsoleteStrict().parse(helems, origin);
    }
    
    public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        if (cookie.getVersion() > 0) {
            if (cookie instanceof SetCookie2) {
                this.getStrict().validate(cookie, origin);
            }
            else {
                this.getObsoleteStrict().validate(cookie, origin);
            }
        }
        else {
            this.getCompat().validate(cookie, origin);
        }
    }
    
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        if (cookie.getVersion() <= 0) {
            return this.getCompat().match(cookie, origin);
        }
        if (cookie instanceof SetCookie2) {
            return this.getStrict().match(cookie, origin);
        }
        return this.getObsoleteStrict().match(cookie, origin);
    }
    
    public List<Header> formatCookies(final List<Cookie> cookies) {
        Args.notNull(cookies, "List of cookies");
        int version = Integer.MAX_VALUE;
        boolean isSetCookie2 = true;
        for (final Cookie cookie : cookies) {
            if (!(cookie instanceof SetCookie2)) {
                isSetCookie2 = false;
            }
            if (cookie.getVersion() < version) {
                version = cookie.getVersion();
            }
        }
        if (version <= 0) {
            return this.getCompat().formatCookies(cookies);
        }
        if (isSetCookie2) {
            return this.getStrict().formatCookies(cookies);
        }
        return this.getObsoleteStrict().formatCookies(cookies);
    }
    
    public int getVersion() {
        return this.getStrict().getVersion();
    }
    
    public Header getVersionHeader() {
        return this.getStrict().getVersionHeader();
    }
    
    @Override
    public String toString() {
        return "best-match";
    }
}
