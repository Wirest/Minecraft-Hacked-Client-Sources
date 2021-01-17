// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.protocol;

import org.apache.http.auth.AuthScheme;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.auth.AuthState;
import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.util.Args;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpResponseInterceptor;

@Deprecated
@Immutable
public class ResponseAuthCache implements HttpResponseInterceptor
{
    private final Log log;
    
    public ResponseAuthCache() {
        this.log = LogFactory.getLog(this.getClass());
    }
    
    public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
        Args.notNull(response, "HTTP request");
        Args.notNull(context, "HTTP context");
        AuthCache authCache = (AuthCache)context.getAttribute("http.auth.auth-cache");
        HttpHost target = (HttpHost)context.getAttribute("http.target_host");
        final AuthState targetState = (AuthState)context.getAttribute("http.auth.target-scope");
        if (target != null && targetState != null) {
            if (this.log.isDebugEnabled()) {
                this.log.debug("Target auth state: " + targetState.getState());
            }
            if (this.isCachable(targetState)) {
                final SchemeRegistry schemeRegistry = (SchemeRegistry)context.getAttribute("http.scheme-registry");
                if (target.getPort() < 0) {
                    final Scheme scheme = schemeRegistry.getScheme(target);
                    target = new HttpHost(target.getHostName(), scheme.resolvePort(target.getPort()), target.getSchemeName());
                }
                if (authCache == null) {
                    authCache = new BasicAuthCache();
                    context.setAttribute("http.auth.auth-cache", authCache);
                }
                switch (targetState.getState()) {
                    case CHALLENGED: {
                        this.cache(authCache, target, targetState.getAuthScheme());
                        break;
                    }
                    case FAILURE: {
                        this.uncache(authCache, target, targetState.getAuthScheme());
                        break;
                    }
                }
            }
        }
        final HttpHost proxy = (HttpHost)context.getAttribute("http.proxy_host");
        final AuthState proxyState = (AuthState)context.getAttribute("http.auth.proxy-scope");
        if (proxy != null && proxyState != null) {
            if (this.log.isDebugEnabled()) {
                this.log.debug("Proxy auth state: " + proxyState.getState());
            }
            if (this.isCachable(proxyState)) {
                if (authCache == null) {
                    authCache = new BasicAuthCache();
                    context.setAttribute("http.auth.auth-cache", authCache);
                }
                switch (proxyState.getState()) {
                    case CHALLENGED: {
                        this.cache(authCache, proxy, proxyState.getAuthScheme());
                        break;
                    }
                    case FAILURE: {
                        this.uncache(authCache, proxy, proxyState.getAuthScheme());
                        break;
                    }
                }
            }
        }
    }
    
    private boolean isCachable(final AuthState authState) {
        final AuthScheme authScheme = authState.getAuthScheme();
        if (authScheme == null || !authScheme.isComplete()) {
            return false;
        }
        final String schemeName = authScheme.getSchemeName();
        return schemeName.equalsIgnoreCase("Basic") || schemeName.equalsIgnoreCase("Digest");
    }
    
    private void cache(final AuthCache authCache, final HttpHost host, final AuthScheme authScheme) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + host);
        }
        authCache.put(host, authScheme);
    }
    
    private void uncache(final AuthCache authCache, final HttpHost host, final AuthScheme authScheme) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Removing from cache '" + authScheme.getSchemeName() + "' auth scheme for " + host);
        }
        authCache.remove(host);
    }
}
