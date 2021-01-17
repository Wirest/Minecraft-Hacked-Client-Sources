// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.ssl;

import javax.net.ssl.SSLEngine;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.SSLException;
import java.io.File;

public abstract class SslContext
{
    public static SslProvider defaultServerProvider() {
        if (OpenSsl.isAvailable()) {
            return SslProvider.OPENSSL;
        }
        return SslProvider.JDK;
    }
    
    public static SslProvider defaultClientProvider() {
        return SslProvider.JDK;
    }
    
    public static SslContext newServerContext(final File certChainFile, final File keyFile) throws SSLException {
        return newServerContext(null, certChainFile, keyFile, null, null, null, 0L, 0L);
    }
    
    public static SslContext newServerContext(final File certChainFile, final File keyFile, final String keyPassword) throws SSLException {
        return newServerContext(null, certChainFile, keyFile, keyPassword, null, null, 0L, 0L);
    }
    
    public static SslContext newServerContext(final File certChainFile, final File keyFile, final String keyPassword, final Iterable<String> ciphers, final Iterable<String> nextProtocols, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        return newServerContext(null, certChainFile, keyFile, keyPassword, ciphers, nextProtocols, sessionCacheSize, sessionTimeout);
    }
    
    public static SslContext newServerContext(final SslProvider provider, final File certChainFile, final File keyFile) throws SSLException {
        return newServerContext(provider, certChainFile, keyFile, null, null, null, 0L, 0L);
    }
    
    public static SslContext newServerContext(final SslProvider provider, final File certChainFile, final File keyFile, final String keyPassword) throws SSLException {
        return newServerContext(provider, certChainFile, keyFile, keyPassword, null, null, 0L, 0L);
    }
    
    public static SslContext newServerContext(SslProvider provider, final File certChainFile, final File keyFile, final String keyPassword, final Iterable<String> ciphers, final Iterable<String> nextProtocols, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        if (provider == null) {
            provider = (OpenSsl.isAvailable() ? SslProvider.OPENSSL : SslProvider.JDK);
        }
        switch (provider) {
            case JDK: {
                return new JdkSslServerContext(certChainFile, keyFile, keyPassword, ciphers, nextProtocols, sessionCacheSize, sessionTimeout);
            }
            case OPENSSL: {
                return new OpenSslServerContext(certChainFile, keyFile, keyPassword, ciphers, nextProtocols, sessionCacheSize, sessionTimeout);
            }
            default: {
                throw new Error(provider.toString());
            }
        }
    }
    
    public static SslContext newClientContext() throws SSLException {
        return newClientContext(null, null, null, null, null, 0L, 0L);
    }
    
    public static SslContext newClientContext(final File certChainFile) throws SSLException {
        return newClientContext(null, certChainFile, null, null, null, 0L, 0L);
    }
    
    public static SslContext newClientContext(final TrustManagerFactory trustManagerFactory) throws SSLException {
        return newClientContext(null, null, trustManagerFactory, null, null, 0L, 0L);
    }
    
    public static SslContext newClientContext(final File certChainFile, final TrustManagerFactory trustManagerFactory) throws SSLException {
        return newClientContext(null, certChainFile, trustManagerFactory, null, null, 0L, 0L);
    }
    
    public static SslContext newClientContext(final File certChainFile, final TrustManagerFactory trustManagerFactory, final Iterable<String> ciphers, final Iterable<String> nextProtocols, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        return newClientContext(null, certChainFile, trustManagerFactory, ciphers, nextProtocols, sessionCacheSize, sessionTimeout);
    }
    
    public static SslContext newClientContext(final SslProvider provider) throws SSLException {
        return newClientContext(provider, null, null, null, null, 0L, 0L);
    }
    
    public static SslContext newClientContext(final SslProvider provider, final File certChainFile) throws SSLException {
        return newClientContext(provider, certChainFile, null, null, null, 0L, 0L);
    }
    
    public static SslContext newClientContext(final SslProvider provider, final TrustManagerFactory trustManagerFactory) throws SSLException {
        return newClientContext(provider, null, trustManagerFactory, null, null, 0L, 0L);
    }
    
    public static SslContext newClientContext(final SslProvider provider, final File certChainFile, final TrustManagerFactory trustManagerFactory) throws SSLException {
        return newClientContext(provider, certChainFile, trustManagerFactory, null, null, 0L, 0L);
    }
    
    public static SslContext newClientContext(final SslProvider provider, final File certChainFile, final TrustManagerFactory trustManagerFactory, final Iterable<String> ciphers, final Iterable<String> nextProtocols, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        if (provider != null && provider != SslProvider.JDK) {
            throw new SSLException("client context unsupported for: " + provider);
        }
        return new JdkSslClientContext(certChainFile, trustManagerFactory, ciphers, nextProtocols, sessionCacheSize, sessionTimeout);
    }
    
    SslContext() {
    }
    
    public final boolean isServer() {
        return !this.isClient();
    }
    
    public abstract boolean isClient();
    
    public abstract List<String> cipherSuites();
    
    public abstract long sessionCacheSize();
    
    public abstract long sessionTimeout();
    
    public abstract List<String> nextProtocols();
    
    public abstract SSLEngine newEngine(final ByteBufAllocator p0);
    
    public abstract SSLEngine newEngine(final ByteBufAllocator p0, final String p1, final int p2);
    
    public final SslHandler newHandler(final ByteBufAllocator alloc) {
        return newHandler(this.newEngine(alloc));
    }
    
    public final SslHandler newHandler(final ByteBufAllocator alloc, final String peerHost, final int peerPort) {
        return newHandler(this.newEngine(alloc, peerHost, peerPort));
    }
    
    private static SslHandler newHandler(final SSLEngine engine) {
        return new SslHandler(engine);
    }
}
