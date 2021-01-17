// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.util.Args;
import java.net.URL;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.Authenticator;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.AuthScope;
import java.util.Map;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.CredentialsProvider;

@ThreadSafe
public class SystemDefaultCredentialsProvider implements CredentialsProvider
{
    private static final Map<String, String> SCHEME_MAP;
    private final BasicCredentialsProvider internal;
    
    private static String translateScheme(final String key) {
        if (key == null) {
            return null;
        }
        final String s = SystemDefaultCredentialsProvider.SCHEME_MAP.get(key);
        return (s != null) ? s : key;
    }
    
    public SystemDefaultCredentialsProvider() {
        this.internal = new BasicCredentialsProvider();
    }
    
    public void setCredentials(final AuthScope authscope, final Credentials credentials) {
        this.internal.setCredentials(authscope, credentials);
    }
    
    private static PasswordAuthentication getSystemCreds(final AuthScope authscope, final Authenticator.RequestorType requestorType) {
        final String hostname = authscope.getHost();
        final int port = authscope.getPort();
        final String protocol = (port == 443) ? "https" : "http";
        return Authenticator.requestPasswordAuthentication(hostname, null, port, protocol, null, translateScheme(authscope.getScheme()), null, requestorType);
    }
    
    public Credentials getCredentials(final AuthScope authscope) {
        Args.notNull(authscope, "Auth scope");
        final Credentials localcreds = this.internal.getCredentials(authscope);
        if (localcreds != null) {
            return localcreds;
        }
        if (authscope.getHost() != null) {
            PasswordAuthentication systemcreds = getSystemCreds(authscope, Authenticator.RequestorType.SERVER);
            if (systemcreds == null) {
                systemcreds = getSystemCreds(authscope, Authenticator.RequestorType.PROXY);
            }
            if (systemcreds != null) {
                final String domain = System.getProperty("http.auth.ntlm.domain");
                if (domain != null) {
                    return new NTCredentials(systemcreds.getUserName(), new String(systemcreds.getPassword()), null, domain);
                }
                if ("NTLM".equalsIgnoreCase(authscope.getScheme())) {
                    return new NTCredentials(systemcreds.getUserName(), new String(systemcreds.getPassword()), null, null);
                }
                return new UsernamePasswordCredentials(systemcreds.getUserName(), new String(systemcreds.getPassword()));
            }
        }
        return null;
    }
    
    public void clear() {
        this.internal.clear();
    }
    
    static {
        (SCHEME_MAP = new ConcurrentHashMap<String, String>()).put("Basic".toUpperCase(Locale.ENGLISH), "Basic");
        SystemDefaultCredentialsProvider.SCHEME_MAP.put("Digest".toUpperCase(Locale.ENGLISH), "Digest");
        SystemDefaultCredentialsProvider.SCHEME_MAP.put("NTLM".toUpperCase(Locale.ENGLISH), "NTLM");
        SystemDefaultCredentialsProvider.SCHEME_MAP.put("negotiate".toUpperCase(Locale.ENGLISH), "SPNEGO");
        SystemDefaultCredentialsProvider.SCHEME_MAP.put("Kerberos".toUpperCase(Locale.ENGLISH), "Kerberos");
    }
}
