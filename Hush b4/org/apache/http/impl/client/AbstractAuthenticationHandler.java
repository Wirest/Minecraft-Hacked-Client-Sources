// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import java.util.Collections;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.util.Asserts;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.auth.AuthScheme;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;
import java.util.Locale;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.FormattedHeader;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.commons.logging.LogFactory;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.AuthenticationHandler;

@Deprecated
@Immutable
public abstract class AbstractAuthenticationHandler implements AuthenticationHandler
{
    private final Log log;
    private static final List<String> DEFAULT_SCHEME_PRIORITY;
    
    public AbstractAuthenticationHandler() {
        this.log = LogFactory.getLog(this.getClass());
    }
    
    protected Map<String, Header> parseChallenges(final Header[] headers) throws MalformedChallengeException {
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
    
    protected List<String> getAuthPreferences() {
        return AbstractAuthenticationHandler.DEFAULT_SCHEME_PRIORITY;
    }
    
    protected List<String> getAuthPreferences(final HttpResponse response, final HttpContext context) {
        return this.getAuthPreferences();
    }
    
    public AuthScheme selectScheme(final Map<String, Header> challenges, final HttpResponse response, final HttpContext context) throws AuthenticationException {
        final AuthSchemeRegistry registry = (AuthSchemeRegistry)context.getAttribute("http.authscheme-registry");
        Asserts.notNull(registry, "AuthScheme registry");
        Collection<String> authPrefs = this.getAuthPreferences(response, context);
        if (authPrefs == null) {
            authPrefs = AbstractAuthenticationHandler.DEFAULT_SCHEME_PRIORITY;
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("Authentication schemes in the order of preference: " + authPrefs);
        }
        AuthScheme authScheme = null;
        for (final String id : authPrefs) {
            final Header challenge = challenges.get(id.toLowerCase(Locale.ENGLISH));
            if (challenge != null) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug(id + " authentication scheme selected");
                }
                try {
                    authScheme = registry.getAuthScheme(id, response.getParams());
                    break;
                }
                catch (IllegalStateException e) {
                    if (!this.log.isWarnEnabled()) {
                        continue;
                    }
                    this.log.warn("Authentication scheme " + id + " not supported");
                    continue;
                }
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Challenge for " + id + " authentication scheme not available");
            }
        }
        if (authScheme == null) {
            throw new AuthenticationException("Unable to respond to any of these challenges: " + challenges);
        }
        return authScheme;
    }
    
    static {
        DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList((List<? extends String>)Arrays.asList("negotiate", "NTLM", "Digest", "Basic"));
    }
}
