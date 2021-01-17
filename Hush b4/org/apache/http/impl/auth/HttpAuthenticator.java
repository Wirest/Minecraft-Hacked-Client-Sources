// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.auth;

import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.util.Asserts;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthOption;
import java.util.Queue;
import java.util.Map;
import org.apache.http.auth.MalformedChallengeException;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.protocol.HttpContext;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.HttpHost;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class HttpAuthenticator
{
    private final Log log;
    
    public HttpAuthenticator(final Log log) {
        this.log = ((log != null) ? log : LogFactory.getLog(this.getClass()));
    }
    
    public HttpAuthenticator() {
        this(null);
    }
    
    public boolean isAuthenticationRequested(final HttpHost host, final HttpResponse response, final AuthenticationStrategy authStrategy, final AuthState authState, final HttpContext context) {
        if (authStrategy.isAuthenticationRequested(host, response, context)) {
            this.log.debug("Authentication required");
            if (authState.getState() == AuthProtocolState.SUCCESS) {
                authStrategy.authFailed(host, authState.getAuthScheme(), context);
            }
            return true;
        }
        switch (authState.getState()) {
            case CHALLENGED:
            case HANDSHAKE: {
                this.log.debug("Authentication succeeded");
                authState.setState(AuthProtocolState.SUCCESS);
                authStrategy.authSucceeded(host, authState.getAuthScheme(), context);
                break;
            }
            case SUCCESS: {
                break;
            }
            default: {
                authState.setState(AuthProtocolState.UNCHALLENGED);
                break;
            }
        }
        return false;
    }
    
    public boolean handleAuthChallenge(final HttpHost host, final HttpResponse response, final AuthenticationStrategy authStrategy, final AuthState authState, final HttpContext context) {
        try {
            if (this.log.isDebugEnabled()) {
                this.log.debug(host.toHostString() + " requested authentication");
            }
            final Map<String, Header> challenges = authStrategy.getChallenges(host, response, context);
            if (challenges.isEmpty()) {
                this.log.debug("Response contains no authentication challenges");
                return false;
            }
            final AuthScheme authScheme = authState.getAuthScheme();
            switch (authState.getState()) {
                case FAILURE: {
                    return false;
                }
                case SUCCESS: {
                    authState.reset();
                    break;
                }
                case CHALLENGED:
                case HANDSHAKE: {
                    if (authScheme == null) {
                        this.log.debug("Auth scheme is null");
                        authStrategy.authFailed(host, null, context);
                        authState.reset();
                        authState.setState(AuthProtocolState.FAILURE);
                        return false;
                    }
                }
                case UNCHALLENGED: {
                    if (authScheme == null) {
                        break;
                    }
                    final String id = authScheme.getSchemeName();
                    final Header challenge = challenges.get(id.toLowerCase(Locale.US));
                    if (challenge == null) {
                        authState.reset();
                        break;
                    }
                    this.log.debug("Authorization challenge processed");
                    authScheme.processChallenge(challenge);
                    if (authScheme.isComplete()) {
                        this.log.debug("Authentication failed");
                        authStrategy.authFailed(host, authState.getAuthScheme(), context);
                        authState.reset();
                        authState.setState(AuthProtocolState.FAILURE);
                        return false;
                    }
                    authState.setState(AuthProtocolState.HANDSHAKE);
                    return true;
                }
            }
            final Queue<AuthOption> authOptions = authStrategy.select(challenges, host, response, context);
            if (authOptions != null && !authOptions.isEmpty()) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Selected authentication options: " + authOptions);
                }
                authState.setState(AuthProtocolState.CHALLENGED);
                authState.update(authOptions);
                return true;
            }
            return false;
        }
        catch (MalformedChallengeException ex) {
            if (this.log.isWarnEnabled()) {
                this.log.warn("Malformed challenge: " + ex.getMessage());
            }
            authState.reset();
            return false;
        }
    }
    
    public void generateAuthResponse(final HttpRequest request, final AuthState authState, final HttpContext context) throws HttpException, IOException {
        AuthScheme authScheme = authState.getAuthScheme();
        Credentials creds = authState.getCredentials();
        switch (authState.getState()) {
            case FAILURE: {
                return;
            }
            case SUCCESS: {
                this.ensureAuthScheme(authScheme);
                if (authScheme.isConnectionBased()) {
                    return;
                }
                break;
            }
            case CHALLENGED: {
                final Queue<AuthOption> authOptions = authState.getAuthOptions();
                if (authOptions != null) {
                    while (!authOptions.isEmpty()) {
                        final AuthOption authOption = authOptions.remove();
                        authScheme = authOption.getAuthScheme();
                        creds = authOption.getCredentials();
                        authState.update(authScheme, creds);
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("Generating response to an authentication challenge using " + authScheme.getSchemeName() + " scheme");
                        }
                        try {
                            final Header header = this.doAuth(authScheme, creds, request, context);
                            request.addHeader(header);
                        }
                        catch (AuthenticationException ex) {
                            if (!this.log.isWarnEnabled()) {
                                continue;
                            }
                            this.log.warn(authScheme + " authentication error: " + ex.getMessage());
                            continue;
                        }
                        break;
                    }
                    return;
                }
                this.ensureAuthScheme(authScheme);
                break;
            }
        }
        if (authScheme != null) {
            try {
                final Header header2 = this.doAuth(authScheme, creds, request, context);
                request.addHeader(header2);
            }
            catch (AuthenticationException ex2) {
                if (this.log.isErrorEnabled()) {
                    this.log.error(authScheme + " authentication error: " + ex2.getMessage());
                }
            }
        }
    }
    
    private void ensureAuthScheme(final AuthScheme authScheme) {
        Asserts.notNull(authScheme, "Auth scheme");
    }
    
    private Header doAuth(final AuthScheme authScheme, final Credentials creds, final HttpRequest request, final HttpContext context) throws AuthenticationException {
        if (authScheme instanceof ContextAwareAuthScheme) {
            return ((ContextAwareAuthScheme)authScheme).authenticate(creds, request, context);
        }
        return authScheme.authenticate(creds, request);
    }
}
