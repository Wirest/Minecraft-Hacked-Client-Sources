// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.params.HttpParams;
import org.apache.http.Header;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.util.Asserts;
import org.apache.http.HttpHost;
import java.net.URISyntaxException;
import org.apache.http.ProtocolException;
import java.net.URI;
import org.apache.http.HttpRequest;
import org.apache.http.util.Args;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.RedirectHandler;

@Deprecated
@Immutable
public class DefaultRedirectHandler implements RedirectHandler
{
    private final Log log;
    private static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
    
    public DefaultRedirectHandler() {
        this.log = LogFactory.getLog(this.getClass());
    }
    
    public boolean isRedirectRequested(final HttpResponse response, final HttpContext context) {
        Args.notNull(response, "HTTP response");
        final int statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode) {
            case 301:
            case 302:
            case 307: {
                final HttpRequest request = (HttpRequest)context.getAttribute("http.request");
                final String method = request.getRequestLine().getMethod();
                return method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("HEAD");
            }
            case 303: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public URI getLocationURI(final HttpResponse response, final HttpContext context) throws ProtocolException {
        Args.notNull(response, "HTTP response");
        final Header locationHeader = response.getFirstHeader("location");
        if (locationHeader == null) {
            throw new ProtocolException("Received redirect response " + response.getStatusLine() + " but no location header");
        }
        final String location = locationHeader.getValue();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Redirect requested to location '" + location + "'");
        }
        URI uri;
        try {
            uri = new URI(location);
        }
        catch (URISyntaxException ex) {
            throw new ProtocolException("Invalid redirect URI: " + location, ex);
        }
        final HttpParams params = response.getParams();
        if (!uri.isAbsolute()) {
            if (params.isParameterTrue("http.protocol.reject-relative-redirect")) {
                throw new ProtocolException("Relative redirect location '" + uri + "' not allowed");
            }
            final HttpHost target = (HttpHost)context.getAttribute("http.target_host");
            Asserts.notNull(target, "Target host");
            final HttpRequest request = (HttpRequest)context.getAttribute("http.request");
            try {
                final URI requestURI = new URI(request.getRequestLine().getUri());
                final URI absoluteRequestURI = URIUtils.rewriteURI(requestURI, target, true);
                uri = URIUtils.resolve(absoluteRequestURI, uri);
            }
            catch (URISyntaxException ex2) {
                throw new ProtocolException(ex2.getMessage(), ex2);
            }
        }
        if (params.isParameterFalse("http.protocol.allow-circular-redirects")) {
            RedirectLocations redirectLocations = (RedirectLocations)context.getAttribute("http.protocol.redirect-locations");
            if (redirectLocations == null) {
                redirectLocations = new RedirectLocations();
                context.setAttribute("http.protocol.redirect-locations", redirectLocations);
            }
            URI redirectURI = null;
            Label_0426: {
                if (uri.getFragment() != null) {
                    try {
                        final HttpHost target2 = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
                        redirectURI = URIUtils.rewriteURI(uri, target2, true);
                        break Label_0426;
                    }
                    catch (URISyntaxException ex2) {
                        throw new ProtocolException(ex2.getMessage(), ex2);
                    }
                }
                redirectURI = uri;
            }
            if (redirectLocations.contains(redirectURI)) {
                throw new CircularRedirectException("Circular redirect to '" + redirectURI + "'");
            }
            redirectLocations.add(redirectURI);
        }
        return uri;
    }
}
