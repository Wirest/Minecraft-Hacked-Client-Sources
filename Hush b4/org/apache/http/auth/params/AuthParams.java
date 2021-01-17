// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.auth.params;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.params.HttpParams;
import org.apache.http.annotation.Immutable;

@Deprecated
@Immutable
public final class AuthParams
{
    private AuthParams() {
    }
    
    public static String getCredentialCharset(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        String charset = (String)params.getParameter("http.auth.credential-charset");
        if (charset == null) {
            charset = HTTP.DEF_PROTOCOL_CHARSET.name();
        }
        return charset;
    }
    
    public static void setCredentialCharset(final HttpParams params, final String charset) {
        Args.notNull(params, "HTTP parameters");
        params.setParameter("http.auth.credential-charset", charset);
    }
}
