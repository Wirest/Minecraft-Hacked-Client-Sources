// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.protocol;

import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.util.Asserts;
import org.apache.http.Header;
import java.util.Queue;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.AuthOption;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthState;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.http.HttpRequestInterceptor;

@Deprecated
abstract class RequestAuthenticationBase implements HttpRequestInterceptor
{
    final Log log;
    
    public RequestAuthenticationBase() {
        this.log = LogFactory.getLog(this.getClass());
    }
    
    void process(final AuthState authState, final HttpRequest request, final HttpContext context) {
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
                            final Header header = this.authenticate(authScheme, creds, request, context);
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
                final Header header2 = this.authenticate(authScheme, creds, request, context);
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
    
    private Header authenticate(final AuthScheme authScheme, final Credentials creds, final HttpRequest request, final HttpContext context) throws AuthenticationException {
        Asserts.notNull(authScheme, "Auth scheme");
        if (authScheme instanceof ContextAwareAuthScheme) {
            return ((ContextAwareAuthScheme)authScheme).authenticate(creds, request, context);
        }
        return authScheme.authenticate(creds, request);
    }
}
