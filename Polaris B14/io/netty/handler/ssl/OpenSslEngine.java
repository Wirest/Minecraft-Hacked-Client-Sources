/*      */ package io.netty.handler.ssl;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ReadOnlyBufferException;
/*      */ import java.security.Principal;
/*      */ import java.security.cert.Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*      */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*      */ import javax.net.ssl.SSLEngine;
/*      */ import javax.net.ssl.SSLEngineResult;
/*      */ import javax.net.ssl.SSLEngineResult.HandshakeStatus;
/*      */ import javax.net.ssl.SSLEngineResult.Status;
/*      */ import javax.net.ssl.SSLException;
/*      */ import javax.net.ssl.SSLPeerUnverifiedException;
/*      */ import javax.net.ssl.SSLSession;
/*      */ import javax.net.ssl.SSLSessionBindingEvent;
/*      */ import javax.net.ssl.SSLSessionBindingListener;
/*      */ import javax.net.ssl.SSLSessionContext;
/*      */ import javax.security.cert.CertificateException;
/*      */ import org.apache.tomcat.jni.Buffer;
/*      */ import org.apache.tomcat.jni.SSL;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class OpenSslEngine
/*      */   extends SSLEngine
/*      */ {
/*   61 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OpenSslEngine.class);
/*      */   
/*   63 */   private static final Certificate[] EMPTY_CERTIFICATES = new Certificate[0];
/*   64 */   private static final SSLException ENGINE_CLOSED = new SSLException("engine closed");
/*   65 */   private static final SSLException RENEGOTIATION_UNSUPPORTED = new SSLException("renegotiation unsupported");
/*   66 */   private static final SSLException ENCRYPTED_PACKET_OVERSIZED = new SSLException("encrypted packet oversized");
/*      */   
/*   68 */   static { ENGINE_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/*   69 */     RENEGOTIATION_UNSUPPORTED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/*   70 */     ENCRYPTED_PACKET_OVERSIZED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/*      */     
/*   72 */     AtomicIntegerFieldUpdater<OpenSslEngine> destroyedUpdater = PlatformDependent.newAtomicIntegerFieldUpdater(OpenSslEngine.class, "destroyed");
/*      */     
/*   74 */     if (destroyedUpdater == null) {
/*   75 */       destroyedUpdater = AtomicIntegerFieldUpdater.newUpdater(OpenSslEngine.class, "destroyed");
/*      */     }
/*   77 */     DESTROYED_UPDATER = destroyedUpdater;
/*   78 */     AtomicReferenceFieldUpdater<OpenSslEngine, SSLSession> sessionUpdater = PlatformDependent.newAtomicReferenceFieldUpdater(OpenSslEngine.class, "session");
/*      */     
/*   80 */     if (sessionUpdater == null) {
/*   81 */       sessionUpdater = AtomicReferenceFieldUpdater.newUpdater(OpenSslEngine.class, SSLSession.class, "session");
/*      */     }
/*   83 */     SESSION_UPDATER = sessionUpdater;
/*      */   }
/*      */   
/*      */ 
/*      */   private static final int MAX_PLAINTEXT_LENGTH = 16384;
/*      */   
/*      */   private static final int MAX_COMPRESSED_LENGTH = 17408;
/*      */   
/*      */   private static final int MAX_CIPHERTEXT_LENGTH = 18432;
/*      */   private static final String PROTOCOL_SSL_V2_HELLO = "SSLv2Hello";
/*      */   private static final String PROTOCOL_SSL_V2 = "SSLv2";
/*      */   private static final String PROTOCOL_SSL_V3 = "SSLv3";
/*      */   private static final String PROTOCOL_TLS_V1 = "TLSv1";
/*      */   private static final String PROTOCOL_TLS_V1_1 = "TLSv1.1";
/*      */   private static final String PROTOCOL_TLS_V1_2 = "TLSv1.2";
/*   98 */   private static final String[] SUPPORTED_PROTOCOLS = { "SSLv2Hello", "SSLv2", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  106 */   private static final Set<String> SUPPORTED_PROTOCOLS_SET = new HashSet(Arrays.asList(SUPPORTED_PROTOCOLS));
/*      */   static final int MAX_ENCRYPTED_PACKET_LENGTH = 18713;
/*      */   static final int MAX_ENCRYPTION_OVERHEAD_LENGTH = 2329;
/*      */   private static final AtomicIntegerFieldUpdater<OpenSslEngine> DESTROYED_UPDATER;
/*      */   private static final AtomicReferenceFieldUpdater<OpenSslEngine, SSLSession> SESSION_UPDATER;
/*      */   private static final String INVALID_CIPHER = "SSL_NULL_WITH_NULL_NULL";
/*      */   
/*      */   static enum ClientAuthMode {
/*  114 */     NONE, 
/*  115 */     OPTIONAL, 
/*  116 */     REQUIRE;
/*      */     
/*      */ 
/*      */ 
/*      */     private ClientAuthMode() {}
/*      */   }
/*      */   
/*      */ 
/*  124 */   private static final long EMPTY_ADDR = Buffer.address(Unpooled.EMPTY_BUFFER.nioBuffer());
/*      */   
/*      */ 
/*      */   private long ssl;
/*      */   
/*      */ 
/*      */   private long networkBIO;
/*      */   
/*      */   private int accepted;
/*      */   
/*      */   private boolean handshakeFinished;
/*      */   
/*      */   private boolean receivedShutdown;
/*      */   
/*      */   private volatile int destroyed;
/*      */   
/*      */   private volatile String cipher;
/*      */   
/*      */   private volatile String applicationProtocol;
/*      */   
/*      */   private volatile Certificate[] peerCerts;
/*      */   
/*  146 */   private volatile ClientAuthMode clientAuth = ClientAuthMode.NONE;
/*      */   
/*      */ 
/*      */   private boolean isInboundDone;
/*      */   
/*      */   private boolean isOutboundDone;
/*      */   
/*      */   private boolean engineClosed;
/*      */   
/*      */   private final boolean clientMode;
/*      */   
/*      */   private final ByteBufAllocator alloc;
/*      */   
/*      */   private final String fallbackApplicationProtocol;
/*      */   
/*      */   private final OpenSslSessionContext sessionContext;
/*      */   
/*      */   private volatile SSLSession session;
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public OpenSslEngine(long sslCtx, ByteBufAllocator alloc, String fallbackApplicationProtocol)
/*      */   {
/*  169 */     this(sslCtx, alloc, fallbackApplicationProtocol, false, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   OpenSslEngine(long sslCtx, ByteBufAllocator alloc, String fallbackApplicationProtocol, boolean clientMode, OpenSslSessionContext sessionContext)
/*      */   {
/*  182 */     OpenSsl.ensureAvailability();
/*  183 */     if (sslCtx == 0L) {
/*  184 */       throw new NullPointerException("sslContext");
/*      */     }
/*  186 */     if (alloc == null) {
/*  187 */       throw new NullPointerException("alloc");
/*      */     }
/*      */     
/*  190 */     this.alloc = alloc;
/*  191 */     this.ssl = SSL.newSSL(sslCtx, !clientMode);
/*  192 */     this.networkBIO = SSL.makeNetworkBIO(this.ssl);
/*  193 */     this.fallbackApplicationProtocol = fallbackApplicationProtocol;
/*  194 */     this.clientMode = clientMode;
/*  195 */     this.sessionContext = sessionContext;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized void shutdown()
/*      */   {
/*  202 */     if (DESTROYED_UPDATER.compareAndSet(this, 0, 1)) {
/*  203 */       SSL.freeSSL(this.ssl);
/*  204 */       SSL.freeBIO(this.networkBIO);
/*  205 */       this.ssl = (this.networkBIO = 0L);
/*      */       
/*      */ 
/*  208 */       this.isInboundDone = (this.isOutboundDone = this.engineClosed = 1);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int writePlaintextData(ByteBuffer src)
/*      */   {
/*  218 */     int pos = src.position();
/*  219 */     int limit = src.limit();
/*  220 */     int len = Math.min(limit - pos, 16384);
/*      */     
/*      */     int sslWrote;
/*  223 */     if (src.isDirect()) {
/*  224 */       long addr = Buffer.address(src) + pos;
/*  225 */       int sslWrote = SSL.writeToSSL(this.ssl, addr, len);
/*  226 */       if (sslWrote > 0) {
/*  227 */         src.position(pos + sslWrote);
/*  228 */         return sslWrote;
/*      */       }
/*      */     } else {
/*  231 */       ByteBuf buf = this.alloc.directBuffer(len);
/*      */       try {
/*  233 */         long addr = memoryAddress(buf);
/*      */         
/*  235 */         src.limit(pos + len);
/*      */         
/*  237 */         buf.setBytes(0, src);
/*  238 */         src.limit(limit);
/*      */         
/*  240 */         sslWrote = SSL.writeToSSL(this.ssl, addr, len);
/*  241 */         if (sslWrote > 0) {
/*  242 */           src.position(pos + sslWrote);
/*  243 */           return sslWrote;
/*      */         }
/*  245 */         src.position(pos);
/*      */       }
/*      */       finally {
/*  248 */         buf.release();
/*      */       }
/*      */     }
/*      */     
/*  252 */     throw new IllegalStateException("SSL.writeToSSL() returned a non-positive value: " + sslWrote);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int writeEncryptedData(ByteBuffer src)
/*      */   {
/*  259 */     int pos = src.position();
/*  260 */     int len = src.remaining();
/*  261 */     if (src.isDirect()) {
/*  262 */       long addr = Buffer.address(src) + pos;
/*  263 */       int netWrote = SSL.writeToBIO(this.networkBIO, addr, len);
/*  264 */       if (netWrote >= 0) {
/*  265 */         src.position(pos + netWrote);
/*  266 */         return netWrote;
/*      */       }
/*      */     } else {
/*  269 */       ByteBuf buf = this.alloc.directBuffer(len);
/*      */       try {
/*  271 */         long addr = memoryAddress(buf);
/*      */         
/*  273 */         buf.setBytes(0, src);
/*      */         
/*  275 */         int netWrote = SSL.writeToBIO(this.networkBIO, addr, len);
/*  276 */         if (netWrote >= 0) {
/*  277 */           src.position(pos + netWrote);
/*  278 */           return netWrote;
/*      */         }
/*  280 */         src.position(pos);
/*      */       }
/*      */       finally {
/*  283 */         buf.release();
/*      */       }
/*      */     }
/*      */     
/*  287 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int readPlaintextData(ByteBuffer dst)
/*      */   {
/*  294 */     if (dst.isDirect()) {
/*  295 */       int pos = dst.position();
/*  296 */       long addr = Buffer.address(dst) + pos;
/*  297 */       int len = dst.limit() - pos;
/*  298 */       int sslRead = SSL.readFromSSL(this.ssl, addr, len);
/*  299 */       if (sslRead > 0) {
/*  300 */         dst.position(pos + sslRead);
/*  301 */         return sslRead;
/*      */       }
/*      */     } else {
/*  304 */       int pos = dst.position();
/*  305 */       int limit = dst.limit();
/*  306 */       int len = Math.min(18713, limit - pos);
/*  307 */       ByteBuf buf = this.alloc.directBuffer(len);
/*      */       try {
/*  309 */         long addr = memoryAddress(buf);
/*      */         
/*  311 */         int sslRead = SSL.readFromSSL(this.ssl, addr, len);
/*  312 */         if (sslRead > 0) {
/*  313 */           dst.limit(pos + sslRead);
/*  314 */           buf.getBytes(0, dst);
/*  315 */           dst.limit(limit);
/*  316 */           return sslRead;
/*      */         }
/*      */       } finally {
/*  319 */         buf.release();
/*      */       }
/*      */     }
/*      */     
/*  323 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int readEncryptedData(ByteBuffer dst, int pending)
/*      */   {
/*  330 */     if ((dst.isDirect()) && (dst.remaining() >= pending)) {
/*  331 */       int pos = dst.position();
/*  332 */       long addr = Buffer.address(dst) + pos;
/*  333 */       int bioRead = SSL.readFromBIO(this.networkBIO, addr, pending);
/*  334 */       if (bioRead > 0) {
/*  335 */         dst.position(pos + bioRead);
/*  336 */         return bioRead;
/*      */       }
/*      */     } else {
/*  339 */       ByteBuf buf = this.alloc.directBuffer(pending);
/*      */       try {
/*  341 */         long addr = memoryAddress(buf);
/*      */         
/*  343 */         int bioRead = SSL.readFromBIO(this.networkBIO, addr, pending);
/*  344 */         if (bioRead > 0) {
/*  345 */           int oldLimit = dst.limit();
/*  346 */           dst.limit(dst.position() + bioRead);
/*  347 */           buf.getBytes(0, dst);
/*  348 */           dst.limit(oldLimit);
/*  349 */           return bioRead;
/*      */         }
/*      */       } finally {
/*  352 */         buf.release();
/*      */       }
/*      */     }
/*      */     
/*  356 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized SSLEngineResult wrap(ByteBuffer[] srcs, int offset, int length, ByteBuffer dst)
/*      */     throws SSLException
/*      */   {
/*  364 */     if (this.destroyed != 0) {
/*  365 */       return new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
/*      */     }
/*      */     
/*      */ 
/*  369 */     if (srcs == null) {
/*  370 */       throw new IllegalArgumentException("srcs is null");
/*      */     }
/*  372 */     if (dst == null) {
/*  373 */       throw new IllegalArgumentException("dst is null");
/*      */     }
/*      */     
/*  376 */     if ((offset >= srcs.length) || (offset + length > srcs.length)) {
/*  377 */       throw new IndexOutOfBoundsException("offset: " + offset + ", length: " + length + " (expected: offset <= offset + length <= srcs.length (" + srcs.length + "))");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  382 */     if (dst.isReadOnly()) {
/*  383 */       throw new ReadOnlyBufferException();
/*      */     }
/*      */     
/*      */ 
/*  387 */     if (this.accepted == 0) {
/*  388 */       beginHandshakeImplicitly();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  393 */     SSLEngineResult.HandshakeStatus handshakeStatus = getHandshakeStatus();
/*      */     
/*  395 */     if (((!this.handshakeFinished) || (this.engineClosed)) && (handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP)) {
/*  396 */       return new SSLEngineResult(getEngineStatus(), SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
/*      */     }
/*      */     
/*  399 */     int bytesProduced = 0;
/*      */     
/*      */ 
/*      */ 
/*  403 */     int pendingNet = SSL.pendingWrittenBytesInBIO(this.networkBIO);
/*  404 */     if (pendingNet > 0)
/*      */     {
/*  406 */       int capacity = dst.remaining();
/*  407 */       if (capacity < pendingNet) {
/*  408 */         return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, handshakeStatus, 0, bytesProduced);
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  413 */         bytesProduced += readEncryptedData(dst, pendingNet);
/*      */       } catch (Exception e) {
/*  415 */         throw new SSLException(e);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  421 */       if (this.isOutboundDone) {
/*  422 */         shutdown();
/*      */       }
/*      */       
/*  425 */       return new SSLEngineResult(getEngineStatus(), getHandshakeStatus(), 0, bytesProduced);
/*      */     }
/*      */     
/*      */ 
/*  429 */     int bytesConsumed = 0;
/*  430 */     int endOffset = offset + length;
/*  431 */     for (int i = offset; i < endOffset; i++) {
/*  432 */       ByteBuffer src = srcs[i];
/*  433 */       if (src == null) {
/*  434 */         throw new IllegalArgumentException("srcs[" + i + "] is null");
/*      */       }
/*  436 */       while (src.hasRemaining())
/*      */       {
/*      */         try
/*      */         {
/*  440 */           bytesConsumed += writePlaintextData(src);
/*      */         } catch (Exception e) {
/*  442 */           throw new SSLException(e);
/*      */         }
/*      */         
/*      */ 
/*  446 */         pendingNet = SSL.pendingWrittenBytesInBIO(this.networkBIO);
/*  447 */         if (pendingNet > 0)
/*      */         {
/*  449 */           int capacity = dst.remaining();
/*  450 */           if (capacity < pendingNet) {
/*  451 */             return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, getHandshakeStatus(), bytesConsumed, bytesProduced);
/*      */           }
/*      */           
/*      */ 
/*      */           try
/*      */           {
/*  457 */             bytesProduced += readEncryptedData(dst, pendingNet);
/*      */           } catch (Exception e) {
/*  459 */             throw new SSLException(e);
/*      */           }
/*      */           
/*  462 */           return new SSLEngineResult(getEngineStatus(), getHandshakeStatus(), bytesConsumed, bytesProduced);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  467 */     return new SSLEngineResult(getEngineStatus(), getHandshakeStatus(), bytesConsumed, bytesProduced);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized SSLEngineResult unwrap(ByteBuffer[] srcs, int srcsOffset, int srcsLength, ByteBuffer[] dsts, int dstsOffset, int dstsLength)
/*      */     throws SSLException
/*      */   {
/*  475 */     if (this.destroyed != 0) {
/*  476 */       return new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
/*      */     }
/*      */     
/*      */ 
/*  480 */     if (srcs == null) {
/*  481 */       throw new NullPointerException("srcs");
/*      */     }
/*  483 */     if ((srcsOffset >= srcs.length) || (srcsOffset + srcsLength > srcs.length))
/*      */     {
/*  485 */       throw new IndexOutOfBoundsException("offset: " + srcsOffset + ", length: " + srcsLength + " (expected: offset <= offset + length <= srcs.length (" + srcs.length + "))");
/*      */     }
/*      */     
/*      */ 
/*  489 */     if (dsts == null) {
/*  490 */       throw new IllegalArgumentException("dsts is null");
/*      */     }
/*  492 */     if ((dstsOffset >= dsts.length) || (dstsOffset + dstsLength > dsts.length)) {
/*  493 */       throw new IndexOutOfBoundsException("offset: " + dstsOffset + ", length: " + dstsLength + " (expected: offset <= offset + length <= dsts.length (" + dsts.length + "))");
/*      */     }
/*      */     
/*      */ 
/*  497 */     int capacity = 0;
/*  498 */     int endOffset = dstsOffset + dstsLength;
/*  499 */     for (int i = dstsOffset; i < endOffset; i++) {
/*  500 */       ByteBuffer dst = dsts[i];
/*  501 */       if (dst == null) {
/*  502 */         throw new IllegalArgumentException("dsts[" + i + "] is null");
/*      */       }
/*  504 */       if (dst.isReadOnly()) {
/*  505 */         throw new ReadOnlyBufferException();
/*      */       }
/*  507 */       capacity += dst.remaining();
/*      */     }
/*      */     
/*      */ 
/*  511 */     if (this.accepted == 0) {
/*  512 */       beginHandshakeImplicitly();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  517 */     SSLEngineResult.HandshakeStatus handshakeStatus = getHandshakeStatus();
/*  518 */     if (((!this.handshakeFinished) || (this.engineClosed)) && (handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_WRAP)) {
/*  519 */       return new SSLEngineResult(getEngineStatus(), SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
/*      */     }
/*      */     
/*  522 */     int srcsEndOffset = srcsOffset + srcsLength;
/*  523 */     int len = 0;
/*  524 */     for (int i = srcsOffset; i < srcsEndOffset; i++) {
/*  525 */       ByteBuffer src = srcs[i];
/*  526 */       if (src == null) {
/*  527 */         throw new IllegalArgumentException("srcs[" + i + "] is null");
/*      */       }
/*  529 */       len += src.remaining();
/*      */     }
/*      */     
/*      */ 
/*  533 */     if (len > 18713) {
/*  534 */       this.isInboundDone = true;
/*  535 */       this.isOutboundDone = true;
/*  536 */       this.engineClosed = true;
/*  537 */       shutdown();
/*  538 */       throw ENCRYPTED_PACKET_OVERSIZED;
/*      */     }
/*      */     
/*      */ 
/*  542 */     int bytesConsumed = -1;
/*      */     try {
/*  544 */       while (srcsOffset < srcsEndOffset) {
/*  545 */         ByteBuffer src = srcs[srcsOffset];
/*  546 */         int remaining = src.remaining();
/*  547 */         int written = writeEncryptedData(src);
/*  548 */         if (written < 0) break;
/*  549 */         if (bytesConsumed == -1) {
/*  550 */           bytesConsumed = written;
/*      */         } else {
/*  552 */           bytesConsumed += written;
/*      */         }
/*  554 */         if (written == remaining)
/*  555 */           srcsOffset++; else {
/*  556 */           if (written == 0) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  564 */       throw new SSLException(e);
/*      */     }
/*  566 */     if (bytesConsumed >= 0) {
/*  567 */       int lastPrimingReadResult = SSL.readFromSSL(this.ssl, EMPTY_ADDR, 0);
/*      */       
/*      */ 
/*      */ 
/*  571 */       if (lastPrimingReadResult <= 0)
/*      */       {
/*  573 */         long error = SSL.getLastErrorNumber();
/*  574 */         if (OpenSsl.isError(error)) {
/*  575 */           String err = SSL.getErrorString(error);
/*  576 */           if (logger.isDebugEnabled()) {
/*  577 */             logger.debug("SSL_read failed: primingReadResult: " + lastPrimingReadResult + "; OpenSSL error: '" + err + '\'');
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  583 */           shutdown();
/*  584 */           throw new SSLException(err);
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  589 */       bytesConsumed = 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  595 */     int pendingApp = (this.handshakeFinished) || (SSL.isInInit(this.ssl) == 0) ? SSL.pendingReadableBytesInSSL(this.ssl) : 0;
/*  596 */     int bytesProduced = 0;
/*      */     
/*  598 */     if (pendingApp > 0)
/*      */     {
/*  600 */       if (capacity < pendingApp) {
/*  601 */         return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, getHandshakeStatus(), bytesConsumed, 0);
/*      */       }
/*      */       
/*      */ 
/*  605 */       int idx = dstsOffset;
/*  606 */       while (idx < endOffset) {
/*  607 */         ByteBuffer dst = dsts[idx];
/*  608 */         if (!dst.hasRemaining()) {
/*  609 */           idx++;
/*      */         }
/*      */         else
/*      */         {
/*  613 */           if (pendingApp <= 0) {
/*      */             break;
/*      */           }
/*      */           int bytesRead;
/*      */           try
/*      */           {
/*  619 */             bytesRead = readPlaintextData(dst);
/*      */           } catch (Exception e) {
/*  621 */             throw new SSLException(e);
/*      */           }
/*      */           
/*  624 */           if (bytesRead == 0) {
/*      */             break;
/*      */           }
/*      */           
/*  628 */           bytesProduced += bytesRead;
/*  629 */           pendingApp -= bytesRead;
/*      */           
/*  631 */           if (!dst.hasRemaining()) {
/*  632 */             idx++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  638 */     if ((!this.receivedShutdown) && ((SSL.getShutdown(this.ssl) & 0x2) == 2)) {
/*  639 */       this.receivedShutdown = true;
/*  640 */       closeOutbound();
/*  641 */       closeInbound();
/*      */     }
/*      */     
/*  644 */     return new SSLEngineResult(getEngineStatus(), getHandshakeStatus(), bytesConsumed, bytesProduced);
/*      */   }
/*      */   
/*      */   public SSLEngineResult unwrap(ByteBuffer[] srcs, ByteBuffer[] dsts) throws SSLException {
/*  648 */     return unwrap(srcs, 0, srcs.length, dsts, 0, dsts.length);
/*      */   }
/*      */   
/*      */   public SSLEngineResult unwrap(ByteBuffer src, ByteBuffer[] dsts, int offset, int length)
/*      */     throws SSLException
/*      */   {
/*  654 */     return unwrap(new ByteBuffer[] { src }, 0, 1, dsts, offset, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Runnable getDelegatedTask()
/*      */   {
/*  662 */     return null;
/*      */   }
/*      */   
/*      */   public synchronized void closeInbound() throws SSLException
/*      */   {
/*  667 */     if (this.isInboundDone) {
/*  668 */       return;
/*      */     }
/*      */     
/*  671 */     this.isInboundDone = true;
/*  672 */     this.engineClosed = true;
/*      */     
/*  674 */     shutdown();
/*      */     
/*  676 */     if ((this.accepted != 0) && (!this.receivedShutdown)) {
/*  677 */       throw new SSLException("Inbound closed before receiving peer's close_notify: possible truncation attack?");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized boolean isInboundDone()
/*      */   {
/*  684 */     return (this.isInboundDone) || (this.engineClosed);
/*      */   }
/*      */   
/*      */   public synchronized void closeOutbound()
/*      */   {
/*  689 */     if (this.isOutboundDone) {
/*  690 */       return;
/*      */     }
/*      */     
/*  693 */     this.isOutboundDone = true;
/*  694 */     this.engineClosed = true;
/*      */     
/*  696 */     if ((this.accepted != 0) && (this.destroyed == 0)) {
/*  697 */       int mode = SSL.getShutdown(this.ssl);
/*  698 */       if ((mode & 0x1) != 1) {
/*  699 */         SSL.shutdownSSL(this.ssl);
/*      */       }
/*      */     }
/*      */     else {
/*  703 */       shutdown();
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized boolean isOutboundDone()
/*      */   {
/*  709 */     return this.isOutboundDone;
/*      */   }
/*      */   
/*      */   public String[] getSupportedCipherSuites()
/*      */   {
/*  714 */     Set<String> availableCipherSuites = OpenSsl.availableCipherSuites();
/*  715 */     return (String[])availableCipherSuites.toArray(new String[availableCipherSuites.size()]);
/*      */   }
/*      */   
/*      */   public String[] getEnabledCipherSuites()
/*      */   {
/*  720 */     String[] enabled = SSL.getCiphers(this.ssl);
/*  721 */     if (enabled == null) {
/*  722 */       return EmptyArrays.EMPTY_STRINGS;
/*      */     }
/*  724 */     for (int i = 0; i < enabled.length; i++) {
/*  725 */       String mapped = toJavaCipherSuite(enabled[i]);
/*  726 */       if (mapped != null) {
/*  727 */         enabled[i] = mapped;
/*      */       }
/*      */     }
/*  730 */     return enabled;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setEnabledCipherSuites(String[] cipherSuites)
/*      */   {
/*  736 */     if (cipherSuites == null) {
/*  737 */       throw new NullPointerException("cipherSuites");
/*      */     }
/*      */     
/*  740 */     StringBuilder buf = new StringBuilder();
/*  741 */     for (String c : cipherSuites) {
/*  742 */       if (c == null) {
/*      */         break;
/*      */       }
/*      */       
/*  746 */       String converted = CipherSuiteConverter.toOpenSsl(c);
/*  747 */       if (converted == null) {
/*  748 */         converted = c;
/*      */       }
/*      */       
/*  751 */       if (!OpenSsl.isCipherSuiteAvailable(converted)) {
/*  752 */         throw new IllegalArgumentException("unsupported cipher suite: " + c + '(' + converted + ')');
/*      */       }
/*      */       
/*  755 */       buf.append(converted);
/*  756 */       buf.append(':');
/*      */     }
/*      */     
/*  759 */     if (buf.length() == 0) {
/*  760 */       throw new IllegalArgumentException("empty cipher suites");
/*      */     }
/*  762 */     buf.setLength(buf.length() - 1);
/*      */     
/*  764 */     String cipherSuiteSpec = buf.toString();
/*      */     try {
/*  766 */       SSL.setCipherSuites(this.ssl, cipherSuiteSpec);
/*      */     } catch (Exception e) {
/*  768 */       throw new IllegalStateException("failed to enable cipher suites: " + cipherSuiteSpec, e);
/*      */     }
/*      */   }
/*      */   
/*      */   public String[] getSupportedProtocols()
/*      */   {
/*  774 */     return (String[])SUPPORTED_PROTOCOLS.clone();
/*      */   }
/*      */   
/*      */   public String[] getEnabledProtocols()
/*      */   {
/*  779 */     List<String> enabled = new ArrayList();
/*      */     
/*  781 */     enabled.add("SSLv2Hello");
/*  782 */     int opts = SSL.getOptions(this.ssl);
/*  783 */     if ((opts & 0x4000000) == 0) {
/*  784 */       enabled.add("TLSv1");
/*      */     }
/*  786 */     if ((opts & 0x8000000) == 0) {
/*  787 */       enabled.add("TLSv1.1");
/*      */     }
/*  789 */     if ((opts & 0x10000000) == 0) {
/*  790 */       enabled.add("TLSv1.2");
/*      */     }
/*  792 */     if ((opts & 0x1000000) == 0) {
/*  793 */       enabled.add("SSLv2");
/*      */     }
/*  795 */     if ((opts & 0x2000000) == 0) {
/*  796 */       enabled.add("SSLv3");
/*      */     }
/*  798 */     int size = enabled.size();
/*  799 */     if (size == 0) {
/*  800 */       return EmptyArrays.EMPTY_STRINGS;
/*      */     }
/*  802 */     return (String[])enabled.toArray(new String[size]);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setEnabledProtocols(String[] protocols)
/*      */   {
/*  808 */     if (protocols == null)
/*      */     {
/*  810 */       throw new IllegalArgumentException();
/*      */     }
/*  812 */     boolean sslv2 = false;
/*  813 */     boolean sslv3 = false;
/*  814 */     boolean tlsv1 = false;
/*  815 */     boolean tlsv1_1 = false;
/*  816 */     boolean tlsv1_2 = false;
/*  817 */     for (String p : protocols) {
/*  818 */       if (!SUPPORTED_PROTOCOLS_SET.contains(p)) {
/*  819 */         throw new IllegalArgumentException("Protocol " + p + " is not supported.");
/*      */       }
/*  821 */       if (p.equals("SSLv2")) {
/*  822 */         sslv2 = true;
/*  823 */       } else if (p.equals("SSLv3")) {
/*  824 */         sslv3 = true;
/*  825 */       } else if (p.equals("TLSv1")) {
/*  826 */         tlsv1 = true;
/*  827 */       } else if (p.equals("TLSv1.1")) {
/*  828 */         tlsv1_1 = true;
/*  829 */       } else if (p.equals("TLSv1.2")) {
/*  830 */         tlsv1_2 = true;
/*      */       }
/*      */     }
/*      */     
/*  834 */     SSL.setOptions(this.ssl, 4095);
/*      */     
/*  836 */     if (!sslv2) {
/*  837 */       SSL.setOptions(this.ssl, 16777216);
/*      */     }
/*  839 */     if (!sslv3) {
/*  840 */       SSL.setOptions(this.ssl, 33554432);
/*      */     }
/*  842 */     if (!tlsv1) {
/*  843 */       SSL.setOptions(this.ssl, 67108864);
/*      */     }
/*  845 */     if (!tlsv1_1) {
/*  846 */       SSL.setOptions(this.ssl, 134217728);
/*      */     }
/*  848 */     if (!tlsv1_2) {
/*  849 */       SSL.setOptions(this.ssl, 268435456);
/*      */     }
/*      */   }
/*      */   
/*      */   private Certificate[] initPeerCertChain() throws SSLPeerUnverifiedException {
/*  854 */     byte[][] chain = SSL.getPeerCertChain(this.ssl);
/*      */     byte[] clientCert;
/*  856 */     byte[] clientCert; if (!this.clientMode)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  861 */       clientCert = SSL.getPeerCertificate(this.ssl);
/*      */     } else {
/*  863 */       clientCert = null;
/*      */     }
/*      */     
/*  866 */     if ((chain == null) && (clientCert == null)) {
/*  867 */       throw new SSLPeerUnverifiedException("peer not verified");
/*      */     }
/*  869 */     int len = 0;
/*  870 */     if (chain != null) {
/*  871 */       len += chain.length;
/*      */     }
/*      */     
/*  874 */     int i = 0;
/*      */     Certificate[] peerCerts;
/*  876 */     if (clientCert != null) {
/*  877 */       len++;
/*  878 */       Certificate[] peerCerts = new Certificate[len];
/*  879 */       peerCerts[(i++)] = new OpenSslX509Certificate(clientCert);
/*      */     } else {
/*  881 */       peerCerts = new Certificate[len];
/*      */     }
/*  883 */     if (chain != null) {
/*  884 */       int a = 0;
/*  885 */       for (; i < peerCerts.length; i++) {
/*  886 */         peerCerts[i] = new OpenSslX509Certificate(chain[(a++)]);
/*      */       }
/*      */     }
/*  889 */     return peerCerts;
/*      */   }
/*      */   
/*      */ 
/*      */   public SSLSession getSession()
/*      */   {
/*  895 */     SSLSession session = this.session;
/*  896 */     if (session == null) {
/*  897 */       session = new SSLSession()
/*      */       {
/*      */         private javax.security.cert.X509Certificate[] x509PeerCerts;
/*      */         
/*      */ 
/*      */         private Map<String, Object> values;
/*      */         
/*      */ 
/*      */         public byte[] getId()
/*      */         {
/*  907 */           byte[] id = SSL.getSessionId(OpenSslEngine.this.ssl);
/*  908 */           if (id == null)
/*      */           {
/*  910 */             throw new IllegalStateException("SSL session ID not available");
/*      */           }
/*  912 */           return id;
/*      */         }
/*      */         
/*      */         public SSLSessionContext getSessionContext()
/*      */         {
/*  917 */           return OpenSslEngine.this.sessionContext;
/*      */         }
/*      */         
/*      */ 
/*      */         public long getCreationTime()
/*      */         {
/*  923 */           return SSL.getTime(OpenSslEngine.this.ssl) * 1000L;
/*      */         }
/*      */         
/*      */ 
/*      */         public long getLastAccessedTime()
/*      */         {
/*  929 */           return getCreationTime();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */         public void invalidate() {}
/*      */         
/*      */ 
/*      */         public boolean isValid()
/*      */         {
/*  939 */           return false;
/*      */         }
/*      */         
/*      */         public void putValue(String name, Object value)
/*      */         {
/*  944 */           if (name == null) {
/*  945 */             throw new NullPointerException("name");
/*      */           }
/*  947 */           if (value == null) {
/*  948 */             throw new NullPointerException("value");
/*      */           }
/*  950 */           Map<String, Object> values = this.values;
/*  951 */           if (values == null)
/*      */           {
/*  953 */             values = this.values = new HashMap(2);
/*      */           }
/*  955 */           Object old = values.put(name, value);
/*  956 */           if ((value instanceof SSLSessionBindingListener)) {
/*  957 */             ((SSLSessionBindingListener)value).valueBound(new SSLSessionBindingEvent(this, name));
/*      */           }
/*  959 */           notifyUnbound(old, name);
/*      */         }
/*      */         
/*      */         public Object getValue(String name)
/*      */         {
/*  964 */           if (name == null) {
/*  965 */             throw new NullPointerException("name");
/*      */           }
/*  967 */           if (this.values == null) {
/*  968 */             return null;
/*      */           }
/*  970 */           return this.values.get(name);
/*      */         }
/*      */         
/*      */         public void removeValue(String name)
/*      */         {
/*  975 */           if (name == null) {
/*  976 */             throw new NullPointerException("name");
/*      */           }
/*  978 */           Map<String, Object> values = this.values;
/*  979 */           if (values == null) {
/*  980 */             return;
/*      */           }
/*  982 */           Object old = values.remove(name);
/*  983 */           notifyUnbound(old, name);
/*      */         }
/*      */         
/*      */         public String[] getValueNames()
/*      */         {
/*  988 */           Map<String, Object> values = this.values;
/*  989 */           if ((values == null) || (values.isEmpty())) {
/*  990 */             return EmptyArrays.EMPTY_STRINGS;
/*      */           }
/*  992 */           return (String[])values.keySet().toArray(new String[values.size()]);
/*      */         }
/*      */         
/*      */         private void notifyUnbound(Object value, String name) {
/*  996 */           if ((value instanceof SSLSessionBindingListener)) {
/*  997 */             ((SSLSessionBindingListener)value).valueUnbound(new SSLSessionBindingEvent(this, name));
/*      */           }
/*      */         }
/*      */         
/*      */         public Certificate[] getPeerCertificates()
/*      */           throws SSLPeerUnverifiedException
/*      */         {
/* 1004 */           Certificate[] c = OpenSslEngine.this.peerCerts;
/* 1005 */           if (c == null) {
/* 1006 */             if (SSL.isInInit(OpenSslEngine.this.ssl) != 0) {
/* 1007 */               throw new SSLPeerUnverifiedException("peer not verified");
/*      */             }
/* 1009 */             c = OpenSslEngine.this.peerCerts = OpenSslEngine.this.initPeerCertChain();
/*      */           }
/* 1011 */           return c;
/*      */         }
/*      */         
/*      */ 
/*      */         public Certificate[] getLocalCertificates()
/*      */         {
/* 1017 */           return OpenSslEngine.EMPTY_CERTIFICATES;
/*      */         }
/*      */         
/*      */         public javax.security.cert.X509Certificate[] getPeerCertificateChain()
/*      */           throws SSLPeerUnverifiedException
/*      */         {
/* 1023 */           javax.security.cert.X509Certificate[] c = this.x509PeerCerts;
/* 1024 */           if (c == null) {
/* 1025 */             if (SSL.isInInit(OpenSslEngine.this.ssl) != 0) {
/* 1026 */               throw new SSLPeerUnverifiedException("peer not verified");
/*      */             }
/* 1028 */             byte[][] chain = SSL.getPeerCertChain(OpenSslEngine.this.ssl);
/* 1029 */             if (chain == null) {
/* 1030 */               throw new SSLPeerUnverifiedException("peer not verified");
/*      */             }
/* 1032 */             javax.security.cert.X509Certificate[] peerCerts = new javax.security.cert.X509Certificate[chain.length];
/* 1033 */             for (int i = 0; i < peerCerts.length; i++) {
/*      */               try {
/* 1035 */                 peerCerts[i] = javax.security.cert.X509Certificate.getInstance(chain[i]);
/*      */               } catch (CertificateException e) {
/* 1037 */                 throw new IllegalStateException(e);
/*      */               }
/*      */             }
/* 1040 */             c = this.x509PeerCerts = peerCerts;
/*      */           }
/* 1042 */           return c;
/*      */         }
/*      */         
/*      */         public Principal getPeerPrincipal() throws SSLPeerUnverifiedException
/*      */         {
/* 1047 */           Certificate[] peer = getPeerCertificates();
/* 1048 */           if ((peer == null) || (peer.length == 0)) {
/* 1049 */             return null;
/*      */           }
/* 1051 */           return principal(peer);
/*      */         }
/*      */         
/*      */         public Principal getLocalPrincipal()
/*      */         {
/* 1056 */           Certificate[] local = getLocalCertificates();
/* 1057 */           if ((local == null) || (local.length == 0)) {
/* 1058 */             return null;
/*      */           }
/* 1060 */           return principal(local);
/*      */         }
/*      */         
/*      */         private Principal principal(Certificate[] certs) {
/* 1064 */           return ((java.security.cert.X509Certificate)certs[0]).getIssuerX500Principal();
/*      */         }
/*      */         
/*      */         public String getCipherSuite()
/*      */         {
/* 1069 */           if (!OpenSslEngine.this.handshakeFinished) {
/* 1070 */             return "SSL_NULL_WITH_NULL_NULL";
/*      */           }
/* 1072 */           if (OpenSslEngine.this.cipher == null) {
/* 1073 */             String c = OpenSslEngine.this.toJavaCipherSuite(SSL.getCipherForSSL(OpenSslEngine.this.ssl));
/* 1074 */             if (c != null) {
/* 1075 */               OpenSslEngine.this.cipher = c;
/*      */             }
/*      */           }
/* 1078 */           return OpenSslEngine.this.cipher;
/*      */         }
/*      */         
/*      */         public String getProtocol()
/*      */         {
/* 1083 */           String applicationProtocol = OpenSslEngine.this.applicationProtocol;
/* 1084 */           if (applicationProtocol == null) {
/* 1085 */             applicationProtocol = SSL.getNextProtoNegotiated(OpenSslEngine.this.ssl);
/* 1086 */             if (applicationProtocol == null) {
/* 1087 */               applicationProtocol = OpenSslEngine.this.fallbackApplicationProtocol;
/*      */             }
/* 1089 */             if (applicationProtocol != null) {
/* 1090 */               OpenSslEngine.this.applicationProtocol = applicationProtocol.replace(':', '_');
/*      */             } else {
/* 1092 */               OpenSslEngine.this.applicationProtocol = (applicationProtocol = "");
/*      */             }
/*      */           }
/* 1095 */           String version = SSL.getVersion(OpenSslEngine.this.ssl);
/* 1096 */           if (applicationProtocol.isEmpty()) {
/* 1097 */             return version;
/*      */           }
/* 1099 */           return version + ':' + applicationProtocol;
/*      */         }
/*      */         
/*      */ 
/*      */         public String getPeerHost()
/*      */         {
/* 1105 */           return null;
/*      */         }
/*      */         
/*      */         public int getPeerPort()
/*      */         {
/* 1110 */           return 0;
/*      */         }
/*      */         
/*      */         public int getPacketBufferSize()
/*      */         {
/* 1115 */           return 18713;
/*      */         }
/*      */         
/*      */         public int getApplicationBufferSize()
/*      */         {
/* 1120 */           return 16384;
/*      */         }
/*      */       };
/*      */       
/* 1124 */       if (!SESSION_UPDATER.compareAndSet(this, null, session))
/*      */       {
/* 1126 */         session = this.session;
/*      */       }
/*      */     }
/*      */     
/* 1130 */     return session;
/*      */   }
/*      */   
/*      */   public synchronized void beginHandshake() throws SSLException
/*      */   {
/* 1135 */     if ((this.engineClosed) || (this.destroyed != 0)) {
/* 1136 */       throw ENGINE_CLOSED;
/*      */     }
/* 1138 */     switch (this.accepted) {
/*      */     case 0: 
/* 1140 */       handshake();
/* 1141 */       this.accepted = 2;
/* 1142 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 1: 
/* 1150 */       this.accepted = 2;
/* 1151 */       break;
/*      */     case 2: 
/* 1153 */       throw RENEGOTIATION_UNSUPPORTED;
/*      */     default: 
/* 1155 */       throw new Error();
/*      */     }
/*      */   }
/*      */   
/*      */   private void beginHandshakeImplicitly() throws SSLException {
/* 1160 */     if ((this.engineClosed) || (this.destroyed != 0)) {
/* 1161 */       throw ENGINE_CLOSED;
/*      */     }
/*      */     
/* 1164 */     if (this.accepted == 0) {
/* 1165 */       handshake();
/* 1166 */       this.accepted = 1;
/*      */     }
/*      */   }
/*      */   
/*      */   private void handshake() throws SSLException {
/* 1171 */     int code = SSL.doHandshake(this.ssl);
/* 1172 */     if (code <= 0)
/*      */     {
/* 1174 */       long error = SSL.getLastErrorNumber();
/* 1175 */       if (OpenSsl.isError(error)) {
/* 1176 */         String err = SSL.getErrorString(error);
/* 1177 */         if (logger.isDebugEnabled()) {
/* 1178 */           logger.debug("SSL_do_handshake failed: OpenSSL error: '" + err + '\'');
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1183 */         shutdown();
/* 1184 */         throw new SSLException(err);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1189 */       this.handshakeFinished = true;
/*      */     }
/*      */   }
/*      */   
/*      */   private static long memoryAddress(ByteBuf buf) {
/* 1194 */     if (buf.hasMemoryAddress()) {
/* 1195 */       return buf.memoryAddress();
/*      */     }
/* 1197 */     return Buffer.address(buf.nioBuffer());
/*      */   }
/*      */   
/*      */   private SSLEngineResult.Status getEngineStatus()
/*      */   {
/* 1202 */     return this.engineClosed ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK;
/*      */   }
/*      */   
/*      */   public synchronized SSLEngineResult.HandshakeStatus getHandshakeStatus()
/*      */   {
/* 1207 */     if ((this.accepted == 0) || (this.destroyed != 0)) {
/* 1208 */       return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
/*      */     }
/*      */     
/*      */ 
/* 1212 */     if (!this.handshakeFinished)
/*      */     {
/* 1214 */       if (SSL.pendingWrittenBytesInBIO(this.networkBIO) != 0) {
/* 1215 */         return SSLEngineResult.HandshakeStatus.NEED_WRAP;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1220 */       if (SSL.isInInit(this.ssl) == 0) {
/* 1221 */         this.handshakeFinished = true;
/* 1222 */         return SSLEngineResult.HandshakeStatus.FINISHED;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1227 */       return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
/*      */     }
/*      */     
/*      */ 
/* 1231 */     if (this.engineClosed)
/*      */     {
/* 1233 */       if (SSL.pendingWrittenBytesInBIO(this.networkBIO) != 0) {
/* 1234 */         return SSLEngineResult.HandshakeStatus.NEED_WRAP;
/*      */       }
/*      */       
/*      */ 
/* 1238 */       return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
/*      */     }
/*      */     
/* 1241 */     return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private String toJavaCipherSuite(String openSslCipherSuite)
/*      */   {
/* 1248 */     if (openSslCipherSuite == null) {
/* 1249 */       return null;
/*      */     }
/*      */     
/* 1252 */     String prefix = toJavaCipherSuitePrefix(SSL.getVersion(this.ssl));
/* 1253 */     return CipherSuiteConverter.toJava(openSslCipherSuite, prefix);
/*      */   }
/*      */   
/*      */ 
/*      */   private static String toJavaCipherSuitePrefix(String protocolVersion)
/*      */   {
/*      */     char c;
/*      */     char c;
/* 1261 */     if ((protocolVersion == null) || (protocolVersion.length() == 0)) {
/* 1262 */       c = '\000';
/*      */     } else {
/* 1264 */       c = protocolVersion.charAt(0);
/*      */     }
/*      */     
/* 1267 */     switch (c) {
/*      */     case 'T': 
/* 1269 */       return "TLS";
/*      */     case 'S': 
/* 1271 */       return "SSL";
/*      */     }
/* 1273 */     return "UNKNOWN";
/*      */   }
/*      */   
/*      */ 
/*      */   public void setUseClientMode(boolean clientMode)
/*      */   {
/* 1279 */     if (clientMode != this.clientMode) {
/* 1280 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean getUseClientMode()
/*      */   {
/* 1286 */     return this.clientMode;
/*      */   }
/*      */   
/*      */   public void setNeedClientAuth(boolean b)
/*      */   {
/* 1291 */     setClientAuth(b ? ClientAuthMode.REQUIRE : ClientAuthMode.NONE);
/*      */   }
/*      */   
/*      */   public boolean getNeedClientAuth()
/*      */   {
/* 1296 */     return this.clientAuth == ClientAuthMode.REQUIRE;
/*      */   }
/*      */   
/*      */   public void setWantClientAuth(boolean b)
/*      */   {
/* 1301 */     setClientAuth(b ? ClientAuthMode.OPTIONAL : ClientAuthMode.NONE);
/*      */   }
/*      */   
/*      */   public boolean getWantClientAuth()
/*      */   {
/* 1306 */     return this.clientAuth == ClientAuthMode.OPTIONAL;
/*      */   }
/*      */   
/*      */   private void setClientAuth(ClientAuthMode mode) {
/* 1310 */     if (this.clientMode) {
/* 1311 */       return;
/*      */     }
/* 1313 */     synchronized (this) {
/* 1314 */       if (this.clientAuth == mode)
/*      */       {
/* 1316 */         return;
/*      */       }
/* 1318 */       switch (mode) {
/*      */       case NONE: 
/* 1320 */         SSL.setVerify(this.ssl, 0, 10);
/* 1321 */         break;
/*      */       case REQUIRE: 
/* 1323 */         SSL.setVerify(this.ssl, 2, 10);
/* 1324 */         break;
/*      */       case OPTIONAL: 
/* 1326 */         SSL.setVerify(this.ssl, 1, 10);
/*      */       }
/*      */       
/* 1329 */       this.clientAuth = mode;
/*      */     }
/*      */   }
/*      */   
/*      */   public void setEnableSessionCreation(boolean b)
/*      */   {
/* 1335 */     if (b) {
/* 1336 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean getEnableSessionCreation()
/*      */   {
/* 1342 */     return false;
/*      */   }
/*      */   
/*      */   protected void finalize()
/*      */     throws Throwable
/*      */   {
/* 1348 */     super.finalize();
/*      */     
/* 1350 */     shutdown();
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\OpenSslEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */