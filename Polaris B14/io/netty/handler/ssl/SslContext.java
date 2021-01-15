/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import java.util.List;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.EncryptedPrivateKeyInfo;
/*     */ import javax.crypto.NoSuchPaddingException;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.SecretKeyFactory;
/*     */ import javax.crypto.spec.PBEKeySpec;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLSessionContext;
/*     */ import javax.net.ssl.TrustManagerFactory;
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
/*     */ public abstract class SslContext
/*     */ {
/*     */   static final CertificateFactory X509_CERT_FACTORY;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  74 */       X509_CERT_FACTORY = CertificateFactory.getInstance("X.509");
/*     */     } catch (CertificateException e) {
/*  76 */       throw new IllegalStateException("unable to instance X.509 CertificateFactory", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SslProvider defaultServerProvider()
/*     */   {
/*  86 */     return defaultProvider();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SslProvider defaultClientProvider()
/*     */   {
/*  95 */     return defaultProvider();
/*     */   }
/*     */   
/*     */   private static SslProvider defaultProvider() {
/*  99 */     if (OpenSsl.isAvailable()) {
/* 100 */       return SslProvider.OPENSSL;
/*     */     }
/* 102 */     return SslProvider.JDK;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SslContext newServerContext(File certChainFile, File keyFile)
/*     */     throws SSLException
/*     */   {
/* 114 */     return newServerContext(certChainFile, keyFile, null);
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
/*     */   public static SslContext newServerContext(File certChainFile, File keyFile, String keyPassword)
/*     */     throws SSLException
/*     */   {
/* 128 */     return newServerContext(null, certChainFile, keyFile, keyPassword);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SslContext newServerContext(File certChainFile, File keyFile, String keyPassword, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 152 */     return newServerContext(null, certChainFile, keyFile, keyPassword, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
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
/*     */   public static SslContext newServerContext(SslProvider provider, File certChainFile, File keyFile)
/*     */     throws SSLException
/*     */   {
/* 168 */     return newServerContext(provider, certChainFile, keyFile, null);
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
/*     */   public static SslContext newServerContext(SslProvider provider, File certChainFile, File keyFile, String keyPassword)
/*     */     throws SSLException
/*     */   {
/* 184 */     return newServerContext(provider, certChainFile, keyFile, keyPassword, null, IdentityCipherSuiteFilter.INSTANCE, null, 0L, 0L);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SslContext newServerContext(SslProvider provider, File certChainFile, File keyFile, String keyPassword, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 212 */     return newServerContext(provider, null, null, certChainFile, keyFile, keyPassword, null, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
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
/*     */   public static SslContext newServerContext(SslProvider provider, File trustCertChainFile, TrustManagerFactory trustManagerFactory, File keyCertChainFile, File keyFile, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 253 */     if (provider == null) {
/* 254 */       provider = defaultServerProvider();
/*     */     }
/*     */     
/* 257 */     switch (provider) {
/*     */     case JDK: 
/* 259 */       return new JdkSslServerContext(trustCertChainFile, trustManagerFactory, keyCertChainFile, keyFile, keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
/*     */     
/*     */ 
/*     */     case OPENSSL: 
/* 263 */       return new OpenSslServerContext(keyCertChainFile, keyFile, keyPassword, trustManagerFactory, ciphers, apn, sessionCacheSize, sessionTimeout);
/*     */     }
/*     */     
/*     */     
/* 267 */     throw new Error(provider.toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SslContext newClientContext()
/*     */     throws SSLException
/*     */   {
/* 277 */     return newClientContext(null, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SslContext newClientContext(File certChainFile)
/*     */     throws SSLException
/*     */   {
/* 288 */     return newClientContext(null, certChainFile);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SslContext newClientContext(TrustManagerFactory trustManagerFactory)
/*     */     throws SSLException
/*     */   {
/* 301 */     return newClientContext(null, null, trustManagerFactory);
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
/*     */   public static SslContext newClientContext(File certChainFile, TrustManagerFactory trustManagerFactory)
/*     */     throws SSLException
/*     */   {
/* 317 */     return newClientContext(null, certChainFile, trustManagerFactory);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SslContext newClientContext(File certChainFile, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 343 */     return newClientContext(null, certChainFile, trustManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
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
/*     */   public static SslContext newClientContext(SslProvider provider)
/*     */     throws SSLException
/*     */   {
/* 357 */     return newClientContext(provider, null, null);
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
/*     */   public static SslContext newClientContext(SslProvider provider, File certChainFile)
/*     */     throws SSLException
/*     */   {
/* 371 */     return newClientContext(provider, certChainFile, null);
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
/*     */   public static SslContext newClientContext(SslProvider provider, TrustManagerFactory trustManagerFactory)
/*     */     throws SSLException
/*     */   {
/* 387 */     return newClientContext(provider, null, trustManagerFactory);
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
/*     */   public static SslContext newClientContext(SslProvider provider, File certChainFile, TrustManagerFactory trustManagerFactory)
/*     */     throws SSLException
/*     */   {
/* 405 */     return newClientContext(provider, certChainFile, trustManagerFactory, null, IdentityCipherSuiteFilter.INSTANCE, null, 0L, 0L);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SslContext newClientContext(SslProvider provider, File certChainFile, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 434 */     return newClientContext(provider, certChainFile, trustManagerFactory, null, null, null, null, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
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
/*     */ 
/*     */ 
/*     */   public static SslContext newClientContext(SslProvider provider, File trustCertChainFile, TrustManagerFactory trustManagerFactory, File keyCertChainFile, File keyFile, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 478 */     if (provider == null) {
/* 479 */       provider = defaultClientProvider();
/*     */     }
/* 481 */     switch (provider) {
/*     */     case JDK: 
/* 483 */       return new JdkSslClientContext(trustCertChainFile, trustManagerFactory, keyCertChainFile, keyFile, keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
/*     */     
/*     */ 
/*     */     case OPENSSL: 
/* 487 */       return new OpenSslClientContext(trustCertChainFile, trustManagerFactory, ciphers, apn, sessionCacheSize, sessionTimeout);
/*     */     }
/*     */     
/*     */     
/* 491 */     throw new Error();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isServer()
/*     */   {
/* 500 */     return !isClient();
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
/*     */   public final SslHandler newHandler(ByteBufAllocator alloc)
/*     */   {
/* 556 */     return newHandler(newEngine(alloc));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final SslHandler newHandler(ByteBufAllocator alloc, String peerHost, int peerPort)
/*     */   {
/* 568 */     return newHandler(newEngine(alloc, peerHost, peerPort));
/*     */   }
/*     */   
/*     */   private static SslHandler newHandler(SSLEngine engine) {
/* 572 */     return new SslHandler(engine);
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
/*     */ 
/*     */ 
/*     */   protected static PKCS8EncodedKeySpec generateKeySpec(char[] password, byte[] key)
/*     */     throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException
/*     */   {
/* 595 */     if ((password == null) || (password.length == 0)) {
/* 596 */       return new PKCS8EncodedKeySpec(key);
/*     */     }
/*     */     
/* 599 */     EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(key);
/* 600 */     SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encryptedPrivateKeyInfo.getAlgName());
/* 601 */     PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
/* 602 */     SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
/*     */     
/* 604 */     Cipher cipher = Cipher.getInstance(encryptedPrivateKeyInfo.getAlgName());
/* 605 */     cipher.init(2, pbeKey, encryptedPrivateKeyInfo.getAlgParameters());
/*     */     
/* 607 */     return encryptedPrivateKeyInfo.getKeySpec(cipher);
/*     */   }
/*     */   
/*     */   public abstract boolean isClient();
/*     */   
/*     */   public abstract List<String> cipherSuites();
/*     */   
/*     */   public abstract long sessionCacheSize();
/*     */   
/*     */   public abstract long sessionTimeout();
/*     */   
/*     */   public abstract ApplicationProtocolNegotiator applicationProtocolNegotiator();
/*     */   
/*     */   public abstract SSLEngine newEngine(ByteBufAllocator paramByteBufAllocator);
/*     */   
/*     */   public abstract SSLEngine newEngine(ByteBufAllocator paramByteBufAllocator, String paramString, int paramInt);
/*     */   
/*     */   public abstract SSLSessionContext sessionContext();
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\SslContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */