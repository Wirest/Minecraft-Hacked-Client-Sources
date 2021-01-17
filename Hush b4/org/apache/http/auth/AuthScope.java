// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.auth;

import org.apache.http.util.LangUtils;
import org.apache.http.util.Args;
import org.apache.http.HttpHost;
import java.util.Locale;
import org.apache.http.annotation.Immutable;

@Immutable
public class AuthScope
{
    public static final String ANY_HOST;
    public static final int ANY_PORT = -1;
    public static final String ANY_REALM;
    public static final String ANY_SCHEME;
    public static final AuthScope ANY;
    private final String scheme;
    private final String realm;
    private final String host;
    private final int port;
    
    public AuthScope(final String host, final int port, final String realm, final String scheme) {
        this.host = ((host == null) ? AuthScope.ANY_HOST : host.toLowerCase(Locale.ENGLISH));
        this.port = ((port < 0) ? -1 : port);
        this.realm = ((realm == null) ? AuthScope.ANY_REALM : realm);
        this.scheme = ((scheme == null) ? AuthScope.ANY_SCHEME : scheme.toUpperCase(Locale.ENGLISH));
    }
    
    public AuthScope(final HttpHost host, final String realm, final String schemeName) {
        this(host.getHostName(), host.getPort(), realm, schemeName);
    }
    
    public AuthScope(final HttpHost host) {
        this(host, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
    }
    
    public AuthScope(final String host, final int port, final String realm) {
        this(host, port, realm, AuthScope.ANY_SCHEME);
    }
    
    public AuthScope(final String host, final int port) {
        this(host, port, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
    }
    
    public AuthScope(final AuthScope authscope) {
        Args.notNull(authscope, "Scope");
        this.host = authscope.getHost();
        this.port = authscope.getPort();
        this.realm = authscope.getRealm();
        this.scheme = authscope.getScheme();
    }
    
    public String getHost() {
        return this.host;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public String getRealm() {
        return this.realm;
    }
    
    public String getScheme() {
        return this.scheme;
    }
    
    public int match(final AuthScope that) {
        int factor = 0;
        if (LangUtils.equals(this.scheme, that.scheme)) {
            ++factor;
        }
        else if (this.scheme != AuthScope.ANY_SCHEME && that.scheme != AuthScope.ANY_SCHEME) {
            return -1;
        }
        if (LangUtils.equals(this.realm, that.realm)) {
            factor += 2;
        }
        else if (this.realm != AuthScope.ANY_REALM && that.realm != AuthScope.ANY_REALM) {
            return -1;
        }
        if (this.port == that.port) {
            factor += 4;
        }
        else if (this.port != -1 && that.port != -1) {
            return -1;
        }
        if (LangUtils.equals(this.host, that.host)) {
            factor += 8;
        }
        else if (this.host != AuthScope.ANY_HOST && that.host != AuthScope.ANY_HOST) {
            return -1;
        }
        return factor;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof AuthScope)) {
            return super.equals(o);
        }
        final AuthScope that = (AuthScope)o;
        return LangUtils.equals(this.host, that.host) && this.port == that.port && LangUtils.equals(this.realm, that.realm) && LangUtils.equals(this.scheme, that.scheme);
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        if (this.scheme != null) {
            buffer.append(this.scheme.toUpperCase(Locale.ENGLISH));
            buffer.append(' ');
        }
        if (this.realm != null) {
            buffer.append('\'');
            buffer.append(this.realm);
            buffer.append('\'');
        }
        else {
            buffer.append("<any realm>");
        }
        if (this.host != null) {
            buffer.append('@');
            buffer.append(this.host);
            if (this.port >= 0) {
                buffer.append(':');
                buffer.append(this.port);
            }
        }
        return buffer.toString();
    }
    
    @Override
    public int hashCode() {
        int hash = 17;
        hash = LangUtils.hashCode(hash, this.host);
        hash = LangUtils.hashCode(hash, this.port);
        hash = LangUtils.hashCode(hash, this.realm);
        hash = LangUtils.hashCode(hash, this.scheme);
        return hash;
    }
    
    static {
        ANY_HOST = null;
        ANY_REALM = null;
        ANY_SCHEME = null;
        ANY = new AuthScope(AuthScope.ANY_HOST, -1, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
    }
}
