// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.auth;

import java.util.Locale;
import org.apache.http.HeaderElement;
import org.apache.http.message.HeaderValueParser;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.message.ParserCursor;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.HttpRequest;
import org.apache.http.Consts;
import java.util.HashMap;
import org.apache.http.auth.ChallengeState;
import java.nio.charset.Charset;
import java.util.Map;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public abstract class RFC2617Scheme extends AuthSchemeBase
{
    private final Map<String, String> params;
    private final Charset credentialsCharset;
    
    @Deprecated
    public RFC2617Scheme(final ChallengeState challengeState) {
        super(challengeState);
        this.params = new HashMap<String, String>();
        this.credentialsCharset = Consts.ASCII;
    }
    
    public RFC2617Scheme(final Charset credentialsCharset) {
        this.params = new HashMap<String, String>();
        this.credentialsCharset = ((credentialsCharset != null) ? credentialsCharset : Consts.ASCII);
    }
    
    public RFC2617Scheme() {
        this(Consts.ASCII);
    }
    
    public Charset getCredentialsCharset() {
        return this.credentialsCharset;
    }
    
    String getCredentialsCharset(final HttpRequest request) {
        String charset = (String)request.getParams().getParameter("http.auth.credential-charset");
        if (charset == null) {
            charset = this.getCredentialsCharset().name();
        }
        return charset;
    }
    
    @Override
    protected void parseChallenge(final CharArrayBuffer buffer, final int pos, final int len) throws MalformedChallengeException {
        final HeaderValueParser parser = BasicHeaderValueParser.INSTANCE;
        final ParserCursor cursor = new ParserCursor(pos, buffer.length());
        final HeaderElement[] elements = parser.parseElements(buffer, cursor);
        if (elements.length == 0) {
            throw new MalformedChallengeException("Authentication challenge is empty");
        }
        this.params.clear();
        for (final HeaderElement element : elements) {
            this.params.put(element.getName(), element.getValue());
        }
    }
    
    protected Map<String, String> getParameters() {
        return this.params;
    }
    
    public String getParameter(final String name) {
        if (name == null) {
            return null;
        }
        return this.params.get(name.toLowerCase(Locale.ENGLISH));
    }
    
    public String getRealm() {
        return this.getParameter("realm");
    }
}
