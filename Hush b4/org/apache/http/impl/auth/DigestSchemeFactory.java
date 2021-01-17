// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.auth;

import org.apache.http.protocol.HttpContext;
import org.apache.http.auth.AuthScheme;
import org.apache.http.params.HttpParams;
import java.nio.charset.Charset;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthSchemeFactory;

@Immutable
public class DigestSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider
{
    private final Charset charset;
    
    public DigestSchemeFactory(final Charset charset) {
        this.charset = charset;
    }
    
    public DigestSchemeFactory() {
        this(null);
    }
    
    public AuthScheme newInstance(final HttpParams params) {
        return new DigestScheme();
    }
    
    public AuthScheme create(final HttpContext context) {
        return new DigestScheme(this.charset);
    }
}
