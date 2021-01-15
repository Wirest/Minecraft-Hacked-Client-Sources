/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import java.io.File;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
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
/*     */ public final class JdkSslClientContext
/*     */   extends JdkSslContext
/*     */ {
/*     */   private final SSLContext ctx;
/*     */   
/*     */   public JdkSslClientContext()
/*     */     throws SSLException
/*     */   {
/*  40 */     this(null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkSslClientContext(File certChainFile)
/*     */     throws SSLException
/*     */   {
/*  50 */     this(certChainFile, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkSslClientContext(TrustManagerFactory trustManagerFactory)
/*     */     throws SSLException
/*     */   {
/*  61 */     this(null, trustManagerFactory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkSslClientContext(File certChainFile, TrustManagerFactory trustManagerFactory)
/*     */     throws SSLException
/*     */   {
/*  74 */     this(certChainFile, trustManagerFactory, null, IdentityCipherSuiteFilter.INSTANCE, JdkDefaultApplicationProtocolNegotiator.INSTANCE, 0L, 0L);
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
/*     */   public JdkSslClientContext(File certChainFile, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/*  99 */     this(certChainFile, trustManagerFactory, ciphers, cipherFilter, toNegotiator(apn, false), sessionCacheSize, sessionTimeout);
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
/*     */   public JdkSslClientContext(File certChainFile, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, JdkApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 124 */     this(certChainFile, trustManagerFactory, null, null, null, null, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
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
/*     */   public JdkSslClientContext(File trustCertChainFile, TrustManagerFactory trustManagerFactory, File keyCertChainFile, File keyFile, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 161 */     this(trustCertChainFile, trustManagerFactory, keyCertChainFile, keyFile, keyPassword, keyManagerFactory, ciphers, cipherFilter, toNegotiator(apn, false), sessionCacheSize, sessionTimeout);
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
/*     */   public JdkSslClientContext(File trustCertChainFile, TrustManagerFactory trustManagerFactory, File keyCertChainFile, File keyFile, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, JdkApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 198 */     super(ciphers, cipherFilter, apn);
/*     */     try
/*     */     {
/* 201 */       if (trustCertChainFile != null) {
/* 202 */         trustManagerFactory = buildTrustManagerFactory(trustCertChainFile, trustManagerFactory);
/*     */       }
/* 204 */       if (keyFile != null) {
/* 205 */         keyManagerFactory = buildKeyManagerFactory(keyCertChainFile, keyFile, keyPassword, keyManagerFactory);
/*     */       }
/* 207 */       this.ctx = SSLContext.getInstance("TLS");
/* 208 */       this.ctx.init(keyManagerFactory == null ? null : keyManagerFactory.getKeyManagers(), trustManagerFactory == null ? null : trustManagerFactory.getTrustManagers(), null);
/*     */       
/*     */ 
/*     */ 
/* 212 */       SSLSessionContext sessCtx = this.ctx.getClientSessionContext();
/* 213 */       if (sessionCacheSize > 0L) {
/* 214 */         sessCtx.setSessionCacheSize((int)Math.min(sessionCacheSize, 2147483647L));
/*     */       }
/* 216 */       if (sessionTimeout > 0L) {
/* 217 */         sessCtx.setSessionTimeout((int)Math.min(sessionTimeout, 2147483647L));
/*     */       }
/*     */     } catch (Exception e) {
/* 220 */       throw new SSLException("failed to initialize the client-side SSL context", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isClient()
/*     */   {
/* 226 */     return true;
/*     */   }
/*     */   
/*     */   public SSLContext context()
/*     */   {
/* 231 */     return this.ctx;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\JdkSslClientContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */