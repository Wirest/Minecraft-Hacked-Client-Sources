// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.ssl;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.Principal;
import javax.net.ssl.SSLSessionContext;
import io.netty.util.internal.EmptyArrays;
import java.nio.ReadOnlyBufferException;
import javax.net.ssl.SSLEngineResult;
import io.netty.buffer.ByteBuf;
import org.apache.tomcat.jni.Buffer;
import java.nio.ByteBuffer;
import org.apache.tomcat.jni.SSL;
import javax.net.ssl.SSLSession;
import io.netty.buffer.ByteBufAllocator;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import javax.net.ssl.SSLException;
import javax.security.cert.X509Certificate;
import java.security.cert.Certificate;
import io.netty.util.internal.logging.InternalLogger;
import javax.net.ssl.SSLEngine;

public final class OpenSslEngine extends SSLEngine
{
    private static final InternalLogger logger;
    private static final Certificate[] EMPTY_CERTIFICATES;
    private static final X509Certificate[] EMPTY_X509_CERTIFICATES;
    private static final SSLException ENGINE_CLOSED;
    private static final SSLException RENEGOTIATION_UNSUPPORTED;
    private static final SSLException ENCRYPTED_PACKET_OVERSIZED;
    private static final int MAX_PLAINTEXT_LENGTH = 16384;
    private static final int MAX_COMPRESSED_LENGTH = 17408;
    private static final int MAX_CIPHERTEXT_LENGTH = 18432;
    static final int MAX_ENCRYPTED_PACKET_LENGTH = 18713;
    static final int MAX_ENCRYPTION_OVERHEAD_LENGTH = 2329;
    private static final AtomicIntegerFieldUpdater<OpenSslEngine> DESTROYED_UPDATER;
    private long ssl;
    private long networkBIO;
    private int accepted;
    private boolean handshakeFinished;
    private boolean receivedShutdown;
    private volatile int destroyed;
    private String cipher;
    private volatile String applicationProtocol;
    private boolean isInboundDone;
    private boolean isOutboundDone;
    private boolean engineClosed;
    private int lastPrimingReadResult;
    private final ByteBufAllocator alloc;
    private final String fallbackApplicationProtocol;
    private SSLSession session;
    
    public OpenSslEngine(final long sslCtx, final ByteBufAllocator alloc, final String fallbackApplicationProtocol) {
        OpenSsl.ensureAvailability();
        if (sslCtx == 0L) {
            throw new NullPointerException("sslContext");
        }
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        this.alloc = alloc;
        this.ssl = SSL.newSSL(sslCtx, true);
        this.networkBIO = SSL.makeNetworkBIO(this.ssl);
        this.fallbackApplicationProtocol = fallbackApplicationProtocol;
    }
    
    public synchronized void shutdown() {
        if (OpenSslEngine.DESTROYED_UPDATER.compareAndSet(this, 0, 1)) {
            SSL.freeSSL(this.ssl);
            SSL.freeBIO(this.networkBIO);
            final long n = 0L;
            this.networkBIO = n;
            this.ssl = n;
            final boolean isInboundDone = true;
            this.engineClosed = isInboundDone;
            this.isOutboundDone = isInboundDone;
            this.isInboundDone = isInboundDone;
        }
    }
    
    private int writePlaintextData(final ByteBuffer src) {
        final int pos = src.position();
        final int limit = src.limit();
        final int len = Math.min(limit - pos, 16384);
        int sslWrote;
        if (src.isDirect()) {
            final long addr = Buffer.address(src) + pos;
            sslWrote = SSL.writeToSSL(this.ssl, addr, len);
            if (sslWrote > 0) {
                src.position(pos + sslWrote);
                return sslWrote;
            }
        }
        else {
            final ByteBuf buf = this.alloc.directBuffer(len);
            try {
                long addr2;
                if (buf.hasMemoryAddress()) {
                    addr2 = buf.memoryAddress();
                }
                else {
                    addr2 = Buffer.address(buf.nioBuffer());
                }
                src.limit(pos + len);
                buf.setBytes(0, src);
                src.limit(limit);
                sslWrote = SSL.writeToSSL(this.ssl, addr2, len);
                if (sslWrote > 0) {
                    src.position(pos + sslWrote);
                    return sslWrote;
                }
                src.position(pos);
            }
            finally {
                buf.release();
            }
        }
        throw new IllegalStateException("SSL.writeToSSL() returned a non-positive value: " + sslWrote);
    }
    
    private int writeEncryptedData(final ByteBuffer src) {
        final int pos = src.position();
        final int len = src.remaining();
        if (src.isDirect()) {
            final long addr = Buffer.address(src) + pos;
            final int netWrote = SSL.writeToBIO(this.networkBIO, addr, len);
            if (netWrote >= 0) {
                src.position(pos + netWrote);
                this.lastPrimingReadResult = SSL.readFromSSL(this.ssl, addr, 0);
                return netWrote;
            }
        }
        else {
            final ByteBuf buf = this.alloc.directBuffer(len);
            try {
                long addr2;
                if (buf.hasMemoryAddress()) {
                    addr2 = buf.memoryAddress();
                }
                else {
                    addr2 = Buffer.address(buf.nioBuffer());
                }
                buf.setBytes(0, src);
                final int netWrote2 = SSL.writeToBIO(this.networkBIO, addr2, len);
                if (netWrote2 >= 0) {
                    src.position(pos + netWrote2);
                    this.lastPrimingReadResult = SSL.readFromSSL(this.ssl, addr2, 0);
                    return netWrote2;
                }
                src.position(pos);
            }
            finally {
                buf.release();
            }
        }
        return 0;
    }
    
    private int readPlaintextData(final ByteBuffer dst) {
        if (dst.isDirect()) {
            final int pos = dst.position();
            final long addr = Buffer.address(dst) + pos;
            final int len = dst.limit() - pos;
            final int sslRead = SSL.readFromSSL(this.ssl, addr, len);
            if (sslRead > 0) {
                dst.position(pos + sslRead);
                return sslRead;
            }
        }
        else {
            final int pos = dst.position();
            final int limit = dst.limit();
            final int len2 = Math.min(18713, limit - pos);
            final ByteBuf buf = this.alloc.directBuffer(len2);
            try {
                long addr2;
                if (buf.hasMemoryAddress()) {
                    addr2 = buf.memoryAddress();
                }
                else {
                    addr2 = Buffer.address(buf.nioBuffer());
                }
                final int sslRead2 = SSL.readFromSSL(this.ssl, addr2, len2);
                if (sslRead2 > 0) {
                    dst.limit(pos + sslRead2);
                    buf.getBytes(0, dst);
                    dst.limit(limit);
                    return sslRead2;
                }
            }
            finally {
                buf.release();
            }
        }
        return 0;
    }
    
    private int readEncryptedData(final ByteBuffer dst, final int pending) {
        if (dst.isDirect() && dst.remaining() >= pending) {
            final int pos = dst.position();
            final long addr = Buffer.address(dst) + pos;
            final int bioRead = SSL.readFromBIO(this.networkBIO, addr, pending);
            if (bioRead > 0) {
                dst.position(pos + bioRead);
                return bioRead;
            }
        }
        else {
            final ByteBuf buf = this.alloc.directBuffer(pending);
            try {
                long addr;
                if (buf.hasMemoryAddress()) {
                    addr = buf.memoryAddress();
                }
                else {
                    addr = Buffer.address(buf.nioBuffer());
                }
                final int bioRead = SSL.readFromBIO(this.networkBIO, addr, pending);
                if (bioRead > 0) {
                    final int oldLimit = dst.limit();
                    dst.limit(dst.position() + bioRead);
                    buf.getBytes(0, dst);
                    dst.limit(oldLimit);
                    return bioRead;
                }
            }
            finally {
                buf.release();
            }
        }
        return 0;
    }
    
    @Override
    public synchronized SSLEngineResult wrap(final ByteBuffer[] srcs, final int offset, final int length, final ByteBuffer dst) throws SSLException {
        if (this.destroyed != 0) {
            return new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
        }
        if (srcs == null) {
            throw new NullPointerException("srcs");
        }
        if (dst == null) {
            throw new NullPointerException("dst");
        }
        if (offset >= srcs.length || offset + length > srcs.length) {
            throw new IndexOutOfBoundsException("offset: " + offset + ", length: " + length + " (expected: offset <= offset + length <= srcs.length (" + srcs.length + "))");
        }
        if (dst.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        if (this.accepted == 0) {
            this.beginHandshakeImplicitly();
        }
        final SSLEngineResult.HandshakeStatus handshakeStatus = this.getHandshakeStatus();
        if ((!this.handshakeFinished || this.engineClosed) && handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
            return new SSLEngineResult(this.getEngineStatus(), SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
        }
        int bytesProduced = 0;
        int pendingNet = SSL.pendingWrittenBytesInBIO(this.networkBIO);
        if (pendingNet <= 0) {
            int bytesConsumed = 0;
            for (int i = offset; i < length; ++i) {
                final ByteBuffer src = srcs[i];
                while (src.hasRemaining()) {
                    try {
                        bytesConsumed += this.writePlaintextData(src);
                    }
                    catch (Exception e) {
                        throw new SSLException(e);
                    }
                    pendingNet = SSL.pendingWrittenBytesInBIO(this.networkBIO);
                    if (pendingNet > 0) {
                        final int capacity = dst.remaining();
                        if (capacity < pendingNet) {
                            return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, this.getHandshakeStatus(), bytesConsumed, bytesProduced);
                        }
                        try {
                            bytesProduced += this.readEncryptedData(dst, pendingNet);
                        }
                        catch (Exception e2) {
                            throw new SSLException(e2);
                        }
                        return new SSLEngineResult(this.getEngineStatus(), this.getHandshakeStatus(), bytesConsumed, bytesProduced);
                    }
                }
            }
            return new SSLEngineResult(this.getEngineStatus(), this.getHandshakeStatus(), bytesConsumed, bytesProduced);
        }
        final int capacity2 = dst.remaining();
        if (capacity2 < pendingNet) {
            return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, handshakeStatus, 0, bytesProduced);
        }
        try {
            bytesProduced += this.readEncryptedData(dst, pendingNet);
        }
        catch (Exception e3) {
            throw new SSLException(e3);
        }
        if (this.isOutboundDone) {
            this.shutdown();
        }
        return new SSLEngineResult(this.getEngineStatus(), this.getHandshakeStatus(), 0, bytesProduced);
    }
    
    @Override
    public synchronized SSLEngineResult unwrap(final ByteBuffer src, final ByteBuffer[] dsts, final int offset, final int length) throws SSLException {
        if (this.destroyed != 0) {
            return new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
        }
        if (src == null) {
            throw new NullPointerException("src");
        }
        if (dsts == null) {
            throw new NullPointerException("dsts");
        }
        if (offset >= dsts.length || offset + length > dsts.length) {
            throw new IndexOutOfBoundsException("offset: " + offset + ", length: " + length + " (expected: offset <= offset + length <= dsts.length (" + dsts.length + "))");
        }
        int capacity = 0;
        final int endOffset = offset + length;
        for (int i = offset; i < endOffset; ++i) {
            final ByteBuffer dst = dsts[i];
            if (dst == null) {
                throw new IllegalArgumentException();
            }
            if (dst.isReadOnly()) {
                throw new ReadOnlyBufferException();
            }
            capacity += dst.remaining();
        }
        if (this.accepted == 0) {
            this.beginHandshakeImplicitly();
        }
        final SSLEngineResult.HandshakeStatus handshakeStatus = this.getHandshakeStatus();
        if ((!this.handshakeFinished || this.engineClosed) && handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
            return new SSLEngineResult(this.getEngineStatus(), SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
        }
        if (src.remaining() > 18713) {
            this.isInboundDone = true;
            this.isOutboundDone = true;
            this.engineClosed = true;
            this.shutdown();
            throw OpenSslEngine.ENCRYPTED_PACKET_OVERSIZED;
        }
        int bytesConsumed = 0;
        this.lastPrimingReadResult = 0;
        try {
            bytesConsumed += this.writeEncryptedData(src);
        }
        catch (Exception e) {
            throw new SSLException(e);
        }
        final String error = SSL.getLastError();
        if (error != null && !error.startsWith("error:00000000:")) {
            if (OpenSslEngine.logger.isInfoEnabled()) {
                OpenSslEngine.logger.info("SSL_read failed: primingReadResult: " + this.lastPrimingReadResult + "; OpenSSL error: '" + error + '\'');
            }
            this.shutdown();
            throw new SSLException(error);
        }
        int pendingApp = (SSL.isInInit(this.ssl) == 0) ? SSL.pendingReadableBytesInSSL(this.ssl) : 0;
        if (capacity < pendingApp) {
            return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, this.getHandshakeStatus(), bytesConsumed, 0);
        }
        int bytesProduced = 0;
        int idx = offset;
        while (idx < endOffset) {
            final ByteBuffer dst2 = dsts[idx];
            if (!dst2.hasRemaining()) {
                ++idx;
            }
            else {
                if (pendingApp <= 0) {
                    break;
                }
                int bytesRead;
                try {
                    bytesRead = this.readPlaintextData(dst2);
                }
                catch (Exception e2) {
                    throw new SSLException(e2);
                }
                if (bytesRead == 0) {
                    break;
                }
                bytesProduced += bytesRead;
                pendingApp -= bytesRead;
                if (dst2.hasRemaining()) {
                    continue;
                }
                ++idx;
            }
        }
        if (!this.receivedShutdown && (SSL.getShutdown(this.ssl) & 0x2) == 0x2) {
            this.receivedShutdown = true;
            this.closeOutbound();
            this.closeInbound();
        }
        return new SSLEngineResult(this.getEngineStatus(), this.getHandshakeStatus(), bytesConsumed, bytesProduced);
    }
    
    @Override
    public Runnable getDelegatedTask() {
        return null;
    }
    
    @Override
    public synchronized void closeInbound() throws SSLException {
        if (this.isInboundDone) {
            return;
        }
        this.isInboundDone = true;
        this.engineClosed = true;
        if (this.accepted != 0) {
            if (!this.receivedShutdown) {
                this.shutdown();
                throw new SSLException("Inbound closed before receiving peer's close_notify: possible truncation attack?");
            }
        }
        else {
            this.shutdown();
        }
    }
    
    @Override
    public synchronized boolean isInboundDone() {
        return this.isInboundDone || this.engineClosed;
    }
    
    @Override
    public synchronized void closeOutbound() {
        if (this.isOutboundDone) {
            return;
        }
        this.isOutboundDone = true;
        this.engineClosed = true;
        if (this.accepted != 0 && this.destroyed == 0) {
            final int mode = SSL.getShutdown(this.ssl);
            if ((mode & 0x1) != 0x1) {
                SSL.shutdownSSL(this.ssl);
            }
        }
        else {
            this.shutdown();
        }
    }
    
    @Override
    public synchronized boolean isOutboundDone() {
        return this.isOutboundDone;
    }
    
    @Override
    public String[] getSupportedCipherSuites() {
        return EmptyArrays.EMPTY_STRINGS;
    }
    
    @Override
    public String[] getEnabledCipherSuites() {
        return EmptyArrays.EMPTY_STRINGS;
    }
    
    @Override
    public void setEnabledCipherSuites(final String[] strings) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String[] getSupportedProtocols() {
        return EmptyArrays.EMPTY_STRINGS;
    }
    
    @Override
    public String[] getEnabledProtocols() {
        return EmptyArrays.EMPTY_STRINGS;
    }
    
    @Override
    public void setEnabledProtocols(final String[] strings) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public SSLSession getSession() {
        SSLSession session = this.session;
        if (session == null) {
            session = (this.session = new SSLSession() {
                @Override
                public byte[] getId() {
                    return String.valueOf(OpenSslEngine.this.ssl).getBytes();
                }
                
                @Override
                public SSLSessionContext getSessionContext() {
                    return null;
                }
                
                @Override
                public long getCreationTime() {
                    return 0L;
                }
                
                @Override
                public long getLastAccessedTime() {
                    return 0L;
                }
                
                @Override
                public void invalidate() {
                }
                
                @Override
                public boolean isValid() {
                    return false;
                }
                
                @Override
                public void putValue(final String s, final Object o) {
                }
                
                @Override
                public Object getValue(final String s) {
                    return null;
                }
                
                @Override
                public void removeValue(final String s) {
                }
                
                @Override
                public String[] getValueNames() {
                    return EmptyArrays.EMPTY_STRINGS;
                }
                
                @Override
                public Certificate[] getPeerCertificates() {
                    return OpenSslEngine.EMPTY_CERTIFICATES;
                }
                
                @Override
                public Certificate[] getLocalCertificates() {
                    return OpenSslEngine.EMPTY_CERTIFICATES;
                }
                
                @Override
                public X509Certificate[] getPeerCertificateChain() {
                    return OpenSslEngine.EMPTY_X509_CERTIFICATES;
                }
                
                @Override
                public Principal getPeerPrincipal() {
                    return null;
                }
                
                @Override
                public Principal getLocalPrincipal() {
                    return null;
                }
                
                @Override
                public String getCipherSuite() {
                    return OpenSslEngine.this.cipher;
                }
                
                @Override
                public String getProtocol() {
                    final String applicationProtocol = OpenSslEngine.this.applicationProtocol;
                    if (applicationProtocol == null) {
                        return "unknown";
                    }
                    return "unknown:" + applicationProtocol;
                }
                
                @Override
                public String getPeerHost() {
                    return null;
                }
                
                @Override
                public int getPeerPort() {
                    return 0;
                }
                
                @Override
                public int getPacketBufferSize() {
                    return 18713;
                }
                
                @Override
                public int getApplicationBufferSize() {
                    return 16384;
                }
            });
        }
        return session;
    }
    
    @Override
    public synchronized void beginHandshake() throws SSLException {
        if (this.engineClosed) {
            throw OpenSslEngine.ENGINE_CLOSED;
        }
        switch (this.accepted) {
            case 0: {
                SSL.doHandshake(this.ssl);
                this.accepted = 2;
                break;
            }
            case 1: {
                this.accepted = 2;
                break;
            }
            case 2: {
                throw OpenSslEngine.RENEGOTIATION_UNSUPPORTED;
            }
            default: {
                throw new Error();
            }
        }
    }
    
    private synchronized void beginHandshakeImplicitly() throws SSLException {
        if (this.engineClosed) {
            throw OpenSslEngine.ENGINE_CLOSED;
        }
        if (this.accepted == 0) {
            SSL.doHandshake(this.ssl);
            this.accepted = 1;
        }
    }
    
    private SSLEngineResult.Status getEngineStatus() {
        return this.engineClosed ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK;
    }
    
    @Override
    public synchronized SSLEngineResult.HandshakeStatus getHandshakeStatus() {
        if (this.accepted == 0 || this.destroyed != 0) {
            return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
        }
        if (!this.handshakeFinished) {
            if (SSL.pendingWrittenBytesInBIO(this.networkBIO) != 0) {
                return SSLEngineResult.HandshakeStatus.NEED_WRAP;
            }
            if (SSL.isInInit(this.ssl) == 0) {
                this.handshakeFinished = true;
                this.cipher = SSL.getCipherForSSL(this.ssl);
                String applicationProtocol = SSL.getNextProtoNegotiated(this.ssl);
                if (applicationProtocol == null) {
                    applicationProtocol = this.fallbackApplicationProtocol;
                }
                if (applicationProtocol != null) {
                    this.applicationProtocol = applicationProtocol.replace(':', '_');
                }
                else {
                    this.applicationProtocol = null;
                }
                return SSLEngineResult.HandshakeStatus.FINISHED;
            }
            return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
        }
        else {
            if (!this.engineClosed) {
                return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
            }
            if (SSL.pendingWrittenBytesInBIO(this.networkBIO) != 0) {
                return SSLEngineResult.HandshakeStatus.NEED_WRAP;
            }
            return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
        }
    }
    
    @Override
    public void setUseClientMode(final boolean clientMode) {
        if (clientMode) {
            throw new UnsupportedOperationException();
        }
    }
    
    @Override
    public boolean getUseClientMode() {
        return false;
    }
    
    @Override
    public void setNeedClientAuth(final boolean b) {
        if (b) {
            throw new UnsupportedOperationException();
        }
    }
    
    @Override
    public boolean getNeedClientAuth() {
        return false;
    }
    
    @Override
    public void setWantClientAuth(final boolean b) {
        if (b) {
            throw new UnsupportedOperationException();
        }
    }
    
    @Override
    public boolean getWantClientAuth() {
        return false;
    }
    
    @Override
    public void setEnableSessionCreation(final boolean b) {
        if (b) {
            throw new UnsupportedOperationException();
        }
    }
    
    @Override
    public boolean getEnableSessionCreation() {
        return false;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(OpenSslEngine.class);
        EMPTY_CERTIFICATES = new Certificate[0];
        EMPTY_X509_CERTIFICATES = new X509Certificate[0];
        ENGINE_CLOSED = new SSLException("engine closed");
        RENEGOTIATION_UNSUPPORTED = new SSLException("renegotiation unsupported");
        ENCRYPTED_PACKET_OVERSIZED = new SSLException("encrypted packet oversized");
        OpenSslEngine.ENGINE_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        OpenSslEngine.RENEGOTIATION_UNSUPPORTED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        OpenSslEngine.ENCRYPTED_PACKET_OVERSIZED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        DESTROYED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(OpenSslEngine.class, "destroyed");
    }
}
