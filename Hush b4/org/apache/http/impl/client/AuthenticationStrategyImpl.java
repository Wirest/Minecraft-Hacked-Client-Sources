// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import java.util.Collections;
import java.util.Arrays;
import org.apache.http.client.AuthCache;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.AuthScheme;
import java.util.Iterator;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.config.Lookup;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthSchemeProvider;
import java.util.LinkedList;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.auth.AuthOption;
import java.util.Queue;
import java.util.Collection;
import org.apache.http.client.config.RequestConfig;
import java.util.Locale;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.FormattedHeader;
import java.util.HashMap;
import org.apache.http.Header;
import java.util.Map;
import org.apache.http.util.Args;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;
import org.apache.http.HttpHost;
import org.apache.commons.logging.LogFactory;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.AuthenticationStrategy;

@Immutable
abstract class AuthenticationStrategyImpl implements AuthenticationStrategy
{
    private final Log log;
    private static final List<String> DEFAULT_SCHEME_PRIORITY;
    private final int challengeCode;
    private final String headerName;
    
    AuthenticationStrategyImpl(final int challengeCode, final String headerName) {
        this.log = LogFactory.getLog(this.getClass());
        this.challengeCode = challengeCode;
        this.headerName = headerName;
    }
    
    public boolean isAuthenticationRequested(final HttpHost authhost, final HttpResponse response, final HttpContext context) {
        Args.notNull(response, "HTTP response");
        final int status = response.getStatusLine().getStatusCode();
        return status == this.challengeCode;
    }
    
    public Map<String, Header> getChallenges(final HttpHost authhost, final HttpResponse response, final HttpContext context) throws MalformedChallengeException {
        Args.notNull(response, "HTTP response");
        final Header[] headers = response.getHeaders(this.headerName);
        final Map<String, Header> map = new HashMap<String, Header>(headers.length);
        for (final Header header : headers) {
            CharArrayBuffer buffer;
            int pos;
            if (header instanceof FormattedHeader) {
                buffer = ((FormattedHeader)header).getBuffer();
                pos = ((FormattedHeader)header).getValuePos();
            }
            else {
                final String s = header.getValue();
                if (s == null) {
                    throw new MalformedChallengeException("Header value is null");
                }
                buffer = new CharArrayBuffer(s.length());
                buffer.append(s);
                pos = 0;
            }
            while (pos < buffer.length() && HTTP.isWhitespace(buffer.charAt(pos))) {
                ++pos;
            }
            final int beginIndex = pos;
            while (pos < buffer.length() && !HTTP.isWhitespace(buffer.charAt(pos))) {
                ++pos;
            }
            final int endIndex = pos;
            final String s2 = buffer.substring(beginIndex, endIndex);
            map.put(s2.toLowerCase(Locale.US), header);
        }
        return map;
    }
    
    abstract Collection<String> getPreferredAuthSchemes(final RequestConfig p0);
    
    public Queue<AuthOption> select(final Map<String, Header> challenges, final HttpHost authhost, final HttpResponse response, final HttpContext context) throws MalformedChallengeException {
        Args.notNull(challenges, "Map of auth challenges");
        Args.notNull(authhost, "Host");
        Args.notNull(response, "HTTP response");
        Args.notNull(context, "HTTP context");
        final HttpClientContext clientContext = HttpClientContext.adapt(context);
        final Queue<AuthOption> options = new LinkedList<AuthOption>();
        final Lookup<AuthSchemeProvider> registry = clientContext.getAuthSchemeRegistry();
        if (registry == null) {
            this.log.debug("Auth scheme registry not set in the context");
            return options;
        }
        final CredentialsProvider credsProvider = clientContext.getCredentialsProvider();
        if (credsProvider == null) {
            this.log.debug("Credentials provider not set in the context");
            return options;
        }
        final RequestConfig config = clientContext.getRequestConfig();
        Collection<String> authPrefs = this.getPreferredAuthSchemes(config);
        if (authPrefs == null) {
            authPrefs = AuthenticationStrategyImpl.DEFAULT_SCHEME_PRIORITY;
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("Authentication schemes in the order of preference: " + authPrefs);
        }
        for (final String id : authPrefs) {
            final Header challenge = challenges.get(id.toLowerCase(Locale.US));
            if (challenge != null) {
                final AuthSchemeProvider authSchemeProvider = registry.lookup(id);
                if (authSchemeProvider == null) {
                    if (!this.log.isWarnEnabled()) {
                        continue;
                    }
                    this.log.warn("Authentication scheme " + id + " not supported");
                }
                else {
                    final AuthScheme authScheme = authSchemeProvider.create(context);
                    authScheme.processChallenge(challenge);
                    final AuthScope authScope = new AuthScope(authhost.getHostName(), authhost.getPort(), authScheme.getRealm(), authScheme.getSchemeName());
                    final Credentials credentials = credsProvider.getCredentials(authScope);
                    if (credentials == null) {
                        continue;
                    }
                    options.add(new AuthOption(authScheme, credentials));
                }
            }
            else {
                if (!this.log.isDebugEnabled()) {
                    continue;
                }
                this.log.debug("Challenge for " + id + " authentication scheme not available");
            }
        }
        return options;
    }
    
    public void authSucceeded(final HttpHost authhost, final AuthScheme authScheme, final HttpContext context) {
        Args.notNull(authhost, "Host");
        Args.notNull(authScheme, "Auth scheme");
        Args.notNull(context, "HTTP context");
        final HttpClientContext clientContext = HttpClientContext.adapt(context);
        if (this.isCachable(authScheme)) {
            AuthCache authCache = clientContext.getAuthCache();
            if (authCache == null) {
                authCache = new BasicAuthCache();
                clientContext.setAuthCache(authCache);
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + authhost);
            }
            authCache.put(authhost, authScheme);
        }
    }
    
    protected boolean isCachable(final AuthScheme authScheme) {
        if (authScheme == null || !authScheme.isComplete()) {
            return false;
        }
        final String schemeName = authScheme.getSchemeName();
        return schemeName.equalsIgnoreCase("Basic") || schemeName.equalsIgnoreCase("Digest");
    }
    
    public void authFailed(final HttpHost authhost, final AuthScheme authScheme, final HttpContext context) {
        Args.notNull(authhost, "Host");
        Args.notNull(context, "HTTP context");
        final HttpClientContext clientContext = HttpClientContext.adapt(context);
        final AuthCache authCache = clientContext.getAuthCache();
        if (authCache != null) {
            if (this.log.isDebugEnabled()) {
                this.log.debug("Clearing cached auth scheme for " + authhost);
            }
            authCache.remove(authhost);
        }
    }
    
    static {
        DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList((List<? extends String>)Arrays.asList("negotiate", "Kerberos", "NTLM", "Digest", "Basic"));
    }
}
