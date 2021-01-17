// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.cookie;

import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.util.Args;
import org.apache.http.cookie.SetCookie;
import org.apache.http.cookie.MalformedCookieException;
import java.util.StringTokenizer;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieAttributeHandler;

@Immutable
public class RFC2965PortAttributeHandler implements CookieAttributeHandler
{
    private static int[] parsePortAttribute(final String portValue) throws MalformedCookieException {
        final StringTokenizer st = new StringTokenizer(portValue, ",");
        final int[] ports = new int[st.countTokens()];
        try {
            int i = 0;
            while (st.hasMoreTokens()) {
                ports[i] = Integer.parseInt(st.nextToken().trim());
                if (ports[i] < 0) {
                    throw new MalformedCookieException("Invalid Port attribute.");
                }
                ++i;
            }
        }
        catch (NumberFormatException e) {
            throw new MalformedCookieException("Invalid Port attribute: " + e.getMessage());
        }
        return ports;
    }
    
    private static boolean portMatch(final int port, final int[] ports) {
        boolean portInList = false;
        for (final int port2 : ports) {
            if (port == port2) {
                portInList = true;
                break;
            }
        }
        return portInList;
    }
    
    public void parse(final SetCookie cookie, final String portValue) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (cookie instanceof SetCookie2) {
            final SetCookie2 cookie2 = (SetCookie2)cookie;
            if (portValue != null && portValue.trim().length() > 0) {
                final int[] ports = parsePortAttribute(portValue);
                cookie2.setPorts(ports);
            }
        }
    }
    
    public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        final int port = origin.getPort();
        if (cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("port") && !portMatch(port, cookie.getPorts())) {
            throw new CookieRestrictionViolationException("Port attribute violates RFC 2965: Request port not found in cookie's port list.");
        }
    }
    
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        final int port = origin.getPort();
        if (cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("port")) {
            if (cookie.getPorts() == null) {
                return false;
            }
            if (!portMatch(port, cookie.getPorts())) {
                return false;
            }
        }
        return true;
    }
}
