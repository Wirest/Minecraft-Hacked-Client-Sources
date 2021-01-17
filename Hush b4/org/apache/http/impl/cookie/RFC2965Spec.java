// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.cookie;

import org.apache.http.message.BufferedHeader;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.util.CharArrayBuffer;
import java.util.Iterator;
import org.apache.http.cookie.SetCookie;
import java.util.Map;
import java.util.Locale;
import org.apache.http.NameValuePair;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.http.HeaderElement;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.util.Args;
import org.apache.http.cookie.Cookie;
import java.util.List;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.Header;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class RFC2965Spec extends RFC2109Spec
{
    public RFC2965Spec() {
        this(null, false);
    }
    
    public RFC2965Spec(final String[] datepatterns, final boolean oneHeader) {
        super(datepatterns, oneHeader);
        this.registerAttribHandler("domain", new RFC2965DomainAttributeHandler());
        this.registerAttribHandler("port", new RFC2965PortAttributeHandler());
        this.registerAttribHandler("commenturl", new RFC2965CommentUrlAttributeHandler());
        this.registerAttribHandler("discard", new RFC2965DiscardAttributeHandler());
        this.registerAttribHandler("version", new RFC2965VersionAttributeHandler());
    }
    
    @Override
    public List<Cookie> parse(final Header header, final CookieOrigin origin) throws MalformedCookieException {
        Args.notNull(header, "Header");
        Args.notNull(origin, "Cookie origin");
        if (!header.getName().equalsIgnoreCase("Set-Cookie2")) {
            throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
        }
        final HeaderElement[] elems = header.getElements();
        return this.createCookies(elems, adjustEffectiveHost(origin));
    }
    
    @Override
    protected List<Cookie> parse(final HeaderElement[] elems, final CookieOrigin origin) throws MalformedCookieException {
        return this.createCookies(elems, adjustEffectiveHost(origin));
    }
    
    private List<Cookie> createCookies(final HeaderElement[] elems, final CookieOrigin origin) throws MalformedCookieException {
        final List<Cookie> cookies = new ArrayList<Cookie>(elems.length);
        for (final HeaderElement headerelement : elems) {
            final String name = headerelement.getName();
            final String value = headerelement.getValue();
            if (name == null || name.length() == 0) {
                throw new MalformedCookieException("Cookie name may not be empty");
            }
            final BasicClientCookie2 cookie = new BasicClientCookie2(name, value);
            cookie.setPath(CookieSpecBase.getDefaultPath(origin));
            cookie.setDomain(CookieSpecBase.getDefaultDomain(origin));
            cookie.setPorts(new int[] { origin.getPort() });
            final NameValuePair[] attribs = headerelement.getParameters();
            final Map<String, NameValuePair> attribmap = new HashMap<String, NameValuePair>(attribs.length);
            for (int j = attribs.length - 1; j >= 0; --j) {
                final NameValuePair param = attribs[j];
                attribmap.put(param.getName().toLowerCase(Locale.ENGLISH), param);
            }
            for (final Map.Entry<String, NameValuePair> entry : attribmap.entrySet()) {
                final NameValuePair attrib = entry.getValue();
                final String s = attrib.getName().toLowerCase(Locale.ENGLISH);
                cookie.setAttribute(s, attrib.getValue());
                final CookieAttributeHandler handler = this.findAttribHandler(s);
                if (handler != null) {
                    handler.parse(cookie, attrib.getValue());
                }
            }
            cookies.add(cookie);
        }
        return cookies;
    }
    
    @Override
    public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        super.validate(cookie, adjustEffectiveHost(origin));
    }
    
    @Override
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        return super.match(cookie, adjustEffectiveHost(origin));
    }
    
    @Override
    protected void formatCookieAsVer(final CharArrayBuffer buffer, final Cookie cookie, final int version) {
        super.formatCookieAsVer(buffer, cookie, version);
        if (cookie instanceof ClientCookie) {
            final String s = ((ClientCookie)cookie).getAttribute("port");
            if (s != null) {
                buffer.append("; $Port");
                buffer.append("=\"");
                if (s.trim().length() > 0) {
                    final int[] ports = cookie.getPorts();
                    if (ports != null) {
                        for (int len = ports.length, i = 0; i < len; ++i) {
                            if (i > 0) {
                                buffer.append(",");
                            }
                            buffer.append(Integer.toString(ports[i]));
                        }
                    }
                }
                buffer.append("\"");
            }
        }
    }
    
    private static CookieOrigin adjustEffectiveHost(final CookieOrigin origin) {
        String host = origin.getHost();
        boolean isLocalHost = true;
        for (int i = 0; i < host.length(); ++i) {
            final char ch = host.charAt(i);
            if (ch == '.' || ch == ':') {
                isLocalHost = false;
                break;
            }
        }
        if (isLocalHost) {
            host += ".local";
            return new CookieOrigin(host, origin.getPort(), origin.getPath(), origin.isSecure());
        }
        return origin;
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public Header getVersionHeader() {
        final CharArrayBuffer buffer = new CharArrayBuffer(40);
        buffer.append("Cookie2");
        buffer.append(": ");
        buffer.append("$Version=");
        buffer.append(Integer.toString(this.getVersion()));
        return new BufferedHeader(buffer);
    }
    
    @Override
    public String toString() {
        return "rfc2965";
    }
}
