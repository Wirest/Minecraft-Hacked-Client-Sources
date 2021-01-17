// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TrustSelfSignedStrategy implements TrustStrategy
{
    public boolean isTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        return chain.length == 1;
    }
}
