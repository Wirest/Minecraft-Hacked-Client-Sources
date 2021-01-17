// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.ssl;

import java.util.Collection;
import io.netty.util.internal.logging.InternalLoggerFactory;
import javax.net.ssl.SSLEngine;
import io.netty.buffer.ByteBufAllocator;
import java.util.Iterator;
import org.apache.tomcat.jni.SSL;
import org.apache.tomcat.jni.SSLContext;
import org.apache.tomcat.jni.Pool;
import java.util.Collections;
import java.util.ArrayList;
import javax.net.ssl.SSLException;
import java.io.File;
import java.util.List;
import io.netty.util.internal.logging.InternalLogger;

public final class OpenSslServerContext extends SslContext
{
    private static final InternalLogger logger;
    private static final List<String> DEFAULT_CIPHERS;
    private final long aprPool;
    private final List<String> ciphers;
    private final List<String> unmodifiableCiphers;
    private final long sessionCacheSize;
    private final long sessionTimeout;
    private final List<String> nextProtocols;
    private final long ctx;
    private final OpenSslSessionStats stats;
    
    public OpenSslServerContext(final File certChainFile, final File keyFile) throws SSLException {
        this(certChainFile, keyFile, null);
    }
    
    public OpenSslServerContext(final File certChainFile, final File keyFile, final String keyPassword) throws SSLException {
        this(certChainFile, keyFile, keyPassword, null, null, 0L, 0L);
    }
    
    public OpenSslServerContext(final File certChainFile, final File keyFile, String keyPassword, Iterable<String> ciphers, Iterable<String> nextProtocols, long sessionCacheSize, long sessionTimeout) throws SSLException {
        this.ciphers = new ArrayList<String>();
        this.unmodifiableCiphers = Collections.unmodifiableList((List<? extends String>)this.ciphers);
        OpenSsl.ensureAvailability();
        if (certChainFile == null) {
            throw new NullPointerException("certChainFile");
        }
        if (!certChainFile.isFile()) {
            throw new IllegalArgumentException("certChainFile is not a file: " + certChainFile);
        }
        if (keyFile == null) {
            throw new NullPointerException("keyPath");
        }
        if (!keyFile.isFile()) {
            throw new IllegalArgumentException("keyPath is not a file: " + keyFile);
        }
        if (ciphers == null) {
            ciphers = OpenSslServerContext.DEFAULT_CIPHERS;
        }
        if (keyPassword == null) {
            keyPassword = "";
        }
        if (nextProtocols == null) {
            nextProtocols = (Iterable<String>)Collections.emptyList();
        }
        for (final String c : ciphers) {
            if (c == null) {
                break;
            }
            this.ciphers.add(c);
        }
        final List<String> nextProtoList = new ArrayList<String>();
        for (final String p : nextProtocols) {
            if (p == null) {
                break;
            }
            nextProtoList.add(p);
        }
        this.nextProtocols = Collections.unmodifiableList((List<? extends String>)nextProtoList);
        this.aprPool = Pool.create(0L);
        boolean success = false;
        try {
            synchronized (OpenSslServerContext.class) {
                try {
                    this.ctx = SSLContext.make(this.aprPool, 6, 1);
                }
                catch (Exception e) {
                    throw new SSLException("failed to create an SSL_CTX", e);
                }
                SSLContext.setOptions(this.ctx, 4095);
                SSLContext.setOptions(this.ctx, 16777216);
                SSLContext.setOptions(this.ctx, 4194304);
                SSLContext.setOptions(this.ctx, 524288);
                SSLContext.setOptions(this.ctx, 1048576);
                SSLContext.setOptions(this.ctx, 65536);
                try {
                    final StringBuilder cipherBuf = new StringBuilder();
                    for (final String c2 : this.ciphers) {
                        cipherBuf.append(c2);
                        cipherBuf.append(':');
                    }
                    cipherBuf.setLength(cipherBuf.length() - 1);
                    SSLContext.setCipherSuite(this.ctx, cipherBuf.toString());
                }
                catch (SSLException e2) {
                    throw e2;
                }
                catch (Exception e) {
                    throw new SSLException("failed to set cipher suite: " + this.ciphers, e);
                }
                SSLContext.setVerify(this.ctx, 0, 10);
                try {
                    if (!SSLContext.setCertificate(this.ctx, certChainFile.getPath(), keyFile.getPath(), keyPassword, 0)) {
                        throw new SSLException("failed to set certificate: " + certChainFile + " and " + keyFile + " (" + SSL.getLastError() + ')');
                    }
                }
                catch (SSLException e2) {
                    throw e2;
                }
                catch (Exception e) {
                    throw new SSLException("failed to set certificate: " + certChainFile + " and " + keyFile, e);
                }
                if (!SSLContext.setCertificateChainFile(this.ctx, certChainFile.getPath(), true)) {
                    final String error = SSL.getLastError();
                    if (!error.startsWith("error:00000000:")) {
                        throw new SSLException("failed to set certificate chain: " + certChainFile + " (" + SSL.getLastError() + ')');
                    }
                }
                if (!nextProtoList.isEmpty()) {
                    final StringBuilder nextProtocolBuf = new StringBuilder();
                    for (final String p2 : nextProtoList) {
                        nextProtocolBuf.append(p2);
                        nextProtocolBuf.append(',');
                    }
                    nextProtocolBuf.setLength(nextProtocolBuf.length() - 1);
                    SSLContext.setNextProtos(this.ctx, nextProtocolBuf.toString());
                }
                if (sessionCacheSize > 0L) {
                    this.sessionCacheSize = sessionCacheSize;
                    SSLContext.setSessionCacheSize(this.ctx, sessionCacheSize);
                }
                else {
                    sessionCacheSize = (this.sessionCacheSize = SSLContext.setSessionCacheSize(this.ctx, 20480L));
                    SSLContext.setSessionCacheSize(this.ctx, sessionCacheSize);
                }
                if (sessionTimeout > 0L) {
                    this.sessionTimeout = sessionTimeout;
                    SSLContext.setSessionCacheTimeout(this.ctx, sessionTimeout);
                }
                else {
                    sessionTimeout = (this.sessionTimeout = SSLContext.setSessionCacheTimeout(this.ctx, 300L));
                    SSLContext.setSessionCacheTimeout(this.ctx, sessionTimeout);
                }
            }
            success = true;
        }
        finally {
            if (!success) {
                this.destroyPools();
            }
        }
        this.stats = new OpenSslSessionStats(this.ctx);
    }
    
    @Override
    public boolean isClient() {
        return false;
    }
    
    @Override
    public List<String> cipherSuites() {
        return this.unmodifiableCiphers;
    }
    
    @Override
    public long sessionCacheSize() {
        return this.sessionCacheSize;
    }
    
    @Override
    public long sessionTimeout() {
        return this.sessionTimeout;
    }
    
    @Override
    public List<String> nextProtocols() {
        return this.nextProtocols;
    }
    
    public long context() {
        return this.ctx;
    }
    
    public OpenSslSessionStats stats() {
        return this.stats;
    }
    
    @Override
    public SSLEngine newEngine(final ByteBufAllocator alloc) {
        if (this.nextProtocols.isEmpty()) {
            return new OpenSslEngine(this.ctx, alloc, null);
        }
        return new OpenSslEngine(this.ctx, alloc, this.nextProtocols.get(this.nextProtocols.size() - 1));
    }
    
    @Override
    public SSLEngine newEngine(final ByteBufAllocator alloc, final String peerHost, final int peerPort) {
        throw new UnsupportedOperationException();
    }
    
    public void setTicketKeys(final byte[] keys) {
        if (keys == null) {
            throw new NullPointerException("keys");
        }
        SSLContext.setSessionTicketKeys(this.ctx, keys);
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        synchronized (OpenSslServerContext.class) {
            if (this.ctx != 0L) {
                SSLContext.free(this.ctx);
            }
        }
        this.destroyPools();
    }
    
    private void destroyPools() {
        if (this.aprPool != 0L) {
            Pool.destroy(this.aprPool);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(OpenSslServerContext.class);
        final List<String> ciphers = new ArrayList<String>();
        Collections.addAll(ciphers, new String[] { "ECDHE-RSA-AES128-GCM-SHA256", "ECDHE-RSA-RC4-SHA", "ECDHE-RSA-AES128-SHA", "ECDHE-RSA-AES256-SHA", "AES128-GCM-SHA256", "RC4-SHA", "RC4-MD5", "AES128-SHA", "AES256-SHA", "DES-CBC3-SHA" });
        DEFAULT_CIPHERS = Collections.unmodifiableList((List<? extends String>)ciphers);
        if (OpenSslServerContext.logger.isDebugEnabled()) {
            OpenSslServerContext.logger.debug("Default cipher suite (OpenSSL): " + ciphers);
        }
    }
}
