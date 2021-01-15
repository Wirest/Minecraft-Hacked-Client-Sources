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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JdkSslServerContext
/*     */   extends JdkSslContext
/*     */ {
/*     */   private final SSLContext ctx;
/*     */   
/*     */   public JdkSslServerContext(File certChainFile, File keyFile)
/*     */     throws SSLException
/*     */   {
/*  44 */     this(certChainFile, keyFile, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkSslServerContext(File certChainFile, File keyFile, String keyPassword)
/*     */     throws SSLException
/*     */   {
/*  56 */     this(certChainFile, keyFile, keyPassword, null, IdentityCipherSuiteFilter.INSTANCE, JdkDefaultApplicationProtocolNegotiator.INSTANCE, 0L, 0L);
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
/*     */   public JdkSslServerContext(File certChainFile, File keyFile, String keyPassword, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/*  80 */     this(certChainFile, keyFile, keyPassword, ciphers, cipherFilter, toNegotiator(apn, true), sessionCacheSize, sessionTimeout);
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
/*     */   public JdkSslServerContext(File certChainFile, File keyFile, String keyPassword, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, JdkApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 104 */     this(null, null, certChainFile, keyFile, keyPassword, null, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
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
/*     */   public JdkSslServerContext(File trustCertChainFile, TrustManagerFactory trustManagerFactory, File keyCertChainFile, File keyFile, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 138 */     this(trustCertChainFile, trustManagerFactory, keyCertChainFile, keyFile, keyPassword, keyManagerFactory, ciphers, cipherFilter, toNegotiator(apn, true), sessionCacheSize, sessionTimeout);
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
/*     */   public JdkSslServerContext(File trustCertChainFile, TrustManagerFactory trustManagerFactory, File keyCertChainFile, File keyFile, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, JdkApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 172 */     super(ciphers, cipherFilter, apn);
/* 173 */     if ((keyFile == null) && (keyManagerFactory == null)) {
/* 174 */       throw new NullPointerException("keyFile, keyManagerFactory");
/*     */     }
/*     */     try
/*     */     {
/* 178 */       if (trustCertChainFile != null) {
/* 179 */         trustManagerFactory = buildTrustManagerFactory(trustCertChainFile, trustManagerFactory);
/*     */       }
/* 181 */       if (keyFile != null) {
/* 182 */         keyManagerFactory = buildKeyManagerFactory(keyCertChainFile, keyFile, keyPassword, keyManagerFactory);
/*     */       }
/*     */       
/*     */ 
/* 186 */       this.ctx = SSLContext.getInstance("TLS");
/* 187 */       this.ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory == null ? null : trustManagerFactory.getTrustManagers(), null);
/*     */       
/*     */ 
/*     */ 
/* 191 */       SSLSessionContext sessCtx = this.ctx.getServerSessionContext();
/* 192 */       if (sessionCacheSize > 0L) {
/* 193 */         sessCtx.setSessionCacheSize((int)Math.min(sessionCacheSize, 2147483647L));
/*     */       }
/* 195 */       if (sessionTimeout > 0L) {
/* 196 */         sessCtx.setSessionTimeout((int)Math.min(sessionTimeout, 2147483647L));
/*     */       }
/*     */     } catch (Exception e) {
/* 199 */       throw new SSLException("failed to initialize the server-side SSL context", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isClient()
/*     */   {
/* 205 */     return false;
/*     */   }
/*     */   
/*     */   public SSLContext context()
/*     */   {
/* 210 */     return this.ctx;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\JdkSslServerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */