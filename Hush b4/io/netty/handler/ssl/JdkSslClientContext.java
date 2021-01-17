// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.ssl;

import javax.net.ssl.SSLSessionContext;
import javax.security.auth.x500.X500Principal;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.security.cert.Certificate;
import io.netty.buffer.ByteBufInputStream;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.TrustManager;
import javax.net.ssl.KeyManager;
import java.util.Collections;
import java.util.ArrayList;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.util.List;
import javax.net.ssl.SSLContext;

public final class JdkSslClientContext extends JdkSslContext
{
    private final SSLContext ctx;
    private final List<String> nextProtocols;
    
    public JdkSslClientContext() throws SSLException {
        this(null, null, null, null, 0L, 0L);
    }
    
    public JdkSslClientContext(final File certChainFile) throws SSLException {
        this(certChainFile, null);
    }
    
    public JdkSslClientContext(final TrustManagerFactory trustManagerFactory) throws SSLException {
        this(null, trustManagerFactory);
    }
    
    public JdkSslClientContext(final File certChainFile, final TrustManagerFactory trustManagerFactory) throws SSLException {
        this(certChainFile, trustManagerFactory, null, null, 0L, 0L);
    }
    
    public JdkSslClientContext(final File certChainFile, TrustManagerFactory trustManagerFactory, final Iterable<String> ciphers, final Iterable<String> nextProtocols, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        super(ciphers);
        if (nextProtocols != null && nextProtocols.iterator().hasNext()) {
            if (!JettyNpnSslEngine.isAvailable()) {
                throw new SSLException("NPN/ALPN unsupported: " + nextProtocols);
            }
            final List<String> nextProtoList = new ArrayList<String>();
            for (final String p : nextProtocols) {
                if (p == null) {
                    break;
                }
                nextProtoList.add(p);
            }
            this.nextProtocols = Collections.unmodifiableList((List<? extends String>)nextProtoList);
        }
        else {
            this.nextProtocols = Collections.emptyList();
        }
        try {
            if (certChainFile == null) {
                this.ctx = SSLContext.getInstance("TLS");
                if (trustManagerFactory == null) {
                    this.ctx.init(null, null, null);
                }
                else {
                    trustManagerFactory.init((KeyStore)null);
                    this.ctx.init(null, trustManagerFactory.getTrustManagers(), null);
                }
            }
            else {
                final KeyStore ks = KeyStore.getInstance("JKS");
                ks.load(null, null);
                final CertificateFactory cf = CertificateFactory.getInstance("X.509");
                final ByteBuf[] certs = PemReader.readCertificates(certChainFile);
                try {
                    for (final ByteBuf buf : certs) {
                        final X509Certificate cert = (X509Certificate)cf.generateCertificate(new ByteBufInputStream(buf));
                        final X500Principal principal = cert.getSubjectX500Principal();
                        ks.setCertificateEntry(principal.getName("RFC2253"), cert);
                    }
                }
                finally {
                    for (final ByteBuf buf2 : certs) {
                        buf2.release();
                    }
                }
                if (trustManagerFactory == null) {
                    trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                }
                trustManagerFactory.init(ks);
                (this.ctx = SSLContext.getInstance("TLS")).init(null, trustManagerFactory.getTrustManagers(), null);
            }
            final SSLSessionContext sessCtx = this.ctx.getClientSessionContext();
            if (sessionCacheSize > 0L) {
                sessCtx.setSessionCacheSize((int)Math.min(sessionCacheSize, 2147483647L));
            }
            if (sessionTimeout > 0L) {
                sessCtx.setSessionTimeout((int)Math.min(sessionTimeout, 2147483647L));
            }
        }
        catch (Exception e) {
            throw new SSLException("failed to initialize the server-side SSL context", e);
        }
    }
    
    @Override
    public boolean isClient() {
        return true;
    }
    
    @Override
    public List<String> nextProtocols() {
        return this.nextProtocols;
    }
    
    @Override
    public SSLContext context() {
        return this.ctx;
    }
}
