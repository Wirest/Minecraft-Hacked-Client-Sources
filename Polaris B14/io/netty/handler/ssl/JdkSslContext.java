/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.KeyException;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.Security;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.crypto.NoSuchPaddingException;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLSessionContext;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.security.auth.x500.X500Principal;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JdkSslContext
/*     */   extends SslContext
/*     */ {
/*  64 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(JdkSslContext.class);
/*     */   static final String PROTOCOL = "TLS";
/*     */   static final String[] PROTOCOLS;
/*     */   static final List<String> DEFAULT_CIPHERS;
/*     */   static final Set<String> SUPPORTED_CIPHERS;
/*     */   private final String[] cipherSuites;
/*     */   private final List<String> unmodifiableCipherSuites;
/*     */   private final JdkApplicationProtocolNegotiator apn;
/*     */   
/*     */   static {
/*     */     SSLContext context;
/*  75 */     try { context = SSLContext.getInstance("TLS");
/*  76 */       context.init(null, null, null);
/*     */     } catch (Exception e) {
/*  78 */       throw new Error("failed to initialize the default SSL context", e);
/*     */     }
/*     */     
/*  81 */     SSLEngine engine = context.createSSLEngine();
/*     */     
/*     */ 
/*  84 */     String[] supportedProtocols = engine.getSupportedProtocols();
/*  85 */     Set<String> supportedProtocolsSet = new HashSet(supportedProtocols.length);
/*  86 */     for (int i = 0; i < supportedProtocols.length; i++) {
/*  87 */       supportedProtocolsSet.add(supportedProtocols[i]);
/*     */     }
/*  89 */     List<String> protocols = new ArrayList();
/*  90 */     addIfSupported(supportedProtocolsSet, protocols, new String[] { "TLSv1.2", "TLSv1.1", "TLSv1" });
/*     */     
/*     */ 
/*     */ 
/*  94 */     if (!protocols.isEmpty()) {
/*  95 */       PROTOCOLS = (String[])protocols.toArray(new String[protocols.size()]);
/*     */     } else {
/*  97 */       PROTOCOLS = engine.getEnabledProtocols();
/*     */     }
/*     */     
/*     */ 
/* 101 */     String[] supportedCiphers = engine.getSupportedCipherSuites();
/* 102 */     SUPPORTED_CIPHERS = new HashSet(supportedCiphers.length);
/* 103 */     for (i = 0; i < supportedCiphers.length; i++) {
/* 104 */       SUPPORTED_CIPHERS.add(supportedCiphers[i]);
/*     */     }
/* 106 */     List<String> ciphers = new ArrayList();
/* 107 */     addIfSupported(SUPPORTED_CIPHERS, ciphers, new String[] { "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", "TLS_RSA_WITH_AES_128_GCM_SHA256", "TLS_RSA_WITH_AES_128_CBC_SHA", "TLS_RSA_WITH_AES_256_CBC_SHA", "SSL_RSA_WITH_3DES_EDE_CBC_SHA", "SSL_RSA_WITH_RC4_128_SHA" });
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
/* 123 */     if (!ciphers.isEmpty()) {
/* 124 */       DEFAULT_CIPHERS = Collections.unmodifiableList(ciphers);
/*     */     }
/*     */     else {
/* 127 */       DEFAULT_CIPHERS = Collections.unmodifiableList(Arrays.asList(engine.getEnabledCipherSuites()));
/*     */     }
/*     */     
/* 130 */     if (logger.isDebugEnabled()) {
/* 131 */       logger.debug("Default protocols (JDK): {} ", Arrays.asList(PROTOCOLS));
/* 132 */       logger.debug("Default cipher suites (JDK): {}", DEFAULT_CIPHERS);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void addIfSupported(Set<String> supported, List<String> enabled, String... names) {
/* 137 */     for (String n : names) {
/* 138 */       if (supported.contains(n)) {
/* 139 */         enabled.add(n);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   JdkSslContext(Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig config, boolean isServer)
/*     */   {
/* 150 */     this(ciphers, cipherFilter, toNegotiator(config, isServer));
/*     */   }
/*     */   
/*     */   JdkSslContext(Iterable<String> ciphers, CipherSuiteFilter cipherFilter, JdkApplicationProtocolNegotiator apn) {
/* 154 */     this.apn = ((JdkApplicationProtocolNegotiator)ObjectUtil.checkNotNull(apn, "apn"));
/* 155 */     this.cipherSuites = ((CipherSuiteFilter)ObjectUtil.checkNotNull(cipherFilter, "cipherFilter")).filterCipherSuites(ciphers, DEFAULT_CIPHERS, SUPPORTED_CIPHERS);
/*     */     
/* 157 */     this.unmodifiableCipherSuites = Collections.unmodifiableList(Arrays.asList(this.cipherSuites));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final SSLSessionContext sessionContext()
/*     */   {
/* 169 */     if (isServer()) {
/* 170 */       return context().getServerSessionContext();
/*     */     }
/* 172 */     return context().getClientSessionContext();
/*     */   }
/*     */   
/*     */ 
/*     */   public final List<String> cipherSuites()
/*     */   {
/* 178 */     return this.unmodifiableCipherSuites;
/*     */   }
/*     */   
/*     */   public final long sessionCacheSize()
/*     */   {
/* 183 */     return sessionContext().getSessionCacheSize();
/*     */   }
/*     */   
/*     */   public final long sessionTimeout()
/*     */   {
/* 188 */     return sessionContext().getSessionTimeout();
/*     */   }
/*     */   
/*     */   public final SSLEngine newEngine(ByteBufAllocator alloc)
/*     */   {
/* 193 */     SSLEngine engine = context().createSSLEngine();
/* 194 */     engine.setEnabledCipherSuites(this.cipherSuites);
/* 195 */     engine.setEnabledProtocols(PROTOCOLS);
/* 196 */     engine.setUseClientMode(isClient());
/* 197 */     return wrapEngine(engine);
/*     */   }
/*     */   
/*     */   public final SSLEngine newEngine(ByteBufAllocator alloc, String peerHost, int peerPort)
/*     */   {
/* 202 */     SSLEngine engine = context().createSSLEngine(peerHost, peerPort);
/* 203 */     engine.setEnabledCipherSuites(this.cipherSuites);
/* 204 */     engine.setEnabledProtocols(PROTOCOLS);
/* 205 */     engine.setUseClientMode(isClient());
/* 206 */     return wrapEngine(engine);
/*     */   }
/*     */   
/*     */   private SSLEngine wrapEngine(SSLEngine engine) {
/* 210 */     return this.apn.wrapperFactory().wrapSslEngine(engine, this.apn, isServer());
/*     */   }
/*     */   
/*     */   public JdkApplicationProtocolNegotiator applicationProtocolNegotiator()
/*     */   {
/* 215 */     return this.apn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static JdkApplicationProtocolNegotiator toNegotiator(ApplicationProtocolConfig config, boolean isServer)
/*     */   {
/* 225 */     if (config == null) {
/* 226 */       return JdkDefaultApplicationProtocolNegotiator.INSTANCE;
/*     */     }
/*     */     
/* 229 */     switch (config.protocol()) {
/*     */     case NONE: 
/* 231 */       return JdkDefaultApplicationProtocolNegotiator.INSTANCE;
/*     */     case ALPN: 
/* 233 */       if (isServer) {
/* 234 */         switch (config.selectorFailureBehavior()) {
/*     */         case FATAL_ALERT: 
/* 236 */           return new JdkAlpnApplicationProtocolNegotiator(true, config.supportedProtocols());
/*     */         case NO_ADVERTISE: 
/* 238 */           return new JdkAlpnApplicationProtocolNegotiator(false, config.supportedProtocols());
/*     */         }
/* 240 */         throw new UnsupportedOperationException("JDK provider does not support " + config.selectorFailureBehavior() + " failure behavior");
/*     */       }
/*     */       
/*     */ 
/* 244 */       switch (config.selectedListenerFailureBehavior()) {
/*     */       case ACCEPT: 
/* 246 */         return new JdkAlpnApplicationProtocolNegotiator(false, config.supportedProtocols());
/*     */       case FATAL_ALERT: 
/* 248 */         return new JdkAlpnApplicationProtocolNegotiator(true, config.supportedProtocols());
/*     */       }
/* 250 */       throw new UnsupportedOperationException("JDK provider does not support " + config.selectedListenerFailureBehavior() + " failure behavior");
/*     */     
/*     */ 
/*     */ 
/*     */     case NPN: 
/* 255 */       if (isServer) {
/* 256 */         switch (config.selectedListenerFailureBehavior()) {
/*     */         case ACCEPT: 
/* 258 */           return new JdkNpnApplicationProtocolNegotiator(false, config.supportedProtocols());
/*     */         case FATAL_ALERT: 
/* 260 */           return new JdkNpnApplicationProtocolNegotiator(true, config.supportedProtocols());
/*     */         }
/* 262 */         throw new UnsupportedOperationException("JDK provider does not support " + config.selectedListenerFailureBehavior() + " failure behavior");
/*     */       }
/*     */       
/*     */ 
/* 266 */       switch (config.selectorFailureBehavior()) {
/*     */       case FATAL_ALERT: 
/* 268 */         return new JdkNpnApplicationProtocolNegotiator(true, config.supportedProtocols());
/*     */       case NO_ADVERTISE: 
/* 270 */         return new JdkNpnApplicationProtocolNegotiator(false, config.supportedProtocols());
/*     */       }
/* 272 */       throw new UnsupportedOperationException("JDK provider does not support " + config.selectorFailureBehavior() + " failure behavior");
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 277 */     throw new UnsupportedOperationException("JDK provider does not support " + config.protocol() + " protocol");
/*     */   }
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
/*     */   protected static KeyManagerFactory buildKeyManagerFactory(File certChainFile, File keyFile, String keyPassword, KeyManagerFactory kmf)
/*     */     throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, CertificateException, KeyException, IOException
/*     */   {
/* 296 */     String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
/* 297 */     if (algorithm == null) {
/* 298 */       algorithm = "SunX509";
/*     */     }
/* 300 */     return buildKeyManagerFactory(certChainFile, algorithm, keyFile, keyPassword, kmf);
/*     */   }
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
/*     */   protected static KeyManagerFactory buildKeyManagerFactory(File certChainFile, String keyAlgorithm, File keyFile, String keyPassword, KeyManagerFactory kmf)
/*     */     throws KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IOException, CertificateException, KeyException, UnrecoverableKeyException
/*     */   {
/* 321 */     KeyStore ks = KeyStore.getInstance("JKS");
/* 322 */     ks.load(null, null);
/* 323 */     CertificateFactory cf = CertificateFactory.getInstance("X.509");
/* 324 */     KeyFactory rsaKF = KeyFactory.getInstance("RSA");
/* 325 */     KeyFactory dsaKF = KeyFactory.getInstance("DSA");
/*     */     
/* 327 */     ByteBuf encodedKeyBuf = PemReader.readPrivateKey(keyFile);
/* 328 */     byte[] encodedKey = new byte[encodedKeyBuf.readableBytes()];
/* 329 */     encodedKeyBuf.readBytes(encodedKey).release();
/*     */     
/* 331 */     char[] keyPasswordChars = keyPassword == null ? EmptyArrays.EMPTY_CHARS : keyPassword.toCharArray();
/* 332 */     PKCS8EncodedKeySpec encodedKeySpec = generateKeySpec(keyPasswordChars, encodedKey);
/*     */     PrivateKey key;
/*     */     try
/*     */     {
/* 336 */       key = rsaKF.generatePrivate(encodedKeySpec);
/*     */     } catch (InvalidKeySpecException ignore) {
/* 338 */       key = dsaKF.generatePrivate(encodedKeySpec);
/*     */     }
/*     */     
/* 341 */     List<Certificate> certChain = new ArrayList();
/* 342 */     ByteBuf[] certs = PemReader.readCertificates(certChainFile);
/*     */     try {
/* 344 */       for (ByteBuf buf : certs)
/* 345 */         certChain.add(cf.generateCertificate(new ByteBufInputStream(buf))); } finally { ByteBuf[] arr$;
/*     */       int len$;
/*     */       int i$;
/* 348 */       ByteBuf buf; for (ByteBuf buf : certs) {
/* 349 */         buf.release();
/*     */       }
/*     */     }
/*     */     
/* 353 */     ks.setKeyEntry("key", key, keyPasswordChars, (Certificate[])certChain.toArray(new Certificate[certChain.size()]));
/*     */     
/*     */ 
/* 356 */     if (kmf == null) {
/* 357 */       kmf = KeyManagerFactory.getInstance(keyAlgorithm);
/*     */     }
/* 359 */     kmf.init(ks, keyPasswordChars);
/*     */     
/* 361 */     return kmf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static TrustManagerFactory buildTrustManagerFactory(File certChainFile, TrustManagerFactory trustManagerFactory)
/*     */     throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException
/*     */   {
/* 373 */     KeyStore ks = KeyStore.getInstance("JKS");
/* 374 */     ks.load(null, null);
/* 375 */     CertificateFactory cf = CertificateFactory.getInstance("X.509");
/*     */     
/* 377 */     ByteBuf[] certs = PemReader.readCertificates(certChainFile);
/*     */     try {
/* 379 */       for (ByteBuf buf : certs) {
/* 380 */         X509Certificate cert = (X509Certificate)cf.generateCertificate(new ByteBufInputStream(buf));
/* 381 */         X500Principal principal = cert.getSubjectX500Principal();
/* 382 */         ks.setCertificateEntry(principal.getName("RFC2253"), cert); } } finally { ByteBuf[] arr$;
/*     */       int len$;
/*     */       int i$;
/* 385 */       ByteBuf buf; for (ByteBuf buf : certs) {
/* 386 */         buf.release();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 391 */     if (trustManagerFactory == null) {
/* 392 */       trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/*     */     }
/* 394 */     trustManagerFactory.init(ks);
/*     */     
/* 396 */     return trustManagerFactory;
/*     */   }
/*     */   
/*     */   public abstract SSLContext context();
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\JdkSslContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */