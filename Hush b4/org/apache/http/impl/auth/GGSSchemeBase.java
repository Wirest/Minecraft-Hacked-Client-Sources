// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.auth;

import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.HttpHost;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.Credentials;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import org.ietf.jgss.GSSManager;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public abstract class GGSSchemeBase extends AuthSchemeBase
{
    private final Log log;
    private final Base64 base64codec;
    private final boolean stripPort;
    private State state;
    private byte[] token;
    
    GGSSchemeBase(final boolean stripPort) {
        this.log = LogFactory.getLog(this.getClass());
        this.base64codec = new Base64(0);
        this.stripPort = stripPort;
        this.state = State.UNINITIATED;
    }
    
    GGSSchemeBase() {
        this(false);
    }
    
    protected GSSManager getManager() {
        return GSSManager.getInstance();
    }
    
    protected byte[] generateGSSToken(final byte[] input, final Oid oid, final String authServer) throws GSSException {
        byte[] token = input;
        if (token == null) {
            token = new byte[0];
        }
        final GSSManager manager = this.getManager();
        final GSSName serverName = manager.createName("HTTP@" + authServer, GSSName.NT_HOSTBASED_SERVICE);
        final GSSContext gssContext = manager.createContext(serverName.canonicalize(oid), oid, null, 0);
        gssContext.requestMutualAuth(true);
        gssContext.requestCredDeleg(true);
        return gssContext.initSecContext(token, 0, token.length);
    }
    
    protected abstract byte[] generateToken(final byte[] p0, final String p1) throws GSSException;
    
    public boolean isComplete() {
        return this.state == State.TOKEN_GENERATED || this.state == State.FAILED;
    }
    
    @Deprecated
    public Header authenticate(final Credentials credentials, final HttpRequest request) throws AuthenticationException {
        return this.authenticate(credentials, request, null);
    }
    
    @Override
    public Header authenticate(final Credentials credentials, final HttpRequest request, final HttpContext context) throws AuthenticationException {
        Args.notNull(request, "HTTP request");
        switch (this.state) {
            case UNINITIATED: {
                throw new AuthenticationException(this.getSchemeName() + " authentication has not been initiated");
            }
            case FAILED: {
                throw new AuthenticationException(this.getSchemeName() + " authentication has failed");
            }
            case CHALLENGE_RECEIVED: {
                try {
                    final HttpRoute route = (HttpRoute)context.getAttribute("http.route");
                    if (route == null) {
                        throw new AuthenticationException("Connection route is not available");
                    }
                    HttpHost host;
                    if (this.isProxy()) {
                        host = route.getProxyHost();
                        if (host == null) {
                            host = route.getTargetHost();
                        }
                    }
                    else {
                        host = route.getTargetHost();
                    }
                    String authServer;
                    if (!this.stripPort && host.getPort() > 0) {
                        authServer = host.toHostString();
                    }
                    else {
                        authServer = host.getHostName();
                    }
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("init " + authServer);
                    }
                    this.token = this.generateToken(this.token, authServer);
                    this.state = State.TOKEN_GENERATED;
                }
                catch (GSSException gsse) {
                    this.state = State.FAILED;
                    if (gsse.getMajor() == 9 || gsse.getMajor() == 8) {
                        throw new InvalidCredentialsException(gsse.getMessage(), gsse);
                    }
                    if (gsse.getMajor() == 13) {
                        throw new InvalidCredentialsException(gsse.getMessage(), gsse);
                    }
                    if (gsse.getMajor() == 10 || gsse.getMajor() == 19 || gsse.getMajor() == 20) {
                        throw new AuthenticationException(gsse.getMessage(), gsse);
                    }
                    throw new AuthenticationException(gsse.getMessage());
                }
            }
            case TOKEN_GENERATED: {
                final String tokenstr = new String(this.base64codec.encode(this.token));
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Sending response '" + tokenstr + "' back to the auth server");
                }
                final CharArrayBuffer buffer = new CharArrayBuffer(32);
                if (this.isProxy()) {
                    buffer.append("Proxy-Authorization");
                }
                else {
                    buffer.append("Authorization");
                }
                buffer.append(": Negotiate ");
                buffer.append(tokenstr);
                return new BufferedHeader(buffer);
            }
            default: {
                throw new IllegalStateException("Illegal state: " + this.state);
            }
        }
    }
    
    @Override
    protected void parseChallenge(final CharArrayBuffer buffer, final int beginIndex, final int endIndex) throws MalformedChallengeException {
        final String challenge = buffer.substringTrimmed(beginIndex, endIndex);
        if (this.log.isDebugEnabled()) {
            this.log.debug("Received challenge '" + challenge + "' from the auth server");
        }
        if (this.state == State.UNINITIATED) {
            this.token = Base64.decodeBase64(challenge.getBytes());
            this.state = State.CHALLENGE_RECEIVED;
        }
        else {
            this.log.debug("Authentication already attempted");
            this.state = State.FAILED;
        }
    }
    
    enum State
    {
        UNINITIATED, 
        CHALLENGE_RECEIVED, 
        TOKEN_GENERATED, 
        FAILED;
    }
}
