// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.protocol.HttpContext;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.HttpHost;
import org.apache.commons.logging.Log;

@Deprecated
public class HttpAuthenticator extends org.apache.http.impl.auth.HttpAuthenticator
{
    public HttpAuthenticator(final Log log) {
        super(log);
    }
    
    public HttpAuthenticator() {
    }
    
    public boolean authenticate(final HttpHost host, final HttpResponse response, final AuthenticationStrategy authStrategy, final AuthState authState, final HttpContext context) {
        return this.handleAuthChallenge(host, response, authStrategy, authState, context);
    }
}
