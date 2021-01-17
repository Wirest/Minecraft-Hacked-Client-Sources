// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.ssl;

import java.security.SecureRandom;
import javax.net.ssl.TrustManager;
import javax.net.ssl.KeyManager;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Iterator;
import java.util.ArrayList;
import javax.net.ssl.SSLEngine;
import io.netty.buffer.ByteBufAllocator;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLContext;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import io.netty.util.internal.logging.InternalLogger;

public abstract class JdkSslContext extends SslContext
{
    private static final InternalLogger logger;
    static final String PROTOCOL = "TLS";
    static final String[] PROTOCOLS;
    static final List<String> DEFAULT_CIPHERS;
    private final String[] cipherSuites;
    private final List<String> unmodifiableCipherSuites;
    
    private static void addIfSupported(final String[] supported, final List<String> enabled, final String... names) {
        for (final String n : names) {
            for (final String s : supported) {
                if (n.equals(s)) {
                    enabled.add(s);
                    break;
                }
            }
        }
    }
    
    JdkSslContext(final Iterable<String> ciphers) {
        this.cipherSuites = toCipherSuiteArray(ciphers);
        this.unmodifiableCipherSuites = Collections.unmodifiableList((List<? extends String>)Arrays.asList((T[])this.cipherSuites));
    }
    
    public abstract SSLContext context();
    
    public final SSLSessionContext sessionContext() {
        if (this.isServer()) {
            return this.context().getServerSessionContext();
        }
        return this.context().getClientSessionContext();
    }
    
    @Override
    public final List<String> cipherSuites() {
        return this.unmodifiableCipherSuites;
    }
    
    @Override
    public final long sessionCacheSize() {
        return this.sessionContext().getSessionCacheSize();
    }
    
    @Override
    public final long sessionTimeout() {
        return this.sessionContext().getSessionTimeout();
    }
    
    @Override
    public final SSLEngine newEngine(final ByteBufAllocator alloc) {
        final SSLEngine engine = this.context().createSSLEngine();
        engine.setEnabledCipherSuites(this.cipherSuites);
        engine.setEnabledProtocols(JdkSslContext.PROTOCOLS);
        engine.setUseClientMode(this.isClient());
        return this.wrapEngine(engine);
    }
    
    @Override
    public final SSLEngine newEngine(final ByteBufAllocator alloc, final String peerHost, final int peerPort) {
        final SSLEngine engine = this.context().createSSLEngine(peerHost, peerPort);
        engine.setEnabledCipherSuites(this.cipherSuites);
        engine.setEnabledProtocols(JdkSslContext.PROTOCOLS);
        engine.setUseClientMode(this.isClient());
        return this.wrapEngine(engine);
    }
    
    private SSLEngine wrapEngine(final SSLEngine engine) {
        if (this.nextProtocols().isEmpty()) {
            return engine;
        }
        return new JettyNpnSslEngine(engine, this.nextProtocols(), this.isServer());
    }
    
    private static String[] toCipherSuiteArray(final Iterable<String> ciphers) {
        if (ciphers == null) {
            return JdkSslContext.DEFAULT_CIPHERS.toArray(new String[JdkSslContext.DEFAULT_CIPHERS.size()]);
        }
        final List<String> newCiphers = new ArrayList<String>();
        for (final String c : ciphers) {
            if (c == null) {
                break;
            }
            newCiphers.add(c);
        }
        return newCiphers.toArray(new String[newCiphers.size()]);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(JdkSslContext.class);
        SSLContext context;
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, null, null);
        }
        catch (Exception e) {
            throw new Error("failed to initialize the default SSL context", e);
        }
        final SSLEngine engine = context.createSSLEngine();
        final String[] supportedProtocols = engine.getSupportedProtocols();
        final List<String> protocols = new ArrayList<String>();
        addIfSupported(supportedProtocols, protocols, "TLSv1.2", "TLSv1.1", "TLSv1", "SSLv3");
        if (!protocols.isEmpty()) {
            PROTOCOLS = protocols.toArray(new String[protocols.size()]);
        }
        else {
            PROTOCOLS = engine.getEnabledProtocols();
        }
        final String[] supportedCiphers = engine.getSupportedCipherSuites();
        final List<String> ciphers = new ArrayList<String>();
        addIfSupported(supportedCiphers, ciphers, "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_RSA_WITH_RC4_128_SHA", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", "TLS_RSA_WITH_AES_128_GCM_SHA256", "SSL_RSA_WITH_RC4_128_SHA", "SSL_RSA_WITH_RC4_128_MD5", "TLS_RSA_WITH_AES_128_CBC_SHA", "TLS_RSA_WITH_AES_256_CBC_SHA", "SSL_RSA_WITH_DES_CBC_SHA");
        if (!ciphers.isEmpty()) {
            DEFAULT_CIPHERS = Collections.unmodifiableList((List<? extends String>)ciphers);
        }
        else {
            DEFAULT_CIPHERS = Collections.unmodifiableList((List<? extends String>)Arrays.asList((T[])engine.getEnabledCipherSuites()));
        }
        if (JdkSslContext.logger.isDebugEnabled()) {
            JdkSslContext.logger.debug("Default protocols (JDK): {} ", Arrays.asList(JdkSslContext.PROTOCOLS));
            JdkSslContext.logger.debug("Default cipher suites (JDK): {}", JdkSslContext.DEFAULT_CIPHERS);
        }
    }
}
