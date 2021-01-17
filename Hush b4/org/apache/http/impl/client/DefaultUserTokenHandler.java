// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.auth.Credentials;
import org.apache.http.auth.AuthScheme;
import javax.net.ssl.SSLSession;
import org.apache.http.HttpConnection;
import org.apache.http.auth.AuthState;
import java.security.Principal;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.UserTokenHandler;

@Immutable
public class DefaultUserTokenHandler implements UserTokenHandler
{
    public static final DefaultUserTokenHandler INSTANCE;
    
    public Object getUserToken(final HttpContext context) {
        final HttpClientContext clientContext = HttpClientContext.adapt(context);
        Principal userPrincipal = null;
        final AuthState targetAuthState = clientContext.getTargetAuthState();
        if (targetAuthState != null) {
            userPrincipal = getAuthPrincipal(targetAuthState);
            if (userPrincipal == null) {
                final AuthState proxyAuthState = clientContext.getProxyAuthState();
                userPrincipal = getAuthPrincipal(proxyAuthState);
            }
        }
        if (userPrincipal == null) {
            final HttpConnection conn = clientContext.getConnection();
            if (conn.isOpen() && conn instanceof ManagedHttpClientConnection) {
                final SSLSession sslsession = ((ManagedHttpClientConnection)conn).getSSLSession();
                if (sslsession != null) {
                    userPrincipal = sslsession.getLocalPrincipal();
                }
            }
        }
        return userPrincipal;
    }
    
    private static Principal getAuthPrincipal(final AuthState authState) {
        final AuthScheme scheme = authState.getAuthScheme();
        if (scheme != null && scheme.isComplete() && scheme.isConnectionBased()) {
            final Credentials creds = authState.getCredentials();
            if (creds != null) {
                return creds.getUserPrincipal();
            }
        }
        return null;
    }
    
    static {
        INSTANCE = new DefaultUserTokenHandler();
    }
}
