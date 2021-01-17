// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.TrustManager;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import org.apache.http.annotation.Immutable;

@Immutable
public class SSLContexts
{
    public static SSLContext createDefault() throws SSLInitializationException {
        try {
            final SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, null, null);
            return sslcontext;
        }
        catch (NoSuchAlgorithmException ex) {
            throw new SSLInitializationException(ex.getMessage(), ex);
        }
        catch (KeyManagementException ex2) {
            throw new SSLInitializationException(ex2.getMessage(), ex2);
        }
    }
    
    public static SSLContext createSystemDefault() throws SSLInitializationException {
        try {
            return SSLContext.getInstance("Default");
        }
        catch (NoSuchAlgorithmException ex) {
            return createDefault();
        }
    }
    
    public static SSLContextBuilder custom() {
        return new SSLContextBuilder();
    }
}
