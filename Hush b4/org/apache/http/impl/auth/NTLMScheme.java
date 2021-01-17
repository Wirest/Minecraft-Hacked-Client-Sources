// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.auth;

import org.apache.http.message.BufferedHeader;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.auth.NTCredentials;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.Args;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class NTLMScheme extends AuthSchemeBase
{
    private final NTLMEngine engine;
    private State state;
    private String challenge;
    
    public NTLMScheme(final NTLMEngine engine) {
        Args.notNull(engine, "NTLM engine");
        this.engine = engine;
        this.state = State.UNINITIATED;
        this.challenge = null;
    }
    
    public NTLMScheme() {
        this(new NTLMEngineImpl());
    }
    
    public String getSchemeName() {
        return "ntlm";
    }
    
    public String getParameter(final String name) {
        return null;
    }
    
    public String getRealm() {
        return null;
    }
    
    public boolean isConnectionBased() {
        return true;
    }
    
    @Override
    protected void parseChallenge(final CharArrayBuffer buffer, final int beginIndex, final int endIndex) throws MalformedChallengeException {
        this.challenge = buffer.substringTrimmed(beginIndex, endIndex);
        if (this.challenge.length() == 0) {
            if (this.state == State.UNINITIATED) {
                this.state = State.CHALLENGE_RECEIVED;
            }
            else {
                this.state = State.FAILED;
            }
        }
        else {
            if (this.state.compareTo(State.MSG_TYPE1_GENERATED) < 0) {
                this.state = State.FAILED;
                throw new MalformedChallengeException("Out of sequence NTLM response message");
            }
            if (this.state == State.MSG_TYPE1_GENERATED) {
                this.state = State.MSG_TYPE2_RECEVIED;
            }
        }
    }
    
    public Header authenticate(final Credentials credentials, final HttpRequest request) throws AuthenticationException {
        NTCredentials ntcredentials = null;
        try {
            ntcredentials = (NTCredentials)credentials;
        }
        catch (ClassCastException e) {
            throw new InvalidCredentialsException("Credentials cannot be used for NTLM authentication: " + credentials.getClass().getName());
        }
        String response = null;
        if (this.state == State.FAILED) {
            throw new AuthenticationException("NTLM authentication failed");
        }
        if (this.state == State.CHALLENGE_RECEIVED) {
            response = this.engine.generateType1Msg(ntcredentials.getDomain(), ntcredentials.getWorkstation());
            this.state = State.MSG_TYPE1_GENERATED;
        }
        else {
            if (this.state != State.MSG_TYPE2_RECEVIED) {
                throw new AuthenticationException("Unexpected state: " + this.state);
            }
            response = this.engine.generateType3Msg(ntcredentials.getUserName(), ntcredentials.getPassword(), ntcredentials.getDomain(), ntcredentials.getWorkstation(), this.challenge);
            this.state = State.MSG_TYPE3_GENERATED;
        }
        final CharArrayBuffer buffer = new CharArrayBuffer(32);
        if (this.isProxy()) {
            buffer.append("Proxy-Authorization");
        }
        else {
            buffer.append("Authorization");
        }
        buffer.append(": NTLM ");
        buffer.append(response);
        return new BufferedHeader(buffer);
    }
    
    public boolean isComplete() {
        return this.state == State.MSG_TYPE3_GENERATED || this.state == State.FAILED;
    }
    
    enum State
    {
        UNINITIATED, 
        CHALLENGE_RECEIVED, 
        MSG_TYPE1_GENERATED, 
        MSG_TYPE2_RECEVIED, 
        MSG_TYPE3_GENERATED, 
        FAILED;
    }
}
