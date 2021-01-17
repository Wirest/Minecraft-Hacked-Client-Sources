// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.auth;

import java.util.Locale;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.FormattedHeader;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.util.Args;
import org.apache.http.Header;
import org.apache.http.auth.ChallengeState;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.ContextAwareAuthScheme;

@NotThreadSafe
public abstract class AuthSchemeBase implements ContextAwareAuthScheme
{
    private ChallengeState challengeState;
    
    @Deprecated
    public AuthSchemeBase(final ChallengeState challengeState) {
        this.challengeState = challengeState;
    }
    
    public AuthSchemeBase() {
    }
    
    public void processChallenge(final Header header) throws MalformedChallengeException {
        Args.notNull(header, "Header");
        final String authheader = header.getName();
        if (authheader.equalsIgnoreCase("WWW-Authenticate")) {
            this.challengeState = ChallengeState.TARGET;
        }
        else {
            if (!authheader.equalsIgnoreCase("Proxy-Authenticate")) {
                throw new MalformedChallengeException("Unexpected header name: " + authheader);
            }
            this.challengeState = ChallengeState.PROXY;
        }
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
        if (!s2.equalsIgnoreCase(this.getSchemeName())) {
            throw new MalformedChallengeException("Invalid scheme identifier: " + s2);
        }
        this.parseChallenge(buffer, pos, buffer.length());
    }
    
    public Header authenticate(final Credentials credentials, final HttpRequest request, final HttpContext context) throws AuthenticationException {
        return this.authenticate(credentials, request);
    }
    
    protected abstract void parseChallenge(final CharArrayBuffer p0, final int p1, final int p2) throws MalformedChallengeException;
    
    public boolean isProxy() {
        return this.challengeState != null && this.challengeState == ChallengeState.PROXY;
    }
    
    public ChallengeState getChallengeState() {
        return this.challengeState;
    }
    
    @Override
    public String toString() {
        final String name = this.getSchemeName();
        if (name != null) {
            return name.toUpperCase(Locale.US);
        }
        return super.toString();
    }
}
