// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.ssl;

import java.security.PrivateKey;
import java.util.Map;
import java.util.HashMap;
import java.net.Socket;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.KeyManagementException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.KeyManagerFactory;
import java.security.UnrecoverableKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.util.HashSet;
import java.security.SecureRandom;
import javax.net.ssl.TrustManager;
import javax.net.ssl.KeyManager;
import java.util.Set;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class SSLContextBuilder
{
    static final String TLS = "TLS";
    static final String SSL = "SSL";
    private String protocol;
    private Set<KeyManager> keymanagers;
    private Set<TrustManager> trustmanagers;
    private SecureRandom secureRandom;
    
    public SSLContextBuilder() {
        this.keymanagers = new HashSet<KeyManager>();
        this.trustmanagers = new HashSet<TrustManager>();
    }
    
    public SSLContextBuilder useTLS() {
        this.protocol = "TLS";
        return this;
    }
    
    public SSLContextBuilder useSSL() {
        this.protocol = "SSL";
        return this;
    }
    
    public SSLContextBuilder useProtocol(final String protocol) {
        this.protocol = protocol;
        return this;
    }
    
    public SSLContextBuilder setSecureRandom(final SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
        return this;
    }
    
    public SSLContextBuilder loadTrustMaterial(final KeyStore truststore, final TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
        final TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmfactory.init(truststore);
        final TrustManager[] tms = tmfactory.getTrustManagers();
        if (tms != null) {
            if (trustStrategy != null) {
                for (int i = 0; i < tms.length; ++i) {
                    final TrustManager tm = tms[i];
                    if (tm instanceof X509TrustManager) {
                        tms[i] = new TrustManagerDelegate((X509TrustManager)tm, trustStrategy);
                    }
                }
            }
            for (final TrustManager tm2 : tms) {
                this.trustmanagers.add(tm2);
            }
        }
        return this;
    }
    
    public SSLContextBuilder loadTrustMaterial(final KeyStore truststore) throws NoSuchAlgorithmException, KeyStoreException {
        return this.loadTrustMaterial(truststore, null);
    }
    
    public SSLContextBuilder loadKeyMaterial(final KeyStore keystore, final char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        this.loadKeyMaterial(keystore, keyPassword, null);
        return this;
    }
    
    public SSLContextBuilder loadKeyMaterial(final KeyStore keystore, final char[] keyPassword, final PrivateKeyStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        final KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keystore, keyPassword);
        final KeyManager[] kms = kmfactory.getKeyManagers();
        if (kms != null) {
            if (aliasStrategy != null) {
                for (int i = 0; i < kms.length; ++i) {
                    final KeyManager km = kms[i];
                    if (km instanceof X509KeyManager) {
                        kms[i] = new KeyManagerDelegate((X509KeyManager)km, aliasStrategy);
                    }
                }
            }
            for (final KeyManager km2 : kms) {
                this.keymanagers.add(km2);
            }
        }
        return this;
    }
    
    public SSLContext build() throws NoSuchAlgorithmException, KeyManagementException {
        final SSLContext sslcontext = SSLContext.getInstance((this.protocol != null) ? this.protocol : "TLS");
        sslcontext.init((KeyManager[])(this.keymanagers.isEmpty() ? null : ((KeyManager[])this.keymanagers.toArray(new KeyManager[this.keymanagers.size()]))), (TrustManager[])(this.trustmanagers.isEmpty() ? null : ((TrustManager[])this.trustmanagers.toArray(new TrustManager[this.trustmanagers.size()]))), this.secureRandom);
        return sslcontext;
    }
    
    static class TrustManagerDelegate implements X509TrustManager
    {
        private final X509TrustManager trustManager;
        private final TrustStrategy trustStrategy;
        
        TrustManagerDelegate(final X509TrustManager trustManager, final TrustStrategy trustStrategy) {
            this.trustManager = trustManager;
            this.trustStrategy = trustStrategy;
        }
        
        public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
            this.trustManager.checkClientTrusted(chain, authType);
        }
        
        public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
            if (!this.trustStrategy.isTrusted(chain, authType)) {
                this.trustManager.checkServerTrusted(chain, authType);
            }
        }
        
        public X509Certificate[] getAcceptedIssuers() {
            return this.trustManager.getAcceptedIssuers();
        }
    }
    
    static class KeyManagerDelegate implements X509KeyManager
    {
        private final X509KeyManager keyManager;
        private final PrivateKeyStrategy aliasStrategy;
        
        KeyManagerDelegate(final X509KeyManager keyManager, final PrivateKeyStrategy aliasStrategy) {
            this.keyManager = keyManager;
            this.aliasStrategy = aliasStrategy;
        }
        
        public String[] getClientAliases(final String keyType, final Principal[] issuers) {
            return this.keyManager.getClientAliases(keyType, issuers);
        }
        
        public String chooseClientAlias(final String[] keyTypes, final Principal[] issuers, final Socket socket) {
            final Map<String, PrivateKeyDetails> validAliases = new HashMap<String, PrivateKeyDetails>();
            for (final String keyType : keyTypes) {
                final String[] aliases = this.keyManager.getClientAliases(keyType, issuers);
                if (aliases != null) {
                    for (final String alias : aliases) {
                        validAliases.put(alias, new PrivateKeyDetails(keyType, this.keyManager.getCertificateChain(alias)));
                    }
                }
            }
            return this.aliasStrategy.chooseAlias(validAliases, socket);
        }
        
        public String[] getServerAliases(final String keyType, final Principal[] issuers) {
            return this.keyManager.getServerAliases(keyType, issuers);
        }
        
        public String chooseServerAlias(final String keyType, final Principal[] issuers, final Socket socket) {
            final Map<String, PrivateKeyDetails> validAliases = new HashMap<String, PrivateKeyDetails>();
            final String[] aliases = this.keyManager.getServerAliases(keyType, issuers);
            if (aliases != null) {
                for (final String alias : aliases) {
                    validAliases.put(alias, new PrivateKeyDetails(keyType, this.keyManager.getCertificateChain(alias)));
                }
            }
            return this.aliasStrategy.chooseAlias(validAliases, socket);
        }
        
        public X509Certificate[] getCertificateChain(final String alias) {
            return this.keyManager.getCertificateChain(alias);
        }
        
        public PrivateKey getPrivateKey(final String alias) {
            return this.keyManager.getPrivateKey(alias);
        }
    }
}
