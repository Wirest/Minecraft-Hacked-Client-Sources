// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.HttpHost;
import org.apache.http.config.Lookup;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.Header;
import java.util.Date;
import java.util.Collection;
import org.apache.http.cookie.Cookie;
import java.util.ArrayList;
import org.apache.http.HttpException;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.util.TextUtils;
import java.net.URISyntaxException;
import java.net.URI;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.Args;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpRequestInterceptor;

@Immutable
public class RequestAddCookies implements HttpRequestInterceptor
{
    private final Log log;
    
    public RequestAddCookies() {
        this.log = LogFactory.getLog(this.getClass());
    }
    
    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        Args.notNull(context, "HTTP context");
        final String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT")) {
            return;
        }
        final HttpClientContext clientContext = HttpClientContext.adapt(context);
        final CookieStore cookieStore = clientContext.getCookieStore();
        if (cookieStore == null) {
            this.log.debug("Cookie store not specified in HTTP context");
            return;
        }
        final Lookup<CookieSpecProvider> registry = clientContext.getCookieSpecRegistry();
        if (registry == null) {
            this.log.debug("CookieSpec registry not specified in HTTP context");
            return;
        }
        final HttpHost targetHost = clientContext.getTargetHost();
        if (targetHost == null) {
            this.log.debug("Target host not set in the context");
            return;
        }
        final RouteInfo route = clientContext.getHttpRoute();
        if (route == null) {
            this.log.debug("Connection route not set in the context");
            return;
        }
        final RequestConfig config = clientContext.getRequestConfig();
        String policy = config.getCookieSpec();
        if (policy == null) {
            policy = "best-match";
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("CookieSpec selected: " + policy);
        }
        URI requestURI = null;
        if (request instanceof HttpUriRequest) {
            requestURI = ((HttpUriRequest)request).getURI();
        }
        else {
            try {
                requestURI = new URI(request.getRequestLine().getUri());
            }
            catch (URISyntaxException ex) {}
        }
        final String path = (requestURI != null) ? requestURI.getPath() : null;
        final String hostName = targetHost.getHostName();
        int port = targetHost.getPort();
        if (port < 0) {
            port = route.getTargetHost().getPort();
        }
        final CookieOrigin cookieOrigin = new CookieOrigin(hostName, (port >= 0) ? port : 0, TextUtils.isEmpty(path) ? "/" : path, route.isSecure());
        final CookieSpecProvider provider = registry.lookup(policy);
        if (provider == null) {
            throw new HttpException("Unsupported cookie policy: " + policy);
        }
        final CookieSpec cookieSpec = provider.create(clientContext);
        final List<Cookie> cookies = new ArrayList<Cookie>(cookieStore.getCookies());
        final List<Cookie> matchedCookies = new ArrayList<Cookie>();
        final Date now = new Date();
        for (final Cookie cookie : cookies) {
            if (!cookie.isExpired(now)) {
                if (!cookieSpec.match(cookie, cookieOrigin)) {
                    continue;
                }
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Cookie " + cookie + " match " + cookieOrigin);
                }
                matchedCookies.add(cookie);
            }
            else {
                if (!this.log.isDebugEnabled()) {
                    continue;
                }
                this.log.debug("Cookie " + cookie + " expired");
            }
        }
        if (!matchedCookies.isEmpty()) {
            final List<Header> headers = cookieSpec.formatCookies(matchedCookies);
            for (final Header header : headers) {
                request.addHeader(header);
            }
        }
        final int ver = cookieSpec.getVersion();
        if (ver > 0) {
            boolean needVersionHeader = false;
            for (final Cookie cookie2 : matchedCookies) {
                if (ver != cookie2.getVersion() || !(cookie2 instanceof SetCookie2)) {
                    needVersionHeader = true;
                }
            }
            if (needVersionHeader) {
                final Header header = cookieSpec.getVersionHeader();
                if (header != null) {
                    request.addHeader(header);
                }
            }
        }
        context.setAttribute("http.cookie-spec", cookieSpec);
        context.setAttribute("http.cookie-origin", cookieOrigin);
    }
}
