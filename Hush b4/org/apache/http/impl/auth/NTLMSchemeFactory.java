// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.auth;

import org.apache.http.protocol.HttpContext;
import org.apache.http.auth.AuthScheme;
import org.apache.http.params.HttpParams;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthSchemeFactory;

@Immutable
public class NTLMSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider
{
    public AuthScheme newInstance(final HttpParams params) {
        return new NTLMScheme();
    }
    
    public AuthScheme create(final HttpContext context) {
        return new NTLMScheme();
    }
}
