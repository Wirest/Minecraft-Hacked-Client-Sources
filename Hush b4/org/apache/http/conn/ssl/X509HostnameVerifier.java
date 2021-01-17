// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.ssl;

import javax.net.ssl.SSLException;
import java.security.cert.X509Certificate;
import java.io.IOException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.HostnameVerifier;

public interface X509HostnameVerifier extends HostnameVerifier
{
    void verify(final String p0, final SSLSocket p1) throws IOException;
    
    void verify(final String p0, final X509Certificate p1) throws SSLException;
    
    void verify(final String p0, final String[] p1, final String[] p2) throws SSLException;
}
