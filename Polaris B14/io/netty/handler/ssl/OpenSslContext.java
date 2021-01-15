/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import org.apache.tomcat.jni.Pool;
/*     */ import org.apache.tomcat.jni.SSLContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class OpenSslContext
/*     */   extends SslContext
/*     */ {
/*  40 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OpenSslContext.class);
/*     */   
/*     */   private static final List<String> DEFAULT_CIPHERS;
/*     */   
/*     */   private static final AtomicIntegerFieldUpdater<OpenSslContext> DESTROY_UPDATER;
/*     */   
/*     */   protected static final int VERIFY_DEPTH = 10;
/*     */   
/*     */   private final long aprPool;
/*     */   private volatile int aprPoolDestroyed;
/*  50 */   private final List<String> ciphers = new ArrayList();
/*  51 */   private final List<String> unmodifiableCiphers = Collections.unmodifiableList(this.ciphers);
/*     */   private final long sessionCacheSize;
/*     */   private final long sessionTimeout;
/*     */   private final OpenSslApplicationProtocolNegotiator apn;
/*     */   protected final long ctx;
/*     */   private final int mode;
/*     */   
/*     */   static
/*     */   {
/*  60 */     List<String> ciphers = new ArrayList();
/*     */     
/*  62 */     Collections.addAll(ciphers, new String[] { "ECDHE-RSA-AES128-GCM-SHA256", "ECDHE-RSA-AES128-SHA", "ECDHE-RSA-AES256-SHA", "AES128-GCM-SHA256", "AES128-SHA", "AES256-SHA", "DES-CBC3-SHA", "RC4-SHA" });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */     DEFAULT_CIPHERS = Collections.unmodifiableList(ciphers);
/*     */     
/*  74 */     if (logger.isDebugEnabled()) {
/*  75 */       logger.debug("Default cipher suite (OpenSSL): " + ciphers);
/*     */     }
/*     */     
/*  78 */     AtomicIntegerFieldUpdater<OpenSslContext> updater = PlatformDependent.newAtomicIntegerFieldUpdater(OpenSslContext.class, "aprPoolDestroyed");
/*     */     
/*  80 */     if (updater == null) {
/*  81 */       updater = AtomicIntegerFieldUpdater.newUpdater(OpenSslContext.class, "aprPoolDestroyed");
/*     */     }
/*  83 */     DESTROY_UPDATER = updater;
/*     */   }
/*     */   
/*     */   OpenSslContext(Iterable<String> ciphers, ApplicationProtocolConfig apnCfg, long sessionCacheSize, long sessionTimeout, int mode) throws SSLException
/*     */   {
/*  88 */     this(ciphers, toNegotiator(apnCfg, mode == 1), sessionCacheSize, sessionTimeout, mode);
/*     */   }
/*     */   
/*     */   OpenSslContext(Iterable<String> ciphers, OpenSslApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout, int mode) throws SSLException
/*     */   {
/*  93 */     OpenSsl.ensureAvailability();
/*     */     
/*  95 */     if ((mode != 1) && (mode != 0)) {
/*  96 */       throw new IllegalArgumentException("mode most be either SSL.SSL_MODE_SERVER or SSL.SSL_MODE_CLIENT");
/*     */     }
/*  98 */     this.mode = mode;
/*     */     
/* 100 */     if (ciphers == null) {
/* 101 */       ciphers = DEFAULT_CIPHERS;
/*     */     }
/*     */     
/* 104 */     for (String c : ciphers) {
/* 105 */       if (c == null) {
/*     */         break;
/*     */       }
/*     */       
/* 109 */       String converted = CipherSuiteConverter.toOpenSsl(c);
/* 110 */       if (converted != null) {
/* 111 */         c = converted;
/*     */       }
/*     */       
/* 114 */       this.ciphers.add(c);
/*     */     }
/*     */     
/* 117 */     this.apn = ((OpenSslApplicationProtocolNegotiator)ObjectUtil.checkNotNull(apn, "apn"));
/*     */     
/*     */ 
/* 120 */     this.aprPool = Pool.create(0L);
/*     */     
/*     */ 
/* 123 */     boolean success = false;
/*     */     try {
/* 125 */       synchronized (OpenSslContext.class) {
/*     */         try {
/* 127 */           this.ctx = SSLContext.make(this.aprPool, 28, mode);
/*     */         } catch (Exception e) {
/* 129 */           throw new SSLException("failed to create an SSL_CTX", e);
/*     */         }
/*     */         
/* 132 */         SSLContext.setOptions(this.ctx, 4095);
/* 133 */         SSLContext.setOptions(this.ctx, 16777216);
/* 134 */         SSLContext.setOptions(this.ctx, 33554432);
/* 135 */         SSLContext.setOptions(this.ctx, 4194304);
/* 136 */         SSLContext.setOptions(this.ctx, 524288);
/* 137 */         SSLContext.setOptions(this.ctx, 1048576);
/* 138 */         SSLContext.setOptions(this.ctx, 65536);
/*     */         
/*     */         try
/*     */         {
/* 142 */           SSLContext.setCipherSuite(this.ctx, CipherSuiteConverter.toOpenSsl(this.ciphers));
/*     */         } catch (SSLException e) {
/* 144 */           throw e;
/*     */         } catch (Exception e) {
/* 146 */           throw new SSLException("failed to set cipher suite: " + this.ciphers, e);
/*     */         }
/*     */         
/* 149 */         List<String> nextProtoList = apn.protocols();
/*     */         
/* 151 */         if (!nextProtoList.isEmpty())
/*     */         {
/* 153 */           StringBuilder nextProtocolBuf = new StringBuilder();
/* 154 */           for (String p : nextProtoList) {
/* 155 */             nextProtocolBuf.append(p);
/* 156 */             nextProtocolBuf.append(',');
/*     */           }
/* 158 */           nextProtocolBuf.setLength(nextProtocolBuf.length() - 1);
/*     */           
/* 160 */           SSLContext.setNextProtos(this.ctx, nextProtocolBuf.toString());
/*     */         }
/*     */         
/*     */ 
/* 164 */         if (sessionCacheSize > 0L) {
/* 165 */           this.sessionCacheSize = sessionCacheSize;
/* 166 */           SSLContext.setSessionCacheSize(this.ctx, sessionCacheSize);
/*     */         }
/*     */         else {
/* 169 */           this.sessionCacheSize = (sessionCacheSize = SSLContext.setSessionCacheSize(this.ctx, 20480L));
/*     */           
/* 171 */           SSLContext.setSessionCacheSize(this.ctx, sessionCacheSize);
/*     */         }
/*     */         
/*     */ 
/* 175 */         if (sessionTimeout > 0L) {
/* 176 */           this.sessionTimeout = sessionTimeout;
/* 177 */           SSLContext.setSessionCacheTimeout(this.ctx, sessionTimeout);
/*     */         }
/*     */         else {
/* 180 */           this.sessionTimeout = (sessionTimeout = SSLContext.setSessionCacheTimeout(this.ctx, 300L));
/*     */           
/* 182 */           SSLContext.setSessionCacheTimeout(this.ctx, sessionTimeout);
/*     */         }
/*     */       }
/* 185 */       success = true;
/*     */     } finally {
/* 187 */       if (!success) {
/* 188 */         destroyPools();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public final List<String> cipherSuites()
/*     */   {
/* 195 */     return this.unmodifiableCiphers;
/*     */   }
/*     */   
/*     */   public final long sessionCacheSize()
/*     */   {
/* 200 */     return this.sessionCacheSize;
/*     */   }
/*     */   
/*     */   public final long sessionTimeout()
/*     */   {
/* 205 */     return this.sessionTimeout;
/*     */   }
/*     */   
/*     */   public ApplicationProtocolNegotiator applicationProtocolNegotiator()
/*     */   {
/* 210 */     return this.apn;
/*     */   }
/*     */   
/*     */   public final boolean isClient()
/*     */   {
/* 215 */     return this.mode == 0;
/*     */   }
/*     */   
/*     */   public final SSLEngine newEngine(ByteBufAllocator alloc, String peerHost, int peerPort)
/*     */   {
/* 220 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final SSLEngine newEngine(ByteBufAllocator alloc)
/*     */   {
/* 228 */     List<String> protos = applicationProtocolNegotiator().protocols();
/* 229 */     if (protos.isEmpty()) {
/* 230 */       return new OpenSslEngine(this.ctx, alloc, null, isClient(), sessionContext());
/*     */     }
/* 232 */     return new OpenSslEngine(this.ctx, alloc, (String)protos.get(protos.size() - 1), isClient(), sessionContext());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final long context()
/*     */   {
/* 240 */     return this.ctx;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final OpenSslSessionStats stats()
/*     */   {
/* 249 */     return sessionContext().stats();
/*     */   }
/*     */   
/*     */   protected final void finalize()
/*     */     throws Throwable
/*     */   {
/* 255 */     super.finalize();
/* 256 */     synchronized (OpenSslContext.class) {
/* 257 */       if (this.ctx != 0L) {
/* 258 */         SSLContext.free(this.ctx);
/*     */       }
/*     */     }
/*     */     
/* 262 */     destroyPools();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final void setTicketKeys(byte[] keys)
/*     */   {
/* 271 */     sessionContext().setTicketKeys(keys);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void destroyPools()
/*     */   {
/* 279 */     if ((this.aprPool != 0L) && (DESTROY_UPDATER.compareAndSet(this, 0, 1))) {
/* 280 */       Pool.destroy(this.aprPool);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static X509Certificate[] certificates(byte[][] chain) {
/* 285 */     X509Certificate[] peerCerts = new X509Certificate[chain.length];
/* 286 */     for (int i = 0; i < peerCerts.length; i++) {
/* 287 */       peerCerts[i] = new OpenSslX509Certificate(chain[i]);
/*     */     }
/* 289 */     return peerCerts;
/*     */   }
/*     */   
/*     */   protected static X509TrustManager chooseTrustManager(TrustManager[] managers) {
/* 293 */     for (TrustManager m : managers) {
/* 294 */       if ((m instanceof X509TrustManager)) {
/* 295 */         return (X509TrustManager)m;
/*     */       }
/*     */     }
/* 298 */     throw new IllegalStateException("no X509TrustManager found");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static OpenSslApplicationProtocolNegotiator toNegotiator(ApplicationProtocolConfig config, boolean isServer)
/*     */   {
/* 310 */     if (config == null) {
/* 311 */       return OpenSslDefaultApplicationProtocolNegotiator.INSTANCE;
/*     */     }
/*     */     
/* 314 */     switch (config.protocol()) {
/*     */     case NONE: 
/* 316 */       return OpenSslDefaultApplicationProtocolNegotiator.INSTANCE;
/*     */     case NPN: 
/* 318 */       if (isServer) {
/* 319 */         switch (config.selectedListenerFailureBehavior()) {
/*     */         case CHOOSE_MY_LAST_PROTOCOL: 
/* 321 */           return new OpenSslNpnApplicationProtocolNegotiator(config.supportedProtocols());
/*     */         }
/* 323 */         throw new UnsupportedOperationException("OpenSSL provider does not support " + config.selectedListenerFailureBehavior() + " behavior");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 329 */       throw new UnsupportedOperationException("OpenSSL provider does not support client mode");
/*     */     }
/*     */     
/* 332 */     throw new UnsupportedOperationException("OpenSSL provider does not support " + config.protocol() + " protocol");
/*     */   }
/*     */   
/*     */   public abstract OpenSslSessionContext sessionContext();
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\OpenSslContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */