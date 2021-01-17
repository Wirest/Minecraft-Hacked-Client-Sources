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
public class BasicSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider
{
    private final Charset charset;
    
    public BasicSchemeFactory(final Charset charset) {
        this.charset = charset;
    }
    
    public BasicSchemeFactory() {
        this(null);
    }
    
    public AuthScheme newInstance(final HttpParams params) {
        return new BasicScheme();
    }
    
    public AuthScheme create(final HttpContext context) {
        return new BasicScheme(this.charset);
    }
}
